'use client';
import { useState } from 'react';
import axios from 'axios';
import { InputText } from 'primereact/inputtext';
import { Password } from 'primereact/password';
import { Button } from 'primereact/button';
import { Message } from 'primereact/message';
import { useRouter } from 'next/navigation';

export default function AuthPage() {
  const [isLogin, setIsLogin] = useState(true);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const router = useRouter();

  const handleLogin = async () => {
    setError('');
    try {
      const res = await axios.post(`${process.env.PUBLIC_URL}/login`, {
        username,
        password,
      });

      if (res.data.status === 200) {
        const { token, userId } = res.data.data;
        localStorage.setItem('token', token);
        localStorage.setItem('userId', userId.toString());
        router.push('/'); // 登录成功跳转首页
      } else {
        setError('登录失败，请检查用户名或密码');
      }
    } catch (erro) {
      setError('登录失败，请检查网络连接');
    }
  };

  const handleRegister = async () => {
    setError('');
    if (password !== confirmPassword) {
      setError('两次密码输入不一致');
      return;
    }

    try {
      const res = await axios.post(`${process.env.PUBLIC_URL}/user`, {
        username,
        password,
      });

      if (res.data.status === 200) {
        setIsLogin(true); // 注册成功，切回登录
      } else if (res.data.status === 422) {
        setError(res.data.error?.msg || '注册失败');
      } else {
        setError('注册失败');
      }
    } catch (erro) {
      setError('注册失败，请检查网络连接');
    }
  };

  return (
    <div style={{ maxWidth: '400px', margin: 'auto', marginTop: '5rem' }}>
      <h2>{isLogin ? '登录' : '注册'}</h2>
      <div className="p-fluid">
        <label>用户名</label>
        <InputText value={username} onChange={(e) => setUsername(e.target.value)} />

        <label style={{ marginTop: '1rem' }}>密码</label>
        <Password value={password} onChange={(e) => setPassword(e.target.value)} toggleMask />

        {!isLogin && (
          <>
            <label style={{ marginTop: '1rem' }}>确认密码</label>
            <Password value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} toggleMask />
          </>
        )}

        {error && (
          <Message severity="error" text={error} style={{ marginTop: '1rem' }} />
        )}

        <Button
          label={isLogin ? '登录' : '注册'}
          onClick={isLogin ? handleLogin : handleRegister}
          style={{ marginTop: '2rem' }}
        />

        <Button
          label={isLogin ? '没有账号？去注册' : '已有账号？去登录'}
          link
          onClick={() => {
            setIsLogin(!isLogin);
            setError('');
            setUsername('');
            setPassword('');
            setConfirmPassword('');
          }}
          style={{ marginTop: '1rem' }}
        />
      </div>
    </div>
  );
}
