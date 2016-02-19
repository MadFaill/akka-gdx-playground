package main.java.events;

import akka.actor.ActorRef;
import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 2L;

    public String message;
    public ActorRef sender;

    public Message(ActorRef sender ,String message) {
        this.sender = sender;
        this.message = message;
    }
}
