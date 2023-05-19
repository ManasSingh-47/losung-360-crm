package com.crm.contacts.util;

import com.crm.contacts.config.Config;
import com.crm.contacts.models.Contacts;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.service.ServiceRegistry;

import java.util.logging.Logger;

public class HibernateUtil {

    private static final Logger LOG = Logger.getLogger(HibernateUtil.class.getName());

    private static SessionFactory sessionFactory;

    public static void initSessionFactory() {
        Configuration configuration = new Configuration();

        configuration.getProperties().setProperty("hibernate.connection.driver_class", Config.hibernateDriverClass);
        configuration.getProperties().setProperty("hibernate.connection.url",Config.hibernateURL);
        configuration.getProperties().setProperty("hibernate.connection.username",Config.hibernateUsername);
        configuration.getProperties().setProperty("hibernate.connection.password",Config.hibernatePassword);
        configuration.getProperties().setProperty("hibernate.dialect", Config.hibernateDialect);
        configuration.addAnnotatedClass(Contacts.class);

        ServiceRegistry builderReplica = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(builderReplica);
    }

    public static Session getHibernateSession() {
        return sessionFactory.openSession();
    }

    /**
     * Begin transaction.
     *
     * @param session the session
     */
    public static void beginTransaction(Session session) {
        session.beginTransaction();
    }

    /**
     * Commit transaction.
     *
     * @param session the session
     */
    public static void commitTransaction(Session session) {
        if(session != null && session.getTransaction().getStatus() == TransactionStatus.ACTIVE){
            session.getTransaction().commit();
        }
    }

    /**
     * Roll back transaction.
     *
     * @param session the session
     */
    public static void rollBackTransaction(Session session) {
        if(session != null && session.getTransaction().getStatus() == TransactionStatus.ACTIVE){
            session.getTransaction().rollback();
        }
    }

    /**
     * Close session.
     *
     * @param session the session
     */
    public static void closeSession(Session session) {
        if(session != null) {
            session.close();
        }
    }
}
