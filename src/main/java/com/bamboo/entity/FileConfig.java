package com.bamboo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "file_config")
@Getter
@Setter
@ToString
public class FileConfig {

    @Id
    @Column(name = "file_config_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long maxFileCount;

    private Long maxFileSize;

    private List<String> allowedFileExtensions;
}
