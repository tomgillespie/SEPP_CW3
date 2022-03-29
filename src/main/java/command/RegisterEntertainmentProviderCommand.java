package command;

import controller.Context;
import model.EntertainmentProvider;
import model.User;

import java.util.List;

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
        if(orgName == null || orgAddress == null || paymentAccountEmail == null || mainRepEmail == null
                || password == null|| otherRepNames == null|| otherRepEmails == null){
            logStatus = LogStatus.USER_REGISTER_FIELDS_CANNOT_BE_NULL;
        }
        for(int i = 0; i < context.getUserState().getAllUsers().size(); i++) {
            if (context.getUserState().getAllUsers().get(i).getEmail() == mainRepEmail) {
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
        if(logStatus != LogStatus.USER_REGISTER_EMAIL_ALREADY_REGISTERED || logStatus!= LogStatus.USER_REGISTER_ORG_ALREADY_REGISTERED){
            this.logStatus = LogStatus.REGISTER_ENTERTAINMENT_PROVIDER_SUCCESS;
            this.newEntertainmentProviderResult = new EntertainmentProvider(orgName, orgAddress, mainRepName, mainRepEmail, otherRepNames, otherRepEmails, mainRepEmail, password, paymentAccountEmail);
            context.getUserState().addUser(newEntertainmentProviderResult);
            context.getUserState().setCurrentUser(newEntertainmentProviderResult);
        }
    }

    @Override
    public Object getResult() {
        return null;
    }
}
