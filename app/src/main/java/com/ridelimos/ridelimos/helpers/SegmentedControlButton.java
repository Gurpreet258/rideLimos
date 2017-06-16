package com.ridelimos.ridelimos.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatRadioButton;

import com.ridelimos.ridelimos.R;

/**
 * 
 */
public class SegmentedControlButton extends AppCompatRadioButton {


    /**
     * 
     */
    public SegmentedControlButton( Context context ) {
        super( context );
    }

    /**
     * 
     */
    public SegmentedControlButton( Context context, AttributeSet attrs ) {
        // It would be great to simply pass in R.style.SegmentedControlButton here, but that won't work due to:
        // https://code.google.com/p/android/issues/detail?id=12683
        super( context, attrs, R.attr.segmentedControlButtonStyle );
    }

    /**
     * 
     */
    public SegmentedControlButton( Context context, AttributeSet attrs, int defStyle ) {
        super( context, attrs, defStyle );
    }
}
