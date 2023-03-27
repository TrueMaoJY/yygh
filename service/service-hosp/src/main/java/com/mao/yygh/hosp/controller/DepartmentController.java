package com.mao.yygh.hosp.controller;

import com.mao.yygh.common.result.Result;
import com.mao.yygh.hosp.service.DepartmentService;
import com.mao.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName DepartmentController
 * @Description TODO
 * @Author MaoJY
 * @DATE 2023/3/23 10:12
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/hosp/department")
//@CrossOrigin
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/getDepList/{hoscode}")
    @ApiOperation(value = "根据医院编号查找科室信息")
    public Result getDepList(@PathVariable ("hoscode")String hoscode){
       List<DepartmentVo> list= departmentService.findDepartmentTree(hoscode);
        return Result.ok(list);
    }

}