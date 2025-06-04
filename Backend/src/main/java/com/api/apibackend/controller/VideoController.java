package com.api.apibackend.controller;

import com.api.apibackend.pojo.ApiResponse;
import com.api.apibackend.pojo.DTO.UploadVideoDTO;
import com.api.apibackend.pojo.User;
import com.api.apibackend.pojo.Video;
import com.api.apibackend.pojo.VideoView;
import com.api.apibackend.service.UserService;
import com.api.apibackend.service.VideoService;
import com.api.apibackend.service.VideoViewService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
public class VideoController {

    private final UserService userService;

    private final VideoService videoService;

    private final VideoViewService videoViewService;

    public static final String NGINX_BASE_PATH = "D:\\nginx\\nginx-1.26.2\\ICPRFiles\\";

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
        return ApiResponse.noMoreUnseenError();
    }

    @PostMapping(value = "/uploadVideoFile")
    public ResponseEntity<ApiResponse<Object>> uploadFile(@RequestBody MultipartFile video) {
        String name = video.getOriginalFilename();
        var f = new File(NGINX_BASE_PATH + name);
        try {
            if (!f.exists()) {
                if (!f.createNewFile()) {
                    throw new IOException();
                }
            }
            FileUtils.writeByteArrayToFile(f, video.getBytes());
        } catch (IOException e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        var url = "http://localhost:65/" + name;

        return ApiResponse.ok(200, url);
    }

    @PostMapping(value = "/video")
    public ResponseEntity<ApiResponse<Integer>> uploadVideo(@RequestBody UploadVideoDTO uploadVideoDTO) {
        Integer userId = uploadVideoDTO.getUserId();
        String title = uploadVideoDTO.getTitle();
        String description = uploadVideoDTO.getDescription();
        String videoUrl = uploadVideoDTO.getVideoUrl();

        // 验证用户是否存在
        User user = userService.getById(userId);
        if (user == null) {
            logger.warn("Login failed. User not found: {}", userId);

            return ApiResponse.userNotExistError(); // 用户不存在
        } else {
            Video video = new Video(
                        title,
                        description,
                        videoUrl,
                        userId,
                        0
                    );
            videoService.save(video);

            return ApiResponse.ok(200, video.getId());
        }

    }

}
