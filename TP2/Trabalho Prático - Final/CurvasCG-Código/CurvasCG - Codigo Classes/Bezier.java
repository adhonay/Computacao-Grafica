
package curvascg;

import java.awt.*;

class Tela extends Canvas {
  Pontos pts[];
  int nline;
  int curObj;
  boolean drawing;  
  int action;
  final int DRAW_BEZIER=1, DRAW_BSPLINE=2, MODIFY=3, DELETE=4;
  ErrorFrame  errDlg;

  Image img = null;
  Graphics backg;

    public Tela() {
      pts = new Pontos[200];
      nline = -1;
      drawing = false;
      action = DRAW_BEZIER;

      
    }

    void setcursor(boolean working)
    {
      Cursor curs;
      if (working) 
	 curs = new Cursor(Cursor.HAND_CURSOR);
      else
         curs = new Cursor(Cursor.DEFAULT_CURSOR);
      setCursor(curs);
    }

  @Override
    public boolean mouseUp(Event evt, int x, int y)
    {
      if (action == DRAW_BEZIER || action == DRAW_BSPLINE)
      {
       if (drawing) {
	  if (!pts[nline].addPoint(x,y))
	  {
		if (!errDlg.isShowing()) errDlg.show();
		drawing = false;
		nline --;
		setcursor(drawing);
	  }
       }
       repaint();
      }
      if (action == MODIFY)
      {
        if (drawing) 
	{
	   drawing = false;
	   setcursor(drawing);
        }
      }
    
      if (action == DELETE)
      {
	if (curObj != -1)
	{
	   for (int i=curObj; i< nline; i++) pts[i] = pts[i+1];
	   nline--;
	   repaint();
	}
      }
      return true;
    }

  @Override
    public boolean mouseDown(Event evt, int x, int y)
    {
      if (action == DRAW_BEZIER )
      {
	if (drawing == false)
	{
	  nline ++;
	  if (action == DRAW_BEZIER) pts[nline] = new LinhaBezier();
	//  if (action == DRAW_BSPLINE) pts[nline] = new bspline();
          pts[nline].addPoint(x,y);
          drawing = true;
	  setcursor(drawing);
        }
	else {
          if (evt.clickCount == 2) {
	    if (!pts[nline].done())
	    {
		if (!errDlg.isShowing()) errDlg.show();
		nline --;
	    }
	    drawing = false;
	    setcursor(drawing);
          }
	}
      }
      if (action == MODIFY)
      {
	if (curObj != -1) {
	   drawing = true;
	   setcursor(drawing);
        }
      }
      return true;
    }

  @Override
    public boolean mouseMove(Event evt, int x, int y)
    {
      if (action == DRAW_BEZIER)
      {
	if (drawing)
	{
	 pts[nline].changePoint(x,y);
	 repaint();
        }
      }
      if (action == MODIFY || action == DELETE)
      {
	if (drawing == false)
	{
	  int oldObj = curObj;
	  curObj = -1;
	  for (int i=0; i<=nline; i++)
	  {
	    if (pts[i].inRegion(x,y) != -1) 
	    {
	      curObj = i;
	      break; 
	    }
          }
	  if (oldObj != curObj) repaint();
	}
      }
      return true;
    }

  @Override
    public boolean mouseDrag(Event evt, int x, int y)
    {
      if (action == MODIFY)
      {
	if (drawing == true)  {
            
	  pts[curObj].changeModPoint(x,y);
          
	  if (!pts[curObj].createFinal())
	  {
		if (!errDlg.isShowing()) errDlg.show();
		nline --;
	  }
	  repaint();
	}
      }
      return true;
    }

    public void ControleBotoes(Object label)
    {
      if (drawing)
      {
        drawing = false;
	setcursor(drawing);
      }
      if (label == "Limpar")
      {
	nline = -1;
	repaint();
	return;
      }
  
      if (action == DRAW_BEZIER || action == DRAW_BSPLINE)
      {
	if (drawing) pts[nline].done();
      }
      if (label == "Adiconar Curva")
      {
	action = DRAW_BEZIER;
	for (int i=0; i<=nline; i++)
	  pts[i].setShow(false);
        repaint();
      }
      else if (label == "Modificar Curva")
      {
	action = MODIFY;
	for (int i=0; i<=nline; i++)
	  pts[i].setShow(true);
        repaint();
      }
      else if (label == "Deletar Curva")
      {
	action = DELETE;
	for (int i=0; i<=nline; i++)
	  pts[i].setShow(true);
        repaint();
      }
    }

  @Override
    public void paint(Graphics g) 
    {
      update(g);
    }

  @Override
    public void update(Graphics g) {    
      int n;
      Dimension d=size();

      if (img == null)
      {
      img = createImage(d.width, d.height);
      backg = img.getGraphics();
      }

      backg.setColor(Color.BLACK);    //Definir cor para o plano de fundo
      backg.fillRect(0,0, d.width, d.height);  //Desenhar fundo

      // draw border
      backg.setColor(new Color(0,0,0));
      backg.drawRect(1,1,d.width-3,d.height-3);
      
      

      for (n=0; n <= nline; n++)
	pts[n].draw(backg);

      g.drawImage(img, 0, 0, this);
    }
}

class ErrorFrame extends Frame
{
    Label label;
    Button button;
    String errMsg;

  
}

 class Pontos {
    Ponto pt[];
    int num;
   
    boolean showLine;
    int curPt;
    final int MAXCNTL = 50;
    final int range = 5;

    Pontos() {
	num = 0;
	curPt = -1;
	pt = new Ponto[MAXCNTL];
    }

    public boolean addPoint(int x, int y)
    {
	if (num == MAXCNTL) return false;
	pt[num] = new Ponto(x,y);
	num++;
	return true;
    }

    void changePoint(int x, int y)
    {
	pt[num-1].x = x;
	pt[num-1].y = y;
    }

    void changeModPoint(int x, int y)
    {
	pt[curPt].x = x;
	pt[curPt].y = y;
    }

    boolean createFinal()
    {
      return true;
    }

    boolean done()
    {
      return true;
    }

    void setShow(boolean show)
    {
      showLine = show;
    }

    int inRegion(int x, int y)
    {
       int i;
       for (i=0; i<num; i++) 
	 if (Math.abs(pt[i].x-x) < range && Math.abs(pt[i].y-y) < range)
	 {
	   curPt = i;
	   return i;
         }
       curPt = -1;
       return -1;
    }

    void draw(Graphics g)
    {
      int i;
      int l = 3;
      for (i=0; i< num-1; i++)
      {
	g.drawLine(pt[i].x-l, pt[i].y, pt[i].x+l, pt[i].y);
	g.drawLine(pt[i].x, pt[i].y-l, pt[i].x, pt[i].y+l);
        DesenhoLinha(g, pt[i].x,pt[i].y,pt[i+1].x,pt[i+1].y);   //desenhar segmento/linha
      }
      g.drawLine(pt[i].x-l, pt[i].y, pt[i].x+l, pt[i].y);
      g.drawLine(pt[i].x, pt[i].y-l, pt[i].x, pt[i].y+l);
    }

    
//desenhar linhas de traÃ§o
    protected void DesenhoLinha(Graphics g, int x1, int y1, int x2, int y2)
    {
      final float seg = 8;
      double xl, yl;

      if (x1 == x2)
      {
	  if (y1 > y2)
	  {
            int tmp = y1;
	    y1 = y2;
	    y2 = tmp;
	  }
	  yl = (double)y1;
	  while (yl < y2)
	  {
	     double y0 = Math.min(yl+seg, (double)y2);
             g.drawLine(x1, (int)yl, x2, (int)y0);
	     yl = y0 + seg;
	  }
	  return;
      }
      else if (x1 > x2) 
      {
	int tmp = x1;
	x1 = x2;
	x2 = tmp;
        tmp = y1;
	y1 = y2;
	y2 = tmp;
      }
      double ratio = 1.0*(y2-y1)/(x2-x1);
      double ang = Math.atan(ratio);
      double xinc = seg * Math.cos(ang);
      double yinc = seg * Math.sin(ang);
      xl = (double)x1;
      yl = (double)y1;

      while ( xl <= x2 )
      {
	  double x0 = xl + xinc;
	  double y0 = yl + yinc;
	  if (x0 > x2) {
	     x0 = x2;
	     y0  = yl + ratio*(x2-xl);
	  }
          g.drawLine((int)xl, (int)yl, (int)x0, (int)y0);
	  xl = x0 + xinc;
	  yl = y0 + yinc;
      }
    }
}


class LinhaBezier extends Pontos {
    Ponto bpt[];
    int bnum;
    boolean ready;
    final int MAXPOINT = 1800;
    final int ENOUGH = 2;
    final int RECURSION = 900;
    int nPointAlloc;
    int enough;		// controle o quão bem desenhamos a curva.
    int nRecur;		// contador de número de recursão
    Ponto buffer[][];
    int nBuf, nBufAlloc;

    LinhaBezier() {
        bpt = new Ponto[MAXPOINT];
	nPointAlloc = MAXPOINT;
	bnum = 0;
	enough = ENOUGH;
	showLine = true;
	ready = false;
	buffer = null;
    }

    protected int Distancia(Ponto p0,Ponto p1,Ponto p2)
    {
        int a,b,y1,x1,d1,d2;
     
	if(p1.x==p2.x && p1.y==p2.y) return Math.min(Math.abs(p0.x-p1.x),Math.abs(p0.y-p1.y));
        a=p2.x-p1.x;    b=p2.y-p1.y;
	y1=b*(p0.x-p1.x)+a*p1.y;
	x1=a*(p0.y-p1.y)+b*p1.x;
	d1=Math.abs(y1-a*p0.y);
	d2=Math.abs(x1-b*p0.x);
	if (a==0) return Math.abs(d2/b);
	if (b==0) return Math.abs(d1/a);
	return Math.min(Math.abs(d1/a),Math.abs(d2/b));
    }

    //mostrar curva
    protected void DivCurva(Ponto p[],Ponto q[],Ponto r[],int num)
    {
      int i,j;
      //num=100;
//      for (i=0;i<num;i++) q[i] = new Ponto(p[i]);
      for (i=0;i<num;i++) q[i].copy(p[i]);
      for (i=1;i<=num-1;i++) {
//         r[num-i] = new Ponto(q[num-1]);
         r[num-i].copy(q[num-1]);
	 for (j=num-1;j>=i;j--) {
//	    q[j] = new Ponto((q[j-1].x+q[j].x)/2, (q[j-1].y+q[j].y)/2);
	    q[j].x = (q[j-1].x+q[j].x)/2;
	    q[j].y = (q[j-1].y+q[j].y)/2;
	 }
      }
//      r[0] = new Ponto(q[num-1]);
      r[0].copy(q[num-1]);
    }

    //reusar buffer
    private Ponto get_buf(int num)[]
    {
      Ponto b[];
      if (buffer == null)
      {
        buffer = new Ponto[500][num];
	nBufAlloc = 500;
	nBuf = 0;
      }
      if (nBuf == 0)
      {
	b = new Ponto[num];
	for (int i=0; i< num; i++) b[i] = new Ponto();
	return b;
      }
      else {
	nBuf --;
	b = buffer[nBuf];
        return b;
      }
    }

    private void put_buf(Ponto b[])
    {
      if (nBuf >= nBufAlloc)
      {
        Ponto newBuf[][] = new Ponto[nBufAlloc + 500][num];
          System.arraycopy(buffer, 0, newBuf, 0, nBuf);//for (int i=0; i<nBuf; i++) newBuf[i] = buffer[i];
	nBufAlloc += 500;
	buffer = newBuf;
      }
      buffer[nBuf] = b;
      nBuf++;
    }

    protected boolean BezierCurvaSec(Ponto pt[], int num, Ponto result[], int n[])
    {
      Ponto   qt[],rt[];	// Dividir 
      int d[],i,max;
      
      nRecur++;
      if (nRecur > RECURSION) return false;

      d = new int[MAXCNTL];
      for (i=1;i<num-1;i++) d[i]=Distancia(pt[i],pt[0],pt[num-1]);
      max=d[1];
      for (i=2;i<num-1;i++) if (d[i]>max) max=d[i];
      if (max <= enough || nRecur > RECURSION) {
	   if (n[0]==0) {
	      if (bnum > 0) 
		 result[0].copy(pt[0]);
	      else
	         result[0] = new Ponto(pt[0]);
	      n[0]=1;
	   }
	   //reuse
	   if (bnum > n[0])
		 result[n[0]].copy(pt[num-1]);
	   else
	         result[n[0]] = new Ponto(pt[num-1]);
	   n[0]++;
	   if (n[0] == MAXPOINT-1) return false;
      }
      else {

           qt = get_buf(num);
           rt = get_buf(num);
	   DivCurva(pt,qt,rt,num);
	   if (!BezierCurvaSec(qt,num,result,n)) return false;
	   put_buf(qt);
	   if (!BezierCurvaSec(rt,num,result,n)) return false;
	   put_buf(rt);
      }
      return true;
    }

    public boolean BezierCurvaPri(Ponto pt[], int num, Ponto result[], int n[])
    {
       int oldN = n[0];

       if (enough == ENOUGH && num > 6) enough += 3;
//       if (enough > ENOUGH) enough -= 5;
       nRecur = 0;
       // em caso de excesso de pilha de recursão, continue tentando
       while (!BezierCurvaSec(pt, num, bpt, n))
       {
          n[0] = oldN;
	  enough += 5;
          nRecur = 0;
       }
       return true;
    }

    @Override
    boolean createFinal()
    {
       int n[];
       n = new int[1];
       if (!BezierCurvaPri(pt, num, bpt, n)) 
       {
	  bnum = 0;
	  return false;
       }
       else {
          bnum = n[0];
          return true;
       }
    }

    @Override
    boolean done()
    {
       num --;
       showLine = false;
       ready = true;
       return createFinal();
    }

    @Override
    void draw(Graphics g)
    {
     Graphics2D g2 = (Graphics2D) g;
        
      if (showLine)
      {
       g.setColor(Color.RED);// cor das linhas
       g2.setStroke(new BasicStroke(2));
	super.draw(g);
	if (curPt != -1)
	  g.drawRect(pt[curPt].x-range, pt[curPt].y-range, 2*range+1,2*range+1);
      }

      if (ready)
           
           g.setColor(Color.WHITE);//cor da curva
      for (int i=0; i< bnum-1; i++)
      {
        g.drawLine(bpt[i].x,bpt[i].y,bpt[i+1].x,bpt[i+1].y);   
      }
    }
}

class Ponto 
{
    int x,y;

    Ponto(Ponto p)
    {
       x = p.x;
       y = p.y;
    }
    Ponto(int _x, int _y)
    {
       x = _x;
       y = _y;
    }
    Ponto()
    {
       x = 0;
       y = 0;
    }
    void copy(Ponto p)
    {
       x = p.x;
       y = p.y;
    }
}
