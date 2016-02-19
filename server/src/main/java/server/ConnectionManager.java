package main.java.server;

import akka.actor.ActorRef;
import akka.io.Tcp.Connected;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConnectionManager {

    private Map<String, ActorRef> connections;

    public ConnectionManager() {
        connections = new HashMap<String, ActorRef>();
    }

    public void addConnection(String identifier, ActorRef conn) {
        System.err.println(identifier);
        connections.put(identifier, conn);
    }

    public ActorRef getConnetcion(String identifier) {
        return connections.get(identifier);
    }

    public static String identifierFromConected(Connected connected) {
        return "/" + connected.remoteAddress().getHostName() + ":" + connected.remoteAddress().getPort();
    }

    public Set<String> connections() {
        return connections.keySet();
    }
}
