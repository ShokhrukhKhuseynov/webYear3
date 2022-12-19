import java.util.ArrayList;
import java.util.List;

/**
 * This Class consist of a field, getter and setter methods for that field and a method that starts all scrapers.
 */
public class WebScraperManager {
    List<WebScraper> webScraperList = new ArrayList<>();

    /**
     * Runs treads of each scraper
     */
    public void startScraping(){
        for(WebScraper webScraper : webScraperList)
            webScraper.start();
    }

    /**
     *
     * @return webScraperList field of this class
     */
    public List<WebScraper> getWebScraperList() {
        return webScraperList;
    }

    /**
     *
     * @param webScraperList the webScraperList that contains all scrapers
     */
    public void setWebScraperList(List<WebScraper> webScraperList) {
        this.webScraperList = webScraperList;
    }
}
