

package SoftPhone.Protocol.Sip.Account;

import SoftPhone.Protocol.Sip.Agent.SipProxy;
import SoftPhone.Protocol.Sip.Agent.SipRegistrar;
import SoftPhone.Protocol.Sip.*;
import SoftPhone.Protocol.Sip.Call.CallParticipantInterface;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.net.InetAddress;


/**
 *
 * @author didier
 */

/*TODO make the gui needed to setup a sip Account, this must start if no account exists or if user asks it
 with the configure bouton and choose the sip configuration in the dialog window*/

//TODO Review all the accessors

//TODO Remove the String sipRegistrar property
public class SipAccount implements CallParticipantInterface
    {

    private SipCredentials credentials=null;
    private String accountName="";
    private String sipAddress="";
    
    @XStreamImplicit(itemFieldName = "Proxy")
    private SipProxy proxy;
    @XStreamImplicit(itemFieldName = "Registrar")
    private SipRegistrar registrar;
   

    public SipAccount()
    {
        this.registrar=new SipRegistrar();
        this.proxy=new SipProxy("", 5060);
	  this.credentials = new SipCredentials();
    }

   

    public SipAccount(String accountName)
    {
        this.accountName =accountName;
	  this.registrar=new SipRegistrar();
        this.proxy=new SipProxy("", 5060);
	  this.credentials = new SipCredentials();

    }




    public String getAccountName()
    {
        return accountName;
    }

    public String getDisplayName()
    {
        return accountName;
    }

    public boolean hasProy() 
    {
        boolean b;
        if(this.getProxy().getUri().equalsIgnoreCase(""))
            b=false;
        else b=true;
        return b;
    }

    public void setDisplayName(String name)
    {
        this.accountName =name;
    }

    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }


    public String getSipAddress()
    {
        return this.sipAddress;
    }

    public void setSipAddress(String sipAddress)
    {
        boolean sipAddressValidated =SipUtils.validateSipAddress(sipAddress);
        if(sipAddressValidated)
        {
            this.sipAddress =sipAddress;
        }
    }

    public SipRegistrar getSipRegistrar()
    {
        return this.registrar;
    }

	public SipCredentials getCredentials()
	{
		return this .credentials;
	}
  


    public String getAccountUsername()
    {
        return this.credentials.getUserName();
    }

    public void setAccountUserName(String accountUserName)
    {
        this.credentials.setUserName(accountUserName);
    }

    public void setAccountUserPassword(String accountUserPassword)
    {
        this.credentials.setPassword(accountUserPassword);
    }


    public String getAccountUserPassword()
    {
        return this.credentials.getPassword();
    }

    public SipRegistrar getRegistrar()
    {
        return registrar;
    }



    public void setRegistrar(SipRegistrar registrar)
    {
        this.registrar = registrar;
    }

    public SipProxy getProxy()
    {
        return proxy;
    }

    public void setProxy(SipProxy proxy)
    {
        this.proxy = proxy;
    }

    public String getUserInfo()
    {
        String userinfo="";
        if(sipAddress.startsWith("sip:"))
        {
            userinfo =sipAddress.substring(4);
            if(userinfo.contains("@"))
            {
                int i=userinfo.indexOf("@");
                userinfo=userinfo.substring(0, i);
            }
        }
          return userinfo;

    }

    public String getUserDomain()
    {
        String domain="";
        if(sipAddress.contains("@"))
        {
            int i=sipAddress.indexOf("@");
            domain =sipAddress.substring(i);
        }
        return domain;
    }
//Juste la en accord avec l'interface CallParticipantInterface
    public void setIPAddress(InetAddress IPAddress)
    {

    }
//Juste la en accord avec l'interface CallParticipantInterface
   // ne pas utiliser utiliser networkConfiguration
    public InetAddress getIPAddress()
    {
      return null;
    }

    public int getSipPort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSipPort(String port) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
