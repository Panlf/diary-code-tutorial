package com.plf.diary.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Test;

/**
 * @author panlf
 * @date 2023/7/26
 */
public class ActivitiOtherEvent {
    /**
     * 终止结束事件
     *
     * 终止结束事件也称为中断结束事件，主要是对流程进行终止的事件，可以在一个复杂的流程中，如果某方想要提前中断这个流程，
     * 可以采用这个事件来处理，可以在并行处理任务中。如果你是在流程实例层处理，整个流程都会被中断，
     * 如果是在子流程中使用，那么当前作用和作用域内的所有的内部流程都会被终止。
     */
    @Test
    public void test01(){
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/event-final-end.bpmn20.xml")
                .name("终止结束事件1")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
        //设置终止结束事件，里面有一个terminateAll默认为false。含义是当终止结束事件在多实例或者
        //嵌套的子流程中。那么不会终止整个流程。如果设置为true那么不管是否嵌套都会终止整个实例流程。
    }

    @Test
    public void test02(){
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/event-final-end2.bpmn20.xml")
                .name("终止结束事件2")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
        //在子流程中触发终止结束事件。terminateAll控制结束事件的范围
    }

    /**
     * 取消结束事件
     *    取消结束事件（cancel end event）只能与BPMN事务子流程（BPMN transaction subprocess）一起使用。
     *    当到达取消结束事件时，会抛出取消事件，且必须由取消边界事件（cancel boundary event）获取。
     *    取消边界事件将取消事务，并触发补偿（compensation）。
     */
    @Test
    public void test03(){
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/event-cancel-end.bpmn20.xml")
                .name("取消结束事件")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
        //isForCompensation="true" 补偿任务是需要这个参数的
        //在流程设计器中没有直接提供事务子流程的图标，我们需要通过普通子流程来设置事务属性即可
        //is a transaction sub process 打勾
    }

    /**
     * 补偿事件
     *  在activiti中，补偿事件（compensation event）是一种用于处理流程中发生异常或者错误的特殊事件。
     *  当流程中的某个任务或活动发生错误或无法继续执行时，补偿事件可以被触发来回滚或修复之前已经完成的任务或活动。
     *
     *  补偿事件通常与错误边界事件（Error Boundary Event）结合使用。错误边界事件在流程中的任务或活动
     *  周围设置得到捕获异常事件。当任务或活动发生异常时，错误边界事件将被触发，进而触发相应的补偿事件。
     *
     *  补偿事件可以执行一系列的补偿操作，包括撤销之前已经完成的任务、还原数据、发送通知等。补偿操作的具体
     *  步骤和逻辑可以在流程定义中定义，并且可以使用Java代码或者脚本来实现。
     *
     *  补偿事件的触发和执行是自动完成的，无需人工干预。一旦补偿事件被触发，Activiti引擎会自动查找相应的补偿事件，
     *  并按照定义的补偿操作进行执行
     *
     *  通过使用补偿事件，可以有效地处理流程中的异常情况，提供流程的鲁棒性和容错性。补偿事件可以帮助流程
     *  在发生错误时自动进行修复，确保流程能够正常完成。
     */
}
