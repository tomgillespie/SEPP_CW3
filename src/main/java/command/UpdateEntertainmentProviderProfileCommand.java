package command;

import controller.Context;

import model.EntertainmentProvider;
import model.User;

import java.util.List;

public class UpdateEntertainmentProviderProfileCommand extends UpdateProfileCommand {

    private String oldPassword;
    private String newOrgName;
    private String newOrgAddress;
    private String newPaymentAccountEmail;
    private String newMainRepName;
    private String newMainRepEmail;
    private String newPassword;
    private List<String> newOtherRepNames;
    private List<String> newOtherRepEmails;
    private LogStatus logStatus;

    public enum LogStatus {
        USER_UPDATE_PROFILE_SUCCESS,
        USER_UPDATE_PROFILE_FIELDS_CANNOT_BE_NULL,
        USER_UPDATE_PROFILE_NOT_ENTERTAINMENT_PROVIDER,
        USER_UPDATE_PROFILE_ORG_ALREADY_REGISTERED
    }

    UpdateEntertainmentProviderProfileCommand(String oldPassword, String newOrgName, String newOrgAddress,
                                              String newPaymentAccountEmail, String newMainRepName, String newMainRepEmail,
                                              String newPassword, List<String> newOtherRepNames, List<String> newOtherRepEmails) {
        this.oldPassword = oldPassword;
        this.newOrgName = newOrgName;
        this.newOrgAddress = newOrgAddress;
        this.newPaymentAccountEmail = newPaymentAccountEmail;
        this.newMainRepName = newMainRepName;
        this.newMainRepEmail = newMainRepEmail;
        this.newPassword = newPassword;
        this.newOtherRepNames = newOtherRepNames;
        this.newOtherRepEmails = newOtherRepEmails;
    }


    @Override
    public void execute(Context context) {
        if (oldPassword == null || newOrgName == null || newOrgAddress == null || newPaymentAccountEmail == null || newMainRepName == null ||
                newMainRepEmail == null || newPassword == null || newOtherRepNames == null || newOtherRepEmails == null) {
            logStatus = LogStatus.USER_UPDATE_PROFILE_FIELDS_CANNOT_BE_NULL;
        }
        User currUser = context.getUserState().getCurrentUser();
        if (isProfileUpdateInvalid(context, oldPassword, newMainRepEmail) && currUser instanceof EntertainmentProvider) {
            logStatus = LogStatus.USER_UPDATE_PROFILE_SUCCESS;
        } else {
            logStatus = LogStatus.USER_UPDATE_PROFILE_NOT_ENTERTAINMENT_PROVIDER;
        }

        for (int i = 0; i < context.getUserState().getAllUsers().size(); i++) {
            User loopUser = context.getUserState().getAllUsers().get(i);
            if (loopUser instanceof EntertainmentProvider && ((EntertainmentProvider) loopUser).getOrgName().equals(newOrgName) && ((EntertainmentProvider) loopUser)
                    .getOrgAddress().equals(newOrgAddress)) {
                logStatus = LogStatus.USER_UPDATE_PROFILE_ORG_ALREADY_REGISTERED;
            }
        }
        if(logStatus == LogStatus.USER_UPDATE_PROFILE_SUCCESS){
            context.getUserState().getCurrentUser().setEmail(newMainRepEmail);
            context.getUserState().getCurrentUser().updatePassword(newPassword);
            context.getUserState().getCurrentUser().setPaymentAccountEmail(newPaymentAccountEmail);
            ((EntertainmentProvider) currUser).setOrgName(newOrgName);
            ((EntertainmentProvider) currUser).setOrgAddress(newOrgAddress);
            ((EntertainmentProvider) currUser).setMainRepName(newMainRepName);
            ((EntertainmentProvider) currUser).setOtherRepNames(newOtherRepNames);
            ((EntertainmentProvider) currUser).setOtherRepEmails(newOtherRepEmails);
        }

    }

}
