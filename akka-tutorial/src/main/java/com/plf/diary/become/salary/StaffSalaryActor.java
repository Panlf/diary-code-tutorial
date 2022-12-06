package com.plf.diary.become.salary;

import akka.actor.UntypedAbstractActor;
import akka.japi.Procedure;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * @author panlf
 * @date 2022/12/6
 */
public class StaffSalaryActor extends UntypedAbstractActor {
    Receive LEVEL3 = ReceiveBuilder.create().matchAny(message->{
        if(message instanceof String){
            if(((String) message).equalsIgnoreCase("end")){
                getContext().unbecome();
            }
        } else {
            Staff staff = (Staff) message;
            double finalSalary = staff.getSalary() * 3;
            System.out.println("员工 : " + staff.getName() + ", 最终薪资 : "+finalSalary);
        }
    }).build();
    Receive LEVEL1 = ReceiveBuilder.create().matchAny(message->{
        if(message instanceof String){
            if(((String) message).equalsIgnoreCase("end")){
                getContext().unbecome();
            } else if(((String) message).equalsIgnoreCase("become3")){
                // 如果是 false 则 无法 切换到 onReceive里面
                // unbecome 失效
                getContext().become(LEVEL3,true);
            }
        } else {
            Staff staff = (Staff) message;
            double finalSalary = staff.getSalary() * 1;
            System.out.println("员工 : " + staff.getName() + ", 最终薪资 : "+finalSalary);
        }
    }).build();
    Receive LEVEL2 = ReceiveBuilder.create().matchAny(message->{
        if(message instanceof String){
            if(((String) message).equalsIgnoreCase("end")){
                getContext().unbecome();
            }
        } else {
            Staff staff = (Staff) message;
            double finalSalary = staff.getSalary() * 2;
            System.out.println("员工 : " + staff.getName() + ", 最终薪资 : "+finalSalary);
        }
    }).build();


    /*Procedure<Object> LEVEL1 = message ->{
        if(message instanceof String){
            if(((String) message).equalsIgnoreCase("end")){
                getContext().unbecome();
            }
        } else {
            Staff staff = (Staff) message;
            double finalSalary = staff.getSalary() * 1.2;
            System.out.println("员工 : " + staff.getName() + ", 最终薪资 : "+finalSalary);
        }
    };

    Procedure<Object> LEVEL2 = message ->{
        if(message instanceof String){
            if(((String) message).equalsIgnoreCase("end")){
                getContext().unbecome();
            }
        } else {
            Staff staff = (Staff) message;
            double finalSalary = staff.getSalary() * 1.5;
            System.out.println("员工 : " + staff.getName() + ", 最终薪资 : "+finalSalary);
        }
    };
*/



    @Override
    public void onReceive(Object message) throws Throwable {
        String level = (String) message;
        if(level.equals("1")){
            System.out.println("准备跳转到Level1");
            this.getContext().become(LEVEL1);
        }else if(level.equals("2")){
            System.out.println("准备跳转到Level2");
            this.getContext().become(LEVEL2);
        }
    }
}
