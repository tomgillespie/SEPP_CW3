package command;

import controller.Context;
import logging.Logger;
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

    public enum LogStatus {
        REGISTER_CONSUMER_SUCCESS,
        USER_REGISTER_FIELDS_CANNOT_BE_NULL,
        USER_REGISTER_EMAIL_ALREADY_REGISTERED,
        USER_LOGIN_SUCCESS
    }

    public RegisterConsumerCommand(String name, String email, String phoneNumber, String password,
                                   String paymentAccountEmail){
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.paymentAccountEmail = paymentAccountEmail;
        this.newConsumerResult = null;
    }

    @Override
    public void execute(Context context) {
        LogStatus logStatus = null;
        if (name == null || email == null || phoneNumber == null || password == null || paymentAccountEmail == null) {
            logStatus = LogStatus.USER_REGISTER_FIELDS_CANNOT_BE_NULL;
            Logger.getInstance().logAction("RegisterConsumerCommand.execute", LogStatus.USER_REGISTER_FIELDS_CANNOT_BE_NULL);
        }
        Map<String, User> allUsers = context.getUserState().getAllUsers();
        for (Map.Entry<String, User> entry : allUsers.entrySet()){
            User currUser = entry.getValue();
            if (currUser.getEmail().equals(email)){
                logStatus = LogStatus.USER_REGISTER_EMAIL_ALREADY_REGISTERED;
                Logger.getInstance().logAction("RegisterConsumerCommand.execute", LogStatus.USER_REGISTER_EMAIL_ALREADY_REGISTERED);
                break;
            }
        }
        if (logStatus == null){
            logStatus = LogStatus.REGISTER_CONSUMER_SUCCESS;
            Logger.getInstance().logAction("RegisterConsumerCommand.execute", LogStatus.REGISTER_CONSUMER_SUCCESS);
            this.newConsumerResult = new Consumer(name, email, phoneNumber, password, paymentAccountEmail);
            context.getUserState().addUser(newConsumerResult);
            context.getUserState().setCurrentUser(newConsumerResult);
            logStatus = LogStatus.USER_LOGIN_SUCCESS;;
            Logger.getInstance().logAction("RegisterConsumerCommand.execute", LogStatus.USER_LOGIN_SUCCESS);
        }
    }

    @Override
    public Object getResult() {
            return newConsumerResult;
    }
}
