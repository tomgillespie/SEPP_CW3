package command;

import controller.Context;
import logging.Logger;
import model.Booking;
import model.Event;
import model.TicketedEvent;

import java.util.List;

public class GetAvailablePerformanceTicketsCommand extends Object implements ICommand{

    private long eventNumber;
    private long performanceNumber;
    private Integer availableTicketsResult;

    public enum LogStatus {
        EVENT_DOES_NOT_EXIST,
        EVENT_NOT_TICKETED,
        EVENT_PERFORMANCE_DOES_NOT_EXIST,
        GET_TICKETS_SUCCESS
    }

    public GetAvailablePerformanceTicketsCommand(long eventNumber, long performanceNumber){
        this.eventNumber = eventNumber;
        this.performanceNumber = performanceNumber;
        this.availableTicketsResult = null;
    }

    @Override
    public void execute(Context context) {
        LogStatus logStatus = null;
        List<Event> allEvents = context.getEventState().getAllEvents();
        Event particularEvent = context.getEventState().findEventByNumber(eventNumber);

        if (!(allEvents.contains(eventNumber))){
            logStatus = LogStatus.EVENT_DOES_NOT_EXIST;
            Logger.getInstance().logAction("GetAvailablePerformanceTicketsCommand.execute", LogStatus.EVENT_DOES_NOT_EXIST);
        }
        if (!(particularEvent instanceof TicketedEvent)){
            logStatus = LogStatus.EVENT_NOT_TICKETED;
            Logger.getInstance().logAction("GetAvailablePerformanceTicketsCommand.execute", LogStatus.EVENT_NOT_TICKETED);
        }
        if (!(particularEvent.getPerformances().containsKey(performanceNumber))
                && logStatus != LogStatus.EVENT_NOT_TICKETED
                && logStatus != LogStatus.EVENT_DOES_NOT_EXIST){
            logStatus = LogStatus.EVENT_PERFORMANCE_DOES_NOT_EXIST;
            Logger.getInstance().logAction("GetAvailablePerformanceTicketsCommand.execute", LogStatus.EVENT_PERFORMANCE_DOES_NOT_EXIST);
        }
        if (logStatus == null){
            logStatus = LogStatus.GET_TICKETS_SUCCESS;
            Logger.getInstance().logAction("GetAvailablePerformanceTicketsCommand.execute", LogStatus.GET_TICKETS_SUCCESS);
            Integer totalPotentialTickets = particularEvent.getPerformanceByNumber(performanceNumber).getCapacityLimit();
            Integer totalTakenTickets = 0;
            List<Booking> bookingListForEvent = context.getBookingState().findBookingsByEventNumber(eventNumber);
            for (int i = 0; i < bookingListForEvent.size(); i++){
                if (bookingListForEvent.get(i).getEventPerformance()
                        == particularEvent.getPerformanceByNumber(performanceNumber)){
                    totalTakenTickets = totalTakenTickets + bookingListForEvent.get(i).getNumTickets();
                }
            }
            this.availableTicketsResult = totalPotentialTickets - totalTakenTickets;
        }
    }

    @Override
    public Object getResult() {
        return availableTicketsResult;
    }
}
