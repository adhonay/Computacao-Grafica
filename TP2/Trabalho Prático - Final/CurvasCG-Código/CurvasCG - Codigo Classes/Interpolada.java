
package curvascg;

import java.awt.*;
import java.util.Vector;

class ControlPointInterpolada extends Object {
    public int x;
    public int y;
    public static final int PT_SIZE = 4;

    public ControlPointInterpolada(int a, int b) {
	x = a;
	y = b;
    }

    public boolean troc(int a, int b) {
        return a >= x - PT_SIZE && 
                b >= y - PT_SIZE &&
                a <= x + PT_SIZE &&
                b <= y + PT_SIZE;
    }
}

class PainelInterpolada extends Panel {
    public static final int INTERPOLADA = 1;
    private int mode = INTERPOLADA ;

    public static final int ADD = 0;
    public static final int MOVE = 1;
    public static final int DELETE = 2;
    private int	   action = ADD;

    private Vector points = new Vector(16,4);

    // Se um ponto de controle estiver sendo movido, este é o índice na lista
    // do ponto móvel. Caso contrário, contém -1
    private int moving_point;
    private int precision=10;

    public PainelInterpolada() {
	setBackground(Color.black);
    }

    public void setAcao(int action) {
  
	switch (action) {
	  case ADD:
	  case MOVE:
	  case DELETE:
	    this.action = action;
	    break;
	  default:
	    throw new IllegalArgumentException();
	}
    }

   public void setCurva(int mode) {
    // Escolher a curva 
	switch (mode) {
	  case INTERPOLADA:
	    this.mode = mode;
	    break;
	  default:
	    throw new IllegalArgumentException();
	}
    }

    public void LimparPontos() {
	points.removeAllElements();
    }

    private int EncontrarPontos(int a, int b) {
    // Digitalize a lista de pontos de controle para descobrir qual (se houver) ponto
    // contém as coordenadas: a, b.
    // Se um ponto for encontrado, retornar o índice do ponto, caso contrário, retornar -1
        int max = points.size();

	for(int i = 0; i < max; i++) {
	    ControlPointInterpolada pnt = (ControlPointInterpolada)points.elementAt(i);
	    if (pnt.troc(a,b)) {
		return i;
	    }
	}
	return -1;
    }

    @Override
    public boolean handleEvent(Event e) {
	switch (e.id) {
	  case Event.MOUSE_DOWN:
	    
	    switch (action) {
	      case ADD:
		
                int np=points.size();
		ControlPointInterpolada pnt;
             
                int i;
                for (i=0;
                    i<=np-1 && ((ControlPointInterpolada) points.elementAt(i)).x<e.x;i++) 
                { }
                pnt = new ControlPointInterpolada(e.x,e.y);
                if (i<=np-1) {
		  points.insertElementAt(pnt,i);
                } else {
                  points.addElement(pnt);
                } 
		repaint();
		break;
	      case MOVE:
		// Tente selecionar o ponto no local especificado.
		//Se não houver um ponto no local, findPoint retorna
		// 1 (isto é, não há nenhum ponto para ser movido)
		moving_point = EncontrarPontos(e.x, e.y);
		break;
	      case DELETE:
		
		int delete_pt = EncontrarPontos(e.x, e.y);
		if(delete_pt >= 0) {
		   points.removeElementAt(delete_pt);
		   repaint();
		}
		break;
	      default:
	        throw new IllegalArgumentException();
	    }
	    return true;
	  case Event.MOUSE_UP:
	    //Nós nos preocupamos apenas com o MOUSE_UP se estivermos movendo um controle
	    // ponto. Se for o caso, solte o ponto de controle.
	    if (moving_point >=0) {
		moving_point = -1;
	        repaint();
	    }
	    return true;
	  case Event.MOUSE_DRAG:
	    // Nós nos preocupamos com o MOUSE_DRAG enquanto estamos movendo um controle
	    // ponto. Caso contrário, não faça nada.
	    if (moving_point >=0) {
               int np=points.size();
              //teste se e.x for entre x de points.elementAt (moving_point + 1)

               if ( (moving_point==np-1 || 
                     e.x<=((ControlPointInterpolada)points.elementAt(moving_point+1)).x) &&
                    (moving_point==0 ||
                     e.x>=((ControlPointInterpolada)points.elementAt(moving_point-1)).x) )
               {    
                 ControlPointInterpolada pnt = (ControlPointInterpolada) points.elementAt(moving_point);
	         pnt.x = e.x;
                 pnt.y = e.y;
               } else {
                 // Caso contrário, encontre o slot correto (ou simplesmente faça isso?)
                 points.removeElementAt(moving_point);
                 int i;
                 for (i=0;
                    i<=np-2 && ((ControlPointInterpolada) points.elementAt(i)).x<e.x;i++) 
                 { }
                 ControlPointInterpolada pnt2 = new ControlPointInterpolada(e.x,e.y);
                 if (i<=np-1) {
		   points.insertElementAt(pnt2,i);
                 } else {
                   points.addElement(pnt2);
                 }
                 moving_point=i;
               }
	       repaint();
	    }
	    return true;
	  case Event.WINDOW_DESTROY:
	    System.exit(0);
	    return true;
	  default:
	    return false;
	}
    }
  
    private void SistemaLi(float sub[], float diag[], float sup[], float b[], int n){
/*                  sistema linear linear com tridiagonal n por matriz n
                     usando a eliminação gaussiana * sem * rotação
                     onde a (i, i-1) = sub [i] para 2 <= i <= n
                             a (i, i) = diag [i] para 1 <= i <= n
                             a (i, i + 1) = sup [i] para 1 <= i <= n-1
                     (os valores sub [1], sup [n] são ignorados)
                     o vetor do lado direito b [1: n] é substituído pela solução
                     NOTA: 1 ... n é usado em todos os arrays, 0 é inutilizado
                      OU SEJA METODO DE ESCALONAMENTO COMECANDO PELA PRIMEIRA COLUNA */
      int i;
/*                 factorização e substituição*/
      for(i=2; i<=n; i++){
        sub[i] = sub[i]/diag[i-1];           // encontrar multiplicador
        diag[i] = diag[i] - sub[i]*sup[i-1]; // atualizar diagonal superior. matriz
        b[i] = b[i] - sub[i]*b[i-1];         //atualizar o vetor rhs
      }
      b[n] = b[n]/diag[n];                   // resolver o diagonal superior. sistema por trás subst.
      for(i=n-1;i>=1;i--){
        b[i] = (b[i] - sup[i]*b[i+1])/diag[i];
      }
    }       

    @Override
    public void paint(Graphics g) {
         
      Graphics2D g2 = (Graphics2D) g;
        
	int np = points.size();           // numero de pontos
        float d[] = new float[np];        // Coeficientes da forma de Newton
        float x[] = new float[np];        //
        float y;
        float t;
        float oldy=0;
        float oldt=0;

        int npp = np*precision ;          // número de pontos utilizados para o desenho
	g.setColor(Color.WHITE);
	g.setPaintMode();
        g2.setStroke(new BasicStroke(2));
	//desenhe uma borda ao redor da tela
	g.drawRect(0,0, size().width-1, size().height-1);
      if (np>0) {
	// desenhe os pontos de controle
	for (int i=0; i < np; i++) {
	    ControlPointInterpolada p = (ControlPointInterpolada)points.elementAt(i);
            x[i]=p.x;
            d[i]=p.y;
	    g.drawRect(p.x-p.PT_SIZE, p.y-p.PT_SIZE,p.PT_SIZE*2,p.PT_SIZE*2); 
        //    g.drawString(String.valueOf(i),p.x+p.PT_SIZE,p.y-p.PT_SIZE);
	}
      switch (mode){ 
      case(INTERPOLADA): 
          g.setColor(Color.RED);
         
        if (np>1){  
          float a[] = new float[np];
          float t1;
          float t2;
          float h[] = new float[np];
          for (int i=1; i<=np-1; i++){
            h[i] = x[i] - x[i-1];
          }
          if (np>2){
            float sub[] = new float[np-1];
            float diag[] = new float[np-1];
            float sup[] = new float[np-1];
            
            for (int i=1; i<=np-2; i++){
              diag[i] = (h[i] + h[i+1])/3;
              sup[i] = h[i+1]/6;
              sub[i] = h[i]/6;
              a[i] = (d[i+1]-d[i])/h[i+1]-(d[i]-d[i-1])/h[i];
            }
            SistemaLi(sub,diag,sup,a,np-2);
          }
        
          oldt=x[0];
          oldy=d[0];
          g.drawLine((int)oldt,(int)oldy,(int)oldt,(int)oldy);
          for (int i=1; i<=np-1; i++) {   // loop entre intervalos entre nós
            for (int j=1; j<=precision; j++){
              t1 = (h[i]*j)/precision;
              t2 = h[i] - t1;
              y = ((-a[i-1]/6*(t2+h[i])*t1+d[i-1])*t2 +
                   (-a[i]/6*(t1+h[i])*t2+d[i])*t1)/h[i];
              t=x[i-1]+t1;
              g.drawLine((int)oldt,(int)oldy,(int)t,(int)y);
              oldt=t;
              oldy=y;
            }
          }
        }
        break;
      }
      }
    }
}

class ControleCurva extends Panel {
    PainelInterpolada target;
    Button st_add_label, st_move_label, st_delete_label, st_clear_label;
       
    public ControleCurva(PainelInterpolada target) {
	this.target = target;

      GridBagLayout layout = new GridBagLayout();
      setLayout(layout);
     
      GridBagConstraints constraints = new GridBagConstraints();
      
      st_add_label = new Button("Adicionar Curva");
      st_move_label = new Button("Modificar Curva");
      st_delete_label = new Button("Deletar Curva");
      st_clear_label = new Button("Limpar");
      
      constraints.fill = GridBagConstraints.BOTH;
      constraints.weightx = 1;
   
      //interpolada
      layout.setConstraints(st_add_label, constraints);
      add(st_add_label);

      
      //tela modifiar
      layout.setConstraints(st_move_label, constraints);
      add(st_move_label);
      
      //tela deletar curva
     
      layout.setConstraints(st_delete_label, constraints);
      add(st_delete_label);

    
      //tela limpar
      layout.setConstraints(st_clear_label, constraints);
      add(st_clear_label);
    
        
    }
    

    @Override
    public void paint(Graphics g) {
	 Dimension r=size();

	g.setColor(Color.lightGray);
	g.draw3DRect(0, 0, r.width, r.height, false);
    }

    @Override
    public boolean action(Event e, Object arg) {
	if (e.target instanceof Button) {
            String button = ((Button)(e.target)).getLabel();
	   // String cbox = ((Checkbox)(e.target)).getLabel();
	    if (button.equals("Adicionar Curva")) {
	    	target.setAcao(PainelInterpolada.ADD);
	    } else if (button.equals("Modificar Curva")) {
	    	target.setAcao(PainelInterpolada.MOVE);
	    } else if (button.equals("Deletar Curva")) {
	    	target.setAcao(PainelInterpolada.DELETE);
	    } else if (button.equals("Limpar")) {
	    	target.LimparPontos();

		// After clearing the control points, put the user back into
		// ADD mode, since none of the other modes make any sense.
	    	target.setAcao(PainelInterpolada.ADD);
		target.repaint();
	    }
	}
	return true;
    }
}


