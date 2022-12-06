package com.plf.diary.become.salary;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * @author panlf
 * @date 2022/12/6
 */
public class StaffSystemActor {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("staffSystem");
        ActorRef actorRef = system.actorOf(Props.create(StaffSalaryActor.class),"staffActor");
        actorRef.tell("1",ActorRef.noSender());
        actorRef.tell(new Staff("Ana",1000),ActorRef.noSender());
        actorRef.tell("become3",ActorRef.noSender());
        actorRef.tell(new Staff("Maybe",2000),ActorRef.noSender());
        actorRef.tell("end",ActorRef.noSender());
        actorRef.tell("2",ActorRef.noSender());
        actorRef.tell(new Staff("Clit",3000),ActorRef.noSender());
        actorRef.tell(new Staff("Dave",4000),ActorRef.noSender());

    }
}
