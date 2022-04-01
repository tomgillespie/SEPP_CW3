package command;

import controller.Context;
import external.EntertainmentProviderSystem;
import model.*;

import java.time.LocalDateTime;
import java.util.List;

public class BookEventCommand implements ICommand{
    private long eventNumber;
    private long performanceNumber;
    private int numTicketsRequested;
    private LogStatus logStatus;
    private Long uniqueBookingNumber;

    public BookEventCommand(long eventNumber, long performanceNumber, int numTicketsRequested){
        this.eventNumber = eventNumber;
        this.performanceNumber = performanceNumber;
        this.numTicketsRequested = numTicketsRequested;
    }

    public enum LogStatus {
        BOOK_EVENT_SUCCESS,
        BOOK_EVENT_USER_NOT_CONSUMER,
        BOOK_EVENT_NOT_A_TICKETED_EVENT,
        BOOK_EVENT_EVENT_NOT_ACTIVE,
        BOOK_EVENT_ALREADY_OVER,
        BOOK_EVENT_INVALID_NUM_TICKETS,
        BOOK_EVENT_NOT_ENOUGH_TICKETS_LEFT,
        BOOK_EVENT_PAYMENT_FAILED,
        BOOK_EVENT_EVENT_NOT_FOUND,
        BOOK_EVENT_PERFORMANCE_NOT_FOUND,
    }

    @Override
    public void execute(Context context){
        User currUser = context.getUserState().getCurrentUser();
        List<Event> allEvents = context.getEventState().getAllEvents();
        Event givenEvent = context.getEventState().findEventByNumber(eventNumber);
        EventPerformance currEventPerformance = context.getEventState().findEventByNumber(eventNumber).getPerformanceByNumber(performanceNumber);
        Integer numTicketsLeft = context.getEventState().findEventByNumber(eventNumber).getOrganiser().getProviderSystem().getNumTicketsLeft(eventNumber, performanceNumber);
        if (!(currUser instanceof Consumer)){
            this.logStatus = LogStatus.BOOK_EVENT_USER_NOT_CONSUMER;
        }
        if (!(allEvents.contains(eventNumber))){
            this.logStatus = LogStatus.BOOK_EVENT_EVENT_NOT_FOUND;
        }
        if (!(givenEvent instanceof TicketedEvent)){
            this.logStatus = LogStatus.BOOK_EVENT_NOT_A_TICKETED_EVENT;
        }
        if (numTicketsRequested < 1){
            this.logStatus = LogStatus.BOOK_EVENT_INVALID_NUM_TICKETS;
        }
        if (!(givenEvent.getPerformances().containsKey(performanceNumber))){
            this.logStatus = LogStatus.BOOK_EVENT_PERFORMANCE_NOT_FOUND;
        }
        if (!(givenEvent.getPerformanceByNumber(performanceNumber).getEndDateTime().isAfter(java.time.LocalDateTime.now()))){
            this.logStatus = LogStatus.BOOK_EVENT_ALREADY_OVER;
        }
        if (numTicketsLeft < numTicketsRequested){
            this.logStatus = LogStatus.BOOK_EVENT_NOT_ENOUGH_TICKETS_LEFT;
        }
        double ticketPrice = ((TicketedEvent)givenEvent).getOriginalTicketPrice();
        double amountPaid = ticketPrice*numTicketsRequested;
        String buyerAccountEmail = currUser.getPaymentAccountEmail();
        String sellerAccountEmail = givenEvent.getOrganiser().getPaymentAccountEmail();
        if (!(context.getPaymentSystem().processPayment(buyerAccountEmail, sellerAccountEmail, amountPaid))){
            this.logStatus = LogStatus.BOOK_EVENT_PAYMENT_FAILED;
        }
        if (!(givenEvent.getStatus() == EventStatus.ACTIVE)){
            this.logStatus = LogStatus.BOOK_EVENT_EVENT_NOT_ACTIVE;
        }
        if (logStatus == null){
            this.uniqueBookingNumber = context.getBookingState().getNextBookingNumber();
            context.getBookingState().createBooking((Consumer) currUser, currEventPerformance, numTicketsRequested, amountPaid);
            this.logStatus = LogStatus.BOOK_EVENT_SUCCESS;
        }
    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.BOOK_EVENT_SUCCESS){
            return uniqueBookingNumber;
        }
        else return null;
    }
}
