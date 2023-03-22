package com.mao.yygh.hosp.controller;

import com.mao.yygh.common.result.Result;
import com.mao.yygh.hosp.service.HospitalService;
import com.mao.yygh.model.hosp.Hospital;
import com.mao.yygh.model.hosp.HospitalSet;
import com.mao.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName HospitalController
 * @Description TODO
 * @Author MAOjy
 * @DATE 2023/3/15 15:01
 * @Version 1.0
 */
@RestController
@Api(tags = "医院管理接口")
@RequestMapping("/admin/hosp/hospital")
@CrossOrigin
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    @GetMapping("list/{page}/{limit}")
    @ApiOperation(value = "获取分页列表")
    public Result index(@PathVariable Integer page,
                        @PathVariable Integer limit, HospitalQueryVo hospitalQueryVo){
        return  Result.ok(hospitalService.selectPage(page,limit,hospitalQueryVo));
    }

    @ApiOperation(value = "医院上下线")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable("id") String id,
                                  @PathVariable("status") Integer status) {
        //根据id查询医院设置信息
        hospitalService.updateStatus(id,status);
        return Result.ok();
    }

    @ApiOperation(value = "医院详情信息")
    @GetMapping("show/{id}")
    public Result show(@PathVariable String id) {
        //根据id查询医院设置信息
        return Result.ok(hospitalService.show(id));
    }
}