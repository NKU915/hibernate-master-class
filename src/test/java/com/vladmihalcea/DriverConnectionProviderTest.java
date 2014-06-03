package com.vladmihalcea;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Properties;

public class DriverConnectionProviderTest {

    @Test
    public void test() {
        final Long containerId;
        final SessionFactory sf = buildSessionFactory();

        { // populate database with a container
            Session session = sf.openSession();
            Transaction txn = session.beginTransaction();

            SecurityId securityId = new SecurityId();
            session.persist(securityId);

            txn.commit();
            session.close();
        }
    }

    private SessionFactory buildSessionFactory() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        properties.put("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
        properties.put("hibernate.connection.url", "jdbc:hsqldb:mem:test");
        properties.put("hibernate.connection.username", "sa");
        properties.put("hibernate.connection.password", "");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.show_sql", "true");

        return new Configuration()
                .addProperties(properties)
                .addAnnotatedClass(SecurityId.class)
                .buildSessionFactory(
                        new StandardServiceRegistryBuilder()
                                .applySettings(properties)
                                .build()
        );
    }

    @Entity(name = "SecurityId")
    private static class SecurityId {
        @Id
        @GeneratedValue
        Long id;
    }
}