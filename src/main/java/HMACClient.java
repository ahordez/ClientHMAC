import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.*;

public class HMACClient
{
    private final static String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private final static String SECRET = "verysecretprivatekey123456789";
    
    public static void main(String[] args) throws HttpException, IOException, NoSuchAlgorithmException, InvalidKeyException
    {
        long timestamp      = System.currentTimeMillis()/1000;
        long expression_id  = 33;
        String toSign = "POST\n/api/v1-2/expression/rateflop\n" + timestamp + "\nexpression_id=" + expression_id;

        String hmac = calculateHMAC(SECRET, toSign);
        System.out.println(hmac);
        
        //OUT : qo3VyovsGjD3hT6qo/cLZFbVynk=
        //test : http://localhost:8084/api/v1-2/expression/rateflop?expression_id=33&timestamp=1357397885&hmac=ltNkoAK7iSDHslOalku7xiBP1mM=
    }

    
    private static String calculateHMAC(String secret, String data) throws NoSuchAlgorithmException, InvalidKeyException
    {
        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data.getBytes());
        String result = new String(Base64.encodeBase64(rawHmac));
        return result;
    }
}