package com.mao.yygh.hosp.repository;

import com.mao.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @InterfaceName DepartmentRepository
 * @Description TODO
 * @Author MAOjy
 * @DATE 2023/2/23 13:30
 * @Version 1.0
 */
public interface DepartmentRepository extends MongoRepository<Department,String> {
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
    List<Department> findAllByHoscode(String hoscode);
    Department findDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
