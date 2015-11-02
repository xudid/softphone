/*
 * traiter la fabrication des message comme des tâches  ainsi que le traitement des réponses aux requêtes
 */

package SoftPhone.Protocol.Sip.UserAgent;

/**
 *
 * @author didier
 */

import SoftPhone.Protocol.Sip.Provider.DialogsRepository;
import SoftPhone.Protocol.Sip.Provider.SipLayerClientListener;
import SoftPhone.Protocol.Sip.Provider.TransactionsRepository;
import SoftPhone.Protocol.Sip.Call.CallParticipantInterface;
import SoftPhone.Protocol.Sip.Call.CallSession;
import SoftPhone.Protocol.Sip.Provider.RegisterSequence;
import SoftPhone.Protocol.Sip.*;
import SoftPhone.Protocol.Sip.Account.SipCredentials;
import SoftPhone.Protocol.Sip.Message.*;
import SoftPhone.Protocol.Sip.Message.Event.*;
import SoftPhone.Protocol.Sip.UserAgent.Event.OutGoingCallAcceptedEvent;
import SoftPhone.Protocol.Sip.UserAgent.Event.ProxyAuthRequired;
import SoftPhone.Protocol.Sip.UserAgent.Event.RegistredEvent;
import SoftPhone.Protocol.Sip.UserAgent.Event.wwwAuthRequired;
import gov.nist.javax.sip.header.CSeq;
import gov.nist.javax.sip.stack.SIPDialog;
import gov.nist.javax.sip.stack.SIPTransaction;
import java.util.ArrayList;
import javax.sip.*;
import javax.sip.address.Address;
import javax.sip.header.CSeqHeader;
import javax.sip.header.ContactHeader;
import javax.sip.message.*;

public class UserAgentClient 
{
    private SipMessageDispatcher sipMessageRouter;
    private SipMessageBank sipMessageBank;
    private DialogsRepository dialogs;
    private TransactionsRepository transactions;
    private ArrayList<UserAgentListener> userAgentClientListeners=null;
    private SipLayerClientListener sipLayerListener;
 
   public UserAgentClient(
                             final SipMessageDispatcher sipMessageRouter
                            ,final SipMessageBank sipMessageBank
                            ,DialogsRepository d
                            ,TransactionsRepository t
                          )
   {
       this.sipMessageRouter =sipMessageRouter;
       this.sipMessageBank =sipMessageBank;
       this.userAgentClientListeners =new ArrayList<UserAgentListener>();
       dialogs =d;
       transactions =t;

       sipLayerListener =new SipLayerClientListener()
       {
            public void processProvisionalResponse(ProvisionnalResponseEvent event)
            {
                String method =((CSeqHeader)event.getResponse()
                                    .getHeader(CSeqHeader.NAME)).getMethod();
                System.out.println(method);
                if(method.equalsIgnoreCase(Request.INVITE))
                {
                    if(event.getResCode()==100)
                    {
                        System.out.println("Trying");
                        fireTryingJoinEvent();
                    }
                    else if(event.getResCode()==180)
                    {
                        fireRingingEvent();
                    }
                    else if(event.getResCode()==183)
                    {
                        fireSessionInProgressEvent();
                    }
                    else
                    {
                        fireProvisionnalResponseEvent();
                    }


                }
            }

            public void processInviteOkResponse(InviteOKResponseEvent event)
            {
                Response r = event.getResponse();
                byte[]contentBytes =r.getRawContent();
                String content =new String(contentBytes);
                String dialogId =event.getDialogId();
                Dialog dialog =dialogs.get(dialogId);
                long cseqNum=event.getCseqNum();
                fireOutGoingCallAccepted(dialogId,content);
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
                fireRedirectCallEvent();

            }

            public void processErrorClientResponse(ClientErrorResponseEvent ev)
            {
               int clientErrorCode=ev.getResCode();
               String m =ev.getRequest().getMethod();
               Response response =ev.getResponse();
               long cseqnum =ev.getCseqNum();
               switch(clientErrorCode)
                {
                   case 401:
                      firewwwAuthRequiredEvent(ev.getRequest()
                                              ,response
                                              ,ev.getCseqNum()
                                              );
                    
                   break;
                   case 407:
                    
                       fireProxyAuthRequestedEvent(ev.getRequest()
                                                  ,response
                                                  ,ev.getCseqNum()
                                                  );

                   break;
                   default:fireCallFailsEvent();

               }
            }

            public void processServerErrorResponse(ServerErrorResponseEvent ev)
            {
                fireCallFailsEvent();

            }

            public void processGlobalErrorResponse(GlobalErrorResponseEvent ev)
            {
                fireCallFailsEvent();
            }

            public void processErrorClientResponse(RedirectEvent event)
            {
                fireCallFailsEvent();
            }
       };
  
   sipMessageRouter.addSipLayerClientListener(sipLayerListener);
   }

   public void AuthRequest(Request request, Response response, long cseqnum, SipCredentials credendials, String offer)
   {
       Request r =sipMessageBank.authenticateRequest(request, cseqnum, response, credendials, "");
   }

    public void Invite(CallSession callSession)
    {
       
       String offer =callSession.getSdpOffer();
       Request request =sipMessageBank.getInviteRequest(callSession,null,offer);
       callSession.setCurrentRequest(request);
       sipMessageRouter.SendRequest(request);
    }

   
   public void Invite(CallSession callSession,long cseqNum,Response response)
    {
       
       String offer =callSession.getSdpOffer();
       Request request =sipMessageBank.getInviteRequest(
                                                              callSession
                                                              ,response
                                                              ,offer
                                                              );
       callSession.setCurrentRequest(request);
       sipMessageRouter.SendRequest(request);

    }
   /*Met fin a une session d'appel envoie une requete BYE*/
    public  void Bye(String sessionID,CallParticipantInterface  callParticipant, Request request)
    {
       SIPDialog dialog= dialogs.get(sessionID);

       Request r =sipMessageBank.getByeRequest(dialog,callParticipant, request);
       sipMessageRouter.SendRequest(dialog,r);
       
    }
    /*Cancel
     @param sessionID l'identifiant dela transaction de la requête à annuler
     @param r requête à annuler doit être de type "INVITE"
     * la requête Cancel n'est envoyée que si la requête à annuler à reçue
     * une réponse provisoire et aucune réponse définitive:
     * état PROCEEDING d'une transaction "INVITE"
     */
    public void Cancel(String sessionID,Request r)
    {//TODO debug exception when try to cancel a call which as not receive a response and is not yet
    	//in the sip message bank ?
        if(r.getMethod().equalsIgnoreCase("INVITE"))
        {
             Request request =sipMessageBank.getCancelRequest(r);
             SIPTransaction transaction =transactions.getTransaction(sessionID);
             if(transaction!=null)
             {
                TransactionState state =transaction.getState();
                if(state.equals(TransactionState.PROCEEDING))
                {
                    sipMessageRouter.SendRequest(request);
                }

             }
        }
       
    }

    /*Register
     @param registerSequence tentative d'enregistrement*/

   public void Register(RegisterSequence registerSequence)
    {
        long cseqnum =1;
		Request request = sipMessageBank.getRegisterRequest(registerSequence
                                                                ,cseqnum
                                                                , null
                                                                );
		sipMessageRouter.SendRequest(request);
	}

   /*Register authentifie une tentative d'enregistrement échouée
     @param registerSequence tentative d'enregistrement
     @param  cseqNum numéro de sequence de la requête "Register" précédente
     @param response réponse demandant l'authentification de la demande*/
    public void Register(RegisterSequence registerSequence
                        ,long cseqNum
                        ,Response response
                        )
    {
        long cseqnum =cseqNum;
		Request request = sipMessageBank.getRegisterRequest(
                                                                registerSequence
                                                                ,cseqnum
                                                                , response
                                                                );
        
		sipMessageRouter.SendRequest(request);
	}

private void fireProvisionnalResponseEvent()
{
    for(UserAgentListener listener:userAgentClientListeners)
          {
              listener.processProvisionnal();
          }
}


private void fireTryingJoinEvent()
{
    for(UserAgentListener listener:userAgentClientListeners)
      {
          listener.processTryingJoin();
      }

}

private void fireRingingEvent()
{
    for(UserAgentListener listener:userAgentClientListeners)
      {
          listener.processRinging();
      }
}

private void fireSessionInProgressEvent()
{
    for(UserAgentListener listener:userAgentClientListeners)
      {
          listener.processProgressing();
      }
}

/*fireOutGoingCallAccepted avertis les objets à l'écoute que l' INVITE
à été accepté par le participant distant et que le dialogue correspondant
à la session est établi ,transmet également la réponse à l'offre média
de l'INVITE
@param dialogId identifiant du dialogue établi pour la session
@param sdpResponse reéponse à l'offre média de l'INVITE*/
public void fireOutGoingCallAccepted(String dialogId,String sdpResponse)
{
    OutGoingCallAcceptedEvent callAcceptedEvent =
            new OutGoingCallAcceptedEvent(this, dialogId, sdpResponse);
    for(UserAgentListener l:userAgentClientListeners)
    {
        l.processOutGoingCallEvent(callAcceptedEvent);
    }
}

private void fireRegistred(Address addr,int expires)
{
    RegistredEvent event =new RegistredEvent(this, addr,expires);
    for(UserAgentListener l:userAgentClientListeners)
    {
        l.processRegistredEvent(event);
    }

}


private void fireRedirectCallEvent()
{
    for(UserAgentListener listener:userAgentClientListeners)
    {
        listener.processRedirect();
    }
}

private void firewwwAuthRequiredEvent(
                                      Request request
                                     ,Response response
                                     ,long csNum
                                     )
{
    wwwAuthRequired event =new wwwAuthRequired(this,request,response,csNum);

    for(UserAgentListener listener:userAgentClientListeners)
    {
        listener.processWWWAuthRequired(event);
    }

}

     private void fireProxyAuthRequestedEvent(
                                              Request request
                                             ,Response response
                                             ,long cseqNum
                                             )
     {
          ProxyAuthRequired authRequired =new ProxyAuthRequired(
                                                                this
                                                               ,request
                                                               ,response
                                                               ,cseqNum
                                                               );
          for(UserAgentListener listener:userAgentClientListeners)
          {
              listener.processProxyAuthenticate(authRequired);
          }
     }

     private void fireCallFailsEvent()
    {
        for(UserAgentListener listener:userAgentClientListeners)
          {
              listener.processCallFails();
          }
    }


     public void addUserAgentClientListener(UserAgentListener ual)
    {
        userAgentClientListeners.add(ual);
    }

    public void removeUserAgentClientListener(UserAgentListener ual)

    {
        if(!userAgentClientListeners.isEmpty())
        {
            userAgentClientListeners.remove(ual);
        }
    }

   
}
