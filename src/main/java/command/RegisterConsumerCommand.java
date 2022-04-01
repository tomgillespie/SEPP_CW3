package command;

import controller.Context;
import model.Consumer;
import model.User;

import java.util.List;
import java.util.Map;

public class RegisterConsumerCommand extends Object implements ICommand{

    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private String paymentAccountEmail;
    private Consumer newConsumerResult;
    private LogStatus logStatus;

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

    @Override
    public void execute(Context context) {
        if (name == null || email == null || phoneNumber == null || password == null || paymentAccountEmail == null) {
            this.logStatus = LogStatus.USER_REGISTER_FIELDS_CANNOT_BE_NULL;
        }
        Map<String, User> allUsers = context.getUserState().getAllUsers();
        for (Map.Entry<String, User> entry : allUsers.entrySet()){
            User currUser = entry.getValue();
            if (currUser.getEmail().equals(email)){
                this.logStatus = LogStatus.USER_REGISTER_EMAIL_ALREADY_REGISTERED;
                break;
            }
            else {
                this.logStatus = LogStatus.REGISTER_CONSUMER_SUCCESS;
            }
        }
        if (logStatus == LogStatus.REGISTER_CONSUMER_SUCCESS){
            this.newConsumerResult = new Consumer(name, email, phoneNumber, password, paymentAccountEmail);
            context.getUserState().addUser(newConsumerResult);
            context.getUserState().setCurrentUser(newConsumerResult);
            this.logStatus = LogStatus.USER_LOGIN_SUCCESS;
        }
    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.USER_LOGIN_SUCCESS){
            return newConsumerResult;
        }
        else return null;
    }
}
