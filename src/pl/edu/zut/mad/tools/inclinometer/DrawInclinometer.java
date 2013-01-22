package pl.edu.zut.mad.tools.inclinometer;

import pl.edu.zut.mad.tools.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawInclinometer extends View {

    private Paint paint = new Paint();
    private Paint textPaint = new Paint();
    private double angle = 0.0;
    private final Bitmap imageInclinometer;

    public DrawInclinometer(Context context) {
	super(context);
	init();
	initTextPaint();
	imageInclinometer = BitmapFactory.decodeResource(getResources(), R.drawable.protractor);
    }

    private void initTextPaint() {
	textPaint.setAntiAlias(true);
	textPaint.setTextSize(70);
	textPaint.setStyle(Paint.Style.STROKE);
	textPaint.setColor(Color.WHITE);
    }

    private void init() {
	paint.setAntiAlias(true);
	paint.setStrokeWidth(7);
	paint.setStyle(Paint.Style.STROKE);
	paint.setColor(Color.CYAN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
	int xPoint = getMeasuredWidth() / 2;
	int yPoint = getMeasuredWidth() / 2;

	double radius = (Math.max(xPoint, yPoint) * 0.8);
	
	canvas.drawBitmap(imageInclinometer, xPoint-imageInclinometer.getWidth()/2, yPoint-imageInclinometer.getHeight(), null);
	
	/*canvas.drawLine(0, getHeight() - 72, getWidth(), getHeight() - 72,
		paint);*/

	canvas.drawLine(
		xPoint-10,
		yPoint-20,
		(float) (xPoint + radius
			* Math.sin((double) (angle) / 180 * 3.14159)),
		(float) (yPoint - radius
			* Math.cos((double) (angle) / 180 * 3.14159)), paint);

	canvas.drawText(String.format("%.0f", Math.abs(angle)), 50,
		100, textPaint);
	canvas.drawText("o", 180,
		70, textPaint);
	

    }

    public void updateData(double position) {
	this.angle = position;
	invalidate();
    }

}
