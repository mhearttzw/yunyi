package com.zheng.ucenter.util;


import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    /**
     * 将MultipartFile保存到指定的路径下
     * @param file      spring的MltipartFile对象
     * @param savePath  保存路径
     * @return          保存的文件名
     * @throws IOException
     */
    public static String save(byte[] file, String savePath, String fileName) throws IOException {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        if (file != null && file.length > 0) {
            File fileFolder = new File(savePath);
            if (!fileFolder.exists()) {
                fileFolder.mkdirs();
            }
            File saveFile = getFile(savePath, fileName);
            try {
                fos = new FileOutputStream(saveFile);
                bos = new BufferedOutputStream(fos);
                bos.write(file);
                bos.flush();
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return saveFile.getName();
        }
        return null;
    }

    /**
     * 删除文件
     * @param filePath
     *              文件路径
     * @return  是否删除成功
     */
    public static boolean delete(String filePath) {
        File file = new File(filePath);
        if (file.isFile()) {
            file.delete();
            return true;
        }
        return false;
    }

    /**
     * 生成文件名
     * @param savePath
     * @param originalFilename
     * @return
     */
    private static File getFile(String savePath, String originalFilename) {
        String fileName = System.currentTimeMillis() + "_" + originalFilename;
        File file = new File(savePath + "/" + fileName);
        if (file.exists()) {
            return getFile(savePath, originalFilename);
        }
        return file;
    }

}
