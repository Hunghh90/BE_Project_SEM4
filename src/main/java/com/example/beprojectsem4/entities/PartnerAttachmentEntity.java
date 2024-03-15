package com.example.beprojectsem4.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "partner_attachment")
@JsonIgnoreProperties({"partnerId"})
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
