package drawer.control;

import java.awt.Point;
import java.awt.image.BufferedImage;



public class Algoritimos {


	
	public double PI  = 3.14;
	
	public Point p1;
	public Point p2;
	
	public int currentFigure; 
	
	 /**
	   *    DDA					0
	   *	Bresenham Lines 	1
	   *	Bresenham Circles	2
	   */
	
	
	/**
	 * Implementação DDA
	 * @param x1 x do ponto 1
	 * @param y1 y do ponto 1
	 * @param x2 x do ponto 2
	 * @param y2 y do ponto 2
	 * @param image 
	 */
	public void dda(double x1, double y1, double x2, double y2, BufferedImage image, int cor){
		
		int dx, dy, steps;
		
	    double xinc, yinc, x, y;

	    dx = (int)(x2-x1);
	    dy = (int)(y2-y1);
	    
	    steps = (Math.abs(dx)> Math.abs(dy)? Math.abs(dx): Math.abs(dy));

	    x = (int)x1; y = (int)y1;
    
	    xinc =(double)dx/steps;
	    yinc =(double)dy/steps;	
	    
	    image.setRGB((int)Math.round(x), (int)Math.round(y), cor );
	    
	    for(int i=1; i<=steps; i++){
	        x+=xinc;
	        y+=yinc;

	        image.setRGB((int)Math.round(x), (int)Math.round(y),  cor);        
	    }
	}
	
	/**
	 * Implementação Algoritmo de Bresenham para desenhar retas.
	 * @param x1 x do ponto 1
	 * @param y1 y do ponto 1
	 * @param x2 x do ponto 2
	 * @param y2 y do ponto 2
	 * @param image
	 */
	public void bresenhamLines(int x1, int y1, int x2, int y2, BufferedImage image, int cor){
		
		int dx = x2-x1;
		int dy = y2-y1;
		int x, y, xincr, yincr, p, c1, c2;
		
		if(dx < 0){
			dx = -dx;
			xincr = -1;
		}
		else xincr = 1;
		
		if(dy < 0){
			dy = -dy;
			yincr = -1;
		}else yincr = 1;
		
		x = x1; y = y1;
		
		image.setRGB(x, y, cor);
		
		if(dx > dy){
			p = 2*dy -dx;
			c1 = 2*dy;
			c2 = 2*(dy-dx);
			for(int i=0; i<dx; i++){
				x+=xincr;
				if(p < 0) p+=c1;
				else{
					p+=c2;
					y+=yincr;
				}
				image.setRGB(x, y, cor);
			}
		}
		else{
			p = 2*dx-dy;
			c1 = 2*dx;
			c2 = 2*(dx-dy);
			for (int i = 0; i < dy; i++){
				y+=yincr;
				if(p < 0) p+=c1;
				else{
					p+=c2;
					x+=xincr;
				}
				image.setRGB(x, y, cor);
			}
		}
		
	}
	/**
	 * Algoritmo de Bresenham para desenhar circunferencias.
	 * @param xc x do centro
	 * @param yc y do centro
	 * @param r raio
	 * @param image
	 */
	public void bresenhamCircles(int xc, int yc, int r, BufferedImage image, int cor){
		int x, y, p;
		x = 0; y = r;
		p = 3-2*r;

		plotSimetricas(xc, yc, x, y, image, cor);
	
	    while (x < y)
	    {
	        if(p < 0) p+=4*x+6;
	        else{
	        	p+=4*(x-y)+10;
	        	y--;
	        }
	        x++;
	        plotSimetricas(xc, yc, x, y, image, cor);
	    }
	}
	
	/**
	 * Funcao auxiliar do bresenhamCircles
	 * @param xc x do centro
	 * @param yc y do centro
	 * @param x do novo ponto a ser plotado
	 * @param y do novo ponto a ser plotado
	 * @param image
	 */
	public void plotSimetricas(int xc, int yc, int x, int y, BufferedImage image, int cor) {

		image.setRGB(xc+x, yc+y, cor);
		image.setRGB(xc-x, yc+y, cor);
		
		image.setRGB(xc+x, yc-y, cor);
		image.setRGB(xc-x, yc-y, cor);
		
		image.setRGB(xc+y, yc+x, cor);
		image.setRGB(xc-y, yc+x, cor);
		
		image.setRGB(xc+y, yc-x, cor);
		image.setRGB(xc-y, yc-x, cor);
	}

	/**
	 * Algoritmo de translação
	 * @param x1 x do ponto 1
	 * @param y1 y do ponto 1
	 * @param x2 x do ponto 2
	 * @param y2 y do ponto 2
	 * @param ta fator de translacao do ponto 1
	 * @param tb fator de translacao do ponto 2
	 * @param image
	 */
	public void translaction(double x1, double y1, double x2, double y2, int ta, int tb, BufferedImage image, int cor){

		// Transladar linhas usando DDA
		
		x1+=ta; y1+=tb;
		x2+=ta; y2+=tb;
		p1.setLocation((int)x1, (int)y1);
		p2.setLocation((int)x2, (int)y2);
		
		if(currentFigure == 0){
			dda(x1, y1, x2, y2, image, cor);
		}
		else if(currentFigure == 1){
			bresenhamLines((int)x1, (int)y1, (int)x2, (int)y2, image, cor);
		}
		else{
			x1 = (int)p1.getX(); x2 = (int) p2.getX();
			y1 = (int)p1.getY(); y2 = (int) p2.getY();
			int r  = (int) Math.pow((x2-x1), 2) + (int)Math.pow((y2-y1), 2);
			r = (int) Math.sqrt(r);
			bresenhamCircles((int)x1, (int)y1, r, image, cor);
		}
	}
	
	
	/**
	 * Algoritmo para escala que considera o ponto 1 como referencia e o move para
	 * origem.
	 * @param x1 x do ponto 1
	 * @param y1 y do ponto 1
	 * @param x2 x do ponto 2
	 * @param y2 y do ponto 2
	 * @param ta fator de escala
	 * @param image
	 */
	public void scale(double x1, double y1, double x2, double y2, double ta, BufferedImage image, int cor){
		
		double x = x1; double y = y1;
		x1 = y1 = 0;
		
		x2 = x2-x;
		y2 = y2-y;
		
		x2 = x2*ta;
		y2 = y2*ta;
		
		x2 = x2+x;
		y2 = y2+y;
		
		x1 = x; y1 = y;
		
		//p1.setLocation((int)x1, (int)y1);
		p2.setLocation((int)x2, (int)y2);
		if(currentFigure == 0){
			dda(x1, y1, x2, y2, image, cor);
		}
		else if(currentFigure == 1){
			bresenhamLines((int)x1, (int)y1, (int)x2, (int)y2, image, cor);
		}
		else{
			x1 = (int)p1.getX(); x2 = (int) p2.getX();
			y1 = (int)p1.getY(); y2 = (int) p2.getY();
			int r  = (int) Math.pow((x2-x1), 2) + (int)Math.pow((y2-y1), 2);
			r = (int) Math.sqrt(r);
			bresenhamCircles((int)x1, (int)y1, r, image, cor);
		}
	}
	
	public void rotate(int x1, int y1, int x2, int y2, double grau, BufferedImage image, int cor){
		double rad = (grau * (PI/180)) * -1;
		int x3 = (int) ((Math.round(x2-x1) * Math.cos(rad)) - ((y2-y1) * Math.sin(rad)));
		int y3 = (int) ((Math.round(x2-x1) * Math.sin(rad)) + ((y2-y1) * Math.cos(rad)));
		
		clearLastObject(x1, y1, x2, y2, image, 0xFFFFFFFF);
		x2 = x3+x1;
		y2 = y3+y1;
		
		
		p2.setLocation((int)x2, (int)y2);
		if(currentFigure == 0){
			dda(x1, y1, x2, y2, image, cor);
		}
		else if(currentFigure == 1){
			bresenhamLines(x1, y1, x2, y2, image, cor);
		}
		else{
			x1 = (int)p1.getX(); x2 = (int) p2.getX();
			y1 = (int)p1.getY(); y2 = (int) p2.getY();
			int r  = (int) Math.pow((x2-x1), 2) + (int)Math.pow((y2-y1), 2);
			r = (int) Math.sqrt(r);
			bresenhamCircles(x1, y1, r, image, cor);
		}
		
	
	}
	
	public void clearLastObject(double x1, double y1, double x2, double y2, BufferedImage image, int cor){
		
		if(currentFigure == 0){
			dda(x1, y1, x2, y2, image, 0xFFFFFFFF);
		}
		else if(currentFigure == 1){
			bresenhamLines((int)x1, (int)y1, (int)x2, (int)y2, image, 0xFFFFFFFF);
		}
		else{
			x1 = (int)p1.getX(); x2 = (int) p2.getX();
			y1 = (int)p1.getY(); y2 = (int) p2.getY();
			int r  = (int) Math.pow((x2-x1), 2) + (int)Math.pow((y2-y1), 2);
			r = (int) Math.sqrt(r);
			bresenhamCircles((int)x1, (int)y1, r, image, 0xFFFFFFFF);
		}
	}

	

	
	
}
