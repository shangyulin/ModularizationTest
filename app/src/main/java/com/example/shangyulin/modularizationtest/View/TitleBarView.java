package com.example.shangyulin.modularizationtest.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shangyulin.modularizationtest.R;

public class TitleBarView extends RelativeLayout {

    private Button left_btn;
    private TextView title;
    private Button right_btn;
    private String left_btnText;
    private String title_Text;
    private String right_btnText;
    private LayoutParams left_params;
    private LayoutParams right_params;
    private LayoutParams title_params;
    private float title_TextSize;

    private TitleBarClickCallback callback;
    private Drawable bg;

    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TitleBarView);
        left_btnText = attributes.getString(R.styleable.TitleBarView_left_text);

        title_Text = attributes.getString(R.styleable.TitleBarView_title);
        title_TextSize = attributes.getInteger(R.styleable.TitleBarView_title_textSize, 18);

        right_btnText = attributes.getString(R.styleable.TitleBarView_right_text);
        attributes.recycle();

        left_btn = new Button(context);
        title = new TextView(context);
        right_btn = new Button(context);

        left_btn.setText(left_btnText);
        left_params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        left_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        addView(left_btn, left_params);

        title.setText(title_Text);
        title.setTextSize(title_TextSize);
        title.setGravity(Gravity.CENTER);
        title_params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        title_params.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(title, title_params);

        right_btn.setText(right_btnText);
        right_params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        right_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        addView(right_btn, right_params);

        left_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.left_Callback();
            }
        });

        right_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.right_Callback();
            }
        });
    }

    public void setTitleBarCallbackListener(TitleBarClickCallback callbackListener) {
        this.callback = callbackListener;
    }

}
