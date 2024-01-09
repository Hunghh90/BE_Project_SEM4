package com.example.beprojectsem4.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "donations")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class DonationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long donationId;
    private BigDecimal amount;
    private String description;
    @Column(name = "created_at")
    private Date createAt;
    @Column(name = "updated_at")
    private Date updatedAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "program_id")
    private ProgramEntity program;

    @PrePersist
    protected void onCreate() {
        createAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public DonationEntity(BigDecimal amount, String description,UserEntity user,ProgramEntity program){
        this.amount = amount;
        this.description = description;
        this.user = user;
        this.program = program;
    }
}
