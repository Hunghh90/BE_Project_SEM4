package com.example.beprojectsem4.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "partner_program")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerProgramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long partnerProgramId;
    @ManyToOne
    @JoinColumn(name = "partner_id")
    private PartnerEntity partner;
    @ManyToOne
    @JoinColumn(name = "program_id")
    private ProgramEntity program;
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

    public PartnerProgramEntity(PartnerEntity partner, ProgramEntity program){
        this.partner = partner;
        this.program = program;
    }
}
