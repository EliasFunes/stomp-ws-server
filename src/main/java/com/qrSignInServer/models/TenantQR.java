package com.qrSignInServer.models;

import javax.persistence.*;

@Entity
@Table(name = "tenant_qr",
        uniqueConstraints = { //other constraints
                @UniqueConstraint(name = "UniqueTenantAndQR", columnNames = { "tenant", "qr_id" })}
)
public class TenantQR {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "tenant", nullable = false)
    private Long tenant;

    @Column(name = "qr_id", nullable = false)
    private String qrID;

    public TenantQR() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQrId() {
        return qrID;
    }

    public void setQrId(String qrID) {
        this.qrID = qrID;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }
}
