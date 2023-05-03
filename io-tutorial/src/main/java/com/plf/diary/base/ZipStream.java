package com.plf.diary.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author panlf
 * @date 2023/5/3
 */
public class ZipStream {

    public static void main(String[] args) throws Exception {
        unzip(new File("D:\\TempData\\file\\1.zip"),new File("D:\\TempData\\file\\zip"));
    }

    public static void unzip(File src, File dest) throws Exception {
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(src));

        ZipEntry zipEntry;

        while ((zipEntry = zipInputStream.getNextEntry()) != null){

            if(zipEntry.isDirectory()){
                File file = new File(dest,zipEntry.toString());
                file.mkdirs();
            }else {
                FileOutputStream fileOutputStream =
                        new FileOutputStream(new File(dest,zipEntry.toString()));

                int b;
                while ((b=zipInputStream.read()) != -1){
                    fileOutputStream.write(b);
                }
                fileOutputStream.close();
                zipInputStream.closeEntry();
            }
        }
        zipInputStream.close();
    }

    public static void zip(File src, File dest) throws Exception{
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(new File(dest,"a.zip")));

        ZipEntry entry = new ZipEntry("a.txt");

        zipOutputStream.putNextEntry(entry);

        FileInputStream fileInputStream = new FileInputStream(src);

        int b;

        while ((b=fileInputStream.read()) != -1){
            zipOutputStream.write(b);
        }

        zipOutputStream.close();

        zipOutputStream.close();

    }

    public static void zip(File src, ZipOutputStream zipOutputStream,String name) throws Exception{
        File[] files = src.listFiles();
        if(files == null){
            return;
        }
        for(File file:files){
            if(file.isFile()){
                ZipEntry zipEntry = new ZipEntry(name+"\\"+file.getName());

                zipOutputStream.putNextEntry(zipEntry);

                FileInputStream fileInputStream = new FileInputStream(file);

                int b;

                while ((b = fileInputStream.read()) != -1){
                    zipOutputStream.write(b);
                }

                fileInputStream.close();
                zipOutputStream.closeEntry();
            } else {
                zip(file,zipOutputStream,name+"\\"+file.getName());
            }
        }

    }

}
