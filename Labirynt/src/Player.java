import java.awt.Color;

import javax.swing.JPanel;


public class Player extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int x, y;
	Color color;
	
    public Player(Color color) {
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
}
