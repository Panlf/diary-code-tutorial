package com.plf.diary.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;

/**
 * 处理当前用户的任务
 *      背后操作的表
 *          act_hi_actinst  已完成的活动信息
 *          act_hi_identitylink 参与者的信息
 *          act_hi_taskinst 任务实例
 *          act_ru_execution    执行表
 *          act_ru_identitylink 参与者的信息
 *          act_ru_task 任务
 * @author Panlf
 * @date 2019/12/10
 */
public class ActivitiCompleteTask {

    public static void main(String[] args) {
        //1、得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3、处理任务，结合当前用户任务列表的查询操作的话，任务ID：7502
        taskService.complete("7502");
    }

    //李四完成任务
    /*public static void main(String[] args) {
        //1、得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3、处理任务，结合当前用户任务列表的查询操作的话，任务ID：5002
        taskService.complete("5002");
    }*/

    //张三完成任务
    /*public static void main(String[] args) {
        //1、得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3、处理任务，结合当前用户任务列表的查询操作的话，任务ID：2505
        taskService.complete("2505");
    }*/
}
