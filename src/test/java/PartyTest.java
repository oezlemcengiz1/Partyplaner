import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class PartyTest {

    @Test
    void testKeinEssen() {
        Party party = new Party("Halle", "100", "DJ", 50, false);
        assertFalse(party.istEssenEnthalten());
    }

    @Test
    void testMitEssen() {
        Party party = new Party("Halle", "100", "DJ", 50, true);
        assertTrue(party.istEssenEnthalten());
    }
}
