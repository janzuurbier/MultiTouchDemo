package nl.hu.zrb;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MultiTouch extends Activity implements OnTouchListener {
	// Matrix instances to move and zoom image
	Matrix matrix = new Matrix();
	Matrix eventMatrix = new Matrix();
	float eventDistance = 0;
	float eventAngle = 0;
	float eventX =0, eventY = 0;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ImageView view = (ImageView) findViewById(R.id.imageView);
		view.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;

		switch (event.getActionMasked() ) {
			case MotionEvent.ACTION_DOWN:
				//primary touch event starts: remember touch down location
				eventX = event.getX(0);
				eventY = event.getY(0);
				return true;
			case MotionEvent.ACTION_POINTER_DOWN: {
				//secondary touch event starts: remember distance
				float x = event.getX(0) - event.getX(1);
				float y = event.getY(0) - event.getY(1);
				eventDistance = (float) Math.sqrt(x * x + y * y);
				eventAngle = (float) Math.atan2(y,x);
				return true;
			}
			case MotionEvent.ACTION_MOVE:
				if(event.getPointerCount() > 1){
					//multi-finger zoom, scale accordingly
					float x = event.getX(0) - event.getX(1);
					float y = event.getY(0) - event.getY(1);
					//float dist = (float) Math.sqrt(x * x + y * y);
					float angle = (float) Math.atan2(y, x);
					matrix.set(eventMatrix);
					//float scale = dist / eventDistance;
					float rot = angle - eventAngle;
					rot = (float) (rot *180/Math.PI);
					//matrix.postScale(scale, scale, eventX, eventY);
					matrix.postRotate(rot, eventX, eventY);
					// Perform the transformation
					view.setImageMatrix(matrix);
					return true;
				}
				else {
					//single finger drag, translate accordingly
					matrix.set(eventMatrix);
					matrix.postTranslate(event.getX(0) - eventX, event.getY(0) - eventY);
					// Perform the transformation
					view.setImageMatrix(matrix);
					return true;
				}

			case MotionEvent.ACTION_UP:

				eventMatrix.set(matrix);
				return true;
			case MotionEvent.ACTION_POINTER_UP:
				//now there is only one pointer left
				//recalculate eventX en eventY the position of the pointer left.
				if(event.getActionIndex() == 0){
					eventX = event.getX(1);
					eventY = event.getY(1);
				}
				else {
					eventX = event.getX(0);
					eventY = event.getY(0);
				}
				eventMatrix.set(matrix);
				return true;
		}

		return false;
	}

}


