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
            this.logStatus = LogStatus.USER_UPDATE_PROFILE_NOT_LOGGED_IN;
        }
        if (context.getUserState().getCurrentUser().checkPasswordMatch(oldPassword) == false) {
            this.logStatus = LogStatus.USER_UPDATE_PROFILE_WRONG_PASSWORD;
        }
        for (int i = 0; i < context.getUserState().getAllUsers().size(); i++) {
            if (context.getUserState().getAllUsers().get(i).getEmail() == newEmail) {
                this.logStatus = LogStatus.USER_UPDATE_PROFILE_EMAIL_ALREADY_IN_USE;
            }
        }
        if (logStatus == LogStatus.USER_UPDATE_PROFILE_EMAIL_ALREADY_IN_USE || logStatus == LogStatus.USER_UPDATE_PROFILE_WRONG_PASSWORD
                || logStatus == LogStatus.USER_UPDATE_PROFILE_NOT_LOGGED_IN ){
            this.successResult = false;
            return false;
        }
        else{
            //what is success result
            this.successResult = true;
            return true;
        }
    }
    @Override
    public Boolean getResult () {
        if(successResult = false){
            return false;
        }
        else{return true;}
    }
}