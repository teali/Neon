package com.example.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;

public class StartMenu {//this class is in charge of the start menu
	Paint paint;//stores the font information for the titles used in the start menu
	Paint credit;//stores font used for the credit and high scores displayed
	int length,height;//stores dimensions of screen
	Typeface font;//temp variable for storing fonts
	RectF border;//stores the region of the border around the Play button
	
	public StartMenu(Context context, int l, int h){//initiates all variables with appropriate fields
		//sets font data
		font = Typeface.createFromAsset(context.getAssets(),"fonts/scoreboard.ttf");
		paint = new Paint();
		credit = new Paint();
		paint.setTypeface(font);
		paint.setARGB(255, 103, 200, 255);
		paint.setStyle(Style.STROKE);
		font = Typeface.createFromAsset(context.getAssets(),"fonts/credit.otf");
		credit.setTypeface(font);
		credit.setARGB(255, 100, 100, 100);
		credit.setTextSize(20);
		//sets dimension data
		length = l;
		height = h;
		//sets button border data
		border = new RectF(length/2 - 60, height/2 + 35,length/2 +65, height/2 +90 );
	}
	
	public boolean update(Canvas canvas,float x,float y,int state,int score){//updates the menu every frame
		paint.setTextSize(100);//sets size of the title
		canvas.drawText("NEON", length/2 - 100, height/2 , paint);//draws the title		
		//draws credits
		canvas.drawText("Head Programmer: Tony Li", 0, height-25, credit);
		canvas.drawText("Co-designer/Programmer: Tommy Jung", 0, height-5, credit);
		//if statement to check if the play button has been pressed.
		if (x > length/2 - 80 && x < length/2 +85 && y > height/2 + 15 && y < height/2 +110 ){//the touch event is in the button region		
			if (state == 0 ){//checks if touch event is currently HOLD DOWN or RELEASE to decide either to light up button or exit the menu to start the game
				paint.setARGB(255, 255, 255, 255);
				paint.setTextSize(50);
				canvas.drawText("PLAY", length/2 - 50, height/2 + 80 , paint);
				canvas.drawRoundRect(border, 10, 10, paint);
						
			}else if (state == 1){
				return true;//game should start
			}
		}
		//draws the play button
		paint.setTextSize(50);
		canvas.drawText("PLAY", length/2 - 50, height/2 + 80 , paint);
		canvas.drawRoundRect(border, 10, 10, paint);
		paint.setARGB(255, 103, 200, 255);	
		paint.setTextSize(25);
		//draws the high score
		canvas.drawText("High Score: "+ Integer.toString(score),(float)( length - (110+Integer.toString(score).length()*9.3)), height - 20 , credit);
		return false;//game should not start yet
	}
}
