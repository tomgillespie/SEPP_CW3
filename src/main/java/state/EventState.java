package state;

import model.*;

import java.time.LocalDateTime;
import java.util.List;

public class EventState implements IEventState{
    private long nextEventNumber;
    private long nextPerformanceNumber;
    private List<Event> events;
    public EventState(){
        this.events = null;
        this.nextEventNumber = 1;
        this.nextPerformanceNumber = 1;
    }
    public EventState(IEventState other){
        new EventState();
        // Deep copy
    }

    @Override
    public List<Event> getAllEvents() {
        return events;
    }

    @Override
    public Event findEventByNumber(long eventNumber) {
        Event correctEvent = null;
        for (int i = 0; i < events.size(); i++){
            if (events.get(i).getEventNumber() == eventNumber){
                correctEvent = events.get(i);
            }
        }
        return correctEvent;
    }

    @Override
    public NonTicketedEvent createNonTicketedEvent(EntertainmentProvider organiser, String title, EventType type) {
        NonTicketedEvent newNonTicketedEvent = new NonTicketedEvent(nextEventNumber, organiser, title, type);
        nextEventNumber = nextEventNumber + 1;
        return newNonTicketedEvent;
    }

    @Override
    public TicketedEvent createTicketedEvent(EntertainmentProvider organiser, String title, EventType type, double ticketPrice, int numTickets) {
        TicketedEvent newTicketedEvent = new TicketedEvent(nextEventNumber, organiser, title, type, ticketPrice, numTickets);
        nextEventNumber = nextEventNumber + 1;
        return newTicketedEvent;
    }

    @Override
    public EventPerformance createEventPerformance(Event event, String venueAddress, LocalDateTime startDateTime, LocalDateTime endDateTime, List<String> performerNames, boolean hasSocialDistancing, boolean hasAirFiltration, boolean isOutdoors, int capacityLimit, int venueSize) {
        EventPerformance newEventPerformance = new EventPerformance(nextPerformanceNumber, event, venueAddress, startDateTime, endDateTime, performerNames, hasSocialDistancing, hasAirFiltration, isOutdoors, capacityLimit, venueSize);
        nextPerformanceNumber = nextPerformanceNumber + 1;
        return newEventPerformance;
    }
}
