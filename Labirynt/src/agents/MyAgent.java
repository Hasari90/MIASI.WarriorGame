package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;


public class MyAgent extends Agent {
	
	private static final long serialVersionUID = 1L;

	public void sendMessage(String dest, String content, int performative){
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(new AID(dest, AID.ISLOCALNAME));
		msg.setPerformative(performative);
		msg.setContent(content);
		
		send(msg);
	}
	
	public void registryDF(String type, String name){
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(type);
		sd.setName(name);
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}	
}
