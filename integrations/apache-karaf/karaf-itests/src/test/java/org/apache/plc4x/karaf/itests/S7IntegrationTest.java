package org.apache.plc4x.karaf.itests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class S7IntegrationTest extends FeaturesIntegrationTest {

    @Test
    public void testInstallS7Feature() throws Exception {
        features.installFeature("plc4x-s7-driver", INSTALL_OPTIONS);

        assertFeatureInstalled("plc4x-s7-driver");
    }

}