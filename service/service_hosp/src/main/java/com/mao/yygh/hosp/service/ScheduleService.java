package com.mao.yygh.hosp.service;

import com.mao.yygh.model.hosp.Schedule;
import com.mao.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

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
}
