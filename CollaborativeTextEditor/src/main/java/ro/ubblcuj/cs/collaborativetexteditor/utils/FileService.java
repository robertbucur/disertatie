package ro.ubblcuj.cs.collaborativetexteditor.utils;

import java.io.*;

public class FileService {

    // save uploaded file to a defined location on the server
    public static void saveFileToServer(InputStream uploadedInputStream,
                                        String serverLocation) {
        try {

            File file = new File(serverLocation);

            if (file.createNewFile()) {
                System.out.println("CTXEFile is created!");
            } else {
                System.out.println("CTXEFile already exists.");
            }

            OutputStream outpuStream;
            int read;
            byte[] bytes = new byte[1024];
            outpuStream = new FileOutputStream(file);
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                outpuStream.write(bytes, 0, read);
            }
            outpuStream.flush();
            outpuStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createNewFileToServer(String fileName) throws IOException {
        File file = new File(fileName);

        if (file.createNewFile()) {
            System.out.println("CTXEFile is created!");
        } else {
            System.out.println("CTXEFile already exists.");
        }
    }

    public static void deleteFileFromServer(String fileName) throws IOException {
        File file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }
    }
}
