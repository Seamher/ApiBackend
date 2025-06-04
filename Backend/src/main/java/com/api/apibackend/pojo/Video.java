package com.api.apibackend.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video {

    @TableId
    private Integer id;

    private String title;

    private String description;

    private String url;

    private Integer likes;

    private Integer userId;

    public Video(String title, String description, String url, Integer userId, Integer likes) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.likes = likes;
        this.userId = userId;
    }

}
