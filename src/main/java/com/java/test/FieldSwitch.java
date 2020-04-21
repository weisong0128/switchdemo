package com.java.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 将中文字段转换为对应英文字段
 * @author: ws
 * @time: 2020/4/16 17:10
 */
public class FieldSwitch {
    private static final Logger logger = LoggerFactory.getLogger(FieldSwitch.class);

    public static void main(String[] args) {
        //中英文对照文档路径
        String originPath = System.getProperty("user.dir") + File.separator + "origin.txt";

        Map<String, Object> originMap = headByEnglish(originPath);
        headConvert(args, originMap);

    }
    private static Map<String, Object> headByEnglish(String str) {
        File file = new File(str);
//        System.out.println(System.getProperty("user.dir") + "/origin.txt");
        Map<String,Object> originMap = new HashMap<>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            long startTime = System.currentTimeMillis();
            while((line=reader.readLine())!=null){
                String[] strArray = null;
                strArray = line.split(",");
                originMap.get(strArray[0].toString());
                originMap.put(strArray[0].toString(),strArray[1].toString());
            }
//            System.out.println(originMap.size());
//            System.out.println(originMap.get("有无咳嗽、胸闷等不适应症状"));
        } catch (IOException e) {
            logger.error("找不到origin.txt文件",e);
//            logger.error(e.getMessage(), e);
            //程序不再往下执行
            System.exit(1);
        }finally{
            try {
                if(null!=reader) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error("reader对象为null",e);
            }
        }
        return originMap;
    }

    private static void headConvert(String[] args, Map<String, Object> originMap) {
        File inFile = new File(args[0]);
        File outFile = new File(args[1]);
        BufferedReader br = null;
        FileWriter writer = null;

        try {
            br = new BufferedReader(new FileReader(inFile));
            writer = new FileWriter(outFile);
            StringBuilder sb = new StringBuilder();
            String lineStr = null;
            while((lineStr=br.readLine())!=null){
                String[] strList = null;

                strList = lineStr.replaceAll("\t",",").split(",");
                for (int i = 0; i < strList.length; i++) {
                    if (originMap.containsKey(strList[i])){
                        sb.append(originMap.get(strList[i]) + ",");
                    }
                }
                String substring = sb.substring(0, sb.toString().length()-1);
                writer.write(substring);
                writer.flush();
                logger.info("{}文件治理完成", args[0]);
//                logger.info(String.format("%s文件治理完成", args[0]));
            }
        } catch (Exception e) {
            logger.error("输入文件异常",e);
        } finally {
            try {
                if(null!=writer) {
                    writer.close();
                }
            } catch (IOException e) {
                logger.error("writer对象为null",e);
            }
            try {
                if(null!=br) {
                    br.close();
                }
            } catch (IOException e) {
                logger.error("br对象为null",e);
            }
        }
    }

}
