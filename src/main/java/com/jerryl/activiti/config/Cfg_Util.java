package com.jerryl.activiti.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jerryl.activiti.vo.Printer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liuruijie on 2017/2/21.
 * 一些工具bean
 */
@Configuration
public class Cfg_Util {
    @Bean
    public Printer printer(){
        return  new Printer();
    }

    //jackson xml util
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
