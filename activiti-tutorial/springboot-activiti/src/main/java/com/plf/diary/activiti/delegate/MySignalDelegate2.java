package com.plf.diary.activiti.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 自定义的委托类
 * @author panlf
 * @date 2023/7/22
 */
public class MySignalDelegate2 implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("MySignalDelegate2====>"+execution.getEventName());
    }
}
