package command;

import controller.Context;
import logging.Logger;
import model.EntertainmentProvider;
import model.Event;
import model.EventPerformance;
import model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AddEventPerformanceCommand extends Object implements ICommand{

    private Long eventNumber;
    private String venueAddress;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private List<String> performerNames;
    private boolean hasSocialDistancing;
    private boolean hasAirFiltration;
    private boolean isOutdoors;
    private int capacityLimit;
    private int venueSize;
    private EventPerformance eventPerformanceResult;

    public enum LogStatus{
        ADD_PERFORMANCE_SUCCESS,
        ADD_PERFORMANCE_START_AFTER_END,
        ADD_PERFORMANCE_CAPACITY_LESS_THAN_1,
        ADD_PERFORMANCE_VENUE_SIZE_LESS_THAN_1,
        ADD_PERFORMANCE_EVENTS_WITH_SAME_TITLE_CLASH,
        ADD_PERFORMANCE_USER_NOT_LOGGED_IN,
        ADD_PERFORMANCE_USER_NOT_ENTERTAINMENT_PROVIDER,
        ADD_PERFORMANCE_EVENT_NOT_FOUND,
        ADD_PERFORMANCE_USER_NOT_EVENT_ORGANISER
    }

    public AddEventPerformanceCommand(
            long eventNumber,
            String venueAddress,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            List<String> performerNames,
            boolean hasSocialDistancing,
            boolean hasAirFiltration,
            boolean isOutdoors,
            int capacityLimit,
            int venueSize
    ){
        this.eventNumber = eventNumber;
        this.venueAddress = venueAddress;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.performerNames = performerNames;
        this.hasSocialDistancing = hasSocialDistancing;
        this.hasAirFiltration = hasAirFiltration;
        this.isOutdoors = isOutdoors;
        this.capacityLimit = capacityLimit;
        this.venueSize = venueSize;
        this.eventPerformanceResult = null;
    }

    @Override
    public void execute(Context context) {
        LogStatus logStatus = null;
        User currUser = context.getUserState().getCurrentUser();
        List<Event> allEvents = context.getEventState().getAllEvents();
        Event particularEvent = context.getEventState().findEventByNumber(eventNumber);
        User eventOrganiser = context.getEventState().findEventByNumber(eventNumber).getOrganiser();
        if (startDateTime.isAfter(endDateTime)) {

            logStatus = LogStatus.ADD_PERFORMANCE_START_AFTER_END;
            Logger.getInstance().logAction("AddEventPerformanceCommand.execute", LogStatus.ADD_PERFORMANCE_START_AFTER_END);
        }
        if (capacityLimit < 1) {
//            this.logStatus = LogStatus.ADD_PERFORMANCE_CAPACITY_LESS_THAN_1;
            logStatus = LogStatus.ADD_PERFORMANCE_CAPACITY_LESS_THAN_1;
            Logger.getInstance().logAction("AddEventPerformanceCommand.execute", LogStatus.ADD_PERFORMANCE_CAPACITY_LESS_THAN_1);
        }
        if (venueSize < 1) {
//            this.logStatus = LogStatus.ADD_PERFORMANCE_VENUE_SIZE_LESS_THAN_1;
            logStatus = LogStatus.ADD_PERFORMANCE_VENUE_SIZE_LESS_THAN_1;
            Logger.getInstance().logAction("AddEventPerformanceCommand.execute", LogStatus.ADD_PERFORMANCE_VENUE_SIZE_LESS_THAN_1);
        }
        if (currUser == null) {
//            this.logStatus = LogStatus.ADD_PERFORMANCE_USER_NOT_LOGGED_IN;
            logStatus = LogStatus.ADD_PERFORMANCE_USER_NOT_LOGGED_IN;
            Logger.getInstance().logAction("AddEventPerformanceCommand.execute", LogStatus.ADD_PERFORMANCE_USER_NOT_LOGGED_IN);
        }
        if (!(currUser instanceof EntertainmentProvider)) {
//            this.logStatus = LogStatus.ADD_PERFORMANCE_USER_NOT_ENTERTAINMENT_PROVIDER;
            logStatus = LogStatus.ADD_PERFORMANCE_USER_NOT_ENTERTAINMENT_PROVIDER;
            Logger.getInstance().logAction("AddEventPerformanceCommand.execute", LogStatus.ADD_PERFORMANCE_USER_NOT_ENTERTAINMENT_PROVIDER);
        }
        if (!(allEvents.contains(particularEvent))) {
//            this.logStatus = LogStatus.ADD_PERFORMANCE_EVENT_NOT_FOUND;
            logStatus = LogStatus.ADD_PERFORMANCE_EVENT_NOT_FOUND;
            Logger.getInstance().logAction("AddEventPerformanceCommand.execute", LogStatus.ADD_PERFORMANCE_EVENT_NOT_FOUND);
        }
        if (!(currUser.equals(eventOrganiser)) && currUser != null) {
//            this.logStatus = LogStatus.ADD_PERFORMANCE_USER_NOT_EVENT_ORGANISER;
            logStatus = LogStatus.ADD_PERFORMANCE_USER_NOT_EVENT_ORGANISER;
            Logger.getInstance().logAction("AddEventPerformanceCommand.execute", LogStatus.ADD_PERFORMANCE_USER_NOT_EVENT_ORGANISER);
        }


//        if (!(currUser.equals(eventOrganiser)) && logStatus!= LogStatus.ADD_PERFORMANCE_USER_NOT_LOGGED_IN){
//            this.logStatus = LogStatus.ADD_PERFORMANCE_USER_NOT_EVENT_ORGANISER;
//            Logger.getInstance().logAction("AddEventPerformanceCommand.execute", LogStatus.ADD_PERFORMANCE_USER_NOT_EVENT_ORGANISER);
//        }
        // Check for title clash by looping through events, then through performances of each event
        for (int i = 0; i < allEvents.size(); i++) {
            if (allEvents.get(i).getTitle().equals(particularEvent.getTitle())) {
                Map<Long, EventPerformance> performanceMap = allEvents.get(i).getPerformances();
                for (Map.Entry<Long, EventPerformance> entry : performanceMap.entrySet()) {
                    EventPerformance currPerformance = entry.getValue();
                    if (currPerformance.getStartDateTime().equals(startDateTime)
                            && currPerformance.getEndDateTime().equals(endDateTime)) {
                        logStatus = LogStatus.ADD_PERFORMANCE_EVENTS_WITH_SAME_TITLE_CLASH;
//                        this.logStatus = LogStatus.ADD_PERFORMANCE_EVENTS_WITH_SAME_TITLE_CLASH;
                        Logger.getInstance().logAction("AddEventPerformanceCommand.execute", LogStatus.ADD_PERFORMANCE_EVENTS_WITH_SAME_TITLE_CLASH);
                        break;
                    }
                }
            }
        }

        if (logStatus == null) {
            logStatus = LogStatus.ADD_PERFORMANCE_SUCCESS;
//          this.logStatus = LogStatus.ADD_PERFORMANCE_SUCCESS;
            Logger.getInstance().logAction("AddEventPerformanceCommand.execute", LogStatus.ADD_PERFORMANCE_SUCCESS);
            // Create event performance
            this.eventPerformanceResult = context.getEventState().createEventPerformance(
                    particularEvent,
                    venueAddress,
                    startDateTime,
                    endDateTime,
                    performerNames,
                    hasSocialDistancing,
                    hasAirFiltration,
                    isOutdoors,
                    capacityLimit,
                    venueSize
            );
            // Record the performance in the external system entertainment provider system
            ((EntertainmentProvider) currUser).getProviderSystem().recordNewPerformance(
                    eventNumber,
                    eventPerformanceResult.getPerformanceNumber(),
                    startDateTime,
                    endDateTime
            );
        }
    }

    @Override
    public Object getResult() {
        return eventPerformanceResult;
    }
}
