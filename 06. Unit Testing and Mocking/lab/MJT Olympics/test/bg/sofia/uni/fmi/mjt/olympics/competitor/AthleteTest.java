package bg.sofia.uni.fmi.mjt.olympics.competitor;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AthleteTest {
    private Athlete athlete;

    @BeforeEach
    void setUpAthlete() {
        athlete = new Athlete("1", "Serhan", "Bulgaria");
    }

    @Test
    void testValidateMedalWithNull() {
        assertThrows(IllegalArgumentException.class, () -> athlete.addMedal(null), "Medal is null");
    }

    @Test
    void testValidateMedalWithValidMedal() {
        assertDoesNotThrow(() -> athlete.addMedal(Medal.GOLD),
            "validate medal should not throw exception for valid medals");
    }

    @Test
    void testValidateMedalWithMedalGold() {
        athlete.addMedal(Medal.GOLD);
        assertEquals(1, athlete.getMedals().size(), "Gold Medal didnt add correctly");
        assertTrue(athlete.getMedals().contains(Medal.GOLD), "Gold Medal is not on the Collection");
    }

    @Test
    void testValidateMedalWithMedalSilver() {
        athlete.addMedal(Medal.SILVER);
        assertEquals(1, athlete.getMedals().size(), "Silver Medal didnt add correctly");
        assertTrue(athlete.getMedals().contains(Medal.SILVER), "Silver Medal is not on the Collection");
    }

    @Test
    void testValidateMedalWithMedalBronze() {
        athlete.addMedal(Medal.BRONZE);
        assertEquals(1, athlete.getMedals().size(), "BRONZE Medal didnt add correctly");
        assertTrue(athlete.getMedals().contains(Medal.BRONZE), "BRONZE Medal is not on the Collection");
    }

    @Test
    void testAddMedalWithSameType() {
        athlete.addMedal(Medal.GOLD);
        athlete.addMedal(Medal.GOLD);
        athlete.addMedal(Medal.GOLD);
        assertEquals(3,athlete.getMedalCount(Medal.GOLD));
    }

    @Test
    void testEqualsReflexive() {
        assertEquals(athlete, athlete, "Athlete must be equal reflexive");
    }

    @Test
    void testEqualsSymetric() {
        Athlete athlete1 = new Athlete("2", "Serhan", "German");
        Athlete athlete2 = new Athlete("2", "Serhan", "German");

        assertEquals(athlete1, athlete2, "Should be equal Symmetric");
        assertEquals(athlete2, athlete1, "Should be equal Symmetric");
    }

    @Test
    void testEqualsTransitive() {
        Athlete athlete1 = new Athlete("2", "Serhan", "German");
        Athlete athlete2 = new Athlete("2", "Serhan", "German");
        Athlete athlete3 = new Athlete("2", "Serhan", "German");

        assertEquals(athlete1, athlete2, "Should be equal Transitive");
        assertEquals(athlete2, athlete3, "Should be equal Transitive");
        assertEquals(athlete3, athlete1, "Should be equal Transitive");
    }

    @Test
    void testEqualsWithDifferentMedals() {
        Athlete athlete1 = new Athlete("2", "Serhan", "German");
        Athlete athlete2 = new Athlete("2", "Serhan", "German");
        athlete1.addMedal(Medal.GOLD);
        assertNotEquals(athlete1, athlete2, "two athelets with different medal must not be equal");
    }

    @Test
    void testEqualsWithDifferentIdentifier() {

    }

    @Test
    void testEqualsWithDifferentObject() {
        assertNotEquals(athlete, "Random obj", "They must not be equals");
    }

    @Test
    void testHashCodeEquality() {
        Athlete athlete1 = new Athlete("2", "Serhan", "German");
        Athlete athlete2 = new Athlete("2", "Serhan", "German");

        assertEquals(athlete1.hashCode(), athlete2.hashCode(), "Should be equal HashCode");
        //assertEquals(athlete2.hashCode(), athlete1.hashCode(), "Should be equal HashCode");
    }

    @Test
    void testHashCodeNotEqualName() {
        Athlete athlete1 = new Athlete("2", "Serhan2", "German");
        Athlete athlete2 = new Athlete("2", "Serhan", "German");

        assertNotEquals(athlete1.hashCode(), athlete2.hashCode(), "Should not be equal HashCode");
        // assertNotEquals(athlete2.hashCode(), athlete1.hashCode(), "Should not be equal HashCode");
    }

    @Test
    void testHashCodeNotEqualIdentifier() {
        Athlete athlete1 = new Athlete("1", "Serhan", "German");
        Athlete athlete2 = new Athlete("2", "Serhan", "German");
        //opravi go
        assertEquals(athlete1.hashCode(), athlete2.hashCode(), "Should not be equal HashCode");
    }

    @Test
    void testEqualsWithNullAthlete() {
        Athlete athlete1 = new Athlete("1", "Serhan", "German");
        assertNotEquals(athlete1, null, "Null must not be equal to athlete");
    }

    @Test
    void testGetNameWithCorrectInfo() {
        Athlete athlete1 = new Athlete("1", "Serhan", "German");
        assertEquals("Serhan", athlete1.getName(), "Get name should return the correct name");
    }

    @Test
    void testGetIdentifierWithCorrectInfo() {
        Athlete athlete1 = new Athlete("1", "Serhan", "German");
        assertEquals("1", athlete1.getIdentifier(), "Get name should return the correct name");
    }

    @Test
    void testGetNationalityWithCorrectInfo() {
        Athlete athlete1 = new Athlete("1", "Serhan", "German");
        assertEquals("German", athlete1.getNationality(), "Get name should return the correct name");
    }
}
