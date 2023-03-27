package com.mao.yygh.msm.service;

/**
 * @InterfaceName MsmService
 * @Description TODO
 * @Author MaoJY
 * @DATE 2023/3/26 16:35
 * @Version 1.0
 */

public interface MsmService {
    //发送手机验证码
    boolean send(String phone, String code);

}
