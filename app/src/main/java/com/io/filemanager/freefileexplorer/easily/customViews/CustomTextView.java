package com.io.filemanager.freefileexplorer.easily.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView {
    private void init(AttributeSet attributeSet) {
    }

    public CustomTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(attributeSet);
    }

    public CustomTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }

    public CustomTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public CustomTextView(Context context) {
        super(context);
        init((AttributeSet) null);
    }
}
