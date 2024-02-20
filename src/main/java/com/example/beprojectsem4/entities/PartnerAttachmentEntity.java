package com.example.beprojectsem4.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "partner_attachment")
public class PartnerAttachmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "partner_id")
    private PartnerEntity partnerId;
    private String type;
    private String url;

    public PartnerAttachmentEntity(PartnerEntity partnerId, String type, String url){
        this.partnerId = partnerId;
        this.type = type;
        this.url = url;
    }
}
