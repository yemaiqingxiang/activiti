package com.jerryl.activiti.biz.basic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.engine.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


public class BasicBiz {
    @Resource
    public RepositoryService repositoryService;
    @Resource
    public ObjectMapper objectMapper;
    @Resource
    public RuntimeService runtimeService;
    @Resource
    public IdentityService identityService;
    @Resource
    public
    TaskService taskService;
    @Resource
    public HistoryService historyService;
}

