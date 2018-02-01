package org.wfp.cats.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wfp.cats.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FormFieldView extends LinearLayout {

    @BindView(R.id.label)
    TextView labelTextView;

    @BindView(R.id.input)
    EditText inputTextView;

    public FormFieldView(Context context) {
        this(context, null);
    }

    public FormFieldView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormFieldView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_single_line_form, this);
        ButterKnife.bind(this);

        // Getting attributes
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FormFieldView, 0, 0);
        try {
            String label = ta.getString(R.styleable.FormFieldView_cats_label);
            labelTextView.setText(label);
        } finally {
            ta.recycle();
        }
    }
}
