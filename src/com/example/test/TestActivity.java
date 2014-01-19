package com.example.test;

import com.example.test.R;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
/*
 * ATTENTION: EXCLUDING THE GENERATED CODE, THIS PROJECT IS WRITTEN 100% FROM SCRATCH	
 */
public class TestActivity extends Activity {
	DrawView drawView;//function that extends the activity and draws to context:This
	MediaPlayer bgMusic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {//function generated when app is started
		super.onCreate(savedInstanceState);
		 drawView = new DrawView(this);//creates the DrawView
	     drawView.setBackgroundColor(Color.WHITE);//sets background color to white
	     setContentView(drawView);//sets drawView to handle drawing to the whole screen
	     drawView.setDrawingCacheEnabled(false);//sets drawing cache to to false to improve performance for animations
	     bgMusic = MediaPlayer.create(this, R.raw.bg_music);
	     bgMusic.start();	 
	     bgMusic.setLooping(true);
	     setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//locks the screen to landscape mode
	     
	}

	@Override
	protected void onPause(){//function called when app is not in foreground of the device
		super.onPause();
		bgMusic.release();
	}


}
