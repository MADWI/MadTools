package pl.edu.zut.mad.tools.inclinometer;

import pl.edu.zut.mad.tools.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class DrawInclinometer extends View {

	private Paint paint = new Paint();
	private Paint textPaint = new Paint();
	private double angle = 0.0;
	private Bitmap imageInclinometer;
	private double proportions;
	private final Context ctx;

	public DrawInclinometer(Context context) {
		super(context);
		ctx = context;

		init();
		initTextPaint();

		int actionBarHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 48, ctx.getResources()
						.getDisplayMetrics());

		Log.e("Ac height", Integer.toString(actionBarHeight));
		imageInclinometer = BitmapFactory.decodeResource(getResources(),
				R.drawable.protractor1);

		proportions = (double) (imageInclinometer.getWidth())
				/ (double) (imageInclinometer.getHeight());

	}

	public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {

		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
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
		paint.setColor(Color.WHITE);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int xPoint = getMeasuredWidth() / 2;
		int yPoint = getMeasuredHeight();

		double radius = (Math.max(xPoint, yPoint) * 0.7);

		if (imageInclinometer.getHeight() != getMeasuredHeight()) {
			imageInclinometer = getResizedBitmap(imageInclinometer,
					(int) (getMeasuredHeight() * proportions),
					getMeasuredHeight());
		}

		canvas.drawBitmap(imageInclinometer,
				xPoint - imageInclinometer.getWidth() / 2, yPoint
						- imageInclinometer.getHeight(), null);
		canvas.drawLine(
				xPoint,
				yPoint,
				(float) (xPoint + radius
						* Math.sin((double) (angle) / 180 * 3.14159)),
				(float) (yPoint - radius
						* Math.cos((double) (angle) / 180 * 3.14159)), paint);

		textPaint.setTextSize(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 48, ctx.getResources()
						.getDisplayMetrics()));
		canvas.drawText(String.format("%.0f \u00B0", Math.abs(angle)),
				(float) (getMeasuredWidth() * 0.05),
				(float) (getMeasuredHeight() * 0.2), textPaint);

	}

	public void updateData(double position) {
		this.angle = position;
		invalidate();
	}

}
