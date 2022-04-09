package com.plf.diary.activiti.more;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程变量的测试
 * @author Panlf
 * @date 2019/12/11
 */
public class VariableTest02 {

    /**
     * 完成任务 -- zhangsan --- lisi ---- 判断流程变量的请假天数  分支：人事经理存档
     */
    @Test
    public void completeTask(){
        //1、创建ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到TaskService
        TaskService taskService = processEngine.getTaskService();
        //3、查询当前用户是否有任务
        String key = "myProcess_1";
        Task task = taskService.createTaskQuery().processDefinitionKey(key)
                .taskAssignee("lisi").singleResult();

        Map<String,Object> map = new HashMap<String, Object>();
        Holiday holiday = new Holiday();
        //5F  另外一条流程
        holiday.setNum(1F);
        map.put("holiday",holiday);


        //4、判断task!=null,说明当前用户有任务
        if(task!=null){
            //完成任务时，设置流程变量的值
            taskService.complete(task.getId(),map);
            System.out.println("任务执行完毕");
        }

    }

    /**
     * 启动流程实例
     */
    @Test
    public void startInstanceWithValue(){
        //1、创建ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //3、流程定义的key问题
        String key = "myProcess_1";

        //4、启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key);

        //5、输出实例信息
        System.out.println(processInstance.getName());
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
                .addClasspathResource("bpmn/holiday2.bpmn")
                .addClasspathResource("bpmn/holiday2.png")
                .name("请假流程-流程变量")
                .deploy();

        //4、输出部署的一些信息
        System.out.println(deployment.getId());
    }
}
