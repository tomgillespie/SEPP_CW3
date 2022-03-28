package command;

import model.Consumer;
import model.User;

public class RegisterConsumerCommand implements ICommand {

    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private String paymentAccountEmail;
    private Consumer newConsumerResult;
    private LogStatus logstatus;

    public enum LogStatus {
        REGISTER_CONSUMER_SUCCESS,
        USER_REGISTER_FIELDS_CANNOT_BE_NULL,
        USER_REGISTER_EMAIL_ALREADY_REGISTERED,
        USER_LOGIN_SUCCESS
    }

    public RegisterConsumerCommand(String name, String email, String phoneNumber, String password, String paymentAccountEmail){
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.paymentAccountEmail = paymentAccountEmail;
    }

    public void execute(controller.Context context){
        if (name == null || email == null || phoneNumber == null || password == null || paymentAccountEmail == null) {
            logstatus = LogStatus.USER_REGISTER_FIELDS_CANNOT_BE_NULL;
        }
        //if the map contains the email??=
        if(state.UserState.users.containsValue(email)){
            logstatus = LogStatus.USER_REGISTER_EMAIL_ALREADY_REGISTERED;
        }
        else{
            logstatus = LogStatus.REGISTER_CONSUMER_SUCCESS;
        }
    }

    public Consumer getResult(){
        if (logstatus == LogStatus.REGISTER_CONSUMER_SUCCESS ){
            //how to log the user in??
            logstatus = LogStatus.USER_LOGIN_SUCCESS;
            //will need to add a new user to the user state
            newConsumerResult = new Consumer(name ,email,phoneNumber,password,paymentAccountEmail);
            return newConsumerResult;
        }
        else{return null;}
    }

}
