import command.*;
import controller.Controller;
import model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import state.BookingState;
import state.EventState;
import state.UserState;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestBookingState {

    // getBookings tests:

    @Test
    @DisplayName("initalGetBookingsTest")
    void initialGetBookingsTest(){
        BookingState bookingState = new BookingState();
        assertEquals(true, bookingState.getBookings().isEmpty());
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
        EntertainmentProvider testEntertainmentProvider = new
                EntertainmentProvider(
                "Euan",
                "org",
                "paymentorg@gmail.com",
                "euan chalmers",
                "euanchalmers@gmail.com",
                "password",
                List.of("person1", "person2"),
                List.of("emailing@email.yahoo.com"));
        NonTicketedEvent testEvent = new NonTicketedEvent(1,
                testEntertainmentProvider,
                "tour de france",
                EventType.Sports);
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

        List<Booking> bookings = bookingState.getBookings();

        assertEquals(1, bookings.size());
        assertEquals("euan@gmail.com", bookings.get(0).getBooker().getEmail());
    }

    // getNextBookingNumber tests:

    // findBookingsByNumber tests:

    // findBookingsByEventNumber tests:

    // createBooking tests:

}