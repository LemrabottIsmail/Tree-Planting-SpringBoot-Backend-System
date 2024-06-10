package com.example.demo.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Table(name = "tree")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Tree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long treeId;
    private Long userId;
    @Lob
    private byte[] treePhoto;
    private String treeLocation;
    private String species;
    @CreationTimestamp
    private LocalDateTime treePlantingDate;
    private String plantingTime;

}