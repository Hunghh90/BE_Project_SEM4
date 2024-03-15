package com.example.beprojectsem4.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_attachment")
@JsonIgnoreProperties({"user"})
public class UserAttachmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private String type;
    private String url;

    public UserAttachmentEntity(UserEntity user, String type, String url) {
        this.user = user;
        this.type = type;
        this.url = url;
    }
}
