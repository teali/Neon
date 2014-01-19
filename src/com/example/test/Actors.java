package com.example.test;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
//this can be argued to be the soul and core of the game as it draws ALL models including their animation. A lot of time was spent on this class to improve performance.
public class Actors {
	//all lines are in format {x1,y1,x2,y2} and each model includes multiple lines
	/*Draw Lines*/
	private float[][][] actorsLine= {	
			{{-12.0f,0.0f,-4.0f,8.0f},{-4.0f,8.0f,4.0f,0.0f},{4.0f,0.0f,-4.0f,-8.0f},{-4.0f,-8.0f,-12.0f,0.0f},{0.0f,0.0f,4.0f,4.0f},{4.0f,4.0f,8.0f,0.0f},{8.0f,0.0f,4.0f,-4.0f},{4.0f,-4.0f,0.0f,0.0f},{-12.0f,0.0f,-2.0f,8.0f},{-2.0f,8.0f,2.0f,0.0f},{2.0f,0.0f,-6.0f,-8.0f},{-6.0f,-8.0f,-12.0f,0.0f},{1.0f,0.0f,4.0f,4.0f},{4.0f,4.0f,8.0f,-1.0f},{8.0f,-1.0f,4.0f,-4.0f},{4.0f,-4.0f,1.0f,0.0f}},
			{{0,0,0,0}},
			{{-2.0f,2.0f,2.0f,2.0f},{2.0f,2.0f,2.0f,-2.0f},{2.0f,-2.0f,-2.0f,-2.0f},{-2.0f,-2.0f,-2.0f,2.0f},{-2.0f,2.0f,0.0f,0.0f},{-2.0f,2.0f,0.0f,0.0f},{2.0f,2.0f,0.0f,0.0f},{2.0f,2.0f,0.0f,0.0f},{2.0f,-2.0f,0.0f,0.0f},{2.0f,-2.0f,0.0f,0.0f},{-2.0f,-2.0f,0.0f,0.0f},{-2.0f,-2.0f,0.0f,0.0f},{0.0f,0.0f,0.0f,0.0f},{0.0f,0.0f,0.0f,0.0f},{0.0f,0.0f,0.0f,0.0f},{0.0f,0.0f,0.0f,0.0f},{0.0f,0.0f,0.0f,0.0f},{0.0f,0.0f,0.0f,0.0f},{0.0f,0.0f,0.0f,0.0f},{0.0f,0.0f,0.0f,0.0f}},
			{{12f,0f,0f,10f},{0f,10f,-12f,0f},{-12f,0f,0f,-10f},{0f,-10f,12f,0f}}
			};
	/*animation displacements*/
	private float[][][] actorsLineAnimate= {
			{{5.0f,0.0f,5.0f,0.0f},{5.0f,0.0f,5.0f,0.0f},{5.0f,0.0f,5.0f,0.0f},{5.0f,0.0f,5.0f,0.0f},{-14.0f,0.0f,-14.0f,0.0f},{-14.0f,0.0f,-14.0f,0.0f},{-14.0f,0.0f,-14.0f,0.0f},{-14.0f,0.0f,-14.0f,0.0f},{5.0f,0.0f,2.0f,0.0f},{2.0f,0.0f,8.0f,0.0f},{8.0f,0.0f,10.0f,0.0f},{10.0f,0.0f,5.0f,0.0f},{-15.0f,0.0f,-15.0f,0.0f},{-15.0f,0.0f,-14.0f,2.0f},{-14.0f,2.0f,-15.0f,0.0f},{-15.0f,0.0f,-15.0f,0.0f}},
			{{0,0,0,0}},
			{{0.5f,-0.5f,-0.5f,-0.5f},{-0.5f,-0.5f,-0.5f,0.5f},{-0.5f,0.5f,0.5f,0.5f},{0.5f,0.5f,0.5f,-0.5f},{0.5f,-0.5f,-4.0f,-7.0f},{0.5f,-0.5f,-7.0f,-4.0f},{-0.5f,-0.5f,-7.0f,4.0f},{-0.5f,-0.5f,-4.0f,7.0f},{-0.5f,0.5f,4.0f,7.0f},{-0.5f,0.5f,7.0f,4.0f},{0.5f,0.5f,7.0f,-4.0f},{0.5f,0.5f,4.0f,-7.0f},{7.0f,-4.0f,7.0f,4.0f},{-7.0f,-4.0f,-7.0f,4.0f},{-4.0f,7.0f,4.0f,7.0f},{-4.0f,-7.0f,4.0f,-7.0f},{-7.0f,4.0f,-4.0f,7.0f},{4.0f,7.0f,7.0f,4.0f},{7.0f,-4.0f,4.0f,-7.0f},{-7.0f,-4.0f,-4.0f,-7.0f}},
			{{3f,0f,0f,-2f},{0f,-2f,-3f,0f},{-3f,0f,0f,2f},{0f,2f,3f,0f}}
			};
	
	/*period of animation*/
	private float[] actorsLinePeriod= {	
			60f,
			60f,
			60f,
			60f
			};
	//all circles are in format {x,y,r}
	/*Draw Circles*/
	private float[][][] actorsCircle= {	
			{{0f,0f,0f}},
			{{0,0,10},{6,0,4},{8,0,2}},
			{{0f,0f,0f}},
			{{0f,0f,4f}}
			};
	/*Animation displacement of circles*/
	private float[][][] actorsCircleAnimate = {	
			{{0f,0f,0f}},
			{{0,0,0},{-6,0,0},{-8,0,0}},
			{{0f,0f,0f}},
			{{0f,0f,0f}}
			};
	/*period of circles*/
	
	private float[] actorsCirclePeriod= {
			60f,
			60f,
			60f,	
			60f
			};
	private float[] actorsCol={10,10,10,10};//creates the collision size of each character
	private Paint[] actColor = new Paint[actorsCircle.length];//creates the color of each model
	
	public Actors(){
		for (int i = 0; i<actColor.length; i++){//sets anti alias on , sets the circles to be outlines instead of fills, and sets the drawing mode to screen
			actColor[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
			actColor[i].setStyle(Style.STROKE);
			actColor[i].setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
		}	
		//sets all the colors for the actors
		actColor[0].setARGB(255, 131, 245, 44);		
		actColor[1].setARGB(255, 103, 200, 255);
		actColor[2].setARGB(255, 255, 0, 102);
		actColor[3].setARGB(255, 131, 245, 44);
		
	}
	public float getACol(int n){//gets collision size
		return actorsCol[n];
	}
	public Paint getPaint(int n){//gets color
		return actColor[n];
	}
	
	public final void drawActor(float x, float y,int actNum,float angle,float t,Canvas canvas){//draws the model ID selected with it's animation interval
		//these variables are used to localize all data accessed to improve performance
		float xDis1,yDis1,xDis2,yDis2,xDis,yDis,rDis;
		float[][] actL = actorsLine[actNum];
		float[][] actLA = actorsLineAnimate[actNum];		
		float[][] actCA = actorsCircleAnimate[actNum];
		float[][] actC = actorsCircle[actNum];
		float actCP = actorsCirclePeriod[actNum];
		float actLP = actorsLinePeriod[actNum];
		Paint color = actColor[actNum];
		double theta = 360*t/actLP;//stores the angle of model
		float cosTheta =  (float)(-Math.cos((double)(theta))+1);//stores the cos of theta to reduce further uses of cos()
		//translation and rotation
		canvas.translate(x,y);
		canvas.rotate(angle);
		//draw lines
		
		for (int i = 0;i < actL.length;i++){//draws all lines with the current animation frame based on a sinusoidal movement
			
			float[] ActLAi = actLA[i];
			xDis1 = ActLAi[0]*cosTheta;
			yDis1 = ActLAi[1]*cosTheta;
			xDis2 = ActLAi[2]*cosTheta;
			yDis2 = ActLAi[3]*cosTheta;
			
			float[] actLi = actL[i];
			canvas.drawLine(actLi[0]+xDis1,
					actLi[1]+yDis1,
					actLi[2]+xDis2,
					actLi[3]+yDis2,
							color);
		}
		//draw circles
		theta = 360*t/actCP;
		cosTheta =  (float)(-Math.cos((double)(theta))+1);
		for (int i = 0;i < actC.length;i++){//draws all circles with the current animation frame based on a sinusoidal movement	
			
			float[] actCAi = actCA[i];
			xDis = actCAi[0]*cosTheta;
			yDis = actCAi[1]*cosTheta;
			rDis = actCAi[2]*cosTheta;
			float[] actCi = actC[i];
			
			canvas.drawCircle(	actCi[0]+xDis,
								actCi[1]+yDis,
								actCi[2]+rDis,
								color);
		}
	}
	
}