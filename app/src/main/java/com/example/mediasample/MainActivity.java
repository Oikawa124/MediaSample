package com.example.mediasample;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.io.IOException;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    // フィールド

    //　メディアプレイヤー
    private MediaPlayer _player;

    // 再生・一時停止ボタンフィールド
    private Button _btPlay;

    // 戻るボタン
    private Button _btBack;

    // 進むボタン
    private Button _btFoward;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // フィールドの各ボタンを取得

        _btPlay = findViewById(R.id.btplay);
        _btBack = findViewById(R.id.btBack);
        _btFoward = findViewById(R.id.btForward);

        // メディアプレイヤーオブジェクトを生成

        _player = new MediaPlayer();

        // 音声ファイルのURL文字列を生成
        String mediaFileUrlStr = "android.resource://" + getPackageName()
                + "/" + R.raw.test;

        // URLをもとにURLオブジェクトを生成
        Uri mediaFileUriStr = Uri.parse(mediaFileUrlStr);

        try {
            // メディアプレイヤーに音声ファイルを指定
            _player.setDataSource(MainActivity.this, mediaFileUriStr);

            // 非同期でのメディア再生準備が完了した際のリスナの設定
            _player.setOnPreparedListener(new PlayerPreparedLisener());

            // メディア再生が終了した際のリスナの設定
            _player.setOnCompletionListener(new PlayerCompletionListener());

            // 非同期でメディア再生を準備
            _player.prepareAsync();
        }catch (IOException e){
            e.printStackTrace();
        }

        Switch loopSwitch = findViewById(R.id.swLoop);

        // スイッチにリスナを設定
        loopSwitch.setOnCheckedChangeListener(new LoopSwitchChangedListener());

    }

    //プレイヤーの再生準備が整ったときのリスナクラス。
    private class PlayerPreparedLisener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            //各ボタンをタップ可能に
            _btBack.setEnabled(true);
            _btPlay.setEnabled(true);
            _btFoward.setEnabled(true);
        }

    }


    //再生が終了したときのリスナクラス
    private class PlayerCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            //再生ボタンラベルを再生に設定
            _btPlay.setText(R.string.bt_play_play);

            if (!_player.isLooping()) {
                _btPlay.setText(R.string.bt_play_play);
            }
        }
    }

    // ボタンの設定
    public void onPlayButtonClick(View view) {

        // プレイヤーが再生中だったら
        if (_player.isPlaying()) {
            //プレイヤーを一時停止
            _player.pause();
            // 再生ボタンのラベルを再生に設定
            _btPlay.setText(R.string.bt_play_play);

        } else { // プレイヤーが再生中でなかったら
            // プレイヤーを再生
            _player.start();
            // 再生ボタンのラベルを一時停止に設定
             _btPlay.setText(R.string.bt_play_pause);
        }
    }

    public void onBackButtonClick(View view) {
        // 再生位置を先頭に変更
        _player.seekTo(0);
    }

    public void onForwardButtonClick(View view) {
        // 現在再生中のメディアファイルの長さを取得
        int duaration = _player.getDuration();

        _player.seekTo(duaration);

        if (!_player.isPlaying()) {
            _player.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (_player.isPlaying()) {
            _player.pause();
        }

        // プレイヤーを解放
        _player.release();

        // プレイヤー用フィールドをnullに
        _player = null;
    }


    private class LoopSwitchChangedListener implements CompoundButton.OnCheckedChangeListener {


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            _player.setLooping(isChecked);
        }
    }
}
