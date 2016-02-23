package main.java.server;


import akka.actor.Cancellable;
import akka.actor.Props;
import main.java.utils.ScheduleActor;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class MapServer extends ScheduleActor {

    private final Cancellable mapTick = createInterval("tick", Duration.create(500, TimeUnit.MILLISECONDS),
            Duration.create(1000, TimeUnit.MILLISECONDS));

    private final Cancellable mobSpawn = createInterval("spawn", Duration.create(500, TimeUnit.MILLISECONDS),
            Duration.create(1000, TimeUnit.MILLISECONDS));

    public static Props props() {
        return Props.create(MapServer.class);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println("Recieved: " + message);
    }
}
