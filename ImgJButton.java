package DRAKO.unit;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.image.*;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class ImgJButton extends JButton
{
	private static final long serialVersionUID=1L;
	Image img;
	BufferedImage img_b;
	Shape shape;
	float al=1.0f;
	String text=new String("");
	int fbig=32;
	boolean if_gray=false;
	public ImgJButton(String imgn){
		img=Toolkit.getDefaultToolkit().createImage(imgn);
		setOpaque(false);
		setPreferredSize();
		shape=getImageShape();
	}
	public ImgJButton(Image img){
		this.img=img;
		setOpaque(false);
		setPreferredSize();
		shape=getImageShape();
	}
	public ImgJButton(String st,Image img,int fbig){
		this.fbig=fbig;
		text=st;
		this.img=img;
		setOpaque(false);
		setPreferredSize();
		shape=getImageShape();
	}
	public void set_btn_img(Image img){
		this.img=img;
		repaint();
	}
	public void setPreferredSize() { 
		ImageIcon tmp=new ImageIcon(img);
		setPreferredSize(new Dimension(tmp.getIconWidth(),tmp.getIconHeight()));
	}
	@Override public void paintComponent(Graphics g){ 
        //super.paintComponent(g);
        Graphics2D g2=(Graphics2D) g;
        Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,al); //指定透明度为半透明90%
		g2.setComposite(alpha);

		g2.setFont(new Font("Courier", Font.BOLD, fbig)); //设置字型

		FontMetrics fm=g2.getFontMetrics(); //取得FontMetrics类实体
		int AppletWidth,AppletHeight,Ascent, Descent, Width,X,Y;
		AppletWidth = getSize().width; //取得显示区域的宽度
		AppletHeight = getSize().height; //取得显示区域的高度
		Ascent=fm.getAscent(); //取得Ascent
		Descent=fm.getDescent(); //取得Descent
		Width=fm.stringWidth(text); //取得字符串宽度
		X = (AppletWidth - Width) / 2;
		Y = (AppletHeight - (Ascent + Descent)) / 2 + Ascent;
		if(!if_gray)g2.drawImage(img,null,null);
		else g2.drawImage(img_b,null,0,0);
		g2.setColor(Color.WHITE);
		g2.drawString(text, X, Y); //绘制字符串阴影
		g2.setColor(new Color(146888));
		g2.drawString(text, X-(fbig>30?2:1), Y-(fbig>30?2:1)); //绘制字符串前景
        //g2.setFont(new Font("SansSerif",Font.ITALIC,28));
        //g2.drawString("text",50,30);
    }
    @Override public boolean contains(int x, int y){
        return shape.contains(x,y);
    } 
    public Shape getImageShape(){
	    ArrayList<Integer> x=new ArrayList<Integer>();
	    ArrayList<Integer> y=new ArrayList<Integer>();   
	    ImageIcon icon = new ImageIcon(img);
	    int width=icon.getIconWidth();//图像宽度
	    int height=icon.getIconHeight();//图像高度
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
	    for(int i=0;i<height;i++)
	    	for(int j=0;j<width;j++){
	    		matrix[i][j]=0;
	    	}
	    //导入坐标ArrayList中的不透明坐标信息
	    for(int c=0;c<x.size();c++)matrix[y.get(c)][x.get(c)]=1;
	    /* 由于Area类所表示区域可以进行合并，我们逐一水平"扫描"图像矩阵的每一行，
	     * 将不透明的像素生成为Rectangle，再将每一行的Rectangle通过Area类的rec
	     * 对象进行合并，最后形成一个完整的Shape图形
	     */
	    Area rec=new Area();
	    int temp=0;
	    for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
	        	if(matrix[i][j]==1){
	        		if(temp==0)temp=j;  
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
	public void setAl(float x){
		al=x;
	}
	@Override public void setText(String str){
		text=str;
		repaint();
	}
	public void set_img_gray(){
		ImageIcon tmp=new ImageIcon(img);
		img_b=new BufferedImage(tmp.getIconWidth(),tmp.getIconHeight(),java.awt.image.BufferedImage.TYPE_BYTE_GRAY);
		img_b.getGraphics().drawImage(img,0,0,null);
		if_gray=true;
	}
}