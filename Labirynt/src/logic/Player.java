package logic;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import javax.swing.JPanel;


public abstract class Player{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public float Speed;
	public int Health,  Damage, Courage, Strength;
	public int type; 
	public List<String> backpack; 
	
	public Player(){
		this.Health =  100;
		this.Damage = 10;
		this.Speed  = 1;
		this.Courage = 1;
		this.Strength =  1;
	}
}
