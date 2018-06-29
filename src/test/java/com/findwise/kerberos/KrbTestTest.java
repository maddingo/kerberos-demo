package com.findwise.kerberos;

import org.apache.kerby.kerberos.kerb.KrbException;
import org.apache.kerby.kerberos.kerb.server.KdcConfig;
import org.apache.kerby.kerberos.kerb.server.SimpleKdcServer;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class KrbTestTest {

    @Test
    public void fullKrbTest() throws Exception {
        SimpleKdcServer kdc = new SimpleKdcServer();

//        kdc.setKdcHost("localhost");
//        kdc.setKdcRealm("goldhmar.tieto.com");
//        kdc.setKdcPort(1088);

        kdc.setAllowUdp(true);
        kdc.setWorkDir(new File("target"));
        kdc.init();

        kdc.start();

        String[] krbTestProperties = createProperties(kdc);
        KrbTest.main(krbTestProperties);
    }

    private String[] createProperties(SimpleKdcServer kdc) throws Exception {
        KdcConfig cfg = kdc.getKadmin().getKdcConfig();
        File propFile = Files.createTempFile("krb_test", ".properties").toFile();
        propFile.deleteOnExit();

        final String servicePrincipal = "HTTP/example.com@EXAMPLE.COM";
        kdc.createPrincipal(servicePrincipal);

        String keytabFileName = String.format("target/%d.keytab", new Random().nextInt());
        File keytabFile = new File(keytabFileName);

        kdc.exportPrincipal(servicePrincipal, keytabFile);
        try (PrintWriter w = new PrintWriter(new FileWriter(propFile))) {
            w.printf("system_java.security.krb5.kdc=%s\n", cfg.getKdcHost());
            w.printf("system_java.security.krb5.realm=%s\n", cfg.getKdcRealm());
            w.printf("keyTab=%s\n", keytabFile.getAbsolutePath());
            w.printf("principal=%s\n", servicePrincipal);
            w.printf("doNotPrompt=true\n");
            w.printf("refreshKrb5Config=true\n");
            w.printf("useTicketCache=false\n");
            w.printf("renewTGT=false\n");
            w.printf("useKeyTab=true\n");
            w.printf("storeKey=true\n");
            w.printf("isInitiator=true\n");
        }

        return new String[] {propFile.getAbsolutePath()};
    }

}
