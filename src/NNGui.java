import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NNGui extends JFrame {
    private JButton guessButton;
    private JTextArea input;
    private JLabel jLabel;
    private JPanel MainPanel;
    private JButton ClearButton;

    public NNGui(Network network, Parser parser) {
        {
            guessButton.addActionListener(e -> {
                if (e.getSource() == guessButton && (input.getText() != null || !input.getText().equals(""))) {
                    float[] inputs = parser.parseString(input.getText());
                    float[] activations = network.getActivations(inputs);
                    jLabel.setText("Język: " + network.translateActivation(activations));
                }
            });
            ClearButton.addActionListener(e -> {
                if (e.getSource() == ClearButton) {
                    input.setText("");
                }
            });
            this.setSize(new Dimension(900, 450));
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setLocationRelativeTo(null);
            this.add(MainPanel);
            this.setVisible(true);
        }
    }
}
