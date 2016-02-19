package main.java.events;

import akka.actor.ActorRef;

public class Message {
    private String message;
    private ActorRef from;

    public Message(ActorRef from ,String message) {
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
