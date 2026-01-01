public class Party {

    String location;
    String qm;
    String musikDJ;
    int personenanzahl;
    boolean essen;

    // Konstruktor
    public Party(String location, String qm, String musikDJ, int personenanzahl, boolean essen) {
        this.location = location;
        this.qm = qm;
        this.musikDJ = musikDJ;
        this.personenanzahl = personenanzahl;
        this.essen = essen;
    }

    //Methode für JUnit-Test
    // → wird später für Filter + JUnit-Test verwendet
    public boolean istEssenEnthalten() {
        return essen && personenanzahl > 0;
    }
}
