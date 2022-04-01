package command;

import controller.Context;
import model.User;

import java.util.Map;

public class LoginCommand extends Object implements ICommand{

    private String email;
    private String password;
    public LogStatus logStatus;
    private User userResult;

    public enum LogStatus {
        USER_LOGIN_SUCCESS,
        USER_LOGIN_EMAIL_NOT_REGISTERED,
        USER_LOGIN_WRONG_PASSWORD
    }

    public LoginCommand(String email, String password){
        this.email = email;
        this.password = password;
    }

    @Override
    public void execute(Context context){
        Map<String, User> allUsers = context.getUserState().getAllUsers();
        if (!(allUsers.containsKey(email))){
            this.logStatus = LogStatus.USER_LOGIN_EMAIL_NOT_REGISTERED;
        }
        if (allUsers.containsKey(email) && !(allUsers.get(email).checkPasswordMatch(password))){
            this.logStatus = LogStatus.USER_LOGIN_WRONG_PASSWORD;
        }
        if (allUsers.containsKey(email) && allUsers.get(email).checkPasswordMatch(password)){
            this.logStatus = LogStatus.USER_LOGIN_SUCCESS;
            this.userResult = allUsers.get(email);
            context.getUserState().setCurrentUser(userResult);
        }
    }


    @Override
    public Object getResult(){
        if (logStatus == LogStatus.USER_LOGIN_SUCCESS){
            return userResult;
        }
        else return null;
    }
}

//    public void execute(Context context){
//        if (context.getUserState().getCurrentUser().getEmail() == email) {
//            logStatus = LogStatus.USER_LOGIN_SUCCESS;
//        }
//        else
//        {logStatus = LogStatus.USER_LOGIN_EMAIL_NOT_REGISTERED;}
//
//        //change this to the status part
//        if (model.User.checkPasswordMatch(password) == true)
//        {logStatus = LogStatus.USER_LOGIN_SUCCESS;}
//        else
//        {logStatus = LogStatus.USER_LOGIN_WRONG_PASSWORD;}
//    }
//
//    public model.User getResult(){
//        if (logStatus == LogStatus.USER_LOGIN_SUCCESS) {
//            return model.User;
//        } else {
//            return null;}
//    }
//}
