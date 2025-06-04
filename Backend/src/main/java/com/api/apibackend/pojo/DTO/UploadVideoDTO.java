package com.api.apibackend.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadVideoDTO {

    private Integer userId;

    private String title;

    private String description;

    private String videoUrl;

}
