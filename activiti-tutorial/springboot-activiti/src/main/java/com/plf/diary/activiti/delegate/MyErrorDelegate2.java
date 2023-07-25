package com.plf.diary.activiti.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import java.util.Date;

/**
 * @author panlf
 * @date 2023/7/25
 */
public class MyErrorDelegate2 implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("错误开始事件2。。。"+new Date());
        
    }
}
