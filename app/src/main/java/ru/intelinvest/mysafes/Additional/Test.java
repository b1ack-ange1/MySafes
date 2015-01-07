package ru.intelinvest.mysafes.Additional;

import ru.intelinvest.mysafes.Additional.SimpleEncryptor;

/**
 * Created by Stanislav on 07.01.2015.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        /*
        LocalEncrypter le = new LocalEncrypter();
        le.setPassword("12345678");
        String encoded = le.encrypt("hello");
        System.out.println(encoded);
        */
        /*
        BFEncrypter bfEncrypter = new BFEncrypter();
        bfEncrypter.setPassword("12345678");
        byte[] encoded = bfEncrypter.encrypt("hello");
        System.out.println("length of encoded "+encoded.length);
        ArrayList<Byte> list = new ArrayList<>();
        for (int i = 0; i< encoded.length; i++){
            //System.out.println(encoded[i]);
            list.add(encoded[i]);
        }
        //System.out.println(sb);
        byte[] decoded = new byte[list.size()];
        for(int i = 0; i< list.size(); i++){
            decoded[i] = list.get(i);
            //System.out.println(sb.charAt(i));
        }
        System.out.println(bfEncrypter.decrypt(decoded));
*/
        SimpleEncryptor se = new SimpleEncryptor();
        se.setPassword("12345678");
        String encoded = se.encryptIt("hello");
        System.out.println(encoded);
    }
}
