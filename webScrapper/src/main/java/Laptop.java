import javax.persistence.*;

/**
 * This Class consist of fields and, getter and setter methods for each field
 */
@Entity
@Table(name="Laptop")
public class Laptop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "brand")
    String brandName;

    @Column(name = "model")
    String model;

    @Column(name = "year")
    int year;

    @Column(name = "ram")
    String ram;

    @Column(name = "ssd")
    String ssd;

    @Column(name = "cpu")
    String cpu;

    @Column(name = "screen")
    String screen;

    @Column(name = "description")
    String description;

    @Column(name = "image_url")
    String imageURL;

    /**
     * @return String the String that contains all the fields of this class
     */
    @Override
    public String toString() {
        return "Laptop{" +
                "id=" + id +
                ",\n brandName='" + brandName + '\'' +
                ",\n model='" + model + '\'' +
                ",\n year=" + year +
                ",\n ram='" + ram + '\'' +
                ",\n ssd='" + ssd + '\'' +
                ",\n cpu='" + cpu + '\'' +
                ",\n screen='" + screen + '\'' +
                ",\n description='" + description + '\'' +
                ",\n imageURL='" + imageURL + '\'' +
                '}';
    }

    /**
     *
     * @return id
     */
    public int getId() {
        return id;
    }
    /**
     *
     * @return brandName field of this class
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     *
     * @param brandName the brandName that initialize branName Field of this class
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     *
     * @return model field of this class
     */
    public String getModel() {
        return model;
    }

    /**
     *
     * @param model the model that initialize model field of this class
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     *
     * @return description field of this class
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description the description that initialize description field of this class
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return imageURL field of this class
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     *
     * @param imageURL the imageURL that initialize imageURL field of this class
     */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     *
     * @return year field of this class
     */
    public int getYear() {
        return year;
    }
    /**
     *
     * @param year the year that initialize year field of this class
     */
    public void setYear(int year) {
        this.year = year;
    }
    /**
     *
     * @return ram field of this class
     */
    public String getRam() {
        return ram;
    }
    /**
     *
     * @param ram the ram that initialize ram field of this class
     */
    public void setRam(String ram) {
        this.ram = ram;
    }
    /**
     *
     * @return ssd field of this class
     */
    public String getSsd() {
        return ssd;
    }
    /**
     *
     * @param ssd the ssd that initialize ssd field of this class
     */
    public void setSsd(String ssd) {
        this.ssd = ssd;
    }
    /**
     *
     * @return cpu field of this class
     */
    public String getCpu() {
        return cpu;
    }
    /**
     *
     * @param cpu the cpu that initialize cpu field of this class
     */
    public void setCpu(String cpu) {
        this.cpu = cpu;
    }
    /**
     *
     * @return screen field of this class
     */
    public String getScreen() {
        return screen;
    }
    /**
     *
     * @param screen the screen that initialize screen field of this class
     */
    public void setScreen(String screen) {
        this.screen = screen;
    }
}
