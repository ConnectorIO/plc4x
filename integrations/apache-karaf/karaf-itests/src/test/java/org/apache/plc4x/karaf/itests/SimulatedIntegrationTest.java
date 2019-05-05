package org.apache.plc4x.karaf.itests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class SimulatedIntegrationTest extends FeaturesIntegrationTest {

    @Test
    public void testInstallSimulatedFeature() throws Exception {
        features.installFeature("plc4x-simulated-driver", INSTALL_OPTIONS);

        assertFeatureInstalled("plc4x-simulated-driver");
    }

}