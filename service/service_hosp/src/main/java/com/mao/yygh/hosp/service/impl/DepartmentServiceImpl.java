package com.mao.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mao.yygh.common.exception.YyghException;
import com.mao.yygh.common.result.ResultCodeEnum;
import com.mao.yygh.hosp.repository.DepartmentRepository;
import com.mao.yygh.hosp.service.DepartmentService;
import com.mao.yygh.model.hosp.Department;
import com.mao.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @ClassName DepartmentServiceImpl
 * @Description TODO
 * @Author MAOjy
 * @DATE 2023/2/23 13:32
 * @Version 1.0
 */
@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Override
    public void save(Map<String, Object> map) {
       Department department= JSONObject.parseObject(JSONObject.toJSONString(map), Department.class);
       //查询库中是否已有该对象
        Department target = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        if(target==null){
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }else{
            //把department中不为null的值复制到target中
            BeanUtils.copyProperties(department,target,Department.class);
            departmentRepository.save(target);
        }

    }
    /**
     * @author MAOjy
     * @description 分页条件查询
     * @date 15:41 2023/2/23
     * @param page
     * @param limit
     * @param departmentQueryVo
     * @return org.springframework.data.domain.Page<com.mao.yygh.model.hosp.Department>
     */
    @Override
    public Page<Department> findDepartmentList(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().
                withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        //创建实例
        Example<Department> e = Example.of(department, exampleMatcher);
        Page<Department> pages = departmentRepository.findAll(e, pageable);
        return pages;
    }

    @Override
    public void removeDepartment(Map<String, Object> map) {
        String hoscode = (String)map.get("hoscode");
        if (StringUtils.isBlank(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        String depcode=(String)map.get("depcode");
        if (StringUtils.isBlank(depcode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(department!=null) departmentRepository.deleteById(department.getId());
    }
}