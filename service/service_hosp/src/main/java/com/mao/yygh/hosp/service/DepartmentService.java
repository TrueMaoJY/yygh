package com.mao.yygh.hosp.service;

import com.mao.yygh.model.hosp.Department;
import com.mao.yygh.vo.hosp.DepartmentQueryVo;
import com.mao.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @InterfaceName DepartmentService
 * @Description TODO
 * @Author MAOjy
 * @DATE 2023/2/23 13:31
 * @Version 1.0
 */
public interface DepartmentService {
    /**
     * @author MaoJY
     * @description 增
     * @date 13:26 2023/3/23
     * @param [map]
     * @return void
     */
    void save(Map<String, Object> map);
    /**
     * @author MaoJY
     * @description 条件分页查询
     * @date 13:26 2023/3/23
     * @param [page, limit, departmentQueryVo]
     * @return org.springframework.data.domain.Page<com.mao.yygh.model.hosp.Department>
     */
    Page<Department> findDepartmentList(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo);
    /**
     * @author MaoJY
     * @description 删
     * @date 13:26 2023/3/23
     * @param [map]
     * @return void
     */
    void removeDepartment(Map<String, Object> map);
    /**
     * @author MaoJY
     * @description 根据医院编号查询科室及其子科室信息
     * @date 13:27 2023/3/23
     * @param [hoscode]
     * @return java.util.List<com.mao.yygh.vo.hosp.DepartmentVo>
     */
    List<DepartmentVo> findDepartmentTree(String hoscode);

    Department getByHoscodeAndDepcode(String hoscode, String depcode);
}
