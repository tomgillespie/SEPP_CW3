package command;

import controller.Context;
import model.EntertainmentProvider;
import model.Event;
import model.EventPerformance;
import model.User;

import java.time.LocalDateTime;
import java.util.List;

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
    private LogStatus logStatus = null;

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

    public AddEventPerformanceCommand(long eventNumber, String venueAddress, LocalDateTime startDateTime, LocalDateTime endDateTime, List<String> performerNames, boolean hasSocialDistancing, boolean hasAirFiltration, boolean isOutdoors, int capacityLimit, int venueSize){
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
    }

    @Override
    public void execute(Context context) {
        if (startDateTime.isAfter(endDateTime)){
            this.logStatus = LogStatus.ADD_PERFORMANCE_START_AFTER_END;
        }
        if (capacityLimit < 1){
            this.logStatus = LogStatus.ADD_PERFORMANCE_CAPACITY_LESS_THAN_1;
        }
        if (venueSize < 1){
            this.logStatus = LogStatus.ADD_PERFORMANCE_VENUE_SIZE_LESS_THAN_1;
        }
        if (!(context.getUserState().getCurrentUser() == null)){
            this.logStatus = LogStatus.ADD_PERFORMANCE_USER_NOT_LOGGED_IN;
        }
        if (!(context.getUserState().getCurrentUser() instanceof EntertainmentProvider)){
            this.logStatus = LogStatus.ADD_PERFORMANCE_USER_NOT_ENTERTAINMENT_PROVIDER;
        }
        if (!(context.getEventState().getAllEvents().contains(eventNumber))){
            this.logStatus = LogStatus.ADD_PERFORMANCE_EVENT_NOT_FOUND;
        }
        User currUser = context.getUserState().getCurrentUser();
        User eventOrganiser = context.getEventState().findEventByNumber(eventNumber).getOrganiser();
        if (!(currUser.equals(eventOrganiser)) && logStatus!= LogStatus.ADD_PERFORMANCE_USER_NOT_LOGGED_IN){
            this.logStatus = LogStatus.ADD_PERFORMANCE_USER_NOT_EVENT_ORGANISER;
        }
        List<Event> allEvents = context.getEventState().getAllEvents();
        Event currEvent = context.getEventState().findEventByNumber(eventNumber);
        for (int i = 0; i < allEvents.size(); i++){
            if (allEvents.get(i).getTitle().equals(currEvent.getTitle())){
                for (int j = 0; j < allEvents.get(i).getPerformances().size(); j++){
                    EventPerformance currPerformance = allEvents.get(i).getPerformances().get(j);
                    if (currPerformance.getStartDateTime().equals(startDateTime) && (currPerformance.getEndDateTime().equals(endDateTime))) {
                        this.logStatus = LogStatus.ADD_PERFORMANCE_EVENTS_WITH_SAME_TITLE_CLASH;
                        break;
                    }
                }
            }
        }
        if (logStatus == null){
            this.logStatus = LogStatus.ADD_PERFORMANCE_SUCCESS;
            eventPerformanceResult = context.getEventState().createEventPerformance(context.getEventState().findEventByNumber(eventNumber), venueAddress, startDateTime, endDateTime, performerNames, hasSocialDistancing, hasAirFiltration, isOutdoors, capacityLimit, venueSize);
            context.getEventState().findEventByNumber(eventNumber).addPerformance(eventPerformanceResult);
        }
    }

    @Override
    public Object getResult() {
        if (logStatus == LogStatus.ADD_PERFORMANCE_SUCCESS){
            return eventPerformanceResult;
        }
        else return false;
    }
}
