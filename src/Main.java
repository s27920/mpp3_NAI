import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        File[] trainingDir = new File("training").listFiles();
        File[] testingDir = new File("test").listFiles();
        Network network = new Network(new int[]{trainingDir.length, trainingDir.length-1, trainingDir.length-1, trainingDir.length});
        Parser parser = new Parser();
        LangFile[] trainingFiles = parser.parseNestedDir(trainingDir);
        LangFile[] testFiles = parser.parseNestedDir(testingDir);
        network.train(trainingFiles, testFiles);

        SwingUtilities.invokeLater( ()-> new NNGui(network, parser));
    }
}
