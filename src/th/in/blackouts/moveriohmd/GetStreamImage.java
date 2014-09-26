package th.in.blackouts.moveriohmd;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
	private WebView webView;

	public GetStreamImage(Context context) {
		this.context = context;
	}

	public void getImage(WebView webView) {
		this.webView = webView;
		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
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
						webView.loadUrl(url);
					}
				});
			}
		}, 0, 500);// refresh rate time interval (ms)

	}

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}
}
