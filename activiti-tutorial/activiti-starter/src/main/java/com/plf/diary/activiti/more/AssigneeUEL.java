package com.plf.diary.activiti.more;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * 启动流程实例，动态设置assignee
 * @author Panlf
 * @date 2019/12/11
 */
public class AssigneeUEL {
    public static void main(String[] args) {
        //1、得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、得到RunService
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //3、设置assignee的取值，用户可以在界面上设置流程的执行人
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("assignee0","zhangsan");
        map.put("assignee1","lisi");
        map.put("assignee2","wangwu");

        //4、启动流程实例，同时还要设置流程定义的assignee的值
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("holiday2",map);

        System.out.println(processInstance.getName());
    }
}
