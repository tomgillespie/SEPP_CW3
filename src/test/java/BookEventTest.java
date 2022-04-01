import command.*;
import controller.Controller;
import logging.Logger;
import model.EventType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BookEventTest {
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

    private Long bookResult1;
    private Long bookResult2;
    private Long bookResult3;

    private void bookEvent(Controller controller){
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

        BookEventCommand bookCmd1  = new BookEventCommand(
                1,
                1,
                10
        );
        controller.runCommand(bookCmd1);
        this.bookResult1 = (Long) bookCmd1.getResult();
    }

    private void notConsumerBookEvent(Controller controller){
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

        BookEventCommand bookCmd1  = new BookEventCommand(
                1,
                1,
                10
        );
        controller.runCommand(bookCmd1);
        this.bookResult2 = (Long) bookCmd1.getResult();
    }

    private void bookEventNotEnoughTickets(Controller controller){
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

        BookEventCommand bookCmd1  = new BookEventCommand(
                1,
                1,
                60
        );
        controller.runCommand(bookCmd1);
        this.bookResult3 = (Long) bookCmd1.getResult();
    }

    @Test
    void testBookedEvent(){
        Controller controller = new Controller();
        bookEvent(controller);
        assertEquals(1,bookResult1);
    }

    @Test
    void testNonConsumerBookedEvent(){
        Controller controller = new Controller();
        notConsumerBookEvent(controller);
        assertNull(bookResult2);
    }

    @Test
    void testBookEventNotEnoughTickets(){
        Controller controller = new Controller();
        bookEventNotEnoughTickets(controller);
        assertNull(bookResult3);
    }

}
