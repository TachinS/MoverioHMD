package th.in.blackouts.moveriohmd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import th.in.blackouts.facedetection.DoFaceDetection;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

/*
 * @author Tachin Srisombat <jidrids@gmail.com>
 * @version 0.1
 * @since 9-26-2014
 */
@SuppressLint("WrongCall")
public class MainScreenActivity extends ActionBarActivity {
	private Button connectButton;
	private Button disconnectButton;
	private ToggleButton panelSwitch;
	private TextView ipAddressText;
	private GetStreamImage getStreamImage;
	private DoFaceDetection doFaceDetection;
	private ViewFlipper fp;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_main_screen);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		this.context = this;
		if (getStreamImage == null)
			getStreamImage = new GetStreamImage(this);	
		if (doFaceDetection == null)
			doFaceDetection = new DoFaceDetection(this);
		fp = (ViewFlipper) findViewById(R.id.viewFlipper1);
		buttonControl();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	/*
	 * Take a screenshot to device memory path.
	 * 
	 * @param filepath name of target's folder for store an screenshot image
	 */
	public void takeScreenshot(String filepath) {
		String mPath = filepath;// Environment.getExternalStorageDirectory().toString()
								// + "/" + filepath;
		Bitmap bitmap;
		View v1 = this.getWindow().getDecorView();
		v1.setDrawingCacheEnabled(true);
		bitmap = Bitmap.createBitmap(v1.getDrawingCache());
		v1.setDrawingCacheEnabled(false);

		OutputStream fout = null;
		File imageFile = new File(mPath);

		try {
			fout = new FileOutputStream(imageFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
			fout.flush();
			fout.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void buttonControl() {
		connectButton = (Button) findViewById(R.id.connectButton);
		connectButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				getStreamImage.setURL("http://192.168.111.1:8888/tincam.jpg");
				getStreamImage.getImage(fp);
				ipAddressUpdate();
//				imageView = doFaceDetection.process();
//				fp.addView(imageView);

			}
		});
		disconnectButton = (Button) findViewById(R.id.disconnectButton);
		disconnectButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				getStreamImage.setURL("IP ADDRESS");
				ipAddressUpdate();
			}
		});
		panelSwitch = (ToggleButton) findViewById(R.id.panelSwitch);
		panelSwitch.setChecked(true);
		panelSwitch.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (panelSwitch.isChecked()) {
					connectButton.setVisibility(View.VISIBLE);
					disconnectButton.setVisibility(View.VISIBLE);
					ipAddressText.setText(getStreamImage.getURL());
				} else {
					connectButton.setVisibility(View.INVISIBLE);
					disconnectButton.setVisibility(View.INVISIBLE);
					ipAddressText.setText("");
				}
			}
		});

	}
	
	public void ipAddressUpdate() {
		ipAddressText = (TextView) findViewById(R.id.ipAddressText);
		ipAddressText.setText(getStreamImage.getURL());
	}
}
