package com.api.apibackend.service.impl;

import com.api.apibackend.mapper.VideoLikeMapper;
import com.api.apibackend.pojo.VideoLike;
import com.api.apibackend.service.VideoLikeService;
import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class VideoLikeServiceImpl extends MppServiceImpl<VideoLikeMapper, VideoLike> implements VideoLikeService {
}
