package main.java;


//import java.io.*;
//import java.net.*;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;

//import com.mad.server.core.Scheduler;
//import com.mad.server.task.MapTask;
//
//import main.java.utils.TmxMapLoader;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
//import main.java.client.ClientActor;
import scala.concurrent.duration.Duration;
import main.java.schedule.ScheduleInConstructor;

import java.util.concurrent.TimeUnit;

//import java.net.InetSocketAddress;

public class ApplicationMain {
    public static void main(String[] args) throws java.io.IOException {

        ActorSystem system = ActorSystem.create("ASServer");
        final ActorSystem map = ActorSystem.create("ASMap");

//        ActorSystem system2 = ActorSystem.create("Test");
        ActorRef schedule = system.actorOf(ScheduleInConstructor.props(), "schedule");
//        ActorRef serverActor = serverActorSystem.actorOf(ServerActor.props(null), "serverActor");


        system.scheduler().scheduleOnce(
                Duration.create(5, TimeUnit.SECONDS),
                schedule, "terminate", system.dispatcher(), null);

        map.scheduler().scheduleOnce(Duration.create(10, TimeUnit.SECONDS), new Runnable() {
            @Override
            public void run() {
                map.terminate();
            }
        }, map.dispatcher());

    }
}