package com.mao.yygh.user.controller;

import com.mao.yygh.common.result.Result;
import com.mao.yygh.user.service.UserInfoService;
import com.mao.yygh.vo.user.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName UserInfoApiController
 * @Description TODO
 * @Author MaoJY
 * @DATE 2023/3/26 13:56
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户登录注册")
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/loginByPhone")
    @ApiOperation(value = "根据手机号进行登录")
    public Result loginByPhone(@RequestBody LoginVo loginVo){
        Map<String,Object> resultMap=userInfoService.loginByPhone(loginVo);
        return Result.ok(resultMap);
    }

}