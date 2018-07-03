package com.findwise.kerberos;

import org.apache.kerby.asn1.Asn1Dumper;
import org.apache.kerby.kerberos.kerb.keytab.Keytab;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class KeytabTest {

    @Test
    public void printKeytab() throws IOException {
        Keytab keytab = Keytab.loadKeytab(new File("/tmp/system.keytab"));
        final Asn1Dumper dumper = new Asn1Dumper();
        keytab.getPrincipals().forEach(p -> {
            p.dumpWith(dumper, 1);
        });
        System.out.println(dumper.output());
        assertThat(keytab.getPrincipals(), is(not(empty())));
    }
}
