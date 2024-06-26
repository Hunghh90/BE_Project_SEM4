package com.example.beprojectsem4.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="programs")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties({"user","partner","attachment","donations","subPrograms"})
public class ProgramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long programId;
    @Column(name = "program_name",unique = true)
    private String programName;
    private Long target;
    @Column(name = "start_donate_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDonateDate;
    @Column(name = "end_donate_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDonateDate;
    @Column(name = "finish_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date finishDate;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String status;
    @Column(name = "recruit_collaborators")
    private boolean recruitCollaborators;
    @Column(name = "reason_rejection")
    private String reasonRejection;
    private int share;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "partner_id", referencedColumnName = "id")
    private PartnerEntity partner;
    @OneToMany(mappedBy = "programId")
    private List<ProgramAttachmentEntity> attachment;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "total")
    private Double totalMoney;
    @OneToMany(mappedBy = "program")
    private List<DonationEntity> donations;
    @OneToMany(mappedBy = "program")
    private List<SubProgramEntity> subPrograms;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public ProgramEntity(String programName,String description,Long target,Date startDonateDate,Date endDonateDate,Date finishDate,String status,boolean recruitCollaborators){
        this.programName = programName;
        this.description = description;
        this.startDonateDate = startDonateDate;
        this.target = target;
        this.endDonateDate = endDonateDate;
        this.finishDate = finishDate;
        this.status = status;
        this.recruitCollaborators = recruitCollaborators;
    }
}
