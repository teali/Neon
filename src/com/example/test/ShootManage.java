package com.example.test;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;

public class ShootManage {//this class stores all information for the bullets. It draws and calculates angle,speed, and collision
	float[][] bPos;//stores all positions of bullets
	float[][] bVelo;//stores the x,y velocities of bullets
	int bMax,btInterval;
	//bMax : the max amount of bullets currently on the screen (used for GC tricks)
	//interval at which the bullets are created
	float vConst,w,h,rAngle;
	//vConstant :dictates the speed of the bullets
	//w,h :stores dimension data
	//rAngle:temp variable to store angle of the bullets
	Paint brush;//stores the color,stroke of the bullets
	
	public ShootManage(int n,float width,float height,int interval){//creates all variables described above with appropriate fields
		bPos = new float[n][2];
		bVelo = new float[n][2];
		bMax = 0;	
		vConst = 10;
		brush = new Paint();
		brush.setARGB(255, 243, 243, 21);
		brush.setStyle(Style.FILL);
		w = width;
		h = height;
		btInterval = interval;
	}
	
	//this class creates bullets with the appropriate x,y velocities calculated based on the quadrant it appears in
	public void createBullet(float angle,float backAngle,float x, float y,float sX,float sY,float mX,float mY){
		
	/*	the speed and direction of the bullet is decided with priority to the bullet joystick.
	 * if the bullet joystick is not being moved, the bullet direct defaults to the direction of the movement joystick
	*/
		if (sX == 0 && sY == 0){//if the bullet joystick is not moved, use movement joystick data to calculate speeds				
			rAngle = (float)(backAngle*Math.PI/180);
			//does math based on quadrants
			if (backAngle < 0 && backAngle > -90){
				bVelo[bMax][0] = Math.abs((float)(vConst*Math.cos(rAngle)));
				bVelo[bMax][1] = -Math.abs((float)(vConst*Math.sin(rAngle)));
			}else if (backAngle < -90 ){
				bVelo[bMax][0] = -Math.abs((float)(vConst*Math.cos(rAngle)));
				bVelo[bMax][1] = -Math.abs((float)(vConst*Math.sin(rAngle)));
			}else if (backAngle > 90){
				bVelo[bMax][0] = -Math.abs((float)(vConst*Math.cos(rAngle)));
				bVelo[bMax][1] = Math.abs((float)(vConst*Math.sin(rAngle)));
			}else if (backAngle > 0 && backAngle < 90){
				bVelo[bMax][0] = Math.abs((float)(vConst*Math.cos(rAngle)));
				bVelo[bMax][1] = Math.abs((float)(vConst*Math.sin(rAngle)));
			}
		}else{//if the bullet joystick is being used, use that data instead
			rAngle = (float)(angle*Math.PI/180);
			if (angle < 0 && angle > -90){
				bVelo[bMax][0] = Math.abs((float)(vConst*Math.cos(rAngle)));
				bVelo[bMax][1] = -Math.abs((float)(vConst*Math.sin(rAngle)));
			}else if (angle < -90 ){
				bVelo[bMax][0] = -Math.abs((float)(vConst*Math.cos(rAngle)));
				bVelo[bMax][1] = -Math.abs((float)(vConst*Math.sin(rAngle)));
			}else if (angle > 90){
				bVelo[bMax][0] = -Math.abs((float)(vConst*Math.cos(rAngle)));
				bVelo[bMax][1] = Math.abs((float)(vConst*Math.sin(rAngle)));
			}else if (angle > 0 && angle < 90){
				bVelo[bMax][0] = Math.abs((float)(vConst*Math.cos(rAngle)));
				bVelo[bMax][1] = Math.abs((float)(vConst*Math.sin(rAngle)));
			}
		}
		
		bPos[bMax][0] = x;
		bPos[bMax][1] = y;
		//if ANY joystick is being moved, then create this bullet, otherwise the character is not being moved so create no bullet
		if ((mX == 0  && mY == 0) == false || (sX == 0  && sY == 0) == false){			
			bMax++;//increments current bullets on screen by 1		
		}
		
		
	}
	
	private void deleteBullet(int n){//deletes the bullet by doing some GC trick and not actually deleting the bullet
		//swaps the bullet at the top of the array with the position of the bullet that is being deleted so there is minimal variable copying and creation.
		bPos[n][0] = bPos[bMax-1][0];
		bPos[n][1] = bPos[bMax-1][1];
		bVelo[n][0] = bVelo[bMax-1][0];
		bVelo[n][1] = bVelo[bMax-1][1];
		bMax -= 1;
	}
	//The next 4 methods are used to calculate POINT-LINE SEGMENT (note that this is actually a lot more complex then POINT LINE collision)
	 private static float dot(float x1,float y1,float x,float y,float x2,float y2){//dot product of p1,float p2 wrt origin p
         return(x1-x)*(x2-x)+(y1-y)*(y2-y);
	 }
	 private static float cross(float x1,float y1,float x,float y,float x2,float y2){//cross product of p1,float p2 wrt origin p
         return(x1-x)*(y2-y)-(y1-y)*(x2-x);
	 }
	 private static float hypot(float x1,float y1,float x,float y){
         return(float)Math.sqrt((x1-=x)*x1+(y1-=y)*y1);
	 }
	 private static float doit(float x1,float y1,float x,float y,float x2,float y2){//segment p1,float p2 and point p
         if(dot(x1,y1,x2,y2,x,y)<=0)//p is too far off p2
                 return hypot(x2,y2,x,y);
         if(dot(x2,y2,x1,y1,x,y)<=0)//p is too far off p1
                 return hypot(x1,y1,x,y);
         return Math.abs(cross(x1,y1,x,y,x2,y2))/hypot(x1,y1,x2,y2);
	 }
	public boolean collide(float x,float y,float r){//checks if the point x,y collides with ANY bullets and delets the bullet if it does collide
		float x1,y1,x2,y2;
		for (int i = 0; i < bMax; i++){//checks for collision and calls deleteBullet();
			x1 = bPos[i][0];
			y1 = bPos[i][1];
			x2 = bPos[i][0] + bVelo[i][0];
			y2 = bPos[i][1] + bVelo[i][1];
			if (doit(x1,y1,x,y,x2,y2) <= r){
				deleteBullet(i);
				return true;
			}
		}
		return false;
	}
	//updates the screen with current bullets and increments movement
	public void updateBullet(Canvas canvas,int time,float moveX ,float moveY,float shootX,float shootY,float moveAngle, float shootAngle,float[] mainPos){
		//if the time meants the time interval for creating a bullet then create the bullet
		if (time % btInterval == 0){
			createBullet(shootAngle, moveAngle,mainPos[0], mainPos[1],shootX,shootY,moveX,moveY);
		}
		
		for (int i = 0; i < bMax; i++){//draws all bullets that are currently on the screen
			canvas.save(Canvas.MATRIX_SAVE_FLAG);
			canvas.drawCircle(bPos[i][0], bPos[i][1], 2, brush);
			canvas.restore();
			bPos[i][0] += bVelo[i][0];
			bPos[i][1] += bVelo[i][1];
			
		}
		
		for (int i = 0; i < bMax; i++){//checks if the bullet has touched the edges of the screen, if so then delete bullet.
			if (bPos[i][0] < 0 || bPos[i][0] > w || bPos[i][1] < 0 || bPos[i][1] > h){
				deleteBullet(i);
			}
		}
	}
}
