package command;

import controller.Context;
import logging.Logger;
import model.EntertainmentProvider;
import model.User;

import java.util.List;
import java.util.Map;

public class RegisterEntertainmentProviderCommand extends Object implements ICommand{

//    private LogStatus logStatus;
    private String orgName;
    private String orgAddress;
    private String paymentAccountEmail;
    private String mainRepName;
    private String mainRepEmail;
    private String password;
    private List<String> otherRepNames;
    private List<String> otherRepEmails;
    private EntertainmentProvider newEntertainmentProviderResult ;
    private EntertainmentProvider attribute;

    public enum LogStatus {
        REGISTER_ENTERTAINMENT_PROVIDER_SUCCESS,
        USER_REGISTER_FIELDS_CANNOT_BE_NULL,
        USER_REGISTER_EMAIL_ALREADY_REGISTERED,
        USER_REGISTER_ORG_ALREADY_REGISTERED,
        USER_LOGIN_SUCCESS
    }

    public RegisterEntertainmentProviderCommand(String orgName, String orgAddress, String paymentAccountEmail,
                                         String mainRepName, String mainRepEmail, String password,
                                                List<String> otherRepNames, List<String> otherRepEmails){
        this.orgName = orgName;
        this.orgAddress = orgAddress;
        this.paymentAccountEmail = paymentAccountEmail;
        this.mainRepName = mainRepName;
        this.mainRepEmail = mainRepEmail;
        this.password = password;
        this.otherRepNames = otherRepNames;
        this.otherRepEmails = otherRepEmails;
        this.newEntertainmentProviderResult = null;
    }

    @Override
    public void execute(Context context) {
        LogStatus logStatus = null;
        // Verifies orgName, orgAddress, paymentAccountEmail, mainRepName,
        // mainRepEmail, password, otherRepNames, and otherRepEmails are all not null
        if(orgName == null || orgAddress == null || paymentAccountEmail == null || mainRepEmail == null
                || password == null|| otherRepNames == null|| otherRepEmails == null){
            logStatus = LogStatus.USER_REGISTER_FIELDS_CANNOT_BE_NULL;
            Logger.getInstance().logAction("RegisterEntertainmentProviderCommand", LogStatus.USER_REGISTER_FIELDS_CANNOT_BE_NULL);
        }

        // Loops through users to check the inputted email against all existing emails
        Map<String, User> allUsers = context.getUserState().getAllUsers();
        for (Map.Entry<String, User> entry : allUsers.entrySet()){
            User currUser = entry.getValue();
            if (currUser.getEmail().equals(mainRepEmail)){
                logStatus = LogStatus.USER_REGISTER_EMAIL_ALREADY_REGISTERED;
                Logger.getInstance().logAction("RegisterEntertainmentProviderCommand", LogStatus.USER_REGISTER_EMAIL_ALREADY_REGISTERED);
                break;
            }
        }
        // Loop through users to check that the organiser is not already registered
        for (Map.Entry<String, User> entry: allUsers.entrySet()){
            User currUser = entry.getValue();
            if (currUser instanceof EntertainmentProvider
                    && ((EntertainmentProvider) currUser).getOrgName().equals(orgName)
                    && ((EntertainmentProvider) currUser).getOrgAddress().equals(orgAddress)){
                logStatus = LogStatus.USER_REGISTER_ORG_ALREADY_REGISTERED;
                Logger.getInstance().logAction("RegisterEntertainmentProviderCommand", LogStatus.USER_REGISTER_ORG_ALREADY_REGISTERED);
            }
        }
        if(logStatus != LogStatus.USER_REGISTER_EMAIL_ALREADY_REGISTERED
                && logStatus!= LogStatus.USER_REGISTER_ORG_ALREADY_REGISTERED
                && logStatus != LogStatus.USER_REGISTER_FIELDS_CANNOT_BE_NULL){
            logStatus = LogStatus.REGISTER_ENTERTAINMENT_PROVIDER_SUCCESS;
            Logger.getInstance().logAction("RegisterEntertainmentProviderCommand", LogStatus.REGISTER_ENTERTAINMENT_PROVIDER_SUCCESS);
            // Create new entertainment provider
            this.newEntertainmentProviderResult = new EntertainmentProvider(
                    orgName,
                    orgAddress,
                    paymentAccountEmail,
                    mainRepName,
                    mainRepEmail,
                    password,
                    otherRepNames,
                    otherRepEmails);
            // Add entertainment provider to list of users
            context.getUserState().addUser(newEntertainmentProviderResult);
            // Log in entertainment provider
            context.getUserState().setCurrentUser(newEntertainmentProviderResult);
            logStatus = LogStatus.USER_LOGIN_SUCCESS;
            Logger.getInstance().logAction("RegisterEntertainmentProviderCommand", LogStatus.REGISTER_ENTERTAINMENT_PROVIDER_SUCCESS);
        }
    }

    @Override
    public Object getResult() {
        return newEntertainmentProviderResult;
    }
}
