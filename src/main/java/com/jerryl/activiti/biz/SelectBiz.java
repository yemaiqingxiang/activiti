package com.jerryl.activiti.biz;


import com.jerryl.activiti.biz.basic.BasicBiz;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 运行查询
 */
@Service
public class SelectBiz extends BasicBiz {
    /**
     * 查询作为候选人的任务
     */
    public List<Task> candidate(String userID) {
        List<Task> task = taskService.createTaskQuery()
                .taskCandidateUser(userID)
                .orderByTaskPriority().desc()//优先级排序
                .orderByTaskCreateTime().desc()//按创建时间排序
                .list();//查询所拥有的个人&候选任务
        return task;
    }

    /**
     * 查询作为审批人的任务
     */
    public List<Task> assignee(String userID) {
        List<Task> task = taskService.createTaskQuery()
                .taskAssignee(userID)
                .orderByTaskPriority().desc()//优先级排序
                .orderByTaskCreateTime().desc()//按创建时间排序
                .list();
        return task;
    }

    /**
     * 查询实例任务
     */
    public List<Task> processInstance(String processInstanceId) {
        List<Task> task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .orderByTaskPriority().desc()//优先级排序
                .orderByTaskCreateTime().desc()//按创建时间排序
                .list();
        return task;
    }
/*****历史查询******/

    /**
     * 完成任务
     */
    public void complete(String taskId,Map<String, Object> variables) {
        taskService.complete(taskId,variables);
    }
}

