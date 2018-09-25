package wizag.com.supa.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class FontManager extends AppCompatTextView {


    public FontManager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FontManager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontManager(Context context) {
        super(context);
        init();
    }

    private void init() {

        //Font name should not contain "/".
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fontawesome_webfont.ttf");
        setTypeface(tf);
    }

}