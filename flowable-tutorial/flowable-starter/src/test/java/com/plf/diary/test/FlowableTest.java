package com.plf.diary.test;

import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author panlf
 * @date 2022/4/24
 */
public class FlowableTest {

    @Test
    public void testProcessEngine(){
        ProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration();
        // 配置 相关的数据库的连接信息
        configuration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("root");
        configuration.setJdbcUrl("jdbc:mysql://localhost:3306/flowable?serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true");
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = configuration.buildProcessEngine();
        System.out.println("processEngine===>"+processEngine);
    }

    ProcessEngineConfiguration configuration = null;

    @BeforeEach
    public void before(){
        configuration = new StandaloneProcessEngineConfiguration();
        // 配置 相关的数据库的连接信息
        configuration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("root");
        configuration.setJdbcUrl("jdbc:mysql://localhost:3306/flowable?serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true");
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    }

    /**
     * 部署
     */
    @Test
    public void testDeploy(){
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("MyHoliday.bpmn20.xml")
                .name("请假流程")
                .deploy();

        System.out.println("deploy id ===>"+deploy.getId());
        System.out.println("deploy name ===>"+deploy.getName());
    }

    /**
     * 查询流程定义信息
     */
    @Test
    public void testDeployQuery(){
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId("1")
                .singleResult();
        System.out.println(processDefinition.getDeploymentId());
        System.out.println(processDefinition.getName());
        System.out.println(processDefinition.getDescription());
        System.out.println(processDefinition.getId());
        System.out.println(processDefinition.getKey());
    }

    /**
     * 删除流程定义
     */
    @Test
    public void testDeleteDeploy(){
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //删除部署的流程 第一个参数是 id 如果部署的流程启动了就不允许删除
        repositoryService.deleteDeployment("1");
        //第二个参数是级联删除，如果流程启动了 相关的任务一并会被删除
        repositoryService.deleteDeployment("1",true);
    }

    @Test
    public void testRunProcess(){
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String,Object> map = new HashMap<>();
        map.put("employee","张三");

        ProcessInstance myHoliday = runtimeService.startProcessInstanceById("MyHoliday", map);
        System.out.println(myHoliday.getProcessDefinitionId());
        System.out.println(myHoliday.getActivityId());
        System.out.println(myHoliday.getId());
    }

    @Test
    public void testQueryTask(){
        ProcessEngine processEngine = configuration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("MyHoliday")
                .taskAssignee("zhangsan")
                .list();
        for (Task t:list){
            System.out.println(t.getName());
            System.out.println(t.getId());
        }
    }

    @Test
    public void testCompleteTask(){
        ProcessEngine processEngine = configuration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("MyHoliday")
                .taskAssignee("zhangsan")
                .singleResult();
        Map<String,Object> map = new HashMap<>();
        map.put("approved",true);
        taskService.complete(task.getId(),map);
    }

    @Test
    public void testHistory(){
        ProcessEngine processEngine = configuration.buildProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processDefinitionId("")
                .finished()
                .orderByHistoricActivityInstanceEndTime().asc()
                .list();
        for (HistoricActivityInstance h:historicActivityInstanceList){
            System.out.println(h.getAssignee());
            System.out.println(h.getActivityId());
            System.out.println(h.getDurationInMillis());
        }
    }
}
