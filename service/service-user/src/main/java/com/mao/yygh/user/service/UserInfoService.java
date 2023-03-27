package com.mao.yygh.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mao.yygh.model.user.UserInfo;
import com.mao.yygh.vo.user.LoginVo;

import java.util.Map;

/**
 * @InterfaceName UserInfoService
 * @Description TODO
 * @Author MaoJY
 * @DATE 2023/3/26 13:58
 * @Version 1.0
 */
public interface UserInfoService  extends IService<UserInfo> {
    Map<String, Object> loginByPhone(LoginVo loginVo);

}
