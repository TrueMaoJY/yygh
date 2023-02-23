package com.mao.yygh.hosp.service;

import com.mao.yygh.model.hosp.Department;
import com.mao.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @InterfaceName DepartmentService
 * @Description TODO
 * @Author MAOjy
 * @DATE 2023/2/23 13:31
 * @Version 1.0
 */
public interface DepartmentService {
    void save(Map<String, Object> map);

    Page<Department> findDepartmentList(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo);

    void removeDepartment(Map<String, Object> map);
}
