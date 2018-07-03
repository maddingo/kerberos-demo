package com.findwise.kerberos;

/**
 * @author Peter Gylling - email: peter.jorgensen@findwise.com
 */

import com.sun.security.auth.module.Krb5LoginModule;

import javax.security.auth.Subject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * This is simple Java program that tests ability to authenticate
 * with Kerberos using the JDK implementation.
 * <p>
 * The program uses no libraries but JDK itself.
 */
public class KrbTest {

    public void login(final Map<String, String> props) throws Exception {
        System.out.println(System.getProperty("java.version"));

        final Subject subject = new Subject();

        final Krb5LoginModule krb5LoginModule = new Krb5LoginModule();

        System.setProperty("sun.security.krb5.debug", "true");

        props.put("debug", "true"); // switch on debug of the Java implementation

        final HashMap<String, String> sharedState = new HashMap<>();
        krb5LoginModule.initialize(subject, null, sharedState, props);

        boolean loginOk = krb5LoginModule.login();
        System.out.println("======= login:  " + loginOk);

        boolean commitOk = krb5LoginModule.commit();
        System.out.println("======= commit: " + commitOk);

        System.out.println("======= Subject: " + subject);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Missing Properties");
        } else {
            KrbTest krbTest = new KrbTest();
            try {
                File f = new File(args[0]);
                Properties props = new Properties();
                try (FileReader fr = new FileReader(f)) {
                    props.load(fr);
                }
                Map<String, String> propMap = props.keySet().stream().collect(Collectors.toMap(Object::toString, Object::toString));
                krbTest.login(propMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
