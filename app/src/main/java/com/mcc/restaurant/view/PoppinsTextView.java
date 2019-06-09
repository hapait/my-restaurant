package com.mcc.restaurant.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class PoppinsTextView extends android.support.v7.widget.AppCompatTextView {

    public PoppinsTextView(Context context) {
        super(context);
        init();
    }

    public PoppinsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PoppinsTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(defStyle);
    }

    private void init() {
        Typeface regularFont=Typeface.createFromAsset(getContext().getAssets(), "fonts/Poppins-SemiBold.ttf");
        this.setTypeface(regularFont);


    }

    private void init(int style) {
        Typeface regularFont=Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        this.setTypeface(regularFont, style);

    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(tf, style);
    }

    @Override
    public void setTypeface(Typeface tf) {
        super.setTypeface(tf);
    }

}
