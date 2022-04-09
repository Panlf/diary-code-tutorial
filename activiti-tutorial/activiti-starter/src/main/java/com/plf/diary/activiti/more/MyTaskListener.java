package com.plf.diary.activiti.more;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 *
 * 监听器分配
 * @author Panlf
 * @date 2019/12/11
 */
public class MyTaskListener implements TaskListener {

    public void notify(DelegateTask delegateTask) {
        //这里指定负责人
        delegateTask.setAssignee("zhangsan");
    }
}
