package com.avnet.TheProphecy;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.TrackingValues;
import com.metaio.sdk.jni.TrackingValuesVector;

public class TutorialQRCodeReader extends ARViewActivity {

	private MetaioSDKCallbackHandler mCallbackHandler;
	private TextView mTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCallbackHandler = new MetaioSDKCallbackHandler();

		// Set QRCode tracking configuration
		metaioSDK.setTrackingConfiguration("QRCODE");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mTextView = (TextView) mGUIView.findViewById(R.id.code);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mCallbackHandler.delete();
		mCallbackHandler = null;
	}

	@Override
	protected int getGUILayout() {
		return R.layout.tutorial_qr_code_reader;
	}

	@Override
	protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
		return mCallbackHandler;
	}

	final class MetaioSDKCallbackHandler extends IMetaioSDKCallback {

		@Override
		public void onTrackingEvent(TrackingValuesVector trackingValues) {
			for (int i = 0; i < trackingValues.size(); i++) {
				final TrackingValues v = trackingValues.get(i);
				if (v.isTrackingState()) {
					// reading the code
					final String[] tokens = v.getAdditionalValues().split("::");
					if (tokens.length > 1) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								mTextView.setText(tokens[1]);
							}
						});
					}
				}

			}
		}

	}

	public void onButtonClick(View v)
	{
		finish();
	}
	
	@Override
	protected void loadContents() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onGeometryTouched(IGeometry geometry) {
		// TODO Auto-generated method stub
		
	}
}
