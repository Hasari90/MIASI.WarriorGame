package agents;
import jade.core.AID;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class AgentGUI extends JFrame {	
		//private Explorer myAgent;
		
		private JTextField titleField, priceField;
		
		AgentGUI()//Explorer a) 
		{
			//super(a.getLocalName());
			
			//myAgent = a;
			
			JPanel p = new JPanel();
			p.setLayout(new GridLayout(2, 2));
			p.add(new JLabel("Book title:"));
			titleField = new JTextField(15);
			p.add(titleField);
			p.add(new JLabel("Price:"));
			priceField = new JTextField(15);
			p.add(priceField);
			getContentPane().add(p, BorderLayout.CENTER);
			
			JButton addButton = new JButton("Add");
			addButton.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					try {
						String title = titleField.getText().trim();
						String price = priceField.getText().trim();
						//myAgent.updateCatalogue(title, Integer.parseInt(price));
						titleField.setText("");
						priceField.setText("");
					}
					catch (Exception e) {
						JOptionPane.showMessageDialog(AgentGUI.this, "Invalid values. "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
					}
				}
			} );
			p = new JPanel();
			p.add(addButton);
			getContentPane().add(p, BorderLayout.SOUTH);
			
			addWindowListener(new	WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					//myAgent.doDelete();
				}
			} );
			
			setResizable(false);
		}
		
		public void showGui() {
			pack();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int centerX = (int)screenSize.getWidth() / 2;
			int centerY = (int)screenSize.getHeight() / 2;
			setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
			super.setVisible(true);
		}

		public static void main(String[] args) {
		}	
	}
