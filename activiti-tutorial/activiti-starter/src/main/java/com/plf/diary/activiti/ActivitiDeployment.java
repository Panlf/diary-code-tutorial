package com.plf.diary.activiti;

import org.activiti.bpmn.model.*;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * 流程定义的部署
 * 涉及到的表
 *      act_re_deployment   部署信息
 *      act_re_procdef      流程定义的一些信息
 *      act_ge_bytearray    流程定义的bpmn文件及png文件
 * @author Panlf
 * @date 2019/12/10
 */
public class ActivitiDeployment {


    @Test
    //单文件流程定义部署 流程制作出来后要上传到服务器 zip文件更便于上传
    public void singleDeployMent(){
        //1、创建ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //3、转换为ZipInputStream流对象
        InputStream inputStream = ActivitiDeployment.class
                .getClassLoader().getResourceAsStream("bpmn/holiday.zip");

        //4、 将inputstream流转换化为ZipInputStream流
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);

        //5、进行部署
        Deployment deployment=repositoryService.createDeployment()
                //添加资源
                .addZipInputStream(zipInputStream)
                .name("请假流程")
                .deploy();


        //6、输出部署的一些信息
        System.out.println(deployment.getId());

    }


    @Test
    public void test01(){
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml")
                .buildProcessEngine();

        System.out.println(processEngine);
    }

    //流程定义部署
    // 同一个key可重复部署 影响的表 act_ge_bytearray 的 DEPLOYMENT_ID_ 关联 act_re_deployment 的 id
    // act_re_procdef 的 DEPLOYMENT_ID_ 关联 act_re_deployment 的 id
    public static void main(String[] args){

        //1、创建ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //3、进行部署
        Deployment deployment=repositoryService.createDeployment()
                //添加资源
                .addClasspathResource("bpmn/holiday.bpmn")
                .addClasspathResource("bpmn/holiday.png")
                .name("请假流程")
                .deploy();


        //4、输出部署的一些信息
        // 1
        System.out.println(deployment.getId());
        // holiday
        System.out.println(deployment.getKey());
        // 请假流程
        System.out.println(deployment.getName());
    }

    /**
     * 获取流程部署的信息
     */
    @Test
    public void test02(){
        //1、创建ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        /*ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionKey("holiday").singleResult();*/

         repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionKey("holiday").list().forEach(x-> {
                    /*System.out.print(x.getId());
                    System.out.print(x.getDeploymentId());
                    System.out.print(x.getKey());*/
                    System.out.printf("%s - %s - %s\n",x.getId(),x.getDeploymentId(),x.getKey());
                }
         );

        // 这里用的ID是用act_re_procdef的ID_
        System.out.println(repositoryService.getProcessDefinition("holiday:2:2504").getName());
    }

    @Test
    public void test03() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();

        BpmnModel bpmnModel = repositoryService.getBpmnModel("holiday:2:2504");


        Collection<FlowElement> flowElements = bpmnModel.getProcesses().get(0).getFlowElements();

        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof UserTask) {
                UserTask userTask = (UserTask) flowElement;
                // 分配到任务的人
                System.out.println(userTask.getAssignee());

                //System.out.println(userTask.getFormKey());

                List<ExtensionElement> extensionElements = userTask.getExtensionElements().get("fields");
                if (!CollectionUtils.isEmpty(extensionElements)) {
                    // 处理需要传递的字段信息
                    for (ExtensionElement extensionElement : extensionElements) {
                        String fieldName = extensionElement.getAttributeValue(null, "fieldName");
                        String fieldType = extensionElement.getAttributeValue(null, "fieldType");
                        // 处理字段信息
                        System.out.println(fieldName);
                        System.out.println(fieldType);
                    }
                }
            } else if (flowElement instanceof ExclusiveGateway) {
                ExclusiveGateway exclusiveGateway = (ExclusiveGateway) flowElement;
                Map<String, List<ExtensionAttribute>> attributes = exclusiveGateway.getAttributes();
                System.out.println(attributes);
            } else if (flowElement instanceof ParallelGateway) {

            } else if (flowElement instanceof EventGateway) {

            }
            //System.out.println(flowElement.getAttributes());
        }

    }

}
