package com.example.demo.model.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data // getters setters
@AllArgsConstructor
@NoArgsConstructor
@ToString // you can print the class
public class TreeDto {

    private Long treeId;
    private Long userId;
    private String treeLocation;
    private String species;
    private LocalDateTime treePlantingDate;
    private String plantingTime;

}
