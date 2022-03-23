package model;

public class SponsorshipRequest {

    private long requestNumber;
    private TicketedEvent event;
    private SponsorshipStatus sponsorshipStatus = SponsorshipStatus.PENDING;
    private Integer sponsoredPricePercent;
    private String sponsorAccountEmail;

    public SponsorshipRequest(long requestNumber, TicketedEvent event){
        this.requestNumber = requestNumber;
        this.event = event;
    }

    public long getRequestNumber(){
        return requestNumber;
    }

    public TicketedEvent getEvent(){
        return event;
    }

    public SponsorshipStatus getStatus(){
        return sponsorshipStatus;
    }

    public Integer getSponsoredPricePercent(){
        return sponsoredPricePercent;
    }

    public String getSponsorAccountEmail(){
        if (sponsorshipStatus == SponsorshipStatus.ACCEPTED) {
            return sponsorAccountEmail;
        }
        else return null;
    }

    public void accept(int percent, String sponsorAccountEmail){
        sponsorshipStatus = SponsorshipStatus.ACCEPTED;
        sponsoredPricePercent = percent;
        this.sponsorAccountEmail = sponsorAccountEmail;
    }

    public void reject(){
        sponsorshipStatus = SponsorshipStatus.REJECTED;
    }

}
