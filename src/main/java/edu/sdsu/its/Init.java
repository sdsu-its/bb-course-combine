package edu.sdsu.its;

import edu.sdsu.its.Jobs.SyncCourseList;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Initialize and Teardown the WebApp and DB
 *
 * @author Tom Paulus
 *         Created on 10/21/2016.
 */
@WebListener
public class Init implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(Init.class);
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_FIRST_NAME = "Administrator";
    private static final String DEFAULT_LAST_NAME = "User";
    private static final String DEFAULT_EMAIL = "";


    /**
     * Initialize the Webapp with the Default User if no users exist.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // TODO Check for DSK, and Create if necessary
        // TODO Add Vault Secret for DSK Name

        // TODO Create First Admin User in DB for WebUI

        try {
            Schedule.getScheduler().start();
        } catch (SchedulerException e) {
            LOGGER.error("Problem Starting Scheduler", e);
        }
        try {
            SyncCourseList.schedule(Schedule.getScheduler(), 2);
        } catch (SchedulerException e) {
            LOGGER.error("Problem Scheduling Course List Update Job", e);
        }
    }

    /**
     * Deregister DB Driver to prevent memory leaks.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            Schedule.getScheduler().shutdown();
        } catch (SchedulerException e) {
            LOGGER.error("Problem shutting down scheduler");
        }

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        // Loop through all drivers
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == cl) {
                // This driver was registered by the webapp's ClassLoader, so deregister it:
                try {
                    LOGGER.info(String.format("Deregistering JDBC driver: %s", driver));
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException ex) {
                    LOGGER.fatal(String.format("Error deregistering JDBC driver: %s", driver), ex);
                }
            } else {
                // driver was not registered by the webapp's ClassLoader and may be in use elsewhere
                LOGGER.info(String.format("Not deregistering JDBC driver %s as it does not belong to this webapp's ClassLoader", driver));
            }
        }
    }
}