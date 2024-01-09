package com.example.beprojectsem4.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "partner_payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private Date createAt;
    @Column(name = "updated_at")
    private Date updatedAt;
    @OneToOne
    @JoinColumn(name = "partner_id")
    private PartnerEntity partner;

    @PrePersist
    protected void onCreate() {
        createAt = new Date();
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
