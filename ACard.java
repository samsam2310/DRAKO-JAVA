package DRAKO.unit;

import DRAKO.img.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class ACard extends ImgJButton
{
	private static final long serialVersionUID=1L;
	int go,size=0,id,hand_id;
	boolean _tag=false;
	ACard _next;
	public ACard(int id,ActionListener cardl,ImgLoader imgloader){
		super(imgloader.get(id));
		this.id=id;
		setBounds(0,0,100,150);
		addActionListener(cardl);
		_next=null;
	}
	public void moveto(int x,int mv){
		go=x;
		_tag=true;
		Thread move= new Thread(new Runnable(){
			public void run(){
				int now=getLocation().x,mv=(now<go)? 1:-1;
				while(now!=go){
					now+=mv;
					setLocation(now,getLocation().y);
					try{
						java.lang.Thread.sleep(1);
					}catch(InterruptedException e){}
				}
				_tag=false;
			}
		});
		if(haveNext())next().moveto(x+mv,mv);
		move.start();
	}
	public int lenght(){
		return size;
	}
	public ACard next(){
		return _next;
	}
	public boolean haveNext(){
		return _next!=null;
	}
	public boolean getTag(){
		if(haveNext())return _tag||_next.getTag();
		return _tag;
	}
	public JButton add(int id,ActionListener cardl,ImgLoader imgloader){
		size+=1;
		if(_next!=null){
			return _next.add(id,cardl,imgloader);
		}
		_next=new ACard(id,cardl,imgloader);
		System.out.println("Success adding ");
		return _next;
	}
	public ACard _remove(int rv){
		size-=1;
		if(rv!=_next.get_hand_id())return _next._remove(rv);
		_next.go=-150;
		_next._tag=true;
		ACard rm=_next;
		Thread move= new Thread(new Runnable(){
			public void run(){
				int now=_next.getLocation().y,mv=-1;
				while(now!=_next.go){
					now+=mv;
					_next.setLocation(_next.getLocation().x,now);
					try{
						java.lang.Thread.sleep(1);
					}catch(InterruptedException e){}
				}
				_next._tag=false;
				_next=_next._next;
			}
		});
		move.start();
		return rm;
	}
	public int get_id(){
		return id;
	}
	public int get_hand_id(){
		return hand_id;
	}
	public void _setEnabled(boolean x){
		if(haveNext())_next._setEnabled(x);
		setEnabled(x);
	}
	public int[][] get_handcard(int[][] stack,int p,int tag){
		if(p==-1)return haveNext()? _next.get_handcard(stack,p+1,tag):stack;
		if(p>=0)stack[p]=new int[]{id,hand_id};
		if(tag==0&&p>=0&&!(id==6||id==7||id==9||id==10||id==16||id==26||id==27))stack[p][1]=0;
		if(haveNext())return _next.get_handcard(stack,p+1,tag);
		return stack;
	}
	public void set_hand_id(int h_id){
		hand_id=h_id;
		if(haveNext())_next.set_hand_id(h_id+1);
	}
}