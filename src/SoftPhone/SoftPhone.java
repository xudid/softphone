/*
 * eliminer le code redondant d'avec UAS concernant le test "application/sdp"
 * déterminer les tâches dans softphone (en vue du multithread)
 * traiter les évènements vers l'ui dans des threads séparé ?
 * vérifier l'encapsulation et la publication de l'état
 * déterminer les accès concurrents
 * synchroniser les méthodes
 */

package SoftPhone;

import SoftPhone.Bell.Bell;
import SoftPhone.Bell.BellController;
import SoftPhone.Protocol.Sip.Message.Event.DialogEstablishedEvent;
import SoftPhone.Protocol.Sip.Message.Event.IncommingCallEvent;
import SoftPhone.Protocol.Sip.Provider.RegisterSequence;
import SoftPhone.Protocol.Sip.Call.CallSession;
import SoftPhone.Protocol.Sip.Account.SipAccount;
import SoftPhone.Media.*;
import SoftPhone.Contact.ContactBook;
import SoftPhone.Network.NetworkUtils;
import SoftPhone.Protocol.Sip.Call.CallParticipant;
import SoftPhone.Protocol.Sip.Call.CallParticipantInterface;
import SoftPhone.Protocol.Sip.UserAgent.Event.*;
import SoftPhone.Protocol.Sip.UserAgent.UserAgentServer;
import SoftPhone.Protocol.Sip.UserAgent.UserAgentClient;
import SoftPhone.Protocol.Sip.UserAgent.UserAgentListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sdp.SdpParseException;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentLengthHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import static java.util.concurrent.TimeUnit.SECONDS;
/**
 *
 * @author didier
 */
public class SoftPhone
    {
        private UserAgentClient userAgentClient;
        private UserAgentServer userAgentServer;
        private ContactBook contactBook;
        private final MediaController mediaController;
        private SdpProcessor sdpProcessor;
        private SipAccount account;
        private RegisterSequence currentRegisterSequence;
        private CallSession currentCall;
       // private CallSession incommingCall;
        private boolean online=false;
        private ArrayList<SoftPhoneListener> softPhoneListeners;
        private ConfigurationProxy configurationProxy;
        private BellController bellController;

        public SoftPhone( final SipAccount account
                        ,UserAgentClient userAgentClient
                        ,UserAgentServer userAgentServer
                        ,ContactBook book
                        ,MediaController mc
                        ,SdpProcessor sp
                        ,ConfigurationProxy configurationProxy
                        )
        {
            this.userAgentClient = userAgentClient;
            this.userAgentServer = userAgentServer;
            this.mediaController =mc;
            this.contactBook =book;
            this.account=account;
            this.sdpProcessor=sp;
            this.bellController =new BellController(new Bell());
            this.configurationProxy=configurationProxy;
            softPhoneListeners =new ArrayList<SoftPhoneListener>();
            this.addListener(bellController);
            UserAgentListener ual =new UserAgentListener()
            {




            public void processIncommingCallEvent(IncommingCallEvent event)
            {
                String sipAddress;
                String offer="";
                 Request request =event.getRequest();
                if(online)
                {
                    RejectIncommingCall(request,"BUSY");
                }
                else
                {
                    online=true;
                    sipAddress=((ContactHeader)request
                        .getHeader(ContactHeader.NAME)).getAddress().toString();
                    String sessionID=event.getTransactionID();
                    CallParticipantInterface remoteCallParticipant =new
                            CallParticipant(sipAddress);
                    int contentLength=((ContentLengthHeader)request
                       .getHeader(ContentLengthHeader.NAME)).getContentLength();


                    if(contentLength>0)
                    {
                         String cType=
                ((ContentTypeHeader)request.getHeader(ContentTypeHeader.NAME))
                                 .getContentType();
                   String subType=
                ((ContentTypeHeader)request.getHeader(ContentTypeHeader.NAME))
                           .getContentSubType();
                   StringBuilder b =new StringBuilder();
                   b.append(cType);
                   b.append("/");
                   b.append(subType);
                   //THIS is already processed in useragentserver
                   if(b.toString().equalsIgnoreCase("application/sdp"))
                   {
                       offer =new String(request.getRawContent());
                        System.out.println(offer);
                        currentCall =
                         new CallSession(account, remoteCallParticipant, offer);
                        currentCall.setIncommingCallRequest(request);
                        currentCall.setCallSessionID(sessionID);
                        String sdpresponse = sdpProcessor
                                            .makeSdpResponse(account.getDisplayName(), offer);
                        System.out.println(sdpresponse);
                        currentCall.setSdpResponse(sdpresponse);
                        RTPSessionConfiguration config =sdpProcessor
                                .getIncommingCallRTPConf(offer, sdpresponse);
//                        incommingCall=new CallSession(account
//                                                 ,remoteCallParticipant
//                                                 ,offer
//                                                 );
                        currentCall.setRtpConf(config);

                        fireIncommingCallEvent();

                   }

              else {
                       String sdpOffer =sdpProcessor.makeSDPOffer(account.getAccountName());
                       currentCall= new CallSession(account, remoteCallParticipant, sdpOffer);
                       currentCall.setIncommingCallRequest(request);
                       currentCall.setCallSessionID(sessionID);
                       fireIncommingCallEvent();
                   }

                    }


                }
            }

             public void processOutGoingCallEvent(OutGoingCallAcceptedEvent ev)
             {
                try {

                    String sdpResponse = ev.getSdpResponse();
                    currentCall.setEstablished(true);
                    currentCall.setCallSessionID(ev.getDialogId());
                    currentCall.setSdpResponse(sdpResponse);
                    CallParticipantInterface c = currentCall
                                                       .getDistantParticipant();
                    String addr = sdpProcessor
                            .makeSessionDescription(sdpResponse)
                                                .getConnection().getAddress();
                    InetAddress IPAddress =NetworkUtils
                                                    .StringToInet4Address(addr);
                    c.setIPAddress(IPAddress);
                    RTPSessionConfiguration config =sdpProcessor
                            .processSdpResponse(ev.getSdpResponse()
                                               ,currentCall.getSdpOffer()
                                               );
                    mediaController.startSendingMedia(config);
                    fireCallEstablished();

                } catch (SdpParseException ex) {
                    Logger.getLogger(SoftPhone.class.getName())
                            .log(Level.SEVERE, null, ex);
                }

             }
            /*L'appel est terminé par le participant distant
             */
            public void processTerminatedCallEvent(TerminatedCallEvent event)
            {
                fireTerminateCallEvent();
                currentCall=null;
                mediaController.StopStreaming();
                online=false;


            }
            /*L'appel est annulé par le correspondant distant*/
          public void processCancelledCallEvent()
          {
             fireTerminateCallEvent();
            currentCall=null;
            online=false;
          }


            /*Traite les réponse provisoires autres*/

            public void processProvisionnal()
            {
                     for(SoftPhoneListener l:softPhoneListeners)
                {
                    l.processCallInProgress();
                }
            }
            /*Traite une réponse provisoire SIP 100 Trying*/
            public void processTryingJoin()
            {
                TryCallEvent ev = null;
                     for(SoftPhoneListener l:softPhoneListeners)
                {
                         System.out.println("Trying");
                    l.processCallTryResponse(ev);

                }
            }
            /*Traite une réponse provisoire SIP 180 Ringing*/
            public void processRinging()
            {
                RingCallEvent ev = null;
                  for(SoftPhoneListener l:softPhoneListeners)
                {
                    l.processCallRingResponse(ev);

                }

            }
            /*Traite une réponse provisoire 183 Session In Progress*/
            public void processProgressing()
            {
                for(SoftPhoneListener l:softPhoneListeners)
                {
                    l.processCallInProgress();
                }

            }
            /* Traite une réponse 200 OK reçue pour un appel sortant*/
            public void processCallEstablishedEvent(CallEstablishedEvent event)
            {
                currentCall.setCallSessionID(event.getDialogId());
                currentCall.setEstablished(true);
                fireCallEstablished();
            }
            /* Traite l'événement de début de session ,garde
             une référence de la session*/
            public void processDialogEstablishedEvent(DialogEstablishedEvent event)
            {
                currentCall.setCallSessionID(event.getDialogID());

            }

            /* Traite une réponse 200 OK reçue pour un enregistrement*/
            public void processRegistredEvent(RegistredEvent event)
            {

                fireRegistred(event);

            }
            /*Traite les réponses 300 de redirection,met fin à l'appel*/
            public void processRedirect()
            {
                currentCall.setEstablished(true);
                TerminateCall();

            }
            /*Traite les réponses 401 et 407 pour les appels sortants et les
             enregistrement*/
            public void processWWWAuthRequired(wwwAuthRequired event)
            {

                Request requestToAuth=event.getRequest();
                String method=requestToAuth.getMethod();
                if(method.equalsIgnoreCase(Request.REGISTER))
                {
                    currentRegisterSequence.setRequestToAuth(requestToAuth);
                    Register(currentRegisterSequence
                            ,event.getCseqNum()
                            ,event.getResponse()
                            );
                }
                else if(method.equalsIgnoreCase(Request.INVITE))
                {
                    currentCall.setCurrentRequest(requestToAuth);
                    Call(currentCall,event.getCseqNum(),event.getResponse());
                }


            }

            public void processProxyAuthenticate(ProxyAuthRequired event)
            {
                Request requestToAuth=event.getRequest();
                String method =event.getRequest().getMethod();
                if(method.equalsIgnoreCase(Request.REGISTER))
                {
                    currentRegisterSequence.setRequestToAuth(requestToAuth);
                    Register(currentRegisterSequence
                            ,event.getCseqNum()
                            ,event.getResponse()
                            );
                }

                else if(method.equalsIgnoreCase(Request.INVITE))
                {
                    currentCall.setCurrentRequest(requestToAuth);
                    Call(currentCall,event.getCseqNum(),event.getResponse());
                }
            }
            /*Traite les autres réponses */
             public void processCallFails()
            {
                 System.out.println("Call fails");
                 for(SoftPhoneListener l:softPhoneListeners)
                {
                    l.processCallNok();

                }
                 currentCall=null;
                 online=false;
                 mediaController.StopStreaming();



            }

            private void fireCallEstablished()
            {
                OKCallEvent ev = null;
                for(SoftPhoneListener l:softPhoneListeners)
            {
                l.processCalloK(ev);

            }
            }
        };

        userAgentClient.addUserAgentClientListener(ual);
        userAgentServer.addUserAgentServerListener(ual);
            }

        /*Commence une session d'appel
         @param sipAddress adresse SIP du participant distant*/
        public void Call(String sipAddress )

        {
            online=true;
            CallParticipant remoteParticipant =new CallParticipant(sipAddress);
            String offer = sdpProcessor.makeSDPOffer(account.getAccountName());
            currentCall =new CallSession(account, remoteParticipant, offer);
            System.out.println("init receiving media");
            mediaController.initReceivingMedia(remoteParticipant);
            System.out.println("init capture media");
            mediaController.configureCaptureMedia();
            System.out.println("softphone ask uac make an invite");
            userAgentClient.Invite(currentCall);

        }

        /*Continue une session d'appel suite à
         une demande d'authentification
         @param callSession session d'appel a authentifié
         @param numéro de sequence de la requete a authentifié
         @param réponse du participant distant ou du proxy qui demande
         l'authentification */

         public void Call(CallSession callSession,long cseqNum,Response r )

        {
            userAgentClient.Invite( callSession, cseqNum, r);
        }

        /*Met fin à l'appel en cours
         * par une requete Cancel si l'Invite n'a pas reçu de réponse finale
         * par une requete Bye dans le cas contraire
         */
        public void TerminateCall()
        {


            if(currentCall.IsEstablished())
                {

                    mediaController.StopStreaming();
                    Request request =currentCall.getCurrentRequest();
                    userAgentClient.Bye(currentCall.getCallSessionID(),account, request);
                    online=false;
                   currentCall=null;
                }

            else
            {
                String sessionID =currentCall.getCallSessionID();
                Request request =currentCall.getCurrentRequest();
                userAgentClient.Cancel(sessionID,request);
                online=false;
            }
        }

        /*Accepte un appel entrant*/
        public void AcceptIncommingCall()
        {
            currentCall.setEstablished(true);
            bellController.stopRinging();
            CallParticipant remoteParticipant =(CallParticipant) currentCall.getDistantParticipant();
            currentCall.setLocalParticipant(this.account);
            RTPSessionConfiguration config =currentCall.getRtpConf();
            mediaController.initReceivingMedia(remoteParticipant);
            mediaController.configureCaptureMedia();
            mediaController.startSendingMedia(config);
            userAgentServer.sendInviteOK(currentCall);

        }
        /*Rejete un appel entrant*/
        public void RejectIncommingCall(String reason)
        {
            RejectIncommingCall(currentCall.getIncommingCallRequest(), reason);
        }
        private void RejectIncommingCall(Request r,String reason)
        {
            online=false;
            userAgentServer.rejectCall(null,reason);
            currentCall=null;
        }

        /*Procede a l'enregistrement sans authentification*/
        public void Register()
        {
           RegisterSequence registerSession =new RegisterSequence(account) ;
           currentRegisterSequence =registerSession;
            userAgentClient.Register(registerSession);
        }
         /*Procede a l'enregistrement avec authentification
          @param registerSequence session ou plutôt*/
        public void Register(RegisterSequence registerSequence,long cseqNum,Response r)
        {
            userAgentClient.Register(registerSequence,cseqNum,r);

        }

        public void addListener(SoftPhoneListener listener )
        {
            softPhoneListeners.add(listener);

        }
        /*Programme le prochaine enregistrement
         Averti l'utilisateur que le SoftPhone est enregistré auprès du serveur
         de localisation
         deplacer la logique de trigger dans userAgentRegister et ne laisser ici
         que le déclenchement d'un évènement vers l'ui*/
        private void fireRegistred(RegistredEvent event)
        {
            long expire =event.getExpires()*3/4;

            ScheduledExecutorService scheduler =
                                  Executors.newSingleThreadScheduledExecutor();

            scheduler.schedule(new RegisterTimer(this),expire,SECONDS);
            for(SoftPhoneListener l:softPhoneListeners)
            {
                l.processRegisterOK(event.getAddress());
            }

        }
        /*Traite un appel */
        private void fireIncommingCallEvent()
        {
            Request request =currentCall.getIncommingCallRequest();
            InCallEvent event = new InCallEvent(this,request);
            for(SoftPhoneListener l:softPhoneListeners)
            {
                l.processIncommingCall(event);
            }

        }
        private void fireTerminateCallEvent()
            {
                 for(SoftPhoneListener l:softPhoneListeners)
            {
                 System.out.println("Appel Entrant fsoftphone");
                l.processTerminateCallByRemotePart();

            }

            }
       /*Affiche le panneau de configuration*/
        public void showConf()
        {
            this.configurationProxy.showConfWINDOW();
        }

    }
