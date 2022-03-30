package command;

import controller.Context;
import model.Booking;
import model.TicketedEvent;

import java.util.List;

public class GetAvailablePerformanceTicketsCommand extends Object implements ICommand{

    private long eventNumber;
    private long performanceNumber;
    private LogStatus logStatus = null;
    private Integer availableTicketsResult;
    private Integer totalTakenTickets;

    public enum LogStatus {
        EVENT_DOES_NOT_EXIST,
        EVENT_NOT_TICKETED,
        EVENT_PERFORMANCE_DOES_NOT_EXIST,
        GET_TICKETS_SUCCESS
    }

    public GetAvailablePerformanceTicketsCommand(long eventNumber, long performanceNumber){
        this.eventNumber = eventNumber;
        this.performanceNumber = performanceNumber;
    }

    @Override
    public void execute(Context context) {
        if (!(context.getEventState().getAllEvents().contains(eventNumber))){
            this.logStatus = LogStatus.EVENT_DOES_NOT_EXIST;
        }
        if (!(context.getEventState().findEventByNumber(eventNumber) instanceof TicketedEvent)){
            this.logStatus = LogStatus.EVENT_NOT_TICKETED;
        }
        if (!(context.getEventState().findEventByNumber(eventNumber).getPerformances().containsKey(performanceNumber)) && logStatus != LogStatus.EVENT_NOT_TICKETED && logStatus != LogStatus.EVENT_DOES_NOT_EXIST){
            this.logStatus = LogStatus.EVENT_PERFORMANCE_DOES_NOT_EXIST;
        }
        if (logStatus == null){
            this.logStatus = LogStatus.GET_TICKETS_SUCCESS;
            Integer totalPotentialTickets = context.getEventState().findEventByNumber(eventNumber).getPerformanceByNumber(performanceNumber).getCapacityLimit();
            this.totalTakenTickets = 0;
            List<Booking> bookingListForEvent = context.getBookingState().findBookingsByEventNumber(eventNumber);
            for (int i = 0; i < bookingListForEvent.size(); i++){
                if (bookingListForEvent.get(i).getEventPerformance() == context.getEventState().findEventByNumber(eventNumber).getPerformanceByNumber(performanceNumber)){
                    this.totalTakenTickets = this.totalTakenTickets + bookingListForEvent.get(i).getNumTickets();
                }
            }
            this.availableTicketsResult = totalPotentialTickets - totalTakenTickets;
        }
    }

    @Override
    public Object getResult() {
        return null;
    }
}
