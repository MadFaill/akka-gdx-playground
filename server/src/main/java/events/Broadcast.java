package main.java.events;

import akka.actor.ActorRef;
import java.io.Serializable;

public class Broadcast implements Serializable {
    private static final long serialVersionUID = 1L;

    public String message;
    public ActorRef sender;

    public Broadcast(ActorRef sender ,String message) {
        this.sender = sender;
        this.message = message;
    }
}
