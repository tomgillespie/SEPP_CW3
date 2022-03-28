package logging;

import java.util.Map;
import java.util.stream.Collectors;

public class LogEntry {
    private String callerName;
    private Object result;
    private Map<String, Object> additionalInfo;
    LogEntry(String callerName, Object result, Map<String, Object> additionalInfo){
        this.callerName = callerName;
        this.result = result;
        this.additionalInfo = additionalInfo
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> String.valueOf(entry.getValue()))
                );
    }
    public String getResult(){
        return String.valueOf(result);
    }
}
