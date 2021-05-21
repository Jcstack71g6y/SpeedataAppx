package com.hxgc.hxj20readerm1;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.hxgc.hxdevicepackage.CpuCardApi;


public class CpuCardAct extends Activity implements OnClickListener {
	private Button btnBack, btnSure, btnClearWindow, btnClearOrder, btnVersion,
			btnSafecode, btnNonOn, btnNonRandom;
	private EditText m_RespView;
	private EditText m_CmdEdit;
	//输入法管理器
	protected InputMethodManager m_imm = null;
	//API等
	private CpuCardApi cpuCardApi;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.function_activity);

		//输入法管理
		m_imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		btnBack = (Button) findViewById(R.id.back_btn);
		btnSure = (Button) findViewById(R.id.sure_btn);
		btnClearWindow = (Button) findViewById(R.id.clearwindow_btn);
		btnClearOrder = (Button) findViewById(R.id.clearorder_btn);
		btnVersion = (Button) findViewById(R.id.version_btn);
		btnSafecode = (Button) findViewById(R.id.safecode_btn);
		btnNonOn = (Button) findViewById(R.id.noncard_onoroff_btn);
		btnNonRandom = (Button) findViewById(R.id.noncard_random_btn);
		
		m_RespView = (EditText) findViewById(R.id.displaywindow_et);
		m_CmdEdit = (EditText) findViewById(R.id.input_et);

		btnBack.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		btnClearOrder.setOnClickListener(this);
		btnClearWindow.setOnClickListener(this);
		btnVersion.setOnClickListener(this);
		btnSafecode.setOnClickListener(this);
		btnNonOn.setOnClickListener(this);
		btnNonRandom.setOnClickListener(this);
		
		//手动输入的指令,以取非接卡随机数为例
		m_CmdEdit.setText("0084000008");

		Editable ea = m_CmdEdit.getText();

		m_CmdEdit.setSelection(ea.length());

		cpuCardApi = new CpuCardApi();
		
		//打开串口,GPIO上电
		cpuCardApi.initDev();
		//隐藏软键盘
		hideSoftInpu();

		///////////////////////////////////////////////////
		//让软键盘不挡住EIDT. 编辑时，将会把把EDIT以上部分整体上移,  而不是压缩紧靠EDIT上方紧邻的那个控件.
		//若是压缩上方控件，则可能上方控件高度不足，无足够压缩空间导致EDIT被挡住.
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		//非接卡软上电 (没有软上电)
		//cpuCardApi.nCPoweron();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 退出读卡线程
		cpuCardApi.releaseDev();

	}

	@Override
	public void onClick(View v) {

		if (v == btnBack) {// 返回
			// 退出读卡线程
			cpuCardApi.releaseDev();
			finish();
		} else if (v == btnSure) {// 发送指令



			String str=m_CmdEdit.getText().toString();
			int q=cpuCardApi.execfasongCmd(str);
			
			if(q==-1){
				m_RespView.append("输入有问题\n");
			}else if(q==0){
				m_RespView.append(cpuCardApi.str);
			}

			
		} else if (v == btnClearOrder) {// 清空指令框
			m_CmdEdit.setText(null);
		} else if (v == btnClearWindow) {// 清空显示窗口
			m_RespView.setText(null);
		} else if (v == btnVersion) {// 版本
			m_RespView.append("1.0\n");

		} else if (v == btnSafecode) {// 安全模块号

			cpuCardApi.readSamID();
			m_RespView.append(cpuCardApi.str2);


		} else if (v == btnNonOn) {// 非接卡寻卡

			//非接卡寻卡
			cpuCardApi.nCSearch();
			m_RespView.append(cpuCardApi.str);

		}

		else if (v == btnNonRandom) {// 非接卡随机数

			cpuCardApi.random();
			m_RespView.append(cpuCardApi.str);

		}
	}


	//隐藏软键盘
	protected void hideSoftInpu()
	{
		if(null != m_imm)
		{
			boolean isOpen = m_imm.isActive();
			if(isOpen)
			{
				m_imm.hideSoftInputFromWindow(m_CmdEdit.getWindowToken(), 0);
			}
		}
	}

}
