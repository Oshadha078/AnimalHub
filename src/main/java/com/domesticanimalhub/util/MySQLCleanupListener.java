package com.domesticanimalhub.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

@WebListener   // works without web.xml entry; you can also add a <listener> if you prefer
public class MySQLCleanupListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 1) Stop MySQL Connector/J background cleanup thread
        try {
            // Requires mysql-connector-j 8.x on classpath
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (Throwable t) {
            // swallow – not fatal
        }

        // 2) Deregister JDBC drivers loaded by this webapp’s classloader
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver d = drivers.nextElement();
            if (d.getClass().getClassLoader() == cl) {
                try {
                    DriverManager.deregisterDriver(d);
                } catch (Exception ignore) { /* no-op */ }
            }
        }

        // 3) If you ever switch to a pool (e.g., HikariCP), close it here.
        // Example:
        // DataSource ds = DB.getDataSourceIfAny();
        // if (ds instanceof HikariDataSource) ((HikariDataSource) ds).close();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // nothing
    }
}
