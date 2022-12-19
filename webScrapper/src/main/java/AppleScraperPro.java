import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * This class consist of non-static methods, which inherited from upper-class.
 * Method run() throws exception
 * It Scrapes Apple.co.uk website
 */
public class AppleScraperPro extends WebScraper {
    private volatile boolean runThread = false;

    /**
     * Gets data from website and stores it in database
     */
    @Override
    public void run() {
        try {
            Document doc = Jsoup.connect("https://www.apple.com/uk/shop/buy-mac/macbook-pro/13-inch")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64;0x 64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                    .get();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            String[] prices = doc.select(".rc-prices-fullprice").text().split(" ");
            String[] description = doc.select(".list.rc-productbundle-specs").text().split("16-core Neural Engine");
            String model = doc.select(".localnav-title").text();

            String[] specs = doc.select(".list-title.rc-productbundle-title").text().split("[810]-Core");
            List<String> specifications = Arrays.stream(specs)
                    .filter(s -> s.length() > 10)
                    .map(s -> s.replaceAll(" core", "-Core"))
                    .map(s -> s.replaceAll(Character.toString((char) 8209), "-"))
                    .map(s -> {
                        if (s.contains("8-Core") && s.contains("10-Core")) {
                            return "8-Core CPU 10-Core" + s;
                        } else if (s.contains("8-Core")) {
                            return "8-Core" + s;
                        } else {
                            return "10-Core" + s;
                        }
                    })
                    .map(s -> s.endsWith("1") ? s.substring(0, s.length() - 1) : s)
                    .map(s -> {
                        if (s.charAt(s.indexOf("¹") + 2) != 'A')
                            return s.replaceAll("¹", " Apple M1 Chip with").replaceAll("cpu", "cpu and");
                        return s.replaceAll("¹", "");
                    })
                    .collect(Collectors.toList());
            runThread = true;
            while (runThread) {
                for (int i = 0; i < specifications.size(); i += 2) {

                    String[] data = specifications.get(i).split(" ");
                    String[] descData = description[i + 1].split(" ");
                    String cpu = data[0];
                    String gpu = data[2];

                    String screenSize = descData[1];
                    Float price = (Float.parseFloat(prices[i].substring(1, 2)) * 1000) + Float.parseFloat(prices[i].substring(3));
                    Laptop laptop = new Laptop();
                    laptop.setBrandName(data[10]);
                    laptop.setModel(model);
                    laptop.setRam(data[4]);
                    laptop.setSsd(data[7].equals("1TB") ? "1000GB" : data[7]);
                    laptop.setCpu(data[11]);
                    laptop.setScreen(screenSize);
                    laptop.setDescription(description[i].concat(specifications.get(i)));
                    laptop.setImageURL("./images/macbookPro.jpg");
                    laptop.setYear(data[11].equals("M2") ? 2022 : 2021);


                    Comparison comparison = new Comparison();
                    comparison.setPrice(price);
                    comparison.setLaptop(laptop);
                    comparison.setSupplier("Apple");
                    comparison.setUrl(laptop.getCpu().equals("M2") ? "https://www.apple.com/uk/shop/buy-mac/macbook-pro/" + laptop.getScreen().toLowerCase(Locale.ROOT) + "-space-grey-apple-m2-chip-with-" + cpu.toLowerCase(Locale.ROOT) + "-cpu-and-" + gpu.toLowerCase(Locale.ROOT) + "-gpu-" + (laptop.getSsd().toLowerCase(Locale.ROOT).equals("1000gb") ? "1tb" : laptop.getSsd().toLowerCase(Locale.ROOT)) :
                            "https://www.apple.com/uk/shop/buy-mac/macbook-pro/" + laptop.getScreen().toLowerCase(Locale.ROOT) + "-space-grey-" + cpu.toLowerCase(Locale.ROOT) + "-cpu-" + gpu.toLowerCase(Locale.ROOT) + "-gpu-" + (laptop.getSsd().toLowerCase(Locale.ROOT).equals("1000gb") ? "1tb" : laptop.getSsd().toLowerCase(Locale.ROOT)));
                    try {
                        super.laptopDao.saveAndMerge(comparison);
                    } catch (Exception ex) {
                        System.out.println("COULD NOT SAVE DATA, other thread is using sessionFactory");
                        ex.printStackTrace();
                    }

                }
                stopThread();
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Stops the thread
     */
    public void stopThread() {
        runThread = false;
    }

}
