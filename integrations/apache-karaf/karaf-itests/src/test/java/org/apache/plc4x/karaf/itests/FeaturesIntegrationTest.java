package org.apache.plc4x.karaf.itests;

import org.apache.karaf.features.Feature;
import org.apache.karaf.features.FeaturesService;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.MavenUtils;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

import javax.inject.Inject;
import java.util.EnumSet;

import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFileExtend;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public abstract class FeaturesIntegrationTest {

    protected final static EnumSet<FeaturesService.Option> INSTALL_OPTIONS = EnumSet.of(FeaturesService.Option.Verbose);
    private final String groupId;
    private final String artifactId;

    @Inject
    protected FeaturesService features;

    protected FeaturesIntegrationTest() {
        this("org.apache.plc4x", "karaf-features");
    }

    protected FeaturesIntegrationTest(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    @Configuration
    public Option[] config() {
        String featuresUrl = maven(groupId, artifactId).classifier("features").type("xml").versionAsInProject().getURL();
        String karafVersion = MavenUtils.getArtifactVersion("org.apache.karaf", "apache-karaf");
        MavenArtifactUrlReference frameworkURL = maven("org.apache.karaf", "apache-karaf").type("zip").version(karafVersion);

        return new Option[]{
            karafDistributionConfiguration().karafVersion(karafVersion).frameworkUrl(frameworkURL),
            editConfigurationFileExtend("etc/org.apache.karaf.features.cfg", "featuresRepositories", "," + featuresUrl)
        };
    }

    protected final void assertFeatureInstalled(String name) throws Exception {
        Feature feature = features.getFeature(name);
        assertTrue("Feature " + name + " should be installed", features.isInstalled(feature));

        features.uninstallFeature(name);
    }
}