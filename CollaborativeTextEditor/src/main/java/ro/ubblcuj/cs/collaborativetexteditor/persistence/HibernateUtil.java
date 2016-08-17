package ro.ubblcuj.cs.collaborativetexteditor.persistence;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFile;
import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFileChange;
import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFileVersion;

import java.util.Date;
import java.util.List;
import java.util.Properties;

public class HibernateUtil {

    private static SessionFactory sessionFactory;
    static {
        Configuration configuration = new Configuration();
        configuration.configure();
        Properties properties = configuration.getProperties();
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(properties).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    public static Session getSession() {
        Session session = sessionFactory.openSession();
        session.flush();
        session.clear();
        return session;
    }

    public static void saveFileToDb(CTXEFile file) {
        Session session = getSession();
        session.beginTransaction();

        session.save(file);
        session.getTransaction().commit();

        session.close();
    }

    public static void saveFileVersionToDb(CTXEFileVersion fileVersion) {
        Session session = getSession();
        session.beginTransaction();

        session.save(fileVersion);
        session.getTransaction().commit();

        session.close();
    }

    public static void deleteFileVersionFromDb(String fileName) {
        Session session = getSession();
        session.beginTransaction();

        Query query = session.createQuery("delete CTXEFileVersion where fileNameComposed =:fileNameComposed")
                .setString("fileNameComposed", fileName);
        query.executeUpdate();
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

    public static List<CTXEFile> getUpdateAllFiles(long lastUpdate){
        Session session = getSession();
        session.beginTransaction();

        List<CTXEFile> files;
        Query query = session.createQuery("from CTXEFile where lastModified>:lastModified order by lastModified asc")
                .setLong("lastModified", lastUpdate);
        files = query.list();

        session.close();
        return files;
    }

    public static List<CTXEFileChange> getAllChangesForFile(int fileId, int fileVersionId, long lastUpdate, String author){
        Session session = getSession();
        session.beginTransaction();

        List<CTXEFileChange> fileChanges;
        Query query = session.createQuery("from CTXEFileChange where fileId=:fileId and fileVersionId=:fileVersionId and datetime >:lastUpdate and author !=:author order by datetime asc")
                .setInteger("fileId", fileId)
                .setInteger("fileVersionId", fileVersionId)
                .setLong("lastUpdate", lastUpdate)
                .setString("author", author);
        fileChanges = query.list();

        session.close();
        return fileChanges;
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

    public static List<CTXEFileVersion> getUpdateAllVersionsForFile (long lastUpdate, int fileId) {
        Session session = getSession();
        session.beginTransaction();

        List<CTXEFileVersion> files;
        Query query = session.createQuery("from CTXEFileVersion where fileId=:fileId and creationDate>:lastUpdate order by creationDate asc")
                .setInteger("fileId", fileId)
                .setLong("lastUpdate", lastUpdate);
        files = query.list();

        session.close();
        return files;
    }

    public static int getNextVersionNumber (int fileId) {
        Session session = getSession();
        session.beginTransaction();

        Query query = session.createQuery("select max(versionNumber) from CTXEFileVersion where fileId=:fileId")
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

    public static CTXEFileVersion getIdentifierDetails(String identifier) {
        Session session = getSession();
        session.beginTransaction();

        List<CTXEFileVersion> files;
        Query query = session.createQuery("from CTXEFileVersion where identifier=:identifier")
                .setString("identifier", identifier);
        files = query.list();

        session.close();
        return files.get(0);
    }
}