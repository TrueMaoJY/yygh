package com.mao.yygh.hosp.service.impl;/**
 * com.mao.yygh.hosp.service.impl
 *
 * @author MaoJY
 * @create 2023-02-22-16-29
 */

import com.alibaba.fastjson.JSONObject;
import com.mao.yygh.hosp.repository.HospitalRepository;
import com.mao.yygh.hosp.service.HospitalService;
import com.mao.yygh.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 *@ClassName HospitalServiceImpl
 *@Description TODO
 *@Author MAOjy
 *@DATE 2023/2/22 16:29
 *@Version 1.0
 */
@Service("hospitalService")
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;
    @Override
    public void save(Map<String, Object> map) {
        Hospital hospital = JSONObject.parseObject(JSONObject.toJSONString(map), Hospital.class);
        Hospital target=hospitalRepository.getHospitalByHoscode(hospital.getHoscode());
        //已存在，把已有的值复制过来
        if (target != null) {
            hospital.setStatus(target.getStatus());
            hospital.setCreateTime(target.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospital.setId(target.getId());
            hospitalRepository.save(hospital);
        }else{
            //不存在，新生成一些值
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }
}