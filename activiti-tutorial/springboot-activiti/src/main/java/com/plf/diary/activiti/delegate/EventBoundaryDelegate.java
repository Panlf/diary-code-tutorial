package com.plf.diary.activiti.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.assertj.core.util.DateUtil;

import java.util.Date;

/**
 * @author panlf
 * @date 2023/7/23
 */
public class EventBoundaryDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println(DateUtil.formatAsDatetime(new Date())+"===>"+"EventBoundaryDelegate触发");
    }
}
