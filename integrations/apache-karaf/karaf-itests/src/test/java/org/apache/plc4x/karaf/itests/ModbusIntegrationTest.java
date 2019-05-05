package org.apache.plc4x.karaf.itests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class ModbusIntegrationTest extends FeaturesIntegrationTest {

    @Test
    public void testInstallModbusFeature() throws Exception {
        features.installFeature("plc4x-modbus-driver", INSTALL_OPTIONS);

        assertFeatureInstalled("plc4x-modbus-driver");
    }

}