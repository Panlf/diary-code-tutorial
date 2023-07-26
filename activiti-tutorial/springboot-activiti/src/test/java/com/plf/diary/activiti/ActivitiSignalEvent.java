package com.plf.diary.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;

/**
 * @author panlf
 * @date 2023/7/25
 */
public class ActivitiSignalEvent {
    /**
     * 信号事件是Activiti中的一种事件类型，用于在流程执行过程中通知其他流程实例或任务实例。
     *
     * 信号事件是一种全局事件，可以在任何流程实例或任务实例中触发和捕获。当一个流程实例或任务实例触发了一个信号事件，
     * 其他等待捕获相同信号的流程实例或任务实例将被唤醒并继续执行。
     *
     * 信号事件可以用于以下场景:
     * 1.并行流程实例之间的协作：当一个流程实例需要与其他并行流程实例进行协作时，可以触发一个信号事件来
     * 通知其他流程实例执行相应的任务。
     * 2．动态流程控制：当流程的执行需要根据外部条件进行动态调整时，可以使用信号事件来触发相应的流程变化。
     * 3．异常处理:当发生异常情况时，可以触发一个信号事件来通知其他流程实例或任务实例进行异常处理。
     *
     * 使用信号事件需要以下几个步骤:
     * 1．定义信号事件：在流程定义中定义一个信号事件，指定信号的名称和其他属性。
     * 2．触发信号事件：在流程实例或任务实例中触发—个信号事件。
     * 3.捕获信号事件：在其他流程实例或任务实例中捕获相同名称的信号事件。
     * 4．响应信号事件：在捕获的信号事件中定义相应的处理逻辑，例如执行任务或流程变化。
     *
     * 信号事件我们可以分为开始事件、中间捕获事件、中间抛出事件、边界事件
     */


    /**
     * 开始事件
     * 1、启动事件是一个特殊的信号事件，用于在流程启动时触发
     * 2、当流程启动时，如果存在一个启动事件，并且该事件匹配到了被触发的信号，流程将会启动
     * 3、启动事件可以用于实现流程启动前的条件判断，例如当某个条件满足时，才会启动流程。
     */
    @Test
    public void test01(){
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/event-signal-start.bpmn20.xml")
                .name("信号开始事件")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    @Test
    public void test02(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 发起流程  需要通过 runtimeService 来实现
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

        //由流程中信号中间抛出事件抛出信号，所有订阅了该信号的信号开始事件所在的流程定义都会被启动
        runtimeService.signalEventReceived("signal01");

        //作为普通开始事件，启动流程
        //runtimeService.startProcessInstanceById("");


        /**
         * 在定义信号的时候有-个Scope属性可以设置为Global或processInstance
         *  Global: 全局范围的信号定义，表示可以在任何流程实例中触发和捕获信号。当一个信号事件被触发时，所有
         * 等待捕获该信号的节点都会被唤醒。
         *  processInstance: 流程实例范围的信号定义，表示只能在当前流程实例中触发和捕获信号。当一个信号事件被
         * 触发时，只有等待在当前流程实例中捕获该信号的节点会被唤醒。
         * 
         * 而当前的启动事件是在流程实例启动时触发的事件，用于执行一些初始化操作。启动事件可以在流程定义的开
         * 始节点上定义，并在开始节点上设置事件类型为start。启动事件只有一个全局范围的信号定义，即scope属性只能
         * 设置为Global。当一个启动事件被触发时， 所有等待捕获该信号的节点都会被唤醒。
         */
    }


    /**
     * 信号中间事件分为捕获事件和抛出事件，当流程流转到信号中间捕获事件时会中断并等待触发。
     * 直到接收到相应的信号后沿信号中间捕获事件的外出顺序继续流转。信号事件默认是全局的，与其他事件（如错误事件）不同，
     * 其信号不会在捕获之后被消费。如果存在多个引用了相同信号的事件被激活，即使它们不在同一个流程实例中，
     * 当接收到该信号时，这些事件也会被一并触发。
     */
    @Test
    public void test03(){
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/event-signal-mid.bpmn20.xml")
                .name("信号中间事件")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    /**
     * 信号边界事件会捕获与其信号事件定义引用的信号具有相同信号名称的信号。当流程流转到信号边界事件
     * 依附的流程活动（如用户任务、子流程等）时，工作流引擎会创建一个捕获事件，在其依附的流程活动的
     * 生命周期内等待一个抛出信号。该信号可以由信号中间抛出事件抛出或由API触发。
     * 信号边界事件被触发后流程会沿其外出顺序流继续流转。如果该边界事件设置为中断，则依附的流程活动将被终止。
     */
    @Test
    public void test04(){
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/event-signal-boundary.bpmn20.xml")
                .name("信号边界事件")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

}
