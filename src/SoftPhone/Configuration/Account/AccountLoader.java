/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Configuration.Account;
import SoftPhone.Protocol.Sip.*;
import SoftPhone.Network.NetworkUtils;
import SoftPhone.Protocol.Sip.Account.SipAccount;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.*;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author didier
 */
public class AccountLoader
{
    private boolean configured=false;
    private  SipAccount  account =null;
    private XStream xstream;
    private String accountPath;


    public AccountLoader(String accountPath)
    {
        this.accountPath=accountPath;
        xstream = new XStream(new DomDriver());
        xstream.alias("SipAccount", SoftPhone.Protocol.Sip.Account.SipAccount.class);
        xstream.alias("Proxy",SoftPhone.Protocol.Sip.Agent.SipProxy.class);
        xstream.alias("Registrar",SoftPhone.Protocol.Sip.Agent.SipRegistrar.class);
        load();
    }


    private void load()
    {
        try {
            String fromFile = accountPath+"\\SipAccount.xml";
            File f = new File(fromFile);
            byte[] bytes = new byte[(int) f.length()];
            FileInputStream fis= new FileInputStream(f);
            fis.read(bytes);
            fis.close();
            String xml = new String(bytes);
            account = (SipAccount) xstream.fromXML(xml);
            configured=verifyAccount(account);

        } catch (IOException ex) {
            try {
                String fromFile = accountPath+"\\SipAccount.xml";
                File f = new File(fromFile);
                f.createNewFile();
                load();
                Logger.getLogger(AccountLoader.class.getName())
                        .log(Level.SEVERE, null, ex);
            } catch (IOException ex1) {
                Logger.getLogger(AccountLoader.class.getName())
                        .log(Level.SEVERE, null, ex1);
            }

        }
        catch(StreamException ex)
        {
            try {
                Logger.getLogger(AccountLoader.class.getName())
                        .log(Level.SEVERE, null, ex);
                SipAccount sipaccount = new SipAccount();
                String fromFile = accountPath+"\\SipAccount.xml";
                File f = new File(fromFile);
                FileOutputStream fos =new FileOutputStream(f);
                xstream.toXML(sipaccount, fos);
                fos.close();
                load();

                }
            catch (IOException ex1)
                {
                Logger.getLogger(AccountLoader.class.getName())
                        .log(Level.SEVERE, null, ex1);

                }
    }
    }

    private boolean verifyAccount(SipAccount account)
    {
        boolean valid=false;
        valid =(
                !account.getAccountName().equalsIgnoreCase("")
              && !account.getAccountUsername().equalsIgnoreCase("")
              && SipUtils.validateSipAddress(account.getSipAddress())
              
                );
        return valid;
    }

    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean Configured) {
        this.configured = Configured;
    }

    public SipAccount getSipAccount()
    {
        return account;
    }



     public  SipAccount getConfiguredAccount()
     {
        return account;
     }

     public void save()
     {
        FileOutputStream fos = null;
        try {
               
                String fromFile = accountPath+"\\SipAccount.xml";
                File f = new File(fromFile);
                fos = new FileOutputStream(f);
                xstream.toXML(account, fos);
                fos.close();
            }
        catch (IOException ex)
            {
                Logger.getLogger(AccountLoader.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
       finally
        {
            try
                {
                    fos.close();
                }
            catch (IOException ex)
                {
                    Logger.getLogger(AccountLoader.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
        }
     }
}
