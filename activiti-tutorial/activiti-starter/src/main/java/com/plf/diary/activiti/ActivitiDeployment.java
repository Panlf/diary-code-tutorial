package com.plf.diary.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

import java.io.InputStream;
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


    //流程定义部署
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
        System.out.println(deployment.getId());
    }
}
