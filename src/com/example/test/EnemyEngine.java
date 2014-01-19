package com.example.test;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

public class EnemyEngine {
	float[][] actorPos;//stores position for each character
	int[] actorTime;//stores the time destroyed for each enemy (used for particle collision)
	int[] actorProfile;//stores the ID of the model of each enemy
	float[] actorCol;//stores the collision size of each character
	float enVelo;//a constant deciding the speed of all enemies
	ParticleDict[] particle;//a class that stores all the data needed for particle creation
	ParticleEngine partEng;//a class that draws particles with the ParticleDict info
	int curMax,length,height,interval,max,lvl;
	//length,height :stores dimensions of devices
	//curMax: stores current amount of enemies on the screen
	//interval : interval at which enemies are created
	//max : stores the maximum amount of enemies allowed on the screen at once
	Levels level;//stores level data
	Random rand;//used to generate random numbers
	Actors actor;//stores all information for drawing actors
	public EnemyEngine(int m,int h,int l,int lv, float v,Context context){//creates all variables
		actorPos = new float[m][2];
		actorTime = new int[m];
		actorProfile = new int[m];
		actorCol = new float[m];
		particle = new ParticleDict[m];
		enVelo = v;
		curMax = 0;
		max = m;
		length = l;
		height = h;		
		level = new Levels();
		lvl = lv;
		interval = level.getInterval(lvl);
		rand = new Random();
		actor = new Actors();
		partEng = new ParticleEngine();
	}
	
	private void enPosUpdate(int n,float[] mainPos){
		//enemy collision/physics engine
		float a,b,c,x,y,enSpeed;
		x = -1;
		y = -1;
		//current enemy speed is a percentage distance traveled between enemy and main character calculated using pythagorean theorem
		enSpeed = (float)(Math.sqrt((enVelo*enVelo)/((mainPos[0]-actorPos[n][0])*(mainPos[0]-actorPos[n][0]) + (mainPos[1]-actorPos[n][1])*(mainPos[1]-actorPos[n][1]))));
		for (int i = 0; i < curMax; i++){//checks for collision between all enemies
			a = actorPos[n][0]-actorPos[i][0];
			b = actorPos[n][1]-actorPos[i][1];
			c = actorCol[n]+actorCol[n];
			if (i != n && a*a+b*b <= c*c){//uses pythagorean theorem to check for collision
				if (x < 0) {
				x = -(a/(mainPos[0]-actorPos[n][0]));
				}
				if (y <0){
				y = -(a/(mainPos[1]-actorPos[n][1]));
				}
			}
		}
		//if nothing is touching the current enemy, then increment closer to main character, otherwise, move back
		if (x < 0 && actorPos[n][0] + (mainPos[0]-actorPos[n][0])*enSpeed - actorCol[n] > 0 && actorPos[n][0] + (mainPos[0]-actorPos[n][0])*enSpeed + actorCol[n] < length) {
			actorPos[n][0] += (mainPos[0]-actorPos[n][0])*enSpeed;
		}else{
			actorPos[n][0] -= (mainPos[0]-actorPos[n][0])*0.5/Math.abs((mainPos[0]-actorPos[n][0]));
		}
		//if nothing is touching the current enemy, then increment closer to main character, otherwise, move back
		if (y <0 && actorPos[n][1] + (mainPos[1]-actorPos[n][1])*enSpeed - actorCol[n]> 0 && actorPos[n][1] + (mainPos[1]-actorPos[n][1])*enSpeed + actorCol[n]< height){
			actorPos[n][1] += (mainPos[1]-actorPos[n][1])*enSpeed;
		}else{
			actorPos[n][1] -= (mainPos[0]-actorPos[n][1])*0.5/Math.abs((mainPos[0]-actorPos[n][1]));
		}
		//updates the position of enemy
		actorPos[n][0] = Math.min(Math.max(actorPos[n][0],actorCol[n]),length-actorCol[n]);
		actorPos[n][1] = Math.min(Math.max(actorPos[n][1],actorCol[n]),height-actorCol[n]);
		
	}
	//calculates hypot.
	private static float hypot(float x1,float y1,float x,float y){
        return(float)(x1-=x)*x1+(y1-=y)*y1;
	 }
	//method used to create enemies/draw enemies/ draw explosions/destroys enemies
	public int update(int time,Canvas canvas,float[] mainPos,ShootManage bullets,ScoreManager score){
		float degrees;
		if (time%interval == 0 && curMax < max){// if the interval is met, create a new enemy at a random location
			while (true){//makes sure the location is not close to the main character for instagib (instant death)
				actorPos[curMax][0] = (float)(rand.nextInt(length));
				actorPos[curMax][1] = (float)(rand.nextInt(height));
				if((actorPos[curMax][0]-mainPos[0])*(actorPos[curMax][0]-mainPos[0]) + (actorPos[curMax][1]-mainPos[1])*(actorPos[curMax][1]-mainPos[1]) > 22500 ){
					break;
				}
			}			
			//sets all nessassary variables to create new enemy
			actorProfile[curMax] = level.createActor(lvl);
			actorCol[curMax] = actor.getACol(actorProfile[curMax]);			
			actorTime[curMax] = time;	
			particle[curMax] = new ParticleDict(actorPos[curMax][0],actorPos[curMax][1],60,16,actor.getPaint(actorProfile[curMax]));
			curMax += 1;//sets current amount of enemies on screen to +1
		}
		
		for (int i = 0; i < curMax; i++){	
			//draws the enemy based on what state it is in		
			if (particle[i].getTime() < 0){//if enemy is not hit, draw the enemy		
				degrees = (float)(Math.atan2(mainPos[1]-actorPos[i][1], mainPos[0]-actorPos[i][0])*180/Math.PI);			
				canvas.save(Canvas.MATRIX_SAVE_FLAG);
				actor.drawActor(actorPos[i][0], actorPos[i][1], actorProfile[i], degrees, (float)(time+actorTime[i]), canvas);
				canvas.restore();
				enPosUpdate(i,mainPos);
			}else if (particle[i].getTime() < particle[i].getPeriod()){// if the enemy is hit, draw the particle explosion
				partEng.drawParticle(particle[i],canvas);
				particle[i].setTime(particle[i].getTime()+1);	
			}else{//when the particle explosion is finished, delete enemy
				actorPos[i][0] = actorPos[curMax-1][0];
				actorPos[i][1] = actorPos[curMax-1][1];
				actorProfile[i] = actorProfile[curMax-1];
				actorCol[i] = actorCol[curMax-1];
				actorTime[i] = actorTime[curMax-1];
				particle[i] = particle[curMax-1];
				curMax --;
			}
			if (hypot(mainPos[0],mainPos[1],actorPos[i][0],actorPos[i][1]) < actorCol[i]*actorCol[i]){
				return score.score();//if the main character is hit, end the game
			}
		}
		
		for (int i = 0; i < curMax; i++){//checks for bullet collision with the enemies, and creates particle explosions
			if (particle[i].getTime() < 0 && bullets.collide(actorPos[i][0], actorPos[i][1], actorCol[i])){
				particle[i].setTime(0);
				particle[i].pos(actorPos[i][0], actorPos[i][1]);
				score.update(100, time);
			}
		}
		
		return -1;
	}
}
