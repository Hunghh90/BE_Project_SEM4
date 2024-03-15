package com.example.beprojectsem4.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"avatarUrl","roles","programs","donations"})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String status;
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;
    @OneToMany(mappedBy = "user")
    private List<DonationEntity> donations;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserAttachmentEntity avatarUrl;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<RoleEntity> roles = new HashSet<>();
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
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
