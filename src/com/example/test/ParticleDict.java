package com.example.test;

import java.util.Random;

import android.graphics.Paint;
//used to store data for drawing particle explosions
public class ParticleDict {
	float partDict[][];
	//stores data for each particle in form:
	//{angle,len,velo}
	float partPos[];//stores center position of each explosion
	float partPeriod;//stores the total time of explosion
	int partTime;//stores creation time of the explosion
	Paint partColor;//stores color used to draw each explosion
	public ParticleDict(float x,float y, float p,int num,Paint paint){//creates each variable
		partPos = new float[2];
		partPos[0] = x;
		partPos[1] = y;
		partColor = paint;
		partPeriod = p;
		partDict = new float[num][3];
		partTime = -1;
		randomize();//calls randomize to randomize velocity and length of each particle
	}
	
	public void pos(float x,float y){//update the position data
		partPos[0] = x;
		partPos[1] = y;
	}
	
	public void randomize(){//randomizes the particle directions/velocities/lengthes
		for (int i = 0; i < partDict.length; i++){
			partDict[i][0] = (i+1)*360/partDict.length;
			partDict[i][1] = (float)(Math.random()*5 + 3);
			partDict[i][2] = (float)(Math.random()*2);
		}
	}
	//standart get/put methods
	public float[][] getDict(){
		return partDict;
	}
	public float[] getPos(){
		return partPos;
	}
	public float getPeriod(){
		return partPeriod;
	}
	public Paint getColor(){
		return partColor;
	}
	public int getTime(){
		return partTime;
	}
	public void setTime(int n){
		partTime = n;
	}
	
}
