package com.mao.yygh.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName UserConfig
 * @Description TODO
 * @Author MaoJY
 * @DATE 2023/3/26 14:06
 * @Version 1.0
 */
@Configuration
@MapperScan("com.mao.yygh.user.mapper")
public class UserConfig {
}