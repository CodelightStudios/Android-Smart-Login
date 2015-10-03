package studios.codelight.smartloginlibrary.util;

import android.view.View;
import android.view.animation.TranslateAnimation;

/**
 * Created by Kalyan on 10/3/2015.
 */
public class AnimUtil {

    // To animate view slide out from bottom to top
    public static void slideToTop(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,-view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }
}
