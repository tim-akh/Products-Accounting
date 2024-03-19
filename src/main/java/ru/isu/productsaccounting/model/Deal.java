package ru.isu.productsaccounting.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "deal")
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "unit")
    @NotNull
    private String unit;

    @Column(name = "operation")
    @NotNull
    private String operation;

    @Column(name = "quantity")
    @DecimalMin(value = "0.001", message = "Количество должно быть больше 0!")
    @NotNull(message = "Количество необходимо заполнить!")
    private Float quantity;

    @Column(name = "price_for_unit")
    @NotNull(message = "Цену необходимо заполнить!")
    private Float priceForUnit;

    @Column(name = "deal_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Дату необходимо заполнить!")
    private Date dealDate;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    public Deal() {
    }

    public Deal(Long id, String unit, String operation, Float quantity,
                Float priceForUnit, Date dealDate, Product product) {
        this.id = id;
        this.unit = unit;
        this.operation = operation;
        this.quantity = quantity;
        this.priceForUnit = priceForUnit;
        this.dealDate = dealDate;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public Float getPriceForUnit() {
        return priceForUnit;
    }

    public void setPriceForUnit(Float priceForUnit) {
        this.priceForUnit = priceForUnit;
    }

    public Date getDealDate() {
        return dealDate;
    }

    public void setDealDate(Date dealDate) {
        this.dealDate = dealDate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
