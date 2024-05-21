package com.bamboo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "hashtag")
@Getter
@Setter
@ToString
public class Hashtag extends BaseEntity {

    @Id
    @Column(name = "hashtag_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "hashtag_name", nullable = false)
    private String name;



    public void updateHashtag(String name) {
        this.name = name;
    }

}
