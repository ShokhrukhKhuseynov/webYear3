import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Locale;

/**
 * This class consist of non-static methods, which inherited from upper-class.
 * Method run() throws exception
 * It Scrapes Apple.co.uk website
 */
public class AppleScraperAir extends WebScraper {
    private volatile boolean runThread = false;

    /**
     * Gets data from website and stores it in database
     */
    @Override
    public void run() {

        try {
            Document doc = Jsoup.connect("https://www.apple.com/uk/shop/buy-mac/macbook-air/m1-chip")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64;0x 64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                    .get();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            String[] specifications = doc.select(".rc-productbundle-specs").text().split("16");
            String[] prices = doc.select(".rc-prices-fullprice").text().split(" ");
            String[] titles = doc.select(".rc-productbundle-title").text().split("Apple ");
            String model = doc.select(".localnav-title").text();

            runThread = true;

            while (runThread) {

                for (int i = 0; i < specifications.length; i += 4) {

                    String screen = specifications[i + 1].substring(20, 27).contains(".") ? specifications[i + 1].substring(20, 29) : specifications[i + 1].substring(20, 27);
                    float price = prices[i].contains(",") ? (Float.parseFloat(prices[i].substring(1, 2)) * 1000) + Float.parseFloat(prices[i].substring(3)) : Float.parseFloat(prices[i].substring(1));
                    String cpuName = titles[i + 1].substring(0, 2);
                    String ram = titles[i + 1].length() < 114 ? titles[i + 1].substring(75, 78) : titles[i + 1].substring(77, 80);
                    String ssd = titles[i + 1].length() < 114 ? titles[i + 1].substring(94, 99) : titles[i + 1].substring(96, 101);
                    int year = cpuName.equals("M1") ? 2020 : 2022;
                    String description = titles[i + 1].substring(0, titles[i + 1].length() - 2).concat("\n16" + specifications[i + 1]);
                    String cpu = titles[i + 1].substring(13, 19).endsWith("e") ? titles[i + 1].substring(13, 19).replace(Character.toString((char) 8209), "-").toLowerCase(Locale.ROOT) : titles[i + 1].substring(13, 20).replace(Character.toString((char) 8209), "-").toLowerCase(Locale.ROOT);
                    String gpu = titles[i + 1].substring(28, 34).endsWith("e") ? titles[i + 1].substring(28, 34).replace(Character.toString((char) 8209), "-").toLowerCase(Locale.ROOT) : titles[i + 1].substring(28, 35).replace(Character.toString((char) 8209), "-").toLowerCase(Locale.ROOT);

                    Laptop laptop = new Laptop();
                    laptop.setBrandName("Apple");
                    laptop.setModel(model);
                    laptop.setSsd(ssd.equals("1TB") ? "1000GB" : ssd);
                    laptop.setCpu(cpuName);
                    laptop.setScreen(screen);
                    laptop.setRam(ram);
                    laptop.setYear(year);
                    laptop.setDescription(description);
                    laptop.setImageURL("./images/air.jpg");

                    Comparison comparison = new Comparison();
                    comparison.setLaptop(laptop);
                    comparison.setSupplier("Apple");
                    comparison.setPrice(price);
                    comparison.setUrl("https://www.apple.com/uk/shop/buy-mac/macbook-air/silver-apple-" + cpuName.toLowerCase(Locale.ROOT) + "-chip-with-" + cpu + "-cpu-and-" + gpu + "-gpu-" + ssd.toLowerCase(Locale.ROOT));

                    try {
                        super.laptopDao.saveAndMerge(comparison);
                    } catch (Exception ex) {
                        System.out.println("COULD NOT SAVE DATA, other thread is using sessionFactory");
                        ex.printStackTrace();
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }
                stopThread();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Stops the thread
     */
    public void stopThread() {
        runThread = false;
    }

}
