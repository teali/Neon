package com.example.test;
 
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.Random;
 
public class DrawView extends View {
	//declares all the variables
    Actors actors;//used to draw actor models
    Canvas draw;//canvas to draw to before updating to screen
    DisplayMetrics metrics;//used to call on length and width data of the device
    Bitmap pane;//reference to bitmap that all graphics are drawn to
    Paint joystick;//color and stoke used to draw the joystick
    Engine game;//engine that handles ALL aspects of the game
    StartMenu menu;//start menu class that controls the start menu
    Context c;//context of the app
    int time,x,y;//time stores the time elapsed since game started, x and y stores the length and width of the screen
    float joyMoveMax;//dictates how far the joystick can move from the relative centers before they are stopped
    float moveSX,shootSX;//stores the center data for left joystick 
    float moveSY,shootSY;//stores the center data for right joystick
    float curMoveX,curShootX;//stores the x,y data for the current x joystick positioning
    float curMoveY,curShootY;//stores the x,y data for the current y joystick positioning
    int menState,gameFlow,gameScore,timeEnd,highScore;
    //menState stores the "menu state"/ if the screen is touched/released/nothing happening
    //gameflow stores the flow of the game which dicates whether to show the menu,game,or lose screen
    //gameScore stores the current score of the current game
    //timeEnd stores when the game has ended
    //highScore stores the local highscore of the app
    float menuX,menuY;//stores the touch position on the start screen
    boolean bg;//signals when to refresh the screen on the start menu
    Paint beforePlay;//stores font data for drawing on menus
    int[] color;//not used but used in later versions for 2*1d gaussian blur
    float[][] in,out;//not used but used in later versions for 2*1d gaussian blur
    SharedPreferences someData;//used to store data
    SharedPreferences.Editor editor;//used to edit stored files
	public static String filename = "score";//filename
    
    public DrawView(Context context) {//constructor method for creating all the variables    	
        super(context);
        highScore = 0;
        c = context;
        bg = false;
        //general initization
        metrics = context.getResources().getDisplayMetrics();
        menu = new StartMenu(context,metrics.widthPixels,metrics.heightPixels);
        pane = Bitmap.createBitmap(metrics.widthPixels,metrics.heightPixels, Bitmap.Config.ARGB_8888);
        draw = new Canvas(pane);
        actors = new Actors();
        joystick = new Paint();
        game = new Engine(0,metrics.widthPixels,metrics.heightPixels,context);
        x = metrics.widthPixels;
        y = metrics.heightPixels;
        time = 0;
        //joystick initization
        moveSX = 100;
        moveSY = metrics.heightPixels-100;
        shootSX = metrics.widthPixels-100;
        shootSY = metrics.heightPixels-100;
        curMoveX = moveSX;
        curMoveY = moveSY;
        curShootX = shootSX;
        curShootY = shootSY;
        joyMoveMax = 50f;
        //menu initization
        menuX = 0;
        menuY = 0;
        menState = -1;
        gameFlow = 0;
        //font initization
        beforePlay = new Paint();//sets font data
        beforePlay.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/scoreboard.ttf"));
        beforePlay.setARGB(255, 103, 200, 255);
        beforePlay.setTextSize(40);
        color = new int[x*y];//not used but used in later versions for 2*1d gaussian blur
        in = new float[3][x*y];//not used but used in later versions for 2*1d gaussian blur
        out = new float[3][x*y];//not used but used in later versions for 2*1d gaussian blur
        someData = context.getSharedPreferences(filename,0);
		editor = someData.edit();
		highScore = someData.getInt(filename, 0);
    }  
    //multi touch joystick pointer indexes
    int moveIndex = -1;
    int shootIndex = -1;
    
    /*raw movement joystick data*/
    float moveRawX = 0;
    float moveRawY = 0;    
    float moveAngle = 0;
    
    /*raw shooting joystick data*/
    float shootRawX = 0;
    float shootRawY = 0;    
    float shootAngle = 0;
    /* x and y data from screen*/
    float rawX[] = {0,0};
    float rawY[] = {0,0};
    double disp[] = {0,0};
    
    @Override    
    public boolean onTouchEvent(MotionEvent e) {//class used for multi touch
    	//stores x,y position for menu touch events
    	menuX = e.getX(0);
    	menuY = e.getY(0);
    	
        int action = e.getActionMasked();//stores the kind of action that is performed
        int actionIndex = e.getActionIndex();//stores index of x,y positions of different touch events
        int pointerNum = e.getPointerCount();//stores the number of touch at current moment
        for (int i = 0; i < pointerNum;i++){//stores the x,y positions for processing
        	rawX[i] = e.getX(i);
        	rawY[i] = e.getY(i);
        	//if statement does calculations to sort out which touch event is for which joystick (this is a lot more tricky then it seems)
        	if (moveIndex == i){//after the touch events have been sorted, some pythagorean theroem is used to calculate distance
            	disp[i] = Math.pow(Math.abs(rawX[i]-moveSX), 2) + Math.pow(Math.abs(rawY[i]-moveSY), 2);
            }else if (shootIndex == i){
            	disp[i] = Math.pow(Math.abs(rawX[i]-shootSX), 2) + Math.pow(Math.abs(rawY[i]-shootSY), 2);
            }
        }
        //switch statement is used to decide what to do based on what touch event is done
        switch (action){
        	case MotionEvent.ACTION_DOWN:case MotionEvent.ACTION_POINTER_DOWN:	//if a new point is currently pressed,it decides which joystick to allocate this
        																		//touch event to based on the regin at which the touch started.
        		menState = 0;
        		if (rawX[actionIndex]< metrics.widthPixels/2){//stores touch event to left joy stick if it starts to the left of the screen
        			moveIndex = actionIndex;
        			//ntested if statement calculates the positioning of the joystick with restrictions of distance from center of joy stick
        			if (disp[actionIndex] > joyMoveMax*joyMoveMax){
        			curMoveX = (float) ((rawX[actionIndex]-moveSX)*joyMoveMax/Math.pow(disp[actionIndex], 0.5) + moveSX);
        			curMoveY = (float) ((rawY[actionIndex]-moveSY)*joyMoveMax/Math.pow(disp[actionIndex], 0.5) + moveSY);
        			}else{
        				curMoveX = rawX[actionIndex];
        				curMoveY = rawY[actionIndex];
        			}
        		}else{        			
        			shootIndex = actionIndex;
        			//ntested if statement calculates the positioning of the joystick with restrictions of distance from center of joy stick
        			if (disp[actionIndex] > joyMoveMax*joyMoveMax){
        			curShootX = (float) ((rawX[actionIndex]-shootSX)*joyMoveMax/Math.pow(disp[actionIndex], 0.5) + shootSX);
        			curShootY = (float) ((rawY[actionIndex]-shootSY)*joyMoveMax/Math.pow(disp[actionIndex], 0.5) + shootSY);
        			}else{
        				curShootX = rawX[actionIndex];
        				curShootY = rawY[actionIndex];
        			}
        		}
        		break;        	
        	case MotionEvent.ACTION_UP://if a point is released at current time, remove the point from the appropriate joystick and reset it's position to center
        		menState = 1;
        		curMoveX = moveSX;
                curMoveY = moveSY;
                curShootX = shootSX;
                curShootY = shootSY;
        		moveIndex = -1;
        		shootIndex = -1;
        		break;
        	case MotionEvent.ACTION_POINTER_UP://if a point is released at current time, remove the point from the appropriate joystick and reset it's position to center
        		if (moveIndex == actionIndex){
        			curMoveX = moveSX;
        	        curMoveY = moveSY;        	        
        	        shootIndex = 0;
        			moveIndex = -1;
        		}else{
        			curShootX = shootSX;
        	        curShootY = shootSY;
        	        moveIndex = 0;
        			shootIndex = -1;        			
        		}        		
        		break;
        	case MotionEvent.ACTION_MOVE: //calculates new position if a current point is dragged to a new position
        		for (int i = 0; i < pointerNum;i++){//for look goes through the 2 points for each joystick
        			//updates potition with some basic math and bounding circles
        			if (moveIndex == i){
        				if (disp[i] > joyMoveMax*joyMoveMax){
                			curMoveX = (float) ((rawX[i]-moveSX)*joyMoveMax/Math.pow(disp[i], 0.5) + moveSX);
                			curMoveY = (float) ((rawY[i]-moveSY)*joyMoveMax/Math.pow(disp[i], 0.5) + moveSY);
                			}else{
                				curMoveX = rawX[i];
                				curMoveY = rawY[i];
                			}
                    }else if (shootIndex == i){
                    	if (disp[i] > joyMoveMax*joyMoveMax){
                			curShootX = (float) ((rawX[i]-shootSX)*joyMoveMax/Math.pow(disp[i], 0.5) + shootSX);
                			curShootY = (float) ((rawY[i]-shootSY)*joyMoveMax/Math.pow(disp[i], 0.5) + shootSY);
                			}else{
                				curShootX = rawX[i];
                				curShootY = rawY[i];
                			}
                    }
                }
        		break;
        	default:       		
        }
        //calcuates the angles of each joystick for directional data
        shootAngle = (float)(Math.atan2((curShootY-shootSY),(curShootX-shootSX))*180/Math.PI);
        moveAngle = (float)(Math.atan2((curMoveY-moveSY),(curMoveX-moveSX))*180/Math.PI);
        //calulates relative x and y instead of absolute x,y co-ordinates
        moveRawX = curMoveX-moveSX;
        moveRawY = curMoveY-moveSY;
        shootRawX = curShootX-shootSX;
        shootRawY = curShootY-shootSY;
        return true;
    }
    /*this function is not used in the current version but is used to implement bloom shader from 2X1D gaussian blurs (FFT gaussian blur/native approach if performance is an issue)
    float[] kernel = {1.0f, 0.8948393168143698f, 0.6411803884299546f, 0.36787944117144233f, 0.1690133154060661f, 0.06217652402211629f};
    float[] weight = {5.2702f,4.3754f,3.73416f,3.3663f,3.1973f};     
    public void bloom(Bitmap mapIn){
    	int lx = x;
    	int ly = y;         
        mapIn.getPixels(color, 0, lx, 0, 0, lx, ly);        
        for (int i = 0; i < ly;i++){
        	for (int j = 0; j < lx;j++){
        		in[0][j+ly*i] = (color[j+ly*i]>>16)&255;
        		in[1][j+ly*i] = (color[j+ly*i]>>8)&255;
        		in[2][j+ly*i] = color[j+ly*i]&255;
        	}
        }        
        mapIn.setPixels(color, 0, x, 0, 0, x, y);    	
    }*/
    
    int tBeforePlay = 30;//time for the "get ready menu"
    @Override
    public void onDraw(Canvas canvas) {  
    	//to save on power for the start menu, the screen is only cleared once at the beginning.
        if (bg == false){
        	draw.drawColor(Color.BLACK);
        	bg = true;
        }
        if (gameFlow == 0 && menu.update(draw,menuX,menuY,menState,highScore)){//if the play button is pressed setes forward the gameflow and starts the game
        	menState = -1;
        	gameFlow = 1;//sets forward the game flow
        	draw.drawColor(Color.BLACK);
        }else if(gameFlow == 1){//starts the game with the "get ready" screen first
        	 time += 1;//increments time
        	 if (time < tBeforePlay){//displays get ready screen for first x amount of seconds during the game
        		 draw.drawText("GET READY", x/2 - 100, y/2 , beforePlay);
        	 }else{//draws the game with joysticks
	        	draw.drawColor(Color.BLACK);//clears screen        	
	            gameScore = game.update(draw, moveRawX, moveRawY, shootRawX, shootRawY, moveAngle,shootAngle,time);//updates the engine that computes EVERYTHING for the game instance.
	            //draws the two joysticks
	            joystick.setARGB(100,100,100,100);
	            joystick.setStrokeWidth(2);
	            joystick.setStyle(Paint.Style.FILL);
	            draw.drawCircle(curMoveX, curMoveY, 50, joystick);
	            draw.drawCircle(curShootX, curShootY, 50, joystick);
	            joystick.setStyle(Paint.Style.STROKE);
	            draw.drawCircle(moveSX, moveSY, 100, joystick);
	            draw.drawCircle(shootSX, shootSY, 100, joystick);
	            //
	            if (gameScore != -1){//if the gamescore is retrieved from the engine, stop the game and start the score menu
	            	gameFlow = 2;
	            	timeEnd = time;
	            }
        	 }
           
         }else if (gameFlow == 2){//when game is finished start the score menu
        	 time += 1;
        	 draw.drawColor(Color.BLACK);  
        	 //displaces all text
        	 beforePlay.setTextSize(40);
    		 draw.drawText("Your Score is: "+ Integer.toString(gameScore),(float)( x/2 - (178+Integer.toString(gameScore).length()*22/2)), y/2 , beforePlay);
    		 if (time-timeEnd < 80){//after a certain amount of time, go back to the start menu
    			 beforePlay.setTextSize(20);
    			 draw.drawText("Exit to menu in  "+ Integer.toString(80- (time-timeEnd)),(float)( x/2 - (89+Integer.toString(gameScore).length()*11/2)), y/2 + 40, beforePlay);
    		 }else{//before going back to the start menu, the gane angine and start menu is refreshed for a new game.
    			 gameFlow = 0;
    			 draw.drawColor(Color.BLACK);
    			 beforePlay.setTextSize(40);
    		     game = new Engine(0,metrics.widthPixels,metrics.heightPixels,c);
    		     menu = new StartMenu(c,metrics.widthPixels,metrics.heightPixels);
    		     menState = -1;
    		     time = 0;
    		     bg = false;
    		     if (gameScore > highScore){//stes the highscore if it has been exceeded
    		    	 highScore = gameScore;
    		    	 editor.putInt(filename, highScore);//saves the highscore
    		    	 editor.commit();
    		     }
    		     gameScore = -1;
    		 }
         }
        
        /*
        */
        //bloom(pane);
        canvas.drawBitmap(pane,0,0,null);//draws the bitmap to screen
        invalidate();//validates canvas and updates to screen
    }
    
    
 
}