package com.example.beprojectsem4.dtos.programDtos;

import com.example.beprojectsem4.entities.PartnerEntity;
import com.example.beprojectsem4.entities.ProgramAttachmentEntity;
import com.example.beprojectsem4.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProgramDto {

        @Nullable
        private String programName;
        @Nullable
        private Long target;
        @Nullable
        private Date startDonateDate;
        @Nullable
        private Date endDonateDate;
        @Nullable
        private Date finishDate;
        @Nullable
        private String description;
        @Nullable
        private boolean finishSoon;
        @Nullable
        private boolean recruitCollaborators;
        @Nullable
        private List<String> imageUrl;
        @Nullable
        private String imageLogo;



}
