package logic;

import jade.Boot;

public class Main extends Boot{

    public static void main(String args[]){
    	Boot.main(args);
    	new Game("Level 0.map");
    }
}
