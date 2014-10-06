package th.in.blackouts.moveriohmd;

import java.util.Timer;
import java.util.TimerTask;

import th.in.blackouts.facedetection.DoFaceDetection;

import android.app.Activity;
import android.content.Context;
import android.widget.ViewFlipper;

/*
 * Class for get Stream image from web camera.
 * 
 * @author Tachin Srisombat
 * 
 */
public class GetStreamImage extends Activity {

	private Context context;
	private Timer autoUpdate;
	private String url;
	private DoFaceDetection doFaceDetection;
	private ViewFlipper viewFlipper;

	public GetStreamImage(Context context) {
		this.context = context;
		doFaceDetection = new DoFaceDetection(this.context);
	}

	public void getImage(ViewFlipper fp) {
		this.viewFlipper = fp;
		viewFlipper.addView(doFaceDetection.process());
		timerSetup();
	}

	public void timerSetup() {
		autoUpdate = new Timer();
		autoUpdate.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(viewFlipper != null)
							viewFlipper.removeAllViews();
						viewFlipper.addView(doFaceDetection.process());
					}
				});
			}
		}, 0, 2000);// refresh rate time interval (ms)

	}

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}
}
