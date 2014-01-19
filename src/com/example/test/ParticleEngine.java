package com.example.test;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
//class draws the particles to screen using the ParticleDict information
public class ParticleEngine {
	
	public ParticleEngine(){//construcor does nothing
	}
	
	public void drawParticle(ParticleDict part,Canvas canvas){
		//all variables are used to localize data from ParticleDict
		float[][] dict = part.getDict();
		float[] pos = part.getPos();
		int time = part.getTime();
		Paint paint = part.getColor();
		for (int i=0; i < dict.length  ;i++){//draws the current particle explosion
			canvas.save(Canvas.MATRIX_SAVE_FLAG);
			canvas.translate(pos[0],pos[1]);//sets position
			canvas.rotate(dict[i][0]);//sets angle of each particle
			if (dict[i][1] > dict[i][2]*time ){//if statement makes sure the right length of each particle is drawn
				canvas.drawLine(0, 0, 0, dict[i][2]*time, paint);
			}else{
				canvas.drawLine(0, dict[i][2]*time-dict[i][1], 0, dict[i][2]*time, paint);
			}
			canvas.restore();
		}
		
	}
}
