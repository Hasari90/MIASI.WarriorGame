package agents;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import logic.Player;

public class WarriorAgent extends MyAgent {
	
	private static final long serialVersionUID = 1L;
	private Player playerInfo;
	private Boolean gameOver = false;

	private class WarriorBehaviour extends MyBehaviour {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void action(){
			ACLMessage msg = blockingReceive();
			String[] parts = msg.getContent().split("-", 2);
			switch(parts[0]){
			case "START": 
				sendReply(msg, "GAME", ACLMessage.ACCEPT_PROPOSAL);
				break;
			case "FIGHT": 
				//int maxBid = Integer.parseInt(parts[1]);
				//lasthit = playerInfo.hit(Damage);
				//sendReply(msg, "F" + lasthit, ACLMessage.PROPOSE);
				break;
			case "KILL": 
				//playerInfo.kill(lasthit);
				//sendReply(msg, "K" + lasthit, ACLMessage.INFORM);
				break;
			case "RUN": 
				sendReply(msg, "RUN", ACLMessage.INFORM);
				break;
			case "CHOOSE":
				sendReply(msg, "CHOOSE", ACLMessage.INFORM);
				break;
			case "MOVE":
				sendReply(msg, "MOVE", ACLMessage.INFORM);
				break;
			default:
				break;
			}
		}
		
		@Override
		public boolean done() {
			return gameOver;
		}
	};

	protected void setup() {
		
		registryDF("Player", getAID().getLocalName());
		
		System.out.println(""+ getAID().getLocalName() +" jest gotowy do gry.");
				
		addBehaviour(new WarriorBehaviour());
	}
	
	protected void takeDown() {
		 
		try {
			 DFService.deregister(this);
		 }
		 catch (FIPAException fe) {
			 fe.printStackTrace();
		 }
		
		System.out.println(""+ getAID().getLocalName() +" umiera.");
	}
}
