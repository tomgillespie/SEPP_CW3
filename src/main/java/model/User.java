package model;

import at.favre.lib.crypto.bcrypt.BCrypt;

public abstract class User {

    private String email;
    private String passwordHash;
    private String paymentAccountEmail;

    protected User(String email, String password, String paymentAccountEmail){
        // assert(password != null);
        // assert(email != null);
        // assert(paymentAccountEmail != null);
        this.email = email;
        // Case to catch a null password - cannot be hashed
        if (password == null){
            this.passwordHash = null;
        }
        else {
            this.passwordHash = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        }
        this.paymentAccountEmail = paymentAccountEmail;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String newEmail){
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
        this.paymentAccountEmail = newPaymentAccountEmail;
    }
}
