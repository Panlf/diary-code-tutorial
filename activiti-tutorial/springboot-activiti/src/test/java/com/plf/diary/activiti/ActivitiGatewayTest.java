package com.plf.diary.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author panlf
 * @date 2023/7/17
 */
public class ActivitiGatewayTest {

    /**
     * 网关
     *   网关可控制流程执行流向，常用于拆分或者合并复杂的流程场景
     *
     *   1、排他网关 Exclusive Gateway 用于在流程中进行条件判断，根据不同的条件选择不同的分支路径。只有满足条件的分支会被执行，其他分支会被忽略
     *   2、并行网关 Parallel GateWay   用于将流程分成多个并行的分支，这些分支可以同时执行。当所有分支都执行完毕后，流程会继续往下执行
     *   3、包容网关 Inclusive Gateway 用于根据多个条件的组合情况选择分支路径。可以选择满足任意一个条件的分支执行，或者选择满足所有条件的分支执行
     *   4、事件网关 Event Gateway   用于根据事件的触发选择分支路径。当指定的事件触发时，流程会选择对应的分支执行。
     *
     * */


    /**
     * 排他网关（exclusive gateway）（也叫异或网关XOR gateway，或者更专业的，基于数据的排他网关 exclusive data-based gateway），用于
     * 对流程中的决策建模，当执行到达这个网关时，会按照所有出口顺序流定义的顺序对他们进行计算。选择第一个条件计算为true的顺序流（当没有设置条件时，
     * 认为顺序流为true）继续流程
     * 排他网关用内部带X图标的标准网关（菱形）表示，X图标代表异或含义。请注意内部没有图标的网关默认为排他网关。BPMN2.0规范不允许在同一个流程
     * 中混合使用及没有X的菱形标志。
     */
    @Test
    public void test01(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("zhangsan").list();
        Map<String,Object> map = new HashMap<>();
        map.put("days",6);
        for(Task task:taskList){
            taskService.complete(task.getId(),map);
        }
    }


    @Test
    public void test02(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("wangwu").list();
        for(Task task:taskList){
            taskService.complete(task.getId());
        }
    }


    /**
     * 并行网关
     *     并行网关允许将流程分成多条分支，也可以将多条分支汇聚到一起，并行网关的功能是基于进入和外出顺序流的
     *
     *     fork分支：并行后的所有外出顺序流，为每个顺序流都创建一个并行分支
     *     join汇聚：所有到达并行网关，在此等待的进入分支，直到所有进入顺序流的分支都到达以后，流程就会通过汇聚网关
     *
     *     注意：如果同一个并行网关有多个进入和多个外出顺序流，它就同时具有分支和汇聚功能。这时，网关会先汇聚所有进入的顺序流，
     *     然后再切分成多个并行分支。
     *
     *     与其他网关的主要区别是，并行网关不会解析条件。即使顺序流中定义了条件也会被忽略。
     *
     */
    @Test
    public void test03(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("s1").list();
        for(Task task:taskList){
            taskService.complete(task.getId());
        }

        /*
        *  在并行网关中我们需要注意的是执行实例的概念
        *   主流程实例   流程启动就会维护一条记录，ACT_RU_EXECUTION中parent_id_为null的记录
        *   子流程实例   流程的每一步操作。都会更新子流程实例。表示当前流程的执行进度。如果进入的是并行网关。
        *               案例中的网关会产生3个子流程实例和一个主流程实例。
        * */
    }


    /*
    * 包容网关
    *   包含网关可以看做是排他网关和并行网关的结合体。和排他网关一样，你可以在外出顺序流上定义条件，
    *   包含网关会解析他们，但是主要的区别是包含网关可以选择多于一条顺序流，这和并行网关一样。
    *
    *   包含网关的功能是基于进入和外出顺序流的：
    *       分支：所有外出顺序流的条件都会被解析，结果为true的顺序会以并行方式继续执行，会为每个顺序流创建一个分支
    *
    *       汇聚：所有并行分支到达包含网关，会进入等待状态，直到每个包含流程token的进入顺序流的分支都达到。
    *           这是与并行网关得到最大不同。换句话说，包含网关只会等待被选中执行了的进入顺序流。
    *           在汇聚之后，流程会穿过包含网关继续执行。
    * */

    @Test
    public void test04(){
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/gateway3.bpmn20.xml")
                .name("包容网关")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }


    /**
     * 事件网关
     *   事件网关允许根据事件判断流向。网关的每个外出顺序流都要连接到一个中间捕获事件。
     *   当流程到达一个基于事件网关，网关会进入等待状态：会暂停执行。与此同时，会为每个外出顺序流创建相对得到事件订阅。
     *   事件网关的外出顺序流和普通顺序流不同，这些顺序流不会真的执行，相反它们让流程引擎去决定执行到事件网关的流程需要订阅哪些事件。
     *   要考虑以下条件：
     *      事件网关必须有两条或以上外出顺序流
     *      事件网关后，只能使用intermediateCatchEvent类型（activiti不支持基于事件网关后连接ReceiveTask）
     *      连接到事件网关的中间捕获事件必须只有一个入口顺序流。
     */
}

