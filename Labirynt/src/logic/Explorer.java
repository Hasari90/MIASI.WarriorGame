package logic;

import java.awt.Color;
import java.util.*;

import javax.swing.JOptionPane;

public class Explorer extends Player{

	Color color;
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
		move.setLocation((0*Game.panelSize)+23, (0*Game.panelSize)+25);
    	move.y = 0;
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
		//System.out.println("x:"+ move.x +" y:" + move.y);
	}
}
