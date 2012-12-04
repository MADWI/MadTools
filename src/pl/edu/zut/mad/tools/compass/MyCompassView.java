package pl.edu.zut.mad.tools.compass;

import pl.edu.zut.mad.tools.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MyCompassView extends View {

	private Paint paint;
	private float position = 0;
	private final Bitmap imageCompass;
	private final Bitmap imageIndicator;

	public MyCompassView(Context context) {
		super(context);
		init();
		//load compass image
		imageCompass = BitmapFactory.decodeResource(getResources(), R.drawable.compass);
		imageIndicator = BitmapFactory.decodeResource(getResources(), R.drawable.wskazowka);
		
	}

	private void init() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(2);
		paint.setTextSize(25);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.WHITE);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		int xPoint = getMeasuredWidth() / 2;
		int yPoint = getMeasuredHeight() / 2;

		float radius = (float) (Math.max(xPoint, yPoint) * 0.6);
		//canvas.drawCircle(xPoint, yPoint, radius, paint);
		// canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(),
		// paint);
		// 3.143 is a good approximation for the circle
//	    Resources res = mContext.getResources();
//	    Drawable myImage = res.getDrawable(R.drawable.my_image);
		
		canvas.drawBitmap(imageIndicator, xPoint-imageIndicator.getWidth()/2, yPoint-imageIndicator.getHeight()/2, null);
		canvas.save();
		canvas.rotate(-position, xPoint ,yPoint);
		canvas.drawBitmap(imageCompass, xPoint- imageCompass.getWidth()/2, yPoint - imageCompass.getHeight()/2, null);
		canvas.restore();
	    //imageCompass.draw(canvas);
		canvas.drawLine(
				xPoint,
				yPoint,
				(float) (xPoint + radius
						* Math.sin((double) (-position) / 180 * 3.143)),
				(float) (yPoint - radius
						* Math.cos((double) (-position) / 180 * 3.143)), paint);

		canvas.drawText(String.valueOf(position), xPoint, yPoint, paint);
	}

	public void updateData(float position) {
		this.position = position;
		invalidate();
	}

}
