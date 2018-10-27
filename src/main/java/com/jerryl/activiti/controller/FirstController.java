package com.jerryl.activiti.controller;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.task.Task;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/first")
public class FirstController {

    @Autowired
    RepositoryService repositoryService;
    @Autowired
    ManagementService managementService;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;
    @Autowired
    ProcessEngineFactoryBean processEngine;
    @Autowired
    HistoryService historyService;
    @Autowired
    TaskService taskService;

    /**
     * 读取带跟踪的图片
     */
    @RequestMapping(value = "/test/{id}")
    public void test(@PathVariable("id") String id, HttpServletResponse response) throws Exception {

        //获取历史流程实例
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(id).singleResult();
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());

        List<HistoricActivityInstance> h = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(id)
                .list();
        List<HistoricActivityInstance> highLightedActivitList = qu(h);
        //高亮环节id集合
        List<String> highLightedActivitis = new ArrayList<String>();
        //高亮线路id集合
        List<String> highLightedFlows = getHighLightedFlows(definitionEntity, highLightedActivitList);
        highLightedActivitis.forEach(System.out::println);
        for (HistoricActivityInstance tempActivity : highLightedActivitList) {
            String activityId = tempActivity.getActivityId();
            highLightedActivitis.add(activityId);
        }

        //中文显示的是口口口，设置字体就好了
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis, highLightedFlows, "宋体","宋体",null,null,0.8);
        //单独返回流程图，不高亮显示
//        InputStream imageStream = diagramGenerator.generatePngDiagram(bpmnModel);
        //输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    /**
     * 获取需要高亮的线
     *
     * @param processDefinitionEntity
     * @param historicActivityInstances
     * @return
     */
    private List<String> getHighLightedFlows(
            ProcessDefinitionEntity processDefinitionEntity,
            List<HistoricActivityInstance> historicActivityInstances) {
        List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
            ActivityImpl activityImpl = processDefinitionEntity
                    .findActivity(historicActivityInstances.get(i)
                            .getActivityId());// 得到节点定义的详细信息
            System.out.println(activityImpl);
            List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();// 用以保存后需开始时间相同的节点
            ActivityImpl sameActivityImpl1 = processDefinitionEntity
                    .findActivity(historicActivityInstances.get(i + 1)
                            .getActivityId());
            // 将后面第一个节点放在时间相同节点的集合里
            sameStartTimeNodes.add(sameActivityImpl1);
            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
                HistoricActivityInstance activityImpl1 = historicActivityInstances
                        .get(j);// 后续第一个节点
                HistoricActivityInstance activityImpl2 = historicActivityInstances
                        .get(j + 1);// 后续第二个节点
                if (activityImpl1.getStartTime().equals(
                        activityImpl2.getStartTime())) {
                    // 如果第一个节点和第二个节点开始时间相同保存
                    ActivityImpl sameActivityImpl2 = processDefinitionEntity
                            .findActivity(activityImpl2.getActivityId());
                    sameStartTimeNodes.add(sameActivityImpl2);
                } else {
                    // 有不相同跳出循环
                    break;
                }
            }
            List<PvmTransition> pvmTransitions = activityImpl
                    .getOutgoingTransitions();// 取出节点的所有出去的线
            for (PvmTransition pvmTransition : pvmTransitions) {
                // 对所有的线进行遍历
                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition
                        .getDestination();
                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
                    highFlows.add(pvmTransition.getId());
                }
            }
        }
        return highFlows;
    }
    private List<HistoricActivityInstance> qu(List<HistoricActivityInstance> one){
        String s = null;
        for (HistoricActivityInstance hai : one) {
            if (hai.getEndTime() == null) {
                s = hai.getActivityId();
            }
        }
        Date date = null;
        for (HistoricActivityInstance h : one) {
            Date d = h.getEndTime();
            String n = h.getActivityId();
            if (s.equals(n) && d != null) {
                date = h.getStartTime();
            }
        }

        List<HistoricActivityInstance> list = new ArrayList<>();
        for (HistoricActivityInstance h : one) {
            Date startTime = h.getStartTime();
            if (startTime.before(date)||startTime.equals(date)) {
                list.add(h);
            }
        }
        return list;
    }
}
