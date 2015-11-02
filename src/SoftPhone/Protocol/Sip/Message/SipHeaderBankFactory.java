/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.Message;

import SoftPhone.Protocol.Sip.Provider.SipStackActivator;
import java.util.logging.*;
import javax.sip.*;
import javax.sip.address.*;
import javax.sip.header.*;
import javax.sip.message.*;

/**
 *
 * @author didier
 */
public class SipHeaderBankFactory
{
    private SipFactory sipFactory;
    private HeaderFactory headerFactory;
    private AddressFactory addressFactory;
    private MessageFactory messageFactory;
    private final SipHeaderBank sipHeaderBank;
    private SipProvider sipProvider;

    private  SipHeaderBankFactory(SipStackActivator sipStack)
    {
        try
            {
                this.sipFactory = sipStack.getSipFactory();
                headerFactory = sipFactory.createHeaderFactory();
                addressFactory = sipFactory.createAddressFactory();
                messageFactory= sipFactory.createMessageFactory();
                sipProvider =sipStack.getSipProvider();

            }
        catch (PeerUnavailableException ex)
            {
                Logger.getLogger(SipHeaderBankFactory.class.getName())
                .log(Level.SEVERE, null, ex);
            }
        
        sipHeaderBank = new SipHeaderBank();
        sipHeaderBank.setAddressFactory(addressFactory);
        sipHeaderBank.setHeaderFactory(headerFactory);
        sipHeaderBank.setMessageFactory(messageFactory);
        sipHeaderBank.setSipProvider(sipProvider);
       
       
       
    }

    

    public static SipHeaderBank getSipHeaderBank(SipStackActivator sipStack)
    {
         SipHeaderBankFactory sipHeaderBankFactory
                              =new SipHeaderBankFactory(sipStack);
         
         return sipHeaderBankFactory.sipHeaderBank;
    }
}
