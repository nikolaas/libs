package org.ns.log;

import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 *
 * @author stupak
 */
public class LogMessage {

    private final Level level;
    private final String message;
    
    public LogMessage(LogRecord record) {
        level = record.getLevel();
        message = record.getMessage();
    }

    public Level getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "LogMessage{" + "level=" + level + ", message=" + message + '}';
    }
    
}
