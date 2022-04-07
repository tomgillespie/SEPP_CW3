import command.*;
import controller.Controller;
import logging.Logger;
import model.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookEventSystemTest {
    @BeforeEach
    void printTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @AfterEach
    void clearLogs() {
        Logger.getInstance().clearLog();
        System.out.println("---");
    }

    // To book an event:
    // Register entertainment provider, (automatically logged in)
    // Create ticketed event
    // Register consumer, (again, automatically logged in)
    // Book event command called


    // The following tests test several the following scenarios: //
    // 1. "Perfect" booking procedure, as decribed above
    // 2. Entertainment Provider should not be able to book an event
    // 3. Not able to book if there are not enough tickets left
    // 4. If event does not exist, booking should fail
    // 5. Consumer should not be able to book NonTicketedEvent
    // 6. If less than 1 ticketed is requested, booking should fail
    // 7. If the performance has already ended, unbookable
    // 8. Logged out user unable to book a test


    @Test
    @DisplayName("PerfectBookEventTest")
    void perfectBookEventTest(){
        Controller controller = new Controller();
        RegisterEntertainmentProviderCommand regCmd1 = new RegisterEntertainmentProviderCommand(
                "University of Edinburgh",
                "Appleton Tower, Edinburgh",
                "edibank@ed.ac.uk",
                "Peter Mathieson",
                "pmathieson@ed.ac.uk",
                "hongkong",
                List.of("chinalover", "protesthater"),
                List.of("chinalover@ed.ac.uk"));
        controller.runCommand(regCmd1);

        CreateTicketedEventCommand eventCmd1 = new CreateTicketedEventCommand(
                "Fire breathing",
                EventType.Theatre,
                50,
                15,
                false
        );
        controller.runCommand(eventCmd1);

        AddEventPerformanceCommand performanceCmd1 = new AddEventPerformanceCommand(
                1,
                "The meadows",
                LocalDateTime.of(2030, 3, 20, 4, 20),
                LocalDateTime.of(2030, 3, 20, 6, 45),
                List.of("Fire breathers"),
                true,
                true,
                true,
                50,
                25
        );
        controller.runCommand(performanceCmd1);

        RegisterConsumerCommand registeredConsumer = new RegisterConsumerCommand(
                "John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        );
        controller.runCommand(registeredConsumer);
        User consumer = (User) registeredConsumer.getResult();

        BookEventCommand bookCmd1  = new BookEventCommand(
                1,
                1,
                10
        );
        controller.runCommand(bookCmd1);
        Long bookResult1 = (Long) bookCmd1.getResult();

        assertEquals(1, bookResult1);
        assertEquals("jbiggson1@hotmail.co.uk",consumer.getEmail());
    }

    @Test
    @DisplayName("nonConsumerBookEventTest")
    void nonConsumerBookEventTest(){
        Controller controller = new Controller();
        RegisterEntertainmentProviderCommand regCmd1 = new RegisterEntertainmentProviderCommand(
                "University of Edinburgh",
                "Appleton Tower, Edinburgh",
                "edibank@ed.ac.uk",
                "Peter Mathieson",
                "pmathieson@ed.ac.uk",
                "hongkong",
                List.of("chinalover", "protesthater"),
                List.of("chinalover@ed.ac.uk"));
        controller.runCommand(regCmd1);

        CreateTicketedEventCommand eventCmd1 = new CreateTicketedEventCommand(
                "Fire breathing",
                EventType.Theatre,
                50,
                15,
                false
        );
        controller.runCommand(eventCmd1);

        AddEventPerformanceCommand performanceCmd1 = new AddEventPerformanceCommand(
                1,
                "The meadows",
                LocalDateTime.of(2030, 3, 20, 4, 20),
                LocalDateTime.of(2030, 3, 20, 6, 45),
                List.of("Fire breathers"),
                true,
                true,
                true,
                50,
                25
        );
        controller.runCommand(performanceCmd1);
        EventPerformance performance = (EventPerformance) performanceCmd1.getResult();

        BookEventCommand bookCmd1  = new BookEventCommand(
                1,
                1,
                10
        );
        controller.runCommand(bookCmd1);
        Long bookResult2 = (Long) bookCmd1.getResult();

        assertNull(bookResult2);
        assertTrue(performance.hasSocialDistancing());
        assertEquals(25, performance.getVenueSize());
    }

    @Test
    @DisplayName("NotEnoughTicketsTest")
    void notEnoughTicketsTest(){
        Controller controller = new Controller();
        RegisterEntertainmentProviderCommand regCmd1 = new RegisterEntertainmentProviderCommand(
                "University of Edinburgh",
                "Appleton Tower, Edinburgh",
                "edibank@ed.ac.uk",
                "Peter Mathieson",
                "pmathieson@ed.ac.uk",
                "hongkong",
                List.of("chinalover", "protesthater"),
                List.of("chinalover@ed.ac.uk"));
        controller.runCommand(regCmd1);

        CreateTicketedEventCommand eventCmd1 = new CreateTicketedEventCommand(
                "Fire breathing",
                EventType.Theatre,
                50,
                15,
                false
        );
        controller.runCommand(eventCmd1);

        AddEventPerformanceCommand performanceCmd1 = new AddEventPerformanceCommand(
                1,
                "The meadows",
                LocalDateTime.of(2030, 3, 20, 4, 20),
                LocalDateTime.of(2030, 3, 20, 6, 45),
                List.of("Fire breathers"),
                true,
                true,
                true,
                50,
                25
        );
        controller.runCommand(performanceCmd1);

        RegisterConsumerCommand registeredConsumer = new RegisterConsumerCommand(
                "John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        );
        controller.runCommand(registeredConsumer);
        User user = (User) registeredConsumer.getResult();

        BookEventCommand bookCmd1  = new BookEventCommand(
                1,
                1,
                60
        );
        controller.runCommand(bookCmd1);
        Long bookResult3 = (Long) bookCmd1.getResult();

        assertNull(bookResult3);
        assertEquals("John Biggson", ((Consumer)user).getName());
    }

    @Test
    @DisplayName("EventNotFoundTest")
    void eventNotFoundTest(){
        Controller controller = new Controller();
        RegisterEntertainmentProviderCommand regCmd1 = new RegisterEntertainmentProviderCommand(
                "University of Edinburgh",
                "Appleton Tower, Edinburgh",
                "edibank@ed.ac.uk",
                "Peter Mathieson",
                "pmathieson@ed.ac.uk",
                "hongkong",
                List.of("chinalover", "protesthater"),
                List.of("chinalover@ed.ac.uk"));
        controller.runCommand(regCmd1);

        CreateTicketedEventCommand eventCmd1 = new CreateTicketedEventCommand(
                "Fire breathing",
                EventType.Theatre,
                50,
                15,
                false
        );
        controller.runCommand(eventCmd1);

        AddEventPerformanceCommand performanceCmd1 = new AddEventPerformanceCommand(
                1,
                "The meadows",
                LocalDateTime.of(2030, 3, 20, 4, 20),
                LocalDateTime.of(2030, 3, 20, 6, 45),
                List.of("Fire breathers"),
                true,
                true,
                true,
                50,
                25
        );
        controller.runCommand(performanceCmd1);

        RegisterConsumerCommand registeredConsumer = new RegisterConsumerCommand(
                "John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        );
        controller.runCommand(registeredConsumer);
        User user = (User) registeredConsumer.getResult();

        BookEventCommand bookCmd1  = new BookEventCommand(
                2,                                   // Event "2" does not exist in this environment
                1,
                60
        );

        controller.runCommand(bookCmd1);
        Long bookResult3 = (Long) bookCmd1.getResult();

        assertNull(bookResult3);
        assertEquals("John Biggson", ((Consumer)user).getName());
    }

    @Test
    @DisplayName("EventNotTicketedTest")
    void eventNotTicketedTest(){
        Controller controller = new Controller();
        RegisterEntertainmentProviderCommand regCmd1 = new RegisterEntertainmentProviderCommand(
                "University of Edinburgh",
                "Appleton Tower, Edinburgh",
                "edibank@ed.ac.uk",
                "Peter Mathieson",
                "pmathieson@ed.ac.uk",
                "hongkong",
                List.of("chinalover", "protesthater"),
                List.of("chinalover@ed.ac.uk"));
        controller.runCommand(regCmd1);
        User user = (User) regCmd1.getResult();

        CreateNonTicketedEventCommand cmd2 = new CreateNonTicketedEventCommand(
                "Free bowling event",
                EventType.Sports
        );
        controller.runCommand(cmd2);
        Long resultingEventNumber = cmd2.getResult();

        AddEventPerformanceCommand performanceCmd1 = new AddEventPerformanceCommand(
                1,
                "The meadows",
                LocalDateTime.of(2030, 3, 20, 4, 20),
                LocalDateTime.of(2030, 3, 20, 6, 45),
                List.of("Fire breathers"),
                true,
                true,
                true,
                50,
                25
        );
        controller.runCommand(performanceCmd1);


        BookEventCommand bookCmd1  = new BookEventCommand(
                1,                                   // Event "2" does not exist in this environment
                1,
                60
        );

        controller.runCommand(bookCmd1);
        Long bookResult3 = (Long) bookCmd1.getResult();

        assertNull(bookResult3);
        assertEquals(1, resultingEventNumber);
    }

    @Test
    @DisplayName("InvalidNumTicketsRequestedTest")
    void InvalidNumTicketsRequestedTest(){
        Controller controller = new Controller();
        RegisterEntertainmentProviderCommand regCmd1 = new RegisterEntertainmentProviderCommand(
                "University of Edinburgh",
                "Appleton Tower, Edinburgh",
                "edibank@ed.ac.uk",
                "Peter Mathieson",
                "pmathieson@ed.ac.uk",
                "hongkong",
                List.of("chinalover", "protesthater"),
                List.of("chinalover@ed.ac.uk"));
        controller.runCommand(regCmd1);

        CreateTicketedEventCommand eventCmd1 = new CreateTicketedEventCommand(
                "Fire breathing",
                EventType.Theatre,
                50,
                15,
                false
        );
        controller.runCommand(eventCmd1);

        AddEventPerformanceCommand performanceCmd1 = new AddEventPerformanceCommand(
                1,
                "The meadows",
                LocalDateTime.of(2030, 3, 20, 4, 20),
                LocalDateTime.of(2030, 3, 20, 6, 45),
                List.of("Fire breathers"),
                true,
                true,
                true,
                50,
                25
        );
        controller.runCommand(performanceCmd1);

        RegisterConsumerCommand registeredConsumer = new RegisterConsumerCommand(
                "John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        );
        controller.runCommand(registeredConsumer);
        User consumer = (User) registeredConsumer.getResult();

        BookEventCommand bookCmd1  = new BookEventCommand(
                1,
                1,
                0
        );
        controller.runCommand(bookCmd1);
        Long bookResult1 = (Long) bookCmd1.getResult();

        assertNull(bookResult1);
        assertEquals("jbiggson1@hotmail.co.uk",consumer.getEmail());
    }

    @Test
    @DisplayName("PerformanceEndedAlreadyTest")
    void PerformanceEndedAlreadyTest(){
        Controller controller = new Controller();
        RegisterEntertainmentProviderCommand regCmd1 = new RegisterEntertainmentProviderCommand(
                "University of Edinburgh",
                "Appleton Tower, Edinburgh",
                "edibank@ed.ac.uk",
                "Peter Mathieson",
                "pmathieson@ed.ac.uk",
                "hongkong",
                List.of("chinalover", "protesthater"),
                List.of("chinalover@ed.ac.uk"));
        controller.runCommand(regCmd1);

        CreateTicketedEventCommand eventCmd1 = new CreateTicketedEventCommand(
                "Fire breathing",
                EventType.Theatre,
                50,
                15,
                false
        );
        controller.runCommand(eventCmd1);

        AddEventPerformanceCommand performanceCmd1 = new AddEventPerformanceCommand(
                1,
                "The meadows",
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(9),
                List.of("Fire breathers"),
                true,
                true,
                true,
                50,
                25
        );
        controller.runCommand(performanceCmd1);

        RegisterConsumerCommand registeredConsumer = new RegisterConsumerCommand(
                "John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        );
        controller.runCommand(registeredConsumer);
        User consumer = (User) registeredConsumer.getResult();

        BookEventCommand bookCmd1  = new BookEventCommand(
                1,
                1,
                10
        );
        controller.runCommand(bookCmd1);
        Long bookResult1 = (Long) bookCmd1.getResult();

        assertNull(bookResult1);
        assertEquals("jbiggson1@hotmail.co.uk",consumer.getEmail());
    }

    @Test
    @DisplayName("loggedOutConsumerBookTest")
    void loggedOutConsumerBookTest(){
        Controller controller = new Controller();
        RegisterEntertainmentProviderCommand regCmd1 = new RegisterEntertainmentProviderCommand(
                "University of Edinburgh",
                "Appleton Tower, Edinburgh",
                "edibank@ed.ac.uk",
                "Peter Mathieson",
                "pmathieson@ed.ac.uk",
                "hongkong",
                List.of("chinalover", "protesthater"),
                List.of("chinalover@ed.ac.uk"));
        controller.runCommand(regCmd1);

        CreateTicketedEventCommand eventCmd1 = new CreateTicketedEventCommand(
                "Fire breathing",
                EventType.Theatre,
                50,
                15,
                false
        );
        controller.runCommand(eventCmd1);

        AddEventPerformanceCommand performanceCmd1 = new AddEventPerformanceCommand(
                1,
                "The meadows",
                LocalDateTime.of(2030, 3, 20, 4, 20),
                LocalDateTime.of(2030, 3, 20, 6, 45),
                List.of("Fire breathers"),
                true,
                true,
                true,
                50,
                25
        );
        controller.runCommand(performanceCmd1);

        RegisterConsumerCommand registeredConsumer = new RegisterConsumerCommand(
                "John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        );
        controller.runCommand(registeredConsumer);
        User consumer = (User) registeredConsumer.getResult();

        controller.runCommand(new LogoutCommand());

        BookEventCommand bookCmd1  = new BookEventCommand(
                1,
                1,
                10
        );
        controller.runCommand(bookCmd1);
        Long bookResult1 = (Long) bookCmd1.getResult();

        assertNull(bookResult1);
        assertEquals("jbiggson1@hotmail.co.uk",consumer.getEmail());
    }
}
