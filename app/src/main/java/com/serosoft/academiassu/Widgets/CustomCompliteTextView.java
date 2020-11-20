package com.serosoft.academiassu.Widgets;

import android.content.Context;
import android.util.AttributeSet;

public class CustomCompliteTextView extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {

    private boolean mIsSearchEnabled = true;

    public CustomCompliteTextView(Context context) {
        super(context);
    }

    public CustomCompliteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCompliteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setSearchEnabled(boolean isEnabled) {
        mIsSearchEnabled = isEnabled;
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        if (mIsSearchEnabled) {
            super.performFiltering(text, keyCode);
        }
    }
}