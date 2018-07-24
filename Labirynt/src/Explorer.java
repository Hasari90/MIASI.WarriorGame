import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.proto.ContractNetInitiator;

import java.util.*;

public class Explorer extends Agent implements Creature {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private AID[] agentID;
	
	public AID[] getAgentID() {
		return agentID;
	}
	public void setAgentID(AID[] agentID) {
		this.agentID = agentID;
	}
	
}
