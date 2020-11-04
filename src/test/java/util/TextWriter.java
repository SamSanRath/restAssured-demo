package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextWriter {

    public static void write(String text, String filePath){
        BufferedWriter bufferedWriter = null;
        Path path = Paths.get(filePath);

        try {
            Files.createDirectories(path.getParent());
            File file = new File(filePath);
            file.createNewFile();
            bufferedWriter = new BufferedWriter(new FileWriter(filePath));
            bufferedWriter.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bufferedWriter != null){
                try {
                    bufferedWriter.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
