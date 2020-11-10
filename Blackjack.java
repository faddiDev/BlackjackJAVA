import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

class Blackjack extends JFrame {

	private JPanel myPanel = new JPanel();
	
	/*cards component */
	private JLabel tableCards = new JLabel();
	private JLabel bandarCards[] = new JLabel[8];
	private JLabel userCards[] = new JLabel[8];
	
	/* user bet component */
	private JLabel modal = new JLabel("Modal :");
	private JTextField modalField = new JTextField();
	private JButton buttonBet[] = new JButton[7];
	
	private String buttonBetText[] = {"START","HIT","PASS","DOUBLE","STAND","CONTINUE","RESTART"};
	private int i=0;
	private int left=0;
	
	/* Constructor */
	public Blackjack() {
		super("Blackjack Game");
		this.frame(); //init GUI frame		
		this.prepareTable(); //init GUI Table Cards
		this.prepareBetComponent(); //init GUI User Bet Component
		
		i=0;
		while(i<buttonBet.length) {
			this.buttonBet[i].addActionListener(new ButtonHandler());
			i++;
		}
		
		this.getContentPane().add(this.myPanel);
		this.setVisible(true);
	}
	
	private void frame() {
		this.setSize(1010,650);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.myPanel.setSize(1010,650);
		this.myPanel.setLayout(null);
	}
	
	private void prepareTable() {
		try {
			this.tableCards = new JLabel(new ImageIcon(ImageIO.read(new File("cards/back.png"))));
			this.tableCards.setBounds(890,77,100,134);
			this.myPanel.add(this.tableCards);
			
			i=0;
			left=10;
			for(i=0;i<2;i++) {
				this.bandarCards[i] = new JLabel(new ImageIcon(ImageIO.read(new File("cards/back.png"))));
				this.userCards[i] = new JLabel(new ImageIcon(ImageIO.read(new File("cards/back.png"))));
				this.bandarCards[i].setBounds(left,10,100,134);
				this.userCards[i].setBounds(left,144,100,134);
				this.myPanel.add(this.bandarCards[i]);
				this.myPanel.add(this.userCards[i]);
				left+=110;
			}
		} catch(Exception e) {
			System.out.println("Error = " + e.getMessage());
		}
	}
	
	private void prepareBetComponent() {
		this.modal.setBounds(10,300,50,25);
		this.myPanel.add(this.modal);
		this.modalField.setBounds(60,300,100,25);
		this.myPanel.add(this.modalField);
		i=0;
		left=170;
		for(i=0;i<7;i++) {
			this.buttonBet[i] = new JButton(this.buttonBetText[i]);
			this.buttonBet[i].setBounds(left,300,100,25);
			this.myPanel.add(this.buttonBet[i]);
			if(i>0) this.buttonBet[i].setEnabled(false);
			left+=110;
		}
	}
	
	/* Button Handler Method */
	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == buttonBet[0]) {
				initStartGame();
			}
		}
	}
	
	private void initStartGame() {
		this.modalField.setEnabled(false);
		i=0;
		while(i<buttonBet.length) {
			this.buttonBet[i].setEnabled(true);
			if(i==0) this.buttonBet[i].setEnabled(false);
			i++;
		}
	}
	
	/* init main app */
	public static void main(String[] args) {
		Blackjack blackjack = new Blackjack();
	}
	
}