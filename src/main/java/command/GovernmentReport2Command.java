package command;

import controller.Context;
import logging.Logger;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The {@link GovernmentReport2Command} allows a {@link model.GovernmentRepresentative} to request a list of {@link model.Consumer}s that have
 * active {@link model.Booking}s for any {@link model.Event}s of a given {@link model.EntertainmentProvider}. The command applies for the currently
 * logged-in {@link model.User}.
 */
public class GovernmentReport2Command extends Object implements ICommand {
    private String orgName;
    private List<Consumer> consumerListResult;
    private List<Event> eventListResult;
    private List<Booking> bookingListResult;

    /**
     * Creates a GovernmentReport2 for the specified organisation
      * @param orgName - identifier of the {@link model.EntertainmentProvider} to provide record for
     */
    public GovernmentReport2Command(String orgName) {
        this.orgName = orgName;
        this.eventListResult = new ArrayList<>();
        this.bookingListResult = new ArrayList<>();
        this.consumerListResult = new ArrayList<>();
    }

    public enum LogStatus {
        INVALID_ORG_NAME,
        MATCHING_ORG_NAME,
        USER_NOT_GOV_REP
    }

    /**
     * Verifies that:
     * \n The orgName refers to a registered entertainment provider
     * \n The {@link model.User} must be a {@link model.GovernmentRepresentative}, and logged in
     *
      * @param context - object that provides access to global application state
     */

    @Override
    public void execute(Context context) {
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

        LogStatus logStatus = null;
        Map<String, User> allUsers = context.getUserState().getAllUsers();

        for (Map.Entry<String, User> entry : allUsers.entrySet()) {
            // If user is an entertainment provider and their organisation name matches
            if ((entry.getValue() instanceof EntertainmentProvider && ((EntertainmentProvider)entry.getValue()).getOrgName().equals(orgName))){
                logStatus = LogStatus.MATCHING_ORG_NAME;
                Logger.getInstance().logAction("GovernmentReport2Command.execute", LogStatus.MATCHING_ORG_NAME);
            }
        }
        User loggedInUser = context.getUserState().getCurrentUser();
        if (!(loggedInUser instanceof GovernmentRepresentative)){
            logStatus = LogStatus.USER_NOT_GOV_REP;
            Logger.getInstance().logAction("GovernmentReport2Command.execute", LogStatus.USER_NOT_GOV_REP);
        }

        if (consumerListResult.size() == 0 || logStatus != LogStatus.MATCHING_ORG_NAME){
            this.consumerListResult = null;
        }
    }

    /**
     *
      * @return A list of {@link model.Consumer}s that have active {@link model.Booking}s at the
     *  {@link model.EntertainmentProvider}s {@link model.Event}s if successful, and null otherwise
     */

    @Override
    public List<Consumer> getResult() {
        if (consumerListResult == null) {
            System.out.println("List is null");
        }
        return consumerListResult;
    }
}
