import command.*;
import controller.Controller;
import logging.Logger;
import model.*;
import org.junit.jupiter.api.*;

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

    private static void loginGovernmentRepresentative(Controller controller) {
        controller.runCommand(new LoginCommand("margaret.thatcher@gov.uk", "The Good times  "));
    }


    // In order to create an event, it is necessary to:
    // 1. Register an entertainment provider
    // 2. Login the entertainment provider
    // 3. Create event - ticketed/nonticketed

    // The following tests test expected behaviour of the CreateEvent, CreateTicketedEvent and //
    // the NonTicketedEvent commands - it is expected that Consumers, Government Representatives //
    // and logged out users should not be able to create events //

    @Test
    @DisplayName("CreateSingleTicketedEventTest")
    void createSingleTicketedEventTest(){
        Controller controller = new Controller();
        RegisterEntertainmentProviderCommand cmd1 = new
                RegisterEntertainmentProviderCommand(
                "bowling club",
                "the bowladrome",
                "bowling@ed.ac.uk",
                "barry bowling",
                "barry@ed.ac.uk",
                "10 pin king",
                List.of("strike", "spare"),
                List.of("spare@ed.ac.uk"));
        controller.runCommand(cmd1);
        User register1 = (User) cmd1.getResult();
        CreateTicketedEventCommand cmd2 = new CreateTicketedEventCommand(
                "BOWLS",
                EventType.Sports,
                10,
                100,
                false
        );
        controller.runCommand(cmd2);
        Long resultingEventNumber = cmd2.getResult();

        assertEquals(1, resultingEventNumber);
    }

    @Test
    @DisplayName("CreateSingleNonTicketedEventTest")
    void createSingleNonTicketedEventTest(){
        Controller controller = new Controller();
        RegisterEntertainmentProviderCommand cmd1 = new
                RegisterEntertainmentProviderCommand(
                "bowling club",
                "the bowladrome",
                "bowling@ed.ac.uk",
                "barry bowling",
                "barry@ed.ac.uk",
                "10 pin king",
                List.of("strike", "spare"),
                List.of("spare@ed.ac.uk"));
        controller.runCommand(cmd1);
        User register1 = (User) cmd1.getResult();
        CreateNonTicketedEventCommand cmd2 = new CreateNonTicketedEventCommand(
                "Free bowling event",
                EventType.Sports
        );
        controller.runCommand(cmd2);
        Long resultingEventNumber = cmd2.getResult();

        assertEquals(1, resultingEventNumber);
    }

    @Test
    @DisplayName("ConsumerCreateTicketedEventTest")
    void consumerCreateTicketedEventTest(){
        Controller controller = new Controller();
        RegisterConsumerCommand regCmd = new RegisterConsumerCommand(
                "Tom",
                "tom@ed.ac.uk",
                "01234",
                "pass",
                "cash@gmail.com"
        );
        controller.runCommand(regCmd);
        User register1 = (User) regCmd.getResult();
        CreateTicketedEventCommand cmd2 = new CreateTicketedEventCommand(
                "BOWLS",
                EventType.Sports,
                10,
                100,
                false
        );
        controller.runCommand(cmd2);
        Long resultingEventNumber = cmd2.getResult();
        assertNull(resultingEventNumber);
    }

    @Test
    @DisplayName("ConsumerCreateNonTicketedEventTest")
    void consumerCreateNonTicketedEventTest(){
        Controller controller = new Controller();
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
        Long eventNumberResult = eventCmd9.getResult();
        assertNull(eventNumberResult);
    }

    @Test
    @DisplayName("LoggedOutUserCreateTicketedEventTest")
    void loggedOutUserCreateTicketedEventTest(){
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
        EntertainmentProvider entProvider = (EntertainmentProvider) regCmd1.getResult();
        controller.runCommand(new LogoutCommand());
        CreateNonTicketedEventCommand eventCmd10 = new CreateNonTicketedEventCommand(
                "Royal mile busking",
                EventType.Music
        );
        controller.runCommand(eventCmd10);
        Long eventNumberResult = eventCmd10.getResult();
        assertNull(eventNumberResult);
    }

    @Test
    @DisplayName("GovernmentRepresentativeCreateTicketedEventTest")
    void govRepCreateTicketedEventTest(){
        Controller controller = new Controller();
        loginGovernmentRepresentative(controller);
        CreateNonTicketedEventCommand eventCmd10 = new CreateNonTicketedEventCommand(
                "Royal mile busking",
                EventType.Music
        );
        controller.runCommand(eventCmd10);
        Long eventNumberResult = eventCmd10.getResult();
        assertNull(eventNumberResult);
    }

    // The following methods aid to set up more complex testing scenarios //

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



    // The following tests test more complex scenarios, such as creating several events //


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
}
