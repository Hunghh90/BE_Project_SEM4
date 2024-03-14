package com.example.beprojectsem4.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;
    @Column(unique = true)
    private String email;
    private String password;
//    @Column(name = "birthday")
//    @Temporal(TemporalType.DATE)
//    @DateTimeFormat(pattern = "yyyy/MM/dd")
//    private Date bod;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String status;
    @Column(name = "display_name")
    private String displayName;
    @OneToMany(mappedBy = "user")
    private List<ProgramEntity> programs;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "created_at")
    private Date createAt;
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserAttachmentEntity userAttachment;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<RoleEntity> roles = new HashSet<>();
    @PrePersist
    protected void onCreate() {
        createAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
    public UserEntity(String email,String password,String phoneNumber,String displayName){
        this.email = email;
        this.password = password;
//        this.bod = bod;
        this.phoneNumber = phoneNumber;
        this.displayName = displayName;
        this.status = "Deactivate";
    }


}
