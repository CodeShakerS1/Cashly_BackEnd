package com.example.cashly_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryid;

    @Column(name = "categoryname", nullable = false, length = 50)
    private String categoryname;

    @Column(name = "icon", length = 255)
    private String icon;

    @Column(name = "limit_amount", precision = 10, scale = 2)
    private BigDecimal limitAmount;

    @Column(name = "userid")
    private Integer userId;

    public Category() {
    }

    public Category(Integer categoryid, String categoryname, String icon, BigDecimal limitAmount, Integer userId) {
        this.categoryid = categoryid;
        this.categoryname = categoryname;
        this.icon = icon;
        this.limitAmount = limitAmount;
        this.userId = userId;
    }

    public Integer getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Integer categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}