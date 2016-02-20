package main.java.server;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.io.Tcp.ConnectionClosed;
import akka.io.Tcp.Received;
import akka.io.TcpMessage;
import akka.util.ByteString;
import main.java.events.Message;

import java.net.InetSocketAddress;

public class SimplisticHandlerActor extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    final private Connection connection;

    public static Props props(Connection connection) {
        return Props.create(SimplisticHandlerActor.class, connection);
    }

    public SimplisticHandlerActor(Connection connection) {
        this.connection = connection;
        this.connection.subscribe(getSelf());
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof Received) {
            final String data = ((Received) msg).data().utf8String();
            final InetSocketAddress address = connection.connection.remoteAddress();
            final String prefix = address.getHostName() + ":" + address.getPort();

            log.info("In SimplisticHandlerActor - Received message: " + data);

            connection.manager.tell(prefix + ":" + data, getSender());
        } else if (msg instanceof ConnectionClosed) {
            getContext().stop(getSelf());
        } else if (msg instanceof Message) {
            connection.client.tell(
                    TcpMessage.write(ByteString.fromArray((((Message) msg).message).getBytes())),
                    getSelf()
            );
        }
    }
}