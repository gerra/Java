package ru.ifmo.ctddev.berezhko.walk;

import java.io.*;

/**
 * Created by german on 17.02.15.
 */
public class Walk {
    private static int getFileHash(String fileName) {
        int fileHash = 0;
        Reader currentFileReader = null;
        try {
            currentFileReader = new InputStreamReader(new FileInputStream(fileName));
            int ch;
            fileHash = 0x811c9dc5;
            try {
                while ((ch = currentFileReader.read()) != -1) {
                    fileHash *= 0x01000193;
                    fileHash ^= ch;
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Something wrong with reading of " + fileName);
                return 0;
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            if (currentFileReader != null) {
                try {
                    currentFileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        }
        return fileHash;
    }

    private static String addLeadingZero(String s, int minLength) {
        String res = "";
        for (int i = 0; i < minLength - s.length(); i++) {
            res += "0";
        }
        return res + s;
    }

    public static void main(String args[]) {
        Reader reader = null;
        Writer writer = null;
        BufferedReader bufferedReader;
        try {
            reader = new InputStreamReader(new FileInputStream(args[0]), "UTF-8");
            writer = new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8");
            bufferedReader = new BufferedReader(reader);
            boolean running = true;
            while (running) {
                try {
                    String fileName = bufferedReader.readLine();
                    if (fileName == null) {
                        running = false;
                        break;
                    }
                    writer.write(addLeadingZero(Integer.toHexString(getFileHash(fileName)), 8) + " " + fileName + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnsupportedEncodingException e) {
            // only UTF-8
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
