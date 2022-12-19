import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.Locale;

/**
 * This class consist of non-static methods, which inherited from upper-class.
 * Method run() throws exception
 * It Scrapes Amazon.co.uk website
 */
public class AmazonScraper extends WebScraper {
    private volatile boolean runThread = false;

    /**
     * Gets data from website and stores it in database
     */
    @Override
    public void run() {

        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);

        WebDriver driver = new ChromeDriver(options);

        String[] cpuList = {"m1", "m2", "i3", "i5", "i7", "i9"};
        String[] modelList = {"air", "pro"};
        String[] ramList = {"8GB", "16GB", "32GB", "64GB"};
        String[] ssdList = {"256GB", "512GB", "1TB", "1000GB"};
        String[] yearList = {"2019", "2020", "2021", "2022"};
        String[] screenSizeList = {"13\"", "13-inch", "13 inch", "13.6\"", "13.6-inch", "13.6 inch", "14\"", "14-inch", "14 inch", "16\"", "16-inch", "16 inch"};

        driver.get("https://www.amazon.co.uk/s?k=macbook&i=computers&rh=n%3A429886031%2Cp_89%3AApple&dc&ds=v1%3A9WTyFXoEE%2FRrBVaLPEIAdgMX6%2F5O8sIngYqVsH9E9so&qid=1669460987&rnid=1632651031&ref=sr_nr_p_89_1");

        runThread = true;
        while (runThread) {

            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                List<WebElement> laptopList = driver.findElements(By.cssSelector(".s-result-item.s-asin.sg-col.s-widget-spacing-small"));

                for (WebElement element : laptopList) {

                    String tempModel = null;
                    String tempYear = null;
                    String tempCpu = null;
                    String tempSsd = null;
                    String tempRam = null;
                    String tempScreen = null;
                    float tempPrice = 0;

                    for (String model : modelList) {
                        if (element.getText().toLowerCase(Locale.ROOT).contains(model)) {
                            tempModel = model;
                            break;
                        }
                    }
                    for (String year : yearList) {
                        if (element.getText().toLowerCase(Locale.ROOT).contains(year)) {
                            tempYear = year;
                            break;
                        }
                    }
                    for (String cpu : cpuList) {
                        if (element.getText().toLowerCase(Locale.ROOT).contains(cpu)) {
                            tempCpu = cpu;
                            break;
                        }
                    }
                    for (String ssd : ssdList) {
                        if (element.getText().toLowerCase(Locale.ROOT).contains(ssd.toLowerCase(Locale.ROOT))) {
                            tempSsd = ssd;
                            break;
                        }
                    }
                    for (String ram : ramList) {
                        if (element.getText().toLowerCase(Locale.ROOT).contains(ram.toLowerCase(Locale.ROOT))) {
                            tempRam = ram;
                            break;
                        }
                    }
                    for (String screen : screenSizeList) {
                        if (element.getText().toLowerCase(Locale.ROOT).contains(screen)) {
                            tempScreen = screen.startsWith("13.6") ? "13.6-inch" : screen.startsWith("13") ? "13-inch" : screen.startsWith("14") ? "14-inch" : "16-inch";
                        }
                    }
                    String[] lines = element.getText().split("\n");
                    for (String line : lines) {
                        if (line.startsWith("£") && line.contains(",")) {
                            String[] price = line.split(",");
                            tempPrice = (Integer.parseInt(price[0].substring(1)) * 1000) + Float.parseFloat(price[1]);
                            break;
                        } else if (line.startsWith("£") && !line.contains(",")) {
                            tempPrice = Float.parseFloat(line.substring(1));
                            break;
                        }
                    }

                    if (tempModel != null && tempRam != null && tempCpu != null && tempYear != null && tempSsd != null && tempScreen != null && tempPrice != 0) {
                        Laptop laptop = new Laptop();
                        laptop.setBrandName("Apple");
                        laptop.setModel("MacBook " + tempModel.substring(0, 1).toUpperCase() + tempModel.substring(1).toLowerCase());
                        laptop.setDescription(element.getText());
                        laptop.setImageURL(tempModel.equals("air") ? "./images/air.jpg" : "./images/macbookPro.jpg");
                        laptop.setSsd(tempSsd.equals("1TB") ? "1000GB" : tempSsd);
                        laptop.setRam(tempRam);
                        laptop.setCpu(tempCpu.startsWith("i") ? "Intel " + tempCpu.toUpperCase(Locale.ROOT) : tempCpu.toUpperCase(Locale.ROOT));
                        laptop.setYear(Integer.parseInt(tempYear));
                        laptop.setScreen(tempScreen);

                        Comparison comparison = new Comparison();
                        comparison.setLaptop(laptop);
                        comparison.setSupplier("Amazon");
                        comparison.setPrice(tempPrice);
                        comparison.setUrl(element.findElement(By.cssSelector(".a-link-normal.s-underline-text.s-underline-link-text.s-link-style.a-text-normal")).getAttribute("href").toString());

                        try {
                            super.laptopDao.saveAndMerge(comparison);
                        } catch (Exception ex) {
                            System.out.println("COULD NOT SAVE DATA, other thread is using sessionFactory");
                            ex.printStackTrace();
                        }
                    }
                }
                WebElement nextBtn = driver.findElement(By.cssSelector(".s-pagination-item.s-pagination-next.s-pagination-button.s-pagination-separator"));
                driver.get(nextBtn.getAttribute("href"));
            }
            stopThread();
        }
    }

    /**
     * Stops the thread
     */
    public void stopThread() {
        runThread = false;
    }
}
