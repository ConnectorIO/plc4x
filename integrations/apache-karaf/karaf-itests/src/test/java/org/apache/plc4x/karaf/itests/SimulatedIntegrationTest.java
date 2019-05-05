/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/
package org.apache.plc4x.karaf.itests;

import org.apache.plc4x.java.DriverManager;
import org.apache.plc4x.java.PlcDriverManager;
import org.apache.plc4x.java.api.PlcConnection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.ProbeBuilder;
import org.ops4j.pax.exam.TestProbeBuilder;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.framework.Constants;

import javax.inject.Inject;

import static junit.framework.TestCase.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.composite;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFileExtend;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFilePut;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class SimulatedIntegrationTest extends FeaturesIntegrationTest {

    @Inject
    DriverManager manager;

    @Test
    public void testInstallSimulatedFeature() throws Exception {
        assertFeatureInstalled("plc4x-simulated-driver");

        PlcConnection connection = manager.getConnection("test:foo");
        assertNotNull(connection);
    }

    /**
     * This is further customization of test execution environemnt.
     *
     * Because test operation #testInstallSimulatedFeature is wrapped in OSGi bundle it requires plc4x-simulated-driver
     * before test execution starts in order to resolve dependencies and let driver be found.
     *
     * @return Updated configuration which boots Karaf with plc4x-simulated-driver for tests.
     */
    @Override
    public Option[] config() {
        return composite(
            editConfigurationFileExtend("etc/org.apache.karaf.features.cfg", "featuresBoot", "plc4x-simulated-driver")
        ).add(super.config()).getOptions();
    }
}