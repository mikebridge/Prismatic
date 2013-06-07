package com.bridgecanada.net;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

//import javax.ws.rs.core.NewCookie;
import java.io.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 02/04/13
 * Time: 9:47 PM
 */
public class CookieSerializer implements Serializable{


    // taken from loopj:
    //  https://github.com/loopj/android-async-http/blob/master/src/com/loopj/android/http/PersistentCookieStore.java

    public static String serializeCookie(Cookie cookie) {
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutStream);
            writeObject(cookie, objectOutputStream);
            objectOutputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return byteArrayToHexString(byteOutStream.toByteArray());
    }

    // taken from loopj:
    //  https://github.com/loopj/android-async-http/blob/master/src/com/loopj/android/http/PersistentCookieStore.java
    public static Cookie deSerializeCookie(String cookieStr) {
        byte[] bytes = hexStringToByteArray(cookieStr);
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);

        try {
            ObjectInputStream ois = new ObjectInputStream(is);

            return readObject(ois);

        } catch (Exception e) {
            // Todo: figure out what todo with the error.
            e.printStackTrace();
        }
        return null;
    }

    // Using some super basic byte array <-> hex conversions so we don't have
    // to rely on any large Base64 libraries. Can be overridden if you like!
    private  static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (byte element : b) {
            int v = element & 0xff;
            if(v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for(int i=0; i<len; i+=2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


    private static void writeObject(Cookie cookie, ObjectOutputStream out) throws IOException {
        out.writeObject(cookie.getName());
        out.writeObject(cookie.getValue());
        out.writeObject(cookie.getPath());
        out.writeObject(cookie.getDomain());
        out.writeObject(cookie.getComment());
        // TODO: FIx date
        if (cookie.getExpiryDate() != null) {
            out.writeLong(cookie.getExpiryDate().getTime());
        }
        else {
            out.writeLong(0L);
        }
        out.writeBoolean(cookie.isSecure());
        out.writeInt(cookie.getVersion());


    }

    private static Cookie readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String name = (String)in.readObject();
        String value = (String)in.readObject();
        String path= ((String)in.readObject());
        String domain = ((String)in.readObject());
        String comment = ((String)in.readObject());
        long expiryDateLong = in.readLong();

        boolean secure = in.readBoolean();
        int version = in.readInt();
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setComment(comment);
        cookie.setPath(path);
        cookie.setDomain(domain);
        cookie.setVersion(version);
        cookie.setSecure(secure);
        if (expiryDateLong != 0L) {
            cookie.setExpiryDate(new Date(expiryDateLong));
        }
        return cookie;
        //return new Cookie(name, value, path, domain, version, comment, maxAge, secure);

    }
}
