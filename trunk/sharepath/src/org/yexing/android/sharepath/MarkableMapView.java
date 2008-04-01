package org.yexing.android.sharepath;

import java.util.ArrayList;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.MapView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author xingye
 *
 */
public class MarkableMapView extends MapView {
	private static final String LOG_TAG = "SharePath";

	int left = 0, top = 0, right = 0, bottom = 0;

	boolean marking = false; //�Ƿ��ڱ�ͼ״̬
	
	boolean clear = false;

	public Path path;
	CornerPathEffect cpe;
	Paint p;
	Bitmap b;
	int delta = 10;
	int lastX = 0, lastY = 0;
	
	//������Ҫͨ��contextȡ��po��������onlayout�У���ʱ����
//	PathOverlay po;
	Context context;


	public ArrayList<KeyPoint> points;//·���ϵ�ת�۵�

	Dialog badgeDialog; //����������ʾ��Ϣ�ĶԻ���
	
	public MarkableMapView(Context context, AttributeSet attrs, Map inflateParams) {
		super(context, attrs, inflateParams);

		Log.v(LOG_TAG, "MarkableMapView");
		
		this.context = context;
		
		//·����ʽ
		cpe = new CornerPathEffect(4);
		p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(2);
		p.setPathEffect(cpe);

		points = new ArrayList<KeyPoint>();

	}

	// @Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	boolean bZoom = false;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
//		if (marking == true) {
		
		
			int action = ev.getAction();
			
			Log.v(LOG_TAG, "action:" + action);
			
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			
			if(action == MotionEvent.ACTION_DOWN) {
				
			}
			if(action == MotionEvent.ACTION_MOVE) {
//				ev.addBatch(100, 100, 1, 1);
//				Log.v(LOG_TAG, "addBatch");
				
				// zoom
				if(ev.getHistorySize() > 0) {
					bZoom = false;
				} else {
					bZoom = true;
				}
//				Log.v(LOG_TAG, "historical size:" + ev.getHistorySize());
				// end zoom
			}
			
			if (action == MotionEvent.ACTION_UP) {
				Log.d(LOG_TAG, "UP x:" + x + " y:" + y
						+ " R:"	+ right + " B:" + bottom);

//				KeyPoint tt = new KeyPoint(new Point(x, y), null);
//				points.add(tt);
				
				// zoom
				if(bZoom == true) {
					displayZoomDialog(x, y);
				}
				bZoom = true;
				// end zoom
			}
			invalidate();
			return true;
//		} else {
//			return super.onTouchEvent(ev);
//		}
	}

	
	
	/**
	 * ���ù�����ʾ
	 */
	public void setBadge() {
		badgeDialog = new Dialog(context);
		badgeDialog.setContentView(R.layout.set_tooltip_dialog);
		badgeDialog.setTitle(context.getString(R.string.tooltip_dialog));

		Button bOk = (Button) badgeDialog.findViewById(R.id.tooltip_ok);
		bOk.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				EditText et = (EditText) badgeDialog.findViewById(R.id.tooltip);
				//Log.d("SharePath", "et " + (et == null ? "error" : "ok"));
				KeyPoint tt = points.get(points.size() - 1);
				//Log.d("SharePath", "tt " + (tt == null ? "error" : "ok"));
				tt.info = new String(et.getText().toString());
				//Log.d("SharePath", "text " + tt.info);
				
				invalidate();
				badgeDialog.dismiss();
			}
		});

		Button bCancel = (Button) badgeDialog.findViewById(R.id.tooltip_cancel);
		bCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				badgeDialog.cancel();
			}
		});

		badgeDialog.show();

	}
	
	
}
