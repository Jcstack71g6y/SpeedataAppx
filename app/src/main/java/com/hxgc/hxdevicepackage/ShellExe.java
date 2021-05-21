
package com.hxgc.hxdevicepackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.io.InputStream;

import android.util.Log;

public class ShellExe 
{

    private static final String TAG = "EM/shellexe";
    public static final String ERROR = "ERROR";
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAIL = -1;
    public static final int RESULT_EXCEPTION = -2;
    private static StringBuilder sResultBuilder = new StringBuilder("");

    /**
     * Get shell command output
     * 
     * @return Shell command output
     */
    public static String getOutput() 
    {
        return sResultBuilder.toString();
    }

    /**
     * Execute shell command
     * @param command Command string need to execute
     * @return Result
     * @throws IOException Throws when occurs #IOException
     */
    public static int execCommand(String command) throws IOException 
    {
        return execCommand(new String[] { "sh", "-c", command });
    }

    /**
     * Execute shell command
     * @param command Shell command array
     * @return Result
     * @throws IOException Throws when occurs #IOException
     */
    public static int execCommand(String[] command) throws IOException 
    {
        int result = RESULT_FAIL;
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec(command);
        BufferedReader bufferedReader = null;
        sResultBuilder.delete(0, sResultBuilder.length());
        try 
        {
            bufferedReader = new BufferedReader(new InputStreamReader(proc .getInputStream(), Charset.defaultCharset()));
           
            if (proc.waitFor() == 0) 
            {
                String line = bufferedReader.readLine();
                if (line != null) 
                {
                    sResultBuilder.append(line);
                    while (true) 
                    {
                        line = bufferedReader.readLine();
                        if (line == null) 
                        {
                            break;
                        } 
                        else 
                        {
                            sResultBuilder.append('\n');
                            sResultBuilder.append(line);
                        }
                    }
                }
                result = RESULT_SUCCESS;
            } 
            else 
            {
                Log.i(TAG, "exit value = " + proc.exitValue());
                sResultBuilder.append(ERROR);
                result = RESULT_FAIL;
            }
        } 
        catch (InterruptedException e) 
        {
            Log.i(TAG, "exe shell command InterruptedException: "
                    + e.getMessage());
            sResultBuilder.append(ERROR);
            result = RESULT_EXCEPTION;
        } 
        finally 
        {
            if (null != bufferedReader) 
            {
                try 
                {
                    bufferedReader.close();
                } 
                catch (IOException e) 
                {
                    Log.w(TAG, "close reader in finally block exception: " + e.getMessage());
                }
            }
        }
        
        return result;
    }
    
    static class StreamGobbler extends Thread
    {
         InputStream is;
         String type;
        
         StreamGobbler(InputStream is, String type)
         {
             this.is = is;
             this.type = type;
         }
        
         public void run()
         {
             try
             {
                 InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader br = new BufferedReader(isr);
                 String line=null;
                 while ( (line = br.readLine()) != null)
                     System.out.println(type + ">" + line);   
                 } catch (IOException ioe)
                   {
                     ioe.printStackTrace(); 
                   }
         }
    }
}
