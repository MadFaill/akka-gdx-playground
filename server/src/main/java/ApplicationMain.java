package main.java;

import akka.actor.ActorSystem;
import main.java.server.CustomRouter;
import main.java.server.ServerActor;
import scala.concurrent.duration.Duration;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class ApplicationMain {

    final public static ActorSystem ServerSystem = ActorSystem.create("GameServer");

    public static void main(String[] args) throws java.io.IOException {

        // create default router
        ServerSystem.actorOf(CustomRouter.props(), "ConnectionRouter");

        // ActorRef schedule = ServerSystem.actorOf(ScheduleInConstructor.props(), "schedule");

        // init TCP actor
        InetSocketAddress inetAddr = new InetSocketAddress("localhost", 9090);
        ServerSystem.actorOf(ServerActor.props(inetAddr), "serverActor");

    }

    public static void terminate(long seconds) {
        ServerSystem.scheduler().scheduleOnce(
                Duration.create(seconds, TimeUnit.SECONDS),
                new Runnable() {
                    @Override
                    public void run() {
                        ServerSystem.terminate();
                    }
                }, ServerSystem.dispatcher());
    }
}