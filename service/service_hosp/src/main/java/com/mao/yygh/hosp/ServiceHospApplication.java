package com.mao.yygh.hosp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author MaoJY
 * @create 2023-02-02 17:54
 * @Description:
 * @ComponentScan(basePackages = "com.mao") 需要service——util模块下的配置类，但该类所在包名称
 * 与本模块包名称不一致，需要设置包扫描路径
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.mao")
//@MapperScan(basePackages = "com.mao.yygh.hosp.mapper")
@ComponentScan(basePackages = "com.mao")
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class,args);
    }
}