package com.aroha.pet.payload;

public class ForgetPassword {

    private String usernameOrEmail;
    private String oneTimePass;
    private String password;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getOneTimePass() {
        return oneTimePass;
    }

    public void setOneTimePass(String oneTimePass) {
        this.oneTimePass = oneTimePass;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ForgetPassword [usernameOrEmail=" + usernameOrEmail + ", oneTimePass=" + oneTimePass + ", password="
                + password + "]";
    }
}
