'use client';
import axios from 'axios';
import { Button } from 'primereact/button';

interface Props {
  userId: number;
  video: {
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
  };
}

export default function VideoPlayer({ userId, video }: Props) {
  const like = () => {
    axios.post(`${process.env.PUBLIC_URL}/like`, {
      userId,
      videoId: video.id,
    });
  };

  const unlike = () => {
    axios.delete(`${process.env.PUBLIC_URL}/like`, {
      data: { userId, videoId: video.id },
    });
  };


  return (
    <div style={{ marginBottom: '2rem' }}>
      <video src={process.env.NEXT_PUBLIC_NGINX_URL + video.url} controls width="500" />
      <h3>{video.title}</h3>
      <p>{video.description}</p>
      <p>作者：{video.author.name}(ID: {video.author.id})</p>
      <p>点赞数：{video.likes}</p>
      {video.liked ? (
        <Button label="取消点赞" onClick={unlike} />
      ) : (
        <Button label="点赞" onClick={like} />
      )}
    </div>
  );
}
