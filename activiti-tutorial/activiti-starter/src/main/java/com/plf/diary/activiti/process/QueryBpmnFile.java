package com.plf.diary.activiti.process;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 需求
 *  1、从activiti的act_ge_bytearray表中读取两个资源文件
 *  2、将两个资源文件保存到路径  E:/temp
 *
 * @author Panlf
 * @date 2019/12/11
 */
public class QueryBpmnFile {
    public static void main(String[] args) throws IOException {
        //1、得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、创建RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //3、得到ProcessDefinitionQuery对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        //4、设置查询条件
        processDefinitionQuery.processDefinitionKey("holiday");

        //5、执行查询操作，查询出想要的流程定义
        ProcessDefinition processDefinition = processDefinitionQuery.singleResult();

        //6、通过流程定义信息，得到部署ID
        String deploymentId = processDefinition.getDeploymentId();

        //7、通过的方法，实现读取图片信息及bpmn文件信息（输入流）
        //getResourceAsStream第一个参数部署id，第二个参数代表资源名称
        //processDefinition.getDiagramResourceName()代表png图片资源的名称
        //processDefinition.getResourceName()代表获取bomn文件的名称
        InputStream pngInputStream = repositoryService
                .getResourceAsStream(deploymentId,processDefinition.getDiagramResourceName());
        InputStream bpmnInputStream = repositoryService
                .getResourceAsStream(deploymentId,processDefinition.getResourceName());

        //8、构建出OutputStream流
        OutputStream pngOutputStream = new FileOutputStream("E:\\temp\\"+processDefinition.getDiagramResourceName());
        OutputStream bpmnOutputStream = new FileOutputStream("E:\\temp\\"+processDefinition.getResourceName());

        //9、输入流、输出流转换
        IOUtils.copy(pngInputStream,pngOutputStream);
        IOUtils.copy(bpmnInputStream,bpmnOutputStream);

        //9、关闭流
        pngInputStream.close();
        bpmnInputStream.close();
        pngOutputStream.close();
        bpmnOutputStream.close();
    }
}
