package logic;

import java.awt.Color;
import java.awt.Point;
import javax.swing.JPanel;

public class Move extends JPanel {
	
	private static final long serialVersionUID = 1L;
	public int x, y;
	Color color;
	
    public Move(Color color) {
    	this.color = color;
    	this.setBackground(color);
    	this.setSize(Game.panelSize, Game.panelSize);
    }

    public void moveLeft() {
    	if(x > 0 && Game.map[x-1][y] == 1){
	    	this.setLocation(this.getX()-25, this.getY());
	    	x--;
    	}
    	else if(x > 0 && Game.map[x-1][y] == 2){
	    	this.setLocation(this.getX()-25, this.getY());
	    	x--;
    	}
    }

    public void moveRight() {
    	if(x < Game.columns-1 && Game.map[x+1][y] == 1){
	    	this.setLocation(this.getX()+25, this.getY());
	    	x++;
    	}
    	else if(x < Game.columns-1 && Game.map[x+1][y] == 2){
	    	this.setLocation(this.getX()+25, this.getY());
	    	x++;
    	}
    }

    public void moveUp() {
    	if(y > 0 && Game.map[x][y-1] == 1){
	    	this.setLocation(this.getX(), this.getY()-25);
	    	y--;
    	}
    	else if(y > 0 && Game.map[x][y-1] == 2){
	    	this.setLocation(this.getX()+25, this.getY());
	    	y--;
    	}
    }

    public void moveDown() {
    	if(y < Game.rows-1 && Game.map[x][y+1] == 1){
	    	this.setLocation(this.getX(), this.getY()+25);
	    	y++;
    	}
    	else if(y < Game.rows-1 && Game.map[x][y+1] == 2){
	    	this.setLocation(this.getX()+25, this.getY());
	    	y++;
    	}
    }
    
    public void moveByPoint(Point move) {
    	if(this.x <move.x) {
    		moveRight();
    	}
    	if(this.x >move.x) {
    		moveLeft();
    	}
    	if(this.y <move.y) {
    		moveDown();
    	}
    	if(this.y >move.y) {
    		moveUp();
    	}
    }
}
