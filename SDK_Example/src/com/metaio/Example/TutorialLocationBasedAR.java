// Copyright 2007-2013 metaio GmbH. All rights reserved.
package com.metaio.Example;

import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.MetaioDebug;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.IRadar;
import com.metaio.sdk.jni.LLACoordinate;
import com.metaio.sdk.jni.Vector3d;
import com.metaio.tools.io.AssetsManager;

public class TutorialLocationBasedAR extends ARViewActivity 
{

	/**
	 * Geometries
	 */
	private IGeometry mGeometryLondon;
	private IGeometry mGeometryMunich;
	private IGeometry mGeometryRome;
	private IGeometry mGeometryTokyo;
	private IGeometry mGeometryParis;
	
	private IRadar mRadar;

	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		// Set GPS tracking configuration
		// The GPS tracking configuration must be set on user-interface thread
		boolean result = metaioSDK.setTrackingConfiguration("GPS");  
		MetaioDebug.log("Tracking data loaded: " + result);  
	}
	
	public void onButtonClick(View v)
	{
		finish();
	}

	@Override
	protected int getGUILayout() 
	{
		return R.layout.tutorial_location_based_ar;
	}

	@Override
	protected IMetaioSDKCallback getMetaioSDKCallbackHandler() 
	{
		return null;
	}

	@Override
	protected void loadContents() 
	{
			
		try
		{
			metaioSDK.setLLAObjectRenderingLimits(300, 400);
			String metaioManModel = AssetsManager.getAssetPath("TutorialLocationBasedAR/Assets/metaioman.md2");			
			if (metaioManModel != null) 
			{
				mGeometryMunich = metaioSDK.createGeometry(metaioManModel);
				LLACoordinate munich = new LLACoordinate(48.160879, 11.552156, 0, 0);
				if (mGeometryMunich != null) 
				{
					mGeometryMunich.setScale(new Vector3d(5.0f, 5.0f, 5.0f));				
					mGeometryMunich.setTranslationLLA(munich);
				}
				else
					MetaioDebug.log(Log.ERROR, "Error loading geometry: "+metaioManModel);

			}
			
			String filepath = AssetsManager.getAssetPath("TutorialLocationBasedAR/Assets/POI_bg.png");
			if (filepath != null) 
			{
				mGeometryLondon = metaioSDK.createGeometryFromImage(createBillboardTexture("London"), true);
				mGeometryParis = metaioSDK.createGeometryFromImage(createBillboardTexture("Paris"), true);
				mGeometryTokyo = metaioSDK.createGeometryFromImage(createBillboardTexture("Tokyo"), true);
				mGeometryRome = metaioSDK.createGeometryFromImage(createBillboardTexture("Rome"), true);

				LLACoordinate london = new LLACoordinate(51.50661, -0.130463, 0, 0);
				LLACoordinate tokyo = new LLACoordinate(35.657464, 139.773865, 0, 0);
				LLACoordinate rome = new LLACoordinate(41.90177, 12.45987, 0, 0);
				LLACoordinate paris = new LLACoordinate(48.85658, 2.348671, 0, 0);
				

				mGeometryLondon.setTranslationLLA(london);
				mGeometryRome.setTranslationLLA(rome);
				mGeometryParis.setTranslationLLA(paris);
				mGeometryTokyo.setTranslationLLA(tokyo);
			}
			
			// create radar
			mRadar = metaioSDK.createRadar();
			mRadar.setBackgroundTexture(AssetsManager.getAssetPath("TutorialLocationBasedAR/Assets/radar.png"));
			mRadar.setObjectsDefaultTexture(AssetsManager.getAssetPath("TutorialLocationBasedAR/Assets/yellow.png"));
			mRadar.setRelativeToScreen(IGeometry.ANCHOR_TL);
							
			// add geometries to the radar
			mRadar.add(mGeometryLondon);
			mRadar.add(mGeometryMunich);
			mRadar.add(mGeometryTokyo);
			mRadar.add(mGeometryParis);
			mRadar.add(mGeometryRome);
			
		}
		
		catch (Exception e)
		{
			 MetaioDebug.log("Exception: " + e.getMessage());
		}
		
	}
	
	private String createBillboardTexture(String billBoardTitle)
    {
           try
           {
                  final String texturepath = getCacheDir() + "/" + billBoardTitle + ".png";
                  Paint mPaint = new Paint();

                  // Load background image (256x128), and make a mutable copy
                  Bitmap billboard = null;
                  
                  //reading billboard background
                  String filepath = AssetsManager.getAssetPath("TutorialLocationBasedAR/Assets/POI_bg.png");
                  Bitmap mBackgroundImage = BitmapFactory.decodeFile(filepath);
                  
                  billboard = mBackgroundImage.copy(Bitmap.Config.ARGB_8888, true);


                  Canvas c = new Canvas(billboard);

                  mPaint.setColor(Color.WHITE);
                  mPaint.setTextSize(24);
                  mPaint.setTypeface(Typeface.DEFAULT);

                  float y = 40;
                  float x = 30;

                  // Draw POI name
                  if (billBoardTitle.length() > 0)
                  {
                        String n = billBoardTitle.trim();

                        final int maxWidth = 160;

                        int i = mPaint.breakText(n, true, maxWidth, null);
                        c.drawText(n.substring(0, i), x, y, mPaint);

                        // Draw second line if valid
                        if (i < n.length())
                        {
                               n = n.substring(i);
                               y += 20;
                               i = mPaint.breakText(n, true, maxWidth, null);

                               if (i < n.length())
                               {
                                      i = mPaint.breakText(n, true, maxWidth - 20, null);
                                      c.drawText(n.substring(0, i) + "...", x, y, mPaint);
                               } else
                               {
                                      c.drawText(n.substring(0, i), x, y, mPaint);
                               }
                        }

                  }


                  // writing file
                  try
                  {
                	  FileOutputStream out = new FileOutputStream(texturepath);
                      billboard.compress(Bitmap.CompressFormat.PNG, 90, out);
                      MetaioDebug.log("Texture file is saved to "+texturepath);
                      return texturepath;
                  } catch (Exception e) {
                      MetaioDebug.log("Failed to save texture file");
                	  e.printStackTrace();
                   }
                 
                  billboard.recycle();
                  billboard = null;

           } catch (Exception e)
           {
                  MetaioDebug.log("Error creating billboard texture: " + e.getMessage());
                  MetaioDebug.printStackTrace(Log.DEBUG, e);
                  return null;
           }
           return null;
    }
	
	@Override
	protected void onGeometryTouched(final IGeometry geometry) 
	{
		MetaioDebug.log("Geometry selected: "+geometry);
		
		mSurfaceView.queueEvent(new Runnable()
		{

			@Override
			public void run() 
			{
				mRadar.setObjectsDefaultTexture(AssetsManager.getAssetPath("TutorialLocationBasedAR/Assets/yellow.png"));
				mRadar.setObjectTexture(geometry, AssetsManager.getAssetPath("TutorialLocationBasedAR/Assets/red.png"));
			}
		
				
		});
	}

}
