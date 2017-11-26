package com.akura.utility;

import org.apache.commons.io.FileUtils;

import java.io.*;

/**
 * Class representing a CommandExecutor.
 */
public class CommandExecutor {

    /**
     * Method used to execute the file reading command.
     *
     * @param command - relevant command.
     * @return - string value.
     */
    public static String executeCommand(String command) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }

    /**
     * Method used to copy file using a stream.
     *
     * @param source - source.
     * @param dest   - destination.
     * @throws IOException - exception.
     */
    public static void copyFileUsingStream(File source, File dest) throws IOException {

        if (!dest.exists()) {
            dest.createNewFile();
        }
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    /**
     * Method used to copy file using apache commons IO.
     *
     * @param source - source.
     * @param dest   - destination.
     * @throws IOException - exception.
     */
    public static void copyFileUsingApacheCommonsIO(File source, File dest) throws IOException {
        FileUtils.copyFile(source, dest);
    }
}
