package logging;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Logger {
    private List<LogEntry> log;
    private static Logger logger = null;
    public static Logger getInstance(){
        if(logger == null){
            logger = new Logger();
        }
        return logger;
    }
    public void logAction(String callerName, Object result){
        LogEntry newEntry = new LogEntry(callerName, result, Collections.emptyMap());
        log.add(newEntry);
    }
    public void logAction(String callerName, Object result, Map<String,Object> additionalInfo){
        LogEntry newEntry = new LogEntry(callerName, result, additionalInfo);
        log.add(newEntry);
    }
    public List<LogEntry> getLog(){
        return log;
    }
    public void clearLog(){
        if (log == null){
            this.log = null;
        }
        else {
            this.log.clear();
        }
    }
}
