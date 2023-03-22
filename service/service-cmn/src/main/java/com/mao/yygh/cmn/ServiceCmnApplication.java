package com.mao.yygh.cmn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName ServiceCmnApplication
 * @Description TODO
 * @Author MAOjy
 * @DATE 2023/2/10 16:27
 * @Version 1.0
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.mao")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.mao")
public class ServiceCmnApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmnApplication.class, args);
    }

}