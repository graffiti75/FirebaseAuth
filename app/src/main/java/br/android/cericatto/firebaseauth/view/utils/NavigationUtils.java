package br.android.cericatto.firebaseauth.view.utils;

import android.app.Activity;

import br.android.cericatto.firebaseauth.R;

/**
 * NavigationUtils.java.
 *
 * @author Rodrigo Cericatto
 * @since Feb 1, 2016
 */
public class NavigationUtils {

    //--------------------------------------------------
    // Enum
    //--------------------------------------------------

    public enum Animation {
        GO,
        BACK
    }

    //--------------------------------------------------
    // Methods
    //--------------------------------------------------

    public static void animate(Activity activity, Animation animation) {
        if (animation == Animation.GO) {
            activity.overridePendingTransition(R.anim.open_next, R.anim.close_previous);
        } else {
            activity.overridePendingTransition(R.anim.open_previous, R.anim.close_next);
            activity.finish();
        }
    }
}