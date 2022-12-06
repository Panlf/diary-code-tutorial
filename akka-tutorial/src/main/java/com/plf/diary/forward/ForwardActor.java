package com.plf.diary.forward;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

public class ForwardActor extends UntypedAbstractActor {
    private final ActorRef targetActor = getContext().actorOf(Props.create(TargetActor.class),"targetActor");

    @Override
    public void onReceive(Object message) throws Throwable {
        System.out.println("ForwardActor get message!");
        targetActor.forward(message,getContext());
    }
}
