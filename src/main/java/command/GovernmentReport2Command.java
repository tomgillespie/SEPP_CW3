package command;

import controller.Context;
import logging.Logger;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class GovernmentReport2Command extends Object implements ICommand {
    private String orgName;
    private List<Consumer> consumerListResult;
    private List<Event> eventListResult;
    private List<Booking> bookingListResult;
//    private LogStatus logStatus;

    public GovernmentReport2Command(String orgName) {
        this.orgName = orgName;
        this.eventListResult = new ArrayList<>();
        this.bookingListResult = new ArrayList<>();
        this.consumerListResult = new ArrayList<>();
    }

    public enum LogStatus {
        INVALID_ORG_NAME
    }

    @Override
    public void execute(Context context) {
        LogStatus logStatus = null;
        if (orgName == null) {
//            this.logStatus = LogStatus.INVALID_ORG_NAME;
            logStatus = LogStatus.INVALID_ORG_NAME;
            Logger.getInstance().logAction("GovernmentReport2Command", LogStatus.INVALID_ORG_NAME);
        }
        List<Event> allEvents = context.getEventState().getAllEvents();
        // Loop through all active, ticketed events to find those organised by given organiser
        for (int i = 0; i < allEvents.size(); i++) {
            Event currEvent = allEvents.get(i);
            if ((currEvent.getOrganiser().getOrgName().equals(orgName))
                    && (currEvent.getStatus() == EventStatus.ACTIVE)
                    && (currEvent instanceof TicketedEvent)) {
                this.eventListResult.add(currEvent);
            }
        }
        // Loop through events to find bookings
        for (int i = 0; i < eventListResult.size(); i++) {
            Event currEvent = eventListResult.get(i);
            List<Booking> currBookingList = context.getBookingState().findBookingsByEventNumber(currEvent.getEventNumber());
            // Loop through bookings and add them to bookingListResult
            for (int j = 0; j < currBookingList.size(); j++) {
                this.bookingListResult.add(currBookingList.get(j));
            }
        }
        // Loop through list of bookings and get associated consumers
        for (int i = 0; i < bookingListResult.size(); i++) {
            this.consumerListResult.add(bookingListResult.get(i).getBooker());
        }
    }

    @Override
    public List<Consumer> getResult() {
        if (consumerListResult == null) {
            System.out.println("List is null");
        }
        return consumerListResult;
    }
}
