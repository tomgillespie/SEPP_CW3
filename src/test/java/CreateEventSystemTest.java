import command.*;
import controller.Controller;
import logging.Logger;
import model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledIfSystemProperties;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CreateEventSystemTest {
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

    // The following tests test several the following scenarios: //

    // Category 1 - Simple event creation tests: //
    // 1. Creating a single TicketedEvent as described above
    // 2. Creating a single NonTicketedEvent as described above
    // 3. Consumer should not be able to create TicketedEvent
    // 4. Consumer should not be able to create NonTicketedEvent
    // 5. Logged out Entertainment Provider should not be able to create event
    // 6. Government Representative should not be able to create event
    // 7. Should not be able to create events with null inputs

    // Category 3 - More complex event creation scenarios //
    // 1. Multiple TicketedEvents can be created //
    // 2. Multiple NonTicketedEvents can be created //
    // 3. Performances can be added to events //

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

    @Test
    @DisplayName("CreateEventWithNullInputsTest")
    void createEventWithNullInputsTest(){
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
                null,
                EventType.Sports,
                10,
                100,
                false
        );
        controller.runCommand(cmd2);
        Long resultingEventNumber = cmd2.getResult();

        CreateTicketedEventCommand cmd3 = new CreateTicketedEventCommand(
                null,
                null,
                10,
                100,
                false
        );
        controller.runCommand(cmd3);
        Long resultingEventNumber2 = cmd3.getResult();

        assertNull(resultingEventNumber);
        assertNull(resultingEventNumber2);
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


    // The following tests test more complex scenarios, such as creating several events or creating events
    // and then adding performances //


    @Test
    @DisplayName("create4TicketedEventsTest")
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
    @DisplayName("create4NonTicketedEventsTest")
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
    @DisplayName("CreateEventAddPerformancesTest")
    void createEventsAndPerfomancesTest(){
        Controller controller = new Controller();
        controller.runCommand(new RegisterEntertainmentProviderCommand(
                "Cinema Conglomerate",
                "Global Office, International Space Station",
                "$$$@there'sNoEmailValidation.wahey!",
                "Mrs Representative",
                "odeon@cineworld.com",
                "F!ghT th3 R@Pture",
                List.of("Dr Strangelove"),
                List.of("we_dont_get_involved@cineworld.com")
        ));
        CreateTicketedEventCommand eventCmd1 = new CreateTicketedEventCommand(
                "James Bond",
                EventType.Movie,
                100,
                15.75,
                false
        );
        controller.runCommand(eventCmd1);
        long eventNumber1 = eventCmd1.getResult();
        AddEventPerformanceCommand perfCmd1 = new AddEventPerformanceCommand(
                eventNumber1,
                "EDINBURGH",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(4),
                List.of("Daniel Craig"),
                false,
                false,
                false,
                50,
                25
        );
        controller.runCommand(perfCmd1);
        EventPerformance performance1 = (EventPerformance) perfCmd1.getResult();
        AddEventPerformanceCommand perfCmd2 = new AddEventPerformanceCommand(
                eventNumber1,
                "MEXICO CITY",
                LocalDateTime.now().plusHours(5),
                LocalDateTime.now().plusHours(8),
                List.of("Daniel Craig"),
                false,
                false,
                false,
                50,
                1
        );
        controller.runCommand(perfCmd2);
        EventPerformance performance2 = (EventPerformance) perfCmd2.getResult();
        AddEventPerformanceCommand perfCmd3 = new AddEventPerformanceCommand(
                eventNumber1,
                "LIMA",
                LocalDateTime.now().plusHours(10),
                LocalDateTime.now().plusHours(13),
                List.of("Daniel Craig"),
                false,
                false,
                false,
                50,
                25
        );
        controller.runCommand(perfCmd3);
        EventPerformance performance3 = (EventPerformance) perfCmd3.getResult();

        // Testing performance1
        assertEquals("EDINBURGH", performance1.getVenueAddress());
        assertEquals(1, performance1.getEvent().getEventNumber());
        assertEquals(25, performance1.getVenueSize());
        assertEquals(1, performance1.getPerformanceNumber());

        // Testing performance2
        assertEquals("MEXICO CITY", performance2.getVenueAddress());
        assertEquals(1, performance2.getEvent().getEventNumber());
        assertEquals(1, performance2.getVenueSize());
        assertEquals(2, performance2.getPerformanceNumber());

        // Testing performance3
        assertEquals("LIMA", performance3.getVenueAddress());
        assertEquals(1, performance3.getEvent().getEventNumber());
        assertEquals(25, performance3.getVenueSize());
        assertEquals(3, performance3.getPerformanceNumber());


    }

}
