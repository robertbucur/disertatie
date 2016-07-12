package ro.ubblcuj.cs.collaborativetexteditor.persistence;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFile;
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


    public static Session getSession(){
        SessionFactory sessionFactory = HibernateUtil.configureSessionFactory();
        Session session = sessionFactory.openSession();
        return session;
    }
}