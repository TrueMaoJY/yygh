package com.mao.yygh.hosp.service;/**
 * com.mao.yygh.hosp.service
 *
 * @author MaoJY
 * @create 2023-02-22-16-28
 */

import com.mao.yygh.model.hosp.Hospital;

import java.util.Map;

/**
 *@InterfaceName Hospital
 *@Description TODO
 *@Author MAOjy
 *@DATE 2023/2/22 16:28
 *@Version 1.0
 */
public interface HospitalService {
    void save(Map<String, Object> map);

    Hospital getByHoscode(String hoscode);
}
