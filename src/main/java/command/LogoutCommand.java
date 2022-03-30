package command;

import controller.Context;

public class LogoutCommand extends Object implements ICommand{
    private LogStatus logStatus;

    public enum LogStatus {
        USER_LOGOUT_SUCCESS,
        USER_LOGOUT_NOT_LOGGED_IN
    }

    public LogoutCommand(){
    }

    @Override
    public void execute(Context context) {
        if (context.getUserState().getCurrentUser() == null){
            this.logStatus = LogStatus.USER_LOGOUT_NOT_LOGGED_IN;
        }
        else {
            context.getUserState().setCurrentUser(null);
            this.logStatus = LogStatus.USER_LOGOUT_SUCCESS;
        }
    }

    @Override
    public Object getResult() {
        return null;
    }
}
