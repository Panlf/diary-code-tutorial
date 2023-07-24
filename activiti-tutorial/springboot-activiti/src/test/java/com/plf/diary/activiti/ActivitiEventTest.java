package com.plf.diary.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;

/**
 * @author panlf
 * @date 2023/7/19
 */
public class ActivitiEventTest {
    /**
     * 事件（event）通常用于为流程生命周期中发生的事情建模。事件总是图形化为圆圈。
     * 在BPMN 2.0中，有两种主要的事件分类：捕获（catching）与抛出（throwing）事件。
     * 捕获：当流程执行到达这个事件时，会等待直到触发器动作。触发器的类型由其中的图标，或者说XML中的类型声明而定义。
     *      捕获事件与抛出事件显示上的区别，是其内部的图标没有填充（即是白色的）。
     * 抛出：当流程执行到达这个事件时，会触发一个触发器。触发器的类型，由其中的图标，
     *      或者说XML中的类型声明而定义。抛出事件与捕获事件显示上的区别，是其内部的图标填充为黑色。
     */


    /**
     * 定时器事件
     *     定时器事件是一种在特定时间触发的事件。在Activiti中，可以通过定时器事件来实现定时执行某个任务
     *     或者触发某个流程实例，具体包括定时器启动事件，定时器捕获中间件事件，定时器边界事件，在很多的业务场景中。
     */



    /*
     * 定时器开始事件
     *   定时器启动事件（timer start event）在指定时间创建流程实例。在流程只需要启动一次，
     *    或者流程需要在特定的时间间隔重复启动时，都可以使用。在使用时我们需要注意如下几点
     *  1、子流程不能有定时器启动事件
     *  2、定时器启动事件，在流程部署的同时就开始计时，不需要调用startProcessInstanceByXXX就会在时间启动。
     *      调用startProcessInstanceByXXX时会在定时启动之外额外启动一个流程
     *  3、当部署带有定时器启动事件的流程的更新版本时，上一版本的定时器作业会被移除。这是因为通常并不希望旧版本
     *      的流程仍然自动启动新的流程实例
     *  4、asyncExecutorActivate：需要设置true，否则定时器不会生效，因为这块需要开启异步任务。
     *
     * 定时器开始事件除了上面的指定固定时间启动外我们还可以通过循环和持续时间来处理
     * timeDate         指定一个具体的日期和时间 例如2023-07-09T00:00:00
     * timeCycle        指定一个重复周期 例如R/PT1H表示每隔1小时触发一次
     * timeDuration     指定一个持续时间 例如PT2H30M表示持续2小时30分钟
     */
    @Test
    public void test01(){
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/event01-timer-begin.bpmn20.xml")
                .name("定时器开始事件")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }
    @Test
    public void test02(){
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/event02-timer-begin.bpmn20.xml")
                .name("定时器开始事件")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }


    @Test
    public void test03(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 发起流程  需要通过 runtimeService 来实现
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        // 通过流程定义ID来启动流程 返回的是流程实例对象
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById("event02-timer-begin:1:50003");

        System.out.println("id : "+processInstance.getId());
        System.out.println("deploymentId : "+processInstance.getDeploymentId());
        System.out.println("description : "+processInstance.getDescription());
    }

    /**
     *
     * 定时器中间事件
     * 在开始事件和结束事件之间发生的事件称为中间事件，
     * 定时器中间捕获事件指在流程中将一个定时器作为独立的节点来运行，是一个捕获事件。
     * 当流程流转到定时器中间捕获事件时，会启动一个定时器，并一直等待触发，只有
     * 到达指定时间定时器才被触发。
     */
    @Test
    public void test04(){
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/event-timer-mid.bpmn20.xml")
                // 当我们审批通过 申请出库 后，等待一分钟触发定时器。然后会进入到 出库处理
                .name("定时器中间事件")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    /**
     *
     * 定时器边界事件
     *
     *   当某个用户任务或者子流程在规定的时间后还没有执行。
     *   那么我们就可以通过定时器边界事件来触发执行特定的处理流程。
     *
     *  注意在定时器边界事件配置了cancelActivity属性，用于说明该事件是否为中断事件。
     *  cancelActivity属性值默认为true，表示它是边界中断事件，当该边界事件触发时，
     *  它所依附的活动实例被终止，原有的执行流程会被中断，流程将沿边界事件的外出顺序继续流传。
     *  如果将其设置为false，表示它是边界非中断事件，当边界事件触发时，则原来的执行流程仍然存在，
     *  所依附的活动实例继续执行，同时也执行边界事件的外出顺序流。
     */
    @Test
    public void test05() {
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/event-time-boundary.bpmn20.xml")
                // 当我们审批通过 申请出库 后，等待一分钟触发定时器。然后会进入到 出库处理
                .name("定时器边界事件")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }


    /**
     * 消息事件
     *  消息事件（message event），是指引用具名消息的事件。消息具有名字与载荷。与信号不同，消息事件只有一个接收者。
     */


    /**
     * 开始事件
     * 消息开始事件，也就是我们通过接收到某些消息后来启动流程实例，比如接收到了一封邮件，一条短信等，具体通过案例来讲解。
     */
    @Test
    public void test06() {
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/event-message-start.bpmn20.xml")
                .name("消息启动事件")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
        //部署完流程后，消息启动事件会在act_ru_event_subscr中记录我们的定义信息。
    }

    @Test
    public void test07() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        //发送消息 发送的消息应该是具体的消息的名称而不是id
        //注意 消息的名称我们不要使用驼峰命名法来定义
        runtimeService.startProcessInstanceByMessage("firstmsg");
    }

    /**
     * 消息中间事件就是在流程运行中需要消息来触发场景，案例演示，自动流程1处理完成后，需要接收特定的消息之后才能进入到自动流程2
     */
    @Test
    public void test08() {
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/event-message-middle.bpmn20.xml")
                .name("消息中间事件")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    @Test
    public void test09(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        Execution execution = runtimeService
                .createExecutionQuery()
                .processInstanceId("")
                .onlyChildExecutions()
                .singleResult();
        runtimeService.messageEventReceived("msg02",execution.getId());
    }

    /**
     * 边界事件
     * 消息边界事件同样针对是用户节点在消息触发前如果还没有审批。就会触发消息事件的处理逻辑。
     */
    @Test
    public void test10() {
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/event-message-boundary.bpmn20.xml")
                .name("消息边界事件")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }
}
