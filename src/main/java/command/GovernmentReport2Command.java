package command;

import controller.Context;
import model.*;

import java.util.List;

public class GovernmentReport2Command extends Object implements ICommand{
    private String orgName;
    private List<Consumer> consumerListResult;
    private List<Event> eventListResult;
    private List<Booking> bookingListResult;
    private LogStatus logStatus;

    public GovernmentReport2Command(String orgName){
        this.orgName = orgName;
    }

    public enum LogStatus{
        CONSUMER_REPORT_SUCCESS,
        INVALID_ORG_NAME
    }

    @Override
    public void execute(Context context) {
        if (orgName == null){
            this.logStatus = LogStatus.INVALID_ORG_NAME;
        }
        // Loop through all active, ticketed events to find those organised by given organiser
        for(int i = 0; i < context.getEventState().getAllEvents().size(); i++){
            Event currEvent = context.getEventState().getAllEvents().get(i);
            if ((currEvent.getOrganiser().getOrgName() == orgName) && (currEvent.getStatus() == EventStatus.ACTIVE) && (currEvent instanceof TicketedEvent)){
                this.eventListResult.add(currEvent);
            }
        }
        // Loop through events to find bookings
        for(int i = 0; i < eventListResult.size(); i++){
            Event currEvent = eventListResult.get(i);
            List<Booking> currBookingList = context.getBookingState().findBookingsByEventNumber(currEvent.getEventNumber());
            // Loop through bookings and add them to bookingListResult
            for (int j = 0; j < currBookingList.size(); j++){
                this.bookingListResult.add(currBookingList.get(j));
            }
        }
        // Loop through list of bookings and get associated consumers
        for (int i = 0; i < bookingListResult.size(); i++){
            this.consumerListResult.add(bookingListResult.get(i).getBooker());
        }
        // If any consumers have been added to the list, return success
        if (consumerListResult.size()>0){
            this.logStatus = LogStatus.CONSUMER_REPORT_SUCCESS;
        }
    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.CONSUMER_REPORT_SUCCESS){
            return consumerListResult;
        }
        else return null;
    }
}
