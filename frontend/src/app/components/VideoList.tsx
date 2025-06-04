'use client';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { Button } from 'primereact/button';

interface Video {
    id: number;
    title: string;
    description: string;
    likes: number;
    author: {
        id: number;
        name: string;
    };
    url: string;
    liked: boolean;
}

export default function VideoList({ userId }: { userId: number }) {
    const [videos, setVideos] = useState<Video[]>([]);

    const fetchVideos = async () => {
        const res = await axios.get(`${process.env.PUBLIC_URL}/listMyVideos`, {
            params: {
                userId,
                limit: 10,
                offset: 1,
            }
        });
        console.log(res.data.data);
        if (res.data.status === 200 || res.data.status === 3) {
            setVideos(res.data.data);
        } else {
            console.error('获取失败', res.data);
        }
    };

    const viewDetail = async (videoId: number) => {
        const res = await axios.get(`${process.env.PUBLIC_URL}/video`, {
            params: { userId, id: videoId },
        });
        alert(`视频详情: ${JSON.stringify(res.data)}`);
    };

    const deleteVideo = async (videoId: number) => {
        await axios.delete(`${process.env.PUBLIC_URL}/video`, {
            data: { userId, id: videoId },
        });
        fetchVideos(); // 刷新
    };

    useEffect(() => {
        fetchVideos();
    }, []);

    return (
        <div>
            <h3>我的视频</h3>
            {videos.map((v) => (
                <div key={v.id} style={{ marginBottom: '1rem' }}>
                    <video src={process.env.NEXT_PUBLIC_NGINX_URL + v.url} controls width="400" />
                    <p>{v.title}</p>
                    <Button label="查看详情" onClick={() => viewDetail(v.id)} />
                    <Button label="删除" onClick={() => deleteVideo(v.id)} severity="danger" />
                </div>
            ))}
        </div>
    );
}