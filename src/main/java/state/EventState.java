package state;

import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventState implements IEventState{
    private long nextEventNumber;
    private long nextPerformanceNumber;
    private ArrayList<Event> events;
    public EventState(){
        this.events = new ArrayList<>();
        this.nextEventNumber = 1;
        this.nextPerformanceNumber = 1;
    }
    public EventState(IEventState other){
        this.events = new ArrayList<Event>(other.getAllEvents());
        this.nextPerformanceNumber = other.getNextPerformanceNumber();
        this.nextEventNumber = other.getNextEventNumber();
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
        this.events.add(newNonTicketedEvent);
        nextEventNumber = nextEventNumber + 1;
        return newNonTicketedEvent;
    }

    @Override
    public TicketedEvent createTicketedEvent(EntertainmentProvider organiser, String title, EventType type, double ticketPrice, int numTickets) {
        TicketedEvent newTicketedEvent = new TicketedEvent(nextEventNumber, organiser, title, type, ticketPrice, numTickets);
        this.events.add(newTicketedEvent);
        nextEventNumber = nextEventNumber + 1;
        return newTicketedEvent;
    }

    @Override
    public EventPerformance createEventPerformance(Event event, String venueAddress, LocalDateTime startDateTime, LocalDateTime endDateTime, List<String> performerNames, boolean hasSocialDistancing, boolean hasAirFiltration, boolean isOutdoors, int capacityLimit, int venueSize) {
        EventPerformance newEventPerformance = new EventPerformance(nextPerformanceNumber, event, venueAddress, startDateTime, endDateTime, performerNames, hasSocialDistancing, hasAirFiltration, isOutdoors, capacityLimit, venueSize);
        event.addPerformance(newEventPerformance);
        nextPerformanceNumber = nextPerformanceNumber + 1;
        return newEventPerformance;
    }

    @Override
    public long getNextEventNumber() {
        return nextEventNumber;
    }

    @Override
    public long getNextPerformanceNumber() {
        return nextPerformanceNumber;
    }
}
