package com.fbrs.battleship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import com.fbrs.game.rebound.render.TextureLoad;
import com.fbrs.rebound.UI.IClickable;
import com.fbrs.rebound.abstraction.TextureLoader;
import com.fbrs.utils.math.LPoint;

public class GameState implements IClickable{

	private enum gameStates {Placeing, p1Turn, p2Turn};

	public gameStates gamestate;

	private HashMap<LPoint, Boolean> hitlocations;
	private HashMap<LPoint, String> sprites;
	private String[] placementsprites = new String[8];
	private ArrayList<LPoint> placed = new ArrayList<LPoint>();
	private ArrayList<String> screens = new ArrayList<String>();
	private Stack<ArrayList<LPoint>> undo = new Stack<ArrayList<LPoint>>();
	private String undoSprite = null;
	private String placingimg = null;
	private int[] shipsizes = {5,4,3,3,2,2};
	private String[] curentshipimg = new String[shipsizes.length];
	private String startbutton = null;
	
	private int currentship = 0;

	public GameState()
	{
		gamestate = gameStates.Placeing;
		hitlocations = new HashMap<LPoint, Boolean>();
		sprites = new HashMap<LPoint, String>();
		for(int i = 0; i < 8; i++)
		{
			placementsprites[i] = TextureLoader.newSprite(-1000, -1000, 0, 1, "b", null);
			i++;
			placementsprites[i] = TextureLoader.newSprite(-1000, -1000, 0, 1, "c", null);
		}
		for(int i = 0; i < curentshipimg.length; i++)
		{
			curentshipimg[i] = TextureLoader.newSprite(-450 + (128 * i), 900, 0, 1, "a", null);
		}
		placingimg = TextureLoader.newSprite(256,128, -300, 800, 0,2, 1, "nowplaceing",null);

		changeShip();
	}
	
	public void startGame()
	{
		
	}

	private ArrayList<LPoint> bool = new ArrayList<LPoint>();
	private ArrayList<LPoint> ship = new ArrayList<LPoint>();

	@Override
	public void onClick(int x, int y) {
		if(gamestate == gameStates.Placeing)
		{
			placeShip(x,y);
		}
	}
	///Placement Methods//
	
	private void placeShip(int x, int y)
	{
		if(undoSprite == null) {
			undoSprite = TextureLoader.newSprite(-500, 1150, 0,2, 1, "undo", new UndoButton(this));
			
		}

		LPoint l = new LPoint(x,y);

		boolean b =  checkGrid(new LPoint(l));
		if(((bool.size()==0 && b) || bool.contains(l)) && currentship < shipsizes.length)  
		{
			placed.add(new LPoint(l));
			String t = TextureLoader.newSprite(x, y, 0, 1, "a", null);
			ship.add(new LPoint(l));
			screens.add(t); 
			sprites.put(new LPoint(l),t);
			bool = SetMovement(new LPoint(l), shipsizes[currentship]);
			if(bool.size() == 1)
			{
				l.set(x, y);
				for(int i = ship.size()+1; i <= shipsizes[currentship]; i++)
				{
					LPoint add = new LPoint(l).Sub(bool.get(0)).Mult(-1, -1);
					l.Add(add);
					placed.add(new LPoint(l));
					t = TextureLoader.newSprite((int)l.X,(int) l.Y, 0, 1, "a", null);
					ship.add(new LPoint(l));
					screens.add(t); 
					sprites.put(new LPoint(l),t);
					bool = SetMovement(new LPoint(l),10);	
					
				}
				addShip();
				SetMovement(null,0);
			}
		}
	}

	private void changeShip()
	{
		//shipsizes[currentship]

		for(int i = 0; i < curentshipimg.length; i++)
		{
			TextureLoader.moveSprite(curentshipimg[i], -1000, -950);
		}
		if(currentship < shipsizes.length) {
			for(int i = 0; i < shipsizes[currentship]; i++)
			{
				TextureLoader.moveSprite(curentshipimg[i], -450 + (128 * i), 900);
			}
		}
		else
		{
			startbutton = TextureLoader.newSprite(256,128, -150, 1150, 0,1.5f, 1, "start", new startbutton(this));
		}
	}

	private void addShip()
	{
		for(LPoint p : ship)
			hitlocations.put(p,true);
		ArrayList<LPoint> newship = new ArrayList<LPoint>();
		newship.addAll(ship);
		undo.add(newship);
		ship.clear();
		bool.clear();
		currentship++;
		changeShip();
	}
	
	public void Undo()
	{
		if(ship.size() != 0 || undo.size() != 0) {
			if(ship.size() != 0)
				clearShip();
			else {
				clearShip(undo.pop());
				currentship--;
				changeShip();
			}
		}
		bool.clear();
	}

	private void clearShip()
	{
		for(LPoint l : ship)
		{
			TextureLoad.removeSprite(sprites.get(l));
			sprites.remove(l);
			placed.remove(l);
		}
		SetMovement(null, 4);
		ship.clear();
	}

	private void clearShip(ArrayList<LPoint> ship)
	{
		for(LPoint l : ship)
		{
			TextureLoad.removeSprite(sprites.get(l));
			sprites.remove(l);
			placed.remove(l);
		}
		SetMovement(null, 4);
		ship.clear();
	}

	private ArrayList<LPoint> SetMovement(LPoint l, int shiplength)
	{
		ArrayList<LPoint> bool = new ArrayList<LPoint>();
		for(int i = 0; i < 8; i++)
			TextureLoader.moveSprite(placementsprites[i], -1000, -1000);
		if(l!=null) 
		{
			float x,y;
			x = l.X; y = l.Y; // for a reset
			int booleandir = 0;
			LPoint dir = null;
			LPoint sideCheck1 = null;
			LPoint sideCheck2 = null;

			//check which direction we are going in.
			if(placed.contains(l.set(x+128, y)))
			{
				dir = new LPoint(-128,0);//were going left
				sideCheck1 = new LPoint(0,128);
				sideCheck2 = new LPoint(0,-128);
				booleandir = 0;
			}
			else if(placed.contains(l.set(x-128, y)))
			{
				dir = new LPoint(128,0);//were going right
				sideCheck1 = new LPoint(0,128);
				sideCheck2 = new LPoint(0,-128);
				booleandir = 1;
			}
			else if(placed.contains(l.set(x, y+128)))
			{
				dir = new LPoint(0,-128);//were going up
				sideCheck1 = new LPoint(128,0);
				sideCheck2 = new LPoint(-128,0);
				booleandir = 2;
			}
			else if(placed.contains(l.set(x, y-128)))
			{
				dir = new LPoint(0,128);//were going down
				sideCheck1 = new LPoint(128,0);
				sideCheck2 = new LPoint(-128,0);
				booleandir = 3;
			}
			if(dir != null)
			{
				float dirx = x + dir.X, diry = y+ dir.Y;
				sideCheck1.Add(x,y); sideCheck2.Add(x,y);
				if(!placed.contains(dir) && !placed.contains(dir.Mult(2, 2)) && !placed.contains(sideCheck1) && !placed.contains(sideCheck2))
				{
					if(!placed.contains(dir.Mult(-shiplength/2, -shiplength/2).Add(x,y))) {
						TextureLoader.moveSprite(placementsprites[0], (int)dirx, (int)diry);
						bool.add(new LPoint(dirx,diry));
					}
					else {
						//addShip();
						return bool;

					}
				}
				//TextureLoader.moveSprite(placementsprites[1], (int)(dir.X), (int)(dir.Y));	
			}
			else
			{
				if(placed.contains(l.set(x+256, y)) || placed.contains(l.set(x+128, y)) || placed.contains(l.set(x+128, y+128)) || placed.contains(l.set(x+128, y-128)) || CheckLine(l.set(x, y), new LPoint(+128,0), shiplength))
				{
					l.set(x+128, y);
					TextureLoader.moveSprite(placementsprites[1], (int)l.X, (int)l.Y);
				}
				else {
					l.set(x+128, y);
					TextureLoader.moveSprite(placementsprites[0], (int)l.X, (int)l.Y);
					bool.add(new LPoint(l));
				}

				if(placed.contains(l.set(x-256, y)) || placed.contains(l.set(x-128, y)) || placed.contains(l.set(x-128, y+128)) || placed.contains(l.set(x-128, y-128)) || CheckLine(l.set(x, y), new LPoint(-128,0), shiplength)) 
				{
					l.set(x-128, y);
					TextureLoader.moveSprite(placementsprites[3], (int)l.X, (int)l.Y);			
				}
				else {
					l.set(x-128, y);
					TextureLoader.moveSprite(placementsprites[2], (int)l.X, (int)l.Y);
					bool.add(new LPoint(l));
				}

				if( placed.contains(l.set(x, y+256)) || placed.contains(l.set(x, y+128)) || placed.contains(l.set(x+128, y+128)) || placed.contains(l.set(x-128, y+128)) || CheckLine(l.set(x, y), new LPoint(0,+128), shiplength))
				{
					l.set(x, y+128);
					TextureLoader.moveSprite(placementsprites[5], (int)l.X, (int)l.Y);
				}
				else {
					l.set(x, y+128);
					TextureLoader.moveSprite(placementsprites[4], (int)l.X, (int)l.Y);
					bool.add(new LPoint(l));
				}

				if(placed.contains(l.set(x, y-256)) || placed.contains(l.set(x, y-128)) || placed.contains(l.set(x+128, y-128)) || placed.contains(l.set(x-128, y-128)) || CheckLine(l.set(x, y), new LPoint(0,-128), shiplength))
				{
					l.set(x, y-128);
					TextureLoader.moveSprite(placementsprites[7], (int)l.X, (int)l.Y);

				}
				else
				{
					l.set(x, y-128);
					TextureLoader.moveSprite(placementsprites[6], (int)l.X, (int)l.Y);
					bool.add(new LPoint(l));
				}

			}

		}
		return bool;

	}

	private boolean CheckLine(LPoint l, LPoint dir, int length)
	{
		float x = l.X, y = l.Y;
		float x1 = dir.X, y1 = dir.Y;
		for(int i = 2; i < length; i++)
		{
			dir.set(x1, y1);
			l.set(x, y);
			if(placed.contains(l.Add(dir.Mult(i, i))) || !checkGrid(l))
				return true;

		}
		return false;
	}

	private boolean checkGrid(LPoint l)
	{
		float x = l.X, y = l.Y;
		if(x > 1536 || x < 384 || y > 1152 || y < 0)
			return false;
		if(placed.contains(l.set(x+128, y)))
		{
			return false;
		}
		else if(placed.contains(l.set(x-128, y))) 
		{
			return false;		
		}
		else if(placed.contains(l.set(x, y+128)))
		{
			return false;
		}
		else if(placed.contains(l.set(x, y-128)))
		{
			return false;
		}
		else
			return true;

	}

	private boolean checkGrid(LPoint l, int i)
	{
		float x = l.X, y = l.Y;
		if(x > 1536 || x < 384 || y > 1152 || y < 0)
			return false;
		if(placed.contains(l.set(x+(128*i), y)))
		{
			return false;
		}
		else if(placed.contains(l.set(x-(128*i), y))) 
		{
			return false;		
		}
		else if(placed.contains(l.set(x, y+(128*i))))
		{
			return false;
		}
		else if(placed.contains(l.set(x, y-(128*i))))
		{
			return false;
		}
		else
			return true;

	}


}
