/*
 * 
 */
package SoftPhone.Protocol.Sip.Message;

import SoftPhone.Protocol.Sip.Provider.RegisterSequence;
import SoftPhone.Protocol.Sip.Agent.SipRegistrar;
import SoftPhone.Protocol.Sip.Account.SipAccount;
import SoftPhone.Configuration.Network.NetworkConfiguration;
import SoftPhone.Configuration.Sip.SipConfiguration;
import SoftPhone.Protocol.Sip.Account.SipCredentials;
import SoftPhone.Protocol.Sip.Call.CallParticipantInterface;
import SoftPhone.Protocol.Sip.Call.CallSession;
import java.text.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.*;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.address.URI;
import javax.sip.header.*;
import javax.sip.header.CSeqHeader;
import javax.sip.header.ProxyAuthorizationHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.*;

/**
 *
 * @author didier
 */

/*TODO instantiate this Class with a static method getInstance(SipStack)
and get Message with non static method*/
public class SipMessageBank {
    
    private  MessageFactory messageFactory;
    private  SipHeaderBank headerBank;
    private  NetworkConfiguration networkConfiguration;
    private  SipConfiguration sipConfiguration;

    public SipMessageBank()
    {
    }

    public Request authenticateRequest(Request request , long cseqnum, Response response ,SipCredentials credentials, String offer)
    {
      
        String uri =request.getRequestURI().toString();
        String requestMethod = request.getMethod();
        ViaHeader v =(ViaHeader)request.getHeader(ViaHeader.NAME);
                  v.removeParameter("branch");
                  CSeqHeader cseq =(CSeqHeader) request.getHeader("CSeq");
            try {
                    cseq.setSeqNumber(cseq.getSeqNumber() + 1);
                }
            catch (InvalidArgumentException ex)
            {
                Logger.getLogger(SipMessageBank.class.getName())
                        .log(Level.SEVERE, null, ex);
            }


        if(response!=null)
        {
            if(response.getStatusCode()==401)
            {
                 AuthorizationHeader auth =headerBank.getAuthorizationHeader(response
                                                              ,credentials
                                                              ,requestMethod
                                                              ,uri
                                                              ,null
                                                              );
                 request.addHeader(auth);

            }
            else if(response.getStatusCode()==407)
            {
               ProxyAuthorizationHeader auth=headerBank
                    .getProxyAuthorizationHeader(
                                                response
                                                , credentials
                                                , requestMethod
                                                , uri
                                                , ""
                                                );
               request.addHeader(auth);
            }


        }
        return request;
    }

    public Request getRegisterRequest(
                                              RegisterSequence registerSession
                                             ,long cseqnum
                                             ,Response response
                                             )

    {
       Request r= registerSession.getRequestToAuth();
       SipAccount account =registerSession.getAccount();
       SipCredentials credentials =account.getCredentials();
       String sipAddress =account.getSipAddress();
       String userName = account.getAccountUsername();
       String displayName = account.getDisplayName();
       SipRegistrar registrar =account.getSipRegistrar();
        {
             String uri =registrar.getSipAddress();
             ArrayList<ViaHeader> via=headerBank.gettViaHeader(
                             networkConfiguration.getLocalIp()
                            , Integer.parseInt(sipConfiguration.getSipListenningPort())
                            , "udp", null);
             if(r==null)
             {
            try{
                 cseqnum=1;
                
r =messageFactory.createRequest(headerBank.getrequestURI(registrar, "udp")
                        , Request.REGISTER
                        , headerBank.getCallIdHeader(null)
                        , headerBank.getCSeqHeader(Request.REGISTER, cseqnum)
                        , headerBank.getFromHeader(sipAddress)
                        , headerBank.getToHeader(sipAddress,displayName)
                        , via
                        , headerBank.getMaxForwards());

                        System.out.println(r.toString());

r.addHeader(headerBank
        .getContact(userName,networkConfiguration.getLocalIp(),3600));
                }

            catch (ParseException ex)
                {
                    Logger.getLogger(SipMessageBank.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
             }
             else
             {
                  ViaHeader v =(ViaHeader)r.getHeader(ViaHeader.NAME);
                  v.removeParameter("branch");
                  CSeqHeader cseq =(CSeqHeader) r.getHeader("CSeq");
            try {
                    cseq.setSeqNumber(cseq.getSeqNumber() + 1);
                } 
            catch (InvalidArgumentException ex) 
            {
                Logger.getLogger(SipMessageBank.class.getName())
                        .log(Level.SEVERE, null, ex);
            }

            }
        if(response!=null)
        {
            if(response.getStatusCode()==401)
            {
                 AuthorizationHeader auth =headerBank.getAuthorizationHeader(response
                                                              ,credentials
                                                              ,Request.REGISTER
                                                              ,uri
                                                              ,null
                                                              );
                 r.addHeader(auth);

            }
            else if(response.getStatusCode()==407)
            {
               ProxyAuthorizationHeader auth=headerBank
                    .getProxyAuthorizationHeader(
                                                response
                                                , credentials
                                                , Request.REGISTER
                                                , uri
                                                , ""
                                                );
               r.addHeader(auth);
            }
 

        }

      }
      return r;
    }

    /*getInviteRequest
     @param callSession la session d'appel initié par l"INVITE"
     @param response une réponse  401 éventuelle à une première "INVITE"
     @param content la description de session proposé
     @return une demande "INVITE" avec une proposition de session ,
     et authentifié au besoin*/
    public Request getInviteRequest(
                                       CallSession callSession
                                      ,Response response
                                      ,String content
                                      )
    {
        CallParticipantInterface localParticipant =callSession.getLocalParticipant();
        
        CallParticipantInterface distantParticipant =callSession.getDistantParticipant();
        Request r =callSession.getCurrentRequest();

         
            long cseqnum;
            if(r==null)
            {
                
                
                ArrayList<ViaHeader> via=headerBank.gettViaHeader(
                            networkConfiguration.getLocalIp()
                            , Integer.parseInt(sipConfiguration.getSipListenningPort())
                            , "udp", null);
            try {
                cseqnum = 1;
                r = messageFactory.createRequest(
                        headerBank.getrequestURI(distantParticipant, "udp")
                        , Request.INVITE
                        , headerBank.getCallIdHeader(null)
                        , headerBank.getCSeqHeader(Request.INVITE, cseqnum)
                        , headerBank.getFromHeader(localParticipant)
                        , headerBank.getToHeader(distantParticipant)
                        , via
                        , headerBank.getMaxForwards()
                        );
                r.addHeader(headerBank.getContact(localParticipant
                                        ,networkConfiguration.getLocalIp()));
                r.addHeader(headerBank.getAllowHeader());
                r.setContent(content, headerBank.getSDPContentTypeHeader());
                } 
            catch (NullPointerException ex) 
            {
                Logger.getLogger(SipMessageBank.class.getName())
                        .log(Level.SEVERE, null, ex);
            } 
            catch (ParseException ex) 
            {
                Logger.getLogger(SipMessageBank.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
           }

            else
            {
                ViaHeader v =(ViaHeader)r.getHeader(ViaHeader.NAME);
                v.removeParameter("branch");
                CSeqHeader cseq =(CSeqHeader) r.getHeader("CSeq");

            try 
            {
                cseq.setSeqNumber(cseq.getSeqNumber() + 1);
            } 
            catch (InvalidArgumentException ex) 
            {
                Logger.getLogger(SipMessageBank.class.getName())
                        .log(Level.SEVERE, null, ex);
            }

                            String  uri=r.getRequestURI().toString();

        if(response!=null)
        { ProxyAuthorizationHeader auth=headerBank
                    .getProxyAuthorizationHeader(
                                                response
                                                ,((SipAccount) localParticipant).getCredentials()
                                                , Request.INVITE
                                                , uri
                                                , content
                                                );
        try
            {
                 r.addLast(auth);
            }

        catch (SipException ex)
            {
                Logger.getLogger(SipMessageBank.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        catch (NullPointerException ex)
            {
                Logger.getLogger(SipMessageBank.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
            }
         
    }
            return r;
    }

    /*getCancelRequest
     @param r la requête a annuler doit être de type "INVITE"
     @return retourne une requête "Cancel pour la demande à annuler*/

     public Request getCancelRequest(Request r)
    {
        Request request = null;
      try
        {
            Vector<ViaHeader> viaHeaders = new Vector<ViaHeader>();
            URI uri = r.getRequestURI();
            FromHeader from = (FromHeader) r.getHeader(FromHeader.NAME);
            ToHeader to = (ToHeader) r.getHeader(ToHeader.NAME);
            CallIdHeader callId = (CallIdHeader) r.getHeader(CallIdHeader.NAME);
            long csnum =((CSeqHeader) r.getHeader("CSeq")).getSeqNumber();
            ViaHeader via = (ViaHeader) r.getHeader(ViaHeader.NAME);
            viaHeaders.add(via);
            RouteHeader route = (RouteHeader) r.getHeader(RouteHeader.NAME);
            messageFactory.createRequest(
                                    uri
                                    ,"CANCEL"
                                    ,callId
                                    ,headerBank.getCSeqHeader("CANCEL",csnum)
                                    ,from
                                    ,to
                                    ,viaHeaders
                                    ,headerBank.getMaxForwards()
                                        );
            if(route!=null)
            {
                try
                    {
                      request.addFirst(route);
                    }
                catch (SipException ex)
                    {
                        Logger.getLogger(SipMessageBank.class.getName())
                            .log(Level.SEVERE, null, ex);
                    }
                catch (NullPointerException ex)
                {
                    Logger.getLogger(SipMessageBank.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
        }
      catch (ParseException ex)
        {
            Logger.getLogger(SipMessageBank.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
      return request;

    }


    /*@param dialog le dialog a aquitter
     *@param cseqNum la valeur du CSeqHeader de la requete a quitter
     */
    public  Request getAckRequest(Dialog dialog,long cseqNum) 
    {
        Request r=null;
        try 
            {
                r = dialog.createAck(cseqNum+1);
            }

        
        catch (InvalidArgumentException ex) 
            {
                Logger.getLogger(SipMessageBank.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        catch (SipException ex) 
            {
                Logger.getLogger(SipMessageBank.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        
        return r;

    }


    
    /*getByeRequest
     @param dialog le dialogue (la session ) à terminé
     @param localCallParticipant le particitant local de cette session
     @return retourne une dempande "BYE" pour cette session et ce participant*/
    public  Request getByeRequest(Dialog dialog
                                 ,CallParticipantInterface localCallParticipant
                                 , Request request)
    {
        Request r=null;
        String branch =((ViaHeader)request.getHeader(ViaHeader.NAME)).getBranch();
        try {
             FromHeader from = headerBank.getFromHeader(
                                                       dialog.getLocalParty()
                                                        ,dialog.getLocalTag()
                                                        );
            ToHeader to = headerBank.getToHeader(
                                                 dialog.getRemoteParty()
                                                ,dialog.getRemoteTag()
                                                 );
            CSeqHeader csh = headerBank.getCSeqHeader(
                                                     Request.BYE
                                                    ,dialog.getLocalSeqNumber()
                                                    );
            ArrayList<ViaHeader>via =headerBank.gettViaHeader(
                                           networkConfiguration.getLocalIp()
                                           ,Integer.parseInt(sipConfiguration.getSipListenningPort())
                                           , "udp"
                                           ,branch
                                           );
            r = messageFactory.createRequest(
                                               dialog.getRemoteParty().getURI()
                                               , "BYE"
                                               , dialog.getCallId()
                                               , csh
                                               , from
                                               , to
                                               , via
                                               , headerBank.getMaxForwards()
                                               );
            }
        catch (ParseException ex)
        {
            Logger.getLogger(SipMessageBank.class.getName())
                                                .log(Level.SEVERE, null, ex);
        }
        return r;
    }
    
/*getOKINVITEResponse
 Si il y avait une proposition média dans l'invite on envoie une réponse 200OK
 avec une réponse sdp sinon la réponse contient une offre média
 @param request requete a laquelle on répond
 @param session appel concerné contenant les paramètres de l'appel*/
    public  Response getOKINVITEResponse(Request request,CallSession session)
    {
        Response response = null;
        String method =request.getMethod();
        CallParticipantInterface cp =session.getLocalParticipant();
        try {
            response = messageFactory.createResponse(200, request);
            response.addHeader(headerBank.getContact(
                                            cp
                                           ,networkConfiguration.getLocalIp()));
            response.addHeader(headerBank.getAllowHeader());
            String sdpResponse=session.getSdpResponse();
            String sdpOffer =session.getSdpOffer();

            if(sdpResponse!=null)
            {
               response.setContent(sdpResponse
                                  ,headerBank.getSDPContentTypeHeader()
                                  );
            }
            else
            {
                response.setContent(sdpOffer
                                  ,headerBank.getSDPContentTypeHeader()
                                  );
            }
            }
        catch (ParseException ex)
        {
            Logger.getLogger(SipMessageBank.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return response;
    }

    public  Response getOKCancelResponse(Request r,long cseqnum)
    {
        Response response=null;
        try {
           response= messageFactory.createResponse(200, r);
            }
        catch (ParseException ex) {
            Logger.getLogger(SipMessageBank.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return response;
    }
     public Response get415Response(Request request)
    {
        Response r=null;
        try
            {
                //Fabriquer une réponse et y ajouter un champ d'entête Accept
                //format supporté "application/sdp"
              r= messageFactory.createResponse(415, request);
              r.addLast(headerBank.getAcceptHeader()) ;
            }
        catch (SipException ex)
            {
                Logger.getLogger(SipMessageBank.class.getName())
                                                .log(Level.SEVERE, null, ex);
            }
        catch (NullPointerException ex)
            {
                Logger.getLogger(SipMessageBank.class.getName())
                                                .log(Level.SEVERE, null, ex);
            }
        catch (ParseException ex)
            {
                Logger.getLogger(SipMessageBank.class.getName())
                                                   .log(Level.SEVERE, null, ex);
            }
        return r;

    }

     public Response get420Response(Request request,RequireHeader rh)
    {
         Response r = null;
         String optionTag=rh.getOptionTag();
        try {

            r = messageFactory.createResponse(420, request);
            r.addLast(headerBank.getUnsupportedHeader(optionTag));
        } catch (SipException ex) {
            Logger.getLogger(SipMessageBank.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(SipMessageBank.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(SipMessageBank.class.getName()).log(Level.SEVERE, null, ex);
        }
         return r;
    }


    public Response get486Response(Request request)
    {
        Response response = null;
        try {
                response = messageFactory.createResponse(486, request);
            }
        catch (ParseException ex)
            {
                Logger.getLogger(SipMessageBank.class.getName())
                                                .log(Level.SEVERE, null, ex);
            }
         return response;
    }

    public Response getResponse(int statusCode, Request request)
    {
        Response r =null;
        try {
             r = messageFactory.createResponse(statusCode, request);

        } catch (ParseException ex) {
            Logger.getLogger(SipMessageBank.class.getName())
                                                .log(Level.SEVERE, null, ex);
        }

         return r;
    }


    public  void setHeaderBank(SipHeaderBank headerBankparam) {
        headerBank = headerBankparam;
    }

    public  void setMessageFactory(MessageFactory messageFactoryparam) {
        messageFactory = messageFactoryparam;
    }

    public void setNetworkConfiguration(NetworkConfiguration
            networkConfigurationparam) {
        networkConfiguration = networkConfigurationparam;
    }

    public void setSipConfiguration(SipConfiguration sipConfiguration) {
        this.sipConfiguration = sipConfiguration;
    }

    
}
