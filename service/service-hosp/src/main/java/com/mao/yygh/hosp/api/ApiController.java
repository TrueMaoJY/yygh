package com.mao.yygh.hosp.api;/**
 * com.mao.yygh.hosp.api
 *
 * @author MaoJY
 * @create 2023-02-22-16-26
 */

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import com.mao.yygh.common.exception.YyghException;
import com.mao.yygh.common.helper.HttpRequestHelper;
import com.mao.yygh.common.result.Result;
import com.mao.yygh.common.result.ResultCodeEnum;
import com.mao.yygh.hosp.service.DepartmentService;
import com.mao.yygh.hosp.service.HospitalService;
import com.mao.yygh.hosp.service.HospitalSetService;
import com.mao.yygh.hosp.service.ScheduleService;
import com.mao.yygh.model.hosp.Department;
import com.mao.yygh.model.hosp.Hospital;
import com.mao.yygh.model.hosp.Schedule;
import com.mao.yygh.vo.hosp.DepartmentQueryVo;
import com.mao.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *@ClassName ApiController
 *@Description TODO
 *@Author MAOjy
 *@DATE 2023/2/22 16:26
 *@Version 1.0
 */
@RestController
@Api(tags = "医院管理API接口")
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalSetService hospitalSetService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ScheduleService scheduleService;
    /**
     * @author MAOjy
     * @description 查询医院
     * @date 13:19 2023/2/23
     * @param
     * @param request
     * @return com.mao.yygh.common.result.Result
     */
    @PostMapping("/hospital/show")
    @ApiOperation(value = "查询医院")
    public Result showHospital(HttpServletRequest request){
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        signKeyVerification(map);
        Hospital hospital=hospitalService.getByHoscode((String)map.get("hoscode"));
        return Result.ok(hospital);
    }
    /**
     * @author MAOjy
     * @description 上传医院信息
     * @date 13:08 2023/2/23
     * @param
     * @param request
     * @return com.mao.yygh.common.result.Result
     */
    @PostMapping("/saveHospital")
    @ApiOperation(value = "上传医院")
    public Result saveHospital(HttpServletRequest request){
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        //将base64传输过程中+变空格改回来
        String logoData = (String) map.get("logoData");
        logoData=logoData.replaceAll(" ","+");
        map.put("logoData",logoData);
        signKeyVerification(map);
        hospitalService.save(map);
        return Result.ok();
    }
    private void signKeyVerification(Map<String, Object> map){
        //进行签名校验
        String hoscodeDb = hospitalSetService.getSignkey((String) map.get("hoscode"));
        boolean signEquals = HttpRequestHelper.isSignEquals(map, hoscodeDb);
        if(!signEquals){
            throw  new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
    }
    @PostMapping("saveDepartment")
    @ApiOperation(value = "上传科室")
    public Result saveDepartment(HttpServletRequest request){
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        signKeyVerification(map);
        //签名校验
        departmentService.save(map);
        return  Result.ok();
    }
    @PostMapping("/department/list")
    @ApiOperation(value = "查询科室")
    public Result departmentList(HttpServletRequest request){
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        signKeyVerification(map);
        //page
        Integer page = Integer.parseInt((String) map.get("page"));
        page=page==null?1:page;
        //limit
        Integer limit = Integer.parseInt((String) map.get("limit"));
        limit =limit==null?10:limit;
        //条件查询对象的封装
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode((String) map.get("hoscode"));
        departmentQueryVo.setDepcode((String) map.get("depcode"));
        //分页查询
        Page<Department> list=departmentService.findDepartmentList(page,limit,departmentQueryVo);
        return Result.ok(list);
    }
    @PostMapping("/department/remove")
    @ApiOperation(value = "删除科室")
    public Result removeDepartment(HttpServletRequest request){
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        signKeyVerification(map);
        departmentService.removeDepartment(map);
        return Result.ok();
    }
    @PostMapping("saveSchedule")
    @ApiOperation(value = "上传排班")
    public Result saveSchedule(HttpServletRequest request){
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        signKeyVerification(map);
        //签名校验
        scheduleService.save(map);
        return  Result.ok();
    }
    @PostMapping("/schedule/list")
    @ApiOperation(value = "查询排班")
    public Result scheduleList(HttpServletRequest request){
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        signKeyVerification(map);
        //page
        Integer page = Integer.parseInt((String) map.get("page"));
        page=page==null?1:page;
        //limit
        Integer limit = Integer.parseInt((String) map.get("limit"));
        limit =limit==null?10:limit;
        //条件查询对象的封装
        ScheduleQueryVo queryVo = new ScheduleQueryVo();
        queryVo.setHoscode((String) map.get("hoscode"));
        queryVo.setDepcode((String) map.get("depcode"));
        //分页查询
        Page<Schedule> list=scheduleService.selectPage(page,limit,queryVo);
        return Result.ok(list);
    }
    @PostMapping("/schedule/remove")
    @ApiOperation(value = "删除排班")
    public Result removeSchedule(HttpServletRequest request){
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        signKeyVerification(map);
        scheduleService.removeSchedule(map);
        return Result.ok();
    }
}