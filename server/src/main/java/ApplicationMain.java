package main.java;

import akka.actor.ActorSystem;
import main.java.server.MapServer;
import main.java.server.ServerActor;

import java.net.InetSocketAddress;

public class ApplicationMain {

    final public static ActorSystem ServerSystem = ActorSystem.create("GameServer");

    public static void main(String[] args) throws java.io.IOException {

//        ServerSystem.actorOf(MapServer.props(), "MapServer");

        // init TCP actor
        InetSocketAddress inetAddr = new InetSocketAddress("localhost", 9090);
        ServerSystem.actorOf(ServerActor.props(inetAddr), "serverActor");

    }
}