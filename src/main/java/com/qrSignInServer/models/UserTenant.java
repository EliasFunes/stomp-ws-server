package com.qrSignInServer.models;


import javax.persistence.*;

@Entity
@Table(name = "users_tenants")
public class UserTenant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "references_key_tenant")
    private String referencesKeyTenant; //el id que maneja el tenant de su usario

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tenants", referencedColumnName = "id")
    private Tenant tenant;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users", referencedColumnName = "id")
    private User user;

    public UserTenant() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferencesKeyTenant() {
        return referencesKeyTenant;
    }

    public void setReferencesKeyTenant(String referencesKeyTenant) {
        this.referencesKeyTenant = referencesKeyTenant;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
