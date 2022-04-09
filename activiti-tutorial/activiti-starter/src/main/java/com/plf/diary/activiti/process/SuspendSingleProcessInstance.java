package com.plf.diary.activiti.process;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * 单个流程挂起
 *      单个人的流程挂起
 *
 *  操作流程对象，针对单个流程执行挂起操作，某个流程实例挂起则此流程不再继续执行，完成
 *  该流程实例的当期任务将报异常 -- Cannot complete a suspended task
 *
 *
 * @author Panlf
 * @date 2019/12/11
 */
public class SuspendSingleProcessInstance {

    public static void main(String[] args) {
        //1、得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、创建RuntimeService对象
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //3、查询流程实例的对象
        ProcessInstance processInstance =runtimeService.createProcessInstanceQuery()
                .processInstanceId("2501").singleResult();

        //5、得到当前流程实例是否为暂停状态
        boolean suspend = processInstance.isSuspended();

        String processInstanceId = processInstance.getId();

        //6、判断
        if(suspend){
            //说明是暂停，就可以激活操作
            runtimeService.activateProcessInstanceById(processInstanceId);
        }else{
            runtimeService.suspendProcessInstanceById(processInstanceId);
        }
    }
}
