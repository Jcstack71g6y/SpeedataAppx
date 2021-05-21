/*
 * Copyright 2009 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hxgc.hxj20readerm1;

import java.io.File;
import java.io.IOException;

import android_serialport_api.SerialPort;

public class Application extends android.app.Application
{
	private SerialPort mSerialPort = null;

	//打开串口
	public SerialPort openSerialPort(String _i_str_path)
	{
		if (mSerialPort == null)
		{
			//String path = "/dev/ttyMT1";
			String path = _i_str_path;
			int baudrate = 115200;

			try
			{
				mSerialPort = new SerialPort(new File(path), baudrate, 0);
			}
			catch (SecurityException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return mSerialPort;
	}

	//关闭串口
	public void closeSerialPort()
	{
		if (mSerialPort != null)
		{
			mSerialPort.close();
			mSerialPort = null;
		}
	}
}
