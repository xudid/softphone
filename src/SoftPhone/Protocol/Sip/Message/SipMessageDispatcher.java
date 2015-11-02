/*
 * 
 */

package SoftPhone.Protocol.Sip.Message;

import SoftPhone.Protocol.Sip.Provider.DialogsRepository;
import SoftPhone.Protocol.Sip.Provider.SipDispatcherServerListener;
import SoftPhone.Protocol.Sip.Provider.SipLayerClientListener;
import SoftPhone.Protocol.Sip.Provider.SipStackActivator;
import SoftPhone.Protocol.Sip.Provider.TransactionsRepository;
import SoftPhone.Protocol.Sip.Message.Event.DialogEstablishedEvent;
import SoftPhone.Protocol.Sip.Message.Event.ClientErrorResponseEvent;
import SoftPhone.Protocol.Sip.Message.Event.RedirectResponseEvent;

import SoftPhone.Protocol.Sip.Message.Event.GlobalErrorResponseEvent;
import SoftPhone.Protocol.Sip.Message.Event.IncommingCallEvent;
import SoftPhone.Protocol.Sip.Message.Event.InviteOKResponseEvent;

import SoftPhone.Protocol.Sip.Message.Event.ProvisionnalResponseEvent;
import SoftPhone.Protocol.Sip.Message.Event.RegisterOkResponseEvent;

import SoftPhone.Protocol.Sip.Message.Event.ServerErrorResponseEvent;
import SoftPhone.Protocol.Sip.*;



import gov.nist.javax.sip.message.SIPResponse;
import gov.nist.javax.sip.stack.SIPClientTransaction;
import gov.nist.javax.sip.stack.* ;
import gov.nist.javax.sip.stack.SIPServerTransaction;
import gov.nist.javax.sip.stack.SIPTransaction;
import java.util.*;
import java.util.logging.*;
import javax.sip.*;
import javax.sip.header.*;
import javax.sip.message.*;


/**
 *
 * @author didier
 */
public class SipMessageDispatcher implements SipListener{

    private ArrayList<SipLayerClientListener> sipLayerClientListeners;
    private ArrayList<SipDispatcherServerListener> sipLayerServerListeners;
    private SipProvider sipProviderTCP;
    private SipProvider sipProviderUDP;
    private TransactionsRepository transactions;
    private DialogsRepository dialogs;
    private HashMap<String,ServerTransaction>serverTransactionMap;
    private SipStackActivator activator;

    public SipMessageDispatcher(SipStackActivator sipStack,DialogsRepository d,TransactionsRepository t)
    {
        sipLayerClientListeners = new ArrayList<SipLayerClientListener>();
        sipLayerServerListeners= new ArrayList<SipDispatcherServerListener>();
        activator=sipStack;
        transactions=t;
        dialogs =d;
        serverTransactionMap=new HashMap<String, ServerTransaction>();
        this.sipProviderTCP =sipStack.getSipProviderTCP();
        this.sipProviderUDP=sipStack.getSipProviderUDP();
        try
            {
                sipProviderTCP.addSipListener(this);
                sipProviderUDP.addSipListener(this);
            }
        catch (TooManyListenersException ex)
            {
                Logger.getLogger(SipMessageDispatcher.class.getName())
                        .log(Level.SEVERE, null, ex);
            }


    }



    public void processRequest(RequestEvent reqEvent)
        {
        Request r =reqEvent.getRequest();
     
        String method =r.getMethod();
        System.out.println(method);
        ServerTransaction serverTransaction;
        Dialog dialog=null;

        if(SipUtils.isMethodSupported(method))
        {
            
               serverTransaction= reqEvent.getServerTransaction();

               if(serverTransaction==null)
               {
                try {
                    serverTransaction = sipProviderUDP.getNewServerTransaction(r);
                } catch (TransactionAlreadyExistsException ex) {
                    Logger.getLogger(SipMessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TransactionUnavailableException ex) {
                    Logger.getLogger(SipMessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
                }
               }
               dialog = reqEvent.getDialog();
               

                RequestEvent re =new RequestEvent(this, serverTransaction, dialog, r);
                fireIncommingRequestEvent(re);
            
        }
        else
        {
            fireUnsupportedMethodEvent(r);
        }
        
        }

    public void processResponse(ResponseEvent respEvent)
    {
       SIPResponse response =(SIPResponse) respEvent.getResponse();
       String branch =((ViaHeader)response.getHeader("Via")).getBranch();

        SIPTransaction ct =(SIPTransaction) transactions.getTransaction(branch);
        int responseStatus =response.getStatusCode();
        CSeqHeader responseCsh=(CSeqHeader) response.getHeader("Cseq");
        long responseCseqNum =responseCsh.getSeqNumber();
          String responseMethod =responseCsh.getMethod();

      if(ct!=null && ct instanceof ClientTransaction)
      {
          Request request =ct.getRequest();
          CSeqHeader requestCsh=(CSeqHeader) request.getHeader("Cseq");
          long requestCseqNum =requestCsh.getSeqNumber();
          String requestMethod =requestCsh.getMethod();


           if((responseCseqNum ==requestCseqNum)&&(responseMethod.equalsIgnoreCase(requestMethod)))
                        {

                    switch(responseStatus/100)

                    {

                    case 1:fireProvisionnalResponseEvent( response,responseStatus , responseCseqNum);
                    break;
                    case 2:if (requestMethod.equalsIgnoreCase("INVITE"))
                        {
                        try 
                        {

                SIPDialog dialog =new SIPDialog((SIPClientTransaction)ct, response);
                String dialogId= dialog.getDialogId();
                dialogs.add(dialogId, dialog);
                Dialog d= dialogs.get(dialogId);
                request=dialog.createAck(requestCseqNum);
                dialog.sendAck(request);
                fireInviteOkResponseEvent(requestCseqNum, response, dialogId);
                        }

                        catch (InvalidArgumentException ex)
                        {
                        Logger.getLogger(SipMessageDispatcher.class.getName())
                                .log(Level.SEVERE, null, ex);
                        }                        catch (SipException ex)
                        {
                        Logger.getLogger(SipMessageDispatcher.class.getName())
                                .log(Level.SEVERE, null, ex);
                        }

                    }

                    if(requestMethod.equalsIgnoreCase("REGISTER"))
                    {
                    fireRegisterOkResponseEvent(requestCseqNum, response, 200);
                    }
                    break;
                        case 3:
            fireRedirectResponseEvent(requestCseqNum, response, responseStatus);
                    break;
                    
                        case 4:
                            fireClientErrorResponseEvent(
                                                        response
                                                        ,request
                                                        ,responseCseqNum
                                                        ,responseStatus
                                                        );
                        
                       
                        break;
                        case 5:
         fireServerErrorResponseEvent(requestCseqNum, response, responseStatus);
                         break;
                        case 6:
              fireGlogalResponseEvent(requestCseqNum, response, responseStatus);
                         break;
                        default:;

      }
           }

      }

      else
      {     
      }

    }

    public void processTimeout(TimeoutEvent te)
    {
        System.out.println(te.toString());
    }

    public void processIOException(IOExceptionEvent arg0)
    {
       /*TODO manage transport error*/
    }

    public void processTransactionTerminated(TransactionTerminatedEvent tte)
    {
        if(tte.isServerTransaction())
        {
           Transaction t= tte.getServerTransaction();
           String transactionID =t.getBranchId();
           transactions.removeTransaction(transactionID,(SIPTransaction) t);
        }
        else
        {
           Transaction t= tte.getClientTransaction();

            String transactionID =t.getBranchId();
            t.getRequest();
          transactions.removeTransaction(transactionID,(SIPTransaction) t);


        }
    }

    public void processDialogTerminated(DialogTerminatedEvent dte)
    {
       System.out.println(dte.toString());
    }

     public void addSipLayerClientListener(SipLayerClientListener
             sipLayerClientListener)
    {
       sipLayerClientListeners.add(sipLayerClientListener);

    }

    public void addSipLayerServerListener(SipDispatcherServerListener
            sipLayerServerListener)
    {
        sipLayerServerListeners.add(sipLayerServerListener);
    }

    public void SendRequest(Request request)
    {
        try {
            Request r = (Request) request.clone();
            SIPClientTransaction ct = 
               (SIPClientTransaction) sipProviderUDP.getNewClientTransaction(r);
            transactions.addTransaction(ct.getBranchId(), ct);
            ct.sendRequest();
            } 
        catch (SipException ex) 
        {
            Logger.getLogger(SipMessageDispatcher.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
     }
    
    public void SendRequest(SIPDialog d,Request r)
    {
        SIPClientTransaction ct ;
        try {
        ct =(SIPClientTransaction) sipProviderUDP.getNewClientTransaction(r);
        String transactionID =ct.getBranchId();
        transactions.addTransaction(transactionID, ct);
        Dialog dialog = d;
        dialog.sendRequest(ct);
            }

        catch (TransactionDoesNotExistException ex)
            {
                Logger.getLogger(SipMessageDispatcher.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
        catch (SipException ex)
            {
                Logger.getLogger(SipMessageDispatcher.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
    }

    

    public void SendResponse(String tid ,Response response) {
        DialogEstablishedEvent establishedEvent;
        try 
            {
                SIPServerTransaction transaction =
                        (SIPServerTransaction) transactions.getTransaction(tid);
        if(transaction.isInviteTransaction()&&(response.getStatusCode()==200))
        {
           Dialog d = transaction.getDialog();
           if(d==null)
           {
               d=sipProviderUDP.getNewDialog(transaction);
           }

        }
                transaction.sendResponse(response);
               Dialog dialog= transaction.getDialog();
           if(dialog!=null&&(dialog.getState().equals(DialogState.CONFIRMED)))
           {
              String dialogID =dialog.getDialogId();
              dialogs.add(dialogID, (SIPDialog)dialog);
              establishedEvent =new DialogEstablishedEvent(dialog, dialogID);
            for(SipDispatcherServerListener l:sipLayerServerListeners)
           {
               l.processNewDialog(establishedEvent);
           }
           }
               
              

            }
        catch (SipException ex)
        {
            System.out.println("erreur SIP");
            Logger.getLogger(SipMessageDispatcher.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
       

    }

    

    private void fireIncommingRequestEvent(RequestEvent requestEvent) {
       for(SipDispatcherServerListener l: sipLayerServerListeners)
       {
           l.requestReceived(requestEvent);
       }
    }

    private void fireProvisionnalResponseEvent(Response response,int resCode,long cseqNum)
    {
         ProvisionnalResponseEvent event =
             new  ProvisionnalResponseEvent(this, resCode, cseqNum, response);
         for(SipLayerClientListener listener :sipLayerClientListeners)
        {
             System.out.println("Trying");
            listener.processProvisionalResponse(event);
        }
    }

    private void fireInviteOkResponseEvent(long cseqNum,Response response,String dialogId)
    {
        InviteOKResponseEvent event =
            new InviteOKResponseEvent(this, 200, cseqNum, response, dialogId);
        for(SipLayerClientListener listener :sipLayerClientListeners)
        {
            listener.processInviteOkResponse(event);
        }
    }

    private void fireRegisterOkResponseEvent(long cseqNum,Response response,int registredTime)
    {
        RegisterOkResponseEvent  event =new RegisterOkResponseEvent(this, 200, cseqNum, response);
        for(SipLayerClientListener listener :sipLayerClientListeners)
        {
            listener.processRegisterOkResponse(event);
        }
    }

    private void fireRedirectResponseEvent(long cseqNum,Response response,int resCode)
    {
        RedirectResponseEvent rre =new  RedirectResponseEvent(this, resCode, cseqNum, response);
         for(SipLayerClientListener listener :sipLayerClientListeners)
        {
            listener.processRedirectResponse(rre);
        }
    }

    private void fireClientErrorResponseEvent(Response  response,Request request,long cseqNum, int resCode)
    {
        ClientErrorResponseEvent event =new ClientErrorResponseEvent(this,request, response,cseqNum, resCode);
        for(SipLayerClientListener listener:sipLayerClientListeners)
        {
            listener.processErrorClientResponse(event);
        }

    }

    private void fireServerErrorResponseEvent(long cseqNum,Response response,int resCode)
    {
        ServerErrorResponseEvent ser = new ServerErrorResponseEvent(response, resCode, cseqNum, response);
         for(SipLayerClientListener listener:sipLayerClientListeners)
        {
            listener.processServerErrorResponse(ser);

        }
    }

    private void fireGlogalResponseEvent(long cseqNum,Response response,int resCode)
    {
        GlobalErrorResponseEvent gere =new GlobalErrorResponseEvent(response, resCode, cseqNum, response);
         for(SipLayerClientListener listener:sipLayerClientListeners)
        {
            listener.processGlobalErrorResponse(gere);
        }

    }

    private void fireUnsupportedMethodEvent(Request r) 
    {
        System.out.println("unsupported method");
    }
}
