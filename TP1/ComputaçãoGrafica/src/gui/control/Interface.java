package gui.control;


import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import drawer.control.Algoritimos;




public class Interface extends JFrame{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Algoritimos algo;
	
	public String [] cores;
	//public String [] resolucoes;
	
	public int color;
	public int resolution;
	
	public BufferedImage image;
	public JLabel label;
	public JFrame frame;
	
	public JMenuBar  menuBar;
	public JComboBox<String> combCorSelector;
	public JComboBox<String> combResoSelector;
	
	
	public JMenu menuLines;
	public JMenu menuCircles;
	public JMenu menuOperations;
	public JMenu menuHelp;

	
	public JMenuItem menuItemDDA;
	public JMenuItem menuItemBresenLines;
	public JMenuItem menuItemBresenCircles;
	public JMenuItem menuItemClear;
	public JMenuItem menuItemTranslation;
	public JMenuItem menuItemScale;
	public JMenuItem menuItemRotation;
	public JMenuItem menuHowUseItem;
	

	
	
	public int clicks;
	

	/**
	 * Construtor principal
	 */
	public Interface(){
		
		
		algo       = new Algoritimos();
		cores      = new String [] {"Vermelho", "Verde", "Azul"};
		color      = 0xFFFF0000;
		
		frame = new JFrame("Computação Gráfica");
		frame.getContentPane().setBackground(new Color(0x00000000));
		image = new BufferedImage(1024, 768, BufferedImage.TYPE_4BYTE_ABGR);	
		label = new JLabel(new ImageIcon(image));

		algo.p1 = new Point();
		algo.p2 = new Point();
		
		
		menuInit();
		
		frame.setSize(1024, 768);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		//frame.setResizable(false);
		
		frame.add(label);
		
		
		

	}
	
	/**
	 * Inicia Todos os componentes Menu da Interface
	 */
	
	public void menuInit(){
		
		menuBar = new JMenuBar();
		
		/* Menus */
		menuLines      = new JMenu("Retas");
		menuCircles    = new JMenu("Circunferência");
		menuOperations = new JMenu("Operações");
		menuHelp	   = new JMenu("Ajuda");
		/* --- */
		
		
		/* Combo Box*/
		combCorSelector  = new JComboBox<String>(cores);
		
		
		/* ----*/
		
		
		/* Menu Itens */
		menuItemDDA  		  = new JMenuItem("DDA");
		menuItemBresenLines   = new JMenuItem("Bresenham");
		
		menuItemBresenCircles = new JMenuItem("Bresenham");
		
		menuItemClear         = new JMenuItem("Limpar Tela");
		menuItemTranslation   = new JMenuItem("Translação");
		menuItemScale		  = new JMenuItem("Escala");
		menuItemRotation	  = new JMenuItem("Rotação");
		menuHowUseItem		  = new JMenuItem("Modo de Uso");
		/* ----- */
		
		frame.setJMenuBar(menuBar);
		

		
		
		/* Adicionando Componentes */
		menuBar.add(menuLines);
		menuBar.add(menuCircles);
		menuBar.add(menuOperations);
		menuBar.add(menuHelp);
		menuBar.add(combCorSelector);
		
		//menuBar.add(combResoSelector);
		
		menuLines.add(menuItemDDA);
		menuLines.add(menuItemBresenLines);
		
		menuCircles.add(menuItemBresenCircles);
		
		menuOperations.add(menuItemTranslation);
		menuOperations.add(menuItemScale);
		menuOperations.add(menuItemRotation);
		menuOperations.add(menuItemClear);
		
		menuHelp.add(menuHowUseItem);
		
	    menuBar.setLayout(new GridLayout(1,1)); 
		
		/* ------- */

	}
	
	/**
	 * Metodo responsavel por capturar e tratar os eventos 
	 * de clique do mouse.
	 */
	public void capture(){
		
		
		frame.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				clicks++;
				System.out.println("Ponto = " + e.getX() + " "+ e.getY());
		    	if(clicks % 2 == 0)
		    		algo.p2.setLocation(e.getX(), e.getY());
		    	else{
		    		algo.p1.setLocation(e.getX(), e.getY());
		    	}
			}
		});
		
		menuItemBresenLines.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ev) {
		            System.out.println("BRESENHAM OK!");
		            algo.currentFigure = 1;
		            if(clicks >= 2){
		            	algo.bresenhamLines((int)algo.p1.getX(), (int)algo.p1.getY(), (int)algo.p2.getX(), (int)algo.p2.getY(), image, color);
		            	frame.repaint();
		            }		      
		    }
		});
		
		menuItemDDA.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ev) {
		    	System.out.println("DDA OK!");
		    	algo.currentFigure = 0;
		        if(clicks >= 2){
		        	algo.dda(algo.p1.getX(), algo.p1.getY(), algo.p2.getX(), algo.p2.getY(), image, color);
		        	frame.repaint();
		        }
		    }
		});
		
		menuItemClear.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				System.out.println("CLEAR OK!");
				
				    for (int i = 0; i < image.getWidth(); i++)
						for (int j = 0; j < image.getHeight(); j++)
							image.setRGB(i, j, 0x00000000);
				    frame.repaint();
				
			}
		});
		menuItemTranslation.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				int x = Integer.parseInt(JOptionPane.showInputDialog(null," Digite o fator de translação para x: "));
				int y = Integer.parseInt(JOptionPane.showInputDialog(null," Digite o fator de translação para y: "));
				
				algo.translaction(algo.p1.getX(), algo.p1.getY(), algo.p2.getX(), algo.p2.getY(), x, y, image, color);
				frame.repaint();
			}
		});
		
		menuItemScale.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				double s = Double.parseDouble(JOptionPane.showInputDialog(null," Digite o fator de escala: "));
				algo.scale(algo.p1.getX(), algo.p1.getY(), algo.p2.getX(), algo.p2.getY(), s, image, color);
				frame.repaint();
			}
		});
		
		menuItemRotation.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ev){
				double g = Double.parseDouble(JOptionPane.showInputDialog(null," Digite quantos graus deseja rotacionar o objeto: "));
				algo.rotate((int)algo.p1.getX(), (int)algo.p1.getY(), (int)algo.p2.getX(), (int)algo.p2.getY(), g, image, color);
				frame.repaint();
			}
		});
		
		menuItemBresenCircles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("BRESENHAM CIRCLES OK!");
				algo.currentFigure = 2;
				if(algo.p1.getX() != 0.0 && algo.p2.getX() != 0.0){

					int x1 = (int)algo.p1.getX(); int x2 = (int) algo.p2.getX();
					int y1 = (int)algo.p1.getY(); int y2 = (int) algo.p2.getY();
					int r  = (int) Math.pow((x2-x1), 2) + (int)Math.pow((y2-y1), 2);

					r = (int) Math.sqrt(r);
					algo.bresenhamCircles(x1, y1, r, image, color);
		        	frame.repaint();
				}
			}
		});
		
		menuHowUseItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(null, "1- Todas as operações serão aplicadas sobre os dois últimos pontos selecionados.\n"
						+ "2- Selecione dois pontos na tela usando o botão esquerdo do mouse.\n"
						+ "3- Utilize os menus no canto superior para selecionar os algoritimos e operações gráficas que deseja trabalhar.\n"
						+ "5- Selecionar a cor desejada correspondente ao algoritimo.\n"
						+ "6- Apos os pontos selecionados, utilize a opereção que deseja posteriormente.\n"
						+ "7- Utilize as cores vermelho e verde para retas e azul para circunferência.", "Ajuda",
				        JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		
		
		combCorSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int x = combCorSelector.getSelectedIndex();
				if(x == 0)
					color = 0xFFFF0000;
				else if(x == 1)
					color = 0xFF00FF00;
				else
					color = 0xFF0000FF;
			}
		});
		
		combResoSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int x = combResoSelector.getSelectedIndex();
				if(x == 0){
					resolution = 0;
					frame.setSize(1600, 900);
				}
				else if(x == 1){
					resolution = 1;
					frame.setSize(1024, 768);

				}
				else{
					resolution = 2;
					frame.setSize(800, 600);
				}
			}
		});
		
	}
	
	public static void main(String [] args){

		Interface a = new Interface();
		a.capture();
		  
	}



}
