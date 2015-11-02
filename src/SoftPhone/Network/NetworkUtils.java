/*
 *TODO introduce a method for RTP FREE PORT which returns a port number
 * within user setup RTPPORTS boundaries
 */

package SoftPhone.Network;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author didier
 */
public  class  NetworkUtils
{   private static final int firstAvailablePort =1024;
    private static  final int lastAvailableport=65535;
    private static final int firstRTPPortRange =8000;
    private static final int lastRTPPortRange=8100;


    public static InetAddress StringToInet4Address(String IPV4)
    {

        InetAddress IPAddress=null;
        byte[] b=new byte[0];
        b=tokenizeIPString(IPV4);
        if (b.length==4&& isIPV4(IPV4))
        {
            try
                {
                    IPAddress = InetAddress.getByAddress(b);
                }
            catch (UnknownHostException ex)
                {
                    Logger.getLogger(NetworkUtils.class.getName())
                    .log(Level.SEVERE, null, ex);
                }
        }
        return IPAddress;
    }

    public static boolean isIPV4(String string)
        {

            return true;
        }

    public static boolean isLoopback(String string)
        {
        boolean yes =false;

            if (NetworkUtils.isIPV4(string))

            {
                InetAddress address =StringToInet4Address(string);
                yes=address.isLoopbackAddress();

            }
        return yes;
        }

    public static boolean ValidatePortNumber(int port)
    {
        System.out.println(port);
        return (port>=1024 &&port<=65535);
    }






    public static int getFreePort()
    {
        int firstPort=firstAvailablePort;
        
        boolean isFree=false;
        while(firstPort <lastAvailableport && !isFree)
        {
            Socket bindingsocket=null;
            

            try
                {

                    firstPort++;

                    bindingsocket = new Socket();
                    bindingsocket.bind(new InetSocketAddress(firstPort));
                    
                    if(bindingsocket.isBound())
                        {
                            isFree=true;
                            bindingsocket.close();
                        }
                }
            catch (UnknownHostException ex)
                {
                    Logger.getLogger(NetworkUtils.class.getName())
                    .log(Level.SEVERE, null, ex);
                }
            catch (IOException ex)
                {
                  
                }



                }
     return firstPort;

   }
    
    public static int getFreeRtpReceivingPort()
    {
        int firstPort=firstRTPPortRange;
        int secondPort=firstRTPPortRange+1;
        
        boolean isFree=false;
        while((firstPort<lastRTPPortRange/2) )
        {
            Socket bindingsocket1=null;
            Socket bindingsocket2=null;
            try
                {
                    bindingsocket1 = new Socket();
                    bindingsocket2 = new Socket();
                    bindingsocket1.bind(new InetSocketAddress(firstPort));
                    bindingsocket2.bind(new InetSocketAddress(secondPort));
                    if(bindingsocket1.isBound()&&bindingsocket2.isBound())
                        {System.out.println("isbound");
                           
                            bindingsocket1.close();
                            bindingsocket2.close();
                            System.out.println(isFree);
                            return firstPort;
                        }
                    else
                    {
                        System.out.println(firstPort);
                        firstPort=firstPort+2;
                        secondPort=secondPort+2;
                    }
                }
            catch (UnknownHostException ex)
                {
                    Logger.getLogger(NetworkUtils.class.getName())
                    .log(Level.SEVERE, null, ex);
                }
            catch (IOException ex)
                {
                  
                }



                }
     return firstPort;

        
    }

    public static int getFreeRtpSendingPort()
    {
        int firstPort=lastRTPPortRange/2+4;
        int secondPort=lastRTPPortRange/2+5;

        boolean isFree=false;
        while((firstPort<lastRTPPortRange) )
        {
            Socket bindingsocket1=null;
            Socket bindingsocket2=null;
            try
                {
                    bindingsocket1 = new Socket();
                    bindingsocket2 = new Socket();
                    bindingsocket1.bind(new InetSocketAddress(firstPort));
                    bindingsocket2.bind(new InetSocketAddress(secondPort));
                    if(bindingsocket1.isBound()&&bindingsocket2.isBound())
                        {System.out.println("isbound");

                            bindingsocket1.close();
                            bindingsocket2.close();
                            System.out.println(isFree);
                            return firstPort;
                        }
                    else
                    {
                        System.out.println(firstPort);
                        firstPort=firstPort+2;
                        secondPort=secondPort+2;
                    }
                }
            catch (UnknownHostException ex)
                {
                    Logger.getLogger(NetworkUtils.class.getName())
                    .log(Level.SEVERE, null, ex);
                }
            catch (IOException ex)
                {

                }



                }
     return firstPort;


    }




    private static byte[] tokenizeIPString(String IPV4)
    {
        //TODO better parse ipv4 string
        byte[] b=new byte[4];
        int i = 0;

        StringTokenizer tk = new StringTokenizer(IPV4, ".");

        while(tk.hasMoreTokens())
                {
                    b[i]= (byte) Integer.parseInt((String)tk.nextElement());
                    i++;
                }
        return b;
    }



}

