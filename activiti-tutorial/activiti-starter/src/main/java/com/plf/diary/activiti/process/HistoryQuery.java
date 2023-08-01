package com.plf.diary.activiti.process;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;

import java.util.List;

/**
 * 历史流程查看
 * @author Panlf
 * @date 2019/12/11
 */
public class HistoryQuery {
    public static void main(String[] args) {
        //1、得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2、得到HistoryService
        HistoryService historyService = processEngine.getHistoryService();

        //3、HistoricActivityInstanceQuery
        HistoricActivityInstanceQuery historicActivityInstanceQuery =
                historyService.createHistoricActivityInstanceQuery();

        //设置流程实例ID
        historicActivityInstanceQuery.processInstanceId("2501");

        //4、执行
        List<HistoricActivityInstance> historicActivityInstanceList = historicActivityInstanceQuery.list();

        //5、遍历查询结果
        for(HistoricActivityInstance historicActivityInstance:historicActivityInstanceList){
            System.out.println(historicActivityInstance.getActivityId());
            System.out.println(historicActivityInstance.getActivityName());
            System.out.println(historicActivityInstance.getAssignee());
            System.out.println(historicActivityInstance.getProcessInstanceId());
            System.out.println("========================");

        }
    }
}
