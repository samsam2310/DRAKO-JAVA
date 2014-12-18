package DRAKO.table;

import DRAKO.unit.*;
import DRAKO.Listener.*;
import DRAKO.talk.*;
import DRAKO.img.*;
import DRAKO.Data.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JLayeredPane; 
import java.util.*;
import java.lang.reflect.*;

public class Game extends JLayeredPane implements GameListener,Runnable
{
	private static final long serialVersionUID=1L;
	CardLayout g=new CardLayout(0,0);
	StartGameListener startgamelistener;
	GameTable _gm;
	SetupMenu _mu;
	Show show;
	CardTable handcard;
	Status status;
	Map map;
	MenuListener menulistener;
	StreamData _talk;
	ImgLoader imgloader;
	Text text;
	Judge judge;
	ChatRoom chatroom;
	String rname,rip,name;
	int[] st=new int[10];
	int step,round,hand_id;
	boolean if_dr,is_your_turn,if_begin=true;
	Thread waiting_rival;

	public Game(ImgLoader imgloader,StartGameListener startgamelistener){
		setBounds(0,0,800,600);
		this.imgloader=imgloader;
		this.startgamelistener=startgamelistener;
		text=new Text();
		show=new Show(imgloader,text,this);
		map=new Map(imgloader,this);
		handcard=new CardTable(imgloader,this);
		status=new Status(map,this,imgloader,handcard);
		judge=new Judge(status,map,handcard,this);
		_mu=new SetupMenu(imgloader,this,text,judge,handcard);
		chatroom=new ChatRoom(imgloader);
		_gm=new GameTable(handcard,status,map,this,judge,show,chatroom);
		menulistener=_mu;

		_gm.setBounds(0,0,800,600);
		_mu.setBounds(0,0,800,600);
		add(_mu,MODAL_LAYER);
		add(show,4);
		add(_gm,DEFAULT_LAYER);

		_mu.setVisible(false);
		map.addJudge(judge);
	}
	public void start(){
		round=1;
		st[0]=0;
		st[1]=19;
		step=2;
		_mu.ask_menu(st,step);
	}
	public void start(int mora){
		mora-=8;
		_talk.write(new GameData(new int[]{mora},1));
		int mora2=_talk.read().get_st()[0];
		if((mora-mora2+3)%3==1){
			st[0]=0;
			st[1]=12;
			step=2;
			_mu.ask_menu(st,step);
		}
		else if((mora-mora2+3)%3==2){  //LOSE
			int tag=_talk.read().get_st()[0];
			if_dr=tag==0;
			resetGame();
		}else{
			start();
		}
	}
	public void start_win(int tag){ //WIN
		tag-=12;
		_talk.write(new GameData(new int[]{(tag+1)%2},1));
		if_dr=tag==0;
		resetGame();
	}
	public void set_hand_id(int x){
		if(x!=0)hand_id=x;
	}
	public void showMenu(boolean x){
		_gm._setEnabled(false);
		_mu.setVisible(x);
		show.black(x);
	}
	public void addStack(int id){
		st[step]=id;
		step+=1;
		String tmp=new String();
		for(int i=0;i<step;i++)tmp=tmp+st[i]+" ";
		_gm._set.setText(tmp);
	}
	public void finish(){
		//try{
			new Thread(new Runnable(){  
				public void run(){
					System.out.println("  finish "+step);
					if(step==0)return;
					int tag=judge.next_step(st,step);
					if(tag==0)do_it(0);
					else if(tag==1)map.ask_where(st,step);
					else if(tag==2)_mu.ask_menu(st,step);
					else if(tag==3)start(st[2]);
					else if(tag==4)start_win(st[2]);
					else if(tag==5)tell_self_defense();
					else if(tag==6)do_it(1);//不對襯動作
					else if(tag==20){
						hand_id=0;
						step=0;
						do_it(1);
					}
				}
			}).start();
	    //}//catch(InterruptedException e){
		//}catch(InvocationTargetException e){}
	}
	public void addStream(StreamData _talk,StreamData _chat){
		this._talk=_talk;
		_gm.chatroom.addStream(_chat);
	}
	void do_it(int tell){
		/*0=same 1=not 2=get rival 3=not rival*/
		System.out.println("doit "+st[0]+" "+st[1]+" "+st[2]+" "+st[3]+" "+st[4]+" "+step);
		if(step==0)handcard._setEnabled(true);
		if(step>0){
			if(tell==0){
				_talk.write(new GameData(st,step));
			}
			if(tell<=1){
				set_round(-judge.get_round(st[1]));
			}
			if(tell==1)_talk.write(new GameData(st,1));
			if(st[1]!=18)step=0;//擋還要用+不會改道後面設定的step
	        show.show_card(st[0]);
	        while(show.get_lock()){try{java.lang.Thread.sleep(100);}catch(InterruptedException e){}}
			handcard.removeCard(hand_id);
			hand_id=0;
			if(tell<=2)_gm.do_it(st,step);
		}
		_gm._setEnabled(true);
		if(judge.if_end()!=0)end_the_game();
		if(round==0)wait_for_rival();
	}
	void resetGame(){
		_gm.setGame(if_dr);
		judge.set(if_dr);
		chatroom.set(if_dr);
		if_begin=true;
		if(if_dr){
			step=0;
			is_your_turn=true;
			_mu.set_is_your_turn(false);
			round=0;
			wait_for_rival();
		}else{
			round=1;
			//map.ask_where(st,step);
			is_your_turn=true;
			_talk.read();//吃掉對面的結束符號
			_gm._setEnabled(false);
			handcard._setEnabled(false);
			st[0]=0;
			st[1]=21;
			step=2;
			System.out.println("212121!!!");
			finish();
		}
	}
	void wait_for_rival(){
		if(is_your_turn||if_begin){
			waiting_rival=new Thread(this);
			waiting_rival.start();
		}
	}
	boolean get_rival_act(){
		System.out.println("GET");
		GameData tmp=_talk.read();
		showMenu(false);
		st=tmp.get_st().clone();
		step=tmp.get_step();
		System.out.println("ACT "+st[1]+" "+st[2]+" "+st[3]+" "+st[4]+" "+step);
		boolean if_turn=step==0;
		if(step>1)do_it(2);
		else do_it(3);
		System.out.println("END GET "+if_turn);
		return if_turn;
	}
	public void run(){//for Thread
		System.out.println("IS WAIT");
    	boolean tag;
    	_mu.set_is_your_turn(false);
    	is_your_turn=false;
    	_talk.write(new GameData(st,0));
    	if(if_begin){
    		if_begin=false;
    		if(if_dr)set_round(-1);
		}else{
			show.show_word("end");
			wait_for_show();
		}
    	while(true){
			tag=get_rival_act();
			if(tag)break;
		}
		set_round(2);
		is_your_turn=true;
		_mu.set_is_your_turn(true);
		System.out.println("END WAIT");
		show.show_word("start");
		wait_for_show();
    }
    public void ask_rival_defense(int id,int x){//id人x血
		System.out.println("ask rival defense "+id+" "+x);
		st[0]=0;st[1]=18;st[2]=id;st[3]=x;
		_talk.write(new GameData(st,4));
		if(_talk.read().get_st()[4]==20)status.attack(id,x);
		step=0;
		end_the_game();
	}
	public void ask_self_defense(){
		System.out.println("ask defense");
		_mu.ask_menu(st,step);
	}
	public void tell_self_defense(){
		System.out.println("tell defense "+st[2]+" "+st[3]+" "+st[4]);
		handcard.removeCard(hand_id);
		hand_id=0;
		_talk.write(new GameData(st,step));
		step=0;
		if(st[4]==20)status.attack(st[2],st[3]);
		end_the_game();
	}
	public int set_round(int x){
		return round+=x;
	}
	void end_the_game(){
		int end=judge.if_end();
		if(end==1||end==2){
			st[0]=0;
			st[1]=25;
			st[2]=end;
			step=3;
			finish();
		}
	}
	public void wait_for_show(){
		while(show.get_lock())try{java.lang.Thread.sleep(10);}catch(InterruptedException e){}
	}
	public void clean(){
		step=0;
	}
	public void stopAll(){
		_talk.close();
		chatroom.close();
		startgamelistener.end_game();
	}
}