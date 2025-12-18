import javax.swing.*;

public class Partyplaner_UI extends JFrame {
    private JPanel hauptPanel;
    private JLabel partyplanerLabel;
    private JLabel locationLabel;
    private JComboBox locationcomboBox;
    private JLabel musikDJLabel;
    private JRadioButton rnbHiphopRadioButton;
    private JRadioButton technoUndElectroRadioButton;
    private JRadioButton afroBeatsRadioButton;
    private JRadioButton rapRadioButton;
    private JLabel essenLabel;
    private JCheckBox essenJaCheckBox;
    private JCheckBox essenNeinCheckBox;
    private JComboBox essenComboBox;
    private JLabel personenanzahlJLabel;
    private JTextField PersonenanzahlTextField;
    private JLabel telNrJLabel;
    private JTextField telNrTextField;
    private JLabel dekorationJLabel;
    private JRadioButton blumenstilRadioButton;
    private JRadioButton landhausstilRadioButton;
    private JRadioButton gerlandeRadioButton;

    public Partyplaner_UI () {
        setTitle("Partyplaner");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(hauptPanel);
        setSize(1000, 500);
        setVisible(true);
        setResizable(false);
    }
    public static void main(String[] args) {
        new Partyplaner_UI();
    }
    private JTextField partyplaner;
    private JLabel partyplanerJLabel;

}
