package com.api.apibackend.pojo;

import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoView {

    @MppMultiId
    private Integer userId;

    @MppMultiId
    private Integer videoId;

}
