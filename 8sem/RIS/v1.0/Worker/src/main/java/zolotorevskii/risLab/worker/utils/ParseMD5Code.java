package zolotorevskii.risLab.worker.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ParseMD5Code {
   public static String parse(String str){
       MessageDigest md;
       String result = null;
       try{
           md = MessageDigest.getInstance("MD5");
           byte[] messageDigest = md.digest(str.getBytes());
           StringBuilder sb = new StringBuilder();
           for (byte b : messageDigest) {
               sb.append(String.format("%02x", b));
           }
           result = sb.toString();
//           byte[] bytesOfMessage = str.getBytes(StandardCharsets.US_ASCII);
//           md = MessageDigest.getInstance("MD5");
//           byte[] theMD5digest = md.digest(bytesOfMessage);
//           result = new String(theMD5digest, StandardCharsets.US_ASCII);

       }catch (NoSuchAlgorithmException e){
           System.out.println("parse error");
           return null;
       }
       return result;
    }

    public static String listCollapse(List<String> list){
       var s = String.join("", list.toArray(new String[0]));
//       if(s.equals("abcd")){
//           System.out.println("gukag");
//       }
        return s;
    }
}
