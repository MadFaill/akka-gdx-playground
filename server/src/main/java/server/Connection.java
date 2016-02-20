package main.java.server;

import akka.actor.ActorRef;
import akka.io.Tcp;
import main.java.events.Subscribe;
import main.java.events.Unsubscribe;

/**
 * Class keep connection info & reference to socket client
 */
public class Connection {
    final public Tcp.Connected connection;
    final public ActorRef client;

    // храним менеджера тут, для удобства доступа ко всем клиентам
    final public ActorRef manager;

    public Connection(Tcp.Connected connection, ActorRef client, ActorRef manager) {
        this.connection = connection;
        this.client = client;
        this.manager = manager;
    }

    public void subscribe(ActorRef handler) {
        manager.tell(new Subscribe(), handler);
    }

    public void unsubscribe(ActorRef handler) {
        manager.tell(new Unsubscribe(), handler);
    }
}
