package com.wc;

import java.io.File;
import java.util.Map;
import java.util.Scanner;

public class WC {

    public static void main(String[] args) {
    	//�������
        System.out.println("������ܣ�wc.exe [parameter] [file_name]");
        System.out.println("wc.exe -c file.c    �����ļ� file.c ���ַ���");
        System.out.println("wc.exe -w file.c    �����ļ� file.c �Ĵʵ���Ŀ  ");
        System.out.println("wc.exe -l file.c    �����ļ� file.c ������");
        System.out.println("-s   �ݹ鴦��Ŀ¼�·����������ļ�");
        System.out.println("-a   ���ظ����ӵ����ݣ������� / ���� / ע���У�");
        System.out.println("-x   ��ʾͼ�ν���");
        System.out.println("please input command");
        
    	Scanner scan = new Scanner(System.in);//��ʾ�Ӽ�������
        
        while (true) {
            boolean sflag = false;
            String cmd = Util.getCmd(scan);//��ȡ��������
            // �Ƿ�ͼ�λ�����
            if (cmd.equals("-x")) {
                Util.graphic();
                continue;
            }
            Map<String,String[]> paramAndFile=Util.converse(cmd);//�洢����������ļ�·��
            if(paramAndFile==null) {
            	System.out.println("unavailable input,please enter correctly");
            	continue;
            }
            // ��ȡ�����������
            String[] params=paramAndFile.get("parameters");
            for (int i = 0; i < params.length ; i++) {
                if (params[i].equals("-s")) sflag = true;
            }
            // ��ȡ�ļ���
            String fileName = paramAndFile.get("fileName")[0];
            String path=fileName.substring(0,fileName.indexOf('*')-1);
            if (sflag == true) {
            	File file=new File(path);
            	if(!file.isDirectory()) {
            		System.out.println("path error");
            		continue;
            	}
            	Util.allFiles(fileName,params);//"-s"������������ļ�
            } else {
                Map<String,Integer> result=Util.count(fileName);//�����ַ�����������
                if(result==null) {
                	System.out.println("unavailable input,please enter correctly");
                	continue;
                }
                Util.print(false,params,result);//������
            }
        }
    }
}