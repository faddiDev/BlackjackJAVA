/*
	Author			: Faddi S 
	JAVA VERSION	: 1.8
	APPLICATION		: Blackjack Cards Game
*/

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.*;

class Blackjack extends JFrame {

	/* Prepare JPanel */
	private JPanel myPanel[] = new JPanel[4];
	
	/* Cards Component */
	private JLabel tableCards = new JLabel();
	private JLabel bandarCards[] = new JLabel[8];
	private JLabel userCards[] = new JLabel[8];
	
	/* User Bet Component */
	private final JLabel modal = new JLabel("Modal :   Rp.");
	private JTextField modalField = new JTextField();
	private JButton buttonBet[] = new JButton[7];
	
	private final String buttonBetText[] = {"START","HIT","PASS","DOUBLE","STAND","CONTINUE","RESTART"};
	private final int left[] = {10, 120, 230, 340, 450, 560, 670, 780};
	private int i=0;
	
	/* Maximum Modal */
	private final int MAX_MODAL = 1000000;
	
	/* User/Bandar Array Cards */
	private int playerArrayCards[] = new int[20];
	private int bandarArrayCards[] = new int[20];
	private int playerArrayInHandsCards[] = new int[20];
	private int bandarArrayInHandsCards[] = new int[20];
	
	/* Flag In-Game */
	private int playerCardsPosition = 0;
	private int playerInHandsCards = 0;
	private int bandarInHandsCards = 0;
	private int playerMaxPass = 2;
	private int bandarMaxPass = 2;
	private int playerSumScore = 0;
	private int bandarSumScore = 0;
	
	/* Constructor */
	public Blackjack() {
		super("Blackjack Game");
		this.frame();
		this.prepareTable();
		this.prepareBetComponent();
		
		i=0;
		while(i<buttonBet.length) {
			this.buttonBet[i].addActionListener(new ButtonHandler());
			i++;
		}
		
		this.getContentPane().add(this.myPanel[0]);
		this.setVisible(true);
	}
	
	/* Prepare Frame Component */
	private void frame() {
		Border border = BorderFactory.createLineBorder(Color.black);
		
		this.setSize(1060,700);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		
		this.myPanel[0] = new JPanel();
		this.myPanel[0].setSize(1060,700);
		this.myPanel[0].setLayout(null);
		i=1;
		int size[][] = {{910,338},{890,154},{890,154}}, bound[][] = {{10,10,910,338},{10,10,890,154},{10,174,890,154}}, panel = 0;
		while(i<4) {
			this.myPanel[i] = new JPanel();
			this.myPanel[i].setSize(size[i-1][0],size[i-1][1]);
			this.myPanel[i].setLayout(null);
			this.myPanel[i].setBorder(border);
			this.myPanel[i].setBounds(bound[i-1][0],bound[i-1][1],bound[i-1][2],bound[i-1][3]);
			i++;
		}
		this.myPanel[0].add(this.myPanel[1]);
		this.myPanel[1].add(this.myPanel[2]);
		this.myPanel[1].add(this.myPanel[3]);
	}
	
	/* Init All Hands Cards */
	private void initAllHands() throws Exception {
		for(i=0;i<userCards.length;i++) {
			if(i<2) {
				this.bandarCards[i] = new JLabel(new ImageIcon(ImageIO.read(new File("cards/back.png"))));
				this.userCards[i] = new JLabel(new ImageIcon(ImageIO.read(new File("cards/back.png"))));
				this.bandarCards[i].setBounds(left[i],10,100,134);
				this.userCards[i].setBounds(left[i],10,100,134);
				this.myPanel[2].add(this.bandarCards[i]);
				this.myPanel[3].add(this.userCards[i]);
			} else {
				this.bandarCards[i] = new JLabel(new ImageIcon(ImageIO.read(new File("cards/back.png"))));
				this.userCards[i] = new JLabel(new ImageIcon(ImageIO.read(new File("cards/back.png"))));
				this.myPanel[2].add(this.bandarCards[i]);
				this.myPanel[3].add(this.userCards[i]);
			}
		}
	}
	
	/* Prepare Table Cards */
	private void prepareTable() {
		try {
			this.tableCards = new JLabel(new ImageIcon(ImageIO.read(new File("cards/back.png"))));
			this.tableCards.setBounds(930,77,100,134);
			this.myPanel[0].add(this.tableCards);
			this.initAllHands();
		} catch(Exception e) {
			System.out.println("Error = " + e.getMessage());
		}
	}
	
	/* Prepare Bet Component for User */
	private void prepareBetComponent() {
		this.modal.setBounds(10,358,70,25);
		this.myPanel[0].add(this.modal);
		this.modalField.setBounds(80,358,100,25);
		this.myPanel[0].add(this.modalField);
		int j=190;
		for(i=0;i<7;i++) {
			this.buttonBet[i] = new JButton(this.buttonBetText[i]);
			this.buttonBet[i].setBounds(j,358,100,25);
			this.myPanel[0].add(this.buttonBet[i]);
			if(i>0) this.buttonBet[i].setEnabled(false);
			j+=110;
		}
	}
	
	/* Button Handler Method */
	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == buttonBet[0]) {
				if(modalField.getText().equals("") || Integer.parseInt(modalField.getText()) == 0) {
					JOptionPane.showMessageDialog(null, "Modal tidak boleh kosong!");
				} else if(Integer.parseInt(modalField.getText()) > MAX_MODAL) {
					JOptionPane.showMessageDialog(null, "Modal tidak boleh lebih dari 1jt!");
				} else {
					initStartGame();
				}
			} else if(e.getSource() == buttonBet[1]) {
				initHitGame();
			} else if(e.getSource() == buttonBet[2]) {
				initPassGame();
			} else if(e.getSource() == buttonBet[3]) {
				initDoubleGame();
			} else if(e.getSource() == buttonBet[4]) {
				initStandGame();
			} else if(e.getSource() == buttonBet[5]) {
				initContinueGame();
			} else if(e.getSource() == buttonBet[6]) {
				initRestartGame();
			}
		}
	}
	
	/* Init Player Hands Cards */
	private void initPlayerHands() {
		try {
			for(i=0;i<this.buttonBet.length-2;i++) {
				this.buttonBet[i].setEnabled(true);
				if(i==0) this.buttonBet[i].setEnabled(false);
			}
			for(i=0;i<playerArrayCards.length;i++) {
				this.playerArrayCards[i] = (int) (Math.random()*13)+1;
				this.playerArrayInHandsCards[i] = this.playerArrayCards[i];
				this.bandarArrayCards[i] = (int) (Math.random()*13)+1;
			}
			for(i=0;i<2;i++) {
				this.myPanel[3].remove(userCards[i]);
				this.userCards[i] = new JLabel(new ImageIcon(ImageIO.read(new File("cards/" + playerArrayCards[i] + "S.png"))));
				this.userCards[i].setBounds(left[i],10,100,134);
				this.myPanel[3].add(this.userCards[i]);
				this.myPanel[3].revalidate();
				this.myPanel[3].repaint();
			}
			this.playerInHandsCards = 2;
			this.playerCardsPosition = 2;
		} catch(Exception e) {
			System.out.println("Error = " + e.getMessage());
		}
	}
	
	/* Init Bandar Hands Cards */
	private void initBandarHands() {
		int j=0;
		for(i=0;i<this.bandarArrayCards.length;i++) {
			if(this.bandarSumScore < 22) {
				this.bandarSumScore += (this.bandarArrayCards[i] > 10) ? 10 : this.bandarArrayCards[i];
				this.bandarInHandsCards+=1;
				this.bandarArrayInHandsCards[j] = (this.bandarArrayCards[i] > 10) ? 10 : this.bandarArrayCards[i];
				if(this.bandarMaxPass > 0) {
					if(this.bandarSumScore > 21) {
						this.bandarSumScore-=(this.bandarArrayCards[i] > 10) ? 10 : this.bandarArrayCards[i];
						this.bandarMaxPass-=1;
						this.bandarInHandsCards -= 1;
						j-=1;
						this.bandarArrayInHandsCards[j] = (this.bandarArrayCards[i-1] > 10) ? 10 : this.bandarArrayCards[i-1];
					}
				}
				if(this.bandarSumScore > 16 && this.bandarSumScore < 22) break;
				j+=1;
			}
		}
	}
	
	/* Init Start the Game */
	private void initStartGame() {
		this.modalField.setEnabled(false);
		this.initPlayerHands();
		this.initBandarHands();
	}
	
	/* Init Hit Game */
	private void initHitGame() {
		try {
			this.playerArrayInHandsCards[this.playerInHandsCards] = this.playerArrayCards[this.playerCardsPosition];
			for(i=0;i<=this.playerInHandsCards;i++) {
				this.myPanel[3].remove(this.userCards[i]);
				this.userCards[i] = new JLabel(new ImageIcon(ImageIO.read(new File("cards/" + this.playerArrayInHandsCards[i] + "S.png"))));
				this.userCards[i].setBounds(left[i],10,100,134);
				this.myPanel[3].add(this.userCards[i]);
				this.myPanel[3].revalidate();
				this.myPanel[3].repaint();
			}
			this.playerCardsPosition += 1;
			this.playerInHandsCards += 1;
		} catch(Exception e) {
			System.out.println("Error = " + e.getMessage());
		}
	}
	
	/* Init Pass Game */
	private void initPassGame() {
		this.playerCardsPosition += 1;
		this.playerMaxPass -= 1;
		if(this.playerMaxPass == 0) this.buttonBet[2].setEnabled(false);
	}
	
	/* Init Double Game */
	private void initDoubleGame() {
		try {
			this.playerArrayInHandsCards[this.playerInHandsCards] = this.playerArrayCards[this.playerCardsPosition];
			for(i=0;i<=this.playerInHandsCards;i++) {
				this.myPanel[3].remove(userCards[i]);
				this.userCards[i] = new JLabel(new ImageIcon(ImageIO.read(new File("cards/" + this.playerArrayInHandsCards[i] + "S.png"))));
				this.userCards[i].setBounds(left[i],10,100,134);
				this.myPanel[3].add(this.userCards[i]);
				this.myPanel[3].revalidate();
				this.myPanel[3].repaint();
			}
			this.initStandGame();
		} catch(Exception e) {
			System.out.println("Error = " + e.getMessage());
		}
	}
	
	/* Init Stand Game */
	private void initStandGame() {
		for(i=0;i<this.playerInHandsCards;i++) {
			this.playerArrayInHandsCards[i] = (this.playerArrayInHandsCards[i] > 10) ? 10 : this.playerArrayInHandsCards[i];
			this.playerSumScore += this.playerArrayInHandsCards[i];
		}
		this.initWinner();
	}
	
	/* Init Bandar Cards Table */
	private void initBandarCardsTable() {
		try {
			for(i=0;i<this.bandarInHandsCards;i++) {
				this.myPanel[2].remove(this.bandarCards[i]);
				this.bandarCards[i] = new JLabel(new ImageIcon(ImageIO.read(new File("cards/" + this.bandarArrayInHandsCards[i] + "S.png"))));
				this.bandarCards[i].setBounds(left[i],10,100,134);
				this.myPanel[2].add(this.bandarCards[i]);
				this.myPanel[2].revalidate();
				this.myPanel[2].repaint();
			}
		} catch(Exception e) {
			System.out.println("Error = " + e.getMessage());
		}
	}
	
	/* Init The Game Winner */
	private void initWinner() {
		this.initBandarCardsTable();
		boolean result = false;
		if(this.playerSumScore > this.bandarSumScore && (this.playerSumScore < 22 && (this.bandarSumScore < 22 || this.bandarSumScore > 21))) {
			JOptionPane.showMessageDialog(null, "Kamu Menang Score : " + this.playerSumScore + " vs " + this.bandarSumScore);
			result = true;
		} else if(this.playerSumScore == this.bandarSumScore && this.playerSumScore < 22) {
			JOptionPane.showMessageDialog(null, "Draw Score : " + this.playerSumScore + " vs " + this.bandarSumScore);
			result = true;
		} else if(this.playerSumScore < 22 && this.bandarSumScore > 21) {
			JOptionPane.showMessageDialog(null, "Kamu Menang Score : " + this.playerSumScore + " vs " + this.bandarSumScore);
			result = true;
		} else {
			JOptionPane.showMessageDialog(null, "Kamu Kalah Score : " + this.playerSumScore + " vs " + this.bandarSumScore);
			result = false;
		}
		
		if(result == true) {
			for(i=0;i<this.buttonBet.length;i++) {
				this.buttonBet[i].setEnabled(false);
				if(i>4) this.buttonBet[i].setEnabled(true);
			}
		} else {
			for(i=0;i<this.buttonBet.length;i++) {
				this.buttonBet[i].setEnabled(false);
				if(i==6) this.buttonBet[i].setEnabled(true);
			}
		}
	}
	
	/* Init Clear All Hands */
	private void initClearAllHands() throws Exception {
		for(i=0;i<this.userCards.length;i++) {
			this.myPanel[2].remove(this.bandarCards[i]);
			this.myPanel[3].remove(this.userCards[i]);
			this.myPanel[2].revalidate();
			this.myPanel[3].revalidate();
			this.myPanel[2].repaint();
			this.myPanel[3].repaint();
		}
	}
	
	/* Init Continue Game */
	private void initContinueGame() {
		try {
			this.initClearAllHands();
			this.initAllHands();
			this.playerCardsPosition = 0;
			this.playerInHandsCards = 0;
			this.bandarInHandsCards = 0;
			this.playerSumScore = 0;
			this.bandarSumScore = 0;
			this.playerArrayCards = new int[20];
			this.bandarArrayCards = new int[20];
			this.playerArrayInHandsCards = new int[20];
			this.bandarArrayInHandsCards = new int[20];
			for(i=0;i<this.buttonBet.length;i++) {
				this.buttonBet[i].setEnabled(false);
				if(i>0 && i<5) this.buttonBet[i].setEnabled(true);
			}
			this.initPlayerHands();
			this.initBandarHands();
		} catch(Exception e) {
			System.out.println("Error = " + e.getMessage());
		}
	}
	
	/* Init Restart Game */
	private void initRestartGame() {
		try {
			this.initClearAllHands();
			this.initAllHands();
			this.playerCardsPosition = 0;
			this.playerInHandsCards = 0;
			this.bandarInHandsCards = 0;
			this.playerSumScore = 0;
			this.bandarSumScore = 0;
			this.playerArrayCards = new int[20];
			this.bandarArrayCards = new int[20];
			this.playerArrayInHandsCards = new int[20];
			this.bandarArrayInHandsCards = new int[20];
			this.modalField.setEnabled(true);
			for(i=0;i<this.buttonBet.length;i++) {
				this.buttonBet[i].setEnabled(true);
				if(i>0) this.buttonBet[i].setEnabled(false);
			}
		} catch(Exception e) {
			System.out.println("Error = " + e.getMessage());
		}
	}
	
	/* init main app */
	public static void main(String[] args) {
		Blackjack blackjack = new Blackjack();
	}
	
}