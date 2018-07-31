package logic;

import java.awt.Color;
import java.util.*;

import javax.swing.JOptionPane;

public class Explorer extends Player{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Color color;
	public List<String> backpack; //for the treasures
	public Move move;
	
	public Explorer(int health, int damage, float speed, int courage, int stength)
	{
		this.Health =  health;
		this.Damage = damage;
		this.Speed  = speed;
		this.Courage = courage;
		this.Strength =  stength;
		move = new Move(Color.getHSBColor(0.5f, 0.5f, 1));
		move.setVisible(true);
	}
	
	public void MovePlayer()
	{
		Random r = new Random();
		int result = r.nextInt(4);
		
		if(result == 0){
			move.moveUp();
		}
		if(result == 1){
			move.moveRight();
			
		}
		if(result == 2){
			move.moveDown();
		}
		if(result == 3){
			move.moveLeft();
		}
		
		if(move.x == Game.columns-1 && move.y == Game.endLevelLoc){
		JOptionPane.showMessageDialog(null, "Gratulujê, gra skoñczona!", "Koniec gry!", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
