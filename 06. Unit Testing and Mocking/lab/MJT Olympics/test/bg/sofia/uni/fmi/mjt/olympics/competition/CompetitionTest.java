package bg.sofia.uni.fmi.mjt.olympics.competition;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CompetitionTest {

    private Competitor competitorUser;

    @BeforeEach
    void setUpCompetitorUser() {
        competitorUser = new Athlete("1", "Serhan", "Volleybal");
    }

    @Test
    void testCompetionWithNullName() {
        Set<Competitor> competitorSet = Set.of(competitorUser);
        //competitorSet.add(competitorUser);

        assertThrows(IllegalArgumentException.class, () -> new Competition(null, "Volleybal", competitorSet),
            "It should Throw IllegalArgumentException for null name");
    }

    @Test
    void testCompetionWithBlankName() {
        Set<Competitor> competitorSet = Set.of(competitorUser);
        //competitorSet.add(competitorUser);
        assertThrows(IllegalArgumentException.class, () -> new Competition("", "Volleybal", competitorSet),
            "It should Throw IllegalArgumentException for Blank name");
    }

    @Test
    void testCompetionWithNullDiscipline() {
        Set<Competitor> competitorSet = Set.of(competitorUser);
        //competitorSet.add(competitorUser);
        assertThrows(IllegalArgumentException.class, () -> new Competition("MJT", null, competitorSet),
            "It should Throw IllegalArgumentException for null discipline");
    }

    @Test
    void testCompetionWithBlankDiscipline() {
        Set<Competitor> competitorSet = Set.of(competitorUser);
        //competitorSet.add(competitorUser);
        assertThrows(IllegalArgumentException.class, () -> new Competition("MJT", "", competitorSet),
            "It should Throw IllegalArgumentException for blank discipline");
    }

    @Test
    void testCompetionWithNullCompetitors() {
        //competitorSet.add(competitorUser);
        assertThrows(IllegalArgumentException.class, () -> new Competition("MJT", "Volleybal", null),
            "It should Throw IllegalArgumentException for null competitors");
    }

    @Test
    void testCompetionWithEmptyCompetitors() {
        Set<Competitor> competitorSet = Set.of();
        //competitorSet.add(competitorUser);
        assertThrows(IllegalArgumentException.class, () -> new Competition("MJT", "Volleybal", competitorSet),
            "It should Throw IllegalArgumentException for empty competitors");
    }

    @Test
    void testCompetionWithCorrectInfo() {
        Set<Competitor> competitorSet = Set.of(competitorUser);
        Competition comp = new Competition("MJT","Volleybal",competitorSet);
        assertEquals("MJT",comp.name(),"Name must be correct");
        assertEquals("Volleybal",comp.discipline(),"discipline must be correct");
        assertIterableEquals(competitorSet,comp.competitors(),"Competitor collection must be equal");
    }
    @Test
    void testEqualsReflexive() {
        Set<Competitor> competitors = Set.of(new Athlete("1", "John Doe", "USA"));
        Competition competition = new Competition("MJT", "Volleyball", competitors);

        assertEquals(competition, competition, "An object should be equal to itself.");
    }
    @Test
    void testEqualsSymmetric() {
        Set<Competitor> competitors = Set.of(new Athlete("1", "John Doe", "USA"));
        Competition competition1 = new Competition("MJT", "Volleyball", competitors);
        Competition competition2 = new Competition("MJT", "Volleyball", competitors);

        assertEquals(competition1, competition2, "Equals should be symmetric.");
        assertEquals(competition2, competition1, "Equals should be symmetric.");
    }

    @Test
    void testEqualsTransitive() {
        Set<Competitor> competitors = Set.of(new Athlete("1", "John Doe", "USA"));
        Competition competition1 = new Competition("MJT", "Volleyball", competitors);
        Competition competition2 = new Competition("MJT", "Volleyball", competitors);
        Competition competition3 = new Competition("MJT", "Volleyball", competitors);

        assertEquals(competition1, competition2, "Equals should be transitive.");
        assertEquals(competition2, competition3, "Equals should be transitive.");
        assertEquals(competition1, competition3, "Equals should be transitive.");
    }

    @Test
    void testEqualsNull() {
        Set<Competitor> competitors = Set.of(new Athlete("1", "John Doe", "USA"));
        Competition competition = new Competition("MJT", "Volleyball", competitors);

        assertNotEquals(competition, null, "An object should not be equal to null.");
    }

    @Test
    void testEqualsDifferentClass() {
        Set<Competitor> competitors = Set.of(new Athlete("1", "John Doe", "USA"));
        Competition competition = new Competition("MJT", "Volleyball", competitors);

        assertNotEquals(competition, "A string object", "An object should not be equal to an object of a different class.");
    }

    @Test
    void testEqualsDifferentValues() {
        Set<Competitor> competitors = Set.of(new Athlete("1", "John Doe", "USA"));
        Competition competition1 = new Competition("MJT", "Volleyball", competitors);
        Competition competition2 = new Competition("MJT", "Basketball", competitors);

        assertNotEquals(competition1, competition2, "Competitions with different disciplines should not be equal.");
    }

    @Test
    void testHashCodeConsistency() {
        Set<Competitor> competitors = Set.of(new Athlete("1", "John Doe", "USA"));
        Competition competition = new Competition("MJT", "Volleyball", competitors);

        int initialHashCode = competition.hashCode();
        assertEquals(initialHashCode, competition.hashCode(), "HashCode should be consistent.");
    }

    @Test
    void testHashCodeEquality() {
        Set<Competitor> competitors = Set.of(new Athlete("1", "John Doe", "USA"));
        Competition competition1 = new Competition("MJT", "Volleyball", competitors);
        Competition competition2 = new Competition("MJT", "Volleyball", competitors);

        assertEquals(competition1.hashCode(), competition2.hashCode(), "Equal objects must have the same hash code.");
    }

    @Test
    void testHashCodeDifferentValues() {
        Set<Competitor> competitors = Set.of(new Athlete("1", "John Doe", "USA"));
        Competition competition1 = new Competition("MJT", "Volleyball", competitors);
        Competition competition2 = new Competition("MJT", "Basketball", competitors);

        assertNotEquals(competition1.hashCode(), competition2.hashCode(), "Different objects should ideally have different hash codes.");
    }
}
