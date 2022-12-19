import javax.persistence.*;

/**
 * This Class consist of fields and, getter and setter methods for each field
 */
@Entity
@Table(name = "Comparison")
public class Comparison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @ManyToOne
    @JoinColumn(name = "laptop_id", nullable = false)
    Laptop laptop;

    @Column(name = "price")
    float price;

    @Column(name = "url")
    String url;

    @Column(name = "supplier")
    String supplier;

    /**
     * @return String the String that contains all the fields of this class
     */
    @Override
    public String toString() {
        return "Comparison{" +
                "id=" + id +
                ",\n laptop=" + laptop +
                ",\n price=" + price +
                ",\n url='" + url + '\'' +
                ",\n supplier='" + supplier + '\'' +
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
     * @return Laptop instance
     */
    public Laptop getLaptop() {
        return laptop;
    }

    /**
     *
     * @param laptop the laptop you want to set
     */
    public void setLaptop(Laptop laptop) {
        this.laptop = laptop;
    }

    /**
     *
     * @return price
     */
    public float getPrice() {
        return price;
    }

    /**
     * @param price the price that you want to set
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     *
     * @return string url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url that you want to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return supplier
     */
    public String getSupplier() {
        return supplier;
    }

    /**
     * @param supplier the supplier that you want to set
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
