package com.mao.yygh.hosp.repository;/**
 * com.mao.yygh.hosp.repository
 *
 * @author MaoJY
 * @create 2023-02-22-16-23
 */

import com.mao.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *@InterfaceName HospitalRepository
 *@Description TODO
 *@Author MAOjy
 *@DATE 2023/2/22 16:23
 *@Version 1.0
 */
public interface HospitalRepository extends MongoRepository<Hospital,String> {
    /**
     * @author MAOjy
     * @description 根据hoscode查询是否已经有hospital对象，
     * mongobd根据方法名自动对方法进行了实现
     * @date 17:06 2023/2/22
     * @param hoscode
     * @return com.mao.yygh.model.hosp.Hospital
     */
    Hospital getHospitalByHoscode(String hoscode);
}
