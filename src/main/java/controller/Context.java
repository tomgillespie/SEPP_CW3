package controller;

import external.MockPaymentSystem;
import external.PaymentSystem;
import state.*;

public class Context {
    private PaymentSystem paymentSystem;
    private IUserState userState;
    private IEventState eventState;
    private IBookingState bookingState;
    private ISponsorshipState sponsorshipState;
    public Context(){
        this.paymentSystem = new MockPaymentSystem();
        this.userState = new UserState();
        this.eventState = new EventState();
        this.bookingState = new BookingState();
        this.sponsorshipState = new SponsorshipState();
    }
    public Context(Context other){
        this.userState = new UserState(other.getUserState());
        this.eventState = new EventState(other.getEventState());
        this.bookingState = new BookingState(other.getBookingState());
        this.sponsorshipState = new SponsorshipState(other.getSponsorshipState());
        this.paymentSystem = other.getPaymentSystem();
    }
    public PaymentSystem getPaymentSystem(){
        return paymentSystem;
    }
    public IUserState getUserState(){
        return userState;
    }
    public IBookingState getBookingState(){
        return bookingState;
    }
    public IEventState getEventState(){
        return eventState;
    }
    public ISponsorshipState getSponsorshipState(){
        return sponsorshipState;
    }
}
