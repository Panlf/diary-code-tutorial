package com.plf.diary.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import java.util.List;

/**
 * 任务查询
 * @author Panlf
 * @date 2019/12/10
 */
public class ActivitiTaskQuery {

    //王五的任务  ---   任务ID 7502 -- 处理任务有用
    public static void main(String[] args) {
        //1、得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3、根据流程定义的key，负责人assignee来实现当前用户的任务列表查询
        Task task =  taskService.createTaskQuery()
                .processDefinitionKey("holiday")
                .taskAssignee("wangwu")
                .singleResult();

        //4、任务列表展示
        System.out.println("流程实例ID："+task.getProcessInstanceId());
        System.out.println("任务ID："+task.getId());
        System.out.println("任务负责人："+task.getAssignee());
        System.out.println("任务名称："+task.getName());

    }



    //李四的任务  ---   任务ID 5002 -- 处理任务有用
    /*public static void main(String[] args) {
        //1、得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3、根据流程定义的key，负责人assignee来实现当前用户的任务列表查询
        List<Task> taskList =  taskService.createTaskQuery()
                .processDefinitionKey("holiday")
                .taskAssignee("lisi")
                .list();

        //4、任务列表展示
        for (Task task:taskList) {
            System.out.println("流程实例ID："+task.getProcessInstanceId());
            System.out.println("任务ID："+task.getId());
            System.out.println("任务负责人："+task.getAssignee());
            System.out.println("任务名称："+task.getName());
        }
    }*/

    //张三的任务  ---   任务ID 2505 -- 处理任务有用
    /*public static void main(String[] args) {
        //1、得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3、根据流程定义的key，负责人assignee来实现当前用户的任务列表查询
        List<Task> taskList =  taskService.createTaskQuery()
                .processDefinitionKey("holiday")
                .taskAssignee("zhangsan")
                .list();

        //4、任务列表展示
        for (Task task:taskList) {
            System.out.println("流程实例ID："+task.getProcessInstanceId());
            System.out.println("任务ID："+task.getId());
            System.out.println("任务负责人："+task.getAssignee());
            System.out.println("任务名称："+task.getName());
        }
    }*/
}
