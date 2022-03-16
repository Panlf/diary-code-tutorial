package com.plf.diary;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

/**
 * 1、创建Actor
 * 2、创建Actor实例
 */
    public class ActorDemo extends UntypedAbstractActor {

    //用于接收并处理消息
    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof String){
            System.out.println("message is ===>" + message.toString());
        } else {
            unhandled(message);
        }
    }

    public static void main(String[] args) {

        ActorSystem actorSystem = ActorSystem.create("actor-demo-sys");

        ActorRef actorRef =actorSystem.actorOf(Props.create(ActorDemo.class),"actor-demo");

    }
}
