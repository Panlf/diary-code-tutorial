package com.plf.diary.activiti.process;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;

/**
 * 全部流程挂起
 *      针对比如请假流程更改，有一些人的流程还未走完，需挂起
 *
 *  操作流程定义为挂起状态，该流程定义下边所有的流程实例全部暂停
 *  流程定义为挂起状态该流程定义将不允许启动新的流程实例，同时该流程定义下所有的
 *  流程实例将全部挂起暂停执行。
 * @author Panlf
 * @date 2019/12/11
 */
public class SuspendProcessInstance {

    public static void main(String[] args) {
        //1、得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、创建RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //3、得到ProcessDefinitionQuery对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        //4、查询流程定义的对象
        ProcessDefinition processDefinition = processDefinitionQuery
                .processDefinitionKey("holiday").singleResult();

        //5、得到当前流程定义的实例是否都为暂停状态
        boolean suspend = processDefinition.isSuspended();

        String processDefinitionId = processDefinition.getId();

        //6、判断
        if(suspend){
            //说明是暂停，就可以激活操作
            repositoryService.activateProcessDefinitionById(processDefinitionId,true,null);
        }else{
            repositoryService.suspendProcessDefinitionById(processDefinitionId,true,null);
        }
    }
}
