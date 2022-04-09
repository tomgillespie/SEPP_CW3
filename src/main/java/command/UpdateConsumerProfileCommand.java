package command;

import controller.Context;
import logging.Logger;
import model.Consumer;
import model.ConsumerPreferences;
import model.User;

public class UpdateConsumerProfileCommand extends UpdateProfileCommand{
    private String oldPassword;
    private String newName;
    private String newEmail;
    private String newPhoneNumber;
    private String newPassword;
    private String newPaymentAccountEmail;
    private ConsumerPreferences newPreferences;

    public enum LogStatus {
        USER_UPDATE_PROFILE_FIELDS_CANNOT_BE_NULL,
        USER_UPDATE_PROFILE_NOT_CONSUMER,
        USER_UPDATE_PROFILE_SUCCESS

    }

    public UpdateConsumerProfileCommand(String oldPassword, String newName, String newEmail, String newPhoneNumber,
                                 String newPassword, String newPaymentAccountEmail, ConsumerPreferences newPreferences){
        this.oldPassword = oldPassword;
        this.newName = newName;
        this.newEmail = newEmail;
        this.newPhoneNumber = newPhoneNumber;
        this.newPassword = newPassword;
        this.newPaymentAccountEmail = newPaymentAccountEmail;
        this.newPreferences = newPreferences;
    }

    @Override
    public void execute(Context context) {
        LogStatus logStatus = null;
        if(oldPassword == null || newName == null || newEmail == null || newPhoneNumber == null || newPassword == null ||
                newPaymentAccountEmail == null || newPreferences == null){
            logStatus = LogStatus.USER_UPDATE_PROFILE_FIELDS_CANNOT_BE_NULL;
            Logger.getInstance().logAction("UpdateConsumerProfileCommand.execute", LogStatus.USER_UPDATE_PROFILE_FIELDS_CANNOT_BE_NULL);
        }
        //implies is true
        User currUser = context.getUserState().getCurrentUser();
        if(!(currUser instanceof Consumer)){
            logStatus = LogStatus.USER_UPDATE_PROFILE_NOT_CONSUMER;
            Logger.getInstance().logAction("UpdateConsumerProfileCommand.execute", LogStatus.USER_UPDATE_PROFILE_NOT_CONSUMER);
        }
        if(!(isProfileUpdateInvalid(context, oldPassword, newEmail)) && currUser instanceof Consumer){
            logStatus = LogStatus.USER_UPDATE_PROFILE_SUCCESS;
            Logger.getInstance().logAction("UpdateConsumerProfileCommand.execute", LogStatus.USER_UPDATE_PROFILE_SUCCESS);
        }

        if(logStatus == LogStatus.USER_UPDATE_PROFILE_SUCCESS){
            //how to make more efficient?
            currUser.setEmail(newEmail);
            currUser.updatePassword(newPassword);
            currUser.setPaymentAccountEmail(newPaymentAccountEmail);
            ((Consumer) currUser).setName(newName);
            ((Consumer) currUser).setPhoneNumber(newPhoneNumber);
            ((Consumer) currUser).setPreferences(newPreferences);
        }
    }
}
