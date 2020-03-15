package com.wc;

import java.io.File;
import java.util.Map;
import java.util.Scanner;

public class WC {

    public static void main(String[] args) {
    	//界面介绍
        System.out.println("输入介绍：wc.exe [parameter] [file_name]");
        System.out.println("wc.exe -c file.c    返回文件 file.c 的字符数");
        System.out.println("wc.exe -w file.c    返回文件 file.c 的词的数目  ");
        System.out.println("wc.exe -l file.c    返回文件 file.c 的行数");
        System.out.println("-s   递归处理目录下符合条件的文件");
        System.out.println("-a   返回更复杂的数据（代码行 / 空行 / 注释行）");
        System.out.println("-x   显示图形界面");
        System.out.println("please input command");
        
    	Scanner scan = new Scanner(System.in);//表示从键盘输入
        
        while (true) {
            boolean sflag = false;
            String cmd = Util.getCmd(scan);//获取输入命令
            // 是否图形化界面
            if (cmd.equals("-x")) {
                Util.graphic();
                continue;
            }
            Map<String,String[]> paramAndFile=Util.converse(cmd);//存储参数数组和文件路径
            if(paramAndFile==null) {
            	System.out.println("unavailable input,please enter correctly");
            	continue;
            }
            // 获取命令参数数组
            String[] params=paramAndFile.get("parameters");
            for (int i = 0; i < params.length ; i++) {
                if (params[i].equals("-s")) sflag = true;
            }
            // 获取文件名
            String fileName = paramAndFile.get("fileName")[0];
            String path=fileName.substring(0,fileName.indexOf('*')-1);
            if (sflag == true) {
            	File file=new File(path);
            	if(!file.isDirectory()) {
            		System.out.println("path error");
            		continue;
            	}
            	Util.allFiles(fileName,params);//"-s"命令，计算所有文件
            } else {
                Map<String,Integer> result=Util.count(fileName);//计算字符数、行数等
                if(result==null) {
                	System.out.println("unavailable input,please enter correctly");
                	continue;
                }
                Util.print(false,params,result);//输出结果
            }
        }
    }

}