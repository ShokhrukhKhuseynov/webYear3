

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
/**
 * This class consist of non-static methods that return instance of a class.
 * All the methods are stored as bean
 * One method throws exception if it cannot build sessionFactory
 */
@Configuration
public class AppConfig {
    SessionFactory sessionFactory;

    /**
     * @return WebScraperManager instance that contains a list of scrapers
     */
    @Bean
    public WebScraperManager scraperManager(){
        WebScraperManager scraperManager = new WebScraperManager();
        
        //Create list of web scrapers and add to scraper manager
        List<WebScraper> scraperList = new ArrayList();
        scraperList.add(amazonScraper());
        scraperList.add(appleScraperAir());
        scraperList.add(appleScraperPro());
        scraperManager.setWebScraperList(scraperList);

        return scraperManager;
    }

    /**
     *
     * @return AmazonScraper instance that contains LaptopDao instance
     */
    @Bean
    public WebScraper amazonScraper(){
        WebScraper scraper1 = new AmazonScraper();
        scraper1.setLaptopDao(laptopDao());
        return scraper1;
    }
    /**
     *
     * @return AppleScraperAir instance that contains LaptopDao instance
     */
    @Bean
    public WebScraper appleScraperAir(){
        WebScraper scraper1 = new AppleScraperAir();
        scraper1.setLaptopDao(laptopDao());
        return scraper1;
    }
    /**
     *
     * @return AppleScraperPro instance that contains LaptopDao instance
     */
    @Bean
    public WebScraper appleScraperPro(){
        WebScraper scraper1 = new AppleScraperPro();
        scraper1.setLaptopDao(laptopDao());
        return scraper1;
    }
    /**
     * @return LaptopDao instance that contains sessionFactory instance
     */
    @Bean
    public LaptopDao laptopDao(){
        LaptopDao laptopDao = new LaptopDao();
        laptopDao.setSessionFactory(sessionFactory());
        return laptopDao;
    }

    /**
     * @return SessionFactory instance
     */
    @Bean
    public SessionFactory sessionFactory(){
        if(sessionFactory == null){//Build sessionFatory once only
            try {
                //Create a builder for the standard service registry
                StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

                //Load configuration from hibernate configuration file.
                //Here we are using a configuration file that specifies Java annotations.
                standardServiceRegistryBuilder.configure("hibernate.cfg.xml");

                //Create the registry that will be used to build the session factory
                StandardServiceRegistry registry = standardServiceRegistryBuilder.build();
                try {
                    //Create the session factory - this is the goal of the init method.
                    sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
                }
                catch (Exception e) {
                        /* The registry would be destroyed by the SessionFactory, 
                            but we had trouble building the SessionFactory, so destroy it manually */
                        System.err.println("Session Factory build failed.");
                        e.printStackTrace();
                        StandardServiceRegistryBuilder.destroy(registry);
                }
                //Output result
                System.out.println("Session factory built.");
            }
            catch (Throwable ex) {
                // Make sure you log the exception, as it might be swallowed
                System.err.println("SessionFactory creation failed." + ex);
                ex.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
