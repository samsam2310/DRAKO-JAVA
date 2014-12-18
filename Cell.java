package DRAKO.table;

import DRAKO.unit.*;
import DRAKO.img.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Cell extends ImgJButton
{
	private static final long serialVersionUID=1L;
	int x,y,sb=4,firej;
	float al=1.0f;
	boolean have_thing;
	Image[] chess_img;
	Image img_uh,img_h,img2;
	Map map;
	public Cell(int x,int y,int x2,int y2,boolean th,ImgLoader imgloader,ActionListener listener,Map map){
		super(imgloader.get_btn(4));
		img2=img_h=img_uh=imgloader.get_btn(4);
		System.out.println(x+" "+y+" TTGGF");
		setBounds(x,y,101,86);
		this.x=x2;
		this.y=y2;
		have_thing=th;
		this.map=map;
		setVisible(!have_thing);
		setBorderPainted(false);
		setEnabled(false);
		al=0.0f;
		addActionListener(listener);
		
		chess_img=new Image[5];
		chess_img[0]=imgloader.get_btn(0);
		chess_img[1]=imgloader.get_btn(1);
		chess_img[2]=imgloader.get_btn(2);
		chess_img[3]=imgloader.get_btn(3);
		chess_img[4]=imgloader.get_btn(4);

		System.out.println(" FF  "+x+" "+y);

		addMouseListener(new MouseAdapter(){ 
	    	public void mouseEntered(MouseEvent e){ 
	    		if(isEnabled()){
	    			if(sb==4)al=0.9f;
	    			else al=1.0f;
	    			img2=img_h;
	    			repaint();
	    		}else{
	    			if(sb==4)al=0.0f;
	    			else al=1.0f;
	    			img2=img_uh;
	    		}
	    	}
	    	public void mouseExited(MouseEvent e){
	    		img2=img_uh;
	    		if(isEnabled()){
	    			if(sb!=4)al=0.8f;
	    			else al=0.5f;
	    			repaint();
	    		}else{
	    			if(sb!=4)al=1.0f;
	    			else al=0.0f;
	    		}
	   		}
	   	});
	}
	
	public void setSB(int sb){
		this.sb=sb;
		if(sb==4)al=0.0f;
		else al=1.0f;
		off();
		repaint();
	}
	public boolean can(){
		if(have_thing||sb!=4)return false;
		return true;
	}
	public boolean on(){
		//System.out.println(x2+" "+y2);
		if(have_thing||sb!=4)return false;
		setEnabled(true);
		al=0.5f;
		repaint();
		return true;
	}
	public void th_on(int j){
		if(sb!=4){
			setEnabled(true);
			al=0.5f;
			repaint();
			firej=j;
		}
	}
	public void on_HU(int id){
		if(this.sb!=4)return;
		boolean tmp=x==0&&(y==0||y==1||y==2)||x==1&&(y==0||y==3)||x==2&&(y==0||y==4)||x==3&&(y==1||y==4)||x==4&&(y==2||y==3||y==4);
		if(tmp){
			img_uh=chess_img[4];
			img_h=chess_img[id];
			img2=chess_img[4];
			al=0.5f;
			repaint();
			setEnabled(true);
		}
	}
	public boolean off(){
		setEnabled(false);
		img_uh=chess_img[sb];
		img_h=chess_img[sb];
		img2=chess_img[sb];
		if(sb==4)al=0.0f;
		repaint();
		return true;
	}
	@Override public void paintComponent(Graphics g){
        Graphics2D g2=(Graphics2D) g;
		Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,al);
		g2.setComposite(alpha);
		g2.drawImage(img2,null,null);
    }
    public int get_x(){
    	return x;
    }
    public int get_y(){
    	return y;
    }
    public int getSB(){
    	return sb;
    }
    public int get_firej(){
    	return firej;
    }
}