package com.plf.diary.become.simple;

import akka.actor.UntypedAbstractActor;
import akka.japi.Procedure;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;


/**
 * @author panlf
 * @date 2022/12/6
 */
public class BecomeActor extends UntypedAbstractActor {
/*
    Procedure<Object> procedure = message -> System.out.println("become : "+message);
*/
    Receive receive = ReceiveBuilder.create().matchAny(message->{
        System.out.println("receive become : "+message);
    }).build();



    @Override
    public void onReceive(Object message) throws Throwable {
        System.out.println("接收到消息 : "+message);
        getContext().become(receive);
    }

}
