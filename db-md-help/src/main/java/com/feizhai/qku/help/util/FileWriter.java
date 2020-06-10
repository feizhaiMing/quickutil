package com.feizhai.qku.help.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

/**
 * @author wxm
 * @date 2020/6/10.
 */
public class FileWriter {
    private BufferedWriter bufferedWriter;
    public void init(String fileName){
        File file = new File(fileName);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            bufferedWriter = new BufferedWriter(new java.io.FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lineWriter(String content){
        try {
            bufferedWriter.write(content);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
