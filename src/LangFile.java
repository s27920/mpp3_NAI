public class LangFile {
    private float[] letters;
    private String label;

    public LangFile(float[] letters, String label) {
        this.letters = letters;
        this.label = label;
    }

    public float[] getLetters() {
        return letters;
    }

    public void setLetters(float[] letters) {
        this.letters = letters;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
