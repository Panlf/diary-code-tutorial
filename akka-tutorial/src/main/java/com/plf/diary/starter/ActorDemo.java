package com.plf.diary.starter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

/**
 * 1、创建Actor
 * 2、创建Actor实例
 */
    public class ActorDemo extends UntypedAbstractActor {

    //用于接收并处理消息
    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof String){

            System.out.println("message is ===>" + message);

            Thread.sleep(3000);

            // 返回数据
            getSender().tell("hi, i receive the ["+message+"]",getSelf());

        } else {
            unhandled(message);
        }
    }

    public static void main(String[] args) {

        ActorSystem actorSystem = ActorSystem.create("actor-demo-sys");

        ActorRef actorRef =actorSystem.actorOf(Props.create(ActorDemo.class),"actor-demo");

        /**
         * 第一个参数 消息 【任何序列化的数据或对象】
         * 第二个参数 发送者 通常来讲是另外一个Actor的引用
         *
         * tell方式
         * 异步处理该消息 发完立即返回
         */
        actorRef.tell("hello akka!",ActorRef.noSender());

        /**
         * ask方式
         * 异步方式 期待得到一个返回结果。假如在设置的Timeout内没有得到返回结果。发送方收到一个超时异常
         */
        // 构建超时时间
        Timeout timeout = new Timeout(Duration.create(2,"seconds"));

        Future<Object> future = Patterns.ask(actorRef,"Akka ask",timeout);

        future.onComplete((message)->{
            if(message.isFailure()){
                System.out.println("future receive timeout,get exception is "+message.failed().get().getMessage());
            }
            if(message.isSuccess()){
                System.out.println("future receive message is "+message);
            }
            return message.get();
        }, actorSystem.dispatcher());
    }
}
