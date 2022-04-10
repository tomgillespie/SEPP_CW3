package command;

import controller.Context;
import logging.Logger;

public class LogoutCommand extends Object implements ICommand{

    public enum LogStatus {
        USER_LOGOUT_SUCCESS,
        USER_LOGOUT_NOT_LOGGED_IN
    }

    public LogoutCommand(){
    }

    @Override
    public void execute(Context context) {
        LogStatus logStatus = null;
        if (context.getUserState().getCurrentUser() == null){
            logStatus = LogStatus.USER_LOGOUT_NOT_LOGGED_IN;
            Logger.getInstance().logAction("LogoutCommand.execute", LogStatus.USER_LOGOUT_NOT_LOGGED_IN);
        }
        else {
            context.getUserState().setCurrentUser(null);
            logStatus = LogStatus.USER_LOGOUT_SUCCESS;
            Logger.getInstance().logAction("LogoutCommand.execute", LogStatus.USER_LOGOUT_SUCCESS);
        }
    }

    @Override
    public Object getResult() {
        return null;
    }
}
