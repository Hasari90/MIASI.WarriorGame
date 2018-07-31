package agents;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public abstract class MyBehaviour extends Behaviour {

	private static final long serialVersionUID = 1L;
	
	public void sendReply(ACLMessage msg, String content, int performative){
		ACLMessage reply = msg.createReply();
		reply.setPerformative(performative);
		reply.setContent(content);
		myAgent.send(reply);
	}

	public AID[] searchPlayers(){
		
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		
		sd.setType("Player");

		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			AID matchingAgents[] = new AID[result.length];
			for (int i = 0; i < result.length; ++i) {
				matchingAgents[i] = result[i].getName();
			}
			
			return matchingAgents;
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		return null;
	}

}
