package com.jerryl.activiti.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class TestController {
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    ObjectMapper objectMapper;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private IdentityService identityService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;

    /**
     * 查询该人下的所有任务
     */
    @GetMapping("queryTask/{name}")
    public Object queryTask(@PathVariable("name") String name) {
        //任务的办理人
        String assignee = name;

        //创建一个任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();
        //办理人的任务列表
        List<Task> list = taskQuery.taskAssignee(assignee)//指定办理人
                .list();
        List<String> collect = list.stream().map(s -> s.getId()).collect(Collectors.toList());
        //遍历任务列表
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                System.out.println("任务的办理人：" + task.getAssignee());
                System.out.println("任务的id：" + task.getId());
                System.out.println("任务的名称：" + task.getName());
            }
        }
        return collect;
    }

    @RequestMapping("open/{key}")
    public String open(@PathVariable("key") String key) {
        // 开始流程
        Map<String, Object> variables = new HashMap();//设置流程变量


        //设置候选人：上级领导审批

        List list = new ArrayList();
        new HashMap<>();
//        list.add(new HashMap("dfd", "dfsdf"));
        list.add("zzz");
        list.add("xxx");
        list.add("aaa");
        list.add("bbb");
        variables.put("users", list);

        ProcessInstance vacationInstance = runtimeService.startProcessInstanceByKey(key, variables);
//        runtimeService.startProcessInstanceById(key);
        return "ok";
    }


}
