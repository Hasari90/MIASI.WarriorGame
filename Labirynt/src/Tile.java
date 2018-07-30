
import javax.swing.JPanel;

public class Tile extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int x, y;
	public Tile parent;
    boolean isWall = true;
    
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

}
