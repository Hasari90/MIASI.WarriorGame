package logic;

import jade.Boot;

public class Main extends Boot{
	
    public static void main(String args[]){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
        	@Override
        	public void run() {
            	Boot.main(args);
            }
        });
    }
}
