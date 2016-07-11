package se.liu.student.frejo105.beerapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import se.liu.student.frejo105.beerapp.R;

public class PlaceholderTextView extends TextView {

    private String pre = "";
    private String post = "";
    private String placeholder = "";

    public PlaceholderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PlaceholderTextView, 0 ,0);


        try {
            String post = a.getString(R.styleable.PlaceholderTextView_post);
            String pre = a.getString(R.styleable.PlaceholderTextView_pre);
            String placeholder = a.getString(R.styleable.PlaceholderTextView_defaultPlaceholder);
            if (post != null) this.post = post;
            if (pre != null) this.pre = pre;
            if (placeholder != null) this.placeholder = placeholder;
        }
        finally {
            a.recycle();
        }
        super.setText(combine());
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        setText(combine());
    }

    private String combine() {
        return pre + " " + placeholder + " " + post;
    }
}
