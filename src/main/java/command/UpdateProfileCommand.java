package command;

import controller.Context;
import logging.Logger;
import model.User;

public abstract class UpdateProfileCommand extends Object implements ICommand {
    protected Boolean successResult;
//    private LogStatus logStatus;

    public
    enum LogStatus {
        USER_UPDATE_PROFILE_NOT_LOGGED_IN,
        USER_UPDATE_PROFILE_WRONG_PASSWORD,
        USER_UPDATE_PROFILE_EMAIL_ALREADY_IN_USE
    }

    UpdateProfileCommand() {
        this.successResult = null;
    }

    protected boolean isProfileUpdateInvalid(Context context, String oldPassword, String newEmail) {
        LogStatus logStatus = null;
        User currUser = context.getUserState().getCurrentUser();
        if (context.getUserState().getCurrentUser() == null) {
            logStatus = LogStatus.USER_UPDATE_PROFILE_NOT_LOGGED_IN;
//            this.logStatus = LogStatus.USER_UPDATE_PROFILE_NOT_LOGGED_IN;
            Logger.getInstance().logAction("UpdateProfileCommand", LogStatus.USER_UPDATE_PROFILE_NOT_LOGGED_IN);
        }
        if (currUser.checkPasswordMatch(oldPassword) == false) {
            logStatus = LogStatus.USER_UPDATE_PROFILE_WRONG_PASSWORD;
//            this.logStatus = LogStatus.USER_UPDATE_PROFILE_WRONG_PASSWORD;
            Logger.getInstance().logAction("UpdateProfileCommand", LogStatus.USER_UPDATE_PROFILE_WRONG_PASSWORD);
        }
        for (int i = 0; i < context.getUserState().getAllUsers().size(); i++) {
            if (context.getUserState().getAllUsers().get(i).getEmail() == newEmail) {
                logStatus = LogStatus.USER_UPDATE_PROFILE_EMAIL_ALREADY_IN_USE;
//                this.logStatus = LogStatus.USER_UPDATE_PROFILE_EMAIL_ALREADY_IN_USE;
                Logger.getInstance().logAction("UpdateProfileCommand", LogStatus.USER_UPDATE_PROFILE_EMAIL_ALREADY_IN_USE);
            }
        }
        if (logStatus == LogStatus.USER_UPDATE_PROFILE_EMAIL_ALREADY_IN_USE
                || logStatus == LogStatus.USER_UPDATE_PROFILE_WRONG_PASSWORD
                || logStatus == LogStatus.USER_UPDATE_PROFILE_NOT_LOGGED_IN ){
            this.successResult = true;
            return true;
        }
        else{
            //what is success result
            this.successResult = false;
            return false;
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