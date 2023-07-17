package com.plf.diary.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author panlf
 * @date 2023/7/15
 */
public class MyFirstListener implements TaskListener {
    private static final long serialVersionUID = 2953832474596635885L;

    /**
     * create  创建：当任务已经创建，并且所有任务参数都已经设置时触发
     * assignment 指派：当任务已经指派给某人时触发。请注意：当流程执行到达用户任务时，create事件触发前，首先触发assignment事件。这看起来不是自然顺序，但是有实际原因的：当收到create事件时，我们通常希望查看任务的所有参数，包括办理人。
     * complete 完成：当任务已经完成，从运行时数据中删除前触发。
     * delete 删除：在任务即将被删除前触发。请注意当任务通过completeTask正常完成时也会触发
     */



    @Override
    public void notify(DelegateTask delegateTask) {
        if(EVENTNAME_CREATE.equals(delegateTask.getEventName())){
            delegateTask.setAssignee("wangwu");
        }
    }
}
