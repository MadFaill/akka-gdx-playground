package main.java.server;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import main.java.events.Message;
import main.java.events.Subscribe;
import main.java.events.Unsubscribe;

import java.util.HashSet;
import java.util.Set;

public class ConnectionManager extends UntypedActor {
    final static Set<ActorRef> subscribers = new HashSet<ActorRef>();

    public static Props props() {
        return Props.create(ConnectionManager.class);
    }

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof Subscribe) {
            subscribers.add(getSender());
        } else if (msg instanceof Unsubscribe) {
            subscribers.remove(getSender());
        } else {
            final ActorSelection selection = getContext().actorSelection("akka://GameServer/user/serverActor/connection:*");
            selection.tell(new Message(getSelf(), (String) msg), getSender());
//            for (ActorRef target: subscribers) {
//                target.tell(new Message(getSelf(), (String) msg), getSender());
//            }
        }
    }
}
