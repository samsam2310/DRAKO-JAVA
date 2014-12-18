package DRAKO.table;

import DRAKO.unit.*;
import DRAKO.Data.*;
import DRAKO.Listener.*;
import DRAKO.img.*;
import java.lang.reflect.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Show extends JPanel
{
	private static final long serialVersionUID=1L;
	ImgLoader imgloader;
	JLayeredPane game;
	Text text;
	String str;
	boolean lock;
	JLabel t;
	ImgJPanel black;
	int id=0;
	/*1=card 2=string*/
	
	public Show(ImgLoader imgloader,Text text,JLayeredPane game){
		this.imgloader=imgloader;
		this.text=text;
		this.game=game;
		
		black=new ImgJPanel(imgloader.get_bg(1));
		black.setBounds(0,0,800,600);
		black.setAl(0.9f);
		black.setVisible(false);
		add(black);
		setLayout(null);
		setBounds(0,0,800,600);
		setOpaque(false);
	}
	public void black(boolean x){
		black.setVisible(x);
	}
	public void show_card(int idx){
		if(idx==0||idx>=27)return;
		lock=true;
		this.id=idx;
		ImageIcon img=new ImageIcon(imgloader.get(id));
		t=new JLabel();
		t.setIcon(img);
		t.setBounds(300,150,100,150);
		new Thread(new Runnable(){  
			public void run(){
				try{java.lang.Thread.sleep(100);}catch(InterruptedException e){System.out.println("sleep!!");}
				add(t);
				for(int i=1;i<105;i+=5){
					t.setIcon(new ImageIcon(imgloader.get(id).getScaledInstance(i,150,5)));
					t.setBounds(300-i/2,150,i,150);
	        		repaint();
					try{java.lang.Thread.sleep(10);}catch(InterruptedException e){System.out.println("sleep!!");}
				}
				try{java.lang.Thread.sleep(1000);}catch(InterruptedException e){System.out.println("sleep!!");}
				for(int i=105;i>=1;i-=5){
					t.setIcon(new ImageIcon(imgloader.get(id).getScaledInstance(i,150,5)));
					t.setBounds(300-i/2,150,i,150);
	        		repaint();
					try{java.lang.Thread.sleep(10);}catch(InterruptedException e){System.out.println("sleep!!");}
				}
				remove(t);
				repaint();
				try{java.lang.Thread.sleep(1000);}catch(InterruptedException e){System.out.println("sleep!!");}
				lock=false;
			}
		}).start();
	}
	public void show_word(String x){
		lock=true;
		str=text.get_text("STR_"+x);
		t=new JLabel(str);
		Font f=new Font("Courier", Font.BOLD, 96);
		t.setFont(f);
		t.setForeground(Color.YELLOW);
		t.setBounds(100,150,500,100);
		new Thread(new Runnable(){  
			public void run(){
				try{java.lang.Thread.sleep(100);}catch(InterruptedException e){System.out.println("sleep!!");}
				add(t);
				for(int i=0;i<450-t.getWidth()/2;i+=10){
					t.setLocation(i,150);
	        		repaint();
					try{java.lang.Thread.sleep(10);}catch(InterruptedException e){System.out.println("sleep!!");}
				}
				try{java.lang.Thread.sleep(1000);}catch(InterruptedException e){System.out.println("sleep!!");}
				for(int i=450-t.getWidth()/2;i<800-t.getWidth()/2;i+=10){
					t.setLocation(i,150);
	        		repaint();
					try{java.lang.Thread.sleep(10);}catch(InterruptedException e){System.out.println("sleep!!");}
				}
				remove(t);
				repaint();
				lock=false;
			}
		}).start();
	}
	public void show_act(int st[],int step){
		;
	}
	public void end(boolean x){
		if(x){
			//W
			show_word("win!!");
		}else{
			//L
			show_word("Lose!!");
		}
	}
    public void dood(){
    	;
    }
    public boolean get_lock(){
    	return lock;
    }
}