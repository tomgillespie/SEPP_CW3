package external;

import model.Event;
import model.EventPerformance;
import model.TicketedEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockEntertainmentProviderSystem implements EntertainmentProviderSystem{
    private String orgName;
    private String orgAddress;
    private Map<Long, Integer> eventMap;
    private Map<Long, Integer> bookingToTicketsMap;
    private Map<Long, Long> performanceMap;
    private Map<Long, Long> bookingToEventMap;
    public MockEntertainmentProviderSystem(String orgName, String orgAddress){
        this.orgName = orgName;
        this.orgAddress = orgAddress;
        this.eventMap = new HashMap<>();
        this.bookingToTicketsMap = new HashMap<>();
        this.bookingToEventMap = new HashMap<>();
        this.performanceMap = new HashMap<>();
    }

    @Override
    public void recordNewEvent(long eventNumber, String title, int numTickets) {
        this.eventMap.put(eventNumber, numTickets);
    }

    @Override
    public void cancelEvent(long eventNumber, String message) {
        this.eventMap.remove(eventNumber);
    }

    @Override
    public void recordNewPerformance(long eventNumber, long performanceNumber,
                                     LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.performanceMap.put(performanceNumber,eventNumber);
    }

    @Override
    public int getNumTicketsLeft(long eventNumber, long performanceNumber) { return eventMap.get(eventNumber); }

    @Override
    public void recordNewBooking(long eventNumber, long performanceNumber, long bookingNumber,
                                 String consumerName, String consumerEmail, int bookedTickets) {
        this.bookingToTicketsMap.put(bookingNumber, bookedTickets);
        this.bookingToEventMap.put(bookingNumber, eventNumber);

        // Update the number of tickets associated with a given event in the eventMap
        Integer totalRemainingTicketsAtEvent = eventMap.get(eventNumber);
        totalRemainingTicketsAtEvent = totalRemainingTicketsAtEvent - bookedTickets;
        eventMap.put(eventNumber, totalRemainingTicketsAtEvent);
    }

    // Not implemented inline with our requirements
    @Override
    public void cancelBooking(long bookingNumber) {

    }

    @Override
    public void recordSponsorshipAcceptance(long eventNumber, int sponsoredPricePercent) {

    }

    @Override
    public void recordSponsorshipRejection(long eventNumber) {

    }
}
