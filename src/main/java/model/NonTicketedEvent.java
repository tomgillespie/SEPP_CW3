package model;

public class NonTicketedEvent extends Event{
    protected NonTicketedEvent(long eventNumber, EntertainmentProvider organiser, String title, EventType type) {
        super(eventNumber, organiser, title, type);
    }
}
