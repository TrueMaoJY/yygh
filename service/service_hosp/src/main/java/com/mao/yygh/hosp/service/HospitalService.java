package com.mao.yygh.hosp.service;/**
 * com.mao.yygh.hosp.service
 *
 * @author MaoJY
 * @create 2023-02-22-16-28
 */

import com.mao.yygh.model.hosp.Hospital;
import com.mao.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 *@InterfaceName Hospital
 *@Description TODO
 *@Author MAOjy
 *@DATE 2023/2/22 16:28
 *@Version 1.0
 */
public interface HospitalService {
    /**
     * @author MaoJY
     * @description 根据map中的参数新增或修改对象
     * @date 16:09 2023/3/22
     * @param [map]
     * @return void
     */
    void save(Map<String, Object> map);
    /**
     * @author MaoJY
     * @description 根据hoscode值查询hospital
     * @date 16:10 2023/3/22
     * @param [hoscode]
     * @return com.mao.yygh.model.hosp.Hospital
     */
    Hospital getByHoscode(String hoscode);
    /**
     * @author MaoJY
     * @description 条件分页查询
     * @date 16:10 2023/3/22
     * @param [page, limit, hospitalQueryVo]
     * @return org.springframework.data.domain.Page<com.mao.yygh.model.hosp.Hospital>
     */
    Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);
    /**
     * @author MaoJY
     * @description   根据id更新状态
     * @date 16:11 2023/3/22
     * @param [id, status]
     * @return void
     */
    void updateStatus(String id, Integer status);
    /**
     * @author MaoJY
     * @description 根据id查询hospital
     * @date 16:11 2023/3/22
     * @param [id]
     * @return
     */
    Map<String,Object> show(String id);

}
