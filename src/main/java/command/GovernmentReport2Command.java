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

    /**
     * Creates a GovernmentReport2 for the specified organisation
     * @param orgName - identifier of the {@link model.EntertainmentProvider} to provide record for
     */
    public GovernmentReport2Command(String orgName) {
        this.orgName = orgName;
        this.consumerListResult = new ArrayList<>();
    }

    public enum LogStatus {
        GOVERNMENT_REPORT2_NOT_LOGGED_IN,
        GOVERNMENT_REPORT2_USER_NOT_GOVERNMENT_REPRESENTATIVE,
        GOVERNMENT_REPORT2_NO_SUCH_ORGANISATION,
        GOVERNMENT_REPORT2_SUCCESS
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
        // To return all information about consumers who have bookings to a given entertainment provider's events,
        // we will take the following steps:
        // 1. Loop through all events and add events organised by the entertainment provider to "eventListResult"
        // 2. Loop through "eventListResult", and for each event:
        // 3. Loop through associated bookings, and add them to "bookingListResult".
        // 4. Loop through "bookingListResult" and add associated consumers to consumerListResult
        // 5. Check different conditions explained below and if any are true, withhold the consumerListResult,
        // and return null instead.

        List<Event> eventListResult = new ArrayList<>();
        List<Booking> bookingListResult = new ArrayList<>();

        // Loop through all active, ticketed events to find those organised by given organiser
        List<Event> allEvents = context.getEventState().getAllEvents();
        for (int i = 0; i < allEvents.size(); i++) {
            Event currEvent = allEvents.get(i);
            if ((currEvent.getOrganiser().getOrgName().equals(orgName))
                    && (currEvent.getStatus() == EventStatus.ACTIVE)
                    && (currEvent instanceof TicketedEvent)) {
                eventListResult.add(currEvent);
            }
        }
        // Loop through events to find bookings
        for (int i = 0; i < eventListResult.size(); i++) {
            Event currEvent = eventListResult.get(i);
            List<Booking> currBookingList = context.getBookingState().findBookingsByEventNumber(currEvent.getEventNumber());
            // Loop through bookings and add them to bookingListResult
            for (int j = 0; j < currBookingList.size(); j++) {
                bookingListResult.add(currBookingList.get(j));
            }
        }
        // Loop through list of bookings and get associated consumers
        for (int i = 0; i < bookingListResult.size(); i++) {
            this.consumerListResult.add(bookingListResult.get(i).getBooker());
        }

        // Now we will verify that none of the following conditions are true:
        // 1. That there is no logged in user
        // 2. That the logged in user is not a government representative
        // 3. That there is no such organisation registered
        // If these conditions are all false, logStatus will remain null, so in this case, the request is successful
        LogStatus logStatus = null;
        User currUser = context.getUserState().getCurrentUser();
        // Check condition 1:
        if (currUser == null){
            logStatus = LogStatus.GOVERNMENT_REPORT2_NOT_LOGGED_IN;
            Logger.getInstance().logAction(
                    "GovernmentReport2Command.execute",
                    LogStatus.GOVERNMENT_REPORT2_NOT_LOGGED_IN
            );
        }
        // Check condition 2:
        if (!(currUser instanceof GovernmentRepresentative)){
            logStatus = LogStatus.GOVERNMENT_REPORT2_USER_NOT_GOVERNMENT_REPRESENTATIVE;
            Logger.getInstance().logAction(
                    "GovernmentReport2Command.execute",
                    LogStatus.GOVERNMENT_REPORT2_USER_NOT_GOVERNMENT_REPRESENTATIVE
            );
        }
        // To check condition 3, we will set our flag to be false, then loop through all users, and if a match with
        // the given orgName is found, we update our flag to true.
        Map<String, User> allUsers = context.getUserState().getAllUsers();
        boolean isMatch = false;
        for (Map.Entry<String, User> entry : allUsers.entrySet()) {
            if (entry.getValue() instanceof EntertainmentProvider
                    && ((EntertainmentProvider)entry.getValue()).getOrgName().equals(orgName)){
                isMatch = true;
            }
        }
        // If isMatch is still false, then the given orgName did not match any of our registered users.
        if (!isMatch){
            logStatus = LogStatus.GOVERNMENT_REPORT2_NO_SUCH_ORGANISATION;
            Logger.getInstance().logAction(
                    "GovernmentReport2Command.execute",
                    LogStatus.GOVERNMENT_REPORT2_USER_NOT_GOVERNMENT_REPRESENTATIVE
            );
        }
        // In these cases, the consumerListResult should not be returned, so null is returned instead.
        if (logStatus != null || consumerListResult.size() == 0){
            this.consumerListResult = null;
        }
        else {
            logStatus = LogStatus.GOVERNMENT_REPORT2_SUCCESS;
            Logger.getInstance().logAction(
                    "GovernmentReport2Command.execute",
                    LogStatus.GOVERNMENT_REPORT2_SUCCESS
            );
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