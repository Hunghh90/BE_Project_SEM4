package com.example.beprojectsem4.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "partner_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"partner"})
public class PartnerPaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long partnerPaymentId;
    @Column(name = "payment_infor")
    private String paypalInfor;
    @Column(name = "bank_account")
    private String bankAccount;
    @Column(name = "bank_name")
    private String bankName;
    private String status;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;
    @OneToOne
    @JoinColumn(name = "partner_id")
    private PartnerEntity partner;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
    public PartnerPaymentEntity(String paypalInfor,String bankAccount,String bankName,PartnerEntity partner,String status) {
        this.paypalInfor = paypalInfor;
        this.bankAccount = bankAccount;
        this.bankName = bankName;
        this.partner = partner;
        this.status = status;
    }
}
