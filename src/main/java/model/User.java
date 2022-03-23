package model;

public abstract class User {

    String email;
    String passwordHash;
    String paymentAccountEmail;

    protected User(String email, String password, String paymentAccountEmail){
        this.email = email;
        this.passwordHash = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        this.paymentAccountEmail = paymentAccountEmail;
    }

    public String getEmail(){
        return email;
    }

    public void SetEmail(String newEmail){
        this.email = newEmail;
    }

    public boolean checkPasswordMatch(String password){
        return BCrypt.verifyer().verify(password.toCharArray(), passwordHash).verified;
    }

    public void updatePassword(String newPassword){
        passwordHash = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
    }

    public String getPaymentAccountEmail(){
        return paymentAccountEmail;
    }

    public void setPaymentAccountEmail(String newPaymentAccountEmail){
        paymentAccountEmail = newPaymentAccountEmail;
    }

    public String toString(){
        return "this class is" ;
    }







}
