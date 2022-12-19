import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * This Class consist of a field, getter and setter methods for that field and a synchronized method.
 * Synchronized method throws exception if it sessionFactory field is being used by other thread.
 */
public class LaptopDao {

    SessionFactory sessionFactory;

    /**
     *
     * @param comparison the comparison that to be stored in database
     * @return comparison that is stored in database
     * @throws Exception if sessionFactory is being used by other thread
     */
    public synchronized Comparison saveAndMerge(Comparison comparison) throws Exception {
        //Get a new Session instance from the session factory and start transaction
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        //First find or create comparison
        String queryStr = "from Laptop where brand='" + comparison.getLaptop().getBrandName()+ "' and " +
                "model='" + comparison.getLaptop().getModel() + "' and " +
                "year=" + comparison.getLaptop().getYear() + " and " +
                "ram='" + comparison.getLaptop().getRam() + "' and " +
                "ssd='" + comparison.getLaptop().getSsd() + "' and " +
                "cpu='" + comparison.getLaptop().getCpu() + "' and " +
                "screen='" + comparison.getLaptop().getScreen() + "'";
        List<Laptop> laptopList = session.createQuery(queryStr).getResultList();

        //laptop is in database
        if(laptopList.size() == 1) {//Found a single laptop
            comparison.setLaptop(laptopList.get(0));
        }
        //No laptop with that name in database
        else if (laptopList.size() == 0){
            session.saveOrUpdate(comparison.getLaptop());
        }
        //Error
        else{
            throw new Exception("Multiple laptops with the same brand and model");
        }

        //Finally save or update comparison
        queryStr = "from Comparison where laptop_id=" + comparison.getLaptop().getId() + " and supplier='"+comparison.getSupplier() +"'";
        List<Comparison> comparisonList = session.createQuery(queryStr).getResultList();

        //Comparison is in database
        if(comparisonList.size() == 1) {//Found a single Comparison
            //Update comparison if necessary
            comparisonList.get(0).setLaptop(laptopList.get(0)); //Update mapped laptop object
            session.saveOrUpdate(comparisonList.get(0));//Will save comparison
        }
        //No student with that name in database
        else if (comparisonList.size() == 0){
            //Create new comparison
            session.saveOrUpdate(comparison);
        }
        //Error
        else{
            System.out.println("Multiple comparisons with the same laptop ids");
            throw new Exception("Multiple comparisons with the same laptop ids");
        }

        //Commit transaction to save changes. Close the session and release database connection
        session.getTransaction().commit();
        session.close();
        return comparison;
    }

    /**
     *
     * @return sessionFactory field of this class
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     *
     * @param sessionFactory the sessionFactory that initialize sessionFactory field.
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
