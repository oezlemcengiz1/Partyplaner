import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
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

        private JButton speichernButton;

        private JButton filternButton;
        private JComboBox filternComboBox;
        private JTable filternTable;

        //TableModel damit wir Zeilen leicht löschen/neu setzen können
        private DefaultTableModel tableModel;

        // ArrayList für Objekte aus Klasse "Party"
        private ArrayList<Party> partyListe;

        //Bestellung erst erlaubt, wenn Kosten berechnet wurden
        private boolean kostenBerechnet = false;

        //Bestellung erst möglich nach dem Speichern
        private boolean partyGespeichert = false;

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

            //Startzustand der Buttons
            //speichern erst nach Kostenberechnung
            speichernButton.setEnabled(false);
            //Bestellung erst nach Speichern
            bestellungausfuehrenButton.setEnabled(false);

            //Damit ComboBox leer steht
            locationComboBox.setSelectedIndex(-1);
            qmCombobox.setSelectedIndex(-1);

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

            //JTable (Spalten definieren)
            initTabelle();

            filternComboBox.setSelectedIndex(-1);

            //damit man Startobjekte sofort sieht, ohne erst auf Filtern zu klicken
            aktualisiereTabelleNachFilter();

            //Reset Listener für Alle Eingaben
            //sobald etwas nach der Kostenberechnung geändert, gelöscht oder neu eingetragen wird
            personenanzahlTextField.getDocument().addDocumentListener(new DocumentListener() {
                @Override public void insertUpdate(DocumentEvent e) { resetStatusNachAenderung(); }
                @Override public void removeUpdate(DocumentEvent e) { resetStatusNachAenderung(); }
                @Override public void changedUpdate(DocumentEvent e) { resetStatusNachAenderung(); }
            });

            //Musik RadioButtons bei Änderungen -> erneut Kosten berechnen (Speichern, Bestellung ausführen Button "erlöscht")
            rnbHiphopRadioButton.addActionListener(e -> resetStatusNachAenderung());
            technoUndElectroRadioButton.addActionListener(e -> resetStatusNachAenderung());
            afroBeatsRadioButton.addActionListener(e -> resetStatusNachAenderung());
            rapRadioButton.addActionListener(e -> resetStatusNachAenderung());

            //bei Änderung von essen ja nein -> erneut Kosten berechnen
            essenJaRadioButton.addActionListener(e -> resetStatusNachAenderung());
            essenNeinRadioButton.addActionListener(e -> resetStatusNachAenderung());

            //Änderung bei Essen ComboBox -> erneut Kosten berechnen
            essenComboBox.addActionListener(e -> resetStatusNachAenderung());

            //Essen = Ja
            essenJaRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // ComboBox aktivieren
                    essenComboBox.setEnabled(true);
                    // Erste Auswahl automatisch setzen (z. B. "offenes Buffet")
                    essenComboBox.setSelectedIndex(-1);
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
            speichernButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    speichern();
                }
            });
            filternButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    aktualisiereTabelleNachFilter();
                }
            });
            locationComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    resetStatusNachAenderung();
                }
            });
            qmCombobox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    resetStatusNachAenderung();
                }
            });
        } // <-- Konstruktor geht zu Ende

        //legt Startobjekte in JTable an, damit Liste nicht leer ist (3 Parties)
        public void initObjekte() {
            partyListe.add(new Party("Innenbereich", "40 - 80 qm", "RNB und HipHop", 30, true));
            partyListe.add(new Party("Außenbereich", "90 - 120 qm", "Techno und Electro", 80, false));
            partyListe.add(new Party("Innen- und Außenbereich", "90 - 120 qm", "Afro Beats", 50, true));
        }

        //Methode: sperrt Speichern und Bestellung ausführen Button
        private void sperreAktionen() {
            speichernButton.setEnabled(false);
            bestellungausfuehrenButton.setEnabled(false);
            partyGespeichert = false;
            kostenBerechnet = false;
        }

        //wenn etwas an der Party geändert wird -> kosten sind nicht mehr gültig, muss erneut berechnet werden
        private void resetStatusNachAenderung() {
            kostenBerechnet = false;
            partyGespeichert = false;
            speichernButton.setEnabled(false);
            bestellungausfuehrenButton.setEnabled(false);
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

        //definiert Spaltennamen in JTable
        private void initTabelle() {
            //Spaltenüberschriften der JTable
            String[] spalten = {"Location", "QM", "Musik/DJ", "Personen", "Essen"};
            //DefaultTableModel: Daten für JTable
            //erste gespeicherte fängt dann direkt bei 0 an
            tableModel = new DefaultTableModel(spalten, 0) {
                @Override
                //isCellEditable: damit man Daten nicht in der Tabelle ändern kann
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            //verknüpft JTable mit TableModel -> JTable weiß welche Daten sie anzeigen soll
            filternTable.setModel(tableModel);

            //Sortierung per Spaltenklick aktivieren, muss nicht sein!!
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
            filternTable.setRowSorter(sorter);
        }

        //löscht alle bisherigen Zeilen
        //Wandelt Party-Objekte in Tabellenzeilen um
        //Erst Tabelle leeren, dann jede Party als neue Zeile hinzufügen
        private void fuelleTabelleMitListe(ArrayList<Party> liste) {
            tableModel.setRowCount(0);

            for(Party p : liste) {
                String essenText = p.essen ? "Ja" : "Nein";

                Object[] zeile = {
                        p.location,
                        p.qm,
                        p.musikDJ,
                        p.personenanzahl,
                        essenText
                };

                tableModel.addRow(zeile);
            }
        }

        //Liest Filterauswahl aus filterComboBox und baut eine gefilterte Liste
        private void aktualisiereTabelleNachFilter() {
            String auswahl = (String) filternComboBox.getSelectedItem();
            ArrayList<Party> gefiltert = new ArrayList<>();
            //falls nicht ausgewählt ist -> alle angezeigt
            if (auswahl == null || auswahl.isBlank()) {
                fuelleTabelleMitListe(partyListe);
                return;
            }
            //Parties nur mit Essen angezeigt
            if ("Parties nur mit Essen".equals(auswahl)) {
                for (Party p : partyListe) {
                    if (p.hatEssen()) {
                        gefiltert.add(p);
                    }
                }
                fuelleTabelleMitListe(gefiltert);
                //Parties nur ohne Essen angezeigt
            } else if ("Parties nur ohne Essen".equals(auswahl)) {
                for (Party p : partyListe) {
                    if (!p.hatEssen()) {
                        gefiltert.add(p);
                    }
                }
                fuelleTabelleMitListe(gefiltert);
                //Alle Parties angezeigt
            } else {
                fuelleTabelleMitListe(partyListe);
            }
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
                    //Buttons werden gesperrt
                    //damit man nicht nochmal was ändern kann und dann ohne die Kosten erneut zu berechnen speichern kann
                    sperreAktionen();
                    return;
                }
                //Essen muss entschieden werden (Ja oder Nein)
                if (!essenJaRadioButton.isSelected() && !essenNeinRadioButton.isSelected()) {
                    JOptionPane.showMessageDialog(this,
                            "Bitte Essen auswählen!",
                            "Hinweis", JOptionPane.ERROR_MESSAGE);

                    //Buttons werden wieder gesperrt
                    sperreAktionen();
                    return;
                }

                if (essenJaRadioButton.isSelected() && essenComboBox.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(this,
                            "Bitte Essen-Art auswählen!",
                            "Hinweis", JOptionPane.ERROR_MESSAGE);
                    sperreAktionen();
                    return;
                }

                if (locationComboBox.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(this,
                            "Bitte eine Location auswählen!",
                            "Hinweis", JOptionPane.ERROR_MESSAGE);
                    sperreAktionen();
                    return;
                }

                if (qmCombobox.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(this,
                            "Bitte eine QM-Größe auswählen!",
                            "Hinweis", JOptionPane.ERROR_MESSAGE);
                    sperreAktionen();
                    return;
                }

                if (!istMusikGewaehlt()) {
                    JOptionPane.showMessageDialog(this,
                            "Bitte eine Musikrichtung auswählen!",
                            "Hinweis", JOptionPane.ERROR_MESSAGE);

                    //Buttons wieder sperren
                    sperreAktionen();
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
                if ("90 - 120 qm".equals(qm)) locationKosten += 250;
                else if ("40 - 80 qm".equals(qm)) locationKosten += 150;

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
                //jetzt darf gespeichert werden
                //kosten wurden erfolgreich berechnet
                kostenBerechnet = true;
                //damit wenn man Kosten berechnet hat erst speichern Button aktiviert wird
                speichernButton.setEnabled(true);
                //Bestellung noch gesperrt da man noch nichts gespeichert hat
                //Bestellung bleibt noch so lange gesperrt bis gespeichert wurde
                bestellungausfuehrenButton.setEnabled(false);
                //Party noch nicht gespeichert
                partyGespeichert = false;

            } catch (NumberFormatException ex) {
                //Falls Personenanzahl keine gültige Zahl war
                JOptionPane.showMessageDialog(this,
                        "Bitte eine gültige Zahl bei der Personenanzahl eingeben!",
                        "Fehler", JOptionPane.ERROR_MESSAGE);
                sperreAktionen();
            }
        }

        //Bestellung speichern
        //wenn Kosten nicht berechnet wurden, dann darf nicht gespeichert werden
        //man könnte auch: if (kostenBerechnet) { speichern(); }
        private void speichern() {
            if (!kostenBerechnet) {
                return;
            }
            //personenanzahl aus dem TextField holen; Integer.parseInt(..)= wandelt den Text "50" in eine Zahl 50 um
            int personen = Integer.parseInt(personenanzahlTextField.getText().trim());
            //location holen
            String location = (String) locationComboBox.getSelectedItem();
            //qm holen
            String qm = (String) qmCombobox.getSelectedItem();
            //musik holen
            String musik = ausgewaehlteMusik();
            //Essen als Wahrheitswert speichern wenn ja oder nein
            boolean essen = essenJaRadioButton.isSelected();

            //Party-Objekte erzeugen -> wie Aufgabenstellung(Objekte einer selbst definierten Klasse erzeugen)
            Party neueParty = new Party(location, qm, musik, personen, essen);
            //neuerzeugte Party-Objekt wird in ArrayList gespeichert
            partyListe.add(neueParty);

            //Pop up das Speichern funktioniert hat
            JOptionPane.showMessageDialog(this,
                    "Party erfolgreich gespeichert!",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

            //nach dem Speichern Tabelle aktualisieren
            aktualisiereTabelleNachFilter();
            //nach dem Speichern kann man "Bestellung ausführen"
            partyGespeichert = true;
            //Bestellung wirdfreigeschalten
            bestellungausfuehrenButton.setEnabled(true);
            // deaktiviert speicherButton, um nicht mehrfach zu speichern
            speichernButton.setEnabled(false);
            /*nachdem man speichern gedrückt hat,
            so wird alles zurückgesetzt und bei geänderten Eingaben
            müssen Kosten erneut berechnet werden, bevor erneut gespeichert wird
             */
            kostenBerechnet = false;
        }

        //Bestellung ausführen
        private void bestellungAusfuehren() {
            //Bestellung nur nach "Kosten berechnen" erlauben
            if (!partyGespeichert) {
                JOptionPane.showMessageDialog(this,
                        "Bitte zuerst Speichern klicken!",
                        "Hinweis", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String location = (String) locationComboBox.getSelectedItem();
            String qm = (String) qmCombobox.getSelectedItem();
            String musik = ausgewaehlteMusik();
            int personen = Integer.parseInt(personenanzahlTextField.getText().trim());
            boolean essen = essenJaRadioButton.isSelected();

            String essenText = essen ? "Ja (" + essenComboBox.getSelectedItem() + ")" : "Nein";

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

    //Main Methode
    public static void main(String[] args) {
        new Partyplaner_UI();
    }
}