'use client';
import { useEffect, useState } from 'react';
import axios from 'axios';
import { ProgressSpinner } from 'primereact/progressspinner';
import VideoPlayer from './components/VideoPlayer';
import { useRouter } from 'next/navigation';
import { Button } from 'primereact/button';

interface Video {
  id: number;
  title: string;
  description: string;
  likes: number;
  url: string;
  liked: boolean;
  author: {
    id: number;
    name: string;
  };
}

export default function HomePage() {
  const userId = Number(localStorage.getItem('userId'));
  const token = localStorage.getItem('token') || '';
  const router = useRouter();

  const [videos, setVideos] = useState<Video[]>([]);
  const [index, setIndex] = useState(0);
  const [loading, setLoading] = useState(false);

  const fetchNewVideo = async () => {
    setLoading(true);
    try {
      const res = await axios.get(`${process.env.PUBLIC_URL}/recommendVideo`, {
        params: { userId },
        headers: { token },
      });

      if (res.data.status === 200 && res.data.data) {
        const newVideo: Video = res.data.data;
        setVideos((prev) => [...prev, newVideo]);
        setIndex(videos.length); // 指向新视频
      } else {
        console.warn('推荐视频返回格式异常：', res.data);
      }
    } catch (err) {
      console.error('推荐视频获取失败：', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    // 初次加载一条
    fetchNewVideo();
  }, []);

  const handleNext = () => {
    if (index < videos.length - 1) {
      setIndex(index + 1);
    } else {
      fetchNewVideo();
    }
  };

  const handlePrev = () => {
    if (index > 0) {
      setIndex(index - 1);
    }
  };

  const currentVideo = videos[index];

  return (
    <div style={{ height: '100vh', overflowY: 'auto', padding: '1rem' }}>
      <Button
        label="进入个人主页"
        onClick={() => router.push('/profile')}
        style={{ marginBottom: '1rem' }}
      />

      <h2>推荐视频</h2>

      {currentVideo ? (
        <VideoPlayer userId={userId} video={currentVideo} />
      ) : loading ? (
        <div style={{ textAlign: 'center', marginTop: '2rem' }}>
          <ProgressSpinner />
        </div>
      ) : (
        <p>暂无视频</p>
      )}

      <div style={{ display: 'flex', gap: '1rem', marginTop: '2rem' }}>
        <Button
          label="上一条"
          onClick={handlePrev}
          disabled={index === 0}
        />
        <Button
          label="下一条"
          onClick={handleNext}
          disabled={loading}
        />
      </div>
    </div>
  );
}
