package com.plf.diary.activiti.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import java.util.Date;

/**
 * @author panlf
 * @date 2023/7/25
 */
public class MyErrorBoundaryDelegate3 implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("错误边界事件3。。。"+new Date());
    }
}
