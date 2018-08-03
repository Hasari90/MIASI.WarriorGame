package logic;

import java.awt.Color;

public class Monster extends Warrior{
				
	public Monster()
	{
		
	}
	
	public Monster(int health, int damage, float speed, int courage, int stength){
		this.Health =  health;
		this.Damage = damage;
		this.Speed  = speed;
		this.Courage = courage;
		this.Strength =  stength;
		this.move = new Move(Color.GREEN);
		this.move.setVisible(true);
		this.path = new Path();
	}
	
	public Tile MoveByOne(Game game) {
		//find end point 
		if(path.getEnd() == null ) {
			this.path.FindNextEndPoint(game.listCharacters);
		}
		if(path.CheckIfEndOfPath()){
			if (!fighting ) {
				//game.removeElement(path.getEnd(),game.listCharacters); 
			}
			path.setEnd(null); 
			path.actualPath = null;
			return null;
		}		
		//find path for the character
		path.MovetoNextPoinInPath();
		
		//move by one on path	
		Tile movePath = path.GetOneMoveFromPath();
	
		return 	movePath;
	}

}
