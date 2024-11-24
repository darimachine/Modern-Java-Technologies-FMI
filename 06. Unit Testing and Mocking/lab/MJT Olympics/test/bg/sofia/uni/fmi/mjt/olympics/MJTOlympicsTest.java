package bg.sofia.uni.fmi.mjt.olympics;


import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
public class MJTOlympicsTest {

    private CompetitionResultFetcher mockResultFetcher;
    private Competitor competitor1;
    private Competitor competitor2;
    private Competition competition;

    private MJTOlympics olympics;

    private Map<String, EnumMap<Medal, Integer>> nationalMedaLTable;

    @BeforeEach
    void setUp() {
        mockResultFetcher = mock();
        competitor1 = new Athlete("1", "Serhan", "German");
        competitor2 = new Athlete("2", "Georgi", "Bulgaria");
        Set<Competitor> competitors = Set.of(competitor1, competitor2);
        competition = new Competition("MJT", "Volleybal", competitors);

        olympics = new MJTOlympics(competitors, mockResultFetcher);

        EnumMap<Medal, Integer> enum1 = new EnumMap<>(Medal.class);
        enum1.put(Medal.GOLD, 3);

        EnumMap<Medal, Integer> enum2 = new EnumMap<>(Medal.class);
        enum2.put(Medal.GOLD, 5);

        EnumMap<Medal, Integer> enum3 = new EnumMap<>(Medal.class);
        enum3.put(Medal.GOLD, 2);
        enum3.put(Medal.SILVER, 5);

        olympics.getNationsMedalTable().put("German", enum1);
        olympics.getNationsMedalTable().put("Greece", enum2);
        olympics.getNationsMedalTable().put("Bulgaria", enum3);
    }

    @Test
    void testUpdateMedalStatisticsWithNullCompetion() {
        assertThrows(IllegalArgumentException.class, () -> olympics.updateMedalStatistics(null),
            "It should throw IllegalArgument Exception");
    }

    @Test
    void testUpdateMedalStatisticsWithCompetitorNotRegistered() {
        Competitor competitor3 = new Athlete("5", "Ahu", "Ihu");
        //olympics = new MJTOlympics(Set.of(competitor3),mockResultFetcher);
        Competition invalidcompetition = new Competition("MJT", "Basketball", Set.of(competitor3));
        assertThrows(IllegalArgumentException.class, () -> olympics.updateMedalStatistics(invalidcompetition),
            "It should throw IllegalArgument Exception for competitor not existing in competion");
    }

    @Test
    void testUpdateMedalStaticticsWithValidData() {
        TreeSet<Competitor> ranking = new TreeSet<>(Comparator.comparing(Competitor::getIdentifier));
        ranking.add(competitor2);
        ranking.add(competitor1);

        when(mockResultFetcher.getResult(competition)).thenReturn(ranking);

        olympics.updateMedalStatistics(competition);
        assertTrue(competitor1.getMedals().contains(Medal.GOLD), "Competitor1 should have Gold Medal");
        assertTrue(competitor2.getMedals().contains(Medal.SILVER), "Competitor1 should have Silver Medal");

        verify(mockResultFetcher, times(1)).getResult(competition);
    }

    @Test
    void testGetNationsMedalTableWithCorrectData() {
        var medalTable = olympics.getNationsMedalTable();
        assertEquals(3, medalTable.size(), "Medal table should contain 3 nations.");
        assertTrue(medalTable.containsKey("Greece"), "Medal table should contain Greece.");
        assertTrue(medalTable.containsKey("German"), "Medal table should contain Germany.");

        // Verify the medals for USA
        EnumMap<Medal, Integer> bulgariaResult = medalTable.get("Bulgaria");
        assertNotNull(bulgariaResult, "Bulgaria's medal data should not be null.");
        assertEquals(2, bulgariaResult.get(Medal.GOLD), "Bulgaria should have 2 gold medals.");
        assertEquals(5, bulgariaResult.get(Medal.SILVER), "Bulgaria should have 2 Silver medals.");
    }
    @Test
    void testGetTotalMedalsWithNull()
    {
        olympics.getNationsMedalTable().put("America",null);
        assertEquals(0,olympics.getTotalMedals("America"),"Should be zero for null medals");
    }
    @Test
    void testGetTotalMedalsWithEmptyMedals()
    {
        EnumMap<Medal,Integer> currEnum = new EnumMap<>(Medal.class);
        olympics.getNationsMedalTable().put("America",currEnum);
        assertEquals(0,olympics.getTotalMedals("America"),"Should be zero for empty medals");

    }

    @Test
    void testGetNationsRankListSortedByMedalCount() {
        TreeSet<String> rankList = olympics.getNationsRankList();
        List<String> expected = List.of("Bulgaria", "Greece", "German");
        assertIterableEquals(expected, rankList, "They should be sorted in descending order");
    }

    @Test
    void testGetNationsRankListSortedBySecondCriteriaName() {
        EnumMap<Medal, Integer> enum4 = new EnumMap<>(Medal.class);
        enum4.put(Medal.GOLD, 2);
        enum4.put(Medal.SILVER, 5);
        olympics.getNationsMedalTable().put("Africa", enum4);
        TreeSet<String> rankList = olympics.getNationsRankList();
        List<String> expected = List.of("Africa", "Bulgaria", "Greece", "German");
        assertIterableEquals(expected, rankList, "The equal medal count should be sorted in ascending order by name");
    }

    @Test
    void testGetTotalMedalsNationalityNull() {
        assertThrows(IllegalArgumentException.class, () -> olympics.getTotalMedals(null));
    }

    @Test
    void testGetTotalMedalsNationalityIsNotRegistered() {
        assertThrows(IllegalArgumentException.class, () -> olympics.getTotalMedals("Brazil"));
    }

    @Test
    void testGetTotalMedalsNationalityByChecking() {
        assertEquals(7, olympics.getTotalMedals("Bulgaria"));
        assertEquals(5, olympics.getTotalMedals("Greece"));
        assertEquals(3, olympics.getTotalMedals("German"));
    }
    @Test
    void testRegisteredCompetitorsByValidData() {
        competitor1 = new Athlete("1", "Serhan", "German");
        competitor2 = new Athlete("2", "Georgi", "Bulgaria");
        Set<Competitor> competitors = Set.of(competitor1, competitor2);
        assertIterableEquals(competitors,olympics.getRegisteredCompetitors(),"They should be equal");
    }
}
