package command;

import controller.Context;
import external.EntertainmentProviderSystem;
import logging.Logger;
import model.*;

import java.time.LocalDateTime;
import java.util.List;

public class BookEventCommand implements ICommand{
    private long eventNumber;
    private long performanceNumber;
    private int numTicketsRequested;
//    private LogStatus logStatus;
    private Long uniqueBookingNumber;

    public BookEventCommand(long eventNumber, long performanceNumber, int numTicketsRequested){
        this.eventNumber = eventNumber;
        this.performanceNumber = performanceNumber;
        this.numTicketsRequested = numTicketsRequested;
        this.uniqueBookingNumber = null;
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
    public void execute(Context context) {
        LogStatus logStatus = null;
        User currUser = context.getUserState().getCurrentUser();
        List<Event> allEvents = context.getEventState().getAllEvents();
        Event givenEvent = context.getEventState().findEventByNumber(eventNumber);
        EventPerformance currEventPerformance = givenEvent.getPerformanceByNumber(performanceNumber);
        Integer numTicketsLeft = givenEvent.getOrganiser().getProviderSystem()
                .getNumTicketsLeft(eventNumber, performanceNumber);
        if (!(currUser instanceof Consumer)) {
            logStatus = LogStatus.BOOK_EVENT_USER_NOT_CONSUMER;
//            this.logStatus = LogStatus.BOOK_EVENT_USER_NOT_CONSUMER;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_USER_NOT_CONSUMER);
        }
        if (!(allEvents.contains(givenEvent))) {
            logStatus = LogStatus.BOOK_EVENT_EVENT_NOT_FOUND;
//            this.logStatus = LogStatus.BOOK_EVENT_EVENT_NOT_FOUND;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_EVENT_NOT_FOUND);
        }
        if (!(givenEvent instanceof TicketedEvent)) {
            logStatus = LogStatus.BOOK_EVENT_NOT_A_TICKETED_EVENT;
//            this.logStatus = LogStatus.BOOK_EVENT_NOT_A_TICKETED_EVENT;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_NOT_A_TICKETED_EVENT);
        }
        if (numTicketsRequested < 1) {
            logStatus = LogStatus.BOOK_EVENT_INVALID_NUM_TICKETS;
//            this.logStatus = LogStatus.BOOK_EVENT_INVALID_NUM_TICKETS;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_INVALID_NUM_TICKETS);
        }
        if (!(givenEvent.getPerformances().containsKey(performanceNumber))) {
            logStatus = LogStatus.BOOK_EVENT_PERFORMANCE_NOT_FOUND;
//            this.logStatus = LogStatus.BOOK_EVENT_PERFORMANCE_NOT_FOUND;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_PERFORMANCE_NOT_FOUND);
        }
        if (!(givenEvent.getPerformanceByNumber(performanceNumber)
                .getEndDateTime().isAfter(java.time.LocalDateTime.now()))) {
            logStatus = LogStatus.BOOK_EVENT_ALREADY_OVER;
//            this.logStatus = LogStatus.BOOK_EVENT_ALREADY_OVER;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_ALREADY_OVER);
        }
        if (numTicketsLeft < numTicketsRequested) {
            logStatus = LogStatus.BOOK_EVENT_NOT_ENOUGH_TICKETS_LEFT;
//            this.logStatus = LogStatus.BOOK_EVENT_NOT_ENOUGH_TICKETS_LEFT;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_NOT_ENOUGH_TICKETS_LEFT);
        }
        double ticketPrice = ((TicketedEvent) givenEvent).getOriginalTicketPrice();
        double amountPaid = ticketPrice * numTicketsRequested;
        String buyerAccountEmail = currUser.getPaymentAccountEmail();
        String sellerAccountEmail = givenEvent.getOrganiser().getPaymentAccountEmail();
        if (!(context.getPaymentSystem().processPayment(buyerAccountEmail, sellerAccountEmail, amountPaid))) {
//            this.logStatus = LogStatus.BOOK_EVENT_PAYMENT_FAILED;
            logStatus = LogStatus.BOOK_EVENT_PAYMENT_FAILED;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_PAYMENT_FAILED);
        }
        if (!(givenEvent.getStatus() == EventStatus.ACTIVE)) {
//            this.logStatus = LogStatus.BOOK_EVENT_EVENT_NOT_ACTIVE;
            logStatus = LogStatus.BOOK_EVENT_EVENT_NOT_ACTIVE;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_EVENT_NOT_ACTIVE);
        }
        if (logStatus == null) {
            this.uniqueBookingNumber = context.getBookingState().getNextBookingNumber();
            // Create booking
            context.getBookingState().createBooking(
                    (Consumer) currUser,
                    currEventPerformance,
                    numTicketsRequested,
                    amountPaid
            );
//          this.logStatus = LogStatus.BOOK_EVENT_SUCCESS;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_SUCCESS);
            // Record booking in the external entertainment provider system
            givenEvent.getOrganiser().getProviderSystem().recordNewBooking(
                    eventNumber,
                    performanceNumber,
                    uniqueBookingNumber,
                    ((Consumer) currUser).getName(),
                    currUser.getEmail(),
                    numTicketsRequested);
            Booking latestBooking = context.getBookingState().findBookingByNumber(uniqueBookingNumber);
            // Add booking to consumer's list of bookings
            ((Consumer) (currUser)).addBooking(latestBooking);
        }
    }

    @Override
    public Object getResult() {
        return uniqueBookingNumber;
    }
}
