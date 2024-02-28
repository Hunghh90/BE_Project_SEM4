package com.example.beprojectsem4.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "program_attachment")
public class ProgramAttachmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "program_id")
    @JsonIgnore
    ProgramEntity programId;
    String type;
    String url;

    public ProgramAttachmentEntity(ProgramEntity programId, String type, String url){
        this.programId = programId;
        this.type = type;
        this.url = url;
    }
}
