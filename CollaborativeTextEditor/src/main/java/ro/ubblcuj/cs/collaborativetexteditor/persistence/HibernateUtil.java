package ro.ubblcuj.cs.collaborativetexteditor.persistence;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFile;
import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFileChange;
import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFileVersion;

import java.util.List;
import java.util.Properties;

public class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;

    public static SessionFactory configureSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        configuration.configure();

        Properties properties = configuration.getProperties();

        serviceRegistry = new ServiceRegistryBuilder().applySettings(properties).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        return sessionFactory;
    }

    public static void saveFileToDb(CTXEFile file) {
        Session session = getSession();
        session.beginTransaction();

        session.save(file);
        session.getTransaction().commit();

        session.close();
    }

    public static List<CTXEFile> getAllFiles(){
        Session session = getSession();
        session.beginTransaction();

        List<CTXEFile> files;
        Query query = session.createQuery("from CTXEFile");
        files = query.list();

        session.close();
        return files;
    }

    public static List<CTXEFileVersion> getAllVersionsForFile (int fileId) {
        Session session = getSession();
        session.beginTransaction();

        List<CTXEFileVersion> files;
        Query query = session.createQuery("from CTXEFileVersion where fileId=:fileId")
                .setInteger("fileId", fileId);
        files = query.list();

        session.close();
        return files;
    }

    public static int getNextVersionNumber (int fileId) {
        Session session = getSession();
        session.beginTransaction();

        Query query = session.createQuery("select max(ctxfv.versionNumber) from CTXEFile ctxf" +
                " join CTXEFileVersion ctxfv on ctxf.id = ctxfv.fileId" +
                " where ctxf = :fileId")
                .setInteger("fileId", fileId);
        int nextVersionNumber = (Integer) query.uniqueResult();

        session.close();
        return ++nextVersionNumber;
    }

    public static void insertFileChange(CTXEFileChange fileChange) {
        Session session = getSession();
        session.beginTransaction();

        session.save(fileChange);
        session.getTransaction().commit();

        session.close();
    }

    public static Session getSession(){
        SessionFactory sessionFactory = HibernateUtil.configureSessionFactory();
        Session session = sessionFactory.openSession();
        return session;
    }
}