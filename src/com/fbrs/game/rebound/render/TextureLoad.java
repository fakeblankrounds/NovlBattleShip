package com.fbrs.game.rebound.render;

import java.util.HashMap;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import com.fbrs.battleship.BattleShip;
import com.fbrs.rebound.UI.IClickable;
import com.fbrs.rebound.abstraction.TextureLoader;
import com.fbrs.utils.math.LPoint;


public class TextureLoad extends TextureLoader{

	private static Texture mTexture;
	private static HashMap<String, TextureRegion> textureloaded = new HashMap<String, TextureRegion>();
	private static HashMap<String, Sprite> spriteMap = new HashMap<String, Sprite>();
	private static int nameing;
	
	public static Sprite getSprite(String s)
	{
		return spriteMap.get(s);
	}

	public static void removeSprite(String s)
	{
		final Sprite obj = getSprite(s);
		spriteMap.remove(s);
		BattleShip.mEngine.runOnUpdateThread(new Runnable() {
			  @Override
			public void run() {
				
				
				BattleShip.scene.getLastChild().detachChild(obj);
				BattleShip.scene.unregisterTouchArea(obj);
				
				BattleShip.scene.getFirstChild().sortChildren();
			}
		});
	}

	public static TextureRegion loadNewTexture(String sprite, LPoint resolution) {
		if(!textureloaded.containsKey(sprite))
		{
			mTexture  = new Texture((int)resolution.X, (int)resolution.Y, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			final TextureRegion faceTextureRegion = TextureRegionFactory.createFromAsset(mTexture, BattleShip.main, "gfx/" + sprite + ".png", 0, 0);
			BattleShip.mEngine.getTextureManager().loadTexture(mTexture);	
			textureloaded.put(sprite, faceTextureRegion);
			return faceTextureRegion;
		}
		else
		{
			return textureloaded.get(sprite);
		}


	}
	@Override
	public void resetTextures()
	{
		textureloaded.clear();
	}

	@Override
	public String MakeSprite(int x, int y, float rot, int pZIndex, String sprite, final IClickable onclick)
	{
		nameing++;
		Sprite s;
		if(onclick != null)
		{
			s = new Sprite(x, y, TextureLoad.loadNewTexture(sprite, new LPoint(128,128)))
			{
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float  pTouchAreaLocalY) {
					if(pSceneTouchEvent.isActionUp())
					onclick.onClick((int)this.getX(), (int)this.getY());
					return true;
				}

			};
		}
		else
		{
			s = new Sprite(x, y, TextureLoad.loadNewTexture(sprite, new LPoint(128,128)));
		}

		s.setRotation(rot);
		s.setZIndex(pZIndex);
		BattleShip.scene.getLastChild().attachChild(s);
		if(onclick != null)
			BattleShip.scene.registerTouchArea(s);
		spriteMap.put(Integer.toString(nameing), s);
		s.sortChildren();
		return Integer.toString(nameing);
	}
	
	@Override
	public String MakeSprite(int x, int y, float rot, float scale, int pZIndex, String sprite, final IClickable onclick)
	{
		nameing++;
		Sprite s;
		if(onclick != null)
		{
			s = new Sprite(x, y, TextureLoad.loadNewTexture(sprite, new LPoint(128,128)))
			{
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float  pTouchAreaLocalY) {
					if(pSceneTouchEvent.isActionUp())
					onclick.onClick((int)this.getX(), (int)this.getY());
					return true;
				}

			};
		}
		else
		{
			s = new Sprite(x, y, TextureLoad.loadNewTexture(sprite, new LPoint(128,128)));
		}
		s.setScale(scale);
		s.setRotation(rot);
		s.setZIndex(pZIndex);
		BattleShip.scene.getLastChild().attachChild(s);
		if(onclick != null)
			BattleShip.scene.registerTouchArea(s);
		spriteMap.put(Integer.toString(nameing), s);
		s.sortChildren();
		return Integer.toString(nameing);
	}
	
	@Override
	public String MakeSprite(int resx, int resy, int x, int y, float rot, float scale, int pZIndex, String sprite, final IClickable onclick)
	{
		nameing++;
		Sprite s;
		if(onclick != null)
		{
			s = new Sprite(x, y, TextureLoad.loadNewTexture(sprite, new LPoint(resx,resy)))
			{
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float  pTouchAreaLocalY) {
					if(pSceneTouchEvent.isActionUp())
					onclick.onClick((int)this.getX(), (int)this.getY());
					return true;
				}

			};
		}
		else
		{
			s = new Sprite(x, y, TextureLoad.loadNewTexture(sprite, new LPoint(resx,resy)));
		}
		s.setScale(scale);
		s.setRotation(rot);
		s.setZIndex(pZIndex);
		BattleShip.scene.getLastChild().attachChild(s);
		if(onclick != null)
			BattleShip.scene.registerTouchArea(s);
		spriteMap.put(Integer.toString(nameing), s);
		s.sortChildren();
		return Integer.toString(nameing);
	}
	
	@Override
	public String MakeSprite(int resx, int resy, int x, int y, float rot, int pZIndex, String img, final IClickable onclick)
	{
		nameing++;
		Sprite s;
		if(onclick != null)
		{
			s = new Sprite(x, y, TextureLoad.loadNewTexture(img, new LPoint(resx,resy)))
			{
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float  pTouchAreaLocalY) {
					if(pSceneTouchEvent.isActionUp())
					onclick.onClick((int)this.getX(), (int)this.getY());
					return true;
				}

			};
		}
		else
		{
			s = new Sprite(x, y, TextureLoad.loadNewTexture(img, new LPoint(resx,resy)));
		}

		s.setRotation(rot);
		s.setZIndex(pZIndex);
		BattleShip.scene.getLastChild().attachChild(s);
		if(onclick != null)
			BattleShip.scene.registerTouchArea(s);
		spriteMap.put(Integer.toString(nameing), s);
		s.sortChildren();
		return Integer.toString(nameing);
	}

	@Override
	public void MoveSprite(String sprite, int x, int y)
	{
		spriteMap.get(sprite).setPosition(x, y);
	}

}
