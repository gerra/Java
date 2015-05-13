import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by root on 06.05.15.
 */
public class CountLine {
    static int fileCount = 0;
    static int lineCount = 0;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java CountLine <path>");
            return;
        }

        Files.walk(Paths.get(args[0]))
            .filter(Files::isRegularFile)
            .forEach(filePath -> {
                File file = filePath.toFile();
                try {
                    if (!file.getCanonicalPath().endsWith(".java")) {
                        return;
                    }
//                    System.out.println(file.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileCount++;
                try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                    String read;
                    while (true) {
                        read = reader.readLine();
                        if (read == null) {
                            break;
                        }
                        if (read.length() > 0) {
                            lineCount++;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        System.out.println(String.format("File count = %d\nLine count = %d", fileCount, lineCount));
    }
}
