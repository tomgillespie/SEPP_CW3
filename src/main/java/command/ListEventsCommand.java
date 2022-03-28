package command;

import model.Event;
import model.EventStatus;
import java.util.List;

public class ListEventsCommand extends Object implements ICommand{

        boolean userEventsOnly ;
        boolean activeEventOnly;
        model.Event eventListResults;
        LogStatus logstatus;

        public enum LogStatus {
            LIST_USER_EVENTS_SUCCESS, LIST_USER_EVENTS_NOT_LOGGED_IN
        }



    public ListEventsCommand(boolean userEventsOnly, boolean activeEventsOnly){
        this.userEventsOnly = userEventsOnly;
        this.activeEventOnly = activeEventsOnly;
    }

    private static List<Event> filterEvents(List<Event> events, Boolean activeEventsOnly){
        if (activeEventsOnly == true) {
            if (model.Event.getStatus() == EventStatus.ACTIVE){
                return events;
                //does the logic of if event is active return events make sense
            }
            return null;
        }
        else
        {
            return events;
        }



        //might need to add how to filter by event created by each providor

    }

    private static List<Event> eventSatisfiesPreferences(model.ConsumerPreferences preferences , List<Event> events) {
        if (preferences == Event.getPerformances()) {
            //might have to get event and performances by number then iterate round
            return events;
        } else { return null;}
    }

    public void execute(controller.Context context){
            if(/*add if logged in*/){logstatus = LogStatus.LIST_USER_EVENTS_SUCCESS;}
            else{logstatus = LogStatus.LIST_USER_EVENTS_NOT_LOGGED_IN;}
    }

    public Object getResult() {
        if (logstatus == LogStatus.LIST_USER_EVENTS_SUCCESS) {
            return eventListResults;
        } else {
            return null;
        }

    }
}
