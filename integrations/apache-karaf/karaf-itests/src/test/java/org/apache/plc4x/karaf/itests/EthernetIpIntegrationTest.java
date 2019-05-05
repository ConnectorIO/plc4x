package org.apache.plc4x.karaf.itests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class EthernetIpIntegrationTest extends FeaturesIntegrationTest {

    @Test
    public void testInstallEthernetIpFeature() throws Exception {
        features.installFeature("plc4x-ethernet-ip-driver", INSTALL_OPTIONS);

        assertFeatureInstalled("plc4x-ethernet-ip-driver");
    }

}