package com.example.beprojectsem4.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "partners")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long partnerId;
    @Column(name = "partner_name")
    private String partnerName;
    private String email;
    private String description;
    private String status;
    @OneToMany(mappedBy = "partner")
    private List<PartnerEntity> attachment;
    @Column(name = "created_at")
    private Date createAt;
    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
    public PartnerEntity(String partnerName,String email,String description,String status) {
        this.partnerName = partnerName;
        this.email = email;
        this.description = description;
        this.status = status;
    }
}
