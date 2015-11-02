/*
 * This the MOINMOIN license
 * you can do that you want !!!!!!!!!
 */

package SoftPhone.Protocol.Sip.Provider;

import gov.nist.javax.sip.stack.SIPTransaction;
import java.util.HashMap;


/**
 *
 * @author didier
 */
public class TransactionsRepository {
    private HashMap<String,SIPTransaction>TransactionMap;

    public TransactionsRepository()
    {
        TransactionMap =new HashMap<String, SIPTransaction>();
    }

    public void addTransaction(String transactionID,SIPTransaction t)
    {
        if(TransactionMap.containsKey(transactionID));
        else TransactionMap.put(transactionID, t);
    }

    public void removeTransaction(String transactionID,SIPTransaction t)
    {
        if(TransactionMap.isEmpty());
        else if(TransactionMap.containsKey(transactionID))
        {
            TransactionMap.remove(transactionID);
        }

    }

    public SIPTransaction getTransaction(String branch)
    {
       if(TransactionMap.isEmpty())return null;
       else if(TransactionMap.containsKey(branch))return TransactionMap.get(branch);
       else return null;
    }



}
