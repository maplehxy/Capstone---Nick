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
public class PathView extends View {
    float[][] route = new float[10][10];
    PointF[] wayPoint = new PointF[10];
    PointF start;
    PointF end;

    public PathView(Context context, PointF start, PointF end) {
        super(context);
        this.start=start;
        this.end=end;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        wayPoint[0]=new PointF(176,326);
        wayPoint[1]=new PointF(176,500);
        wayPoint[2]=new PointF(200,577);
        wayPoint[3]=new PointF(325,577);
        wayPoint[4]=new PointF(325,450);
        wayPoint[5]=new PointF(325,213);
        wayPoint[6]=new PointF(625,213);
        wayPoint[7]=new PointF(810,180);

        for (int i=0;i<9;i++){
            for (int j=0;j<0;j++){
                route[i][j]=0;
            }
        }
        connect(0,1);
        connect(1,2);
        connect(2,3);
        connect(3,4);
        connect(4,5);
        connect(5,6);
        connect(6,7);

        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(getResources().getColor(R.color.colorAccent));

        int index1=0;
        float distance= (float)Math.sqrt((start.x-wayPoint[0].x)*(start.x-wayPoint[0].x)+(start.y-wayPoint[0].y)*(start.y-wayPoint[0].y));
        for (int i=1;i<8;i++) {
            if ((float)Math.sqrt((start.x-wayPoint[i].x)*(start.x-wayPoint[i].x)+(start.y-wayPoint[i].y)*(start.y-wayPoint[i].y))<distance) {
                distance=(float)Math.sqrt((start.x-wayPoint[i].x)*(start.x-wayPoint[i].x)+(start.y-wayPoint[i].y)*(start.y-wayPoint[i].y));
                index1=i;
            }
        }
        canvas.drawLine(start.x, start.y, wayPoint[index1].x, wayPoint[index1].y, paint);

        int index2=0;
        distance= (float)Math.sqrt((end.x-wayPoint[0].x)*(end.x-wayPoint[0].x)+(end.y-wayPoint[0].y)*(end.y-wayPoint[0].y));
        for (int i=1;i<8;i++) {
            if ((float)Math.sqrt((end.x-wayPoint[i].x)*(end.x-wayPoint[i].x)+(end.y-wayPoint[i].y)*(end.y-wayPoint[i].y))<distance) {
                distance=(float)Math.sqrt((end.x-wayPoint[i].x)*(end.x-wayPoint[i].x)+(end.y-wayPoint[i].y)*(end.y-wayPoint[i].y));
                index2=i;
            }
        }
        canvas.drawLine(end.x, end.y, wayPoint[index2].x, wayPoint[index2].y, paint);

        if (index1>index2){
            int temp = index1;
            index1 = index2;
            index2 = temp;
        }
        for (int i=index1;i<index2;i++) {
            canvas.drawLine(wayPoint[i].x, wayPoint[i].y, wayPoint[i+1].x, wayPoint[i+1].y, paint);
        }
    }

    private void connect(int i,int j) {
        route[i][j]=(float) Math.sqrt((wayPoint[i].x-wayPoint[j].x)*(wayPoint[i].x-wayPoint[j].x)+
                (wayPoint[i].y-wayPoint[j].y)*(wayPoint[i].y-wayPoint[j].y));
    }
}
