package com.zby.fin.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.zby.fin","com.zby.common"})//能扫描所有项目的这个com.zby.fin包
public class ServiceCoreApplication {


    public static void main(String[] args) {

            ConfigurableApplicationContext application = SpringApplication.run(ServiceCoreApplication.class, args);

    }
}