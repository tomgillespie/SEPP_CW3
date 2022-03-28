package command;

import model.EntertainmentProvider;

import java.util.List;

public class RegisterEntertainmentProviderCommand implements ICommand{

    public enum LogStatus {
        REGISTER_ENTERTAINMENT_PROVIDER_SUCCESS,
        USER_REGISTER_FIELDS_CANNOT_BE_NULL,
        USER_REGISTER_EMAIL_ALREADY_REGISTERED,
        USER_REGISTER_ORG_ALREADY_REGISTERED,
        USER_LOGIN_SUCCESS
    }

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
    private LogStatus logstatus;

    RegisterEntertainmentProviderCommand(String orgName, String orgAddress, String paymentAccountEmail,
                                          String mainRepName, String mainRepEmail, String password, List<String> otherRepNames,
                                         List<String> otherRepEmails){
        this.orgName = orgName;
        this.orgAddress = orgAddress;
        this.paymentAccountEmail = paymentAccountEmail;
        this.mainRepEmail = mainRepEmail;
        this.password = password;
        this.otherRepNames = otherRepNames;
        this.otherRepEmails = otherRepEmails;
    }

    public void execute(controller.Context context){
        if(orgName == null || orgAddress == null || paymentAccountEmail == null || mainRepEmail == null
                || password == null|| otherRepNames == null|| otherRepEmails == null){
            logstatus = LogStatus.USER_REGISTER_FIELDS_CANNOT_BE_NULL;
        }
        if(/* no account same email */){
            logstatus = LogStatus.USER_REGISTER_EMAIL_ALREADY_REGISTERED;
        }
        if(/*no org with same name and address*/){
            logstatus = LogStatus.USER_REGISTER_ORG_ALREADY_REGISTERED;
        }
        else{
            logstatus = LogStatus.REGISTER_ENTERTAINMENT_PROVIDER_SUCCESS;
        }
        //if no account with same name and address both using the event provider state
    }

    public EntertainmentProvider getResult(){
        if(logstatus ==  LogStatus.REGISTER_ENTERTAINMENT_PROVIDER_SUCCESS){
            newEntertainmentProviderResult = new EntertainmentProvider(orgName,orgAddress, mainRepName,mainRepEmail,
                    List<String>otherRepNames,List<String>otherRepEmails,password,paymentAccountEmail);
            logstatus = LogStatus.USER_LOGIN_SUCCESS
            return newEntertainmentProviderResult;
            //need to change entertainment providor class to remove email

        }
        else{return null;}
    }


}
