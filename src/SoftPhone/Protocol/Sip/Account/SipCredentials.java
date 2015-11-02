/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone.Protocol.Sip.Account;

/**
 *
 * @author didier
 */
public class SipCredentials {
    private String userName="";
    private String password="";

    public SipCredentials()
    {

    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
