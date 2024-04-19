import java.math.BigDecimal;
import java.math.RoundingMode;

public class Network{
    static public int neuronCounter = 0;
    private int genCounter = 1;
    static public int connectionCounter = 0;
    private Perceptron[] initLayer;
    public Perceptron[] terminalLayer;
    private double targetAccuracy = 0.96f;
    private final float learningRate = 0.2f;

    public Network(int[] structure) {
        if (structure.length > 1){
            targetAccuracy= 0.99999f;
        }
        Perceptron[][] layers = createNetwork(27, structure);
        this.initLayer = layers[0];
        this.terminalLayer = layers[1];
    }

    public void train(LangFile[] trainingFiles, LangFile[] testFiles){
            for (LangFile file : trainingFiles) {
                getActivations(file.getLetters());
                backProp(file);
            }
        getAccuracy(trainingFiles, testFiles);
    }

    public String translateActivation(float[] activations){
        return Parser.languageMap.get(ArrayUtil.findMaxIndex(activations));
    }
    public void getAccuracy(LangFile[] trainingFiles, LangFile[] testFiles){
        int correct = 0;
        int total = 0;
        for (LangFile file : testFiles) {
            float[] activations = getActivations(file.getLetters());
            int neuronIndex = ArrayUtil.findMaxIndex(activations);
            correct+=getCorrect(neuronIndex, file);
            total++;
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(((correct * 1.0) / total) * 100);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        System.out.println("Gen: " + genCounter++ + " accuracy: " + bigDecimal + " %");
        if (((correct*1.0)/total) < targetAccuracy){
            train(trainingFiles, testFiles);
        }
    }

    private int getCorrect(int neuronIndex, LangFile current){
        if(Parser.languageMap.get(neuronIndex).equals(current.getLabel())){
            return 1;
        }
        return 0;
    }

    public float[] getActivations(float[] letters){
        for (int i = 0; i < initLayer.length; i++) {
            initLayer[i].setInputs(letters);
        }
        Perceptron[] currLayer = initLayer;
        for(;currLayer.length > 0; currLayer = currLayer[0].getSuccessors()){
            for (Perceptron current : currLayer) {
                current.getOutsideInputs();
            }
            if (currLayer[0].getSuccessors() == null) {
                break;
            }
        }
        int currLength = currLayer.length;
        float []activations = new float[currLength];
        for (int i = 0; i < currLength; i++) {
            activations[i] = currLayer[i].getOutput();
        }
        return activations;
    }
    public void backProp(LangFile curr){
        Perceptron[] currLayer = terminalLayer;
        float[] sucErrorGradient = new float[currLayer.length];
        for (int i = 0; i < currLayer.length; i++) {
            float[] weights = currLayer[i].getWeights();
            float[] inputs = currLayer[i].getInputs();
            float output = currLayer[i].getOutput();
            float error = (getCorrect(i, curr) - output);
            float errorGradient = (error * ((1-output)*output));

            for (int j = 0; j < weights.length; j++) {
                weights[j] += learningRate * errorGradient * inputs[j];
            }
            sucErrorGradient[i] = errorGradient;
        }
        if (currLayer.length > 0) {
            currLayer = currLayer[0].getPredecessors();
            while (currLayer != null) {
                Perceptron[] sucLayer = currLayer[0].getSuccessors();
                float[] tmpErrorGradient = new float[currLayer.length];
                for (int i = 0; i < currLayer.length; i++) {
                    float errorGradient = 0.0f;
                    for (int j = 0; j < sucLayer.length; j++) {
                        float[] weights = sucLayer[j].getWeights();
                        for (int k = 0; k < weights.length; k++) {
                            if (i == k) {
                                errorGradient += sucErrorGradient[j] * weights[k];
                            }
                        }
                    }
                    float[] inputs = currLayer[i].getInputs();
                    float[] weights = currLayer[i].getWeights();
                    float output = currLayer[i].getOutput();
                    for (int j = 0; j < weights.length; j++) {
                        weights[j] += learningRate * ((1 - output) * output) * errorGradient * inputs[j];
                    }
                    tmpErrorGradient[i] = errorGradient;
                }
                sucErrorGradient = tmpErrorGradient;
                currLayer = currLayer[0].getPredecessors();
            }
        }
    }


    private Perceptron[][] createNetwork(int inputSize, int[] structure){
        int initLayerSize = structure[0];
        Perceptron[] initLayer = new Perceptron[initLayerSize];
        for (int i = 0; i < initLayerSize; i++) {
            initLayer[i] = new Perceptron(inputSize);
            if (i + 1 < structure.length){
                initLayer[i].setSuccessors(new Perceptron[structure[1]]);
            }
            initLayer[i].initWeights();
        }
        if (structure.length == 1){
            return new Perceptron[][]{initLayer, initLayer};
        }
        Perceptron[] terminalLayer = hiddenLayers(structure, initLayer);
        System.out.println("network created");
        System.out.println("Number of neurons: " + neuronCounter);
        if (connectionCounter > 0) {
            System.out.println("Number of connections: " + connectionCounter);
        }
        return new Perceptron[][]{initLayer, terminalLayer};
    }
    private Perceptron[] hiddenLayers(int[] structure, Perceptron[] previousLayer){
        Perceptron[] currLayer = new Perceptron[0];
        for (int i = 1; i < structure.length; i++) {
            currLayer = new Perceptron[structure[i]];
            for (int j = 0; j < structure[i]; j++) {
                Perceptron perceptron = new Perceptron(structure[i-1]);
                perceptron.setPredecessors(new Perceptron[structure[i-1]]);
                if (i+1 < structure.length){
                    perceptron.setSuccessors(new Perceptron[structure[i+1]]);
                }
                perceptron.twoWayLink(previousLayer);
                perceptron.initWeights();
                currLayer[j] = perceptron;
            }
            previousLayer = currLayer;
        }

        return currLayer;
    }
}