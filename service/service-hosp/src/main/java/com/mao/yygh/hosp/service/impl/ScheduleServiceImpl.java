package com.mao.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mao.yygh.common.exception.YyghException;
import com.mao.yygh.common.result.ResultCodeEnum;
import com.mao.yygh.hosp.repository.ScheduleRepository;
import com.mao.yygh.hosp.service.DepartmentService;
import com.mao.yygh.hosp.service.HospitalService;
import com.mao.yygh.hosp.service.ScheduleService;
import com.mao.yygh.model.hosp.Department;
import com.mao.yygh.model.hosp.Hospital;
import com.mao.yygh.model.hosp.Schedule;
import com.mao.yygh.vo.hosp.BookingScheduleRuleVo;
import com.mao.yygh.vo.hosp.ScheduleQueryVo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.ExecutableAggregationOperation;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ScheduleServiceImpl
 * @Description TODO
 * @Author MAOjy
 * @DATE 2023/3/15 10:10
 * @Version 1.0
 */
@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private DepartmentService departmentService;
    @Override
    public void save(Map<String, Object> map) {
        Schedule schedule = JSONObject.parseObject(JSONObject.toJSONString(map), Schedule.class);
       Schedule target= scheduleRepository.findScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(),schedule.getHosScheduleId());
       if(null==target){
           schedule.setCreateTime(new Date());
           schedule.setUpdateTime(new Date());
           schedule.setIsDeleted(0);
           scheduleRepository.save(schedule);
       }else {
           BeanUtils.copyProperties(schedule,target,Schedule.class);
           scheduleRepository.save(target);
       }
    }

    @Override
    public Page<Schedule> selectPage(Integer page, Integer limit, ScheduleQueryVo queryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(queryVo,schedule);
        schedule.setIsDeleted(0);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().
                withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        //创建实例
        Example<Schedule> e = Example.of(schedule, exampleMatcher);
        Page<Schedule> pages = scheduleRepository.findAll(e, pageable);
        return pages;
    }

    @Override
    public void removeSchedule(Map<String, Object> map) {
        String hoscode = (String)map.get("hoscode");
        if (StringUtils.isBlank(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        String depcode=(String)map.get("depcode");
        if (StringUtils.isBlank(depcode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        Schedule schedule= scheduleRepository.getScheduleByHoscodeAndDepcode(hoscode, depcode);
        if(schedule!=null) scheduleRepository.deleteById(schedule.getId());
    }

    @Override
    public Map<String, Object> getScheduleRule(Integer page, Integer limit, String hoscode, String depcode) {
        //1和2类似于写了一段sql语句
        //1.构造查询对象
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        //2.构造分组 聚合对象
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate").first("workDate")
                        .as("workDate")
                        //计算号源数量
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                //排序
                Aggregation.sort(Sort.Direction.ASC,"workDate"),
                //分页
                Aggregation.skip((page-1)*limit),
                Aggregation.limit(limit)
        );
        //调用方法最终查询
        AggregationResults<BookingScheduleRuleVo> aggregate = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> mappedResults = aggregate.getMappedResults();
//        System.out.println(mappedResults.size()); 分组数
        //分组查询的总记录数
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalAggResults = mongoTemplate.aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        //当前医院当前科室的所有排班记录数
        int total=totalAggResults.getMappedResults().size();

        //把日期和星期做匹配
        for (BookingScheduleRuleVo ruleVo : mappedResults) {
            Date workDate = ruleVo.getWorkDate();
          String dayOfWeek=this.getDayOfWeek(new DateTime(workDate));
          ruleVo.setDayOfWeek(dayOfWeek);

        }
        //封装数据 返回
        Map<String,Object> resultMap=new HashMap<>(24);
        resultMap.put("bookingScheduleRuleList",mappedResults);
        resultMap.put("total",total);
        //获取医院名称
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        if(hospital!=null){
            Map<String, String> baseMap = new HashMap<>(12);
            baseMap.put("hosname",hospital.getHosname());
            resultMap.put("baseMap",baseMap);
        }
        return resultMap;
    }

    @Override
    public List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate) {
       List<Schedule> result= scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode,depcode,new DateTime(workDate).toDate());
       result.stream().forEach(item->{
           this.packageSchedule(item);
       });
        return result;
    }

    private void packageSchedule(Schedule item) {
        Hospital hospital = hospitalService.getByHoscode(item.getHoscode());
        String hosname=hospital==null?"":hospital.getHosname();
        Department department = departmentService.getByHoscodeAndDepcode(item.getHoscode(), item.getDepcode());
        String depname=department==null?"":department.getDepname();
        item.getParam().put("hosname",hosname);
        item.getParam().put("depname",depname);
        item.getParam().put("dayOfWeek",this.getDayOfWeek(new DateTime(item.getWorkDate())));
    }

    //日期-星期转换
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;

    }
}