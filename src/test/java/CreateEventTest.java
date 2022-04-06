import command.*;
import controller.Controller;
import logging.Logger;
import model.EntertainmentProvider;
import model.EventType;
import model.NonTicketedEvent;
import model.TicketedEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CreateEventTest {
    @BeforeEach
    void printTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @AfterEach
    void clearLogs() {
        Logger.getInstance().clearLog();
        System.out.println("---");
    }





















    // In order to create an event, it is necessary to:
    // 1. Register an entertainment provider
    // 2. Login the entertainment provider
    // 3. Create event - ticketed/nonticketed

    private EntertainmentProvider entProvider1;
    private long eventNumber1;
    private long eventNumber2;
    private long eventNumber3;
    private long eventNumber4;
    private long eventNumber5;
    private long eventNumber6;
    private long eventNumber7;
    private long eventNumber8;
    private Long eventNumber9;
    private Long eventNumber10;

    private void createFringeProviderWith4TicketedEvents(Controller controller){
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
        this.entProvider1 = (EntertainmentProvider) regCmd1.getResult();
        controller.runCommand(new LogoutCommand());
        controller.runCommand(new LoginCommand("pmathieson@ed.ac.uk", "hongkong"));

        CreateTicketedEventCommand eventCmd1 = new CreateTicketedEventCommand(
                "Fire breathing",
                EventType.Theatre,
                50,
                15,
                false
        );
        controller.runCommand(eventCmd1);
        this.eventNumber1 = eventCmd1.getResult();

        CreateTicketedEventCommand eventCmd2 = new CreateTicketedEventCommand(
                "Comedy",
                EventType.Theatre,
                500,
                15,
                false
        );
        controller.runCommand(eventCmd2);
        this.eventNumber2 = eventCmd2.getResult();

        // Deliberately create same event twice
        CreateTicketedEventCommand eventCmd3 = new CreateTicketedEventCommand(
                "Comedy",
                EventType.Theatre,
                500,
                15,
                false
        );
        controller.runCommand(eventCmd3);
        this.eventNumber3 = eventCmd3.getResult();

        CreateTicketedEventCommand eventCmd4 = new CreateTicketedEventCommand(
                "Juggling",
                EventType.Sports,
                55,
                25,
                false
        );
        controller.runCommand(eventCmd4);
        this.eventNumber4 = eventCmd4.getResult();
        controller.runCommand(new LogoutCommand());
    }

    private void createFringeProviderWith4NonTicketedEvents(Controller controller){
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
        this.entProvider1 = (EntertainmentProvider) regCmd1.getResult();
        controller.runCommand(new LogoutCommand());
        controller.runCommand(new LoginCommand("pmathieson@ed.ac.uk", "hongkong"));

        CreateNonTicketedEventCommand eventCmd5 = new CreateNonTicketedEventCommand(
                "Royal mile busking",
                EventType.Music
        );
        controller.runCommand(eventCmd5);
        this.eventNumber5 = eventCmd5.getResult();

        CreateNonTicketedEventCommand eventCmd6 = new CreateNonTicketedEventCommand(
                "Middle meadow walk busking",
                EventType.Music
        );
        controller.runCommand(eventCmd6);
        this.eventNumber6 = eventCmd6.getResult();

        CreateNonTicketedEventCommand eventCmd7 = new CreateNonTicketedEventCommand(
                "Leith busking",
                EventType.Music
        );
        controller.runCommand(eventCmd7);
        this.eventNumber7 = eventCmd7.getResult();

        CreateNonTicketedEventCommand eventCmd8 = new CreateNonTicketedEventCommand(
                "Portobello run",
                EventType.Sports
        );
        controller.runCommand(eventCmd8);
        this.eventNumber8 = eventCmd8.getResult();
    }

    private void consumerTriesEventCreation(Controller controller){
        RegisterConsumerCommand registeredConsumer = new RegisterConsumerCommand(
                "John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        );
        controller.runCommand(registeredConsumer);
        controller.runCommand(new LogoutCommand());
        controller.runCommand(new LoginCommand("jbiggson1@hotmail.co.uk", "jbiggson2"));
        CreateNonTicketedEventCommand eventCmd9 = new CreateNonTicketedEventCommand(
                "John's dance event",
                EventType.Dance
        );
        controller.runCommand(eventCmd9);
        this.eventNumber9 = eventCmd9.getResult();
    }

    private void loggedOutUserTriesEventCreation(Controller controller){
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
        this.entProvider1 = (EntertainmentProvider) regCmd1.getResult();
        controller.runCommand(new LogoutCommand());

        CreateNonTicketedEventCommand eventCmd10 = new CreateNonTicketedEventCommand(
                "Royal mile busking",
                EventType.Music
        );
        controller.runCommand(eventCmd10);
        this.eventNumber10 = eventCmd10.getResult();
    }


    @Test
    void test4TicketedEvents(){
        Controller controller = new Controller();
        createFringeProviderWith4TicketedEvents(controller);

        TicketedEvent ticketedEvent1 = new TicketedEvent(
                1,
                entProvider1,
                "Fire breathing",
                EventType.Theatre,
                15,
                50
        );

        TicketedEvent ticketedEvent2 = new TicketedEvent(
                2,
                entProvider1,
                "Comedy",
                EventType.Theatre,
                15,
                500
        );

        TicketedEvent ticketedEvent3 = new TicketedEvent(
                3,
                entProvider1,
                "Comedy",
                EventType.Theatre,
                15,
                500
        );

        TicketedEvent ticketedEvent4 = new TicketedEvent(
                4,
                entProvider1,
                "Juggling",
                EventType.Sports,
                25,
                55
        );

        assertEquals(eventNumber1, ticketedEvent1.getEventNumber());
        assertEquals(eventNumber2, ticketedEvent2.getEventNumber());
        assertEquals(eventNumber3, ticketedEvent3.getEventNumber());
        assertEquals(eventNumber4, ticketedEvent4.getEventNumber());
    }

    @Test
    void test4NonTicketedEvents(){
        Controller controller = new Controller();
        createFringeProviderWith4NonTicketedEvents(controller);

        NonTicketedEvent nonTicketedEvent1 = new NonTicketedEvent(
                1,
                entProvider1,
                "Royal mile busking",
                EventType.Music
        );

        NonTicketedEvent nonTicketedEvent2 = new NonTicketedEvent(
                2,
                entProvider1,
                "Middle meadow walk busking",
                EventType.Music
        );

        NonTicketedEvent nonTicketedEvent3 = new NonTicketedEvent(
                3,
                entProvider1,
                "Leith busking",
                EventType.Music
        );

        NonTicketedEvent nonTicketedEvent4 = new NonTicketedEvent(
                4,
                entProvider1,
                "Portobello run",
                EventType.Sports
        );

        assertEquals(eventNumber5, nonTicketedEvent1.getEventNumber());
        assertEquals(eventNumber6, nonTicketedEvent2.getEventNumber());
        assertEquals(eventNumber7, nonTicketedEvent3.getEventNumber());
        assertEquals(eventNumber8, nonTicketedEvent4.getEventNumber());
    }

    @Test
    void testConsumerEventCreation(){
        Controller controller = new Controller();
        consumerTriesEventCreation(controller);
        // Consumer should not be able to create event, so null is returned
        assertNull(eventNumber9);
    }

    @Test
    void testLoggedOutUserEventCreation(){
        Controller controller = new Controller();
        loggedOutUserTriesEventCreation(controller);
        assertNull(eventNumber10);
    }
}
