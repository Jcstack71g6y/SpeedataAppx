package com.hxgc.hxj20readerm1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MenuActivity extends Activity implements OnClickListener {
    private Button btnID, btnFunction, btnM1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        btnID = (Button) findViewById(R.id.id_btn);
        btnFunction = (Button) findViewById(R.id.many_btn);

        btnID.setOnClickListener(this);
        btnFunction.setOnClickListener(this);

        btnM1 = findViewById(R.id.m1_btn);
        btnM1.setOnClickListener(this);

        creatrFile();

    }

    private void creatrFile() {

        copyfile("/sdcard/wltlib", "base.dat", R.raw.base);
        copyfile("/sdcard/wltlib", "license.lic", R.raw.license);

    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == btnID) {
            Intent intent = new Intent(MenuActivity.this, IdentityAct.class);
            startActivity(intent);

        } else if (v == btnFunction) {
            Intent intent = new Intent(MenuActivity.this, CpuCardAct.class);
            startActivity(intent);
        } else if (v==btnM1) {

            Intent intent = new Intent(MenuActivity.this, M1CardAct.class);
            startActivity(intent);
        }


    }


    private void copyfile(String fileDirPath, String fileName, int id) {
        String filePath = fileDirPath + "/" + fileName;// 文件路径
        try {

            File files = new File("/sdcard/wltlib");

            if (!files.exists()) {
                files.mkdirs();

            }

            // 文件夹存在，则将apk中raw文件夹中的须要的文档拷贝到该文件夹下
            File file = new File(filePath);
            if (!file.exists()) {// 文件不存在
                InputStream is = getResources().openRawResource(
                        id);// 通过raw得到数据资源
                FileOutputStream fs = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int count = 0;// 循环写出
                while ((count = is.read(buffer)) > 0) {
                    fs.write(buffer, 0, count);
                }
                fs.close();// 关闭流
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
