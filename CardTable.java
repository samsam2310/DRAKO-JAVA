package DRAKO.table;

import DRAKO.unit.*;
import DRAKO.Data.*;
import DRAKO.img.*;
import DRAKO.Listener.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class CardTable extends ImgJPanel implements CardData
{
	private static final long serialVersionUID=1L;
	int[] deck;
	int p,rm_card_id;
	ACard head;
	ImgLoader imgloader;
	GameListener game;
	ActionListener cdl;
	boolean if_dr;
	public CardTable(ImgLoader imgloader,GameListener game){
		super(imgloader.get_bg(8).getScaledInstance(600,150,5));
		this.imgloader=imgloader;
		setPreferredSize(new Dimension(600,150));
		setLayout(null);
		this.game=game;
		cdl=new ActionListener(){
			@Override public void actionPerformed(ActionEvent e){
				tell_Game(e);
			}
		};
	}
	void tell_Game(ActionEvent e){
		_setEnabled(false);
		game.clean();
		game.set_hand_id(((ACard)e.getSource()).get_hand_id());
		Ask_Card(((ACard)e.getSource()).get_id());
	}
	void Ask_Card(int id){
		game.addStack(id);
		game.finish();
	}
	public void reset(boolean if_dr){
		this.if_dr=if_dr;
		head=new ACard(-1,cdl,imgloader);
		mackDack(if_dr);
		for(int i=0;i<4;i++){
			addCard(deck[p]);
			p+=1;
		}
		rearrange();
	}
	public void mackDack(boolean if_dr){
		if(if_dr)deck=DR;
		else deck=HU;
		ArrayList<Nub> tmp=new ArrayList<Nub>();
		for(int j=0;j<5;j++){
			int tag=(int)(Math.random()*37);
			for(int i=0;i<38;i++)tmp.add(new Nub(deck[(i+tag)%38]));
			deck=new int[38];
			for(int i=0;i<38;i++)deck[i]=tmp.remove((int)(Math.random()*10000)%(38-i)).get();
		}
		p=0;
	}
	public boolean draw(boolean ask){
		if(ask)return if_dr? p<38:p<36;
		int i=p+2;
		for(;p<i;p++)addCard(deck[p]);
		if(head.lenght()>6)game.set_round(1);
		rearrange();
		return true;
	}
	public void addCard(int x){
		System.out.println(head.lenght());
		add(head.add(x,cdl,imgloader));
	}
	public void rearrange(){
		head.set_hand_id(0);
		Thread doit= new Thread(new Runnable(){
	        public void run(){
	        	wait_for_head();
	        	if(head.haveNext())head.next().moveto(0,0);
				wait_for_head();
				try{
                	java.lang.Thread.sleep(300);
	            }catch(InterruptedException e){}
				System.out.println("!!!!!!!");
				int mv=(head.lenght()>6)? (600/head.lenght()):100;
				if(head.haveNext())head.next().moveto(0,mv);
				wait_for_head();
				_setEnabled(true);//讓卡可以按
				if(head.lenght()>6){
					too_many_card();
				}
	        }
	    });
		doit.start();
	}
	void wait_for_head(){
		while(head.getTag()){
			try{
            	java.lang.Thread.sleep(10);
            }catch(InterruptedException e){}
		}
	}
	void too_many_card(){
		game.addStack(0);
		game.addStack(23);
		game.finish();
	}
	public void removeCard(int x){
		if(x==0){
			_setEnabled(true);//讓卡可以按
			return;
		}
		if(head.lenght()>7)game.set_round(1);
    	rm_card_id=x;
    	Thread doit= new Thread(new Runnable(){
	        public void run(){
		    	wait_for_head();
		    	ACard rm=head._remove(rm_card_id);
				wait_for_head();
				remove(rm);
				try{
		        	java.lang.Thread.sleep(300);
		        }catch(InterruptedException e){}
				System.out.println("???????");
				rearrange();
			}
		});
		doit.start();
	}
	public void _setEnabled(boolean x){
		head._setEnabled(x);
	}
	public int[][] get_handcard(int tag){
		wait_for_head();
		int[][] hand_deck=new int[8][2];
		return head.get_handcard(hand_deck,-1,tag);
	}
	public int lenght(){
		return head.lenght();
	}
	public int get_p(){
		return p;
	}
}


class Nub{
	int x;
	Nub(int x){
		this.x=x;
	}
	public int get(){
		return x;
	}

	/*
	11
10
2
4
7
7
11
7
7
10
1
13
8
16
4
2
9
12
7
2
4
3
3
9
3
15
4
9
10
10
3
3
5
14
9
5
6
6

3
3
11
7
10
7
10
2
6
7
13
11
15
5
7
4
9
3
4
16
9
2
4
10
9
1
5
9
3
4
10
3
2
7
6
12
8
14
*/

}