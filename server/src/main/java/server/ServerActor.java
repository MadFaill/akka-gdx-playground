package main.java.server;

import java.net.InetSocketAddress;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.io.Tcp;
import akka.io.Tcp.Bound;
import akka.io.Tcp.CommandFailed;
import akka.io.Tcp.Connected;
import akka.io.TcpMessage;
import main.java.events.Broadcast;
import main.java.events.Message;

public class ServerActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private InetSocketAddress inetAddr;
    private final static ConnectionManager manager = new ConnectionManager();

    public static Props props(InetSocketAddress addr) {
        return Props.create(ServerActor.class, addr);
    }

    public ServerActor(InetSocketAddress addr) { this.inetAddr= addr; }

    @Override
    public void preStart() throws Exception {
        ActorRef tcpActor = Tcp.get(getContext().system()).manager();
        tcpActor.tell(TcpMessage.bind(getSelf(), inetAddr, 100), getSelf());
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof Bound) {
            log.info("In ServerActor - received message: bound");
        } else if (msg instanceof CommandFailed) {
            getContext().stop(getSelf());
        } else if (msg instanceof Connected) {
            log.info("In ServerActor - received message: connected");

            final Connected conn = (Connected) msg;
            final ActorRef handler = getContext().actorOf(SimplisticHandlerActor.props(getSelf()));

            manager.addConnection(ConnectionManager.identifierFromConected(conn), handler);

            getSender().tell(TcpMessage.register(handler), getSelf());
        } else if (msg instanceof Broadcast) {
            final Broadcast bc = (Broadcast) msg;

            for (String connName: manager.connections()) {
                final ActorRef client = manager.getConnetcion(connName);
                if (client != bc.getFrom()) {
                    client.tell(new Message(getSelf(), bc.getMessage()), bc.getFrom());
                }
            }
        }
    }
}