package state;

import model.SponsorshipRequest;
import model.SponsorshipStatus;
import model.TicketedEvent;

import java.util.List;

public class SponsorshipState implements ISponsorshipState{
    private List<SponsorshipRequest> sponsorshipRequests;
    private long nextSponsorshipRequestNumber;
    public SponsorshipState(){
        this.sponsorshipRequests = null;
        this.nextSponsorshipRequestNumber = 1;
    }
    public SponsorshipState(ISponsorshipState other){
        this.sponsorshipRequests = other.getAllSponsorshipRequests();
        this.nextSponsorshipRequestNumber = other.getNextSponsorshipRequestNumber();
    }
    @Override
    public SponsorshipRequest addSponsorshipRequest(TicketedEvent event) {
        SponsorshipRequest newSponsorshipRequest = new SponsorshipRequest(nextSponsorshipRequestNumber, event);
        nextSponsorshipRequestNumber = nextSponsorshipRequestNumber + 1;
        return newSponsorshipRequest;
    }

    @Override
    public List<SponsorshipRequest> getAllSponsorshipRequests() {
        return sponsorshipRequests;
    }

    @Override
    public List<SponsorshipRequest> getPendingSponsorShipRequests() {
        List<SponsorshipRequest> pendingSponsorshipRequests = null;
        for (int i = 0; i < sponsorshipRequests.size(); i++){
            if (sponsorshipRequests.get(i).getStatus() == SponsorshipStatus.PENDING){
                pendingSponsorshipRequests.add(sponsorshipRequests.get(i));
            }
        }
        return pendingSponsorshipRequests;
    }

    @Override
    public SponsorshipRequest findRequestByNumber(long requestNumber) {
        SponsorshipRequest correctRequest = null;
        for (int i = 0; i < sponsorshipRequests.size(); i++){
            if (sponsorshipRequests.get(i).getRequestNumber() == requestNumber){
                correctRequest = sponsorshipRequests.get(i);
            }
        }
        return correctRequest;
    }

    @Override
    public long getNextSponsorshipRequestNumber() {
        return nextSponsorshipRequestNumber;
    }
}
