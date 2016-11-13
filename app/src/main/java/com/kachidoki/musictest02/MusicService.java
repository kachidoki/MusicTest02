package com.kachidoki.musictest02;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by mayiwei on 16/11/13.
 */
public class MusicService extends Service {

    private String[] musicDir = new String[]{
            Environment.getExternalStorageDirectory().getAbsolutePath()+"/张国荣当爱已成往事.mp3",
            Environment.getExternalStorageDirectory().getAbsolutePath()+"/张国荣千千阙歌.mp3",
            Environment.getExternalStorageDirectory().getAbsolutePath()+"/1.mp3",
            };
    private int musicIndex = 1;

    public final IBinder binder = new MyBinder();
    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public static MediaPlayer mp = new MediaPlayer();
    public MusicService() {
        super();
        Log.e("Test","-------Service构造--------");
        try {
            mp.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath()+"/1.mp3");
            mp.prepare();
            Log.e("Test","-------mediaIsPrepare-------");
            musicIndex = 1;
        } catch (Exception e) {
            Log.d("hint","can't get to the song");
            e.printStackTrace();
        }

    }


    public void playOrPause() {
        if(mp.isPlaying()){
            mp.pause();
        } else {
            mp.start();
        }
    }
    public void stop() {
        if(mp != null) {
            mp.stop();
            try {
                mp.prepare();
                mp.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void nextMusic() {
        if(mp != null && musicIndex < 3) {
            mp.stop();
            try {
                mp.reset();
                mp.setDataSource(musicDir[musicIndex+1]);
                musicIndex++;
                mp.prepare();
                mp.seekTo(0);
                mp.start();
            } catch (Exception e) {
                Log.d("hint", "can't jump next music");
                e.printStackTrace();
            }
        }
    }
    public void preMusic() {
        if(mp != null && musicIndex > 0) {
            mp.stop();
            try {
                mp.reset();
                mp.setDataSource(musicDir[musicIndex-1]);
                musicIndex--;
                mp.prepare();
                mp.seekTo(0);
                mp.start();
            } catch (Exception e) {
                Log.d("hint", "can't jump pre music");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Test","------ServiceOnCreate-----");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Test","------ServiceOnStartCommand-----");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mp.stop();
        mp.release();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
