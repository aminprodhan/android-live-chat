package amin_chats.share;

/**
 * Created by cursor on 4/9/2020.
 */

public class LocationBoundedCheck {
   public boolean isBounded(double top, double left,
                      double bottom, double right,
                      double latitude, double longitude){
            /* Check latitude bounds first. */
        if(top >= latitude && latitude >= bottom){
                    /* If your bounding box doesn't wrap
                       the date line the value
                       must be between the bounds.
                       If your bounding box does wrap the
                       date line it only needs to be
                       higher than the left bound or
                       lower than the right bound. */
            if(left <= right && left <= longitude && longitude <= right){
                return true;
            } else if(left > right && (left <= longitude || longitude <= right)) {
                return true;
            }
        }
        return false;
    }
}
