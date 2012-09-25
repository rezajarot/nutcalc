package com.gpimports.chicken;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.content.Intent;
import android.util.DisplayMetrics;

public class ChickenToss extends BaseExample {

	private int camera_width;
	private int camera_height;
	private float scaled_width = 1.0f;
	private float scaled_height = 1.0f;
	
	private Camera mCamera;
	
	private Scene mScene;
	private Texture mBackgroundTexture;
	private Texture mMenuButton[];
	private Texture mSoundTexture[];
	
	private TextureRegion mBackgroundTextureRegion;
	private TextureRegion mMenuButtonRegion[];
	private TextureRegion mSoundRegion[];
	
	private Sprite mBackgroundSprite;
	private Sprite mMenuButtonSprite[];
	private Sprite mSoundSprite[];

	private String strImgSuf = new String("med");
	
	public Engine onLoadEngine() {
		getScaledCoordinate();
		this.mCamera = new Camera(0, 0, camera_width, camera_height);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(camera_width, camera_height), this.mCamera));
	}
	
	public void onLoadResources() {
		mBackgroundTexture = new Texture(1024, 1024, TextureOptions.DEFAULT);
		mBackgroundTextureRegion = TextureRegionFactory.createFromAsset(mBackgroundTexture, this, "gfx/" + strImgSuf + "/background_menu.png", 0, 0);
		
		mMenuButton = new Texture[Global.MENU_BUTTON_COUNT];
		mMenuButtonRegion = new TextureRegion[Global.MENU_BUTTON_COUNT];
		
		mSoundTexture = new Texture[2];
		mSoundTexture[0] = new Texture(128, 128, TextureOptions.DEFAULT);
		mSoundTexture[1] = new Texture(128, 128, TextureOptions.DEFAULT);
		
		mSoundRegion = new TextureRegion[2];
		mSoundRegion[0] = TextureRegionFactory.createFromAsset(mSoundTexture[0], this, "gfx/" + strImgSuf + "/sound_off.png", 0, 0);
		mSoundRegion[1] = TextureRegionFactory.createFromAsset(mSoundTexture[1], this, "gfx/" + strImgSuf + "/sound_on.png", 0, 0);
		
		int nIdx = 0;
		String[] str = new String[] {"easy", "medium", "hard", "quit"};
		for(; nIdx < Global.MENU_BUTTON_COUNT; nIdx ++)
		{
			mMenuButton[nIdx] = new Texture(256, 256, TextureOptions.DEFAULT);
			mMenuButtonRegion[nIdx] = TextureRegionFactory.createFromAsset(mMenuButton[nIdx], this, "gfx/" + strImgSuf + "/menu_" + str[nIdx] + "_btn.png", 0, 0);
			mEngine.getTextureManager().loadTexture(mMenuButton[nIdx]);
		}
		mEngine.getTextureManager().loadTextures(mBackgroundTexture, mSoundTexture[0], mSoundTexture[1]);

		String app_dir = this.getFilesDir().getAbsolutePath();
		Global.file = new File(app_dir, "tempfile.tmp");
		if (Global.file.exists())
		{
			FileInputStream fis;
			DataInputStream dis;
			try {
				fis = new FileInputStream(Global.file);
				dis = new DataInputStream(fis);
				Global.gEasyScore = dis.readInt();
				Global.gMiddleScore = dis.readInt();
				Global.gHardScore = dis.readInt();
				dis.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		}
	}
	
	public Scene onLoadScene() {
		mScene = new Scene(2);
		mBackgroundSprite = new Sprite(0, 0, mBackgroundTextureRegion);
		mBackgroundSprite.setSize(mBackgroundTextureRegion.getWidth() * scaled_width, mBackgroundTextureRegion.getHeight() * scaled_height);
		
		mMenuButtonSprite = new Sprite[Global.MENU_BUTTON_COUNT];
		
		loadSoundImage();
		loadMenuButton();
		
		mScene.getLayer(0).addEntity(mBackgroundSprite);
		
		mScene.setOnAreaTouchListener(new IOnAreaTouchListener(){
			
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					ITouchArea pTouchArea, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				Intent intent;
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP)
				{
					if(pTouchArea.equals(mMenuButtonSprite[Global.EASY_BUTTON]))
					{
						intent = new Intent(ChickenToss.this, EasyScene.class);
						ChickenToss.this.startActivity(intent);
					}
					if(pTouchArea.equals(mMenuButtonSprite[Global.MIDDLE_BUTTON]))
					{
						intent = new Intent(ChickenToss.this, MiddleScene.class);
						ChickenToss.this.startActivity(intent);
					}
					if(pTouchArea.equals(mMenuButtonSprite[Global.HARD_BUTTON]))
					{
						intent = new Intent(ChickenToss.this, HardScene.class);
						ChickenToss.this.startActivity(intent);
					}
					if(pTouchArea.equals(mMenuButtonSprite[Global.QUIT_BUTTON]))
					{
						finish();
						System.exit(0);
					}
					
					if(mSoundSprite[Global.SOUN_STATE].contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY()))
					{
						if(Global.SOUN_STATE == 0)
						{
							mScene.getLayer(1).removeEntity(mSoundSprite[0]);
							mScene.getLayer(1).addEntity(mSoundSprite[1]);
							Global.SOUN_STATE = 1;
						}
						else if(Global.SOUN_STATE == 1)
						{
							mScene.getLayer(1).removeEntity(mSoundSprite[1]);
							mScene.getLayer(1).addEntity(mSoundSprite[0]);
							Global.SOUN_STATE = 0;
						}
					}
				}
				return true;
			}
		});
		return mScene;
	}
	
	public void onLoadComplete() {
	}

	private void getScaledCoordinate()
	{
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		
		camera_width = displayMetrics.widthPixels;
		camera_height = displayMetrics.heightPixels;
		
		if (camera_width >= 480)
		{
			strImgSuf = "lg";
			Global.CAMERA_WIDTH = 480.0f;
			Global.CAMERA_HEIGHT = 800.0f;
		} else if (camera_width >= 320) {
			strImgSuf = "med";
			Global.CAMERA_WIDTH = 320.0f;
			Global.CAMERA_HEIGHT = 480.0f;
		}
		
		scaled_width = camera_width / Global.CAMERA_WIDTH;
		scaled_height = camera_height / Global.CAMERA_HEIGHT;
	}
	
	private void loadMenuButton()
	{
		float rWidth, rHeight;
		
		int nIdx = 0;
		for(; nIdx < Global.MENU_BUTTON_COUNT; nIdx ++)
		{
			rWidth = mMenuButtonRegion[nIdx].getWidth();
			rHeight = mMenuButtonRegion[nIdx].getHeight();
			mMenuButtonSprite[nIdx] = new Sprite(0, 0, mMenuButtonRegion[nIdx]);
			mMenuButtonSprite[nIdx].setSize(rWidth * scaled_width, rHeight * scaled_height);
		}
		
		if (strImgSuf == "med") {
			mMenuButtonSprite[0].setPosition(88 * scaled_width, 254 * scaled_height);
			mMenuButtonSprite[1].setPosition(96 * scaled_width, 298 * scaled_height);
			mMenuButtonSprite[2].setPosition(90 * scaled_width, 344 * scaled_height);
			mMenuButtonSprite[3].setPosition(98 * scaled_width, 387 * scaled_height);
		} else if (strImgSuf == "lg") {
			mMenuButtonSprite[0].setPosition(132 * scaled_width, 421 * scaled_height);
			mMenuButtonSprite[1].setPosition(144 * scaled_width, 487 * scaled_height);
			mMenuButtonSprite[2].setPosition(135 * scaled_width, 556 * scaled_height);
			mMenuButtonSprite[3].setPosition(146 * scaled_width, 619 * scaled_height);
		}
		
		for(nIdx = 0; nIdx < Global.MENU_BUTTON_COUNT; nIdx ++)
		{
			mScene.getLayer(1).addEntity(mMenuButtonSprite[nIdx]);
			mScene.getLayer(1).registerTouchArea(mMenuButtonSprite[nIdx]);
		}
	}
	
	private void loadSoundImage()
	{
		mSoundSprite = new Sprite[2];
		mSoundSprite[0] = new Sprite(0, 0, mSoundRegion[0]);
		mSoundSprite[1] = new Sprite(0, 0, mSoundRegion[1]);
		mSoundSprite[0].setSize(mSoundRegion[0].getWidth() * scaled_width, mSoundRegion[0].getHeight() * scaled_height);
		mSoundSprite[1].setSize(mSoundRegion[1].getWidth() * scaled_width, mSoundRegion[1].getHeight() * scaled_height);
		if (strImgSuf == "med") {
			mSoundSprite[0].setPosition((320 - mSoundRegion[0].getWidth() - 50) * scaled_width, 80 * scaled_height);
			mSoundSprite[1].setPosition((320 - mSoundRegion[0].getWidth() - 50) * scaled_width, 80 * scaled_height);
		} else if (strImgSuf == "lg") {
			mSoundSprite[0].setPosition((480 - mSoundRegion[0].getWidth() - 70) * scaled_width, 160 * scaled_height);
			mSoundSprite[1].setPosition((480 - mSoundRegion[0].getWidth() - 70) * scaled_width, 160 * scaled_height);
		}
		mScene.getLayer(1).addEntity(mSoundSprite[Global.SOUN_STATE]);
		mScene.getLayer(1).registerTouchArea(mSoundSprite[0]);
		mScene.getLayer(1).registerTouchArea(mSoundSprite[1]);
	}
}