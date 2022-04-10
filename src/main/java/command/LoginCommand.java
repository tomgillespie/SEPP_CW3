package command;

import controller.Context;
import logging.Logger;
import model.User;

import java.util.Map;

public class LoginCommand extends Object implements ICommand{

    private String email;
    private String password;
    private User userResult;

    public enum LogStatus {
        USER_LOGIN_SUCCESS,
        USER_LOGIN_EMAIL_NOT_REGISTERED,
        USER_LOGIN_WRONG_PASSWORD
    }

    public LoginCommand(String email, String password){
        this.email = email;
        this.password = password;
        this.userResult = null;
    }

    @Override
    public void execute(Context context){
        LogStatus logStatus = null;
        Map<String, User> allUsers = context.getUserState().getAllUsers();
        if (!(allUsers.containsKey(email))){
            logStatus = LogStatus.USER_LOGIN_EMAIL_NOT_REGISTERED;
            Logger.getInstance().logAction("LoginCommand.execute", LogStatus.USER_LOGIN_EMAIL_NOT_REGISTERED);
        }
        if (allUsers.containsKey(email) && !(allUsers.get(email).checkPasswordMatch(password))){
            logStatus = LogStatus.USER_LOGIN_WRONG_PASSWORD;
            Logger.getInstance().logAction("LoginCommand.execute", LogStatus.USER_LOGIN_WRONG_PASSWORD);
        }
        if (allUsers.containsKey(email) && allUsers.get(email).checkPasswordMatch(password)){
            logStatus = LogStatus.USER_LOGIN_SUCCESS;
            Logger.getInstance().logAction("LoginCommand.execute", LogStatus.USER_LOGIN_SUCCESS);
            this.userResult = allUsers.get(email);
            context.getUserState().setCurrentUser(userResult);
        }
    }

    @Override
    public Object getResult(){
            return userResult;
    }
}