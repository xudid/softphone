/*
 * TODO Penser à implémenter une exception qui est levée
 * si l'algorithme n'est pas MD5 au non spécifié
 */

package SoftPhone.Protocol.Sip.Provider;

/**
 *
 * @author didier
 */


// pour les propos de ce document , un digest MD5 de 128 bits est représenté
// comme des caractères ASCII32 bits
// Les bits dans le digest de 128 bits sont convertis du bit le plus
// significatif au bit le moins significatif
// 4 bits a la fois en leur représentation ASCII comme suit:
// Chaque groupe de 4 bits est représenté par sa notation hexadecimal depuis
// les caractères 0123456789ABCDEF
// OOOO est représenté par 0,0001 par 1 et 1111 par f
// On traite les 128 bits du hash MD5 4 par 4 du bit la plus significatif au
// moins significatif
// pour chaque groupe de 4 bits on prend la valeur en base 16 équivalente et
// l'on place sa
// représentation sous forme de caractére dans la chaine résultat
// 0000 =>0=>0 |0001 =>1 =>1|0010 =>2 =>2 |0011 =>3 =>3|0100 =>4 =>4 |0101 =>5
// =>5
// 0110 =>6 =>6 |0111 =>7 =>7 | 1000 =>8 =>8 | 1001 =>9 =>9 | 1010 =>a|....
// 1111 =>f
// ........................................................................
// la représentation binaire d'un caractéres est stockée sur 8 bits en ASCII

// du hash en terme de bit (4 bits de hash => 8 bit de caractére dans la chaine)
// on considere les caractéres ASCII de '0' à 'f'

// la methode digest renvoie un tableau de bytes un élément de tableau contient
// 8 bits
// les 8 bits les plus significatifs sont placés a gauche dans un mot de 8 bits
// ont fait donc un decalage de 4 bits vers la droite de chaque byte pour
// traiter d'abords les
// 4 bits les plus significatifs en premier
// sur 4 bits d'un byte on a 32 valeurs possible 16 positives et 16 négatives (0
// est dans les positifs pour ce coup là)
// hors on cherche une représentation des valeurs hexa positives entre 0 et f on
// ajoute donc 0X0F au résultat de la transformation
// du byte
//

// H(data) = MD5(data)
// KD(secret, data) = H(concat(secret, ":", data))







import SoftPhone.Protocol.Sip.Account.SipCredentials;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.sip.header.AuthorizationHeader;


public class DigestCalculate
{
	private static char[] toHex = { 
                                    '0', '1', '2', '3'
                                  , '4', '5', '6', '7'
                                  , '8', '9', 'a', 'b'
                                  , 'c', 'd', 'e', 'f'
                                  };
	/**
	 * @param digesturivalue uri of the request
	 * @param Method the method of the request
	 * @param auth entête d'autorisation présentant l'interface AuthorizationHeader
	 * @param usernamevalue  the account username
	 * @param realmvalue the realm of the registrar
	 * @param passwd the account password
     * @param content  contenu porté par le message SIP
	*/
   

	public static String CalculateResponse( 
                                          AuthorizationHeader auth
                                        , SipCredentials credentials
                                        , String Method
                                        ,String content
                                        )
    {
         String digesturivalue= auth.getURI().toString();
         String noncevalue=auth.getNonce();
         String realmvalue=auth.getRealm();
         String qop=auth.getQop();
         String algo=auth.getAlgorithm();
         String usernamevalue= credentials.getUserName();
         String passwd =credentials.getPassword();
         String entitybody=content;

		

		String A1="";
        String A2;

        if(algo.equalsIgnoreCase("MD5")||algo==null)A1 = usernamevalue
                                            +":"
                                            +realmvalue
                                            +":"
                                            +passwd;
        System.out.println("Dans calculate A1"+A1);

//       if (qop.contentEquals("auth-int"))A2 = Method
//                                               +":"
//                                               + digesturivalue
//                                               +":"
//                                               +H(entitybody);
//        else { A2 = Method +":"+ digesturivalue;}
         A2 = Method +":"+ digesturivalue;
		String s = KD(H(A1),noncevalue+":"+H(A2));
		return s;

	}
    // Instantiation de l'algorithme MD5
	private static String H(String s) {

		try {
			MessageDigest  digest = MessageDigest.getInstance("MD5");
			return toHexString(digest.digest(s.getBytes()));

		} catch (NoSuchAlgorithmException e) {
			// Manage failed to instantiate MD5 algorithm exception
			e.printStackTrace();
			return null;
		}

	}

	private static String toHexString(byte[] digest) {
		int pos = 0;
		// 1 byte =8 bits =>2 caractéres dans la représentation
		// la chaine de caractéres représentant le hash MD5 sera donc d'une
		// taille
		// double de celle du digest
		char[] c = new char[digest.length * 2];

		for (int i = 0; i < digest.length; i++) {
			// décalage des 4 bits de poids fort vers la droite et and bit à bit
			// avec 0X0F (00001111 en binaire)car on garde que 4 bits
			// et ajout à la chaine représentative du hash
			c[pos++] = toHex[(digest[i] >> 4) & 0x0F];
			// and avec 0x0F pour récupérer les 4 bits de poids faible
			// et ajout à la chaine représentative du hash
			c[pos++] = toHex[digest[i] & 0x0f];
		}
		return new String(c);
	}

	private static String KD(String a,String b) {
		String s =H(a+":"+ b);
		return s;
	}


	}



