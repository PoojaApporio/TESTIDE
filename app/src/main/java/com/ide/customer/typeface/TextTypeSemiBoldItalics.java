package com.ide.customer.typeface;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextTypeSemiBoldItalics extends TextView {

    public TextTypeSemiBoldItalics(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextTypeSemiBoldItalics(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextTypeSemiBoldItalics(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("OpenSans_SemiboldItalic.ttf", context);
        setTypeface(customFont);
    }
}