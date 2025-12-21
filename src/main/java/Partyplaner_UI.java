import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
        private ButtonGroup musikGruppe;
        private JLabel essenLabel;
        private JCheckBox essenJaCheckBox;
        private JCheckBox essenNeinCheckBox;
        private JComboBox essenComboBox;
        private JLabel personenanzahlLabel;
    private JTextField PersonenanzahlTextField;
    private JComboBox qmCombobox;
    private JTextField kostenTextField;
    private JLabel kostenLabel;
        private JButton berechneButton;
        private JButton bestellungausfuehrenButton;
        // ArrayList für Objekte aus Klasse "Party"
    private ArrayList<Party> party;

    public Partyplaner_UI() {
        setTitle("Partyplaner");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(hauptPanel);
        setSize(1000, 500);
        setVisible(true);
        setResizable(false);

        // Array initialisieren
        party = new ArrayList<>();

        // EssenComboBox zu Beginn deaktivieren
        essenComboBox.setEnabled(false);

        //ButtonGroup für Musik-RadioButtons: damit nur ein DJ ausgewählt werden kann
        musikGruppe = new ButtonGroup();
        musikGruppe.add(rnbHiphopRadioButton);
        musikGruppe.add(technoUndElectroRadioButton);
        musikGruppe.add(afroBeatsRadioButton);
        musikGruppe.add(rapRadioButton);

        //Action Listener: Essen JA
        essenJaCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // "Nein" automatisch abwählen
                essenNeinCheckBox.setSelected(false);
                // ComboBox aktivieren
                essenComboBox.setEnabled(true);
                // Erste Auswahl automatisch setzen (z. B. "offenes Buffet")
                essenComboBox.setSelectedIndex(0);
            }
        });
        //Action Listener: ESSEN NEIN
        essenNeinCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // "Ja" automatisch abwählen
                essenJaCheckBox.setSelected(false);
                // ComboBox deaktivieren
                essenComboBox.setEnabled(false);
                // Auswahl entfernen
                essenComboBox.setSelectedIndex(-1);
            }
        });
    }
    // Methoden

    //Objekte erstellen

    //Main Methode
    public static void main(String[] args) {
        new Partyplaner_UI();
    }
}