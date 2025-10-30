package edu.school21.model;

import edu.school21.annotation.OrmColumn;
import edu.school21.annotation.OrmColumnId;
import edu.school21.annotation.OrmEntity;

@OrmEntity(table = "product")
public class Product {
    @OrmColumnId
    private Long id;
    @OrmColumn(name = "name_col", length = 50)
    private String name;
    @OrmColumn(name = "price_col")
    private Double price;
    @OrmColumn(name = "weight_col")
    private Integer weight;

    public Product(Long id, String name, Double price, Integer weight) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return String.format("{id=%d, name='%s', price=%f, weight=%d}", id, name, price, weight);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCount() {
        return weight;
    }

    public void setCount(Integer weight) {
        this.weight = weight;
    }
}
