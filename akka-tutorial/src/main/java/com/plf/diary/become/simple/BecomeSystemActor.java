package com.plf.diary.become.simple;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * @author panlf
 * @date 2022/12/6
 */
public class BecomeSystemActor {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("becomeSystem");
        ActorRef actorRef = system.actorOf(Props.create(BecomeActor.class),"becomeActor");
        actorRef.tell("hello",ActorRef.noSender());
        actorRef.tell("hi",ActorRef.noSender());
        actorRef.tell("akka",ActorRef.noSender());
    }
}
