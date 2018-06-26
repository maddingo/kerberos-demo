package com.findwise.kerberos;

/**
 * @author Peter Gylling - email: peter.jorgensen@findwise.com
 */

import com.sun.security.auth.module.Krb5LoginModule;

import javax.security.auth.Subject;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * This is simple Java program that tests ability to authenticate
 * with Kerberos using the JDK implementation.
 * <p>
 * The program uses no libraries but JDK itself.
 */
public class KrbTest {

    private void loginImpl(final String propertiesFileName) throws Exception {
        System.out.println(System.getProperty("java.version"));

        final Subject subject = new Subject();

        final Krb5LoginModule krb5LoginModule = new Krb5LoginModule();
        final Map<String, String> optionMap = new HashMap<>();

        File f = new File(propertiesFileName);
        System.out.println("======= loading property file [" + f.getAbsolutePath() + "]");
        Properties p = new Properties();
        try (InputStream is = new FileInputStream(f)) {
            p.load(is);
        }
        Properties sysProps = new Properties();
        Iterator<Map.Entry<Object,Object>> propsIterator = p.entrySet().iterator();
        while (propsIterator.hasNext()) {
            Map.Entry<Object, Object> entry = propsIterator.next();
            String entryKey = entry.getKey().toString();
            if (entryKey.startsWith("system_")) {
                sysProps.put(entryKey.substring("system_".length()), entry.getValue());
                propsIterator.remove();
            }
        }
        for (Map.Entry<?,?> entry : sysProps.entrySet()) {
            System.setProperty(entry.getKey().toString(), entry.getValue().toString());
        }
        System.setProperty("sun.security.krb5.debug", "true");

        optionMap.putAll((Map) p);

        optionMap.put("debug", "true"); // switch on debug of the Java implementation

        krb5LoginModule.initialize(subject, null, new HashMap<String, String>(), optionMap);

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
                krbTest.loginImpl(args[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
