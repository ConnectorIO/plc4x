package org.apache.plc4x.karaf.itests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class AdsIntegrationTest extends FeaturesIntegrationTest {

    @Test
    public void testInstallAdsFeature() throws Exception {
        features.installFeature("plc4x-ads-driver", INSTALL_OPTIONS);

        assertFeatureInstalled("plc4x-ads-driver");
    }

}