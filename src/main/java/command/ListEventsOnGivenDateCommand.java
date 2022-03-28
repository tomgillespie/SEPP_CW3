package command;

import java.time.LocalDateTime;

public class ListEventsOnGivenDateCommand extends ListEventsCommand{

    LocalDateTime searchDateTime;

    public ListEventsOnGivenDateCommand(boolean userEventsOnly, boolean activeEventsOnly, LocalDateTime searchDateTime) {
        super(userEventsOnly, activeEventsOnly);
    }

    public void execute(controller.Context context){
        //if within the dat range show return the evnt
    }

}


//stop implemtion
