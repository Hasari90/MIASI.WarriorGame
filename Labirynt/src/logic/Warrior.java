package logic;

import java.awt.Color;
import java.awt.Point;


public class Warrior extends Player implements IDatable {
	
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
	}

	//Attack opponent
	public int Attack(Warrior opponent) {
		return opponent.Health -= this.Strength;	
	}
	
	
	
}
