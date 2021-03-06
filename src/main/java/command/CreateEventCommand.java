package command;

import controller.Context;
import logging.Logger;
import model.EntertainmentProvider;
import model.EventType;

public abstract class CreateEventCommand extends Object implements ICommand{
    protected Long eventNumberResult;
    protected final String title;
    protected final EventType type;

    public enum LogStatus {
        CREATE_EVENT_USER_NOT_ENTERTAINMENT_PROVIDER,
        CREATE_EVENT_USER_NOT_LOGGED_IN,
        CREATE_EVENT_NULL_INPUT
    }

    public CreateEventCommand(String title, EventType type){
        this.title = title;
        this.type = type;
        this.eventNumberResult = null;
    }
    protected boolean isUserAllowedToCreateEvent(Context context){
        LogStatus logStatus = null;
        if (context.getUserState().getCurrentUser() == null){
            logStatus = LogStatus.CREATE_EVENT_USER_NOT_LOGGED_IN;
            Logger.getInstance().logAction("CreateEventCommand.isUserAllowedToCreateEvent", LogStatus.CREATE_EVENT_USER_NOT_LOGGED_IN);
            return false;
        }
        if (!(context.getUserState().getCurrentUser() instanceof EntertainmentProvider)){
            logStatus = LogStatus.CREATE_EVENT_USER_NOT_ENTERTAINMENT_PROVIDER;
            Logger.getInstance().logAction("CreateEventCommand.isUserAllowedToCreateEvent", LogStatus.CREATE_EVENT_USER_NOT_ENTERTAINMENT_PROVIDER);
            return false;
        }
        else {
            return true;
        }
    }

    protected boolean areInputsValid(Context context){
        if (title == null || type == null){
            Logger.getInstance().logAction("CreateEventCommand.areInputsValid", LogStatus.CREATE_EVENT_NULL_INPUT);
            return false;
        }
        else return true;
    }

    @Override
    public Long getResult(){
            return eventNumberResult;
    }

}
