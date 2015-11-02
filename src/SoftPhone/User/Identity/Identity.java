/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone.User.Identity;


import SoftPhone.Protocol.ProtocolAccount;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author didier
 */
public class Identity
{
     @XStreamImplicit(itemFieldName = "ProtocolAccounts")
private List<ProtocolAccount> accounts = new LinkedList();
}
