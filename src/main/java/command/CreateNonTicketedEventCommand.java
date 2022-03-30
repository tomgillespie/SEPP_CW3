package command;

import controller.Context;
import model.EntertainmentProvider;
import model.EventType;

public class CreateNonTicketedEventCommand extends CreateEventCommand {
    public CreateNonTicketedEventCommand(String title, EventType type) {
        super(title, type);
    }

    @Override
    public void execute(Context context) {
        if (isUserAllowedToCreateEvent(context)) {
            context.getEventState().createNonTicketedEvent((EntertainmentProvider) context.getUserState().getCurrentUser(), title, type);
        }
    }
}
