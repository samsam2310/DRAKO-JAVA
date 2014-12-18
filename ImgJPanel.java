package DRAKO.unit;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.image.PixelGrabber;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class ImgJPanel extends JPanel
{
	private static final long serialVersionUID=1L;
	Image img;
	int height,width;
	float al=1.0f;
	Shape shape;
	GraphicsConfiguration gc;
	public ImgJPanel(String imgn){
		img=Toolkit.getDefaultToolkit().createImage(imgn);
		setPreferredSize();
		shape=getImageShape();
		setOpaque(false);

		// gc=new this.getGraphicsConfiguration();
		// Image drawimg=gc.createCompatibleImage(width,height,Transparency.TRANSLUCENT);
		// Graphics2D g=(Graphics2D)drawimg.getGraphics(); //在画布上创建画笔
		// Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .9f); //指定透明度为半透明90%
		// g.setComposite(alpha);
		// g.drawImage(img,0,0,this); //注意是,将image画到g画笔所在的画布上
		// g.setColor(Color.black);//设置颜色为黑色
	}
	public ImgJPanel(Image imgn){
		this.img=imgn;
		setPreferredSize();
		shape=getImageShape();
		setOpaque(false);
	}
	public void set_img(Image imgn){
		this.img=imgn;
		repaint();
	}
	public void setPreferredSize() { 
		ImageIcon tmp=new ImageIcon(img);
		setPreferredSize(new Dimension(tmp.getIconWidth(),tmp.getIconHeight()));
	}
	@Override public void paintComponent(Graphics g){
		super.paintComponent(g);
        Graphics2D g2=(Graphics2D) g;
        Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,al); //指定透明度为半透明90%
		g2.setComposite(alpha);
        //g.setColor(this.getBackground());
        //g.setColor(this.getForeground());
        //g.drawPolygon(img);
        g2.drawImage(img,null,null);
    }
    @Override public boolean contains(int x, int y){
        return shape.contains(x,y);
    }
    public Shape getImageShape(){
	    ArrayList<Integer> x=new ArrayList<Integer>();
	    ArrayList<Integer> y=new ArrayList<Integer>();   
	    ImageIcon icon = new ImageIcon(img);
	    width=icon.getIconWidth();//图像宽度
	    height=icon.getIconHeight();//图像高度
	    //筛选像素
	    //首先获取图像所有的像素信息
	    PixelGrabber pgr = new PixelGrabber(img, 0, 0, -1, -1, true);
	    try{
	    	pgr.grabPixels();
	    }catch(InterruptedException ex){
	    	ex.getStackTrace();
	    }
	    int pixels[] = (int[]) pgr.getPixels();
	    //循环像素
	    for(int i=0;i<pixels.length;i++){
	        //筛选，将不透明的像素的坐标加入到坐标ArrayList x和y中      
	        int alpha = getAlpha(pixels[i]);
	        if (alpha == 0){
		    	continue;        
		    }else{
		    	x.add(i%width>0 ? i%width-1:0);
		    	y.add(i%width==0 ? (i==0 ? 0:i/width-1):i/width);
	        }      
	    }
	    //建立图像矩阵并初始化(0为透明,1为不透明)
	    int[][] matrix=new int[height][width];    
	    for(int i=0;i<height;i++){
	    	for(int j=0;j<width;j++){
	    		matrix[i][j]=0;
	    	}
	    }
	    //导入坐标ArrayList中的不透明坐标信息
	    for(int c=0;c<x.size();c++){
	    	matrix[y.get(c)][x.get(c)]=1;
	    }
	    /* 由于Area类所表示区域可以进行合并，我们逐一水平"扫描"图像矩阵的每一行，
	     * 将不透明的像素生成为Rectangle，再将每一行的Rectangle通过Area类的rec
	     * 对象进行合并，最后形成一个完整的Shape图形
	     */
	    Area rec=new Area();
	    int temp=0;
	    for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
	        	if(matrix[i][j]==1){
	        		if(temp==0)
	            		temp=j;  
	          		else if(j==width){
	            		if(temp==0){
	            			Rectangle rectemp=new Rectangle(j,i,1,1);
	            			rec.add(new Area(rectemp));
	            		}else{
	              			Rectangle rectemp=new Rectangle(temp,i,j-temp,1);
	            			rec.add(new Area(rectemp));
	            			temp=0;
	            		}
	          		}
	        	}else{
	        		if(temp!=0){
	            		Rectangle rectemp=new Rectangle(temp,i,j-temp,1);
	            		rec.add(new Area(rectemp));
	        			temp=0;
	          		}
	        	}
	    	}
	    	temp=0;
		}
		return rec;
	}
	private int getAlpha(int pixel) {
		return (pixel >> 24) & 0xff;
	}
	public void setAl(float al){
		this.al=al;
	}
}