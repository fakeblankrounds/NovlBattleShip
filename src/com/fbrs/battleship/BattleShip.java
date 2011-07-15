package com.fbrs.battleship;

import java.io.IOException;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.ZoomCamera;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchController;
import org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector;
import org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.anddev.andengine.extension.input.touch.exception.MultiTouchException;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.input.touch.detector.ScrollDetector;
import org.anddev.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.anddev.andengine.input.touch.detector.SurfaceScrollDetector;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.widget.Toast;

import com.fbrs.game.rebound.render.AnimationBuilder;
import com.fbrs.game.rebound.render.TextureLoad;
import com.fbrs.rebound.abstraction.AnimationFactory;
import com.fbrs.rebound.abstraction.TextureLoader;
import com.fbrs.rebound.map.MapLoad;
import com.fbrs.rebound.map.MapMaker;

public class BattleShip extends BaseGameActivity implements IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener{
	
	public static BattleShip main;
	
	public static final int CAM_W = 800;
	public static final int CAM_H = 480;  
	public static Engine mEngine;
	public static Scene scene;
	public static HUD hud;
	public static ZoomCamera mZoomCamera;
	private SurfaceScrollDetector mScrollDetector;
	private PinchZoomDetector mPinchZoomDetector;
	private float mPinchZoomStartedCameraZoomFactor;
	
	private MapMaker playermap = new MapMaker();
	private MapMaker othermap = new MapMaker();

	@Override
	public Engine onLoadEngine() {
		BattleShip.mZoomCamera = new ZoomCamera(0, 0, CAM_W, CAM_H);
		
		hud = new HUD();
		BattleShip.mZoomCamera.setHUD(hud);
		//mZoomCamera.setZoomFactor(pZoomFactor)

		mEngine = new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAM_W, CAM_H), BattleShip.mZoomCamera).setNeedsMusic(true));

		main = this;

		try {
			if(MultiTouch.isSupported(this)) {
				mEngine.setTouchController(new MultiTouchController());
			} else {
				Toast.makeText(this, "Sorry your device does NOT support MultiTouch!\n\n(No PinchZoom is possible!)", Toast.LENGTH_LONG).show();
			}
		} catch (final MultiTouchException e) {
			Toast.makeText(this, "Sorry your Android Version does NOT support MultiTouch!\n\n(No PinchZoom is possible!)", Toast.LENGTH_LONG).show();
		}

		return mEngine;
	}

	@Override
	public void onLoadResources() {
		//load each sprite img.

		TextureLoader.setTextureLoader(new TextureLoad());
		AnimationFactory.SetImplementer(new AnimationBuilder());
	}

	@Override
	public Scene onLoadScene() {
		mEngine.registerUpdateHandler(new FPSLogger());
		scene = new Scene(1);

		scene.setBackground(new ColorBackground(0, 0f, 0.3f));

		this.mScrollDetector = new SurfaceScrollDetector(this);
		if(MultiTouch.isSupportedByAndroidVersion()) {
			try {
				this.mPinchZoomDetector = new PinchZoomDetector(this);
			} catch (final MultiTouchException e) {
				this.mPinchZoomDetector = null;
			}
		} else {
			this.mPinchZoomDetector = null;
		}

		scene.setOnSceneTouchListener(this);
		scene.setTouchAreaBindingEnabled(true);

		return scene;
	}

	@Override
	public void onLoadComplete() {
		
		TextureLoader.newSprite(512, 512, 760, 380, 0, 2.7f, 0, "bg", null);
		TextureLoader.newSprite(512, 512, -370, 50, 0, 1.4f, 0, "bg", null);

		try {
			playermap.ParseFile(BattleShip.main.getAssets().open(
					"scripts/scripts.tct"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//playermap.map.addMapClick(new GameState());
		playermap.map.addDrawOffset(-464, -40);
		playermap.map.setMapScale(0.5f);
		playermap.map.RenderMap();
		
		
		try {
			othermap.ParseFile(BattleShip.main.getAssets().open(
					"scripts/scripts.tct"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		othermap.map.addMapClick(new GameState());
		othermap.map.addDrawOffset(384, 0);
		othermap.map.RenderMap();
		
		
		BattleShip.mZoomCamera.setBounds(0, (playermap.map.MapDimX +1)*128, 0, (playermap.map.MapDimY + 1)*128);
		BattleShip.mZoomCamera.setZoomFactor((float)CAM_W/(((playermap.map.MapDimX+2)*1.7f)* 128));
		BattleShip.mZoomCamera.setCenter((playermap.map.MapDimX+1)*64, (playermap.map.MapDimY+1)*64);
		BattleShip.mZoomCamera.setBoundsEnabled(true);
		
		
		
		//AnimationFactory.StartNewAnimation("commandcenter", new LPoint(0,0), new LPoint(400,400), 60, AnimationType.explonential);
	}
	
	@Override
    public void onScroll(final ScrollDetector pScollDetector, final TouchEvent pTouchEvent, final float pDistanceX, final float pDistanceY) {
            final float zoomFactor = BattleShip.mZoomCamera.getZoomFactor();
           BattleShip.mZoomCamera.offsetCenter(-pDistanceX / (zoomFactor / 2), -pDistanceY / (zoomFactor/2));
          //  BattleShip.mZoomCamera.offsetCenter(-pDistanceX, -pDistanceY );
            //gamestates.sprite.setPosition(this.mZoomCamera.)
            
            
    }

    @Override
    public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent) {
            this.mPinchZoomStartedCameraZoomFactor = BattleShip.mZoomCamera.getZoomFactor();
    }
    
    private final static float MAX_ZOOM = 0.2f;

    @Override
    public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
            if(mPinchZoomStartedCameraZoomFactor * pZoomFactor > MAX_ZOOM)
            	BattleShip.mZoomCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
            else
            	BattleShip.mZoomCamera.setZoomFactor(MAX_ZOOM);
           
    }

    @Override
    public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
    	if(mPinchZoomStartedCameraZoomFactor * pZoomFactor > MAX_ZOOM)
        	BattleShip.mZoomCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
        else
        	BattleShip.mZoomCamera.setZoomFactor(MAX_ZOOM);
            
    }


    @Override
    public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
            if(this.mPinchZoomDetector != null) {
                    this.mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

                    if(this.mPinchZoomDetector.isZooming()) {
                            this.mScrollDetector.setEnabled(false);
                    } else {
                            if(pSceneTouchEvent.isActionDown()) {
                                    this.mScrollDetector.setEnabled(true);
                            }
                            this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
                    }
            } else {
                    this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
            }

            return true;
    }
    
    @Override
    public void onBackPressed()
    {
    	super.onBackPressed();
    	this.finish();
    }
    
    @Override
	public void onStart()
	{
		super.onStart();
		
		TextureLoader.ReloadTextures();
		
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		TextureLoader.ReloadTextures();
	}
	
	@Override
	public void onRestart()
	{
		super.onRestart();
		TextureLoader.ReloadTextures();
	}

}