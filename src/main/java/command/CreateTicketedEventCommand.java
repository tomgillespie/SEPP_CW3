package command;

import controller.Context;
import model.EntertainmentProvider;
import model.Event;
import model.EventType;

public class CreateTicketedEventCommand extends CreateEventCommand{
    private int numTickets;
    private double ticketPrice;
    private boolean requestSponsorship;
    private LogStatus logStatus;

    public enum LogStatus{
        CREATE_TICKETED_EVENT_SUCCESS;
    }

    public CreateTicketedEventCommand(String title, EventType type, int numTickets,double ticketPrice, boolean requestSponsorship) {
        super(title, type);
        this.numTickets = numTickets;
        this.ticketPrice = ticketPrice;
        this.requestSponsorship = requestSponsorship;
    }

    @Override
    public void execute(Context context) {
        if (isUserAllowedToCreateEvent(context)){
            Event newEvent = context.getEventState().createTicketedEvent((EntertainmentProvider) context.getUserState().getCurrentUser(), title, type, ticketPrice, numTickets);
            this.eventNumberResult = newEvent.getEventNumber();
            this.logStatus = LogStatus.CREATE_TICKETED_EVENT_SUCCESS;
        }
    }
}
