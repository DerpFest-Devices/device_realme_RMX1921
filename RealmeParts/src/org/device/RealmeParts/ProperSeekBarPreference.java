// let's make nice and clear,
// proper seekbar preference.

package org.device.RealmeParts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProperSeekBarPreference extends Preference implements SeekBar.OnSeekBarChangeListener,
        View.OnClickListener, View.OnLongClickListener {
    protected final String TAG = getClass().getName();
    protected static final String ANDROIDNS = "http://schemas.android.com/apk/res/android";

    protected int mInterval = 1;
    protected boolean mShowSign = false;
    protected String mUnits = "";
    protected boolean mContinuousUpdates = false;

    protected int mMinValue = 0;
    protected int mMaxValue = 100;
    protected boolean mDefaultValueExists = false;
    protected int mDefaultValue;

    protected int mValue;

    protected TextView mValueTextView;
    protected ImageView mResetImageView;
    protected ImageView mMinusImageView;
    protected ImageView mPlusImageView;
    protected SeekBar mSeekBar;

    protected boolean mTrackingTouch = false;
    protected int mTrackingValue;

    public ProperSeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProperSeekBarPreference);
        try {
            mInterval = a.getInt(R.styleable.ProperSeekBarPreference_interval, mInterval);
            mShowSign = a.getBoolean(R.styleable.ProperSeekBarPreference_showSign, mShowSign);
            String units = a.getString(R.styleable.ProperSeekBarPreference_units);
            if (units != null)
                mUnits = units;
            mContinuousUpdates = a.getBoolean(R.styleable.ProperSeekBarPreference_continuousUpdates, mContinuousUpdates);
        } finally {
            a.recycle();
        }

        mMinValue = attrs.getAttributeIntValue(ANDROIDNS, "min", mMinValue);
        mMaxValue = attrs.getAttributeIntValue(ANDROIDNS, "max", mMaxValue);
        if (mMaxValue < mMinValue)
            mMaxValue = mMinValue;
        String defaultValue = attrs.getAttributeValue(ANDROIDNS, "defaultValue");
        mDefaultValueExists = defaultValue != null && !defaultValue.isEmpty();
        if (mDefaultValueExists) {
            mDefaultValue = getLimitedValue(Integer.parseInt(defaultValue));
            mValue = mDefaultValue;
        } else {
            mValue = mMinValue;
        }

        setLayoutResource(R.layout.preference_proper_seekbar);
    }

    public ProperSeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ProperSeekBarPreference(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context,
                androidx.preference.R.attr.preferenceStyle,
                android.R.attr.preferenceStyle));
    }

    public ProperSeekBarPreference(Context context) {
        this(context, null);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        mValueTextView = (TextView) holder.findViewById(R.id.value);
        mResetImageView = (ImageView) holder.findViewById(R.id.reset);
        mMinusImageView = (ImageView) holder.findViewById(R.id.minus);
        mPlusImageView = (ImageView) holder.findViewById(R.id.plus);

        mSeekBar = (SeekBar) holder.findViewById(R.id.seekbar);

        mSeekBar.setMax(getSeekValue(mMaxValue));
        mSeekBar.setProgress(getSeekValue(mValue));

        updateValueViews();

        mSeekBar.setOnSeekBarChangeListener(this);

        mResetImageView.setOnClickListener(this);
        mMinusImageView.setOnClickListener(this);
        mPlusImageView.setOnClickListener(this);
        mResetImageView.setOnLongClickListener(this);
        mMinusImageView.setOnLongClickListener(this);
        mPlusImageView.setOnLongClickListener(this);
    }

    protected int getLimitedValue(int v) {
        return v < mMinValue ? mMinValue : (v > mMaxValue ? mMaxValue : v);
    }

    protected int getSeekValue(int v) {
        return 0 - Math.floorDiv(mMinValue - v, mInterval);
    }

    protected String getTextValue(int v) {
        return (mShowSign && v > 0 ? "+" : "") + String.valueOf(v) + mUnits;
    }

    protected void updateValueViews() {
        mValueTextView.setText(getContext().getString(R.string.proper_seekbar_value,
                (!mTrackingTouch || mContinuousUpdates ? getTextValue(mValue) + (mDefaultValueExists && mValue == mDefaultValue ? " (" + getContext().getString(R.string.proper_seekbar_default_value) + ")" : "")
                    : "[" + getTextValue(mTrackingValue) + "]")));
        if (!mDefaultValueExists || mValue == mDefaultValue || mTrackingTouch)
            mResetImageView.setVisibility(View.INVISIBLE);
        else
            mResetImageView.setVisibility(View.VISIBLE);
        if (mValue == mMinValue || mTrackingTouch) {
            mMinusImageView.setClickable(false);
            mMinusImageView.setColorFilter(getContext().getColor(R.color.disabled_text_color), PorterDuff.Mode.MULTIPLY);
        } else {
            mMinusImageView.setClickable(true);
            mMinusImageView.clearColorFilter();
        }
        if (mValue == mMaxValue || mTrackingTouch) {
            mPlusImageView.setClickable(false);
            mPlusImageView.setColorFilter(getContext().getColor(R.color.disabled_text_color), PorterDuff.Mode.MULTIPLY);
        } else {
            mPlusImageView.setClickable(true);
            mPlusImageView.clearColorFilter();
        }
    }

    protected void changeValue(int newValue) {
        // for subclasses
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int newValue = getLimitedValue(mMinValue + (progress * mInterval));
        if (mTrackingTouch && !mContinuousUpdates) {
            mTrackingValue = newValue;
            updateValueViews();
        } else if (mValue != newValue) {
            // change rejected, revert to the previous value
            if (!callChangeListener(newValue)) {
                mSeekBar.setProgress(getSeekValue(mValue));
                return;
            }
            // change accepted, store it
            changeValue(newValue);
            persistInt(newValue);

            mValue = newValue;
            updateValueViews();

            notifyChanged();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mTrackingValue = mValue;
        mTrackingTouch = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mTrackingTouch = false;
        if (!mContinuousUpdates)
            onProgressChanged(mSeekBar, getSeekValue(mTrackingValue), false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset:
                Toast.makeText(getContext(), getContext().getString(R.string.proper_seekbar_default_value_to_set, getTextValue(mDefaultValue)),
                        Toast.LENGTH_LONG).show();
                break;
            case R.id.minus:
                setValue(mValue - mInterval, true);
                break;
            case R.id.plus:
                setValue(mValue + mInterval, true);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.reset:
                setValue(mDefaultValue, true);
                //Toast.makeText(getContext(), getContext().getString(R.string.proper_seekbar_default_value_is_set),
                //        Toast.LENGTH_LONG).show();
                break;
            case R.id.minus:
                setValue(mMaxValue - mMinValue > mInterval * 2 && mMaxValue + mMinValue < mValue * 2 ? Math.floorDiv(mMaxValue + mMinValue, 2) : mMinValue, true);
                break;
            case R.id.plus:
                setValue(mMaxValue - mMinValue > mInterval * 2 && mMaxValue + mMinValue > mValue * 2 ? -1 * Math.floorDiv(-1 * (mMaxValue + mMinValue), 2) : mMaxValue, true);
                break;
        }
        return true;
    }

    // dont need too much shit about initial and default values
    // its all done in constructor already

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        if (restoreValue)
            mValue = getPersistedInt(mValue);
    }

    @Override
    public void setDefaultValue(Object defaultValue) {
        if (defaultValue instanceof Integer)
            setDefaultValue((Integer) defaultValue, mSeekBar != null);
        else
            setDefaultValue(defaultValue == null ? (String) null : defaultValue.toString(), mSeekBar != null);
    }

    public void setDefaultValue(int newValue, boolean update) {
        newValue = getLimitedValue(newValue);
        if (!mDefaultValueExists || mDefaultValue != newValue) {
            mDefaultValueExists = true;
            mDefaultValue = newValue;
            if (update)
                updateValueViews();
        }
    }

    public void setDefaultValue(String newValue, boolean update) {
        if (mDefaultValueExists && (newValue == null || newValue.isEmpty())) {
            mDefaultValueExists = false;
            if (update)
                updateValueViews();
        } else if (newValue != null && !newValue.isEmpty()) {
            setDefaultValue(Integer.parseInt(newValue), update);
        }
    }

    public void setValue(int newValue, boolean update) {
        newValue = getLimitedValue(newValue);
        if (mValue != newValue) {
            if (update)
                mSeekBar.setProgress(getSeekValue(newValue));
            else
                mValue = newValue;
        }
    }

    public int getValue() {
        return mValue;
    }

    // need some methods here to set/get other attrs at runtime,
    // but who really need this ...

    public void refresh(int newValue) {
        // this will ...
        setValue(newValue, mSeekBar != null);
    }
}
