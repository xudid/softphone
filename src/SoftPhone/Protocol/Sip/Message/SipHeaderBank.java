


package SoftPhone.Protocol.Sip.Message;


import SoftPhone.Protocol.Sip.Call.CallParticipantInterface;
import SoftPhone.Protocol.Sip.Provider.DigestCalculate;
import SoftPhone.Protocol.Sip.Agent.SipRegistrar;
import SoftPhone.Protocol.Sip.Account.SipCredentials;
import gov.nist.javax.sip.address.SipUri;
import java.text.ParseException;
import java.util.*;
import java.util.logging.*;
import javax.sip.*;
import javax.sip.address.*;
import javax.sip.header.*;
import javax.sip.message.*;


/**
 *
 * @author didier
 */
public class SipHeaderBank
{
    private  AddressFactory addressFactory;
    private  HeaderFactory headerFactory;
    private  MessageFactory messageFactory;
    private SipProvider sipProvider;
   

    public SipHeaderBank() 
    {

    }

    public void setSipProvider(SipProvider sipProvider)
    {
        this.sipProvider = sipProvider;
    }

    public HeaderFactory getHeaderFactory()
    {
        return headerFactory;
    }

    public  void setHeaderFactory(HeaderFactory headerFactoryParam)
    {
        headerFactory = headerFactoryParam;
    }

    public  AddressFactory getAddressFactory()
    {
        return addressFactory;
    }

    public void setAddressFactory(AddressFactory addressFactoryParam)
    {
       addressFactory = addressFactoryParam;
    }

    public   MessageFactory getMessageFactory() {
        return messageFactory;
    }

    public  void  setMessageFactory(MessageFactory messageFactoryParam) {
        messageFactory=messageFactoryParam;
    }



 public   FromHeader getFromHeader(Address localParty, String localTag)
   {
       FromHeader from =null;
        try {
            from = headerFactory.createFromHeader(localParty, localTag);
            }
        catch (ParseException ex)
        {
            Logger.getLogger(SipHeaderBank.class.getName()).log(Level.SEVERE, null, ex);
        }
       return from;
   }

 public   FromHeader getFromHeader(CallParticipantInterface callParticipant) {
        FromHeader fh =null;
        Address address;
        try {
            address = addressFactory.createAddress(callParticipant.getSipAddress());
            Date d =new Date();
             System.out.println(d);
            String tag =  Integer.toString(d.hashCode());
            System.out.println(tag);
            fh=headerFactory.createFromHeader(address, tag);
            }
        catch (ParseException ex)
            {
            Logger.getLogger(SipHeaderBank.class.getName())
                    .log(Level.SEVERE, null, ex);
            }

        return fh;
    }

 public FromHeader getFromHeader(String sipAddress)
 {
      FromHeader fh =null;
        Address address;
        try {
            address = addressFactory.createAddress(sipAddress);
            Date d =new Date();
             System.out.println(d);
            String tag =  Integer.toString(d.hashCode());
            System.out.println(tag);
            fh=headerFactory.createFromHeader(address, tag);
            }
        catch (ParseException ex)
            {
            Logger.getLogger(SipHeaderBank.class.getName())
                    .log(Level.SEVERE, null, ex);
            }

        return fh;
    }


 public ToHeader getToHeader(CallParticipantInterface remoteParticipant)
    {
        ToHeader toHeader=null;
        try
            {
                Address toAddress;
                toAddress = addressFactory.createAddress(remoteParticipant.getSipAddress());
                toAddress.setDisplayName(remoteParticipant.getDisplayName());
                toHeader=headerFactory.createToHeader(toAddress, null);
            }
        catch (ParseException ex)
            {
            Logger.getLogger(SipHeaderBank.class.getName())
            .log(Level.SEVERE, null, ex);
            }
        return toHeader;
    }

 public ToHeader getToHeader(String sipAddress,String userName)
    {
        ToHeader toHeader=null;
        try
            {
                Address toAddress;
                toAddress = addressFactory.createAddress(sipAddress);
                toAddress.setDisplayName(userName);
                toHeader=headerFactory.createToHeader(toAddress, null);
            }
        catch (ParseException ex)
            {
            Logger.getLogger(SipHeaderBank.class.getName())
            .log(Level.SEVERE, null, ex);
            }
        return toHeader;
    }




 public  ToHeader getToHeader(Address remoteParty, String remoteTag)
    {
        ToHeader to =null;
        try
            {
            to = headerFactory.createToHeader(remoteParty, remoteTag);
            }
        catch (ParseException ex)
            {
            Logger.getLogger(SipHeaderBank.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
        return to;

    }



    public  AllowHeader getAllowHeader()
    {
        AllowHeader allowHeader = null;
        try {
			allowHeader
            =headerFactory.createAllowHeader("INVITE,ACK,BYE,OPTIONS,CANCEL");
            }
        catch (ParseException e)
            {
			// TODO allow header is create with hard parameters this will never
            //happen
			e.printStackTrace();
            }
        return allowHeader;
    }

   

    public  ContactHeader getContact(CallParticipantInterface cp,String IP)
    {
         ContactHeader ch =null;
        try {
            SipUri uri = (SipUri) addressFactory
                    .createSipURI(cp.getUserInfo(), IP);
            Address address = addressFactory.createAddress(uri);
            ch = headerFactory.createContactHeader(address);
           
            }
        catch (ParseException ex)
            {
            Logger.getLogger(SipHeaderBank.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
        return ch;
    }

   

     public  ContactHeader getContact(
                                                  String userName
                                                 ,String IP
                                                 ,int expire
                                                 )

    {
       ContactHeader ch =null;
        try {
            SipUri uri = (SipUri) addressFactory
                    .createSipURI(userName, IP);
            System.out.println(uri.toString());
            Address address = addressFactory.createAddress(uri);
            ch = headerFactory.createContactHeader(address);

            }
        catch (ParseException ex)
            {
            Logger.getLogger(SipHeaderBank.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
        return ch;
    }

   

    public  ExpiresHeader getExpire()
    {
        ExpiresHeader expire=null;
        try
            {
                expire =headerFactory.createExpiresHeader(3600);
            }
        catch (InvalidArgumentException e)
            {
				e.printStackTrace();
            }
        return  expire;
    }

    

    

    

    public  MaxForwardsHeader getMaxForwards()
    {
        MaxForwardsHeader maxForwards=null;
        try
            {
                maxForwards = null;
                maxForwards = headerFactory.createMaxForwardsHeader(70);
            }
        catch (InvalidArgumentException ex)
            {
                Logger.getLogger(SipHeaderBankFactory.class.getName())
                .log(Level.SEVERE, null, ex);
            }
        return maxForwards;
    }

   

    


   

    public CallIdHeader getCallIdHeader(CallIdHeader callid)
    {
		if (callid != null)
            {
                return callid;
            }
        else 
            {
                return sipProvider.getNewCallId();
            }
	}

   public   ArrayList<ViaHeader> gettViaHeader(
                                                             String IP
                                                           , int Port
                                                           , String transport
                                                           , String branch
                                                           )
   {
        ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
        ViaHeader viaHeader = null;
        try
        {
            /*TODO make a method to generate via branchid
            as describe in the RFC3261*/
            String uniqueBranch;
            if(branch==null)
            {
                Date d = new Date();
                 uniqueBranch = Integer.toString(d.hashCode());

            }

            else
            {
                uniqueBranch= branch;
            }
            
            viaHeader = headerFactory.createViaHeader(
                                                        IP
                                                        , Port
                                                        , transport
                                                        , "z9hG4bK"
                                                        + uniqueBranch
                                                        );
            viaHeaders.add(viaHeader);
          }
        catch (ParseException e)
            {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
        catch (InvalidArgumentException e)
            {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
        return viaHeaders;

	}
   
   

    

   public  CSeqHeader getCSeqHeader(String method,long cseqnum)
   {
        CSeqHeader cseqHeader =null;
		try
            {
                cseqHeader=headerFactory.createCSeqHeader(cseqnum,method);
            }
        catch (ParseException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        catch (InvalidArgumentException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return cseqHeader;
	}

   public   SipURI getrequestURI(SipRegistrar registrar
                                     ,String transport)
    {
		SipURI URI = null;
		try
            {

                URI =(SipURI) addressFactory.createURI(registrar.getSipAddress());
                URI.setTransportParam(transport);
            }
        catch (ParseException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        return URI;

	}

    public   SipURI getrequestURI(CallParticipantInterface distantParticipant
                              ,String transport)
    {
		SipURI URI = null;
		try
            {
               Address address =addressFactory.createAddress(distantParticipant.getSipAddress());
               URI =(SipURI) address.getURI();
                URI.setTransportParam(transport);
            }
        catch (ParseException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
		return URI;
	}

    

    public  ContentTypeHeader getSDPContentTypeHeader()
    {   ContentTypeHeader contentTypeHeader = null;
   try {
            contentTypeHeader = headerFactory.createContentTypeHeader("application", "sdp");
        } catch (ParseException ex) {
            Logger.getLogger(SipHeaderBankFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contentTypeHeader;
    }

  

    

    

     
    /*getAuthorizationHeader
     @param  response reponse provoquant l'authentification de la request
     @param  account  identité a authentifié
     @param method    méthode de la requête à identifié
     @param uri       uri de la demande à identifié
     @param content que pourrait porter le message
     @return AuthorizationHeader
     avec un digest basé sur un Digest calculé avec un
     hachage MD5*/

    public  AuthorizationHeader getAuthorizationHeader(
                                                          Response response
                                                          ,SipCredentials credentials
                                                          ,String method
                                                          ,String uri
                                                          ,String content)
    {
        WWWAuthenticateHeader authenticateHeader
         =(WWWAuthenticateHeader)response.getHeader(WWWAuthenticateHeader.NAME);
        String scheme =authenticateHeader.getScheme();
        AuthorizationHeader auth = null;

		try
            {
			auth =headerFactory.createAuthorizationHeader(scheme);
            if(auth !=null)
            {
            auth.setUsername(credentials.getUserName());
            String nonce =authenticateHeader.getNonce();
            String realm =authenticateHeader.getRealm();
            String algo =authenticateHeader.getAlgorithm();
            auth.setRealm(realm);
            auth.setNonce(nonce);
            auth.setParameter("uri", uri);
            auth.setAlgorithm(algo);
            auth.setResponse(
                    DigestCalculate.CalculateResponse(auth
                                                      ,credentials
                                                      ,method
                                                      ,content
                                                      )
                    );
			}
            }
        catch (ParseException e1)
            {
				e1.printStackTrace();
            }
        return auth;
    }

     /*getProxyAuthorizationHeader
     @param  response reponse provoquant l'authentification de la request
     @param  account  identité a authentifié
     @param method    méthode de la requête à identifié
     @param uri       uri de la demande à identifié
     @param "content" porté par la demande SIP
     @return AuthorizationHeader
     avec un digest basé sur un Digest calculé avec un
     hachage MD5*/

     public  ProxyAuthorizationHeader getProxyAuthorizationHeader(Response response,SipCredentials credentials,String method,String uri,String content)
     {
         ProxyAuthenticateHeader authenticateHeader =(ProxyAuthenticateHeader) response.getHeader(ProxyAuthenticateHeader.NAME);
        String scheme =authenticateHeader.getScheme();
        ProxyAuthorizationHeader auth = null;

		try
            {
			auth =headerFactory.createProxyAuthorizationHeader(scheme);
            if(auth !=null)
                {
                    String realm =authenticateHeader.getRealm();
                    String nonce =authenticateHeader.getNonce();
                    String algo =authenticateHeader.getAlgorithm();
                    auth.setRealm(realm);
                    auth.setUsername(credentials.getUserName());
                    auth.setNonce(nonce);
                    auth.setParameter("uri", uri);
                    auth.setAlgorithm(algo);
                    auth.setResponse(
                                      DigestCalculate.CalculateResponse(
                                                                        auth
                                                                       , credentials
                                                                       , method
                                                                       ,content
                                                                       )
                                     );
                }

			

            }
        catch (ParseException e1)
            {
			
			e1.printStackTrace();
            }
        return auth;
    }

   public  AcceptHeader getAcceptHeader()
    {
        AcceptHeader acceptHeader = null;
        try
            {
                acceptHeader =headerFactory
                                    .createAcceptHeader("application", "sdp");
            }
        catch (ParseException ex)
            {
                Logger.getLogger(SipHeaderBank.class.getName())
                                                .log(Level.SEVERE, null, ex);
            }
        return  acceptHeader;
    }

   public  UnsupportedHeader getUnsupportedHeader(String optionTag)
   {
       UnsupportedHeader uh = null;
        try {


            uh =headerFactory.createUnsupportedHeader(optionTag);
        } catch (ParseException ex) {
            Logger.getLogger(SipHeaderBank.class.getName()).log(Level.SEVERE, null, ex);
        }
        return uh;
   }

   public RouteHeader getRouteHeader(String uri) 
   {
       RouteHeader routeHeader = null;
        try {
            
            Address address = addressFactory.createAddress(uri);
            routeHeader = headerFactory.createRouteHeader(address);
           
           
        } catch (ParseException ex) {
            Logger.getLogger(SipHeaderBank.class.getName()).log(Level.SEVERE, null, ex);
        }
        return routeHeader;
   }


}
