import java.time.Duration;
import java.util.Calendar;

/**
 * @author Joakim Tubring
 * @version 2021-09-21
 *
 * A class for accessing the model.
 */

public class Model {

/**
* Checks if last routes time is above 18 hours.
*/

    public boolean checkRouteTime(User user){
        return getdiff(user.getLastRoute().getTime()) > 18;
    }

/**
* Gets the difference between the last route and current time.
*/

    private int getdiff(Calendar lastRoute){

        Duration difference =  Duration.between(lastRoute.toInstant(),
                Calendar.getInstance().toInstant());
        return Math.abs((int) difference.toHours());
    }
}