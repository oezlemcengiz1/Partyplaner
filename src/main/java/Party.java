public class Party {

    String location;
    String musikDJ;
    int personenanzahl;
    boolean essen;

    // Konstruktor
    public Party(String location, String musikDJ, int personenanzahl, boolean essen) {
        this.location = location;
        this.musikDJ = musikDJ;
        this.personenanzahl = personenanzahl;
        this.essen = essen;
    }

    //Methode für JUnit-Test
    // → wird später für Filter + JUnit-Test verwendet
    public boolean hatEssen () {
        return essen;
    }
}
