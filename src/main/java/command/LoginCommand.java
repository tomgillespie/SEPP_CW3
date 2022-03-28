package command;

public class LoginCommand extends Object implements ICommand{

        private String email;
        private String password;
        private model.User userResult;
        public LogStatus logStatus;

        public enum LogStatus {
        USER_LOGIN_SUCCESS,
        USER_LOGIN_EMAIL_NOT_REGISTERED,
        USER_LOGIN_WRONG_PASSWORD
        }

    LoginCommand(String email, String password){
        this.email = email;
        this.password = password;
    }

    public void execute(controller.Context context){
        if (model.User.getEmail() == email) {
            logStatus = LogStatus.USER_LOGIN_SUCCESS;
        }
        else
            {logStatus = LogStatus.USER_LOGIN_EMAIL_NOT_REGISTERED;}

        //change this to the status part
        if (model.User.checkPasswordMatch(password) == true)
            {logStatus = LogStatus.USER_LOGIN_SUCCESS;}
        else
            {logStatus = LogStatus.USER_LOGIN_WRONG_PASSWORD;}
    }

    public model.User getResult(){
        if (logStatus == LogStatus.USER_LOGIN_SUCCESS) {
            return model.User;
        } else {
            return null;}
    }
}
