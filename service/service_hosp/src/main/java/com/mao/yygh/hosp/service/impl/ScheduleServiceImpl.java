package com.mao.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mao.yygh.common.exception.YyghException;
import com.mao.yygh.common.result.ResultCodeEnum;
import com.mao.yygh.hosp.repository.ScheduleRepository;
import com.mao.yygh.hosp.service.ScheduleService;
import com.mao.yygh.model.hosp.Department;
import com.mao.yygh.model.hosp.Hospital;
import com.mao.yygh.model.hosp.Schedule;
import com.mao.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
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
}