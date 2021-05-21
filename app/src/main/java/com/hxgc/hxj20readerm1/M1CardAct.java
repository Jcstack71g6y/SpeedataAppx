package com.hxgc.hxj20readerm1;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.serialport.DeviceControl;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hxgc.hxdevicepackage.DataConversionUtils;
import com.hxgc.hxdevicepackage.hxgcJ20Reader;
import com.hxgc.hxdevicepackage.hxgcJ20M1Card;

import java.io.IOException;

public class M1CardAct extends Activity implements View.OnClickListener {

    private hxgcJ20Reader m_oJ20Reader = null;
    private hxgcJ20M1Card m_oJ20M1Card = null;
    private TextView tvShow;
    private Button btnExcu,btnExcu2;

    private DeviceControl deviceControl;//思必拓上电控制类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvShow = findViewById(R.id.tv_show);
        btnExcu = findViewById(R.id.btn_excu);
        btnExcu.setOnClickListener(this);

        btnExcu2 = findViewById(R.id.btn_excu);
        btnExcu2.setOnClickListener(this);
        m_oJ20Reader = new hxgcJ20Reader();
        m_oJ20M1Card = new hxgcJ20M1Card(m_oJ20Reader);

        m_oJ20Reader.openPort();

        //初始化上电 kt50 上电端口为94
        try {
            deviceControl=new DeviceControl(DeviceControl.PowerType.MAIN,94);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        m_oJ20Reader.closePort();
        try {
            deviceControl.PowerOffDevice();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            deviceControl.PowerOnDevice();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            deviceControl.PowerOffDevice();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    boolean bResult = true;

    byte[] bysCardType = new byte[1]; //卡类型 0x0A, 0x0B
    byte[] bysCardUid = new byte[4]; //卡UID

    byte[] bysKey = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    byte[] bysWrite = {0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x39};
    byte[] bysRead = new byte[16];

    byte[] bysSW = {0x00, 0x00};


    private byte shanqu = 0x01;
    private byte block = 0x01;

    private byte shanqu2 = 0x02;
    private byte block2 = 0x02;

    @Override
    public void onClick(View view) {
        if (view == btnExcu) {
            do {
                tvShow.setText("---激活卡片---\n");
                //
                //以下各函数说明请看hxgcJ20M1Card类的代码注释.
                //
                //激活卡片
                bResult = m_oJ20M1Card.ActiveCard(bysCardType, bysCardUid);
                if (!bResult) {
                    tvShow.append("---激活失败---\n");
                    break;
                }
                tvShow.append("---激活成功---\n");
//
//                //密码验证
                bResult = m_oJ20M1Card.AuthCard(shanqu, (byte) 0x60, bysKey, bysSW);
                if (!bResult) {
                    tvShow.append("---秘钥验证失败---\n");
                    break;
                }
                tvShow.append("---秘钥验证成功---\n");

                //写卡 - 每次写一块,16字节
                bResult = m_oJ20M1Card.WriteBlock(shanqu, block, bysWrite, bysSW);
                if (!bResult) {
                    tvShow.append("---写卡失败---\n" + DataConversionUtils.byteArrayToString(bysWrite));
                    break;
                }
                tvShow.append("---写卡成功---\n" + DataConversionUtils.byteArrayToString(bysWrite) + " \n");

                //读卡 - 每次读一块, 16字节
                bResult = m_oJ20M1Card.ReadBlock(shanqu, block, bysRead, bysSW);
                if (!bResult) {
                    tvShow.append("---读卡失败---\n");
                    break;
                }
                tvShow.append("---读卡成功---" + DataConversionUtils.byteArrayToString(bysRead));


                SystemClock.sleep(100);
                bResult = m_oJ20M1Card.ActiveCard(bysCardType, bysCardUid);
                if (!bResult) {
                    tvShow.append("---激活失败---\n");
                    break;
                }
                tvShow.append("---激活成功---\n");

                //密码验证
                bResult = m_oJ20M1Card.AuthCard(shanqu2, (byte) 0x60, bysKey, bysSW);
                if (!bResult) {
                    tvShow.append("---秘钥验证失败---\n");
                    break;
                }
                tvShow.append("---秘钥验证成功---\n");

                //写卡 - 每次写一块,16字节
                bResult = m_oJ20M1Card.WriteBlock(shanqu2, block2, bysWrite, bysSW);
                if (!bResult) {
                    tvShow.append("---写卡失败---\n" + DataConversionUtils.byteArrayToString(bysWrite));
                    break;
                }
                tvShow.append("---写卡成功---\n" + DataConversionUtils.byteArrayToString(bysWrite) + " \n");

                //读卡 - 每次读一块, 16字节
                bResult = m_oJ20M1Card.ReadBlock(shanqu2, block2, bysRead, bysSW);
                if (!bResult) {
                    tvShow.append("---读卡失败---\n");
                    break;
                }
                tvShow.append("---读卡成功---" + DataConversionUtils.byteArrayToString(bysRead));

            } while (false);
            return;
        }else{
            do {

                bResult = m_oJ20M1Card.ActiveCard(bysCardType, bysCardUid);
                if (!bResult) {
                    tvShow.append("---激活失败---\n");
                    break;
                }
                tvShow.append("---激活成功---\n");

                //密码验证
                bResult = m_oJ20M1Card.AuthCard(shanqu2, (byte) 0x60, bysKey, bysSW);
                if (!bResult) {
                    tvShow.append("---秘钥验证失败---\n");
                    break;
                }
                tvShow.append("---秘钥验证成功---\n");

                //写卡 - 每次写一块,16字节
                bResult = m_oJ20M1Card.WriteBlock(shanqu2, block2, bysWrite, bysSW);
                if (!bResult) {
                    tvShow.append("---写卡失败---\n" + DataConversionUtils.byteArrayToString(bysWrite));
                    break;
                }
                tvShow.append("---写卡成功---\n" + DataConversionUtils.byteArrayToString(bysWrite) + " \n");

                //读卡 - 每次读一块, 16字节
                bResult = m_oJ20M1Card.ReadBlock(shanqu2, block2, bysRead, bysSW);
                if (!bResult) {
                    tvShow.append("---读卡失败---\n");
                    break;
                }
                tvShow.append("---读卡成功---" + DataConversionUtils.byteArrayToString(bysRead));

            } while (false);
            return;
        }
    }
}
