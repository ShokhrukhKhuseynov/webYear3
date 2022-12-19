import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@DisplayName("Test LaptopDao")
class ScrapersTest {
    static LaptopDao laptopDao;

    @BeforeAll
    static void iniAll(){
        laptopDao = mock(LaptopDao.class);
    }
    @Test
    @DisplayName("Test Save and Merge")
    void saveAndMergeTest() throws Exception {
        Comparison comparison = new Comparison();
        when(laptopDao.saveAndMerge(comparison)).thenReturn(comparison);
        Assertions.assertEquals(comparison, laptopDao.saveAndMerge(comparison));
    }

    @ParameterizedTest
    @MethodSource("generateScrapers")
    @DisplayName("Test Save and Merge form Scrapers")
    void ScrapersTest (WebScraper webScraper) throws Exception{
        Comparison comparison = new Comparison();
        when(laptopDao.saveAndMerge(comparison)).thenReturn(comparison);
        webScraper.setLaptopDao(laptopDao);
        Assertions.assertEquals(comparison, webScraper.getLaptopDao().saveAndMerge(comparison));
    }

    private static Stream<Arguments> generateScrapers(){
        return Stream.of(
                Arguments.of(new AmazonScraper()),
                Arguments.of(new AppleScraperAir()),
                Arguments.of(new AppleScraperPro())
        );
    }
}
