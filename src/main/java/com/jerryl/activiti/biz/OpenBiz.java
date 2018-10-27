package com.jerryl.activiti.biz;


import com.jerryl.activiti.biz.basic.BasicBiz;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开启任务
 */
@Service
public class OpenBiz extends BasicBiz {
    /**
     * 开启任务设置候选人
     */
    public void candidate(String key, List<String> candidate, Map<String, Object> variables) {
        // 开始流程
        //设置候选人：上级领导审批
        variables.put("managerIds", candidate);
        ProcessInstance vacationInstance = runtimeService.startProcessInstanceByKey(key, variables);
    }
    /**
     * 开启任务会签
     */
    public void  jointly(String key, List<String> jointly, Map<String, Object> variables){
        variables.put("jointlys", jointly);
        ProcessInstance vacationInstance = runtimeService.startProcessInstanceByKey(key, variables);
    }
}
