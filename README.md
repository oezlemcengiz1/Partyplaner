Partyplaner – Java Projekt

Projektbeschreibung

Der Partyplaner ist eine grafische Java-Anwendung, mit der Benutzer eine Party planen, berechnen und speichern können.
Der Benutzer wählt Location, Größe, Musik, Personenanzahl und Essen aus, woraufhin das Programm die Kosten berechnet und die Party in einer Tabelle speichert.

Datenverwaltung

Alle geplanten Parties werden als Objekte der Klasse Party in der partyListe (ArrayList) gespeichert, welche als interne Datenbank des Programms dient.
Jede neue Party wird zur partyListe hinzugefügt und anschließend in der JTable angezeigt.

Zentrale Funktionen

Startobjekte erzeugen
Erstellt mehrere Beispiel-Parties und speichert sie direkt in der partyListe, damit beim Start bereits Daten vorhanden sind.
Methode: (initObjekte)
Kosten berechnen
Berechnet die Gesamtkosten der Party anhand von Location, qm, Musik, Essen und Personenanzahl.
Methode: (berechneKosten)
Party speichern
Erstellt aus den aktuellen Eingaben ein neues Party-Objekt und fügt es zur partyListe hinzu sowie in die Tabelle ein.
Methode: (speichern)
Tabelle aktualisieren und filtern
Liest die Daten aus der partyListe und zeigt entweder alle oder gefilterte Parties in der JTable an.
Methode: (aktualisiereTabelleNachFilter)
Tabelle füllen
Überträgt alle Party-Objekte aus der partyListe oder einer gefilterten Liste in Tabellenzeilen.
Methode: (fuelleTabelleMitListe)
Alle Parties löschen
Löscht alle Party-Objekte aus der partyListe und leert gleichzeitig die JTable.
Methode: (alleloeschen)
Bestellung ausführen
Zeigt eine Zusammenfassung der aktuell geplanten Party an.
Methode: (bestellungAusfuehren)
Programmstart
Startet das Programm und öffnet die Benutzeroberfläche.
Methode: (main)
