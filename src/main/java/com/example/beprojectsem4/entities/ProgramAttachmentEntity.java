package com.example.beprojectsem4.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "program_attachment")
@JsonIgnoreProperties({"programId"})
public class ProgramAttachmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "program_id")
    ProgramEntity programId;
    String type;
    String url;

    public ProgramAttachmentEntity(ProgramEntity programId, String type, String url){
        this.programId = programId;
        this.type = type;
        this.url = url;
    }
}
