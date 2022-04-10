package state;

import model.Booking;
import model.Consumer;
import model.EventPerformance;
import model.User;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookingState implements IBookingState{
    private long nextBookingNumber;
    private ArrayList<Booking> bookings;
    private Booking correctBooking;

    public BookingState(){
        this.bookings = new ArrayList<>();
        this.nextBookingNumber = 1;
    }
    public BookingState(IBookingState other){
        this.bookings = new ArrayList<Booking>(other.getBookings());
        this.nextBookingNumber = other.getNextBookingNumber();
    }

    @Override
    public List<Booking> getBookings(){
        return bookings;
    }

    @Override
    public long getNextBookingNumber(){
        return nextBookingNumber;
    }

    @Override
    public Booking findBookingByNumber(long bookingNumber) {
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getBookingNumber() == bookingNumber){
                this.correctBooking = bookings.get(i);
            }
        }
        return correctBooking;
    }

    @Override
    public List<Booking> findBookingsByEventNumber(long eventNumber) {
        List<Booking> correctBookings = new ArrayList<>();
        for (int i = 0; i < bookings.size(); i++) {
            if(bookings.get(i).getEventPerformance().getEvent().getEventNumber()==eventNumber){
                correctBookings.add(bookings.get(i));
            }
        }
        return correctBookings;
    }

    @Override
    public Booking createBooking(Consumer booker, EventPerformance performance, int numTickets, double amountPaid) {
        Booking newBooking = new Booking(nextBookingNumber,
                booker,
                performance,
                numTickets,
                amountPaid,
                java.time.LocalDateTime.now());
        this.bookings.add(newBooking);
        nextBookingNumber = nextBookingNumber + 1;
        // Add to booking state
        return newBooking;
    }
}
