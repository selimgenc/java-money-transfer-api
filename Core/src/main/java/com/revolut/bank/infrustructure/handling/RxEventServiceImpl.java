package com.revolut.bank.infrustructure.handling;

import com.revolut.bank.application.events.Event;
import com.revolut.bank.application.events.EventHandlerService;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.PublishProcessor;

import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
@Singleton
public class RxEventServiceImpl implements EventHandlerService {
    private static final Logger logger = Logger.getLogger(RxEventServiceImpl.class.getName());

    private static final Map<Class, PublishProcessor> subjects = new ConcurrentHashMap<>();

    public RxEventServiceImpl() {
        RxJavaPlugins.setErrorHandler(e -> {
            logger.log(Level.SEVERE, "Error Happened while processind event.", e);
        });
    }

    @Override
    public void send(Event event) {
        if(subjects.containsKey(event.getType()))
        {
            PublishProcessor subject = subjects.get(event.getType());
            try {
                subject.onNext(event);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error on consuming event", e);
            }
        }else {
            logger.log(Level.FINE, "No handler found for event {}", event.getType());
        }

    }

    public <E extends Event> void register(Class<E> eventType, Consumer<Event> consumer){
        if(!subjects.containsKey(eventType)){
            PublishProcessor<E> subjectForEventType = PublishProcessor.create();
            subjects.put(eventType, subjectForEventType);
        }

        PublishProcessor subject = subjects.get(eventType);
        subject.subscribe(consumer, throwable -> {
            register(eventType, consumer);
        });

        logger.log(Level.FINE, "Subscriber {0} registered for event {1} ", new String[]{subject.getClass().getSimpleName(), eventType.getSimpleName()});

    }

}
