/*
 * Rendre le sdp processor multitache
 *  */

package SoftPhone.Media;

import SoftPhone.Configuration.Media.MediaConfiguration;
import SoftPhone.Configuration.Network.NetworkConfiguration;
import SoftPhone.Network.*;
import gov.nist.javax.sdp.fields.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;
import javax.media.rtp.*;
import javax.sdp.*;



    /**
 *
 * @author didier
 */
public class SdpProcessor
{
    private SdpFactory sdpFactory;
    private MediaConfiguration mediaConfiguration;
    private NetworkConfiguration networkConfiguration;


    public SdpProcessor(
                           MediaConfiguration mediaConfiguration
                         , NetworkConfiguration networkConfiguration
                         ) 
    {
         sdpFactory =SdpFactory.getInstance();
        this.mediaConfiguration = mediaConfiguration;
        this.networkConfiguration = networkConfiguration;
    }

    public SdpProcessor()
    {

    }


    public SessionDescription makeSessionDescription(String sdpmessage)
    {
         SessionDescription sd=null;
        try {
                sd = sdpFactory.createSessionDescription(sdpmessage);

            }

        catch (SdpParseException ex)
        {
            Logger.getLogger(MediaController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        return sd;
    }


    public String makeSDPOffer(String accountName)
    {
        SessionDescription sessionDescription = null;
        try {
            int rtpPort=NetworkUtils.getFreeRtpReceivingPort();
            sessionDescription = sdpFactory.createSessionDescription();
            Version v = sdpFactory.createVersion(0);
            sessionDescription.setVersion(v);
            String addressType = Connection.IP4;
            long sessionId = 0;
            long sessionVersion = 0;
            String publicAddress = networkConfiguration.getLocalIp();
            Origin o = sdpFactory.createOrigin(
                                                accountName
                                               , sessionId
                                               , sessionVersion
                                               , "IN"
                                               , addressType
                                               , publicAddress
                                               );
            sessionDescription.setOrigin(o);
            Connection c = sdpFactory.createConnection("IN"
                                                       , addressType
                                                       , publicAddress
                                                       );
            sessionDescription.setConnection(c);
            SessionName sn = sdpFactory.createSessionName("session");
            sessionDescription.setSessionName(sn);
            TimeDescription td = sdpFactory.createTimeDescription();
            Vector <TimeDescription>tds = new Vector<TimeDescription>();
            tds.add(td);
            sessionDescription.setTimeDescriptions(tds);
            MediaDescription md = sdpFactory
            .createMediaDescription(
                                    "audio"
                                    , rtpPort
                                    , 1
                                    , "RTP/AVP"
                                    , mediaConfiguration.getPrefferedCodecs()
                                    );

            Vector< MediaDescription> mds = new Vector< MediaDescription>();
            mds.add(md);
            sessionDescription.setMediaDescriptions(mds);
            Attribute attribute = sdpFactory.createAttribute(
                                                             addressType
                                                             , addressType
                                                            );
            md.setAttribute("rtpmap", "3");
            AttributeField f = new AttributeField();
            f.setName("rtpmap");
            f.setValue("O");
            md.addAttribute(f);
            md.setAttribute("sendrecv", null);
            System.out.println(sessionDescription.toString());
        } 
        catch (SdpException ex)
        {
            Logger.getLogger(SdpProcessor.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return sessionDescription.toString();
    }


/*processSDPResponse
     Si la réponse SDP contient un nombre de description média différent
     de l'offre retourne "null"
     retourne une configuration de session
     si le participant distant à accepté un flux correspondant à l'offre
     initiale ou retourne null sinon

     Si le flux est audio :
     il extrait le format ,fait la correspondance avec le format RTP et
     place la correspondance dans la configuration de session
     il créer avec les éléments de connection de la réponse sdp
     l' adresse de session RTP pour le participant distant et la place dans
     la configuration de session
     il créer une adresse de session RTP pour le participant local et la
     place dans la configuration de session
     retourne la configuration de session

     */
    public RTPSessionConfiguration processSdpResponse(
                                                        String response
                                                        ,String offer
                                                        )
    {
         RTPSessionConfiguration configuration= null;
        String remoteAddress=" ";
        String localAddress=" ";
        int localPort=NetworkUtils.getFreeRtpSendingPort();
        int remotePort=0;
        boolean acceptedMedia;
        int acceptedFormat=-1;
        try
            {
            System.out.println("construction de la configuration de la session rtp");
            SessionDescription osd= makeSessionDescription(offer);
            SessionDescription rsd = makeSessionDescription(response);
             System.out.println("on a récupérer des descriptions de sessions");
             Vector<MediaDescription> rmd = rsd.getMediaDescriptions(true);
             Vector<MediaDescription> omd= osd.getMediaDescriptions(true);
        /*il y a le même nombre de description média  dans la  réponse
         que dans l'offre et comme l'offre ne comportait qu'une description*/
            System.out.println("Taille de l'offre: "+omd.size());
            System.out.println("Taille de la réonse: "+rmd.size());
             if((omd.size()==rmd.size())&&(rmd.size()==1))

             {
                 Connection con =rsd.getConnection();
                 if(con!=null)remoteAddress = con.getAddress();
                
                System.out.println("adresse de connection distante dans la description de session"+remoteAddress);
                Iterator iter =rmd.iterator();
                MediaDescription m =(MediaDescription)iter.next();
                 System.out.println("on compare l'offre et la demande");
                Connection mediaConnection=m.getConnection();
                
                if(mediaConnection !=null)
                  {
                    remoteAddress =mediaConnection.getAddress();
                    System.out.println("adresse de connection distante dans la description de session"+remoteAddress);
                  }
                 Media media =m.getMedia();
                 System.out.println("le type de media est :"+media);
                 if(media.getMediaType().equalsIgnoreCase("audio"))
                   {
                    remotePort=media.getMediaPort();
                     System.out.println("le port rtp distant est :"+Integer.toString(remotePort));

                    if(remotePort==0)
                      {
                        acceptedMedia=false;
                      }
                    else
                      {
                        acceptedMedia=true;
                        Vector<String> formats=media.getMediaFormats(false);
                        acceptedFormat =AcceptFormat(formats);
                      }
                    }
            }
        }
            catch (SdpException ex)
            {
                Logger.getLogger(MediaController.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        InetAddress localInetAddress=networkConfiguration.getLocaladdress();
        InetAddress remoteInetAddress=NetworkUtils.StringToInet4Address(
                remoteAddress);

        SessionAddress localSessionAddress =new SessionAddress(
                                                              localInetAddress
                                                            , localPort
                                                            , localInetAddress
                                                            , localPort+1
                                                            );



        SessionAddress remoteSessionAddress=new SessionAddress(
                                                           remoteInetAddress
                                                           , remotePort
                                                           , remoteInetAddress
                                                           , remotePort+1
                                                           );
        String audioFormat =MediaConfiguration.getJMFEncodingForPT(acceptedFormat);
        System.out.println("Format audio négocié: "+audioFormat);

        configuration =new RTPSessionConfiguration(
                                                    localSessionAddress
                                                    , remoteSessionAddress
                                                    , audioFormat);

         return configuration;
    }

    public String makeSdpResponse(String accountDisplayName
                                 ,String offer)
    {
        SessionDescription sessionDescription = null;
        int rtpPort =NetworkUtils.getFreeRtpReceivingPort();
        try {
            System.out.println(offer.length());
            int acceptedFormat=-1;
            Vector<MediaDescription> rmd =new Vector<MediaDescription>();
            SessionDescription osd =sdpFactory.createSessionDescription(offer);

            sessionDescription = sdpFactory.createSessionDescription();
            Version v = sdpFactory.createVersion(0);
            sessionDescription.setVersion(v);
            String addressType = Connection.IP4;
            long sessionId = 0;
            long sessionVersion = 0;
            String publicAddress = networkConfiguration.getLocalIp();
            Origin o = sdpFactory.createOrigin(
                                            accountDisplayName
                                           , sessionId
                                           , sessionVersion
                                           , "IN"
                                           , addressType
                                           , publicAddress
                                           );
            sessionDescription.setOrigin(o);
            Connection c = sdpFactory.createConnection("IN"
                                                       , addressType
                                                       , publicAddress
                                                       );
            sessionDescription.setConnection(c);
            SessionName sn = sdpFactory.createSessionName("session");
            sessionDescription.setSessionName(sn);
            TimeDescription td = sdpFactory.createTimeDescription();
            Vector<TimeDescription> tds = new Vector<TimeDescription>();
            tds.add(td);
            sessionDescription.setTimeDescriptions(tds);
           
            Vector<MediaDescription> omd = osd.getMediaDescriptions(true);
            /*Pour tous les flux media
             si le flux est de type audio et pas de flux accepté
             procédé les support de se flux et et garder celui qui
             à la meilleur préférence,proposer le flux pour le support
             sélectionné et inclure la proposition avec  un port local libre
             sinon si le flux n'est pas audio recopié l'offre de flux
             en mettant sont numéro de port à 0*/
            ListIterator<MediaDescription> iter =omd.listIterator();
            while(iter.hasNext())
            {
                boolean notAccepted=true;
                MediaDescription md =iter.next();
                Media m=md.getMedia();
                String type =m.getMediaType();
                if(type.equalsIgnoreCase("audio")&&notAccepted)
                {
                    Vector<String> formats=m.getMediaFormats(false);
                        acceptedFormat =AcceptFormat(formats);
                        int[] acc={acceptedFormat};
                        MediaDescription mda = sdpFactory
            .createMediaDescription(
                                    "audio"
                                    , 8000
                                    , 1
                                    , "RTP/AVP"
                                    , acc
                                    );
                        mda.setAttributes(md.getAttributes(false));
                        rmd.add(mda);
                        
                }

                else
                {
                   m.setMediaPort(0);
                   rmd.add(md);

                }
            }
             sessionDescription.setMediaDescriptions(rmd);
        }
        catch (SdpException ex)
        {
            Logger.getLogger(SdpProcessor.class.getName())
                    .log(Level.SEVERE, null, ex);
        }


        return sessionDescription.toString();
    }

    public RTPSessionConfiguration getIncommingCallRTPConf(String offer,String response)
    {
        RTPSessionConfiguration config = null;
        String remoteAddress = null;
        String localAddress = " ";
            int localPort = NetworkUtils.getFreeRtpSendingPort();
            int remotePort = 0;
            boolean acceptedMedia;
            int acceptedFormat = -1;
        try {



            SessionDescription osd = makeSessionDescription(offer);
            SessionDescription rsd = makeSessionDescription(response);
            System.out.println("on a récupérer des descriptions de sessions");
            Vector<MediaDescription> rmd = rsd.getMediaDescriptions(true);
            Vector<MediaDescription> omd = osd.getMediaDescriptions(true);
            /*il y a le même nombre de description média  dans la  réponse
            que dans l'offre et comme l'offre ne comportait qu'une description*/
            System.out.println("Taille de l'offre: " + omd.size());
            System.out.println("Taille de la réonse: " + rmd.size());
            if ((omd.size() == rmd.size())) {
                Connection con = osd.getConnection();
                if (con != null) {
                    remoteAddress = con.getAddress();
                }
                System.out.println("adresse de connection distante dans la description de session" + remoteAddress);
                Iterator iter = omd.iterator();
                MediaDescription m = (MediaDescription) iter.next();
                System.out.println("on compare l'offre et la demande");
                Connection mediaConnection = m.getConnection();
                if (mediaConnection != null) {
                    remoteAddress = mediaConnection.getAddress();
                    System.out.println("adresse de connection distante dans la description de session" + remoteAddress);
                }
                Media media = m.getMedia();
                System.out.println("le type de media est :" + media);
                if (media.getMediaType().equalsIgnoreCase("audio")) {
                    remotePort = media.getMediaPort();
                    System.out.println("le port rtp distant est :" + Integer.toString(remotePort));
                    if (remotePort == 0) {
                        acceptedMedia = false;
                    } else {
                        acceptedMedia = true;
                        Vector<String> formats = media.getMediaFormats(false);
                        acceptedFormat = AcceptFormat(formats);
                    }
                }
            }
        } catch (SdpException ex) {
            Logger.getLogger(SdpProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }


        InetAddress localInetAddress=networkConfiguration.getLocaladdress();
        InetAddress remoteInetAddress=NetworkUtils.StringToInet4Address(
                remoteAddress);


        SessionAddress localSessionAddress =new SessionAddress(
                                                              localInetAddress
                                                            , localPort
                                                            , localInetAddress
                                                            , localPort+1
                                                            );



        SessionAddress remoteSessionAddress=new SessionAddress(
                                                           remoteInetAddress
                                                           , remotePort
                                                           , remoteInetAddress
                                                           , remotePort+1
                                                           );
        String audioFormat =MediaConfiguration.getJMFEncodingForPT(acceptedFormat);
        System.out.println("Format audio configuré: "+audioFormat);

        config =new RTPSessionConfiguration(
                                                    localSessionAddress
                                                    , remoteSessionAddress
                                                    , audioFormat);


        return config;

    }

    

    /*Compare les formats de l'offre et de la réponse
     et retourne le numéro de charge utile RTP du format préféré entre l'offre
     et la réponse , parmis les formats acceptés
     si aucun n'est accepté retourne "-1"*/

     private int AcceptFormat(Vector<String> fts)
    {
         boolean notaccepted =true;
         int format =-1;
         int[] prefered =MediaConfiguration.getPrefferedCodecs();
      for(String f:fts)
      {
         int i=Integer.parseInt(f);
           int j=0;
          while(j<=prefered.length&&notaccepted)
           {
               
               System.out.println(prefered[j]);
               if(i==prefered[j])
               {
                  format=i;
                  notaccepted=false;
               }
                j=j+1;
            }
      }
        return format;

    }

    


}
