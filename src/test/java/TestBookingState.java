import model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testng.Assert;
import state.BookingState;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestBookingState {

    //The following test the functionality of the booking state methods
    //Note: that the system environment has to be created including the consumer, provider,event
    // and performance before some of the tests can be run
    //1. test the initial bookings list is empty
    //2. test that the create booking method correctly creates events
    //3. test that the initial booking number is 1
    //4. test that the booking number correctly increments
    //5. test that the bookings can be filtered by event number
    //6. test that if an event isnt the event number it is not returned by the find by event number
    //7. test that the bookings can be filtered by booking number
    //8. test that the findbyeventnumber returns a null list if no bookings have been made
    //9. test that the find by booking number method is null if no bookings created


    @Test
    @DisplayName("initialGetBookingsTest")
    void initialGetBookingsTest(){
        BookingState bookingState = new BookingState();
        assertTrue(bookingState.getBookings().isEmpty());
    }

    @Test
    @DisplayName("addBookingsThenGetBookingsTest")
    void addBookingsThenGetBookingsTest(){
        BookingState bookingState = new BookingState();
        Consumer testConsumer = new Consumer(
                "Euan",
                "euan@gmail.com",
                "077872172228",
                "password1234",
                "paymenteuan@gmail.com");
        EntertainmentProvider testEntertainmentProvider = new EntertainmentProvider(
                "Euan",
                "org",
                "paymentorg@gmail.com",
                "euan chalmers",
                "euanchalmers@gmail.com",
                "password",
                List.of("person1", "person2"),
                List.of("emailing@email.yahoo.com"));
        TicketedEvent testEvent = new TicketedEvent(
                1,
                testEntertainmentProvider,
                "tour de france",
                EventType.Sports,
                12,
                100);
        EventPerformance testPerformance = new EventPerformance(
                1,
                testEvent,
                "address",
                LocalDateTime.of(2030, 3, 21, 4, 20),
                LocalDateTime.of(2030, 3, 21, 7, 0),
                List.of("The usual"),
                true,
                true,
                false,
                100,
                200);

        bookingState.createBooking(testConsumer, testPerformance, 1, 12);

        List<Booking> bookings = bookingState.getBookings();
        assertEquals(1, bookings.size());
        assertEquals("euan@gmail.com", bookings.get(0).getBooker().getEmail());
    }

    @Test
    @DisplayName("tests the next booking number starts at 1")
    void testNextBookingNumberInitial(){
        BookingState bookingState = new BookingState();
        assertEquals(1, bookingState.getNextBookingNumber());
    }

    @Test
    @DisplayName("tests the next booking number increments correctly")
    void testNextBookingNumberIncrements() {
        BookingState bookingState = new BookingState();

        Consumer testConsumer = new Consumer(
                "Euan",
                "euan@gmail.com",
                "077872172228",
                "password1234",
                "paymenteuan@gmail.com");
        EntertainmentProvider testEntertainmentProvider = new EntertainmentProvider(
                "Euan",
                "org",
                "paymentorg@gmail.com",
                "euan chalmers",
                "euanchalmers@gmail.com",
                "password",
                List.of("person1", "person2"),
                List.of("emailing@email.yahoo.com"));
        TicketedEvent testEvent = new TicketedEvent(
                1,
                testEntertainmentProvider,
                "tour de france",
                EventType.Sports,
                12,
                200);
        EventPerformance testPerformance = new EventPerformance(
                1,
                testEvent,
                "address",
                LocalDateTime.of(2030, 3, 21, 4, 20),
                LocalDateTime.of(2030, 3, 21, 7, 0),
                List.of("The usual"),
                true,
                true,
                false,
                100,
                200);

        bookingState.createBooking(testConsumer, testPerformance, 1, 0);
        assertEquals(2, bookingState.getNextBookingNumber());
    }

    @Test
    @DisplayName("test find booking by booking no")
    public void testFindBookingByBookingNo() {
        BookingState bookingState = new BookingState();
        Consumer testConsumer = new Consumer(
                "Euan",
                "euan@gmail.com",
                "077872172228",
                "password1234",
                "paymenteuan@gmail.com");

        Consumer testConsumer2 = new Consumer(
                "Tom",
                "Tom@gmail.com",
                "077872172228",
                "passy test",
                "paymentTom@gmail.com");

        Consumer testConsumer3 = new Consumer(
                "Bill",
                "bill@gmail.com",
                "077872172228",
                "worstpassword",
                "paymentemail@gmail.com");

        EntertainmentProvider testEntertainmentProvider = new EntertainmentProvider(
                "Euan",
                "org",
                "paymentorg@gmail.com",
                "euan chalmers",
                "euanchalmers@gmail.com",
                "password",
                List.of("person1", "person2"),
                List.of("emailing@email.yahoo.com"));

        TicketedEvent testEvent = new TicketedEvent(
                1,
                testEntertainmentProvider,
                "tour de france",
                EventType.Sports,
                20,
                200);

        EventPerformance testPerformance = new EventPerformance(
                1,
                testEvent,
                "address",
                LocalDateTime.of(2030, 3, 21, 4, 20),
                LocalDateTime.of(2030, 3, 21, 7, 0),
                List.of("The usual"),
                true,
                true,
                false,
                100,
                200);

        bookingState.createBooking(testConsumer, testPerformance, 1, 20.00);
        bookingState.createBooking(testConsumer2, testPerformance, 2, 20.00);
        bookingState.createBooking(testConsumer3, testPerformance, 2, 40.00);

        Booking testBooking = new Booking(
                1,
                testConsumer,
                testPerformance,
                1,
                20.00,
                java.time.LocalDateTime.now());

        Booking testBookingTwo = new Booking(
                2,
                testConsumer,
                testPerformance,
                1,
                20.00,
                java.time.LocalDateTime.now());

        //test both an extreme booking number and a normal booking number
        assertEquals(testBooking.getBookingNumber(), bookingState.findBookingByNumber(1).getBookingNumber());
        assertEquals(testBookingTwo.getBookingNumber(), bookingState.findBookingByNumber(2).getBookingNumber());
    }

    @Test
    @DisplayName("test for find booking by event number")
    public void testFindBookingByEventNo() {
        BookingState bookingState = new BookingState();
        Consumer testConsumer = new Consumer(
                "Euan",
                "euan@gmail.com",
                "077872172228",
                "password1234",
                "paymenteuan@gmail.com");

        Consumer testConsumer2 = new Consumer(
                "Tom",
                "Tom@gmail.com",
                "077872172228",
                "passy test",
                "paymentTom@gmail.com");

        Consumer testConsumer3 = new Consumer(
                "Bill",
                "bill@gmail.com",
                "077872172228",
                "worstpassword",
                "paymentemail@gmail.com");

        EntertainmentProvider testEntertainmentProvider = new EntertainmentProvider(
                "Euan",
                "org",
                "paymentorg@gmail.com",
                "euan chalmers",
                "euanchalmers@gmail.com",
                "password",
                List.of("person1", "person2"),
                List.of("emailing@email.yahoo.com"));

        TicketedEvent testEvent = new TicketedEvent(
                1,
                testEntertainmentProvider,
                "tour de france",
                EventType.Sports,
                20,
                200);

        EventPerformance testPerformance = new EventPerformance(
                1,
                testEvent,
                "address",
                LocalDateTime.of(2030, 3, 21, 4, 20),
                LocalDateTime.of(2030, 3, 21, 7, 0),
                List.of("The usual"),
                true,
                true,
                false,
                100,
                200);

        bookingState.createBooking(testConsumer, testPerformance, 1, 20.00);
        bookingState.createBooking(testConsumer2, testPerformance, 2, 20.00);
        bookingState.createBooking(testConsumer3, testPerformance, 2, 40.00);

        Booking testBooking = new Booking(
                1,
                testConsumer,
                testPerformance,
                1,
                20.00,
                java.time.LocalDateTime.now());

        Booking testBookingTwo = new Booking(
                2,
                testConsumer,
                testPerformance,
                1,
                20.00,
                java.time.LocalDateTime.now());

        List<Booking> bookings = bookingState.findBookingsByEventNumber(1);
        assertEquals(3, bookingState.findBookingsByEventNumber(1).size());
        assertEquals("euan@gmail.com", bookings.get(0).getBooker().getEmail());
        assertEquals("Tom@gmail.com", bookings.get(1).getBooker().getEmail());
        assertEquals("bill@gmail.com", bookings.get(2).getBooker().getEmail());
    }

    @Test
    @DisplayName("test that the find by event number returns the correct bookings")
    public void testFindBookingByEventWithTwoEvents() {
        BookingState bookingState = new BookingState();
        Consumer testConsumer = new Consumer(
                "Euan",
                "euan@gmail.com",
                "077872172228",
                "password1234",
                "paymenteuan@gmail.com");

        Consumer testConsumer2 = new Consumer(
                "Tom",
                "Tom@gmail.com",
                "077872172228",
                "passy test",
                "paymentTom@gmail.com");

        Consumer testConsumer3 = new Consumer(
                "Bill",
                "bill@gmail.com",
                "077872172228",
                "worstpassword",
                "paymentemail@gmail.com");

        EntertainmentProvider testEntertainmentProvider = new EntertainmentProvider(
                "Euan",
                "org",
                "paymentorg@gmail.com",
                "euan chalmers",
                "euanchalmers@gmail.com",
                "password",
                List.of("person1", "person2"),
                List.of("emailing@email.yahoo.com"));

        TicketedEvent testEvent = new TicketedEvent(
                1,
                testEntertainmentProvider,
                "tour de france",
                EventType.Sports,
                20,
                200);

        TicketedEvent testEvent2 = new TicketedEvent(
                2,
                testEntertainmentProvider,
                "shakespear",
                EventType.Theatre,
                200,
                200);

        EventPerformance testPerformance = new EventPerformance(
                1,
                testEvent,
                "address",
                LocalDateTime.of(2030, 3, 21, 4, 20),
                LocalDateTime.of(2030, 3, 21, 7, 0),
                List.of("The usual"),
                true,
                true,
                false,
                100,
                200);

        EventPerformance testPerformance2 = new EventPerformance(
                1,
                testEvent2,
                "the globe",
                LocalDateTime.of(2030, 3, 21, 4, 20),
                LocalDateTime.of(2030, 3, 21, 7, 0),
                List.of("The usual"),
                true,
                true,
                false,
                100,
                200);

        bookingState.createBooking(testConsumer, testPerformance, 1, 20.00);
        bookingState.createBooking(testConsumer2, testPerformance2, 2, 20.00);
        bookingState.createBooking(testConsumer3, testPerformance, 2, 40.00);

        Booking testBooking = new Booking(
                1,
                testConsumer,
                testPerformance,
                1,
                20.00,
                java.time.LocalDateTime.now());

        Booking testBookingTwo = new Booking(
                2,
                testConsumer,
                testPerformance,
                1,
                20.00,
                java.time.LocalDateTime.now());

        List<Booking> bookings = bookingState.findBookingsByEventNumber(1);
        assertEquals(2, bookingState.findBookingsByEventNumber(1).size());
        assertEquals("euan@gmail.com", bookings.get(0).getBooker().getEmail());
        assertEquals("bill@gmail.com", bookings.get(1).getBooker().getEmail());
    }

    @Test
    @DisplayName("tests that the find by event number returns null if there are no bookings")
    void testFindBookingByEventNumberWithZeroBookings() {
        BookingState bookingState = new BookingState();

        EntertainmentProvider testEntertainmentProvider = new EntertainmentProvider(
                "Euan",
                "org",
                "paymentorg@gmail.com",
                "euan chalmers",
                "euanchalmers@gmail.com",
                "password",
                List.of("person1", "person2"),
                List.of("emailing@email.yahoo.com"));

        TicketedEvent testEvent = new TicketedEvent(
                1,
                testEntertainmentProvider,
                "tour de france",
                EventType.Sports,
                20,
                200);

        EventPerformance testPerformance = new EventPerformance(
                1,
                testEvent,
                "address",
                LocalDateTime.of(2030, 3, 21, 4, 20),
                LocalDateTime.of(2030, 3, 21, 7, 0),
                List.of("The usual"),
                true,
                true,
                false,
                100,
                200);

        assertTrue(bookingState.findBookingsByEventNumber(1).isEmpty());
    }

    @Test
    @DisplayName("find a booking when no bookings exist")
    void testfindbyBookingNoWithZeroBookings(){
        BookingState bookingState = new BookingState();
        assertNull(bookingState.findBookingByNumber(1));
    }
}