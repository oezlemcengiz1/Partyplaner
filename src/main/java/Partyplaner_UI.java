import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

    public class Partyplaner_UI extends JFrame {
        private JPanel hauptPanel;
        private JLabel partyplanerLabel;

        private JLabel musikDJLabel;
        private JRadioButton rnbHiphopRadioButton;
        private JRadioButton technoUndElectroRadioButton;
        private JRadioButton afroBeatsRadioButton;
        private JRadioButton rapRadioButton;
        private ButtonGroup musikGruppe;

        private JLabel essenLabel;
        private ButtonGroup essenGruppe;
        private JComboBox essenComboBox;
        private JRadioButton essenJaRadioButton;
        private JRadioButton essenNeinRadioButton;

        private JLabel personenanzahlLabel;
        private JTextField personenanzahlTextField;

        private JLabel locationLabel;
        private JComboBox locationComboBox;
        private JComboBox qmCombobox;

        private JTextField kostenTextField;
        private JLabel kostenLabel;
        private JButton kostenBerechneButton;
        private JButton bestellungausfuehrenButton;

        // ArrayList für Objekte aus Klasse "Party"
        private ArrayList<Party> partyListe;

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
            partyListe = new ArrayList<>();
            // Startobjekte erzeugen
            initObjekte();

            //ButtonGroup für Musik-RadioButtons: damit nur ein DJ ausgewählt werden kann
            musikGruppe = new ButtonGroup();
            musikGruppe.add(rnbHiphopRadioButton);
            musikGruppe.add(technoUndElectroRadioButton);
            musikGruppe.add(afroBeatsRadioButton);
            musikGruppe.add(rapRadioButton);

            // ButtonGroup für Essen-RadioButtons (Ja / Nein)
            essenGruppe = new ButtonGroup();
            essenGruppe.add(essenJaRadioButton);
            essenGruppe.add(essenNeinRadioButton);

            // Startzustand: ComboBox Essen-Auswahl ausblenden
            essenComboBox.setEnabled(false);
            essenComboBox.setSelectedIndex(-1);

            //Essen = Ja
            essenJaRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // ComboBox aktivieren
                    essenComboBox.setEnabled(true);
                    // Erste Auswahl automatisch setzen (z. B. "offenes Buffet")
                    essenComboBox.setSelectedIndex(0);
                }
            });
            //Essen = Nein
            essenNeinRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // ComboBox deaktivieren
                    essenComboBox.setEnabled(false);
                    // Auswahl entfernen
                    essenComboBox.setSelectedIndex(-1);
                }
            });
            //Kosten berechnen
            kostenBerechneButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    berechneKosten();
                }
            });
            //Bestellung ausführen
            bestellungausfuehrenButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    bestellungAusfuehren();
                }
            });
        } // <-- Konstruktor geht zu Ende

        // legt Startobjekte an, damit Liste nicht leer ist
        public void initObjekte() {
            partyListe.add(new Party("Innenbereich", "RNB", 30, true));
            partyListe.add(new Party("Außenbereich", "Techno", 80, false));
            partyListe.add(new Party("Innen- und Außenbereich", "Afro Beats", 50, true));
        }

        // Methode: prüfen ob Musik gewählt wurde
        private boolean istMusikGewaehlt() {
            return rnbHiphopRadioButton.isSelected()
                    || technoUndElectroRadioButton.isSelected()
                    || afroBeatsRadioButton.isSelected()
                    || rapRadioButton.isSelected();
        }

        //Musik als String zurückgeben -> für Popup
        private String ausgewaehlteMusik() {
            if (rnbHiphopRadioButton.isSelected()) return "RNB/HipHop";
            if (technoUndElectroRadioButton.isSelected()) return "Techno/Electro";
            if (afroBeatsRadioButton.isSelected()) return "Afro Beats";
            if (rapRadioButton.isSelected()) return "Rap";
            return "";
        }

        // Gesamtkosten berechnen
        private void berechneKosten() {
            try {
                //Personenanzahl
                int personen = Integer.parseInt(personenanzahlTextField.getText().trim());

                //Personenanzahl darf nicht 0 oder negativ sein
                if (personen <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "Personenanzahl muss größer als 0 sein!",
                            "Fehler", JOptionPane.ERROR_MESSAGE);
                    kostenBerechnet = false;
                    return;
                }
                //Essen muss entschieden werden (Ja oder Nein)
                if (!essenJaRadioButton.isSelected() && !essenNeinRadioButton.isSelected()) {
                    JOptionPane.showMessageDialog(this,
                            "Bitte Essen auswählen!",
                            "Hinweis", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!istMusikGewaehlt()) {
                    JOptionPane.showMessageDialog(this,
                            "Bitte eine Musikrichtung auswählen!",
                            "Hinweis", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // ComboBox denkt Inhalt sind Objekte, in unserem fall aber Text bzw. String.
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
                    locationKosten += 250;
                } else if ("40 - 80 qm".equals(qm)) {
                    locationKosten += 150;
                }

                //DJ-Kosten berechnen:
                double djKosten = 0;
                if (rnbHiphopRadioButton.isSelected()) djKosten = 200;
                else if (technoUndElectroRadioButton.isSelected()) djKosten = 250;
                else if (afroBeatsRadioButton.isSelected()) djKosten = 230;
                else if (rapRadioButton.isSelected()) djKosten = 190;

                //Kosten Essen berechnen, nur wenn "Ja":
                double essenKosten = 0;
                // ComboBox denkt Inhalt sind Objekte, in unserem fall aber Text bzw. String.
                if (essenJaRadioButton.isSelected()) {
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
            if (!kostenBerechnet) {
                JOptionPane.showMessageDialog(this,
                        "Bitte zuerst die Kosten berechnen!",
                        "Hinweis", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String location = (String) locationComboBox.getSelectedItem();
            String qm = (String) qmCombobox.getSelectedItem();
            String musik = ausgewaehlteMusik();
            int personen = Integer.parseInt(personenanzahlTextField.getText().trim());
            boolean essen = essenJaRadioButton.isSelected();

            // NEU: Objekt erzeugen & speichern (Pflicht!)
            Party neueParty = new Party(location, musik, personen, essen);
            partyListe.add(neueParty);

            String essenText = essen
                    ? "Ja (" + essenComboBox.getSelectedItem() +
                    ")" : "Nein";
            JOptionPane.showMessageDialog(this,
                    "Danke für Ihre Bestellung!\n\n" +
                            "Personen: " + personen + "\n" +
                            "Bereich: " + location + "\n" +
                            "Größe: " + qm + "\n" +
                            "Musik: " + musik + "\n" +
                            "Essen: " + essenText + "\n" +
                            "Kosten: " + kostenTextField.getText()
            );
        }
            // Nutzung der Objekt-Methode
        private void zeigePartysMitEssen() {
            for (Party p : partyListe) {
                if (p.hatEssen()) {
                    System.out.println(p.location + " | " + p.musikDJ);
                }
            }
        }

    //Main Methode
    public static void main(String[] args) {
        new Partyplaner_UI();
    }
}