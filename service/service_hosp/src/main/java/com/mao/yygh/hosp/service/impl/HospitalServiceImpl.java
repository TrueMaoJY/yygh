package com.mao.yygh.hosp.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.mao.yygh.cmn_client.feign.DictFeignClient;
import com.mao.yygh.enums.DictEnum;
import com.mao.yygh.hosp.repository.HospitalRepository;
import com.mao.yygh.hosp.service.HospitalService;
import com.mao.yygh.model.hosp.Hospital;
import com.mao.yygh.model.hosp.Schedule;
import com.mao.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName HospitalServiceImpl
 * @Description TODO
 * @Author MAOjy
 * @DATE 2023/2/22 16:29
 * @Version 1.0
 */
@Service("hospitalService")
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public void save(Map<String, Object> map) {
        Hospital hospital = JSONObject.parseObject(JSONObject.toJSONString(map), Hospital.class);
        Hospital target = hospitalRepository.getHospitalByHoscode(hospital.getHoscode());
        //已存在，把已有的值复制过来
        if (target != null) {
            hospital.setStatus(target.getStatus());
            hospital.setCreateTime(target.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospital.setId(target.getId());
            hospitalRepository.save(hospital);
        } else {
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

    @Override
    public Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);
        hospital.setIsDeleted(0);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().
                withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        //创建实例
        Example<Hospital> e = Example.of(hospital, exampleMatcher);
        Page<Hospital> pages = hospitalRepository.findAll(e, pageable);
        pages.getContent().stream().forEach(item -> {
            this.packHospital(item);
        });
        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        if(status==1||status==0){
            Hospital hospital = hospitalRepository.findById(id).get();
            hospital.setStatus(status);
            hospital.setUpdateTime(new Date());
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Map<String,Object> show(String id) {
        HashMap<String, Object> result = new HashMap<>();
        Hospital hospital = this.packHospital(this.getById(id));
        result.put("hospital",hospital);
        //单独将bookingRule封装
        result.put("bookingRule",hospital.getBookingRule());
        hospital.setBookingRule(null);
        return  result;
    }

    private Hospital getById(String id) {
        return hospitalRepository.findById(id).get();
    }

    /**
     * @author MaoJY
     * @description hospital中 省市区只有一串数字，需要通过feign调用cmn模块中的接口得到实际
     * 数据并封装起来
     * @date 9:51 2023/3/16
     * @param [hospital]
     * @return com.mao.yygh.model.hosp.Hospital
     */
    private Hospital packHospital(Hospital hospital) {
        String hostypeString = dictFeignClient.getName(DictEnum.HOSTYPE.getDictCode(), hospital.getHostype());
        String provinceString = dictFeignClient.getName(hospital.getProvinceCode());
        String cityString = dictFeignClient.getName(hospital.getCityCode());
        String districtString = dictFeignClient.getName(hospital.getDistrictCode());
        //BaseMongoEntity对象有一个hashMap
        hospital.getParam().put("hostypeString", hostypeString);
        hospital.getParam().put("fullAddress", provinceString + cityString + districtString + hospital.getAddress());
        return hospital;
    }
}