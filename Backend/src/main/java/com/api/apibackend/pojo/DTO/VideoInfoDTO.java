package com.api.apibackend.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoInfoDTO {

    private Integer id;

    private String title;

    private String description;

    private Integer likes;

    private Author author;

    private String url;

    private boolean liked;


    @Data
    @AllArgsConstructor
    public static class Author {
        private int id;
        private String name;
    }
}
