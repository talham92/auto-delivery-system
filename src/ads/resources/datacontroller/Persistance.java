/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.datacontroller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 *
 * @author mgamell
 */
/**
 * A class designed to set or end the persistence of other classes in the
 * system.
 */
public class Persistance {

    private static EntityManagerFactory emf;
//    private static EntityManager em = null;
    private static Logger logger = Logger.getLogger(Persistance.class.getName());

    private Persistance() throws Exception {
        throw new Exception("Don't try to instantiate Persistance");
    }

    /**
     * initPersistance is the initiation function of the class.
     *     
*/
    public static void initPersistance() {
        Map<String, String> prop = new HashMap();
//todo        prop.put("javax.persistence.jdbc.url", "jdbc:mysql://sansor:3306/ads?zeroDateTimeBehavior=convertToNull");
        prop.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/ads?zeroDateTimeBehavior=convertToNull");
        try {
            emf = Persistence.createEntityManagerFactory("adsPU", prop);
            emf.createEntityManager();
        } catch (Exception e) {
            prop = new HashMap();
            prop.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/ads?zeroDateTimeBehavior=convertToNull");
            emf = Persistence.createEntityManagerFactory("adsPU", prop);
        }
//        em = emf.createEntityManager();
    }

    /**
     * deleteAllPersistanceRecords is a function used to delete all delivery
     * records .
     *     
*/
    public static void deleteAllPersistanceRecords() {
        EntityManager em = Persistance.getEntityManager();
        try {
            for (Class c : ClassesPackagesFinder.getClasses("ads.resources.data")) {
                final String className = c.getName();
                logger.finest("Trying select * from: " + className);
                try {
                    em.getTransaction().begin();
                    Query q = em.createQuery("select c from " + className + " c");
                    for (Object o : q.getResultList()) {
                        em.remove(o);
                    }
                    em.getTransaction().commit();
                    logger.finest("-ok: " + className);
                } catch (Exception e) {
                    em.getTransaction().rollback();
                    logger.finest("-not ok: " + className);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Persistance.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Persistance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * deinitPersistance is the destructive function of the class.
     *     
*/
    public static void deinitPersistance() {
//        em.close();
        emf.close();
    }

    /**
     *
     * @return
     */
    public static EntityManager getEntityManager() {
        if (emf == null) {
            throw new RuntimeException("getEntityManager called before initPersistance");
        }
        return emf.createEntityManager();
    }

    private static class ClassesPackagesFinder {

        /**
         * Scans all classes accessible from the context class loader which
         * belong to the given package and subpackages.
         *
         * @param packageName The base package
         * @return The classes
         * @throws ClassNotFoundException
         * @throws IOException
         */
        private static Class[] getClasses(String packageName)
                throws ClassNotFoundException, IOException {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            assert classLoader != null;
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            List<File> dirs = new ArrayList<File>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                dirs.add(new File(resource.getFile()));
            }
            ArrayList<Class> classes = new ArrayList<Class>();
            for (File directory : dirs) {
                classes.addAll(findClasses(directory, packageName));
            }
            return classes.toArray(new Class[classes.size()]);
        }

        /**
         * Recursive method used to find all classes in a given directory and
         * subdirs.
         *
         * @param directory The base directory
         * @param packageName The package name for classes found inside the base
         * directory
         * @return The classes
         * @throws ClassNotFoundException
         */
        private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
            List<Class> classes = new ArrayList<Class>();
            if (!directory.exists()) {
                return classes;
            }
            File[] files = directory.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    assert !file.getName().contains(".");
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                }
            }
            return classes;
        }
    }
}
