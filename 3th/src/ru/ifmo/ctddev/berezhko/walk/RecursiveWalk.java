package ru.ifmo.ctddev.berezhko.walk;

import java.io.*;

public class RecursiveWalk {
    private interface FileReadListener {
        public void onGetFile(File file);
    }

    private static int getFileHash(File file) {
        if (!file.exists()) {
            System.err.println("File or directory \"" + file.getPath() + "\" from input file not found");
            return 0;
        }
        int fileHash = 0;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            try {
                int ch;
                fileHash = 0x811c9dc5;
                while ((ch = fileInputStream.read()) != -1) {
                    fileHash *= 0x01000193;
                    fileHash ^= ch;
                }
            } catch (IOException e) {
                System.err.println("Something wrong with reading of " + file.getAbsolutePath());
                return 0;
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            return 0;
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        }
        return fileHash;
    }

    private static void recursiveWalk(File fileOrDirectory, FileReadListener listener) {
        if (fileOrDirectory.isDirectory()) {
            File[] files = fileOrDirectory.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    recursiveWalk(file, listener);
                } else {
                    listener.onGetFile(file);
                }
            }
        } else {
            listener.onGetFile(fileOrDirectory);
        }
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
                    String dirOrFileName = bufferedReader.readLine();
                    if (dirOrFileName == null) {
                        running = false;
                        break;
                    }
                    final File file = new File(dirOrFileName);

                    final Writer finalWriter = writer;
                    recursiveWalk(file, new FileReadListener() {
                        @Override
                        public void onGetFile(File file) {
                            try {
                                finalWriter.write(String.format("%08x", getFileHash(file)) + " " + file.getPath() + "\n");
                            } catch (IOException e) {
                            }
                        }
                    });
                } catch (IOException e) {
                }
            }
        } catch (UnsupportedEncodingException e) {
            // only UTF-8
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
