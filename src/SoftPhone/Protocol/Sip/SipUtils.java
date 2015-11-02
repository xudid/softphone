  /*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip;

/**
 *
 * @author didier
 */

import javax.sip.address.URI.*;
import javax.sip.message.Request;
public class SipUtils
{   public static enum existingMethods {INVITE,CANCEL,BYE,ACK,OPTIONS,INFO,MESSAGE,NOTIFY,REFER,PRACK,PUBLISH,REGISTER,SUSCRIBE,UPDATE};
    public static enum supportedMethods {INVITE,CANCEL,BYE,ACK,OPTION};
    public static boolean isMethodSupported(String method)
    {
        if(method.equalsIgnoreCase(Request.INVITE))return true;
        else if(method.equalsIgnoreCase(Request.REGISTER))return true;
        else if(method.equalsIgnoreCase(Request.CANCEL))return true;
        else if(method.equalsIgnoreCase(Request.BYE))return true;
        else if(method.equalsIgnoreCase(Request.ACK))return true;
        else if(method.equalsIgnoreCase(Request.OPTIONS))return true;
        else return false;
    }


    public static boolean validateSipAddress(String sipAddress)
    {
     boolean b=  sipAddress.startsWith("sip:")&&(!sipAddress.startsWith("sips:"));
     boolean b1=false;
     
         if(b)
         {
             String [] addr=sipAddress.substring(4,sipAddress.length()).split("@");
             if(addr.length==2)
             {
                     b1=true;
             }
         }

             return b&&b1;
    }
    
    public static boolean validateRegistrarSipAddress(String sipAddress)
    {
        boolean b=  sipAddress.startsWith("sip:")&&(!sipAddress.startsWith("sips:"));
     boolean b1=false;
     
     if(b)
     {
         b1=!sipAddress.contains("@");
     }
     
     return (b&b1);
    }


}