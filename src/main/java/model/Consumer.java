package model;

import java.util.List;
import java.util.prefs.Preferences;

public class Consumer extends User{
    private String name;
    private String phoneNumber;
    private List<Booking> bookings;
    private ConsumerPreferences preferences;
    public Consumer(String name, String phoneNumber, String email, String password, String paymentAccountEmail) {
        super(email, password, paymentAccountEmail);
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
    public void addBooking(Booking booking){
        this.bookings.add(booking);
    }
    public String getName(){
        return name;
    }
    public ConsumerPreferences getPreferences(){
        return preferences;
    }
    public void setPreferences(ConsumerPreferences preferences){
        this.preferences = preferences;
    }
    public List<Booking> getBookings(){
        return bookings;
    }
    public void notify(String message){
        System.out.println(message);
    }
    public void setName(String newName){
        this.name = newName;
    }
    public void setPhoneNumber(String newPhoneNumber){
        this.phoneNumber = newPhoneNumber;
    }


}
