package com.wc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFileChooser;

public class Util {
    private static JFileChooser chooser;
    private static Set<String> set=new HashSet<String>();//�����ȷ�Ĳ�������
    
    //��ȡ����
    public static String getCmd(Scanner scan) {
    	String cmd="";
    	while(true) {
    		cmd=scan.nextLine();
            //���������ж�
            if(cmd.length()<8 ||!cmd.substring(0, 6).equals("wc.exe")) {
            	System.out.println("unavailable input,please enter correctly");
            	continue;
            }else {
            	cmd=cmd.substring(7);
            	break;
            }
    	}
    	return cmd;
    }
    
    //������ת����map,map�а�������������ļ���
    public static Map<String,String[]> converse(String cmd) {
    	set.add("-c");set.add("-w");set.add("-l");set.add("-s");set.add("-a");
    	Map<String,String[]> paramAndFile=new HashMap<String,String[]>();
    	String[] paramWithPath=cmd.split(" ");
    	String[] param=new String[paramWithPath.length-1];
    	for(int i=0;i<paramWithPath.length;i++) {
    		if(i==paramWithPath.length-1) {
    			paramAndFile.put("fileName",new String[] {paramWithPath[i]});
    		}else {
    			if(!set.contains(paramWithPath[i])) return null;
    			else param[i]=paramWithPath[i];
    		}
    	}
    	paramAndFile.put("parameters",param);
    	return paramAndFile;
    }
    
    //ͼ�λ�����
    public static void graphic() {
        chooser = new JFileChooser();
        int value = chooser.showOpenDialog(null);
        if (value == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String filename = file.getAbsolutePath();
            Map<String,Integer> map=count(filename);
            print(true,null,map);
        }
    }
    
    //�����ַ�����������
    public static Map<String,Integer> count(String fileName) {
    	Map<String,Integer> map=new HashMap<String,Integer>();
        int linecount = 0;//����
        int wordcount = 0;//������
        int charcount = 0;//�ַ���
        int nullLinecount = 0;// ������
        int codeLinecount = 0;// ��������
        int noteLinecount = 0;// ע������
        File file = new File(fileName);
        if (file.exists()) {
            try {
            	//�ļ�����ȡ�ļ�
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String line = "";
                StringBuffer sb = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    linecount++;
                    sb.append(line);
                    charcount += line.length();
                   //ȥ����β�ո�
                    line = line.trim();
                    // �հ���
                    if (line == "" || line.length() <= 1) {
                        nullLinecount++;
                        continue;
                    }
                    // ע����
                    int a = line.indexOf("/");
                    int b = line.substring(a + 1).indexOf("/");
                    if (b == 0) {
                        noteLinecount++;
                        continue;
                    }
                    // ������
                    codeLinecount++;
                }
                wordcount = sb.toString().split("\\s+").length;//������
                br.close();
                isr.close();
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            map.put("linecount", linecount);
            map.put("wordcount", wordcount);
            map.put("charcount", charcount);
            map.put("nullLinecount", nullLinecount);
            map.put("codeLinecount", codeLinecount);
            map.put("noteLinecount", noteLinecount);
            return map;
        } else {
            System.out.println("path error");
            return null;
        }
    }
    
    //"-s"�ݹ鷵�ط����������ļ�
    public static void allFiles(String fileName,String[] param) {
        String path = fileName.substring(0, fileName.indexOf("*") - 1);
        String type = fileName.substring(fileName.indexOf("*") + 1, fileName.length());
//        System.out.print("Path:");
//        System.out.println(path + "\n");
        File dir = new File(path);
        if (dir.isDirectory()) {
            File next[] = dir.listFiles();
            for (int i = 0; i < next.length; i++) {
                if (!next[i].isDirectory()) {
                    // System.out.println(next[i].getName());
                    if (next[i].getName().substring(next[i].getName().indexOf("."), next[i].getName().length())
                            .equals(type)) {
                        System.out.println(dir.getAbsolutePath()+"\\"+next[i].getName());
                        fileName = path + "\\" + next[i].getName();
                        Map<String,Integer> map=count(fileName);
                        print(false,param,map);
                    }
                }else {
                	String filePath=path+"\\"+next[i].getName()+"\\*"+type;
                	//System.out.println(filePath);
                	allFiles(filePath,param);
                }
            }
        } else {
            if (dir.getName().substring(dir.getName().indexOf("."), dir.getName().length())
                    .equals(type)) {
                fileName = path + "\\" + dir.getName();
                Map<String,Integer> map=count(fileName);
                System.out.println(fileName);
                print(false,param,map);
            }
        }
    }
    
    //�������
    public static void print(boolean isGraphic,String[] param,Map<String,Integer> map) {
        if (isGraphic == true) {
            System.out.print("line count��");
            System.out.println(map.get("linecount"));	
            System.out.print("word count��");
            System.out.println(map.get("wordcount"));	
            System.out.print("char count��");
            System.out.println(map.get("charcount"));
            System.out.print("blank lines count��");
            System.out.println(map.get("nullLinecount"));
            System.out.print("code lines count��");
            System.out.println(map.get("codeLinecount"));
            System.out.print("note lines count��");
            System.out.println(map.get("noteLinecount"));
        } else {
            for (int i = 0; i < param.length; i++) {
                if (param[i].equals("-l")) {
                    System.out.print("line count��");
                    System.out.println(map.get("linecount"));
                } else if (param[i].equals("-w")) {
                    System.out.print("word count��");
                    System.out.println(map.get("wordcount"));
                } else if (param[i].equals("-c")) {
                    System.out.print("char count��");
                    System.out.println(map.get("charcount"));
                } else if (param[i].equals("-a")) {
                    System.out.print("blank lines count��");
                    System.out.println(map.get("nullLinecount"));
                    System.out.print("code lines count��");
                    System.out.println(map.get("codeLinecount"));
                    System.out.print("note lines count��");
                    System.out.println(map.get("noteLinecount"));
                }
            }
        }
        System.out.println("");
    }
}