import java.util.Random;
public class Perceptron {

    private float[] inputs;
    private float[] weights;
    private Perceptron[] predecessors = null;
    private Perceptron[] successors = null;
    private int sucIndex;

    Perceptron(int inputsSize) {
        this.inputs = new float[inputsSize];
        this.weights = new float[inputsSize];
        Network.neuronCounter++;
        this.sucIndex = 0;
    }

    public void getOutsideInputs() {
        if (this.predecessors != null) {
            float[] outputs = new float[inputs.length];
            for (int i = 0; i < predecessors.length; i++) {
                outputs[i] = predecessors[i].getOutput();
            }
            this.inputs = outputs;
        }
    }
    float getOutput(){
        float sum = 0.0f;
        for (int i = 0; i < inputs.length; i++) {
            sum+=inputs[i] * weights[i];
        }
        return (float) (1 / (1 + Math.exp(-sum)));
    }

    public void twoWayLink(Perceptron[] predecessors) {
        this.setPredecessors(predecessors);
        for (int i = 0; i < predecessors.length; i++) {
            Network.connectionCounter++;
            predecessors[i].addSuccessor(this, predecessors[i].getSucIndex());
        }
    }

    public void addSuccessor(Perceptron perceptron, int index){
        this.successors[index] = perceptron;
        this.sucIndex++;
    }
    public void initWeights(){
        Random random = new Random();
        int inputNum = 1;
        int outputNum = weights.length;
        double stdDev = Math.sqrt(2.0 / (inputNum + outputNum));
        for (int i = 0; i < outputNum; i++) {
            weights[i] = (float) (random.nextGaussian() * stdDev);
        }
    }
    int getPredLength(){
        if(predecessors == null){
            return 1;
        }else {
            return predecessors.length;
        }
    }
    public int getSucIndex() {
        return sucIndex;
    }
    public float[] getInputs() {
        return inputs;
    }

    public float[] getWeights() {
        return weights;
    }

    public Perceptron[] getPredecessors() {
        return predecessors;
    }
    public Perceptron[] getSuccessors() {
        return successors;
    }
    public void setInputs(float[] inputs) {
        this.inputs = inputs;
    }
    public void setWeights(float[] weights) {
        this.weights = weights;
    }

    public void setPredecessors(Perceptron[] predecessors) {
        this.predecessors = predecessors;
    }

    public void setSuccessors(Perceptron[] successors) {
        this.successors = successors;
    }
}

