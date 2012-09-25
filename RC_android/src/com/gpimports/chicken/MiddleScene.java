package com.gpimports.chicken;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.DisplayMetrics;

public class MiddleScene extends BaseExample{

	private static int camera_width;
	private static int camera_height;
	private float scaled_width = 1.0f;
	private float scaled_height = 1.0f;
	
	private Camera mCamera;
	
	private Texture mMiddleBackgoundTexture;
	private Texture mArrowTexture;
	private Texture mScoreFontTexture;
	private Texture mPaperTexture;
	private Texture mWindArrowTexture[];
	private Texture mMenuTexture;
	private Texture mRecycleTexture;
	private Texture mScoreBoardTexture;
	private Texture mWindValueTexture;
	
	private TextureRegion mMiddleBackgroundRegion;
	private TextureRegion mArrowTextureRegion;
	private TiledTextureRegion mPaperTextureRegion;
	private TextureRegion mWindArrowRegion[];
	private TextureRegion mMenuRegion;
	private TextureRegion mRecycleTextureRegion;
	private TextureRegion mScoreBoardRegion;
	private TextureRegion mWindValueRegion;
	
	private Font mFont;
	private ChangeableText mScoreText;
	private ChangeableText mBestText;

	private Sprite mMiddleBackgroundSprite;
	private Sprite mArrowSprite;
	private TiledSprite mPaperSprite;
	private Sprite mWindArrowSprite[];
	private Sprite mMenuSprite;
	private Sprite mRecycleSprite;
	private Sprite mScoreBoardSprite;
	private Sprite mWindValueSprite;
	
	private Scene mScene;
	
	private PointF ptScore, ptBest;
	private PointF ptPaper;
	public static int mScore = 0;
	public static int mBest = 0;
	
	private TimerHandler handle, fanHandle;
	private int mFanPosition;
	private final int FAN_POSITION_LEFT = 0;
	private final int FAN_POSITION_RIGHT = 1;
	
	private float m_rFan_A;
	private ChangeableText mSpeedText;
	
	private TrackMaker mTrackMaker;
	private float mAlpha = 0.0f;
	private boolean mMove = false;
	private boolean mGoal = false;
	private static boolean m_fSubmit = false;
	private int m_nFrameNum = 0;
	
	private int[] m_nCenterX, m_nCenterY, m_nBallIndex, nState;
	private float[] m_rScale;
	private float m_rInitPaperSize;
	private RectF mPaperInitRect;
	
	private float initWidth = 0.0f;
	private float initHeight = 0.0f;
	private Music mMiddleBackMusic = null;
	private Sound mSounds[];
	
	private String strImgSuf = new String("med");
	
	private boolean mTopPaper = false;
	
	public Engine onLoadEngine() {
		getScaledCoordinate();
		this.mCamera = new Camera(0, 0, camera_width, camera_height);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(camera_width, camera_height), this.mCamera).setNeedsMusic(true).setNeedsSound(true));
	}

	public void onLoadResources() {
		mMiddleBackgoundTexture = new Texture(1024, 1024, TextureOptions.DEFAULT);
		mArrowTexture = new Texture(256, 256, TextureOptions.DEFAULT);
		mScoreFontTexture = new Texture(256, 256, TextureOptions.DEFAULT);
		mPaperTexture = new Texture(2048, 256, TextureOptions.DEFAULT);
		mMenuTexture = new Texture(256, 256, TextureOptions.DEFAULT);
		mRecycleTexture = new Texture(256, 256, TextureOptions.DEFAULT);
		mScoreBoardTexture = new Texture(256, 256, TextureOptions.DEFAULT);
		mWindValueTexture = new Texture(128, 128, TextureOptions.DEFAULT);
		
		mWindArrowTexture = new Texture[2];
		mWindArrowTexture[0] = new Texture(256, 256, TextureOptions.DEFAULT);
		mWindArrowTexture[1] = new Texture(256, 256, TextureOptions.DEFAULT);
		
		mMiddleBackgroundRegion = TextureRegionFactory.createFromAsset(mMiddleBackgoundTexture, this, "gfx/" + strImgSuf + "/background_lvl2.png", 0, 0);
		mArrowTextureRegion = TextureRegionFactory.createFromAsset(mArrowTexture, this, "gfx/" + strImgSuf + "/arrow.png", 0, 0);
		mPaperTextureRegion = TextureRegionFactory.createTiledFromAsset( mPaperTexture, this, "gfx/" + strImgSuf + "/chicken.png", 0, 0, 8, 1 );
		mMenuRegion = TextureRegionFactory.createFromAsset(mMenuTexture, this, "gfx/" + strImgSuf + "/menu.png", 0, 0);
		mRecycleTextureRegion = TextureRegionFactory.createFromAsset(mRecycleTexture, this, "gfx/" + strImgSuf + "/bucket_lvl2.png", 0, 0);
		mScoreBoardRegion = TextureRegionFactory.createFromAsset(mScoreBoardTexture, this, "gfx/" + strImgSuf + "/score.png", 0, 0);
		mWindValueRegion = TextureRegionFactory.createFromAsset(mWindValueTexture, this, "gfx/" + strImgSuf + "/breeze_box.png", 0, 0);
		
		mWindArrowRegion = new TextureRegion[2];
		mWindArrowRegion[0] = TextureRegionFactory.createFromAsset(mWindArrowTexture[0], this, "gfx/" + strImgSuf + "/breeze_R.png", 0, 0);
		mWindArrowRegion[1] = TextureRegionFactory.createFromAsset(mWindArrowTexture[1], this, "gfx/" + strImgSuf + "/breeze_L.png", 0, 0);
		
		m_rInitPaperSize = mPaperTextureRegion.getWidth() * scaled_width;

		int fontsize = (strImgSuf == "lg") ? 24 : 16;
		mFont = new Font(this.mScoreFontTexture, Typeface.createFromAsset(this.getAssets(), "font/berthold-city-light.ttf"), fontsize * scaled_width, true, Color.WHITE);

		loadSound();
		
		mWindArrowSprite = new Sprite[2];
		
		mEngine.getTextureManager().loadTextures(mMiddleBackgoundTexture, mArrowTexture, mScoreBoardTexture, mWindValueTexture,
				mScoreFontTexture, mPaperTexture, mWindArrowTexture[0], mWindArrowTexture[1], mMenuTexture, mRecycleTexture);
		
		mEngine.getFontManager().loadFonts(mFont);
	}
	
	public Scene onLoadScene() {
		mScene = new Scene(5);
		
		loadTrackMaker();
		
		setLayer_0();
		setLayer_1();
		setLayer_2();
		setLayer_3();
		
		playSound(Global.BACK_SOUND);
		
		initGame();
		
		fanHandle = new TimerHandler(1 / 60.0f, true, new ITimerCallback(){
			public void onTimePassed(final TimerHandler pTimerHandler) {
				startPlay();
			}
		});
		
		handle = new TimerHandler(1 / 30.0f, true, new ITimerCallback(){
			public void onTimePassed(final TimerHandler pTimerHandler) {
				movePaper();
			}
		});
		
		mScene.registerUpdateHandler(fanHandle);
		
		mScene.getLayer(0).registerTouchArea(mMenuSprite);
		mScene.getLayer(0).registerTouchArea(mMiddleBackgroundSprite);
		mScene.getLayer(3).registerTouchArea(mPaperSprite);

		mScene.setOnAreaTouchListener(new IOnAreaTouchListener(){
			boolean paper_click = false;
			
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					ITouchArea pTouchArea, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				if( !paper_click )
				{
					if(pTouchArea.equals(mMenuSprite))
					{
						stopSound(Global.BACK_SOUND);
						MiddleScene.this.finish();
					}
					if( mPaperInitRect.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY()) )
						paper_click = true;
				}
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN)
				{
				}
				else if( pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP )
				{
					if( paper_click )
					{
						// get paper position from each frame
						if( !calcAlpha( pSceneTouchEvent.getX(), pSceneTouchEvent.getY() ) )
						{
							paper_click = false;
							return false;
						}
						if (mTrackMaker.setThrowAngle(mAlpha) == 0)
						{
							paper_click = false;
							return false;
						}
						
						float rAlpha = mAlpha * 180 / 3.14f;
						mArrowSprite.setRotation(90 - rAlpha);
						
						mMove = true;
						paper_click = false;
						mScene.registerUpdateHandler(handle);
					}
				}
				return true;
			}
		});
		return mScene;
	}

	public void onLoadComplete() {
	}
	
	private void initGame()
	{
		randomDirectionAndSpeed();
		updateEntityDirAndSpeed( false );
		
		int nDirect;
		
		nDirect = mFanPosition == FAN_POSITION_LEFT ? 1 : -1;
		
		mTrackMaker.setWindSpeed(nDirect, m_rFan_A);
		
		mPaperSprite.setCurrentTileIndex(0);
		mPaperSprite.setSize(initWidth, initHeight);
		float x = (ptPaper.x-mPaperSprite.getWidth()/2);
		float y = (ptPaper.y-mPaperSprite.getHeight()/2);
		mPaperSprite.setPosition(x, y);

		mMove = false;
		mGoal = false;
		
		mScene.getLayer(4).removeEntity(mRecycleSprite);

		m_nFrameNum = 0;
		mPaperInitRect = new RectF(mPaperSprite.getX(), mPaperSprite.getY(), 
				mPaperSprite.getX()+mPaperSprite.getWidth(),
				mPaperSprite.getY()+mPaperSprite.getHeight());
	}

	private void getScaledCoordinate()
	{
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		
		camera_width = displayMetrics.widthPixels;
		camera_height = displayMetrics.heightPixels;
		
		if (camera_width >= 480)
			strImgSuf = "lg";
		else if (camera_width >= 320)
			strImgSuf = "med";
		
		scaled_width = camera_width / Global.CAMERA_WIDTH;
		scaled_height = camera_height / Global.CAMERA_HEIGHT;
	}
	
	private void loadSound()
	{
		mSounds = new Sound[6];
		try{
			mMiddleBackMusic = MusicFactory.createMusicFromAsset(mEngine.getMusicManager(), this, "mfx/Level_Medium_Background.mp3");
			mSounds[0] = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "mfx/Level_Medium_BounceIn.mp3");
			mSounds[1] = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "mfx/BounceOut.mp3");
			mSounds[2] = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "mfx/BounceOut.mp3");
			mSounds[3] = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "mfx/BounceOut.mp3");
			mSounds[4] = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "mfx/Level_Medium_Miss.mp3");
			mSounds[5] = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "mfx/DogBark2Loud.mp3");
		} catch(final IOException e) {
		}
	}
	
	private void setLayer_3() {
		mPaperSprite = new TiledSprite(0, 0, mPaperTextureRegion);
		initWidth = mPaperSprite.getWidth()*scaled_width;
		initHeight = mPaperSprite.getHeight()*scaled_height;
		float x = (ptPaper.x-initWidth/2);
		float y = (ptPaper.y-initHeight/2);
		mPaperSprite.setPosition(x, y);
		mScene.getLayer(3).addEntity(mPaperSprite);
	}

	private void setLayer_2() {
		if (strImgSuf == "med") {
			mArrowSprite = new Sprite(96 * scaled_width, 364 * scaled_height, mArrowTextureRegion);
			
			mWindArrowSprite[0] = new Sprite(107 * scaled_width, 312 * scaled_height, mWindArrowRegion[0]);
			mWindArrowSprite[1] = new Sprite(107 * scaled_width, 312 * scaled_height, mWindArrowRegion[1]);
		} else {
			mArrowSprite = new Sprite(145 * scaled_width, 623 * scaled_height, mArrowTextureRegion);
			
			mWindArrowSprite[0] = new Sprite(160 * scaled_width, 512 * scaled_height, mWindArrowRegion[0]);
			mWindArrowSprite[1] = new Sprite(160 * scaled_width, 512 * scaled_height, mWindArrowRegion[1]);
		}

		mArrowSprite.setSize(mArrowTextureRegion.getWidth() * scaled_width, mArrowTextureRegion.getHeight() * scaled_height);
		mWindArrowSprite[0].setSize(mWindArrowRegion[0].getWidth() * scaled_width, mWindArrowRegion[0].getHeight() * scaled_height);
		mWindArrowSprite[1].setSize(mWindArrowRegion[1].getWidth() * scaled_width, mWindArrowRegion[1].getHeight() * scaled_height);

		mScene.getLayer(2).addEntity(mArrowSprite);
		
		randomDirectionAndSpeed();
		
		updateEntityDirAndSpeed( true );
	}

	private void setLayer_1() {
		if (strImgSuf == "med") {
			mRecycleSprite = new Sprite(138 * scaled_width, 207 * scaled_height, mRecycleTextureRegion);
		} else {
			mRecycleSprite = new Sprite(207 * scaled_width, 349 * scaled_height, mRecycleTextureRegion);
		}
		mRecycleSprite.setSize(mRecycleTextureRegion.getWidth() * scaled_width, mRecycleTextureRegion.getHeight() * scaled_height);
		mScene.getLayer(1).addEntity(mRecycleSprite);

		if (strImgSuf == "med") {
			ptScore = new PointF(25, 17);
			ptBest = new PointF(25, 46);
		} else {
			ptScore = new PointF(45, 33);
			ptBest = new PointF(45, 73);
		}
		mBest = Global.gMiddleScore;
		initText(mScore, mBest);
	}

	private void setLayer_0()
	{
		// background
		mMiddleBackgroundSprite = new Sprite(0, 0, mMiddleBackgroundRegion);
		mMiddleBackgroundSprite.setSize(mMiddleBackgroundRegion.getWidth() * scaled_width, mMiddleBackgroundRegion.getHeight() * scaled_height);
		mScene.getLayer(0).addEntity(mMiddleBackgroundSprite);
		
		// menu
		if (strImgSuf == "med") {
			mMenuSprite = new Sprite(15 * scaled_width, 430 * scaled_height, mMenuRegion);
			mScoreBoardSprite = new Sprite(10 * scaled_width, 10 * scaled_height, mScoreBoardRegion);
			mWindValueSprite = new Sprite(141 * scaled_width, 284 * scaled_height, mWindValueRegion);
		} else {
			mMenuSprite = new Sprite(28 * scaled_width, 728 * scaled_height, mMenuRegion);
			mScoreBoardSprite = new Sprite(21 * scaled_width, 21 * scaled_height, mScoreBoardRegion);
			mWindValueSprite = new Sprite(211 * scaled_width, 468 * scaled_height, mWindValueRegion);
		}
			
		mMenuSprite.setSize(mMenuRegion.getWidth() * scaled_width, mMenuRegion.getHeight() * scaled_width);
		mScoreBoardSprite.setSize(mScoreBoardRegion.getWidth() * scaled_width, mScoreBoardRegion.getHeight() * scaled_height);
		mWindValueSprite.setSize(mWindValueRegion.getWidth() * scaled_width, mWindValueRegion.getHeight() * scaled_height);
		
		mScene.getLayer(0).addEntity(mMenuSprite);
		mScene.getLayer(0).addEntity(mScoreBoardSprite);
		mScene.getLayer(0).addEntity(mWindValueSprite);
	}
	
	private void randomDirectionAndSpeed()
	{
		m_rFan_A = 0.0f;
		
		randomDirection();
		
		randomSpeed();
	}
	
	private void updateEntityDirAndSpeed(boolean first)
	{
		//float fontsize = 14 + 2 * m_rFan_A * scaled_width;
		Texture mBreezeFontTexture = new Texture(256, 256, TextureOptions.DEFAULT);
		int fontsize = (strImgSuf == "med") ? 18 : 27;
		Font font = new Font(mBreezeFontTexture, Typeface.createFromAsset(this.getAssets(), "font/berthold-city-light.ttf"), fontsize * scaled_width, true, Color.WHITE);
		mEngine.getFontManager().loadFont(font);
		mEngine.getTextureManager().loadTexture(mBreezeFontTexture);
		
		if (!first)
			mScene.getLayer(2).removeEntity(mSpeedText);

		String str = String.format("%.2f", m_rFan_A);
		if (strImgSuf == "med") {
			mSpeedText = new ChangeableText(145 * scaled_width, 285 * scaled_height, font, str);
		} else {
			mSpeedText = new ChangeableText(218 * scaled_width, 470 * scaled_height, font, str);
		}
		
		if( first )
		{
			if( mFanPosition == FAN_POSITION_LEFT )
			{
				//mScene.getLayer(1).addEntity(mFanLeftSprite[0]);
				mScene.getLayer(2).addEntity(mWindArrowSprite[0]);
			}
			else
			{
				//mScene.getLayer(1).addEntity(mFanRightSprite[0]);
				mScene.getLayer(2).addEntity(mWindArrowSprite[1]);
				//mWindArrowSprite[1].setRotation(180.0f);
			}
			mScene.getLayer(2).addEntity(mSpeedText);
		}
		else
		{
			if( mFanPosition == FAN_POSITION_LEFT )
			{
				//mScene.getLayer(1).setEntity(0, mFanLeftSprite[0]);
				mScene.getLayer(2).setEntity(1, mWindArrowSprite[0]);
				//mWindArrowSprite.setRotation(360.0f);
			}
			else
			{
				//mScene.getLayer(1).setEntity(0, mFanRightSprite[0]);
				mScene.getLayer(2).setEntity(1, mWindArrowSprite[1]);
				//mWindArrowSprite.setRotation(180.0f);
			}
			//String str = String.format("%.2f", m_rFan_A);
			//mSpeedText.setText(str);
			mScene.getLayer(2).addEntity(mSpeedText);
		}
	}

	private void loadTrackMaker()
	{
		mTrackMaker = new TrackMaker(scaled_width, scaled_height);
		mTrackMaker.setLevel(Global.LV_2);
		
		int x = mTrackMaker.m_levelInfo.nPaperInitX;
		int y = mTrackMaker.m_levelInfo.nPaperInitY;
		ptPaper = new PointF(x, y); // center position
	}
	
	private void playSound(int nType)
	{
		if(Global.SOUN_STATE == Global.SOUND_OFF)
			return;
		
		switch(nType)
		{
		case 0:
			mMiddleBackMusic.play();
			break;
		case Global.SOUND_STATE_IN:
			mSounds[0].play();
			break;
		case Global.SOUND_STATE_SIDE_IN:
			mSounds[1].play();
			break;
		case Global.SOUND_STATE_RING_IN:
			mSounds[2].play();
			break;
		case Global.SOUND_STATE_RING_OUT:
			mSounds[3].play();
			break;
		case Global.SOUND_STATE_OUT:
			mSounds[4].play();
			break;
		case Global.SOUND_BONUS:
			mSounds[5].play();
			break;
		default:
			break;
		}
	}
	
	private void repeatSound()
	{
		if(Global.SOUN_STATE == Global.SOUND_OFF)
			return;
		
		if(!mMiddleBackMusic.isPlaying())
		{
			mMiddleBackMusic.play();
		}
	}

	private void stopSound(int nType)
	{
		if(Global.SOUN_STATE == Global.SOUND_OFF)
			return;
		
		switch(nType)
		{
		case 0:
			mMiddleBackMusic.stop();
			break;
		}
	}
	
	private void startPlay()
	{
		repeatSound();
	}
	
	private void movePaper()
	{
		if ( !mMove )
			return;
		m_nFrameNum ++;
		
		m_nCenterX = new int[1];
		m_nCenterY = new int[1];
		m_nBallIndex = new int[1];
		nState = new int[1];
		m_rScale = new float[1];
		
		if( m_nFrameNum == mTrackMaker.UP_FRAME_NUM )
		{
			mScene.getLayer(1).removeEntity(mRecycleSprite);
			mScene.getLayer(4).addEntity(mRecycleSprite);
			mScene.getLayer(3).removeEntity(mPaperSprite);
			mScene.getLayer(0).addEntity(mPaperSprite);
			mTopPaper = true;
		}
		mTrackMaker.getTrack(m_nFrameNum, m_nCenterX, m_nCenterY, m_rScale, m_nBallIndex, nState);
		
		if(Global.SOUN_STATE == Global.SOUND_ON && nState[0] != 0)
			playSound(nState[0]);
		
		if (mTrackMaker.getSuccess())
		{
			mGoal = true;
		}
		
		if (m_nFrameNum < mTrackMaker.getRealFrameNum())
		{
			calcBallInfo();			
		}
		
		int nScoreChange;
		
		if (m_nFrameNum == mTrackMaker.getRealFrameNum())
		{
			mArrowSprite.setRotation(0);
			nScoreChange = mScore;
			
			if (mGoal)
			{
				mScore ++;
				mBest = (mScore > mBest) ? mBest + 1 : mBest;			
				
				m_fSubmit = true;
			}
			else
			{
				mScore = 0;
			}
			
			if (m_fSubmit && (mScore != nScoreChange))
			{
				updateText(mScore, mBest);
				if (mScore == 5)
				{
					playSound(Global.SOUND_BONUS);
				}
			}
			
			mScene.unregisterUpdateHandler(handle);
			mScene.getLayer(0).removeEntity(mPaperSprite);
			mScene.getLayer(3).addEntity(mPaperSprite);
			mScene.getLayer(4).removeEntity(mRecycleSprite);
			mScene.getLayer(1).addEntity(mRecycleSprite);
			mTopPaper = false;
			initGame();
		}	
	}

	private void randomDirection()
	{
		int nDir = -1;

		nDir = (int)(Math.random() * 100) % 2;
		mFanPosition = (nDir == 0) ? FAN_POSITION_LEFT : FAN_POSITION_RIGHT;
	}
	
	private void randomSpeed()
	{
		int i = (int)(Math.random() * 100) % 6;
		int j = (int)(Math.random() * 1000) % 100;		
		float rTemp = j / 100.0f;
		m_rFan_A = i + rTemp;
	}
	
	private void initText(int score, int best)
	{
		mScoreText = new ChangeableText(ptScore.x * scaled_width, ptScore.y * scaled_height, this.mFont, "SCORE   000");
		mBestText = new ChangeableText(ptBest.x * scaled_width, ptBest.y * scaled_height, this.mFont, "BEST    000");
		mScoreText.setColor(1.0f, 1.0f, 1.0f);
		mBestText.setColor(1.0f, 1.0f, 1.0f);

		mScene.getLayer(1).addEntity(mScoreText);
		mScene.getLayer(1).addEntity(mBestText);

		mScoreText.setText("SCORE  " + mScore);
		mBestText.setText("BEST    " + mBest);
	}
	
	private void updateText(int score, int best)
	{
		mScore = score;
		mBest = best;
		mScoreText.setText("SCORE  " + mScore);
		mBestText.setText("BEST    " + mBest);
		Global.gMiddleScore = mBest;
		
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(Global.file));
			dos.writeInt(Global.gEasyScore);
			dos.writeInt(Global.gMiddleScore);
			dos.writeInt(Global.gHardScore);
			dos.close();
		} catch (Exception e) {}
	}
	
	private boolean calcAlpha(float touchX, float touchY)
	{
		int x = mTrackMaker.m_levelInfo.nPaperInitX;
		int y = mTrackMaker.m_levelInfo.nPaperInitY;
		PointF pos = new PointF(x, y); // center position

		double rw = touchX - pos.x;
		double rh = touchY - pos.y;
		
		if (rh >= 0)
			return false;
		
		rh = -rh; 
		mAlpha = (float)Math.atan2(rh, rw);
		
		return true;
	}
	
	private void calcBallInfo()
	{
		float rWidth;
		
		mPaperSprite.setCurrentTileIndex(m_nBallIndex[0]);
		float x = m_nCenterX[0]-mPaperSprite.getWidth()/2;
		float y = m_nCenterY[0]-mPaperSprite.getHeight()/2;
		mPaperSprite.setPosition(x, y);
		rWidth = m_rInitPaperSize/8 * m_rScale[0];
		mPaperSprite.setSize(rWidth, rWidth);
		if( !mTopPaper )
			mScene.getLayer(3).setEntity(0, mPaperSprite);
		//else
			//mScene.getLayer(0).setEntity(3, mPaperSprite);
	}
}
