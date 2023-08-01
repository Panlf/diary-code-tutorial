package com.plf.diary.activiti;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author panlf
 * @date 2023/8/1
 */
public class ActivitiChangeUser {

    @Test
    public void test01(){
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("instance/change-assign.bpmn20.xml")
                .name("流程转办")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }
    @Test
    public void test02(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 发起流程  需要通过 runtimeService 来实现
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

        Map<String,Object> map = new HashMap<>();
        map.put("user","张三");
        // 通过流程定义ID来启动流程 返回的是流程实例对象
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("change-assign",map);
        System.out.println("id : "+processInstance.getId());
        System.out.println("deploymentId : "+processInstance.getDeploymentId());
        System.out.println("description : "+processInstance.getDescription());
    }


    @Test
    public void test03(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("李四").list();
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
    public void test04(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskAssignee("李四").singleResult();
        //转办：直接将办理人换成别人，这时任务的拥有着不再是转办人。
        taskService.setAssignee(task.getId(),"李四");
    }
}
