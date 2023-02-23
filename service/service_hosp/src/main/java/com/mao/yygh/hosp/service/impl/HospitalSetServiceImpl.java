package com.mao.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mao.yygh.hosp.mapper.HospitalSetMapper;
import com.mao.yygh.model.hosp.HospitalSet;
import com.mao.yygh.hosp.service.HospitalSetService;
import com.mao.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.stereotype.Service;

/**
 * @author MaoJY
 * @create 2023-02-02 19:13
 * @Description:
 */
@Service("hospitalSetService")
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {


    @Override
    public Page<HospitalSet> findPageHospSet(long current, long limit, HospitalSetQueryVo hospitalSetQueryVo) {
        //创建page对象，传递当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current,limit);
        //构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        if (hospitalSetQueryVo!=null) {
            String hosname = hospitalSetQueryVo.getHosname();
            String hoscode = hospitalSetQueryVo.getHoscode();
            if(!StringUtils.isBlank(hosname)) {
                wrapper.like("hosname",hospitalSetQueryVo.getHosname());
            }
            if(!StringUtils.isBlank(hoscode)) {
                wrapper.eq("hoscode",hospitalSetQueryVo.getHoscode());
            }
        }
        //调用方法实现分页查询
        return this.page(page, wrapper);
    }
    /**
    * Description:
    * date: 2023/2/23 10:10
    * @author: MaoJY
    * @since JDK 1.8
    */
    @Override
    public String getSignkey(String hoscode) {
        HospitalSet one = baseMapper.selectOne(new QueryWrapper<HospitalSet>().eq("hoscode", hoscode));
        return one==null?null:one.getSignKey();
    }
}