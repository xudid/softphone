/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone.Protocol.Sip.Account;

import SoftPhone.Protocol.Sip.Account.SipAccount;

/**
 *
 * @author didier
 */
public class SipAccountController
{
    private String defaultSipAddress =" ";

    public SipAccountController()
    {

    }

    public boolean Exists(String sipAddress)
    {
        return true;
    }

    public String getAccountState(String sipAddress)

    {return "";}

    public String getDefaultSipAddress()
    {return "";}



    public void setDefaultSipAddress(String sipAddress)
    {this.defaultSipAddress = sipAddress;}

    public String isAvailableDestination(String sipAddress)
    {return "";}

    public void addAccount(SipAccount account)
    {}

    public void removeAccount(String SipAccountName)
    {}







}
