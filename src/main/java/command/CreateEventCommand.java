package command;

import controller.Context;
import model.EntertainmentProvider;
import model.EventType;

import java.util.Collection;

public abstract class CreateEventCommand extends Object implements ICommand{
    protected Long eventNumberResult;
    protected final String title;
    protected final EventType type;
    protected LogStatus logStatus;

    public enum LogStatus {
        CREATE_EVENT_USER_NOT_ENTERTAINMENT_PROVIDER,
        CREATE_EVENT_USER_NOT_LOGGED_IN
    }

    public CreateEventCommand(String title, EventType type){
        this.title = title;
        this.type = type;
    }
    protected boolean isUserAllowedToCreateEvent(Context context){
        if (context.getUserState().getCurrentUser() == null){
            logStatus = LogStatus.CREATE_EVENT_USER_NOT_LOGGED_IN;
            return false;
        }
        if (!(context.getUserState().getCurrentUser() instanceof EntertainmentProvider)){
            logStatus = LogStatus.CREATE_EVENT_USER_NOT_ENTERTAINMENT_PROVIDER;
            return false;
        }
        else {
            return true;
        }
    }
    @Override
    public Long getResult(){
        if (logStatus == LogStatus.CREATE_EVENT_USER_NOT_ENTERTAINMENT_PROVIDER || logStatus == LogStatus.CREATE_EVENT_USER_NOT_LOGGED_IN){
            return null;
        }
        else{
            return eventNumberResult;
        }
    }

}
