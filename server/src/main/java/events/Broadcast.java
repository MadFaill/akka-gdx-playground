package main.java.events;

import akka.actor.ActorRef;

public class Broadcast {
    private String message;
    private ActorRef from;

    public Broadcast(ActorRef from ,String message) {
        this.from = from;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ActorRef getFrom() {
        return from;
    }
}
