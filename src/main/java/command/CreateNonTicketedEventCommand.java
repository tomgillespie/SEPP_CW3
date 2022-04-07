package command;

import controller.Context;
import logging.Logger;
import model.EntertainmentProvider;
import model.Event;
import model.EventType;

public class CreateNonTicketedEventCommand extends CreateEventCommand {
//    private LogStatus logStatus;

    public CreateNonTicketedEventCommand(String title, EventType type) {
        super(title, type);
    }

    public enum LogStatus{
        CREATE_NON_TICKETED_EVENT_SUCCESS
    }

    @Override
    public void execute(Context context) {
        if (isUserAllowedToCreateEvent(context) && areInputsValid(context)) {
            // Create non ticketed event
            Event newEvent = context.getEventState().createNonTicketedEvent(
                    (EntertainmentProvider) context.getUserState().getCurrentUser(),
                    title,
                    type
            );
            this.eventNumberResult = newEvent.getEventNumber();
//            this.logStatus = LogStatus.CREATE_NON_TICKETED_EVENT_SUCCESS;
            Logger.getInstance().logAction("CreateNonTicketedEvent.execute", LogStatus.CREATE_NON_TICKETED_EVENT_SUCCESS);
            // Record creation of new non ticketed event in the external entertainment provider system
            ((EntertainmentProvider) context.getUserState().getCurrentUser()).getProviderSystem().recordNewEvent(
                    eventNumberResult,
                    title,
                    0);
        }
    }
}
