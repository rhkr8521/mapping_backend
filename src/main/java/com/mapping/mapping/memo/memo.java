package com.mapping.mapping.memo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class memo {
    @Id @GeneratedValue
    private Long id;
    private String content;
    private String writer;
    private String lat;
    private String lng;
    private String img;
    private String date;
    private String tag;
    public memo() {
    }
    public memo(String content, String writer, String lat, String lng, String img, String date, String tag) {
        this.content = content;
        this.writer = writer;
        this.lat = lat;
        this.lng = lng;
        this.img = img;
        this.date = date;
        this.tag = tag;
    }
}