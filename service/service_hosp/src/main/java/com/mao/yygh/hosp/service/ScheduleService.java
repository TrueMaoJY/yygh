package com.mao.yygh.hosp.service;

import com.mao.yygh.model.hosp.Schedule;
import com.mao.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @InterfaceName ScheduleService
 * @Description TODO
 * @Author MAOjy
 * @DATE 2023/3/15 10:10
 * @Version 1.0
 */
public interface ScheduleService {
    void save(Map<String, Object> map);

    Page<Schedule> selectPage(Integer page, Integer limit, ScheduleQueryVo queryVo);

    void removeSchedule(Map<String, Object> map);
    /**
     * @author MaoJY
     * @description 根据医院编号和科室编号查询排班信息
     * @date 13:41 2023/3/23
     * @param [page, limit, hoscode, depcode]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> getScheduleRule(Integer page, Integer limit, String hoscode, String depcode);
    /**
     * @author MaoJY
     * @description 根据医院编号，部门编号，workDate查找所有排班
     * @date 12:29 2023/3/24
     * @param [hoscode, depcode, workDate]
     * @return java.util.List<com.mao.yygh.model.hosp.Schedule>
     */
    List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate);
}
