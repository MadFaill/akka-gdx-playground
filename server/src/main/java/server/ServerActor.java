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

public class ServerActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private InetSocketAddress inetAddr;
    final private ActorRef connectionManager = getContext().actorOf(ConnectionManager.props());

    public static Props props(InetSocketAddress addr) {
        return Props.create(ServerActor.class, addr);
    }

    public ServerActor(InetSocketAddress addr) {
        this.inetAddr= addr;
    }

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
            final Connection connection = new Connection(conn, getSender(), connectionManager);
            final ActorRef handler = getContext().actorOf(SimplisticHandlerActor.props(connection));

            getSender().tell(TcpMessage.register(handler), getSelf());
        }
    }
}