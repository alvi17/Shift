/*
 * Copyright (C) 2010 Florian Sundermann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package alvi17.shift;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.transitions.CCTransitionScene;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class Main extends Activity {
	
	private CCGLSurfaceView mGLSurfaceView;
	
	public static final float SUPPOSED_WIN_WIDTH  = 320; 
	public static final float SUPPOSED_WIN_HEIGHT = 480;
	public static float SCREEN_HEIGHT;
	public static float SCALE_X;
	public static float SCALE_Y;
	public static float SCALE;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	MyResources.setContext(this);
    	// set the window status, no tile, full screen and don't sleep
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    	mGLSurfaceView = new CCGLSurfaceView(this);
    	// 32 bit
    	mGLSurfaceView.setEGLConfigChooser(8,8,8,8,0,0);
    	mGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    	
    	calcScreenDimensions();

    	CCDirector.sharedDirector().setScreenSize(SUPPOSED_WIN_WIDTH, SUPPOSED_WIN_HEIGHT);
        CCDirector.sharedDirector().setDeviceOrientation(CCDirector.kCCDeviceOrientationPortrait);
        CCDirector.sharedDirector().setDisplayFPS(false);
        CCDirector.sharedDirector().attachInView(mGLSurfaceView);
        
	    setContentView(mGLSurfaceView);
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
		try
		{
			CCScene running = CCDirector.sharedDirector().getRunningScene();
			if (running != null) {
				for (CCNode node : running.getChildren()) {
					if (node instanceof KeyHandler)
						if (((KeyHandler)node).HandleKeyEvent(event))
							return true;
				}
			}
		}
		catch(Exception e)
		{
			// Could not dispatch Key event...
		}
    	return super.dispatchKeyEvent(event);		
    }
    
    
    private void calcScreenDimensions() {
        final DisplayMetrics pDisplayMetrics = new DisplayMetrics();
		CCDirector.sharedDirector().getActivity().getWindowManager().getDefaultDisplay().getMetrics(pDisplayMetrics);
		
		SCREEN_HEIGHT = pDisplayMetrics.heightPixels;
		SCALE_X = pDisplayMetrics.widthPixels / SUPPOSED_WIN_WIDTH;
		SCALE_Y = pDisplayMetrics.heightPixels / SUPPOSED_WIN_HEIGHT;
		
		final float mRatio = (float)SUPPOSED_WIN_WIDTH / SUPPOSED_WIN_HEIGHT;
		final float realRatio = (float)pDisplayMetrics.widthPixels / pDisplayMetrics.heightPixels;

		if(realRatio < mRatio) {
			SCALE = SCALE_X;
		} else {
			SCALE = SCALE_Y;
		}
	}
      
    public static CCTransitionScene getTransisionFor(CCScene scene) {
    	return CCFadeTransition.transition(0.5f, scene);
    }
    
    @Override
    public void onStart() {
        super.onStart(); 
        // frames per second
        CCDirector.sharedDirector().setAnimationInterval(1.0f / 60);       
        if (Game.Current().getIsPlaying())
        	CCDirector.sharedDirector().runWithScene(Board.scene());
        else
        	CCDirector.sharedDirector().runWithScene(MainMenu.scene());
    }

    @Override
    public void onPause() {
    	try{
    		super.onPause();
        	CCDirector.sharedDirector().pause();
    	} catch (Exception ex){}
    }

    @Override
    public void onResume() {
    	try
    	{
    		super.onResume();
        	CCDirector.sharedDirector().resume();
    	} catch (Exception ex){}
    }

    @Override
    public void onDestroy() {
    	try {
    		super.onStop();
        	CCDirector.sharedDirector().end();
    	} catch (Exception ex){}
    }
    
}