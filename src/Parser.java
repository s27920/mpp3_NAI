import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Parser {
    static public Map<Integer, String> languageMap;
    private int languageCounter;
    public Parser() {
        languageMap = new HashMap<>();
        languageCounter = 0;
    }
    public LangFile[] parseNestedDir(File[] tmpDir){
        LangFile[] files = new LangFile[0];
        for (File languageDir : tmpDir) {
            files = ArrayUtil.mergeArrays(files, parseDir(languageDir));
        }
        return files;
    }

    public LangFile[] parseDir(File tmpDir){
        File[] dir = tmpDir.listFiles();
        String name = tmpDir.getName();
        languageMap.put(languageCounter, name);
        languageCounter++;
        int dirSize = dir.length;
        LangFile[] toReturn = new LangFile[dirSize];
        for (int i = 0; i < dirSize; i++) {
            toReturn[i] = parseFile(dir[i].getPath(), name);
        }
        return toReturn;
    }

    private LangFile parseFile(String filePath, String language){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))){
            String fileContents = bufferedReader.lines().collect(Collectors.joining());
            return new LangFile(parseString(fileContents), language);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public float[] parseString(String fileContents){
        float[] letters = new float[27];
        fileContents = fileContents.toLowerCase();
        fileContents = fileContents.replaceAll("\\p{Punct}", "");
        fileContents = fileContents.replaceAll("\\d+", "");
        fileContents = fileContents.replaceAll("\\s+", "");
        char[] chars = fileContents.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            try{
                letters[chars[i]-97]++;
            }catch (ArrayIndexOutOfBoundsException ex){
                letters[26]++;
            }
        }
        return ArrayUtil.normalize(letters);
    }

}
