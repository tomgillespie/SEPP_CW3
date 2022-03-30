package command;

import controller.Context;
import model.EntertainmentProvider;
import model.User;

import java.util.List;
import java.util.Map;

public class RegisterEntertainmentProviderCommand extends Object implements ICommand{

    private LogStatus logStatus;
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
                                         String mainRepName, String mainRepEmail, String password, List<String> otherRepNames,
                                         List<String> otherRepEmails){
        this.orgName = orgName;
        this.orgAddress = orgAddress;
        this.paymentAccountEmail = paymentAccountEmail;
        this.mainRepName = mainRepName;
        this.mainRepEmail = mainRepEmail;
        this.password = password;
        this.otherRepNames = otherRepNames;
        this.otherRepEmails = otherRepEmails;
    }

    @Override
    public void execute(Context context) {
        // Verifies orgName, orgAddress, paymentAccountEmail, mainRepName, mainRepEmail, password, otherRepNames, and otherRepEmails are all not null
        if(orgName == null || orgAddress == null || paymentAccountEmail == null || mainRepEmail == null
                || password == null|| otherRepNames == null|| otherRepEmails == null){
            logStatus = LogStatus.USER_REGISTER_FIELDS_CANNOT_BE_NULL;
        }

        Map<String, User> allUsers = context.getUserState().getAllUsers();
        for(int i = 0; i < allUsers.size(); i++) {
            User currUser = allUsers.get(String.valueOf(i));
            if (currUser.getEmail() == mainRepEmail) {
                this.logStatus = LogStatus.USER_REGISTER_EMAIL_ALREADY_REGISTERED;
                break;
            }
        }
        for(int i = 0; i < context.getUserState().getAllUsers().size(); i++){
            User currUser = context.getUserState().getAllUsers().get(i);
            if (currUser instanceof EntertainmentProvider && ((EntertainmentProvider) currUser).getOrgName().equals(orgName) && ((EntertainmentProvider) currUser).getOrgAddress().equals(orgAddress)){
                this.logStatus = LogStatus.USER_REGISTER_ORG_ALREADY_REGISTERED;
                break;
            }
        }
        if(logStatus != LogStatus.USER_REGISTER_EMAIL_ALREADY_REGISTERED && logStatus!= LogStatus.USER_REGISTER_ORG_ALREADY_REGISTERED && logStatus != LogStatus.USER_REGISTER_FIELDS_CANNOT_BE_NULL){
            this.logStatus = LogStatus.REGISTER_ENTERTAINMENT_PROVIDER_SUCCESS;
            this.newEntertainmentProviderResult = new EntertainmentProvider(orgName, orgAddress, mainRepName, mainRepEmail, otherRepNames, otherRepEmails, mainRepEmail, password, paymentAccountEmail);
            context.getUserState().addUser(newEntertainmentProviderResult);
            context.getUserState().setCurrentUser(newEntertainmentProviderResult);
            this.logStatus = LogStatus.USER_LOGIN_SUCCESS;
        }
    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.REGISTER_ENTERTAINMENT_PROVIDER_SUCCESS || logStatus == LogStatus.USER_LOGIN_SUCCESS){
            return newEntertainmentProviderResult;
            //need to change entertainment providor class to remove email

        }
        else return null;
    }


}
