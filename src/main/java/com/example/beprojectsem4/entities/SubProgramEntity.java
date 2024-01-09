package com.example.beprojectsem4.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "sub_program")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubProgramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long subProgramId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private String type;
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

    public SubProgramEntity(UserEntity user, ProgramEntity program,String type){
        this.user = user;
        this.program = program;
        this.type = type;
    }
}
