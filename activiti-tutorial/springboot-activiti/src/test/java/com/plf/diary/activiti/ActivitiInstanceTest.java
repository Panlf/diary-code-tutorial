package com.plf.diary.activiti;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author panlf
 * @date 2023/7/31
 */
public class ActivitiInstanceTest {

    /**
     * 部署
     */
    @Test
    public void test01(){
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("instance/multi-instance.bpmn20.xml")
                .name("多实例子流程")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    /**
     * 开启流程
     */
    @Test
    public void test02(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 发起流程  需要通过 runtimeService 来实现
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

        // 通过流程定义ID来启动流程 返回的是流程实例对象
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("multi-instance");
    //70001
        System.out.println("id : "+processInstance.getId());
        System.out.println("deploymentId : "+processInstance.getDeploymentId());
        System.out.println("description : "+processInstance.getDescription());
    }

    @Test
    public void test03(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("zhangsan").list();

        //zhangsan ===> 分为 a1 a2 a3 三个人
        Map<String,Object> param = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("a1");
        list.add("a2");
        list.add("a3");
        list.add("a4");
        param.put("userList",list);
        if(!CollectionUtils.isEmpty(taskList)){
            taskList.forEach(x->{
                System.out.println(x.getProcessInstanceId());
                System.out.println(x.getId());
                System.out.println(x.getName());
                System.out.println(x.getAssignee());
                taskService.complete(x.getId(),param);
            });
        }
    }

    @Test
    public void test04(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("zhangsan").list();
        if(!CollectionUtils.isEmpty(taskList)) {
            taskList.forEach(x -> {
                System.out.println(x.getProcessInstanceId());
                System.out.println(x.getId());
                System.out.println(x.getName());
                System.out.println(x.getAssignee());
               }
            );
        }
    }

    @Test
    public void test05(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("a4").list();
        Map<String,Object> param = new HashMap<>();
        param.put("lastuser","b4");
        if(!CollectionUtils.isEmpty(taskList)) {
            taskList.forEach(x -> {
                        taskService.complete(x.getId(),param);
                }
            );
        }
    }

    @Test
    public void test06(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("b4").list();
        if(!CollectionUtils.isEmpty(taskList)) {
            taskList.forEach(x -> {
                        taskService.complete(x.getId());
                    }
            );
        }
    }
}
