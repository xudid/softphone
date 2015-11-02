/*TODO MediaConfiguration make soft coded setup
 *TODO move the sdp JMF translation in a another class
*/

package SoftPhone.Configuration.Media;

import javax.media.format.AudioFormat;
import javax.sdp.SdpConstants;

/**
 *
 * @author didier
 */
public class MediaConfiguration {
    private static String [] availableCodecs={"PCMU","GSM"};

    public MediaConfiguration() {
    }

    

    public static String[] getAvailableCodecs()
    {
        return availableCodecs;
    }

    public static int[] getPrefferedCodecs()
    {
        int[]pc={3,0};
        return pc;
    }

    public String getSDPForJMFEncoding(String JMFConstant)
    {
       if (JMFConstant.equals(null))return null;
       else if (JMFConstant.equals(AudioFormat.ULAW_RTP))return Integer.toString(SdpConstants.PCMU);
       else if (JMFConstant.equals(AudioFormat.GSM_RTP))return Integer.toString(SdpConstants.GSM);
       else if (JMFConstant.equals(AudioFormat.G723_RTP))return Integer.toString(SdpConstants.G723);
      
       else return null;

    }

    public static String getJMFEncodingForPT(int pt)
    {
       String  af =null;
        switch(pt)
        {
            case SdpConstants.PCMU: af= AudioFormat.ULAW_RTP;
            break;

            case SdpConstants.GSM: af=AudioFormat.GSM_RTP;
            break;

            case SdpConstants.G723:af=AudioFormat.G723_RTP;
            break;

            default:;
        }
        return af;
    }

}
