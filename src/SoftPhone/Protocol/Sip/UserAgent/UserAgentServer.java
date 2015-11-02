/*
 * MODIFY THIS CLASS TO PASS AN INCOMMINGCALLEVENT TO THE SOFTPHONE
 * IF THE INCOMMING CALL CONTAINS AN SDP OFFER PUT THIS OFFER INTO THE INCOMMINGCALLEVENT
 * the incommingcallevent must have the remotecallparticipant sipaddress
 * and the request
 */

package SoftPhone.Protocol.Sip.UserAgent;

import SoftPhone.Protocol.Sip.Call.CallSession;
import SoftPhone.Protocol.Sip.Account.SipAccount;
import SoftPhone.Protocol.Sip.Provider.SipDispatcherServerListener;
import SoftPhone.Configuration.Network.NetworkConfiguration;
import SoftPhone.Protocol.Sip.Message.SipMessageBank;
import SoftPhone.Protocol.Sip.Message.SipMessageDispatcher;
import SoftPhone.Protocol.Sip.Message.Event.*;

import SoftPhone.Protocol.Sip.Provider.DialogsRepository;
import SoftPhone.Protocol.Sip.Provider.TransactionsRepository;
import SoftPhone.Protocol.Sip.UserAgent.Event.TerminatedCallEvent;
import gov.nist.javax.sip.header.To;
import gov.nist.javax.sip.stack.SIPDialog;
import gov.nist.javax.sip.stack.SIPServerTransaction;
import gov.nist.javax.sip.stack.SIPTransaction;
import java.util.ArrayList;
import javax.sip.RequestEvent;
import javax.sip.TransactionState;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.ContentLengthHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.RequireHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.*;


/**
 *
 * @author didier
 */
public class UserAgentServer implements SipDispatcherServerListener
{
    private SipMessageDispatcher sipMessageRouter;
    private SipAccount account;
    private ArrayList<UserAgentListener> userServerListeners;
    private SipMessageBank sipMessageBank;
    private NetworkConfiguration networkConfiguration;
    private DialogsRepository dialogs;
    private TransactionsRepository transactions;

    public  UserAgentServer(SipMessageDispatcher sipMessageRouter
                           ,SipMessageBank sipMessageBank
                           ,NetworkConfiguration networkConfiguration
                           ,SipAccount account, DialogsRepository dialogs,TransactionsRepository transactions)
  {
     this.sipMessageRouter=sipMessageRouter;
     this.sipMessageBank=sipMessageBank;
     this.account=account;
     this.networkConfiguration=networkConfiguration;
     this.userServerListeners =new ArrayList<UserAgentListener>();
     this.transactions=transactions;
     this.dialogs=dialogs;
     sipMessageRouter.addSipLayerServerListener(this);
  }

    public void addUserAgentServerListener(UserAgentListener ual)
    {
        userServerListeners.add(ual);
    }

    public void rejectCall(CallSession callSession, String reason)
    {
        Response response;
        Request request =callSession.getIncommingCallRequest();
        String transactionID =callSession.getCallSessionID();
        if(reason.equalsIgnoreCase("BUSY"))
        {
           response= sipMessageBank.get486Response(request);
            sipMessageRouter.SendResponse(transactionID, response);
        }
        else if(reason.equalsIgnoreCase("REFUSED"))
        {
           response= sipMessageBank.getResponse(603, request);
           sipMessageRouter.SendResponse(transactionID, response);
        }
        else if(reason.equalsIgnoreCase("NOTACCEPTABLE"))
        {
            
        }

    }
        

    public void requestReceived(RequestEvent requestEvent)
    {

        
        Request  request =requestEvent.getRequest();
        To to=(To) request.getHeader(ToHeader.NAME);
        RequireHeader rh =(RequireHeader) request.getHeader(RequireHeader.NAME);
        ContentLengthHeader clh=(ContentLengthHeader)
                                    request.getHeader(ContentLengthHeader.NAME);
        ContentTypeHeader cth=(ContentTypeHeader)
                                       request.getHeader(ContentTypeHeader.NAME);
        String contentType="";
        if(cth!=null)
        {
           String type =cth.getContentType();
        String subtype=cth.getContentSubType();
         contentType =type+"/"+subtype;
        }
        
       URI requestUri=request.getRequestURI();
       if(requestUri.isSipURI())
       {
               if(validateReceiver((SipURI)requestUri))
               {
                   if((to.hasTag())&&(!to.getTag().isEmpty()))
                   {
                       //Tester l'entête Require ,doit être absent ou null
                      if(rh==null)
                      {/*
                       Tester la longueur et le type de contenu
                        si le contenu est différents  de application/sdp
                        répondre avec réponse 415 "type de support non
                        supporté"
                       */
                          if(clh.getContentLength()!=0&&contentType.equalsIgnoreCase("application/sdp"))
                          {
                             processRequest(request,null);
                          }
                          else
                          {
                              if(clh.getContentLength()!=0)
                              {
                                 Response response =sipMessageBank.get415Response(request);
                                 sipMessageRouter.SendResponse(contentType, response);
                              }
                              else
                              {
                                  processRequest(request,requestEvent);
                                  System.out.println("La requete na pas de tag to");
                              }
                             
                          }
                          
                          
                      }
                      //L'ENTÊTE REQUIRE EST PRESENT =>REPONSE 420 (SOFTPHONE)
                      else
                      {
                         Response response=sipMessageBank.get420Response(request,rh);
                         System.out.println("La requete possede un champ require");
                      }
                       
                   }
                   //TO Header n'à pas d'étiquette TAG pas de dialogue 
               /*Vérifier si la demande ne corresponde pas a une demande en cours */
                   else
                   {
                       if(rh==null)
                      {System.out.println("la requete n' as pas d'header require");
                          /*
                           Tester la longueur et le type de contenu
                            si le contenu est différents  de application/sdp
                            répondre avec réponse 415 "type de support non
                            supporté"
                           */
                          if(clh.getContentLength()!=0&&contentType.equalsIgnoreCase("application/sdp"))
                          {
                              System.out.println("la requete à un corp de message de type SDP");
                             processRequest(request,requestEvent);
                          }
                          else
                          {
                              if(clh.getContentLength()!=0)
                              {
                                 Response response =sipMessageBank.get415Response(request);
                                 sipMessageRouter.SendResponse(contentType, response);
                              }
                              else
                              {
                                  processRequest(request,requestEvent);
                                  
                              }

                          }


                      }
                      //L'ENTÊTE REQUIRE EST PRESENT =>REPONSE 420 (SOFTPHONE)
                      else
                      {
                         Response response=sipMessageBank.get420Response(request,rh);
                         sipMessageRouter.SendResponse(contentType, response);
                         
                      }

                   }

               }
                //la requête n'est pas destiné au compte SIP courant
            else
            {
               Response response= sipMessageBank.getResponse(404,request);
               sipMessageRouter.SendResponse(contentType, response);
            }
       }

       //le format d'URI n'est pas supporté
       else
       {
          Response response= sipMessageBank.getResponse(416,request);
          sipMessageRouter.SendResponse(contentType, response);
       }
    }

    public void sendInviteOK(CallSession currentCall)
    {
        Request request =currentCall.getIncommingCallRequest();
        String tid=currentCall.getCallSessionID();
        Response response= sipMessageBank.getOKINVITEResponse(request,currentCall);
        sipMessageRouter.SendResponse(tid, response);
    }


    private void processRequest(Request request,RequestEvent ev)
    {
        Request r =ev.getRequest();
        String method =r.getMethod();
        SIPServerTransaction serverTransaction=
                (SIPServerTransaction) ev.getServerTransaction();
        SIPDialog dialog=(SIPDialog) ev.getDialog();
        long cseqnum =serverTransaction.getCSeq();
        String branchID =serverTransaction.getBranchId();
        transactions.addTransaction(branchID,(SIPTransaction) serverTransaction);
        String dialogID="";
        if(dialog!=null)
        {
            dialogID =dialog.getDialogId();
        dialogs.add(dialogID, dialog);

        }
        if (serverTransaction.isInviteTransaction())
                    {
                        processInviteRequest(request,branchID,dialogID,cseqnum);
                    }
                   else if (method.equalsIgnoreCase("ACK"))
                   {
                       processAckRequest();
                   }

                   else if (serverTransaction.isByeTransaction())
                   {
                        processByeRequest(request,branchID,dialogID,cseqnum);
                   }

                   else if (serverTransaction.isCancelTransaction())
                   {
                        processCancelRequest(request,branchID,cseqnum);
                   }

                   else if (method.equalsIgnoreCase("OPTIONS"))
                   {
                        processOptionRequest(ev.getRequest());
                   }
                   else
                   {
                        /*La methode n'est pas supporté et la réponse est déja
                         traité*/

                   }

    }

	protected void processInviteRequest(Request request,String tid,String did,long cseqNum)
    {


       String branchID = tid;
       String dialogID = did;
       SIPTransaction t = transactions.getTransaction(tid);
       SIPDialog dialog = dialogs.get(did);
       if(dialog==null)
       {
        IncommingCallEvent event =
                            new IncommingCallEvent(this, tid, request, cseqNum);
        for(UserAgentListener listener:userServerListeners)
        {
            listener.processIncommingCallEvent(event);
        }
          Response response= sipMessageBank.getResponse(180, request);
      sipMessageRouter.SendResponse(tid, response);
       }
       else
       {
           /*C'est un réinvite on y répond par une réponse
            488 Non Acceptable ici*/
          SIPDialog existingDialog=(SIPDialog) t.getDialog();
          Response reponse =sipMessageBank.getResponse(488, request);
          sipMessageRouter.SendResponse(tid, reponse);
          
       }
	}

    protected void processByeRequest(Request request,String tid,String did,long cseqNum)
    {
        String branchID = tid;
       String dialogID = did;
       SIPTransaction t = transactions.getTransaction(tid);
       SIPDialog dialog = dialogs.get(did);

         if(dialog==null)
         {
            Response response= sipMessageBank.getResponse(481, request);
             sipMessageRouter.SendResponse(tid, response);
         }
         else
         {
             long dialogCseq =dialog.getRemoteSeqNumber();
             if(dialogCseq==0)
             {
                 dialogCseq=t.getCSeq();
             }else if(t.getCSeq()<dialog.getRemoteSeqNumber())
             {
               Response response=  sipMessageBank.getResponse(500, request);
                 sipMessageRouter.SendResponse(tid, response);
             }
             else
             {
                  dialogCseq=t.getCSeq();
                TerminatedCallEvent event = null;
        for(UserAgentListener listener:userServerListeners)
        {
            listener.processTerminatedCallEvent(event);
        }
       Response response= sipMessageBank.getResponse(200, request);
       sipMessageRouter.SendResponse(tid, response);
       
             }
         }

    }

	protected void processCancelRequest(Request r,String tid,long cseqnum)
    {
        String branchID = tid;

       SIPTransaction t = transactions.getTransaction(branchID);

       if(t==null)
       {
         Response response=  sipMessageBank.getResponse(481, r);
           sipMessageRouter.SendResponse(tid, response);
       }

       else
       {
           if(t.isInviteTransaction()&&(t.getState().equals(TransactionState.PROCEEDING)))
           {
               System.out.println("annulation de l'appel entrant en cours");
              
               for(UserAgentListener listener:userServerListeners)
        {
             System.out.println("Transmission fin d'appel au softphone");
            listener.processCancelledCallEvent();


        }
             Request request=  t.getOriginalRequest();
               Response response =sipMessageBank.getResponse(487, request);
               System.out.println(response);
               sipMessageRouter.SendResponse(tid, response);
               
           }
       }

	}

	
	

	protected void processOptionRequest(Request request)
    {
		
		

	}

    private void processAckRequest()
    {
        System.out.println("Requete ACK recu");
    }

    public void processNewDialog(DialogEstablishedEvent event)
    {
        for(UserAgentListener l:userServerListeners)
        {
            l.processDialogEstablishedEvent(event);
        }
    }

    

    

    private boolean validateReceiver(SipURI uRI)
    {
        boolean b=(uRI.getUser().equalsIgnoreCase(account.getUserInfo()));
        boolean b1=(uRI.getHost().equalsIgnoreCase(account.getUserDomain()));
        boolean b2=(uRI.getHost().equalsIgnoreCase(networkConfiguration.getLocalIp()));

        return (b&(b1|b2));
    }

	



}
