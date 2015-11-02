/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.Provider;

import SoftPhone.Protocol.Sip.Account.SipAccount;
import SoftPhone.Protocol.Sip.Account.SipCredentials;
import javax.sip.message.Request;

/**
 *
 * @author didier
 */
public class RegisterSequence
{
    private SipAccount account;
    private SipCredentials credentials;
    private Request request;

    public RegisterSequence(SipAccount account) {
        this.account = account;
    }

    public RegisterSequence(SipCredentials credentials) {
        this.credentials = credentials;
    }

    public SipCredentials getCredentials() {
        return credentials;
    }



    public SipAccount getAccount() {
        return account;
    }

    public Request getRequestToAuth()
    {
        return request;
    }
    
    
    public void setRequestToAuth(Request requestToAuth)
    {
        this.request=requestToAuth;
    }



    

}
