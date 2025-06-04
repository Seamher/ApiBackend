package com.api.apibackend.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListMyVideosDTO {

    private Integer userId;

    private Integer limit;

    private Integer offset;

}
