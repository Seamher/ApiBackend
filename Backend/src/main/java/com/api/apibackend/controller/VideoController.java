package com.api.apibackend.controller;

import com.api.apibackend.pojo.*;
import com.api.apibackend.pojo.DTO.UserVideoDTO;
import com.api.apibackend.pojo.DTO.VideoInfoDTO;
import com.api.apibackend.service.UserService;
import com.api.apibackend.service.VideoLikeService;
import com.api.apibackend.service.VideoService;
import com.api.apibackend.service.VideoViewService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping
public class VideoController {

    private final UserService userService;

    private final VideoService videoService;

    private final VideoViewService videoViewService;

    private final VideoLikeService videoLikeService;

    public VideoController(UserService userService, VideoService videoService, VideoViewService videoViewService, VideoLikeService videoLikeService) {
        this.userService = userService;
        this.videoService = videoService;
        this.videoViewService = videoViewService;
        this.videoLikeService =videoLikeService;
    }


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
        if (user == null) {
            return ApiResponse.userNotExistError();
        }
        // 获取视频作者
        QueryWrapper<User> queryWrapperUser = new QueryWrapper<>();
        queryWrapperUser.eq("id", video.getUserId());
        User author = userService.getOne(queryWrapperUser);
        // 获取是否被点赞
        QueryWrapper<VideoLike> queryWrapperVideoLike = new QueryWrapper<>();
        queryWrapperVideoLike.eq("user_id", author.getId()).eq("video_id", video.getId());
        VideoLike videoLike = videoLikeService.getOne(queryWrapperVideoLike);

        VideoInfoDTO videoInfoDTO;
        if(videoLike == null){
            videoInfoDTO = new VideoInfoDTO(video, author, false);
        }else{
            videoInfoDTO = new VideoInfoDTO(video, author, true);
        }

        return ApiResponse.ok(200, videoInfoDTO);
    }


    /**
     * 推荐视频
     *
     * @param userId 用户ID
     * @return 推荐视频结果
     */
    @GetMapping("/recommendVideo")
    public ResponseEntity<ApiResponse<Object>> recommendVideo(@RequestParam int userId) {
        //用户不存在
        User user = userService.getById(userId);
        if (user == null){
            return ApiResponse.userNotExistError();
        }
        List<Video> videos = videoService.list();
        //按点赞数降序排序
        List<Video> sortedVideosDesc = videos.stream()
                .sorted(Comparator.comparingInt(Video::getLikes).reversed())
                .toList();
        //遍历全部视频
        for(Video video : sortedVideosDesc){
            QueryWrapper<VideoView> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("video_id", video.getId()).eq("user_id",userId);
            VideoView videoView = videoViewService.getOne(queryWrapper);
            if(videoView == null){
                // 当前用户没看过该视频
                videoViewService.save(new VideoView(userId,video.getId()));
                // 获取视频作者
                QueryWrapper<User> queryWrapperUser = new QueryWrapper<>();
                queryWrapperUser.eq("id", video.getUserId());
                User author = userService.getOne(queryWrapperUser);
                // 获取是否被点赞
                QueryWrapper<VideoLike> queryWrapperVideoLike = new QueryWrapper<>();
                queryWrapperVideoLike.eq("user_id", author.getId()).eq("video_id", video.getId());
                VideoLike videoLike = videoLikeService.getOne(queryWrapperVideoLike);

                VideoInfoDTO videoInfoDTO;
                if(videoLike == null){
                    videoInfoDTO = new VideoInfoDTO(video, author, false);
                }else{
                    videoInfoDTO = new VideoInfoDTO(video, author, true);
                }

                return ApiResponse.ok(200, videoInfoDTO);
            }
        }
        // 没有更多视频
        return ApiResponse.noMoreUnseenError();
    }

    /**
     * 点赞视频
     *
     * @param userVideoDTO 点赞信息
     * @return 点赞视频结果
     */
    @PostMapping("/like")
    public ResponseEntity<ApiResponse<Object>> like(@RequestBody UserVideoDTO userVideoDTO) {
        int userId = userVideoDTO.getUserId();
        int videoId = userVideoDTO.getVideoId();
        //用户不存在
        User user = userService.getById(userId);
        if (user == null){
            return ApiResponse.userNotExistError();
        }
        //视频不存在
        Video video = videoService.getById(videoId);
        if (video == null) {
            return ApiResponse.videoNotExistError();
        }
        //重复点赞
        QueryWrapper<VideoLike> queryWrapperVideoLike = new QueryWrapper<>();
        queryWrapperVideoLike.eq("user_id", userId).eq("video_id", videoId);
        VideoLike videoLike = videoLikeService.getOne(queryWrapperVideoLike);
        if(videoLike != null){
            return ApiResponse.likeLikedError();
        }
        videoLikeService.save(new VideoLike(userId,videoId));
        return ApiResponse.ok(200, null);
    }

    /**
     * 删除视频
     *
     * @param videoId 视频id
     * @param userId 用户id
     * @return 删除视频结果
     */
    @DeleteMapping("/video")
    public ResponseEntity<ApiResponse<Object>> videoDelete(@RequestParam int videoId, @RequestParam int userId) {
        //用户不存在
        User user = userService.getById(userId);
        if (user == null){
            return ApiResponse.userNotExistError();
        }
        //视频不存在
        Video video = videoService.getById(videoId);
        if (video == null) {
            return ApiResponse.videoNotExistError();
        }
        //没有权限
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", videoId).eq("user_id", userId);
        Video videoCheck = videoService.getOne(queryWrapper);
        if(videoCheck == null){
            return ApiResponse.noDeletePermissionError();
        }

        videoService.remove(queryWrapper);
        return ApiResponse.ok(200, null);
    }

    /**
     * 取消点赞视频
     *
     * @param videoId 视频id
     * @param userId 用户id
     * @return 取消点赞视频结果
     */
    @DeleteMapping("/like")
    public ResponseEntity<ApiResponse<Object>> likeDelete(@RequestParam int userId, @RequestParam int videoId) {
        //用户不存在
        User user = userService.getById(userId);
        if (user == null){
            return ApiResponse.userNotExistError();
        }
        //视频不存在
        Video video = videoService.getById(videoId);
        if (video == null) {
            return ApiResponse.videoNotExistError();
        }
        //取消未点赞视频
        QueryWrapper<VideoLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id", videoId).eq("user_id", userId);
        VideoLike videoLike = videoLikeService.getOne(queryWrapper);
        if(videoLike == null){
            return ApiResponse.unlikeUnlikedError();
        }

        videoLikeService.remove(queryWrapper);
        return ApiResponse.ok(200, null);
    }
}
