package com.phoenix.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

    /**
     * 使用注解的形式配置多表关系
     * 1.声明关系
     *      @OneToMany: 配置一对多关系
     *      targetEntity：对方对象的字节码对象
     * 2.配置外键(中间表)
     *
     * 在客户实体类上（一的一方）添加了外键的配置，所以对于客户而言，也具备了维护外键的作用
     */
    //配置客户和联系人之间的关系（一对多关系）
    // @OneToMany(targetEntity = LinkMan.class,fetch = FetchType.LAZY)
    // @JoinColumn(name = "lkm_cust_id",referencedColumnName = "cust_id")
    /**
     * 放弃外键维护权利
     * 上面两句话是一的一方维护外键
     * 但一的一方需要放弃维护外键，只需要维护关系
     * //mappedBy里面的值是多的一方的配置的关系的属性名称
     *
     * cascade级联操作
     *      CascadeType.ALL: 所有
     *      CascadeType.PERSIST  新增
     *                  MERGE    更新
     *                  REMOVE   删除
     *
     * 对象导航查询，
     * 一对多
     * 一的一方，默认就是延迟加载
     * fetch = FetchType.LAZY
     */
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private Set<LinkMan> linkMans= new HashSet<>();

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

    public Set<LinkMan> getLinkMans() {
        return linkMans;
    }

    public void setLinkMans(Set<LinkMan> linkMans) {
        this.linkMans = linkMans;
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