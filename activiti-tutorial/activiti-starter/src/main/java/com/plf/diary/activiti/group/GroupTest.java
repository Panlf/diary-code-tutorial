package com.plf.diary.activiti.group;

import com.plf.diary.activiti.more.Holiday;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * 组任务测试
 * @author Panlf
 * @date 2019/12/12
 */
public class GroupTest {

    @Test
    public void findOwnerTask(){
        //1、创建ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到TaskService
        TaskService taskService = processEngine.getTaskService();

        //3、查询当前用户是否有任务
        String key = "holiday3";
        String assignee = "Mary";

        //4、执行查询
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(assignee)
                .singleResult();

        if(task!=null){
            System.out.println(task.getProcessInstanceId());
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getAssignee());
        }

    }

    /**
     * 归还个人任务为组任务，其实就是设置Task的Assignee为NULL
     */
    @Test
    public void rebackTask(){
        //1、创建ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到TaskService
        TaskService taskService = processEngine.getTaskService();

        //3、查询当前用户是否有任务
        String key = "holiday3";
        String assignee = "Mary";

        //4、执行查询
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(assignee)
                .singleResult();

        if(task!=null){
            taskService.setAssignee(task.getId(),null);
            System.out.println("任务归还成功");
        }

    }

    /**
     * 拾取用户
     *  抽取任务的过程就是将候选用户转换为真正任务的负责人(让任务的assigee有值)
     */
    @Test
    public void ClaimTask(){
        //1、创建ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到TaskService
        TaskService taskService = processEngine.getTaskService();

        //3、查询当前用户是否有任务
        String key = "holiday3";
        String candidateUsers = "Mary";

        //4、执行查询
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskCandidateUser(candidateUsers)
                .singleResult();

        if(task!=null){
            taskService.claim(task.getId(),candidateUsers);
            System.out.println("任务拾取成功");
        }

    }

    /**
     * 查询候选用户的组任务
     */
    @Test
    public void findGroupTask(){
        //1、创建ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到TaskService
        TaskService taskService = processEngine.getTaskService();

        //3、查询当前用户是否有任务
        String key = "holiday3";
        String candidateUsers = "Mary";

        //4、执行查询
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskCandidateUser(candidateUsers)
                .list();

        //5、输出
        for(Task task:taskList){
            System.out.println(task.getProcessInstanceId());
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getAssignee());

        }
    }

    /**
     * 填写请假单的任务要执行完成
     */
    @Test
    public void completeTask(){
        //1、创建ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到TaskService
        TaskService taskService = processEngine.getTaskService();

        //3、查询当前用户是否有任务
        String key = "holiday3";
        Task task = taskService.createTaskQuery().processDefinitionKey(key)
                .taskAssignee("zhangsan").singleResult();

        //4、判断task!=null,说明当前用户有任务
        if(task!=null){
            //完成任务时，设置流程变量的值
            taskService.complete(task.getId());
            System.out.println("任务执行完毕");
        }

    }

    @Test
    public void startInstance(){
        //1、创建ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //3、流程定义的key问题
        String key = "holiday3";

        //4、启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key);

        //5、输出实例信息
        System.out.println("流程定义ID"+processInstance.getProcessDefinitionId());
        System.out.println("流程实例ID"+processInstance.getId());
    }

    /**
     * 新的请假流程定义的部署
     */
    @Test
    public void deployment(){
        //1、创建ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //3、进行部署
        Deployment deployment=repositoryService.createDeployment()
                //添加资源
                .addClasspathResource("bpmn/holiday3.bpmn")
                .addClasspathResource("bpmn/holiday3.png")
                .name("请假流程3")
                .deploy();

        //4、输出部署的一些信息
        System.out.println(deployment.getId());
    }
}
