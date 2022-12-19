import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
/**
 * Holds the main method for the application
 */
public class Main {

    public static void main(String[] args) {

        //Instruct Spring to create and wire beans using annotations.
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        //Get web scraper manager bean - ONLY PULL A SINGLE CLASS FROM THE SPRING CONTEXT!
        WebScraperManager manager = (WebScraperManager) context.getBean("scraperManager");

        //Start the web scraping
        manager.startScraping();
     }

}
