package com.mao.yygh.hosp.api;

import com.mao.yygh.common.result.Result;
import com.mao.yygh.hosp.service.DepartmentService;
import com.mao.yygh.hosp.service.HospitalService;
import com.mao.yygh.model.hosp.Hospital;
import com.mao.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName HospitalApiController
 * @Description TODO
 * @Author MaoJY
 * @DATE 2023/3/25 13:50
 * @Version 1.0
 */
@Api(tags = {"前台首页医院相关内容接口"})
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospitalApiController {
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private DepartmentService departmentService;
    @ApiOperation(value = "分页条件查询医院")
    @GetMapping("/findHospList/{page}/{limit}")
    public Result findHospList(@PathVariable Integer page,
                               @PathVariable Integer limit,
                               HospitalQueryVo hospitalQueryVo){
        return Result.ok(hospitalService.selectPage(page,limit,hospitalQueryVo));
    }

    @ApiOperation(value="根据医院名称模糊查询")
    @GetMapping("findByHosname/{hosname}")
    public Result findByHosname(@PathVariable String hosname){
        List<Hospital> list=hospitalService.findByHosname(hosname);
        return Result.ok(list);
    }
    @ApiOperation(value="根据hoscode获取医院预约挂号详情")
    @GetMapping("getBookingDetail/{hoscode}")
    public Result getBookingDetail(@PathVariable String hoscode){
        Map<String,Object> resultMap=hospitalService.bookingDetail(hoscode);
        return Result.ok(resultMap);
    }

    @ApiOperation(value = "查询当前医院的所有科室")
    @GetMapping("findDepartmentList/{hoscode}")
    public Result findDepartmentList(@PathVariable String hoscode){
        return Result.ok(departmentService.findDepartmentTree(hoscode));
    }
}