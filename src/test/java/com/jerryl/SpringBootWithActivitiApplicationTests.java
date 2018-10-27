package com.jerryl;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootWithActivitiApplicationTests {
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private IdentityService identityService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    RepositoryService repositoryService;
    @Resource
    ManagementService managementService;

    /**
     * 历史任务查询
     */
    @Test
    public void HistoricalExampleQuery() {
        List<HistoricActivityInstance> list = historyService // 历史相关Service
                .createHistoricActivityInstanceQuery() // 创建历史活动实例查询
                .processInstanceId("45036") // 执行流程实例id
                .finished()
                .list();
        for (HistoricActivityInstance hai : list) {
            System.out.println("活动ID:" + hai.getId());
            System.out.println("流程实例ID:" + hai.getProcessInstanceId());
            System.out.println("活动名称：" + hai.getActivityName());
            System.out.println("办理人：" + hai.getAssignee());
            System.out.println("开始时间：" + hai.getStartTime());
            System.out.println("结束时间：" + hai.getEndTime());
            System.out.println("=================================");
        }
    }

    /**
     * 查看最新版本的流程定义
     */
    @Test
    public void listLastProcDef() {

        List<ProcessDefinition> listAll = repositoryService// 获取service
                .createProcessDefinitionQuery() // 创建流程定义查询
                .orderByProcessDefinitionVersion().asc() // 根据流程定义版本升序
                .list();  // 返回一个集合

        // 定义有序Map，相同的Key，假如添加map的值  后者的值会覆盖前面相同的key的值
        Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();
        // 遍历集合，根据key来覆盖前面的值，来保证最新的key覆盖前面所有老的key的值
        for (ProcessDefinition pd : listAll) {
            map.put(pd.getKey(), pd);
        }

        List<ProcessDefinition> pdList = new LinkedList<ProcessDefinition>(map.values());
        for (ProcessDefinition pd : pdList) {
            BpmnModel model = repositoryService.getBpmnModel(pd.getId());
            if (model != null) {
                Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
                for (FlowElement e : flowElements) {
                    System.out.println("flowelement id:" + e.getId() + "  name:" + e.getName() + "   class:" + e.getClass().toString());
                }
            }
            System.out.println("ID_" + pd.getId());
            System.out.println("NAME_" + pd.getName());
            System.out.println("KEY_" + pd.getKey());
            System.out.println("VERSION_" + pd.getVersion());
            System.out.println("=========");
        }
    }

    /**
     * 流程实例查询
     */
    @Test
    public void li() {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()
//                .processInstanceId("10001")
                .list();
        for (ProcessInstance pd : list) {
            System.out.println("ID_" + pd.getId());
            System.out.println("KEY_" + pd.getProcessDefinitionKey());
            System.out.println("NAME_" + pd.getProcessDefinitionName());
            System.out.println("=========");
        }
    }


    /**
     * 查看任务
     */
    @Test
    public void find_task() {

        List<Task> taskList = taskService.createTaskQuery()
//                .taskCandidateUser("one")
//                .taskAssignee("one")
                .list();
        for (Task task : taskList) {
            taskService.resolveTask(task.getId());
            System.out.println("任务ID:" + task.getId());
            System.out.println("任务名称:" + task.getName());
            System.out.println("任务创建时间:" + task.getCreateTime());
            System.out.println("任务委派人:" + task.getAssignee());
            System.out.println("任务委派人:" + task.getCreateTime());
            System.out.println("流程实例ID:" + task.getProcessInstanceId());
        }

    }

    /**
     * 查询流程审批历史
     */
    @Test
    public void getHistory() {
        List<HistoricActivityInstance> one = historyService
                .createHistoricActivityInstanceQuery()
//                .taskAssignee("one")
                .processInstanceId("10001")
                .list();
        one.forEach(hai -> {
            System.out.println("活动ID:" + hai.getId());
            System.out.println("流程实例ID:" + hai.getProcessInstanceId());
            System.out.println("活动名称：" + hai.getActivityName());
            System.out.println("办理人：" + hai.getAssignee());
            System.out.println("ttast：" + hai.getTaskId());
            System.out.println("f：" + hai.getActivityId());
            System.out.println("开始时间：" + hai.getStartTime());
            System.out.println("结束时间：" + hai.getEndTime());
            System.out.println("=================================");
        });
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
            if (startTime.before(date) || startTime.equals(date)) {
                list.add(h);
            }
        }
        list.forEach(l -> System.out.println(l.getActivityId() + "....." + l.getEndTime()));


    }

    /**
     * 完成任务
     */
    @Test
    public void okTask() {
        // 查询子流程的执行流
        List<Execution> exe = runtimeService.createExecutionQuery()
                .processInstanceId("20077")
//                .activityId("45002")
                .list();
//                .singleResult();
        exe.forEach(e->System.out.println(e.getId()));
        // 让执行流到达第二个任务

        List<Task> task = taskService.createTaskQuery()
                .taskAssignee("zzz")
//                .taskPriority()
//                .processInstanceId("20035")
                .list();
//        List<Task> task =taskService.createTaskQuery().taskCandidateUser("zzz")
//
//                  .list();//查询所拥有的个人&候选任务

//        task.forEach(System.out::println);
//        Map map = new HashMap();
//        map.put("user", "two-user,three-user");
//
        task.forEach(task1 -> {

//            taskService.resolveTask(task1.getId());
//            taskService.saveTask(task1);
            taskService.complete(task1.getId());
        });


    }

    @Test
    public void toTask() {
        List<HistoricTaskInstance> task = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId("10001")
                .list();
        task.forEach(f -> System.out.println(f.getId()));
        //根据要跳转的任务ID获取其任务
        HistoricTaskInstance hisTask = historyService
                .createHistoricTaskInstanceQuery().taskId("20001")
                .singleResult();
        //进而获取流程实例
        ProcessInstance instance = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(hisTask.getProcessInstanceId())
                .singleResult();
        //取得流程定义
        ProcessDefinitionEntity definition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(hisTask.getProcessDefinitionId());
        //获取历史任务的Activity
        ActivityImpl hisActivity = definition.findActivity(hisTask.getTaskDefinitionKey());
        //实现跳转
        managementService.executeCommand(new JumpCmd(instance.getId(), hisActivity.getId()));
    }

}

class JumpCmd implements Command<ExecutionEntity> {

    private String processInstanceId;
    private String activityId;
    public static final String REASION_DELETE = "deleted";

    public JumpCmd(String processInstanceId, String activityId) {
        this.processInstanceId = processInstanceId;
        this.activityId = activityId;
    }

    @Override
    public ExecutionEntity execute(CommandContext commandContext) {
        ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findExecutionById(processInstanceId);

        executionEntity.destroyScope(REASION_DELETE);
        ProcessDefinitionImpl processDefinition = executionEntity.getProcessDefinition();
        ActivityImpl activity = processDefinition.findActivity(activityId);
        executionEntity.executeActivity(activity);

        return executionEntity;
    }

}