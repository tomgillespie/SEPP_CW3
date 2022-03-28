package command;

import controller.Context;

public class LogoutCommand implements ICommand{

    private LogStatus logstatus;

    public enum LogStatus {
        USER_LOGOUT_SUCCESS,
        USER_LOGOUT_NOT_LOGGED_IN
    }

    public LogoutCommand(){

    }

    public void execute(Context context) {
        if (controller.Context.getCurrentUser() == null){
            logstatus = LogStatus.USER_LOGOUT_NOT_LOGGED_IN;
        } //wont be an error once the context controller has been created
    }

    @Override
    public Object getResult() {
        return null;
    }

}

