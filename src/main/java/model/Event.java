package model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class Event {

    private long eventNumber;
    private EntertainmentProvider organiser;
    private String title;
    private EventType type;
    private EventStatus eventStatus = EventStatus.ACTIVE;
    private Map<Long, EventPerformance> performances;

    protected Event(long eventNumber, EntertainmentProvider organiser, String title, EventType type){
        this.eventNumber = eventNumber;
        this.organiser = organiser;
        this.title = title;
        this.type = type;
        this.performances = Map.of();
    }

    public long getEventNumber(){
        return eventNumber;
    }

    public EntertainmentProvider getOrganiser(){
        return organiser;
    }

    public String getTitle(){
        return title;
    }

    public EventType getType(){
        return type;
    }

    public EventStatus getStatus(){
        return eventStatus;
    }

    public void cancel(){
        eventStatus = EventStatus.CANCELLED;
    }

    public Long createMapKey(){
        return Long.valueOf(performances.size());
    }

    public void addPerformance(EventPerformance performance){
        performances.put(createMapKey(),performance);
    }

    public EventPerformance getPerformanceByNumber(long performanceNumber){
        return performances.get(performanceNumber);
    }

    public Map<Long,EventPerformance> getPerformances(){
        return performances;
    }
}
