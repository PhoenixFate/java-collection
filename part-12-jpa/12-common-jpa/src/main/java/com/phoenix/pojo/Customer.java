package com.phoenix.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 客户实体类
 * 配置映射关系
 *    1.实体和表的映射关系
 *    2.实体中的属性和表中字段的映射关系
 *
 * @Entity 声明实体类
 * @Table 配置实体类和表的映射关系关系
 *
 *
 *
 */
@Entity
@Table(name = "cst_customer")
public class Customer implements Serializable {


    /**
     * @Id 声明主键,声明某个属性是表中的id
     * @GeneratedValue 配置主键的生成策略
     *     GenerationType.IDENTITY 自增 mysql
     *          底层数据库必须支持自动增长（以底层数据库支持的增长方式，对id自增）
     *     GenerationType.SEQUENCE 序列 由于oracle不支持自增，需要只能设置序列
     *          底层数据库必须支持序列 例如oracle
     *     GenerationType.TABLE
     *          jpa提供的一种机制，通过一张数据库表的形式来帮助我们完成主键自增
     *     GenerationType.AUTO
     *          程序根据数据库、以及配置自动的选择
     * @Column 配置属性和字段的映射关系
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_id")
    private Long custId;
    @Column(name = "cust_name")
    private String custName;
    @Column(name = "cust_source")
    private String custSource;
    @Column(name = "cust_industry")
    private String custIndustry;
    @Column(name = "cust_level")
    private String custLevel;
    @Column(name = "cust_address")
    private String custAddress;
    @Column(name = "cust_phone")
    private String custPhone;

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustSource() {
        return custSource;
    }

    public void setCustSource(String custSource) {
        this.custSource = custSource;
    }

    public String getCustIndustry() {
        return custIndustry;
    }

    public void setCustIndustry(String custIndustry) {
        this.custIndustry = custIndustry;
    }

    public String getCustLevel() {
        return custLevel;
    }

    public void setCustLevel(String custLevel) {
        this.custLevel = custLevel;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "custId=" + custId +
                ", custName='" + custName + '\'' +
                ", custSource='" + custSource + '\'' +
                ", custIndustry='" + custIndustry + '\'' +
                ", custLevel='" + custLevel + '\'' +
                ", custAddress='" + custAddress + '\'' +
                ", custPhone='" + custPhone + '\'' +
                '}';
    }
}