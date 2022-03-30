package command;

import controller.Context;

public abstract class UpdateProfileCommand extends Object implements ICommand {
    protected Boolean successResult;
    private LogStatus logStatus;

    public
    enum LogStatus {
        USER_UPDATE_PROFILE_NOT_LOGGED_IN,
        USER_UPDATE_PROFILE_WRONG_PASSWORD,
        USER_UPDATE_PROFILE_EMAIL_ALREADY_IN_USE
    }

    UpdateProfileCommand() {
    }

    protected boolean isProfileUpdateInvalid(Context context, String oldPassword, String newEmail) {
        if (context.getUserState().getCurrentUser() == null) {
            logStatus = LogStatus.USER_UPDATE_PROFILE_NOT_LOGGED_IN;
        }
        if (context.getUserState().getCurrentUser().checkPasswordMatch(oldPassword) == false) {
            logStatus = LogStatus.USER_UPDATE_PROFILE_WRONG_PASSWORD;
        }
        for (int i = 0; i < context.getUserState().getAllUsers().size(); i++) {
            if (context.getUserState().getAllUsers().get(i).getEmail() == newEmail) {
                logStatus = LogStatus.USER_UPDATE_PROFILE_EMAIL_ALREADY_IN_USE;
            }
        }
        if (logStatus == LogStatus.USER_UPDATE_PROFILE_EMAIL_ALREADY_IN_USE || logStatus == LogStatus.USER_UPDATE_PROFILE_WRONG_PASSWORD
                || logStatus == LogStatus.USER_UPDATE_PROFILE_NOT_LOGGED_IN ){
            successResult = false;
            return false;
        }
        else{
            //what is success result
            successResult = true;
            return true;
        }
    }

    public Boolean getResult () {
        if(successResult = false){
            return Boolean.FALSE;
        }
        else{return Boolean.TRUE;}
            }
        }
