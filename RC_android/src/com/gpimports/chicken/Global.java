package com.gpimports.chicken;

import java.io.File;

public class Global {
	// constant
	public static float CAMERA_WIDTH = 320.0f;
	public static float CAMERA_HEIGHT = 480.0f;
	public static final int MENU_BUTTON_COUNT = 4;
	
	//menu button constant
	public static final int EASY_BUTTON = 0;
	public static final int MIDDLE_BUTTON = 1;
	public static final int HARD_BUTTON = 2;
	public static final int QUIT_BUTTON = 3;
	
	//font size constant
	public static final int FONT_SIZE_SOUND	= 32;
	public static final int FONT_SIZE_ARROW = 20;
	
	//sound on/off flag
	public static int SOUN_STATE = 1;
	public final static int SOUND_ON = 1;
	public final static int SOUND_OFF = 0;
	
	//xml save /field name
	public static final String EASY_LEVEL = "EASY_LEVEL";
	public static final String MIDDLE_LEVEL = "MIDDLE_LEVEL";
	public static final String HARD_LEVEL = "HARD_LEVEL";
	
	//scores
	public static int gEasyScore = 0;
	public static int gMiddleScore = 0;
	public static int gHardScore = 0;
	
	public static File file = null;
	
	//
	public static boolean mIsStart = true;
	public static final int LV_1 = 0;
	public static final int LV_2 = 1;
	public static final int LV_3 = 2;
	
	//sound index
	public static final int BACK_SOUND = 0;
	public static final int SOUND_STATE_IN = 1;
	public static final int SOUND_STATE_SIDE_IN	= 2;
	public static final int SOUND_STATE_RING_IN = 3;
	public static final int SOUND_STATE_RING_OUT = 4;
	public static final int SOUND_STATE_OUT = 5;
	public static final int SOUND_BONUS = 6;
}
