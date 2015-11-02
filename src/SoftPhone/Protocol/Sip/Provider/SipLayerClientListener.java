/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.Provider;

import SoftPhone.Protocol.Sip.Message.Event.ClientErrorResponseEvent;
import SoftPhone.Protocol.Sip.Message.Event.GlobalErrorResponseEvent;
import SoftPhone.Protocol.Sip.Message.Event.InviteOKResponseEvent;
import SoftPhone.Protocol.Sip.Message.Event.ProvisionnalResponseEvent;
import SoftPhone.Protocol.Sip.Message.Event.RedirectResponseEvent;
import SoftPhone.Protocol.Sip.Message.Event.RegisterOkResponseEvent;
import SoftPhone.Protocol.Sip.Message.Event.ServerErrorResponseEvent;
import SoftPhone.Protocol.Sip.Message.RedirectEvent;

/**
 *
 * @author didier
 */
public interface SipLayerClientListener
{

    
    
    public void processProvisionalResponse(ProvisionnalResponseEvent event);
    public void processInviteOkResponse(InviteOKResponseEvent event);
    public void processRegisterOkResponse(RegisterOkResponseEvent event);
    public void processRedirectResponse(RedirectResponseEvent  event);
    public void processErrorClientResponse(ClientErrorResponseEvent event);
    public void processErrorClientResponse(RedirectEvent event);
    public void processServerErrorResponse(ServerErrorResponseEvent event);
    public void processGlobalErrorResponse(GlobalErrorResponseEvent  event);



}
