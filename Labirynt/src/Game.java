
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Game extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int rows = 30;
    public static int columns = 30;
    public static int panelSize = 25;
    public static int map[][] = new int[columns][rows];
    public static int endLevelLoc;
    public static Tile tile;
    public List<Warrior> listWarriors;
    public List<Explorer> listExplorers;
    Explorer e;
	private BufferedReader br;
    
    public Game(String str){
        loadMap(str);
        this.setResizable(false);
        this.setSize((columns*panelSize)+50, (rows*panelSize)+70);
        this.setTitle("WarriorGame");
        this.setLayout(null);

        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Timer timer = new Timer(100, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tick();
                    }
                });
                timer.setRepeats(true);
                timer.setCoalesce(true);
                timer.start();
            }
        });
        
        this.addWindowListener( new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                JFrame frame = (JFrame)e.getSource();
                frame.dispose();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
            }
        });
        
        this.setLocationRelativeTo(null);
        
        //Create player
        //Explorer e = new Explorer(100,10,1,1,1);
        //listExplorers.add(e);
        //listExplorers.add(new Explorer(100,10,1,1,1));
        e = new Explorer(100,10,1,1,1);
    	this.add(e.player);
    	
        //Color map
        for(int y = 0; y < columns; y++){
            for(int x = 0; x < rows; x++){
                tile = new Tile(x, y);
                tile.setSize(panelSize, panelSize);
                tile.setLocation((x*panelSize)+23, (y*panelSize)+25);
                if(map[x][y] == 0){
                    tile.setBackground(Color.GRAY);
                }else if(map[x][y] == 2)
                {
                	tile.setBackground(Color.YELLOW);
                }
                else{
                    tile.setBackground(Color.WHITE);
                    tile.setWall(false);
                    if(x == 0){
                    	e.player.setLocation((x*panelSize)+23, (y*panelSize)+25);
                    	e.player.y = y;
                    }
                    if(x == columns-1){
                    	endLevelLoc = y;
                    }
                }
                
                tile.setVisible(true);
                this.add(tile);
            }
        }
        this.setVisible(true);
    }
    
    public static void main(String args[]){
    	new Game("Level 0.map");
    }
    
    public void loadMap(String str){
        try{
            br = new BufferedReader(new FileReader(str));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String mapStr = sb.toString();
            
            int counter = 0;
            for(int y = 0; y < columns; y++){
                for(int x = 0; x < rows; x++){
                    String mapChar = mapStr.substring(counter, counter+1);
                    if(!mapChar.equals("\r\n") && !mapChar.equals("\n")&& !mapChar.equals("\r")){
                        map[x][y] = Integer.parseInt(mapChar);
                    }else{
                        x--;
                        System.out.print(mapChar);
                    }
                    counter++;
                }
            }
        }catch(Exception e){
            System.out.println("Problem z za³adowaniem mapy.");
        }
    }
    
    protected void tick() {
    	//List<Explorer> copy = new ArrayList<Explorer>(listExplorers);
//    	for(Explorer e: listExplorers)
//    	{
//    		//e = new Explorer(100,10,1,1,1);
//        	this.add(e.player);
//        	
//        	e.player.setLocation((0*panelSize)+23, (0*panelSize)+25);
//        	e.player.y = 0;
//    	}
    	
    	revalidate();
		repaint();
		
		e.MovePlayer();
		
//		for(Explorer e:listExplorers)
//    	{
//    		e.MovePlayer();
//    	}
		
    }
}
