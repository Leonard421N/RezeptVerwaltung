package wizardofba.rezeptverwaltung.utility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.content.res.AppCompatResources;
import android.view.animation.AccelerateDecelerateInterpolator;

import wizardofba.rezeptverwaltung.R;

public class Animation {

    public static boolean positiveFab(final FloatingActionButton fab, boolean isChanged) {

        fab.setClickable(false);
        fab.animate().setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        fab.setBackgroundTintList(AppCompatResources.getColorStateList(fab.getContext(), R.color.colorAccept));
                        fab.setImageResource(R.drawable.ic_baseline_check_white_24);
                        fab.animate().setStartDelay(700).setInterpolator(new FastOutSlowInInterpolator()).setDuration(300)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        fab.setBackgroundTintList(AppCompatResources.getColorStateList(fab.getContext(), R.color.colorAccent));
                                        fab.setImageResource(R.drawable.ic_save_white_24dp);
                                        fab.setClickable(true);
                                    }
                                })
                                .rotation(-360);
                    }
                })
                .rotation(360);

        return isChanged;
    }
}
