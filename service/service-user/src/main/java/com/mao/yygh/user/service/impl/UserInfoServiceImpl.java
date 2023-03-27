package com.mao.yygh.user.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mao.yygh.common.exception.YyghException;
import com.mao.yygh.common.helper.JwtHelper;
import com.mao.yygh.common.result.ResultCodeEnum;
import com.mao.yygh.model.cmn.Dict;
import com.mao.yygh.model.user.UserInfo;
import com.mao.yygh.user.mapper.UserInfoMapper;
import com.mao.yygh.user.service.UserInfoService;
import com.mao.yygh.vo.user.LoginVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("userInfoService")
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public Map<String, Object> loginByPhone(LoginVo loginVo) {
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        //参数校验
        if(StringUtils.isEmpty(phone)){
            throw   new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        if(StringUtils.isEmpty(code)){
            throw   new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //校验验证码是否正确
        String targetCode = redisTemplate.opsForValue().get(phone);
        if(!code.equals(targetCode)) {
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

        //判断是否第一次登录，是，则自动注册，
        UserInfo user = baseMapper.selectOne(new QueryWrapper<UserInfo>().eq("phone", phone));
        if (user == null) {
            user=new UserInfo();
            user.setName(phone);
            user.setPhone(phone);
            user.setStatus(1);
            this.save(user);
        }
        //校验是否被禁用
        if(user.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        //TODO 记录登录
        //返回页面显示名称
        Map<String, Object> map = new HashMap<>();
        String name = user.getName();
        if(StringUtils.isEmpty(name)) {
            name = user.getNickName();
        }
        if(StringUtils.isEmpty(name)) {
            name = user.getPhone();
        }
        map.put("name", name);
        String token = JwtHelper.createToken(user.getId(), name);
        map.put("token", token);
        return map;
    }
}
