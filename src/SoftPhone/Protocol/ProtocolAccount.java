/*
 * this class will be a generic class for all protocol account
 * 
 */

package SoftPhone.Protocol;

/**
 *
 * @author didier
 */
public class ProtocolAccount
{
    private String protocol="";
    private String accountName="";
    private String userName="";
    private String password="";

    public ProtocolAccount()
    {

    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public void setUserName(String username) {
        this.userName = username;
    }





}
