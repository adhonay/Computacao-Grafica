package curvascg;


import java.awt.*;
import java.applet.*;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;


public class CurvasCG extends Applet  {


   Button draw1Button, modifyButton, deleteButton, clearButton;
   Tela canvas;
   TextField statusBar;

   
   public void interpolada1(){
       
      setLayout(new BorderLayout());
	PainelInterpolada dp = new PainelInterpolada();
	add("Center", dp);
	add("North",new ControleCurva(dp));
    
        
   }
   
   public void bezier1(){
       
          
      GridBagLayout layout = new GridBagLayout();
      setLayout(layout);
     
      GridBagConstraints constraints = new GridBagConstraints();
 

      draw1Button = new Button("Adiconar Curva");
      modifyButton = new Button("Modificar Curva");
      deleteButton = new Button("Deletar Curva");
      clearButton = new Button("Limpar");

      constraints.fill = GridBagConstraints.BOTH;
      constraints.weightx = 1;
   
      //bzier
      layout.setConstraints(draw1Button, constraints);
      add(draw1Button);

      
      //tela modifiar
      layout.setConstraints(modifyButton, constraints);
      add(modifyButton);
      
      //tela deletar curva
      //constraints.gridwidth = GridBagConstraints.RELATIVE;
      layout.setConstraints(deleteButton, constraints);
      add(deleteButton);

    
      //tela limpar
  
      constraints.gridwidth = GridBagConstraints.REMAINDER;
      layout.setConstraints(clearButton, constraints);
      add(clearButton);
 
      canvas = new Tela();
      constraints.weighty = 1;
      layout.setConstraints(canvas, constraints);
      add(canvas);

   }
  
    public void init(String x) {
     
     if(x.equalsIgnoreCase("interpolada1")){
       
      interpolada1();
      
     }else if(x.equalsIgnoreCase("bezier1")){
       
      bezier1();       
   }
}

 
   @Override
    public boolean action(Event evt, Object arg)
    {
      if (evt.target instanceof Button)
      { 
	HandleButtons(arg);
      }
      return true; 
    }

    public void HandleButtons(Object label)
    {
       
      canvas.ControleBotoes(label);
    }
    
    public static void bezier() {   
         
        JFrame bezier = new JFrame("Curva: Bézier"); 
        bezier.setSize(1280,720);
        bezier.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bezier.setLocationRelativeTo(null); 
        bezier.setVisible(true);
	CurvasCG interp = new CurvasCG();
	interp.init("bezier1");
	interp.start();
        interp.getLayout();
	bezier.add("Center", interp);
	bezier.show();
       
    }
    
    public static void interpolada(){
        
        JFrame interpo = new JFrame("Curva: Interpolada"); 
        interpo.setSize(1280,720);
        interpo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        interpo.setLocationRelativeTo(null); 
        interpo.setVisible(true);
	CurvasCG interp = new CurvasCG();
	interp.init("Interpolada1");
	interp.start();
        interp.getLayout();
	interpo.add("Center", interp);
	interpo.show();
    }
    

    
      public static void inicializar(){
          
         JButton bezier;
         JButton interpolada;
         
         bezier = new JButton("Bezier");
         interpolada = new JButton("Interpolada");
         
            JFrame teste = new JFrame("Escolha a Curva:");
         teste.setSize(500,100);
         teste.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         teste.setLocationRelativeTo(null); 
         GridBagLayout layout = new GridBagLayout();
         GridBagConstraints constraints = new GridBagConstraints();
         teste.setLayout(layout);

          constraints.fill = GridBagConstraints.BOTH;
        
          constraints.weighty = 33;
          constraints.weightx = 20;
          //bzier
          layout.setConstraints(bezier, constraints);
          teste.add(bezier);
          bezier.setBackground(Color.BLACK);
          bezier.setForeground(Color.RED);
          bezier.setFont(new Font("Calibri", Font.PLAIN, 21));
          //interpolada
          layout.setConstraints(interpolada, constraints);
          teste.add(interpolada);
          interpolada.setBackground(Color.BLACK);
          interpolada.setForeground(Color.RED);
          interpolada.setFont(new Font("Calibri", Font.PLAIN, 21));
          teste.show();
          
          bezier.addActionListener((ActionEvent e) -> {
              bezier();
              teste.dispose();
         });
          
          interpolada.addActionListener((ActionEvent e) -> {
              interpolada();
              teste.dispose();
         });
          
         
      }

     
     public static void main (String [] args){
         
         inicializar();
         //interpolada();
         //bezier();

    }
     
}
