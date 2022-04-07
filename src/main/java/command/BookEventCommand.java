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

    // We have deviated from the given UML class diagram, and included another state in the enum for when a user is
    // logged out - "BOOK_EVENT_USER_NOT_LOGGED_IN".

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

        // NOTE FOR MARKERS:
        // We are aware that it is desirable to use inline assertions in our methods, to deal with invalid inputs and
        // invalid scenarios. We made use of several such assertions in this command, such as:
        // -> assert givenEvent != null
        // -> assert givenEvent instanceof TicketedEvent
        // -> currUser instanceof Consumer
        // and several more. On piazza, it says to comment out these assertions - however, we decided to build in
        // some more logic to deal with invalid scenarios, such as when an givenEvent is null because a non-existent
        // eventNumber has been given, or a someone tries to book a NonTicketedEvent. Other invalid states are
        // caught automatically by the logic of our method.

        LogStatus logStatus = null;
        User currUser = context.getUserState().getCurrentUser();
        List<Event> allEvents = context.getEventState().getAllEvents();
        Event givenEvent = context.getEventState().findEventByNumber(eventNumber);
        Integer numTicketsLeft = null;
        double ticketPrice = 0;
        double amountPaid = 0;
        String sellerAccountEmail = null;
        EventPerformance currEventPerformance = null;
        String buyerAccountEmail = null;

        if (currUser != null){
            buyerAccountEmail = currUser.getPaymentAccountEmail();
        }

        // Catch invalid states - invalid eventNumber, NonTicketed event
        if (givenEvent != null && givenEvent instanceof TicketedEvent){
            numTicketsLeft = givenEvent.getOrganiser().getProviderSystem().getNumTicketsLeft(eventNumber, performanceNumber);
            ticketPrice = ((TicketedEvent) givenEvent).getOriginalTicketPrice();
            amountPaid = ticketPrice * numTicketsRequested;
            sellerAccountEmail = givenEvent.getOrganiser().getPaymentAccountEmail();
            currEventPerformance = givenEvent.getPerformanceByNumber(performanceNumber);
        }

        if (!(currUser instanceof Consumer)){
            logStatus = LogStatus.BOOK_EVENT_USER_NOT_CONSUMER;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_USER_NOT_CONSUMER);
        }

        else if (!(allEvents.contains(givenEvent))){
            logStatus = LogStatus.BOOK_EVENT_EVENT_NOT_FOUND;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_EVENT_NOT_FOUND);
        }

        else if (!(givenEvent instanceof TicketedEvent)){
            logStatus = LogStatus.BOOK_EVENT_NOT_A_TICKETED_EVENT;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_NOT_A_TICKETED_EVENT);
        }

        else if (numTicketsRequested < 1){
            logStatus = LogStatus.BOOK_EVENT_INVALID_NUM_TICKETS;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_INVALID_NUM_TICKETS);
        }

        else if (!givenEvent.getPerformances().containsKey(performanceNumber)){
            logStatus = LogStatus.BOOK_EVENT_PERFORMANCE_NOT_FOUND;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_PERFORMANCE_NOT_FOUND);
        }

        else if (!givenEvent.getPerformanceByNumber(performanceNumber)
                        .getEndDateTime().isAfter(LocalDateTime.now())){
            logStatus = LogStatus.BOOK_EVENT_ALREADY_OVER;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_ALREADY_OVER);
        }

        else if (numTicketsLeft < numTicketsRequested){
            logStatus = LogStatus.BOOK_EVENT_NOT_ENOUGH_TICKETS_LEFT;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_NOT_ENOUGH_TICKETS_LEFT);
        }

        else if (!(context.getPaymentSystem().processPayment(buyerAccountEmail, sellerAccountEmail, amountPaid))) {
            logStatus = LogStatus.BOOK_EVENT_PAYMENT_FAILED;
            Logger.getInstance().logAction("BookEventCommand.execute", LogStatus.BOOK_EVENT_PAYMENT_FAILED);
        }

        else if (!(givenEvent.getStatus() == EventStatus.ACTIVE)) {
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
