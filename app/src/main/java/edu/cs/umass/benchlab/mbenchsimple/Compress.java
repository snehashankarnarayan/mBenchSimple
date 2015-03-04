package edu.cs.umass.benchlab.mbenchsimple;

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
/**
 * Created by snehas on 2/28/15.
 */
import java.io.File;

public class Compress {

    private static final int BUFFER = 2048;

    private LinkedList<String> _files;
    private String _zipFile;

    private LinkedList<String> getAllFiles(File directory) {
        LinkedList<String> filenames = new LinkedList<String>();

        if(directory.isDirectory())
        {
            File[] fileList = directory.listFiles();
            for(File file : fileList) {
                if(file.isDirectory()) {
                   filenames.addAll(getAllFiles(file));
                }
                else if(file.isFile()) {
                    filenames.add(file.getAbsolutePath());
                }
            }
        }

        return filenames;
    }

    public void zipDirectory(String dirName, String zipFile)
    {
        _zipFile = zipFile;
        File directory = new File(dirName);
        _files = getAllFiles(directory);
        zip();

    }

    private void zip() {
        try  {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(_zipFile);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER];

            for(String f : _files) {
                Log.v("Compress", "Adding: " + f);
                FileInputStream fi = new FileInputStream(f);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(f.substring(f.lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
