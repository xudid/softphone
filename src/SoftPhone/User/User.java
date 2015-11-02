/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SoftPhone.User;

import SoftPhone.User.Identity.Identity;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author didier
 */
public class User {
     @XStreamImplicit(itemFieldName = "Contacts")
private List<Identity> Identities =new LinkedList() ;


}
