package org.apache.plc4x.java.osgi;

import org.apache.plc4x.java.DriverManager;
import org.apache.plc4x.java.spi.PlcDriver;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ServiceLoader;

public class Activator implements BundleActivator, BundleTrackerCustomizer<List<ServiceRegistration<PlcDriver>>>,
    ServiceFactory<DriverManager> {

    private Logger logger = LoggerFactory.getLogger(Activator.class);
    private ServiceRegistration<DriverManager> registration;
    private BundleTracker<List<ServiceRegistration<PlcDriver>>> tracker;

    @Override
    public void start(BundleContext context) throws Exception {
        tracker = new BundleTracker<>(context, Bundle.ACTIVE, this);
        tracker.open();

        registration = context.registerService(DriverManager.class, new OsgiDriverManager(context), new Hashtable<>());
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        registration.unregister();

        tracker.close();
    }

    @Override
    public List<ServiceRegistration<PlcDriver>> addingBundle(Bundle bundle, BundleEvent event) {
        if (bundle.getBundleId() == 0) {
            return null;
        }
        try {
            ClassLoader cl = bundle.adapt(BundleWiring.class).getClassLoader();
            ServiceLoader<PlcDriver> drivers = ServiceLoader.load(PlcDriver.class, cl);
            List<ServiceRegistration<PlcDriver>> registrations = new ArrayList<>();
            for (PlcDriver driver : drivers) {
                Hashtable<String, String> props = new Hashtable<String, String>();
                props.put(OsgiDriverManager.PROTOCOL_CODE, driver.getProtocolCode());
                props.put(OsgiDriverManager.PROTOCOL_NAME, driver.getProtocolName());
                ServiceRegistration<PlcDriver> reg = bundle.getBundleContext().registerService(PlcDriver.class, driver, props);
                registrations.add(reg);
            }

            return registrations.isEmpty() ? null : registrations;
        } catch (Exception ex) {
            logger.error("Could not register PlcDrivers from bundle {}", bundle.getSymbolicName(), ex);
            return null;
        }
    }

    @Override
    public void modifiedBundle(Bundle bundle, BundleEvent event, List<ServiceRegistration<PlcDriver>> object) {
    }

    @Override
    public void removedBundle(Bundle bundle, BundleEvent event, List<ServiceRegistration<PlcDriver>> object) {
        for (ServiceRegistration<PlcDriver> reg : object) {
            reg.unregister();
        }
    }

    @Override
    public DriverManager getService(Bundle bundle, ServiceRegistration<DriverManager> registration) {
        return new OsgiDriverManager(bundle.getBundleContext());
    }

    @Override
    public void ungetService(Bundle bundle, ServiceRegistration<DriverManager> registration, DriverManager service) {
        if (service instanceof OsgiDriverManager) {
            ((OsgiDriverManager) service).close();
        }
    }
}
