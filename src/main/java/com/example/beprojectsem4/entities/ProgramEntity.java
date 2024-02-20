package com.example.beprojectsem4.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="programs")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ProgramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long programId;
    @Column(name = "program_name")
    private String programName;
    private Long target;
    @Column(name = "start_donate_date")
    private Date startDonateDate;
    @Column(name = "end_donate_date")
    private Date endDonateDate;
    @Column(name = "finish_date")
    private Date finishDate;
    private String description;
    private String status;
    @OneToMany(mappedBy = "program")
    private List<ProgramAttachmentEntity> attachment;
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

    public ProgramEntity(String programName,String description,Long target,Date startDonateDate,Date endDonateDate,Date finishDate,String status){
        this.programName = programName;
        this.description = description;
        this.startDonateDate = startDonateDate;
        this.target = target;
        this.endDonateDate = endDonateDate;
        this.finishDate = finishDate;
        this.status = status;
    }
}
