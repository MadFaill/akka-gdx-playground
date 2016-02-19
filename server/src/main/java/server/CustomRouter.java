package main.java.server;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import main.java.events.Message;
import main.java.events.Subscribe;
import main.java.events.Unsubscribe;

import java.util.HashSet;
import java.util.Set;

public class CustomRouter extends UntypedActor {
    final static Set<ActorRef> subscribers = new HashSet<ActorRef>();

    public static Props props() {
        return Props.create(CustomRouter.class);
    }

    @Override
    public void onReceive(Object msg) {
        System.err.println("INFO: " + subscribers);

        if (msg instanceof Subscribe) {
            subscribers.add(getSender());
        } else if (msg instanceof Unsubscribe) {
            subscribers.remove(getSender());
        } else {
            for (ActorRef target: subscribers) {
                System.out.println("tell to " + target);
                target.tell(new Message(getSelf(), (String) msg), getSender());
            }
        }
    }
}
