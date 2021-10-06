import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Route {
    private String id;
    private Calendar time;
    private double startX;
    private double startY;
    private double endX;
    private double endY;

    public Route(String id, double startX, double startY, double endX, double endY){
        this.id = id;
        this.time = Calendar.getInstance();
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

    }

    public String getId(){
        return this.id;
    }

    public Calendar getTime(){
        return this.time;
    }

}
