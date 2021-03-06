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
    private static Set<String> set=new HashSet<String>();//存放正确的参数集合
    
    //获取命令
    public static String getCmd(Scanner scan) {
    	String cmd="";
    	while(true) {
    		cmd=scan.nextLine();
            //输入命令判断
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
    
    //将命令转换成map,map中包含参数数组和文件名
    public static Map<String,String[]> converse(String cmd) {
    	set.add("-c");set.add("-w");set.add("-l");set.add("-s");set.add("-a");//初始化合法参数集合
    	Map<String,String[]> paramAndFile=new HashMap<String,String[]>(); //存储参数和文件地址
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
    
    //图形化界面
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
    
    //计算字符数、行数等
    public static Map<String,Integer> count(String fileName) {
    	Map<String,Integer> map=new HashMap<String,Integer>();
        int lineCount = 0;//行数
        int wordCount = 0;//单词数
        int charCount = 0;//字符数
        int nullLineCount = 0;// 空行数
        int codeLineCount = 0;// 代码行数
        int noteLineCount = 0;// 注释行数
        File file = new File(fileName);
        if (file.exists()) {
            try {
            	//文件流读取文件
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String line = "";
                StringBuffer sb = new StringBuffer();
                while ((line = br.readLine()) != null) {
                	lineCount++;
                    sb.append(line);
                    charCount += line.length();
                   //去掉首尾空格
                    line = line.trim();
                    // 空白行
                    if (line == "" || line.length() <= 1) {
                    	nullLineCount++;
                        continue;
                    }
                    // 注释行
                    int a = line.indexOf("/");
                    int b = line.substring(a + 1).indexOf("/");
                    if (b == 0) {
                    	noteLineCount++;
                        continue;
                    }
                    codeLineCount++;// 代码行
                }
                //如果文件为空
                if(sb.length()==0) wordCount=0;
                else wordCount = sb.toString().split("\\s+").length;//单词行
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
            map.put("lineCount", lineCount);
            map.put("wordCount", wordCount);
            map.put("charCount", charCount);
            map.put("nullLineCount", nullLineCount);
            map.put("codeLineCount", codeLineCount);
            map.put("noteLineCount", noteLineCount);
            return map;
        } else {
            System.out.println("path error");
            return null;
        }
    }
    
    //"-s"递归返回符合条件的文件
    public static void allFiles(String fileName,String[] param) {
        String path = fileName.substring(0, fileName.indexOf("*") - 1);
        String type = fileName.substring(fileName.indexOf("*") + 1, fileName.length());
        File dir = new File(path);
        if (dir.isDirectory()) {
            File next[] = dir.listFiles();
            for (int i = 0; i < next.length; i++) {
                if (!next[i].isDirectory()) {
                    if (next[i].getName().substring(next[i].getName().indexOf("."), next[i].getName().length())
                            .equals(type)) {
                        System.out.println(dir.getAbsolutePath()+"\\"+next[i].getName());
                        fileName = path + "\\" + next[i].getName();
                        Map<String,Integer> map=count(fileName);
                        print(false,param,map);
                    }
                }else {
                	String filePath=path+"\\"+next[i].getName()+"\\*"+type;
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
    
    //输出函数
    public static void print(boolean isGraphic,String[] param,Map<String,Integer> map) {
        if (isGraphic == true) {
            System.out.print("line count：");
            System.out.println(map.get("lineCount"));	
            System.out.print("word count：");
            System.out.println(map.get("wordCount"));	
            System.out.print("char count：");
            System.out.println(map.get("charCount"));
            System.out.print("blank lines count：");
            System.out.println(map.get("nullLineCount"));
            System.out.print("code lines count：");
            System.out.println(map.get("codeLineCount"));
            System.out.print("note lines count：");
            System.out.println(map.get("noteLineCount"));
        } else {
            for (int i = 0; i < param.length; i++) {
                if (param[i].equals("-l")) {
                    System.out.print("line count：");
                    System.out.println(map.get("lineCount"));
                } else if (param[i].equals("-w")) {
                    System.out.print("word count：");
                    System.out.println(map.get("wordCount"));
                } else if (param[i].equals("-c")) {
                    System.out.print("char count：");
                    System.out.println(map.get("charCount"));
                } else if (param[i].equals("-a")) {
                    System.out.print("blank lines count：");
                    System.out.println(map.get("nullLineCount"));
                    System.out.print("code lines count：");
                    System.out.println(map.get("codeLineCount"));
                    System.out.print("note lines count：");
                    System.out.println(map.get("noteLineCount"));
                }
            }
        }
        System.out.println("");
    }
}