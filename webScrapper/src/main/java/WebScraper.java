/**
 * This Class consist of a field and, getter and setter methods for that field.
 *
 */
public class WebScraper extends Thread{
    LaptopDao laptopDao;

    /**
     *
     * @return LaptopDao field of this class
     */
    public LaptopDao getLaptopDao() {
        return laptopDao;
    }

    /**
     *
     * @param laptopDao the LaptopDao that initialize laptopDao field of this class
     */
    public void setLaptopDao(LaptopDao laptopDao) {
        this.laptopDao = laptopDao;
    }
}
