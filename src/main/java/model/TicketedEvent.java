package model;

public class TicketedEvent extends Event{

    private double ticketPrice;
    private int numTickets;
    private SponsorshipRequest sponsorshipRequest;
    private SponsorshipStatus sponsorshipStatus;

    public TicketedEvent(long eventNumber, EntertainmentProvider organiser, String title, EventType type, double ticketPrice, int numTickets) {
        super(eventNumber, organiser, title, type);
        this.ticketPrice = ticketPrice;
        this.numTickets = numTickets;
    }

    public String toString(){
        return getTitle();
    }

    public double getOriginalTicketPrice(){
        return ticketPrice;
    }

    public double getDiscountedTicketPrice(){
        if (sponsorshipStatus == SponsorshipStatus.ACCEPTED){
            return ticketPrice* sponsorshipRequest.getSponsoredPricePercent();
        }
        else return ticketPrice;
    }

    public int getNumTickets(){
        return numTickets;
    }

    public String getSponsorAccountEmail(){
        if (sponsorshipStatus == SponsorshipStatus.ACCEPTED){
            return getSponsorAccountEmail();
        }
        else return null;
    }

    public boolean isSponsored(){
        if (sponsorshipStatus == SponsorshipStatus.ACCEPTED){
            return true;
        }
        else return false;
    }

    public void setSponsorshipRequest(SponsorshipRequest sponsorshipRequest){
        this.sponsorshipRequest = sponsorshipRequest;
    }

}
