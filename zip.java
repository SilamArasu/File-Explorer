
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FilenameUtils;

public class zip {
    public static List<String> fileList = new ArrayList<>();

    public static void compress(String pat, String zipFile) {
        File f = new File(pat);
      byte[] buf = new byte[1024];
        try {
            fileList.clear();
            FileOutputStream fos  = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
           if(f.isDirectory()){
               getFileList(f);
            for (String filePath : fileList) {
                System.out.println("Compressing: " + filePath);
                String name = filePath.substring(f.getAbsolutePath().length() + 1,filePath.length());
                ZipEntry zipEntry = new ZipEntry(name);
                zos.putNextEntry(zipEntry);
                FileInputStream fis = new FileInputStream(filePath);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
           }
          else
           {
             ZipEntry zipEntry = new ZipEntry(f.getName());
                        zos.putNextEntry(zipEntry);
                        FileInputStream fileInputStream = new FileInputStream(f.getAbsolutePath());
int bytesRead;
                        while ((bytesRead = fileInputStream.read(buf)) > 0) {
                            zos.write(buf, 0, bytesRead);

                        }
            }
          zos.close();
            fos.close();
           
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void getFileList(File f) {
        File[] files = f.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(file.getAbsolutePath());
                } else {
                    getFileList(file);
                }
            }
        }

    }
      public static void unzip(String zipFilePath) throws IOException {
        String destDirectory=new File(zipFilePath).getParent()+"\\"+FilenameUtils.getBaseName(new File(zipFilePath).getAbsolutePath());
        System.out.println("abs"+new File(zipFilePath).getAbsolutePath());
         System.out.println("dest"+destDirectory);
         System.out.println("zip"+zipFilePath);
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));

        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
            new File(filePath).getParentFile().mkdirs();
            extractFile(zipIn, filePath);
            System.out.println(filePath);
        } else {
            File dir = new File(filePath);
            System.out.println(filePath);
            dir.mkdirs();
        }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }
   public static final int BUFFER_SIZE = 4096;  
   public static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

}