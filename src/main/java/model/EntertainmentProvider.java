package model;

import external.EntertainmentProviderSystem;

import java.util.List;

public class EntertainmentProvider extends User {
    private String orgName;
    private String orgAddress;
    private String mainRepName;
    private String mainRepEmail;
    private List<String> otherRepNames;
    private List<String> otherRepEmails;
    private List<Event> events;
    private EntertainmentProviderSystem entertainmentProviderSystem;

    protected EntertainmentProvider(String orgName, String orgAddress, String mainRepName, String mainRepEmail, List<String> otherRepNames, List<String> otherRepEmails, String email, String password, String paymentAccountEmail) {
        super(email, password, paymentAccountEmail);
        this.orgName = orgName;
        this.orgAddress = orgAddress;
        this.mainRepName = mainRepName;
        this.mainRepEmail = mainRepEmail;
        this.otherRepNames = otherRepNames;
        this.otherRepEmails = otherRepEmails;
    }
    public void addEvent(Event event){
        events.add(event);
    }
    public String getOrgName(){
        return orgName;
    }
    public void setOrgName(String orgName){
        this.orgName = orgName;
    }
    public String getOrgAddress(){
        return orgAddress;
    }
    public void setOrgAddress(String orgAddress){
        this.orgAddress = orgAddress;
    }
    public List<Event> getEvents(){
        return events;
    }
    public void setMainRepName(String mainRepName){
        this.mainRepEmail = mainRepName;
    }
    public void setOtherRepNames(List<String> otherRepNames){
        this.otherRepNames = otherRepNames;
    }
    public void setOtherRepEmails(List<String> otherRepEmails){
        this.otherRepEmails = otherRepEmails;
    }
    public EntertainmentProviderSystem getProviderSystem(){
        return entertainmentProviderSystem;
    }

}
