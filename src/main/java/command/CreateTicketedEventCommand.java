package command;

import controller.Context;
import logging.Logger;
import model.EntertainmentProvider;
import model.Event;
import model.EventType;

public class CreateTicketedEventCommand extends CreateEventCommand{
    private int numTickets;
    private double ticketPrice;
    private boolean requestSponsorship;
//    private LogStatus logStatus;

    public enum LogStatus{
        CREATE_TICKETED_EVENT_SUCCESS;
    }

    public CreateTicketedEventCommand(
            String title,
            EventType type,
            int numTickets,
            double ticketPrice,
            boolean requestSponsorship
    ) {
        super(title, type);
        this.numTickets = numTickets;
        this.ticketPrice = ticketPrice;
        this.requestSponsorship = requestSponsorship;
    }

    @Override
    protected boolean areInputsValid(Context context){
        if (title == null || type == null || numTickets < 1 || ticketPrice  < 0 ){ // Haven't included requestSponsorship
            // as we are not implementing this use case
            return false;
        }
        else return true;
    }

    @Override
    public void execute(Context context) {
        if (isUserAllowedToCreateEvent(context) && areInputsValid(context)){
            // Create ticketed event
            Event newEvent = context.getEventState().createTicketedEvent(
                    (EntertainmentProvider) context.getUserState().getCurrentUser(),
                    title,
                    type,
                    ticketPrice,
                    numTickets
            );
            this.eventNumberResult = newEvent.getEventNumber();
            Logger.getInstance().logAction("CreateTicketedEventCommand.execute", LogStatus.CREATE_TICKETED_EVENT_SUCCESS);
//            this.logStatus = LogStatus.CREATE_TICKETED_EVENT_SUCCESS;
            // Record creation of new ticketed event in the external entertainment provider system
            ((EntertainmentProvider) context.getUserState().getCurrentUser()).getProviderSystem().recordNewEvent(
                    eventNumberResult,
                    title,
                    numTickets);
        }
    }
}
