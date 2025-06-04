package com.api.apibackend.service.impl;

import com.api.apibackend.mapper.VideoMapper;
import com.api.apibackend.pojo.Video;
import com.api.apibackend.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {
}
