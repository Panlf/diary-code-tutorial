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
public class ActivitiErrorEvent {
    /**
     * 错误事件
     *  错误事件可以用做一个流程的开始事件或者作为一个任务或者子流程的边界事件，错误事件没有提供作用
     *  中间事件的功能，在错误事件中提供了错误结束事件。
     *
     */


    /**
     * 开始事件
     *   错误开始事件（error start event）可以触发一个事件子流程，且总是在另外一个流程异常结束时触发。
     *   BPMN2.0规定了错误开始事件只能在事件子流程中被触发，不能在其他流程中被触发，包括顶级流程、
     *   嵌套子流程和调用活动。错误启动事件不能用于启动流程实例。
     *   错误启动事件总是中断。
     */
    @Test
    public void test01(){
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/event-error-start.bpmn20.xml")
                .name("错误开始事件")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }
    @Test
    public void test02(){

        /**
         * 错误开始事件可以在如下的场景
         * 1、输入验证失败：当用户提交工作流启动请求时，需要对输入的数据进行验证。
         * 如果数据不符合预期的格式或规则，可以使用错误开始事件来捕获并处理验证失败的情况。
         * 2、权限验证失败：在某些情况下，只有特定的用户或用户组才能启动某个工作流。当非授权用户
         * 尝试启动工作流时，可以使用错误开始事件来捕获并处理权限验证失败的情况。
         * 3、前置条件不满足：在工作流启动之前，可能需要满足一些前置条件，例如某个数据已经存在或某个服务可用。
         * 如果前置条件不满足，可以使用错误开始事件来捕获并处理这种情况。
         * 4、数据源异常：在工作流启动过程中，可能需要从外部数据源获取数据。如果数据源出现异常导致无法获取数
         * 据，可以使用错误开始事件来捕获并处理数据源异常的情况。
         *
         */

        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 发起流程  需要通过 runtimeService 来实现
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        // 通过流程定义ID来启动流程 返回的是流程实例对象
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById("event-error-start:1:60003");

        System.out.println("id : "+processInstance.getId());
        System.out.println("deploymentId : "+processInstance.getDeploymentId());
        System.out.println("description : "+processInstance.getDescription());
    }


    /**
     * 边界事件
     * 当某个任务发生错误时，可以通过错误边界事件来捕获并处理该错误，以保证流程的正常执行。
     *
     * 错误边界事件可以在流程中的任务节点上定义，并与该任务节点关联。
     * 当任务节点执行过程中发生错误时，错误边界事件会被触发，并执行相应的处理逻辑，如发送错误通知、重新分配任务、跳转到其他节点等。
     *
     * 错误边界事件可以捕获多种类型的错误，如异常、超时、网络故障等。通过使用错误边界事件，
     * 可以增加流程的容错性，并提供更好的错误处理机制，保证流程的稳定性和可靠性。

     * 需要注意的是，错误边界事件只能与任务节点关联，而不能与其他类型的节点(如网关、开始节点、结束节
     * 点)关联。此外，在设计流程时，需要准确定义错误边界事件的触发条件和处理逻辑，以确保错误能够被正确捕获和处理。
     */
    @Test
    public void test03(){
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/event-error-boundary.bpmn20.xml")
                .name("错误边界事件")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    /**
     * 结束事件
     *  在Activiti中，错误结束事件（Error End Event)是一个用于标记流程实例在特定错误条件下结束的节点。
     *  当流程实例执行到错误结束事件时，流程实例将立即终止执行，并且流程实例的状态将被标记为"错误结束"。
     *
     *  错误结束事件可以与错误边界事件(Error BoundaryEvent)结合使用，用于在流程中捕获和处理特定的错误。
     *  当错误边界事件触发时，流程会跳转到与错误边界事件关联的错误结束事件，从而使流程实例结束。
     *
     *  错误结束事件可以配置一个错误代码，用于标识特定的错误类型。在流程定义中，可以定义多个错误结束事件，
     *  每个事件可以有不同的错误代码。当流程实例执行到错误结束事件时，可以根据错误代码进行相应的处理，
     *  例如记录日志、发送通知等。
     *
     *  错误结束事件可以用于处理各种错误情况，例如系统异常、业务规则异常等。通过使用错误结束事件，
     *  可以使流程能够在错误发生时进行合理的处理。提高系统的可靠性和稳定性。
     *
     *  总之，错误结束事件是Activiti中的一个节点，用于标记流程实例在特定错误条件下结束。
     *  它可以与错误边界事件结合使用，用于捕获和处理特定的错误。通过使用错误结束事件，
     *  可以实现对流程中各种错误情况的处理和管理。
     */

    @Test
    public void test04(){
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/event-error-end.bpmn20.xml")
                .name("错误结束事件")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }
}
