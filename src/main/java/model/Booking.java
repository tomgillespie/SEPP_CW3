package model;

import java.time.LocalDateTime;

public class Booking {
    private long bookingNumber;
    private Consumer booker;
    private EventPerformance performance;
    private int numTickets;
    private double amountPaid;
    private LocalDateTime bookingDateTime;
    private BookingStatus bookingStatus = BookingStatus.Active;

    public Booking(long bookingNumber, Consumer booker, EventPerformance performance, int numTickets, double amountPaid, LocalDateTime bookingDateTime){
        this.bookingNumber = bookingNumber;
        this.booker = booker;
        this.performance = performance;
        this.numTickets = numTickets;
        this.amountPaid = amountPaid;
        this.bookingDateTime = bookingDateTime;
    }
    public long getBookingNumber(){
        return bookingNumber;
    }
    public BookingStatus getStatus(){
        return bookingStatus;
    }
    public Consumer getBooker(){
        return booker;
    }
    public EventPerformance getEventPerformance(){
        return performance;
    }
    public int getNumTickets(){
        return numTickets;
    }
    public double getAmountPaid(){
        return amountPaid;
    }
    public void cancelByConsumer(){
        bookingStatus = BookingStatus.CancelledByConsumer;
    }
    public void cancelPaymentFailed(){
        bookingStatus = BookingStatus.PaymentFailed;
    }
    public void cancelByProvider(){
        bookingStatus = BookingStatus.CancelledByProvider;
    }
}
