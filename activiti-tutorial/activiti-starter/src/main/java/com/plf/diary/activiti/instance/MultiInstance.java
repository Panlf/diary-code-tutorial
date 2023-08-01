package com.plf.diary.activiti.instance;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author panlf
 * @date 2023/8/1
 */
public class MultiInstance {

    /**
     * 流程部署
     */
    @Test
    public void test01(){
        //1、创建ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3、进行部署
        Deployment deployment=repositoryService.createDeployment()
                //添加资源
                .addClasspathResource("more/data-capture.bpmn20.xml")
                .name("多级数据处理流程")
                .deploy();
        //4、输出部署的一些信息
        System.out.println(deployment.getId());
    }

    /**
     * 启动流程
     * a 启动流程
     * 设置 b1为一级联络员
     * 并设置为 一级任务
     *
     */
    @Test
    public void test02(){
        //1、得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、得到RunService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //3、创建流程实例
        Map<String,Object> param = new HashMap<>();
        param.put("starter","a");
        param.put("streetLink","b1");
        param.put("isVillageTask",false);

        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("data-capture",param);
        //4、输出实例的相关信息
        System.out.println("流程部署的ID："+processInstance.getDeploymentId());

        //data-capture:1:5003
        System.out.println("流程定义的ID："+processInstance.getProcessDefinitionId());

        //7501
        System.out.println("流程实例的ID："+processInstance.getId());

        System.out.println("活动的ID："+processInstance.getActivityId());
    }

    /**
     * 查询任务
     */
    @Test
    public void test03(){
        //1、得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3、根据流程定义的key，负责人assignee来实现当前用户的任务列表查询
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("a").list();

        if(!CollectionUtils.isEmpty(list)){
           list.forEach(task->{
               System.out.println("流程实例ID："+task.getProcessInstanceId());
               System.out.println("任务ID："+task.getId());
               System.out.println("任务负责人："+task.getAssignee());
               System.out.println("任务名称："+task.getName());
           });
        }
    }

    /**
     * 发起人审批任务
     */
    @Test
    public void test04(){
        //1、得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("a").list();
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(task->{
                taskService.complete(task.getId());
            });
        }
    }

    /**
     * 一级任务指派经办人
     */
    @Test
    public void test05(){
        //1、得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("b1").list();
        Map<String,Object> param = new HashMap<>();
        param.put("streetHandle","b1-1");
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(task->{
                taskService.complete(task.getId(),param);
            });
        }
    }

    /**
     * 一级任务经办人处理
     */
    @Test
    public void test06(){
        //1、得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("b1-1").list();
        Map<String,Object> param = new HashMap<>();
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(task->{
                taskService.complete(task.getId(),param);
            });
        }
    }
}
