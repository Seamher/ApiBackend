package com.api.apibackend.service.impl;

import com.api.apibackend.mapper.VideoViewMapper;
import com.api.apibackend.pojo.VideoView;
import com.api.apibackend.service.VideoViewService;
import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class VideoViewServiceImpl extends MppServiceImpl<VideoViewMapper, VideoView> implements VideoViewService {
}
