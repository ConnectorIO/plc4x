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

import org.apache.karaf.features.Feature;
import org.apache.karaf.features.FeaturesService;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.MavenUtils;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.ProbeBuilder;
import org.ops4j.pax.exam.TestProbeBuilder;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.framework.Constants;

import javax.inject.Inject;
import java.util.EnumSet;

import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.debugConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFilePut;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;

/**
 * Base class for feature/driver tests.
 *
 * There is separate file for each driver in order to decouple them from each other. If you need a common method or
 * adjust runtime settings for all drivers this is place to do such job. If you require adjustments specific to your
 * driver please override {@link #config()} method.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public abstract class FeaturesIntegrationTest {

    protected final static EnumSet<FeaturesService.Option> INSTALL_OPTIONS = EnumSet.of(FeaturesService.Option.Verbose);

    @Inject
    protected FeaturesService features;

    protected final void assertFeatureInstalled(String name) throws Exception {
        Feature feature = features.getFeature(name);
        assertTrue("Feature " + name + " should be installed", features.isInstalled(feature));

        features.uninstallFeature(name);
    }

    @Configuration
    public Option[] config() {
        String karafVersion = MavenUtils.getArtifactVersion("org.apache.karaf", "apache-karaf");
        MavenArtifactUrlReference frameworkURL = maven("org.apache.plc4x", "karaf-distribution").type("tar.gz").versionAsInProject();

        return new Option[] {
            karafDistributionConfiguration().karafVersion(karafVersion).frameworkUrl(frameworkURL)
        };
    }

    /**
     * Below method customize classpath for tests which are declared by all subclasses.
     *
     * This means that plc4x-api must be installed before test is executed. That's why we get a custom karaf distribution
     * which embeds plc4x and starts by default plc4x-api bundle.
     *
     * @param probe Test probe builder.
     * @return Created probe.
     */
    @ProbeBuilder
    public TestProbeBuilder probeConfiguration(TestProbeBuilder probe) {
        probe.setHeader(Constants.IMPORT_PACKAGE, "*,org.apache.plc4x.java");
        return probe;
    }

}