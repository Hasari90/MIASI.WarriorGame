package logic;

import java.awt.Point;

import javax.swing.JPanel;

public class Tile extends JPanel  implements IDatable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int x, y;
	public Tile parent;
    boolean isWall = true;
    
    public Tile() {
    	
    }
    
    public Tile(int x, int y){
        this.x = x;
        this.y = y;
        this.parent = null;
    }    
    
    public Tile(int x, int y,Tile parent) {
    	this.x = x;
        this.y = y;
        this.parent = parent;
    }    
    
    public void setWall(boolean isWall){
        this.isWall = isWall;
    }
    
    @Override
    public int GetX() {
    	// TODO Auto-generated method stub
    	return this.x;
    }
    
    @Override
    public int GetY() {
    	// TODO Auto-generated method stub
    	return this.y;
    }

    @Override
    public Point GetPoint() {
    	// TODO Auto-generated method stub
    	return new Point(this.x, this.y);
    }
    
	 @Override
	public void SetX(int x) {
		// TODO Auto-generated method stub
		this.x = x;
	}
	 
	 @Override
	public void SetY(int y) {
		// TODO Auto-generated method stub
		this.x = y;
	}


}
