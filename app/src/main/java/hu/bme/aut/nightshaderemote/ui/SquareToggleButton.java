package hu.bme.aut.nightshaderemote.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

/**
 * Olyan ToggleButton, ami minden körülmények között nyégyzetesen jelenik meg a kijelzőn.
 *
 * Created by Marci on 2014.04.29..
 */
public class SquareToggleButton extends ToggleButton {
    public SquareToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SquareToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareToggleButton(Context context) {
        super(context);
    }

    @Override
      protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Négyzetes felület kikényszerítése
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0
                ? h
                : h == 0
                ? w
                : w < h
                ? w
                : h;
        setMeasuredDimension(d, d);
    }
}
