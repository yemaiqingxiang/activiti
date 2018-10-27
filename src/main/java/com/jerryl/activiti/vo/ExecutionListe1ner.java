package com.jerryl.activiti.vo;


import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import java.util.ArrayList;
import java.util.List;

public class ExecutionListe1ner implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) throws Exception {
        //设置办理人
        List<String> list = new ArrayList<String>();
        list.add("张三丰");
        list.add("张四丰");
        list.add("张五丰");
        execution.setVariable("users", list);
    }
}
