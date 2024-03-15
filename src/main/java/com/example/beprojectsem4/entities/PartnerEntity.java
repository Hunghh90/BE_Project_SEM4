package com.example.beprojectsem4.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "partners")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"programs","attachment"})
public class PartnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long partnerId;
    @Column(name = "partner_name",unique = true)
    private String partnerName;
    @Column(unique = true)
    private String email;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String status;
    @OneToMany(mappedBy = "partner")
    private List<ProgramEntity> programs;
    @OneToMany(mappedBy = "partnerId")
    private List<PartnerAttachmentEntity> attachment;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
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
