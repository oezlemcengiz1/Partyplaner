import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

    public class Partyplaner_UI extends JFrame {
        private JPanel hauptPanel;
        private JLabel partyplanerLabel;
        private JLabel locationLabel;
        private JComboBox locationComboBox;
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
        private JButton kostenBerechneButton;
        private JButton bestellungausfuehrenButton;

        // ArrayList für Objekte aus Klasse "Party"
    private ArrayList<Party> party;

    //Bestellung erst erlaubt, wenn Kosten berechnet wurden
    private boolean kostenBerechnet = false;

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
        //Action Listener: Kosten berechnen
        kostenBerechneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                berechneKosten();
            }
        });
        //Action Listener: Bestellung ausführen
        bestellungausfuehrenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bestellungAusfuehren();
                setVisible(true);
            }
        });
    }

    // Methode: prüfen ob Musik gewählt wurde
    private boolean istMusikGewaehlt() {
        return rnbHiphopRadioButton.isSelected()
                || technoUndElectroRadioButton.isSelected()
                || afroBeatsRadioButton.isSelected()
                || rapRadioButton.isSelected();
        }

    // Gesamtkosten berechnen
    private void berechneKosten() {
        try {
            //Personenanzahl
            int personen = Integer.parseInt(PersonenanzahlTextField.getText().trim());
            //Personenanzahl darg nicht 0 odr negativ sein
            if (personen <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Personenanzahl muss größer als 0 sein!",
                        "Fehler", JOptionPane.ERROR_MESSAGE);
                kostenBerechnet = false;
                return;
            }

            // getSelectedItem liefert OBjekt, brauchen aber String dafür deshalb -> (String)
            String location = (String) locationComboBox.getSelectedItem();
            String qm = (String) qmCombobox.getSelectedItem();

            //Location kosten berechnen:
            double locationKosten = 0;

            //Grundpreis je nach Location
            if ("Innenbereich".equals(location)) locationKosten = 150;
            else if ("Außenbereich".equals(location)) locationKosten = 200;
            else if ("Außenbereich mit Pool".equals(location)) locationKosten = 300;
            else if ("Innen- und Außenbereich".equals(location)) locationKosten = 350;

            //+ qm
            if ("90 - 120 qm".equals(qm)) {
                locationKosten += 100;
            } else if ("40 - 80 qm".equals(qm)) {
                locationKosten += 0;
            }

            //DJ-Kosten berechnen:
            double djKosten = 0;

            if (rnbHiphopRadioButton.isSelected()) djKosten = 200;
            else if (technoUndElectroRadioButton.isSelected()) djKosten = 250;
            else if (afroBeatsRadioButton.isSelected()) djKosten = 230;
            else if (rapRadioButton.isSelected()) djKosten = 190;

            //Essenkosten berechnen(nur wenn "Ja":
            double essenKosten = 0;
            if (essenJaCheckBox.isSelected()) {
                String essenArt = (String) essenComboBox.getSelectedItem();

                if ("finger food".equals(essenArt)) essenKosten = 5 * personen;
                else if ("offenes Buffet".equals(essenArt)) essenKosten = 10 * personen;
                else if ("3-gänge Menü".equals(essenArt)) essenKosten = 15 * personen;
                else if ("5-gänge Menü".equals(essenArt)) essenKosten = 20 * personen;
            }

            //Gesamtkosten berechnen
            double gesamtKosten = locationKosten + djKosten + essenKosten;

            // Ausgabe formatieren: zwei Nachkommastellen + €
            kostenTextField.setText(String.format("%.2f €", gesamtKosten));
            //kosten wurden erfolgreich berechnet
            kostenBerechnet = true;
        } catch (NumberFormatException ex) {
            //Falls Personenanzahl keine gültige Zahl war
            JOptionPane.showMessageDialog(this,
                    "Bitte eine gültige Zahl bei der Personenanzahl eingeben!",
                    "Fehler", JOptionPane.ERROR_MESSAGE);
            kostenBerechnet = false;
        }
    }

    //Bestellung ausführen
    private void bestellungAusfuehren() {
        //Bestellung nur nach "Kosten berechnen" erlauben
        if (!kostenBerechnet || kostenTextField.getText() == null || kostenTextField.getText().isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Bitte zuerst die Kosten berechnen!",
                    "Hinweis", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Musik muss ausgewählt sein
        if (!istMusikGewaehlt()) {
            JOptionPane.showMessageDialog(this,
            "Bitte eine Musikrichtung auswählen!",
                    "Hinweis", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Essen muss entschieden werden (Ja oder Nein)
        if (!essenJaCheckBox.isSelected() && !essenNeinCheckBox.isSelected()) {
            JOptionPane.showMessageDialog(this,
                    "Bitte Essen auswählen!",
                    "Hinweis", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Daten aus GUI für Pop up Zusammenfassung
        String location = (String) locationComboBox.getSelectedItem();
        String qm = (String) qmCombobox.getSelectedItem();
        String musik = ausgewaehlteMusik();

        //Pop up Essenstext
        String essenText;
        if (essenJaCheckBox.isSelected()) {
            essenText = "Ja (" + essenComboBox.getSelectedItem() + ")";
        } else {
            essenText = "Nein";
        }

        //Personenanzahl für Popup
        int personen;
        try {
            personen = Integer.parseInt(PersonenanzahlTextField.getText().trim());
        } catch (Exception e) {
            personen = 0;
        }

        //Popup nach "Bestellung ausführen"
        JOptionPane.showMessageDialog(
                this,
                "Danke für Ihre Bestellung!\n\n" +
                        "Zusammenfassung:\n" +
                        "- Personen: " + personen + "\n" +
                        "- Bereich: " + location + "\n" +
                        "- Größe: " + qm + "\n" +
                        "- Musik/DJ: " + musik + "\n" +
                        "- Essen: " + essenText + "\n" +
                        "- Gesamtkosten: " + kostenTextField.getText(),
                "Bestellung", JOptionPane.INFORMATION_MESSAGE
        );
    }

    //Musik als String zurückgeben -> für Popup
    private String ausgewaehlteMusik() {
        if (rnbHiphopRadioButton.isSelected()) return "RNB/HipHop";
        if (technoUndElectroRadioButton.isSelected()) return "Techno/Electro";
        if (afroBeatsRadioButton.isSelected()) return "Afro Beats";
        if (rapRadioButton.isSelected()) return "Rap";
        return "Keine Auswahl";
    }

    //Objekte erstellen

    //Main Methode
    public static void main(String[] args) {
        new Partyplaner_UI();
    }
}