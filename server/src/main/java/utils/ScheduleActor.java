package main.java.utils;

import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import scala.concurrent.duration.FiniteDuration;

public abstract class ScheduleActor extends UntypedActor {

    protected Cancellable createInterval(String message, FiniteDuration start, FiniteDuration duration) {
        return getContext().system().scheduler().schedule(start, duration,
                getSelf(), message, getContext().dispatcher(), null);
    }
}
