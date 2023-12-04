package mx.linkom.caseta_los_cabos.Animaciones;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.constraintlayout.widget.ConstraintLayout;

import mx.linkom.caseta_los_cabos.R;

public class AnimationUtil {
    public static void startAnimation(Context context, ConstraintLayout layout) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim);
        layout.startAnimation(animation);
    }
}