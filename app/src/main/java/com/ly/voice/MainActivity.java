package com.ly.voice;

import android.app.Activity;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView tvStart, tvStop, tvResult ;
    private SpeechRecognizer mIat ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化即创建语音配置对象，只有初始化后才可以使用MSC的各项服务
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=573d5b32");

        //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        mIat= SpeechRecognizer.createRecognizer(this, null);
        //2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");

        tvStart = (TextView) findViewById(R.id.tv_start);
        tvStop = (TextView) findViewById(R.id.tv_stop);
        tvResult = (TextView) findViewById(R.id.tv_result);

        tvStart.setOnClickListener(this);
        tvStop.setOnClickListener(this);



        }
    //听写监听器
    private RecognizerListener mRecoListener = new RecognizerListener(){
        //听写结果回调接口(返回Json格式结果，用户可参见附录12.1)；
        //一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
        //关于解析Json的代码可参见MscDemo中JsonParser类；
        //isLast等于true时会话结束。
        public void onResult(RecognizerResult results, boolean isLast) {
            tvResult.setText(results.getResultString());
            Log.d("Result:", results.getResultString());
        }

        //会话发生错误回调接口
        public void onError(SpeechError error) {
            error.getPlainDescription(true) ;//获取错误码描述
        }
        //开始录音
        // 音量值0~30
        @Override
        public void onVolumeChanged(int volume, byte[] bytes) {

        }

        public void onBeginOfSpeech() {
            Toast.makeText(MainActivity.this, "begin", Toast.LENGTH_SHORT).show();
        }

        //结束录音
        public void onEndOfSpeech() {
            Toast.makeText(MainActivity.this, "stop", Toast.LENGTH_SHORT).show();
        }
        //扩展用接口
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {}
    };

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_start:
                mIat.startListening(mRecoListener);
                break;

            case R.id.tv_stop:
                mIat.stopListening();
                break;

            default:
                break;
        }
    }
}
