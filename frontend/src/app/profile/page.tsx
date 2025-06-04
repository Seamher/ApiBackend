'use client';

import VideoList from '../components/VideoList';

export default function ProfilePage() {
  const userId = Number(localStorage.getItem('userId'));

  return (
    <div>
      <h2>个人视频</h2>
      <VideoList userId={userId} />
    </div>
  );
}
