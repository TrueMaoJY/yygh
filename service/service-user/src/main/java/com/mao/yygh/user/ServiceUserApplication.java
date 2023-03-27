package com.mao.yygh.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
/**
 * @author MaoJY
 * @description 用户服务类，注册，登录
 * @date 13:52 2023/3/26
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.mao")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.mao")
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }
}
