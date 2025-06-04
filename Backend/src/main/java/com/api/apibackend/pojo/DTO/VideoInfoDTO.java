package com.api.apibackend.pojo.DTO;

import com.api.apibackend.pojo.User;
import com.api.apibackend.pojo.Video;
import lombok.Data;

@Data
public class VideoInfoDTO {

    private Integer id;

    private String title;

    private String description;

    private Integer likes;

    private Author author;

    private String url;

    private boolean liked;

    public VideoInfoDTO(Video video, User author, boolean liked) {
        this.id = video.getId();
        this.title = video.getTitle();
        this.description = video.getDescription();
        this.likes = video.getLikes();
        this.author = new Author(author);
        this.url = video.getUrl();
        this.liked = liked;
    }

    @Data
    public static class Author {
        private int id;

        private String name;

        public Author(User author) {
            this.id = author.getId();
            this.name = author.getUsername();
        }
    }
}
