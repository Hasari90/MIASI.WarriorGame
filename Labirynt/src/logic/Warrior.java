package logic;

import java.awt.Color;
import java.awt.Point;


public class Warrior extends Player implements IDatable {
	
	public Path path; //path of the warrior
	public Move move; // info about position in map visualized	
	public boolean fighting = false;

	public Warrior() {
		// TODO Auto-generated constructor stub
	}
	
	public Warrior(int health, int damage, float speed, int courage, int stength){
		this.Health =  health;
		this.Damage = damage;
		this.Speed  = speed;
		this.Courage = courage;
		this.Strength =  stength;
		this.move = new Move(Color.RED);
		this.move.setVisible(true);
		this.path = new Path();
	}

	//Attack opponent
	public int Attack(Warrior opponent) {
		return opponent.Health -= this.Strength;	
	}
	
	//find next object to go
	public void Move(Game game) {		
		path.setStart(new Point(this.move.x,this.move.y));
		Tile tile = MoveByOne(game);
		if(tile != null) {
			move.moveByPoint(new Point(tile.x,tile.y));
		}
	}

	 public Tile MoveByOne(Game game) {
		//find end point 
		if(path.getEnd() == null ) {
			path.FindNextEndPoint(game.elements);
		}
		if(path.CheckIfEndOfPath()){
			game.removeElement(path.getEnd(),game.elements);
			path.setEnd(null); 
			path.actualPath = null;
			return null;
		}
		//find path for the character
		path.MovetoNextPoinInPath();
		
		//move by one on path		
		return path.GetOneMoveFromPath();
	}
	
	 @Override
	public int GetX() {
		// TODO Auto-generated method stub
		return this.move.x;
	}
	 @Override
	public int GetY() {
		// TODO Auto-generated method stub
		return this.move.y;
	}
	 
	 @Override
	public void SetX(int x) {
		this.move.x = x;
	}
	 
	 @Override
	public void SetY(int y) {
		this.move.x = y;
	}
	 

	public void SetHealth(int health) {
		this.Health =  health;
	}

	@Override
	public Point GetPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLocation(int x, int y) {
		move.x = x;
		move.y = y;		
	} 	
	
}
