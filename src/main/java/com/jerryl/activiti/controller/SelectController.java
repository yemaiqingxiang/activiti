package com.jerryl.activiti.controller;


import com.jerryl.activiti.biz.SelectBiz;
import org.activiti.engine.task.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
public class SelectController {
    @Resource
    SelectBiz selectBiz;

    @GetMapping("candidate/{name}")
    public void candidate(@PathVariable("name") String name){
        List<Task> task =  selectBiz.candidate(name);
        task.forEach(System.out::println);
    }
}
