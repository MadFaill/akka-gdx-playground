package main.java.schedule;

import akka.actor.Cancellable;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class ScheduleInConstructor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final Cancellable tick = getContext().system().scheduler().schedule(
            Duration.create(500, TimeUnit.MILLISECONDS),
            Duration.create(1000, TimeUnit.MILLISECONDS),
            getSelf(), "tick", getContext().dispatcher(), null);

    public static Props props() {
        return Props.create(ScheduleInConstructor.class);
    }

    public ScheduleInConstructor() {

    }

    @Override
    public void postStop() {
        tick.cancel();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message.equals("tick")) {
            log.info("tick");
        }
        else if (message.equals("terminate")) {
            log.info("terminate");
            tick.cancel();
            getContext().system().terminate();
        }
        else {
//            getContext().system().actorSelection("akka://Test");
            unhandled(message);
        }
    }
}
