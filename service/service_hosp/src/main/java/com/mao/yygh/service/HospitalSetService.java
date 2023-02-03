package com.mao.yygh.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mao.yygh.model.hosp.HospitalSet;
import com.mao.yygh.vo.hosp.HospitalSetQueryVo;

/**
 * @author MaoJY
 * @create 2023-02-02 19:13
 * @Description:
 */
public interface HospitalSetService extends IService<HospitalSet> {
    /**
     *
     * @param current
     * @param limit
     * @param hospitalSetQueryVo
     * @return
     */
    Page<HospitalSet> findPageHospSet(long current, long limit, HospitalSetQueryVo hospitalSetQueryVo);
}
