package com.mao.yygh.hosp.controller;

import com.mao.yygh.common.result.Result;
import com.mao.yygh.hosp.service.ScheduleService;
import com.mao.yygh.model.hosp.Schedule;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ScheduleController
 * @Description TODO
 * @Author MaoJY
 * @DATE 2023/3/23 13:33
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/hosp/schedule")
@CrossOrigin
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    @ApiOperation(value = "根据医院编号和部门编号分页查询排班信息")
    public Result getScheduleRule(@PathVariable("page") Integer page,
                                  @PathVariable("limit") Integer limit,
                                  @PathVariable("hoscode")String hoscode,
                                  @PathVariable("depcode") String depcode){
       Map<String,Object> map= scheduleService.getScheduleRule(page,limit,hoscode,depcode);
       return Result.ok(map);
    }
    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    @ApiOperation(value = "根据医院编号，部门编号，workDate查找所有排班")
    public Result getScheduleDetail(@PathVariable("hoscode")String hoscode,
                                    @PathVariable("depcode")String depcode,
                                    @PathVariable("workDate") String workDate){
        List<Schedule> list=scheduleService.getScheduleDetail(hoscode,depcode,workDate);
        return Result.ok(list);
    }
}