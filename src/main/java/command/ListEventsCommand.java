//package command;
//
//import controller.Context;
//import model.Consumer;
//import model.EntertainmentProvider;
//import model.Event;
//import model.User;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ListEventsCommand extends Object implements ICommand{
//
//    private boolean userEventsOnly;
//    private boolean activeEventsOnly;
//    private List<Event> eventListResult;
//    private LogStatus logStatus;
//    private List<Event> entertainmentProvidersEvents;
//
//    public enum LogStatus{
//        LIST_USER_EVENTS_SUCCESS,
//        LIST_USER_EVENTS_NOT_LOGGED_IN
//    }
//
//    public ListEventsCommand(boolean userEventsOnly, boolean activeEventsOnly){
//        this.userEventsOnly = userEventsOnly;
//        this.activeEventsOnly = activeEventsOnly;
//        this.eventListResult = new ArrayList<>();
//        this.entertainmentProvidersEvents = new ArrayList<>();
//    }
//
////    @Override
////    public void execute(Context context) {
////        List<Event> allEvents = context.getEventState().getAllEvents();
////        User currUser = context.getUserState().getCurrentUser();
////        if (userEventsOnly == true){
////            if (currUser == null){
////                this.eventListResult = new ArrayList<>();
////            }
////            if (currUser instanceof EntertainmentProvider){
////                for (int i = 0; i < allEvents.size(); i++){
////                    if (allEvents.get(i).getOrganiser() == currUser){
////                        this.entertainmentProvidersEvents.add(allEvents.get(i));
////                    }
////                }
////                this.eventListResult = entertainmentProvidersEvents;
////            }
////            if (currUser instanceof Consumer){
////                for (int i = 0; i < allEvents.size(); i++) {
////                    if (allEvents.get(i).g)
////                }
////                }
////            }
////        }
////    }
//
//    @Override
//    public Object getResult() {
//        return null;
//    }
//}
