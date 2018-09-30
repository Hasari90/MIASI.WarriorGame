package logic;

import java.awt.Color;
import java.awt.Point;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import jade.core.AID;


public abstract class Player{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public float Speed;
	public int Health,  Damage, Courage, Strength;
	public int type; 
	public List<String> backpack; 
	Color color;
	public Move move;
	public Path path;
	public String agent;
	
	
	public Player(){
		this.Health =  100;
		this.Damage = 10;
		this.Speed  = 1;
		this.Courage = 1;
		this.Strength =  1;
		this.move = new Move(GetColor());
		this.move.setVisible(true);
		move.setLocation((0*Game.panelSize)+23, (0*Game.panelSize)+25);
    	move.y = 0;
		this.path = new Path();
	}
	
	//find next object to go
		public void Move(Game gameInfo) {		
			path.setStart(new Point(this.move.x,this.move.y));
			Tile tile = MoveByOne(gameInfo);
			if(tile != null) {
				move.moveByPoint(new Point(tile.x,tile.y));
			}
		}

		 public Tile MoveByOne(Game gameInfo) {
			//find end point 
			if(path.getEnd() == null ) {
				path.FindNextEndPoint(gameInfo.elements);
			}
			if(path.CheckIfEndOfPath()){
				gameInfo.removeElement(path.getEnd(),gameInfo.elements);
				path.setEnd(null); 
				path.actualPath = null;
				return null;
			}
			//find path for the character
			path.MovetoNextPoinInPath();
			
			//move by one on path		
			return path.GetOneMoveFromPath();
		}
		
		public int GetX() {
			// TODO Auto-generated method stub
			return this.move.x;
		}

		public int GetY() {
			// TODO Auto-generated method stub
			return this.move.y;
		}
		 
		public void SetX(int x) {
			this.move.x = x;
		}
		 
		public void SetY(int y) {
			this.move.x = y;
		}
		 

		public void SetHealth(int health) {
			this.Health =  health;
		}

		public Point GetPoint() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setLocation(int x, int y) {
			move.x = x;
			move.y = y;		
		} 	
	
	public Color GetColor()
	{
		Random randomGenerator = new Random();
		int red = randomGenerator.nextInt(256);
		int green = randomGenerator.nextInt(256);
		int blue = randomGenerator.nextInt(256);
	
		Color randomColour = new Color(red,green,blue);
		
		return randomColour;
	}
}
