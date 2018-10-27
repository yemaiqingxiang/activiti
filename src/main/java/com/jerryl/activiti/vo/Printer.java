package com.jerryl.activiti.vo;

import groovy.transform.Canonical;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

public class Printer {

    public String printMessage(Object o) {
        System.out.println(o);
        return "hello";
    }

    public boolean isComplete(ActivityExecution execution){
        //完成会签的次数
        Integer completeCounter=(Integer)execution.getVariable("nrOfCompletedInstances");
        //会签总数
        Integer instanceOfNumbers=(Integer)execution.getVariable("nrOfInstances");
        System.out.println(completeCounter);
        System.out.println(instanceOfNumbers);
        //获得会签的数据
        System.out.println(execution.getActivity());
        return false;
    }
}
