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
    private double angle = 0.0;
    private final Bitmap imageInclinometer;

    public DrawInclinometer(Context context) {
	super(context);
	init();
	imageInclinometer = BitmapFactory.decodeResource(getResources(), R.drawable.inclinometer);
    }

    private void init() {
	paint = new Paint();
	paint.setAntiAlias(true);
	paint.setStrokeWidth(7);
	paint.setTextSize(40);
	paint.setStyle(Paint.Style.FILL_AND_STROKE);
	paint.setColor(Color.CYAN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
	int xPoint = getMeasuredWidth() / 2;
	int yPoint = getMeasuredWidth() / 2;

	double radius = (Math.max(xPoint, yPoint) * 0.8);
	
	canvas.drawBitmap(imageInclinometer, 100, 89, null);
	
	canvas.drawLine(0, getHeight() - 55, getWidth(), getHeight() - 55,
		paint);

	canvas.drawLine(
		xPoint,
		yPoint,
		(float) (xPoint + radius
			* Math.sin((double) (angle) / 180 * 3.143)),
		(float) (yPoint - radius
			* Math.cos((double) (angle) / 180 * 3.143)), paint);

	/*do sprawdzenia - jest 3:30 nie mam ju¿ si³y*/
	/*if (angle <= 0.1 || angle >= 359.9) {
	    angle = 90;
	    canvas.drawText(String.format("%.0f", angle), getWidth() / 3,
			getHeight() / 4, paint);
	} 
	if (angle <= 270.1) {
	    angle = 0;
	    canvas.drawText(String.format("%.0f", angle), getWidth() / 3,
			getHeight() / 4, paint);
	}
	if(angle >= 89.9){
	    angle = 180;
	    canvas.drawText(String.format("%.0f", angle), getWidth() / 3,
			getHeight() / 4, paint);
	}
	if(angle >= 270.1 && angle <= 359.9){
	    canvas.drawText(String.format("%.0f", 360 - angle), getWidth() / 3,
			getHeight() / 4, paint);
	}
	if(angle>=0.1 & angle <= 89.9){
	    canvas.drawText(String.format("%.0f", 90+angle), getWidth() / 3,
			getHeight() / 4, paint);
	}*/
	canvas.drawText(String.format("%.0f", angle), 100,
		100, paint);
	canvas.drawText("o", 180,
		70, paint);
	

    }

    public void updateData(double position) {
	this.angle = position;
	invalidate();
    }

}
