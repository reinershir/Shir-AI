package io.github.reinershir.boot.core.easygenerator.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DailyFileUtil {

    // 遍历目录(递归
    public static String folderMethod2(String path) {
        File file = new File (path);

        String fileNames = "";
        if (file.exists ( )) {
            File[] files = file.listFiles ( );
            if (null != files) {
                for (File file2 : files) {
                    if (file2.isDirectory ( )) {
                        System.out.println ("文件夹:" + file2.getAbsolutePath ( ));
                        folderMethod2 (file2.getAbsolutePath ( ));
                    } else {
                        fileNames += file2.getName ( ) + ",";
                        System.out.println ("文件:" + file2.getAbsolutePath ( ));
                    }
                }
            }
        } else {
            System.out.println ("文件不存在!");
        }
        return fileNames;
    }

    public static void fileToZip(String filePath, ZipOutputStream zipOut) throws IOException {
        // 需要压缩的文件
        File file = new File (filePath);
        // 获取文件名称,如果有特殊命名需求,可以将参数列表拓展,传fileName
        String fileName = file.getName ( );
        FileInputStream fileInput = new FileInputStream (filePath);
        // 缓冲
        byte[] bufferArea = new byte[1024 * 10];
        BufferedInputStream bufferStream = new BufferedInputStream (fileInput, 1024 * 10);
        // 将当前文件作为一个zip实体写入压缩流,fileName代表压缩文件中的文件名称
        zipOut.putNextEntry (new ZipEntry (fileName));
        int length = 0;
        // 最常规IO操作,不必紧张
        while ((length = bufferStream.read (bufferArea, 0, 1024 * 10)) != -1) {
            zipOut.write (bufferArea, 0, length);
        }
        //关闭流
        fileInput.close ( );
        // 需要注意的是缓冲流必须要关闭流,否则输出无效
        bufferStream.close ( );
        // 压缩流不必关闭,使用完后再关
    }


    /**
     * 文件压缩
     *
     * @param srcFile 目录或者单个文件
     * @param zipFile 压缩后的ZIP文件
     */
    public static void doCompress(File srcFile, File zipFile) throws IOException {
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream (new FileOutputStream (zipFile));
            doCompress (srcFile, out);
        } catch (Exception e) {
            throw e;
        } finally {
            out.close ( );//记得关闭资源
        }
    }

    public static void doCompress(String filelName, ZipOutputStream out) throws IOException {
        doCompress (new File (filelName), out);
    }

    public static void doCompress(File file, ZipOutputStream out) throws IOException {
        doCompress (file, out, "");
    }

    public static void doCompress(File inFile, ZipOutputStream out, String dir) throws IOException {
        if (inFile.isDirectory ( )) {
            File[] files = inFile.listFiles ( );
            if (files != null && files.length > 0) {
                for (File file : files) {
                    String name = inFile.getName ( );
                    if (!"".equals (dir)) {
                        name = dir + "/" + name;
                    }
                    DailyFileUtil.doCompress (file, out, name);
                }
            }
        } else {
            DailyFileUtil.doZip (inFile, out, dir);
        }
    }

    public static void doZip(File inFile, ZipOutputStream out, String dir) throws IOException {
        String entryName = null;
        if (!"".equals (dir)) {
            entryName = dir + "/" + inFile.getName ( );
        } else {
            entryName = inFile.getName ( );
        }
        ZipEntry entry = new ZipEntry (entryName);
        out.putNextEntry (entry);

        int len = 0;
        byte[] buffer = new byte[1024];
        FileInputStream fis = new FileInputStream (inFile);
        while ((len = fis.read (buffer)) > 0) {
            out.write (buffer, 0, len);
            out.flush ( );
        }
        out.closeEntry ( );
        fis.close ( );
    }

    // 工具api(重载复用)
    public static boolean createFileOrDir(String path) {
        return createFileOrDir (new File (path));
    }

    // 没有文件则创建
    private static boolean createFileOrDir(File file) {
        if (file.isDirectory ( )) {
            return file.mkdirs ( );
        }
        File parentFile = file.getParentFile ( );
        if (!parentFile.exists ( )) {
            System.out.println (parentFile.getPath ( ));
            boolean mkdirs = parentFile.mkdirs ( );
            if (!mkdirs)
                return false;
        } else {
            if (!parentFile.isDirectory ( )) {
                boolean delete = parentFile.delete ( );
                boolean mkdirs = parentFile.mkdirs ( );
                if (!delete || !mkdirs) return false;
            }
        }
        try {
            return file.createNewFile ( );
        } catch (IOException e) {
            e.printStackTrace ( );
        }
        return false;
    }
}
