package Client;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class Object implements Serializable {
    private int sequenceNumer;
    private String timeMark;
    private int totalSentObjects;

    public Object(int sequenceNumer, String timeMark, int totalSentObjects) {
        this.sequenceNumer = sequenceNumer;
        this.timeMark = timeMark;
        this.totalSentObjects = totalSentObjects;
    }

    public int getSequenceNumer() {
        return sequenceNumer;
    }

    public void setSequenceNumer(int sequenceNumer) {
        this.sequenceNumer = sequenceNumer;
    }

    public String getTimeMark() {
        return timeMark;
    }

    public void setTimeMark(String timeMark) {
        this.timeMark = timeMark;
    }

    public int getTotalSentObjects() {
        return totalSentObjects;
    }

    public void setTotalSentObjects(int totalSentObjects) {
        this.totalSentObjects = totalSentObjects;
    }

}
