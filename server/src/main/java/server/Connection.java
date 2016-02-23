package main.java.server;

import akka.actor.ActorRef;
import akka.io.Tcp;

/**
 * Class keep connection info & reference to socket client
 */
public class Connection {
    final public Tcp.Connected connection;
    final public ActorRef client;

    public Connection(Tcp.Connected connection, ActorRef client) {
        this.connection = connection;
        this.client = client;
    }
}
