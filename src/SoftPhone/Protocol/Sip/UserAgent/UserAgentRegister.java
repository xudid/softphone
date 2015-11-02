/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 * Cette classe a pour but de gérer les enregistrements des comptes SIP
 * auprès du ou des registrars configurés
 *
 */

package SoftPhone.Protocol.Sip.UserAgent;

import SoftPhone.Protocol.Sip.Account.SipAccount;
import SoftPhone.Protocol.Sip.Message.Event.ClientErrorResponseEvent;
import SoftPhone.Protocol.Sip.Message.Event.GlobalErrorResponseEvent;
import SoftPhone.Protocol.Sip.Message.Event.InviteOKResponseEvent;
import SoftPhone.Protocol.Sip.Message.Event.ProvisionnalResponseEvent;
import SoftPhone.Protocol.Sip.Message.Event.RedirectResponseEvent;
import SoftPhone.Protocol.Sip.Message.Event.RegisterOkResponseEvent;
import SoftPhone.Protocol.Sip.Message.Event.ServerErrorResponseEvent;
import SoftPhone.Protocol.Sip.Message.RedirectEvent;
import SoftPhone.Protocol.Sip.*;
import SoftPhone.Protocol.Sip.Message.SipMessageBank;
import SoftPhone.Protocol.Sip.Message.SipMessageDispatcher;
import SoftPhone.Protocol.Sip.Provider.DialogsRepository;
import SoftPhone.Protocol.Sip.Provider.SipLayerClientListener;
import SoftPhone.Protocol.Sip.Provider.TransactionsRepository;
import SoftPhone.Protocol.Sip.UserAgent.Event.RegistredEvent;
import java.util.ArrayList;
import javax.sip.address.Address;
import javax.sip.header.ContactHeader;
import javax.sip.message.Response;

/**
 *
 * @author didier
 */
public class UserAgentRegister
{
    private SipMessageDispatcher sipMessageRouter;
    private SipMessageBank sipMessageBank;
    private DialogsRepository dialogs;
    private TransactionsRepository transactions;
    private ArrayList<UserAgentRegistrerListener> userAgentRegistrerListeners=null;
    private SipLayerClientListener sipLayerListener;
    private ArrayList<SipAccount> accounts;
 
    public UserAgentRegister(
                             final SipMessageDispatcher sipMessageRouter
                            ,final SipMessageBank sipMessageBank
                            ,DialogsRepository d
                            ,TransactionsRepository t
                            ,ArrayList<SipAccount> accounts
                          )
   {
       this.sipMessageRouter =sipMessageRouter;
       this.sipMessageBank =sipMessageBank;
       this.userAgentRegistrerListeners =new ArrayList<UserAgentRegistrerListener>();
       this.accounts =accounts;
       dialogs =d;
       transactions =t;

       sipLayerListener =new SipLayerClientListener() {

            public void processProvisionalResponse(ProvisionnalResponseEvent event)
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void processInviteOkResponse(InviteOKResponseEvent event)
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void processRegisterOkResponse(RegisterOkResponseEvent event)
            {
                Response  r=event.getResponse();
                 int expires=0;
                ContactHeader h=( ContactHeader)r.getHeader(ContactHeader.NAME);
                if(h!=null)
                    {
                         expires =h.getExpires();
                         Address addr =h.getAddress();
                         fireRegistred(addr,expires);
                    }

            }

            public void processRedirectResponse(RedirectResponseEvent event)
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void processErrorClientResponse(ClientErrorResponseEvent event)
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void processErrorClientResponse(RedirectEvent event) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void processServerErrorResponse(ServerErrorResponseEvent event)
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void processGlobalErrorResponse(GlobalErrorResponseEvent event)
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

public void Register(SipAccount account)
{

}

public void UnRegister(SipAccount account)
{

}

public void RegisterAll()
{

}

private void fireRegistred(Address addr,int expires)
{
    RegistredEvent event =new RegistredEvent(this, addr,expires);
    for(UserAgentRegistrerListener l:userAgentRegistrerListeners)
    {
        l.processRegistredEvent(event);
    }

}
}


