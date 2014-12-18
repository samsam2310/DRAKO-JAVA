package DRAKO.unit;

import DRAKO.img.*;
import DRAKO.Listener.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class ASelect extends ImgJButton
{
	private static final long serialVersionUID=1L;
	int go,size=0,id,hand_id;
	ExplanationListener explanation;
	ASelect _next=null;

	public ASelect(int id,int x,ActionListener listener,ImgLoader imgloader,ExplanationListener explanation,boolean tag,int if_th){
		super(if_th==1? imgloader.get_th(id):(if_th==0? imgloader.get_act(id):imgloader.get(id)));
		this.id=id;
		this.explanation=explanation;
		setBounds(x,0,100,150);
		addActionListener(listener);
		setEnabled(tag);
		if(!tag)set_img_gray();

		_next=null;

		addMouseListener(new MouseAdapter(){ 
	    	public void mousePressed(MouseEvent e){
	    		//System.out.println("Good!");
	    	}
	    	public void mouseEntered(MouseEvent e){ 
	    		setText();
	    	}
	    	public void mouseExited(MouseEvent e){
	    		;//poly.setBackground(Color.blue);
	   		}
	   	});
	}
	public int lenght(){
		return size;
	}
	public ASelect next(){
		return _next;
	}
	public boolean haveNext(){
		return _next!=null;
	}
	public JButton add(int id,int x,ActionListener listener,ImgLoader imgloader,boolean tag,int if_th,int hand_id){
		size+=1;
		if(_next!=null){
			return _next.add(id,x+100,listener,imgloader,tag,if_th,hand_id);
		}
		_next=new ASelect(id,x+100,listener,imgloader,explanation,tag,if_th);
		_next.set_hand_id(hand_id);
		return _next;
	}//add card
	public void remove(){
		if(haveNext())_next.remove();
		_next=null;
	}
	public int get_id(){
		return id;
	}
	public void setText(){
		explanation.setText(id);
	}
	public void set_hand_id(int x){
		hand_id=x;
	}
	public int get_hand_id(){
		return hand_id;
	}
}