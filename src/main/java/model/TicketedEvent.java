package model;

public class TicketedEvent extends Event{
    protected TicketedEvent(long eventNumber, EntertainmentProvider organiser, String title, EventType type) {
        super(eventNumber, organiser, title, type);
    }
}
