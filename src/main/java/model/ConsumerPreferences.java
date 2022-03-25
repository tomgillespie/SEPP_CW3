package model;

public class ConsumerPreferences {
    public boolean preferAirFiltration;
    public boolean preferOutdoorsOnly;
    public int preferredMaxCapacity;
    public int preferredMaxVenueSize;
    public boolean preferSocialDistancing;
    protected ConsumerPreferences(){
        this.preferAirFiltration = false;
        this.preferSocialDistancing = false;
        this.preferOutdoorsOnly = false;
        this.preferredMaxCapacity = Integer.MAX_VALUE;
        this.preferredMaxVenueSize = Integer.MAX_VALUE;
    }
}
