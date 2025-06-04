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

}
