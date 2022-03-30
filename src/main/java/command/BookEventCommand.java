package command;

import controller.Context;
import external.EntertainmentProviderSystem;
import model.Consumer;
import model.EventStatus;
import model.TicketedEvent;
import model.User;

import java.time.LocalDateTime;

public class BookEventCommand implements ICommand{
    private long eventNumber;
    private long performanceNumber;
    private int numTicketsRequested;
    private LogStatus logStatus;
    private long uniqueBookingNumber;

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
        if (!(context.getUserState().getCurrentUser() instanceof Consumer)){
            this.logStatus = LogStatus.BOOK_EVENT_USER_NOT_CONSUMER;
        }
        if (!(context.getEventState().getAllEvents().contains(eventNumber))){
            this.logStatus = LogStatus.BOOK_EVENT_EVENT_NOT_FOUND;
        }
        if (!(context.getEventState().findEventByNumber(context.getEventState().getNextEventNumber()) instanceof TicketedEvent)){
            this.logStatus = LogStatus.BOOK_EVENT_NOT_A_TICKETED_EVENT;
        }
        if (numTicketsRequested < 1){
            this.logStatus = LogStatus.BOOK_EVENT_INVALID_NUM_TICKETS;
        }
        if (!(context.getEventState().findEventByNumber(eventNumber).getPerformances().containsKey(performanceNumber))){
            this.logStatus = LogStatus.BOOK_EVENT_PERFORMANCE_NOT_FOUND;
        }
        if (!(context.getEventState().findEventByNumber(eventNumber).getPerformanceByNumber(performanceNumber).getEndDateTime().isAfter(java.time.LocalDateTime.now()))){
            this.logStatus = LogStatus.BOOK_EVENT_ALREADY_OVER;
        }
//        if (requested no. tickets unavailable according to EntertainmentProviderSystem){
//            this.logStatus = LogStatus.BOOK_EVENT_NOT_ENOUGH_TICKETS_LEFT;
//        }
//        if (booking payment via payment system unsuccessful){
//            this.logStatus = LogStatus.BOOK_EVENT_PAYMENT_FAILED;
//        }
        if (!(context.getEventState().findEventByNumber(eventNumber).getStatus() == EventStatus.ACTIVE)){
            this.logStatus = LogStatus.BOOK_EVENT_EVENT_NOT_ACTIVE;
        }
        else{
            this.logStatus = LogStatus.BOOK_EVENT_SUCCESS;
            this.uniqueBookingNumber = context.getBookingState().getNextBookingNumber();
        }
    }

//    @Override
//    public void execute(Context context) {
//        if (context.getUserState().getCurrentUser().getClass() == Consumer.class){} // Currently logged in user is a consumer
//        if (context.getUserState().getCurrentUser() instanceof Consumer){} // Currently logged in user is a consumer
//        if (context.getEventState().getAllEvents().contains(context.getEventState().getNextEventNumber())){} // Event number corresponds to an existing event
//        if (context.getEventState().getAllEvents().contains(eventNumber)){} // Event number corresponds to an existing event
//        if (context.getEventState().findEventByNumber(context.getEventState().getNextEventNumber()) instanceof TicketedEvent){} // Event is a ticketed event
//        if (context.getEventState().findEventByNumber(eventNumber) instanceof TicketedEvent){} // Event is a ticketed event
//        if (numTicketsRequested > 0){} // Number of requested tickets is not less than 1
//        if (context.getEventState().findEventByNumber(eventNumber).getPerformances().contains(performanceNumber)){} // Performance number corresponds to an existing performance of the event
//        if (context.getEventState().findEventByNumber(eventNumber).getPerformanceByNumber(performanceNumber).getEndDateTime().isAfter(java.time.LocalDateTime.now())){}
////        if (numTicketsRequested < EntertainmentProviderSystem number of tickets available ){} //The requested number of tickets is still available according to the organisers Entertainment provider system
////        if booking payment via payment method succeeds
////        Book event
//        this.uniqueBookingNumber = context.getBookingState().getNextBookingNumber();
//    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.BOOK_EVENT_SUCCESS){
            return uniqueBookingNumber;
        }
        else return null;
    }
}
