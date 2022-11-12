package com.qrSignInServer.models;

import javax.persistence.*;

@Entity
@Table(name = "relation",
        uniqueConstraints = { //other constraints
                @UniqueConstraint(name = "UniqueLessorAndTenant", columnNames = { "lessor", "tenant" })}
)
public class Relation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "lessor", nullable = false)
    private Long lessor;

    @Column(name = "tenant", nullable = false)
    private Long tenant;

    @Column(name = "reference", nullable = false)
    private String reference;


    public Relation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLessor() {
        return lessor;
    }

    public void setLessor(Long lessor) {
        this.lessor = lessor;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
