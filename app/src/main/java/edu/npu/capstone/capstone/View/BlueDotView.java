package edu.npu.capstone.capstone.View;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

import edu.npu.capstone.capstone.R;

/**
 * Extends great ImageView library by Dave Morrissey. See more:
 * https://github.com/davemorrissey/subsampling-scale-image-view.
 */
public class BlueDotView extends View {

    private float radius = 1.0f;
    private PointF dotCenter = null;
    private float accuracy;

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setDotCenter(PointF dotCenter) {
        this.dotCenter = dotCenter;
    }



    public BlueDotView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dotCenter != null) {
            float scaledRadius = 1.0f * radius;
            Paint paint = new Paint();

            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);

            paint.setColor(getResources().getColor(R.color.circle));
            canvas.drawCircle(dotCenter.x, dotCenter.y, scaledRadius + accuracy, paint);

            paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
            canvas.drawCircle(dotCenter.x, dotCenter.y, scaledRadius + 3, paint);

            paint.setColor(getResources().getColor(R.color.white));
            canvas.drawCircle(dotCenter.x, dotCenter.y, scaledRadius + 2, paint);


            paint.setColor(getResources().getColor(R.color.ia_blue));
            canvas.drawCircle(dotCenter.x, dotCenter.y, scaledRadius, paint);

        }
    }
}
