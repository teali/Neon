package com.example.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;
//this class controls everything that is going on for the game. This includes: enemies, particle explosions, bullets, boundaries, screen shifting, score, main character
public class Engine {
	float[] mainPos;//stores the main position of the main character
	float mainVelo;//stores the main velocity of the main character
	int l,length,height;//stores the screen dimension data
	EnemyEngine enemy;//class used to control enemies as well as particle explosions
	Levels lvls;//stores lvl data (mainly used for later versions of the game)
	Actors actor;//class used to draw ALL characters on the screen
	ShootManage bullet;//stores bullet information for drawing and collision detection
	int endGame;//outputs other then -1 to signify the score as well as end of the game	
	ScoreManager score;//used to manage scores
	Paint border;//draws the boundaries for the game screen
	int offset;//stores amount offset from side of the screen so that the main character will never reach the side of the screen
	float tX,tY;//stores the translation data of x,y to move the screen
	

	
	public Engine(int lvl,int wid,int len,Context context){//creates all the variables/instantiates them		
		lvls = new Levels();
		actor = new Actors();
		mainPos = new float[]{400,200};		;
		bullet = new ShootManage(200,wid,len,5);
		l = lvl;	
		enemy = new EnemyEngine(lvls.getMax(lvl),len,wid,l,2f,context);
		mainVelo = 0.1f;
		length = wid;
		height = len;
		score = new ScoreManager(5,0,context);
		border = new Paint();
		border.setARGB(255, 100, 100, 100);
		offset = 75;
		
	}
	
	public int update(Canvas canvas,float moveX ,float moveY,float shootX,float shootY,float moveAngle, float shootAngle,int time){
		//checks if character is too close to the sides of the screen, if so, the whole screen is offset so character is never too close to the edge
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		if (mainPos[1] > height - 200){
			tY = -(mainPos[1] - height + 200);
		}else if (mainPos[1] < offset){
			tY = offset - mainPos[1];
		}
		if (mainPos[0] > length - offset){
			tX = -(mainPos[0] - length + offset);
		}else if (mainPos[0] < offset){
			tX = offset - mainPos[0];
		}
		canvas.translate(tX, tY);//translates the offset
		//draws the border lines if there is an offset
		canvas.drawLine(-1, height, length, height, border);
		canvas.drawLine(length, height, length,-1, border);
		canvas.drawLine(length, -1, -1, -1, border);
		canvas.drawLine(-1,-1, -1, height, border);
		//draws main character
		mainPos[0] = Math.min(Math.max(mainPos[0] + moveX*mainVelo,0+10),length-10);//calculates position
		mainPos[1] = Math.min(Math.max(mainPos[1] + moveY*mainVelo,0+10),height-10);
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		actor.drawActor(mainPos[0],mainPos[1],3,moveAngle,time,canvas);//draws model of main character
		canvas.restore();		
		endGame = enemy.update(time,canvas,mainPos,bullet,score);//updates the enemy engine and draws enemies
		 if (endGame != -1){//checks if game is done (player has lost)
			 canvas.restore();
			 return endGame;
		 }
		bullet.updateBullet(canvas,time,moveX,moveY,shootX,shootY,moveAngle,shootAngle,mainPos);//updates the bullets
		canvas.restore();
		score.draw(canvas);//updates score
		return -1;//returns -1 if game is not done		
	}
}
