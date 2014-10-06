package th.in.blackouts.facedetection;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class DoFaceDetection {
	private ImageViewPlus imageViewPlus;
	private Bitmap mFaceBitmap;
	private int mFaceWidth = 200;
	private int mFaceHeight = 200;
	private static final int MAX_FACES = 10;
	private static String TAG = "Do face detection";
	private static boolean DEBUG = false;
	private Bitmap b = null;
	private Context context;
	protected static final int GUIUPDATE_SETFACE = 999;
	protected Handler mHandler = new Handler() {
		// @Override
		public void handleMessage(Message msg) {
			imageViewPlus.invalidate();
			super.handleMessage(msg);
		}
	};
	public DoFaceDetection(Context context)
	{
		this.context = context;
	}
	
	public ImageView process()
	{
		if(imageViewPlus == null)
			imageViewPlus = new ImageViewPlus(context);
		String url = "http://192.168.111.1:8888/tincam.jpg";
		if(b!=null)
			b.recycle();
		try {
			b = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(mFaceBitmap!=null)
			mFaceBitmap.recycle();
		mFaceBitmap = b.copy(Bitmap.Config.RGB_565, true); 
		b.recycle();
		mFaceWidth = mFaceBitmap.getWidth();
		mFaceHeight = mFaceBitmap.getHeight();  
		imageViewPlus.setImageBitmap(mFaceBitmap); 
		imageViewPlus.invalidate();
		doLengthyCalc();

		return imageViewPlus;
	}
	private void doLengthyCalc() {
    	Thread t = new Thread() {
    		Message m = new Message();
    		public void run() {
    			try {
    				setFaceRec();     		    
    			} catch (Exception e) { 
    				Log.e(TAG, "doLengthyCalc(): " + e.toString());
    			}
    		}
    	};      
    	t.start();        
    }  
	public void setFaceRec() {
		FaceDetector fd;
		FaceDetector.Face [] faces = new FaceDetector.Face[MAX_FACES];
		PointF midpoint = new PointF();
		int [] fpx = null;
		int [] fpy = null;
		int count = 0;
		try {
			fd = new FaceDetector(mFaceWidth, mFaceHeight, MAX_FACES);        
			count = fd.findFaces(mFaceBitmap, faces);
		} catch (Exception e) {
			Log.e(TAG, "setFace(): " + e.toString());
			return;
		}
		if (count > 0) {
			fpx = new int[count];
			fpy = new int[count];

			for (int i = 0; i < count; i++) { 
				try {                 
					faces[i].getMidPoint(midpoint);                  

					fpx[i] = (int)midpoint.x;
					fpy[i] = (int)midpoint.y;
				} catch (Exception e) { 
					Log.e(TAG, "setFace(): face " + i + ": " + e.toString());
				}            
			}      
		}
		imageViewPlus.setDisplayPoints(fpx, fpy, count, 0);
	} 
}
