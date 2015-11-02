/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.Message;

import SoftPhone.Configuration.Network.NetworkConfiguration;
import SoftPhone.Configuration.Sip.SipConfiguration;

/**
 *
 * @author didier
 */
public class SipMessageBankFactory
{
    private final SipMessageBank sipMessageBank;

    private SipMessageBankFactory(SipHeaderBank headerBank,NetworkConfiguration networkConfiguration, SipConfiguration sipConfiguration)
    {
        sipMessageBank =new SipMessageBank();
        sipMessageBank.setHeaderBank(headerBank);
        sipMessageBank.setMessageFactory(headerBank.getMessageFactory());
        sipMessageBank.setNetworkConfiguration(networkConfiguration);
        sipMessageBank.setSipConfiguration(sipConfiguration);
    }


    public static SipMessageBank getSipMessageBankInstance(SipHeaderBank headerBank,NetworkConfiguration networkConfiguration, SipConfiguration sipConfiguration)
    {
        SipMessageBankFactory factory =new SipMessageBankFactory(headerBank, networkConfiguration, sipConfiguration);
        return factory.sipMessageBank;
    }

}
