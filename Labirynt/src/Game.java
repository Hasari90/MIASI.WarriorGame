
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
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;

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
    Explorer e;
    Player p2;
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

                //tick();

                Timer timer = new Timer(1000, new ActionListener() {
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
    
        
//        this.addKeyListener(new KeyListener(){
//
//			@Override
//			public void keyPressed(KeyEvent e) {
//				int key = e.getKeyCode();
//				
//				revalidate();
//				repaint();
//				
//				//Player movement
//				//if(key == KeyEvent.VK_W){
//				//	p.moveUp();
//				//}
//				//if(key == KeyEvent.VK_A){
//				//	p.moveLeft();
//				//}
//				//if(key == KeyEvent.VK_S){
//				//	p.moveDown();
//				//}
//				//if(key == KeyEvent.VK_D){
//				//	p.moveRight();
//				//}
//				
//				if(key == KeyEvent.VK_UP){
//					p2.moveUp();
//				}
//				if(key == KeyEvent.VK_LEFT){
//					p2.moveLeft();
//				}
//				if(key == KeyEvent.VK_DOWN){
//					p2.moveDown();
//				}
//				if(key == KeyEvent.VK_RIGHT){
//					p2.moveRight();
//				}
//				
//				//if(e.p == columns-1 && e.p.y == endLevelLoc){
//				//	JOptionPane.showMessageDialog(null, "Gratuluj�, gra sko�czona!", "Koniec gry!", JOptionPane.INFORMATION_MESSAGE);
//				//	dispose();
//				//}
//				
//				if(p2.x == columns-1 && p2.y == endLevelLoc){
//					JOptionPane.showMessageDialog(null, "Gratuluj�, gra sko�czona!", "Koniec gry!", JOptionPane.INFORMATION_MESSAGE);
//					dispose();
//				}
//			}
//
//			@Override
//			public void keyReleased(KeyEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void keyTyped(KeyEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//        	
//        });
//        
//        this.addWindowListener(new WindowAdapter(){
//            public void windowClosing(WindowEvent e) {
//                System.exit(0);
//            }
//        });
        
        this.setLocationRelativeTo(null);
        
        //Create player
        //e = new Explorer();
    	//this.add(e.p);
    	
    	p2 = new Player(Color.getHSBColor(0.5f, 0.5f, 1));
    	p2.setVisible(true);
    	this.add(p2);
    	
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
                    	//e.p.setLocation((x*panelSize)+23, (y*panelSize)+25);
                    	//e.p.y = y;
                    	p2.setLocation((x*panelSize)+23, (y*panelSize)+25);
                    	p2.y = y;
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
            System.out.println("Problem z za�adowaniem mapy.");
        }
    }
    
    protected void tick() {
    	revalidate();
		repaint();
		
		Random r = new Random();
		int result = r.nextInt(3);
		
		if(result == 0){
			p2.moveUp();
		}
		if(result == 1){
			p2.moveRight();
			
		}
		if(result == 2){
			p2.moveDown();
		}
		if(result == 3){
			p2.moveLeft();
		}
		
		if(p2.x == columns-1 && p2.y == endLevelLoc){
		JOptionPane.showMessageDialog(null, "Gratuluj�, gra sko�czona!", "Koniec gry!", JOptionPane.INFORMATION_MESSAGE);
		dispose();
		}
    }
}