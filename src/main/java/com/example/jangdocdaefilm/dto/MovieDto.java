package com.example.jangdocdaefilm.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieDto {
    private String adult;
    private String backdrop_path;
    private List<String> genre_ids;
    private String id;
    private String original_language;
    private String original_title;
    private String overview;
    private String popularity;
    private String poster_path;
    private String release_date;
    private String title;
    private String video;
    private String vote_average;
    private String vote_count;
}
