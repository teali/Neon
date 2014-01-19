package com.example.test;
import java.util.Random;

public class Levels {
	int[][] lOptions;//stores all models allowed in the level
	int[] lMax;//stores max amount of enemies in the level
	int[] lInterval;//will use in later versions
	int[] lFin;//will use in later versions
	int[] lSpeed;//will use in later versions
	Random rand;//used to generate random numbers
	public Levels(){//creates all variables described above
		lOptions = new int[][]{{0,1,2}};
		lMax = new int[]{20};
		lInterval = new int[]{10};
		lFin = new int[]{100};
		lSpeed = new int[]{2};
		rand = new Random();
	}
	public int createActor(int lvl){//randomly selects a actor ID from the current level ID pool
		return lOptions[lvl][rand.nextInt(lOptions[lvl].length)];
	}
	//standard get methods
	public int getMax(int lvl){
		return lMax[lvl];
	}
	public int getInterval(int lvl){
		return lInterval[lvl];
	}
	public int getFin(int lvl){
		return lFin[lvl];
	}
	public int getSpeed(int lvl){
		return lSpeed[lvl];
	}
}
