package state;

import model.Booking;
import model.Consumer;
import model.EventPerformance;
import model.User;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class BookingState implements IBookingState{
    private long nextBookingNumber;
    private List<Booking> bookings;
    public BookingState(){
        this.bookings = null;
        this.nextBookingNumber = 1;
    }
    public BookingState(IBookingState other){
        new BookingState();
//        this.nextBookingNumber = other.nextBookingNumber;
//        this.bookings = other.bookings;
    }

    @Override
    public Booking findBookingByNumber(long bookingNumber) {
        Booking correctBooking = null;
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getBookingNumber() == bookingNumber){
                correctBooking = bookings.get(i);
            }
        }
        return correctBooking;
    }

    @Override
    public List<Booking> findBookingsByEventNumber(long eventNumber) {
        List<Booking> correctBookings = null;
        for (int i = 0; i < bookings.size(); i++) {
            if(bookings.get(i).getEventPerformance().getEvent().getEventNumber()==eventNumber){
                correctBookings.add(bookings.get(i));
            }
        }
        return correctBookings;
    }

    @Override
    public Booking createBooking(Consumer booker, EventPerformance performance, int numTickets, double amountPaid) {
        Booking newBooking = new Booking(nextBookingNumber, booker, performance, numTickets, amountPaid, java.time.LocalDateTime.now());
        nextBookingNumber = nextBookingNumber + 1;
        // Add to booking state
        return newBooking;
    }
}
