package jpa1;

import javax.persistence.*;

@Entity
@Table(name="Dishes")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="myid")
    private long id;

    @Column(nullable = false)
    private String name;

    private int price;

    private int weight;

    private String action;

    public Dish() {}

    public Dish(String name, int price,int weight,String action) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.action = action;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", weight="+ weight +
                ", action="+ action +
                '}';
    }
}
