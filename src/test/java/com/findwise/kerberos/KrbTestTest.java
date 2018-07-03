package com.findwise.kerberos;

//import org.apache.kerby.kerberos.kdc.impl.NettyKdcServerImpl;
import org.apache.kerby.kerberos.kerb.server.KdcConfig;
import org.apache.kerby.kerberos.kerb.server.SimpleKdcServer;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class KrbTestTest {

    @Test
    public void fullKrbTest() throws Exception {
        SimpleKdcServer kdc = new SimpleKdcServer();

//        kdc.setKdcHost("localhost");
//        kdc.setKdcRealm("goldhmar.tieto.com");
//        kdc.setKdcPort(1088);

        kdc.setAllowUdp(false);
        kdc.setWorkDir(new File("target"));
//        kdc.setInnerKdcImpl(new NettyKdcServerImpl(kdc.getKdcSetting()));
        kdc.init();

        kdc.start();

        Map<String, String> krbTestProperties = createProperties(kdc);
        new KrbTest().login(krbTestProperties);
    }

    private Map<String, String> createProperties(SimpleKdcServer kdc) throws Exception {
        KdcConfig cfg = kdc.getKadmin().getKdcConfig();

        final String servicePrincipal = "HTTP/example.com@EXAMPLE.COM";
        kdc.createPrincipal(servicePrincipal);

        String keytabFileName = String.format("target/keytab_%x.keytab", new Random().nextInt());
        File keytabFile = new File(keytabFileName);

        kdc.exportPrincipal(servicePrincipal, keytabFile);
        Map<String, String> props = new HashMap<>();
        props.put("keyTab", keytabFile.getAbsolutePath());
        props.put("principal", servicePrincipal);
        props.put("doNotPrompt", "true");
        props.put("refreshKrb5Config", "true");
        props.put("useTicketCache", "false");
        props.put("renewTGT", "false");
        props.put("useKeyTab", "true");
        props.put("storeKey", "true");
        props.put("isInitiator", "true");

        return props;
    }

}
