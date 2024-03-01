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
    @Column(name = "program_name",unique = true)
    private String programName;
    private Long target;
    @Column(name = "start_donate_date")
    private Date startDonateDate;
    @Column(name = "end_donate_date")
    private Date endDonateDate;
    @Column(name = "finish_date")
    private Date finishDate;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String status;
    @Column(name = "finish_soon")
    private boolean finishSoon;
    @Column(name = "recruit_collaborators")
    private boolean recruitCollaborators;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "partner_id", referencedColumnName = "id")
    private PartnerEntity partner;
    @OneToMany(mappedBy = "programId")
    private List<ProgramAttachmentEntity> attachment;
    @Column(name = "created_at")
    private Date createAt;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "total")
    private Double totalMoney;

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
