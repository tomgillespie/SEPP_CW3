
import model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import state.EventState;
import state.IEventState;
import model.EventType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestEventState {
    EventState eventState = new EventState();
    @Test
    @DisplayName("Get all events test")
    public void testAllEvents () {
        List<Event> allEvents = eventState.getAllEvents();
        assert (allEvents.size() == 0);
    }
    @Test
    @DisplayName("Test create non ticketed event")
    public void testCreateNonTicketedEvent () {
        EntertainmentProvider organiser = new EntertainmentProvider(
                "TestOrganiser",
                "home",
                "@email.com",
                "important person",
                "sss@email.com",
                "password12345:)",
                List.of("person1", "person2"),
                List.of("emailing@email.yahoo.com"));
        eventState.createNonTicketedEvent(organiser, "The games",
                EventType.Sports);
        assertEquals(1, eventState.getAllEvents().size());
    }
    @Test
    @DisplayName("Test the ticketed event booking works")
    public void testCreateTicketedEvent () {
        EntertainmentProvider organiser = new EntertainmentProvider(
                "TestOrganiser",
                "home",
                "@email.com",
                "important person",
                "sss@email.com",
                "password12345:)",
                List.of("person1", "person2"),
                List.of("emailing@email.yahoo.com"));
        eventState.createTicketedEvent(organiser, "The games",
                EventType.Sports, 11.99, 100);
        assertEquals(1, eventState.getAllEvents().size());
    }
    @Test
    @DisplayName("Test get next event number gives event map size +1")
    public void testGetNextEventNumber () {
        assertEquals(1, eventState.getNextEventNumber());
    }
    @Test
    @DisplayName("test get next event number iterates correctly")
    public void testGetNextEventNumberIterates () {
        EventState eventState = new EventState();
        EntertainmentProvider organiser = new EntertainmentProvider(
                "TestOrganiser",
                "home",
                "@email.com",
                "important person",
                "sss@email.com",
                "password12345:)",
                List.of("person1", "person2"),
                List.of("emailing@email.yahoo.com"));
        eventState.createTicketedEvent(organiser, "The games",
                EventType.Sports, 11.99, 100);
        assertEquals(1, eventState.getAllEvents().size());
        eventState.createTicketedEvent(organiser, "The games 2.0",
                EventType.Sports, 20, 2000);
        assertEquals(2, eventState.getAllEvents().size());
        assertEquals(3, eventState.getNextEventNumber());
    }
    @Test
    @DisplayName("Test get next performance gives event map size +1")
    public void testGetNextPerformanceNumber () {
        EventState eventState = new EventState();
        assertEquals(1, eventState.getNextPerformanceNumber());
    }
    @Test
    @DisplayName("create event performance test")
    public void testCreateEventPerformance () {
        EventState eventState = new EventState();
//must create an event first to assign the performace too
        EntertainmentProvider organiser = new EntertainmentProvider(
                "TestOrganiser",
                "home",
                "@email.com",
                "important person",
                "sss@email.com",
                "password12345:)",
                List.of("person1", "person2"),
                List.of("emailing@email.yahoo.com"));
        eventState.createNonTicketedEvent(organiser, "The games",
                EventType.Sports);
        assertEquals(1, eventState.getAllEvents().size());
        eventState.createEventPerformance(eventState.getAllEvents().get(0),
                "olyimpic park",
                LocalDateTime.now().plusMonths(1).plusDays(1),
                LocalDateTime.now().plusMonths(1).plusDays(1).plusHours(6),
                List.of("david", "james"),
                true,
                true,
                false,
                100,
                200);
        assertEquals(1,
                eventState.getAllEvents().get(0).getPerformances().size());
    }
    @Test
    @DisplayName("find event by event number test, testing with mutiple events")
            public void testGetEventByNumber () {
            EventState eventState = new EventState();
//must create an event first to assign the performance too
            EntertainmentProvider organiser = new EntertainmentProvider(
            "TestOrganiser",
            "home",
            "@email.com",
            "important person",
            "sss@email.com",
            "password12345:)",
            List.of("person1", "person2"),
            List.of("emailing@email.yahoo.com"));
            eventState.createNonTicketedEvent(organiser, "The games",
            EventType.Sports);
eventState.createTicketedEvent(organiser, "Formula 1",
    EventType.Sports, 11.99, 100);
eventState.createNonTicketedEvent(organiser, "Royal Variety Show",
    EventType.Dance);
    assertEquals(3, eventState.getAllEvents().size());
    TicketedEvent testEvent = new TicketedEvent(
            2,
            organiser,
            "Formula 1",
            EventType.Sports,
            11.99,
            100);
    Event theEvent = eventState.findEventByNumber(2);
    assertTrue(theEvent.getTitle().equals(testEvent.getTitle()));
    assertTrue(theEvent.getOrganiser().equals(testEvent.getOrganiser()));
}
    @Test
    @DisplayName("test find event number with number on limit of the array")
    public void testGetEventByNumberLimit () {
        EventState eventState = new EventState();
//must create an event first to assign the performance too
        EntertainmentProvider organiser = new EntertainmentProvider(
                "TestOrganiser",
                "home",
                "@email.com",
                "important person",
                "sss@email.com",
                "password12345:)",
                List.of("person1", "person2"),
                List.of("emailing@email.yahoo.com"));
        eventState.createNonTicketedEvent(organiser, "The games",
                EventType.Sports);
        eventState.createTicketedEvent(organiser, "Formula 1",
                EventType.Sports, 11.99, 100);
        eventState.createNonTicketedEvent(organiser, "Royal Variety Show",
                EventType.Dance);
        assertEquals(3, eventState.getAllEvents().size());
        NonTicketedEvent testEvent = new NonTicketedEvent(
                3,
                organiser,
                "Royal Variety Show",
                EventType.Dance);
        Event theEvent = eventState.findEventByNumber(3);
        assertTrue(theEvent.getTitle().equals(testEvent.getTitle()));
        assertTrue(theEvent.getOrganiser().equals(testEvent.getOrganiser()));
    }
@Test
@DisplayName("test find event number with event number outiwth the range of array")
        public void testGetEventByNumberOutwithLimit () {
        EventState eventState = new EventState();
//must create an event first to assign the performance too
        EntertainmentProvider organiser = new EntertainmentProvider(
        "TestOrganiser",
        "home",
        "@email.com",
        "important person",
        "sss@email.com",
        "password12345:)",
        List.of("person1", "person2"),
        List.of("emailing@email.yahoo.com"));
        eventState.createNonTicketedEvent(organiser, "The games",
        EventType.Sports);
eventState.createTicketedEvent(organiser, "Formula 1",
        EventType.Sports, 11.99, 100);
        eventState.createNonTicketedEvent(organiser, "Royal Variety Show",
        EventType.Dance);
        assertEquals(3, eventState.getAllEvents().size());
        assertNull( eventState.findEventByNumber(4));
        }
}
