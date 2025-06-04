package com.api.apibackend.controller;

import com.api.apibackend.pojo.ApiResponse;
import com.api.apibackend.pojo.User;
import com.api.apibackend.pojo.Video;
import com.api.apibackend.pojo.VideoView;
import com.api.apibackend.service.UserService;
import com.api.apibackend.service.VideoService;
import com.api.apibackend.service.VideoViewService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class VideoController {

    private final UserService userService;

    private final VideoService videoService;

    private final VideoViewService videoViewService;

    public VideoController(UserService userService, VideoService videoService, VideoViewService videoViewService) {
        this.userService = userService;
        this.videoService = videoService;
        this.videoViewService = videoViewService;
    }

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * 获取视频信息
     *
     * @param id 视频 ID 编号
     * @param userId 用户ID
     * @return 获取视频信息结果
     */
    @GetMapping("/video")
    public ResponseEntity<ApiResponse<Object>> getVideo(@RequestParam int id, @RequestParam int userId) {
        User user = userService.getById(userId);
        Video video = videoService.getById(id);
        if (video == null) {
            return ApiResponse.videoNotExistError();
        }
        if(user == null){
            return ApiResponse.userNotExistError();
        }
        return ApiResponse.ok(200, video);
    }


    /**
     * 推荐视频
     *
     * @param userId 用户ID
     * @return 推荐视频结果
     */
    @GetMapping("//recommendVideo")
    public ResponseEntity<ApiResponse<Object>> getVideo(@RequestParam int userId) {
        List<Video> videos = videoService.list();
        //遍历全部视频
        for(Video video : videos){
            QueryWrapper<VideoView> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("video_id", video.getId()).eq("user_id",userId);
            VideoView videoView = videoViewService.getOne(queryWrapper);
            if(videoView == null){
                //当前用户没看过该视频
                return ApiResponse.ok(200, video);
            }
        }
    }


}
