package main.java.server;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.io.Tcp;
import akka.io.Tcp.ConnectionClosed;
import akka.io.Tcp.Received;
import akka.io.TcpMessage;
import akka.util.ByteString;
import main.java.events.Message;
import main.java.events.Subscribe;

public class SimplisticHandlerActor extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    final private Tcp.Connected connection;
    final private ActorRef router;
    final private ActorRef sender;


    public static Props props(Tcp.Connected conn, ActorRef tcpSender) {
        return Props.create(SimplisticHandlerActor.class, conn, tcpSender);
    }

    public SimplisticHandlerActor(Tcp.Connected conn, ActorRef tcpSender) {
        connection = conn;
        sender = tcpSender;
        router = getContext().actorOf(CustomRouter.props());
        router.tell(new Subscribe(), getSelf());
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof Received) {
            final String data = ((Received) msg).data().utf8String();
            log.info("In SimplisticHandlerActor - Received message: " + data);
            router.tell(data, getSender());
        } else if (msg instanceof ConnectionClosed) {
            getContext().stop(getSelf());
        } else if (msg instanceof Message) {
            sender.tell(
                    TcpMessage.write(ByteString.fromArray(("I say: "+((Message) msg).message).getBytes())),
                    getSelf()
            );
        }
    }
}