package main.java.server;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.io.Tcp.ConnectionClosed;
import akka.io.Tcp.Received;
import akka.io.TcpMessage;
import akka.util.ByteString;
import main.java.events.Broadcast;
import main.java.events.Message;

public class SimplisticHandlerActor extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorRef server;

    public static Props props(ActorRef server) {
        return Props.create(SimplisticHandlerActor.class, server);
    }

    public SimplisticHandlerActor(ActorRef server) {
        this.server = server;
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof Received) {
            final String data = ((Received) msg).data().utf8String();
            log.info("In SimplisticHandlerActor - Received message: " + data);
//            getSender().tell(TcpMessage.write(ByteString.fromArray(("echo "+data).getBytes())), getSelf());
            server.tell(new Broadcast(getSelf(), data), getSelf());
        } else if (msg instanceof ConnectionClosed) {
            getContext().stop(getSelf());
        } else if (msg instanceof Message) {
            getSender().tell(
                    TcpMessage.write(
                            ByteString.fromArray((((Message) msg).getMessage()).getBytes())
                    ),
                    getSelf());
        }
    }
}