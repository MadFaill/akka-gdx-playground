package main.java.server;

import java.net.InetSocketAddress;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.io.Tcp;
import akka.io.Tcp.CommandFailed;
import akka.io.Tcp.Connected;
import akka.io.TcpMessage;

public class ServerActor extends UntypedActor {

    private InetSocketAddress inetAddr;

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
        if (msg instanceof Connected) {
            final Connected conn = (Connected) msg;
            final Connection connection = new Connection(conn, getSender());
            final ActorRef handler = getContext().actorOf(
                    SimplisticHandlerActor.props(connection), "connection:"+conn.remoteAddress().getPort());

            getSender().tell(TcpMessage.register(handler), getSelf());
        } else if (msg instanceof CommandFailed) {
            getContext().stop(getSelf());
        } else if (msg instanceof Tcp.Bound) {
            // pass
        } else {
            unhandled(msg);
        }
    }
}