package com.example.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
//class is used to modify/draw the score onto screen
public class ScoreManager {
	int score,multi,time,threshold;
	//score: stores the score
	//multi: stores the current multiplier
	//time: stores the current time (used to modify multiplier)
	//threshold: stores the time needed to activate the multiplier
	Paint paint;//stores the font/color of the score
	Typeface font;//stores the font
	public ScoreManager(int thres,int sc,Context context){//creates all variables described above
		
		score = sc;
		multi = 1;
		threshold = thres;
		font = Typeface.createFromAsset(context.getAssets(),"fonts/scoreboard.ttf");		
		paint = new Paint();
		paint.setTypeface(font);
		paint.setARGB(255, 103, 200, 255);
		paint.setTextSize(40);
	}
	
	public void update(int s,int newtime){//updates the score with "s" being the score value and newtime being the time
		
		score += s*multi;//calculates the score with multiplier
		if ( newtime - time <= threshold){//checks if the multiplyer should go up or down
			multi += 1;
		}else{
			multi = Math.max(multi-1, 1);
		}
		time = newtime;
	}
	
	public int score(){//gets the score
		return score;
	}
	public void draw(Canvas canvas){//draws the score onto screen
		canvas.drawText("00000000".substring(0, 7-Integer.toString(score).length())+Integer.toString(score) , 10, 30, paint);
		canvas.drawText("X"+ Integer.toString(multi), 170, 30, paint);
	}
	
}
