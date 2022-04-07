package com.plf.diary.test;

import com.plf.diary.drools.entity.ComparisonOperatorEntity;
import com.plf.diary.drools.entity.Order;
import com.plf.diary.drools.entity.Student;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class DroolsTest {

    private KieSession session = null;

    @BeforeEach
    public void getKieSession() {
        // 设置日期格式
        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm");
        KieServices kieServices = KieServices.Factory.get();

        //获得Kie容器对象
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        //从Kie容器对象中获取会话对象
        session = kieContainer.newKieSession();
    }

    @Test
    public void test1() {
        //Fact对象 事实对象
        Order order = new Order();
        order.setOriginalPrice(80d);

        //将Order对象插入到工作内存中
        session.insert(order);

        //激活规则，由Drools框架自动进行规则匹配，如果规则匹配陈功，则执行
        session.fireAllRules();

        System.out.println("优惠后价格:" + order.getRealPrice());
    }

    @Test
    public void test2() {
        ComparisonOperatorEntity entity = new ComparisonOperatorEntity();
        entity.setNames("王大毛");
        entity.setList(Arrays.asList("王小毛", "王中王"));

        session.insert(entity);

        // 激活规则 使用框架提供的规则过滤器执行指定规则
        //session.fireAllRules(new RuleNameEqualsAgendaFilter("rule_comparison_matches"));
        session.fireAllRules(new RuleNameStartsWithAgendaFilter("rule_"));
    }

    @Test
    public void test3() {

        Student student = new Student();
        student.setId(1l);
        student.setName("Vivi");
        student.setAge(10);

        session.insert(student);

        session.fireAllRules();
    }

    @Test
    public void test4() {
        //指定组获得焦点
        session.getAgenda().getAgendaGroup("agenda_group_2").setFocus();
        session.fireAllRules();
    }

    @Test
    public void test5() {
        new Thread(() -> {
            //启动规则引擎进行规则匹配，直到调用Halt方法才结束规则引擎
            session.fireUntilHalt();
        }).start();

        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        session.halt();
    }

    @Test
    public void test6() {
        session.fireAllRules();
    }

    @Test
    public void test7() {
        //设置全局变量，变量名称必须和规则文件一致
        session.setGlobal("count", 5);
        List<String> list = new ArrayList<>();
        list.add("rule");
        session.setGlobal("gList", list);
        session.fireAllRules(new RuleNameStartsWithAgendaFilter("rule_global"));
    }

    @Test
    public void test8() {
        Student s = new Student();
        s.setAge(100);
        s.setName("John");
        session.insert(s);

        QueryResults results = session.getQueryResults("query_1");
        int size = results.size();

        System.out.println("1-符合条件的Fact对象个数:" + size);

        results = session.getQueryResults("query_2","John");
        size = results.size();
        System.out.println("2-符合条件的Fact对象个数:" + size);
    }

    @Test
    public void test9() {
        Student s = new Student();
        s.setAge(28);
        s.setName("JYY");
        session.insert(s);
        session.fireAllRules(new RuleNameStartsWithAgendaFilter("rule_function"));
    }

    @Test
    public void test910() {
        session.fireAllRules(new RuleNameStartsWithAgendaFilter("rule_rhs"));
    }

    @AfterEach
    public void disposeSession() {
        session.dispose();
    }

}
