package DRAKO.table;

import DRAKO.unit.*;
import DRAKO.talk.*;
import DRAKO.Listener.*;
import DRAKO.Data.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class GameTable extends JPanel 
{
	private static final long serialVersionUID=1L;
	public JButton _set=new JButton("Set");
	Map map;
	Status status;
	CardTable handcard;
	GameListener game;
	ChatRoom chatroom;
	JButton draw,nets;
	Judge judge;
	Show show;
	boolean if_dr;

	ActionListener listener=new ActionListener(){
	    @Override
	    public void actionPerformed(ActionEvent e) {
	        if(e.getSource()==draw){
	        	game.addStack(30);
	        }
	        if(e.getSource()==nets){
	        	game.addStack(judge.if_DR()? 28:29);
	        }
	        game.finish();
		}
	};
	public GameTable(CardTable handcard,Status status,Map map,GameListener game,Judge judge,Show show,ChatRoom chatroom){
		this.handcard=handcard;
		this.status=status;
		this.map=map;
		this.game=game;
		this.judge=judge;
		this.show=show;
		this.chatroom=chatroom;

		setLayout(null);
		setPreferredSize(new Dimension(800,600));
		_set.setBounds(0,0,800,20);
		add(_set);
		map.setBounds(0,20,600,400);
		add(map);
		status.setBounds(600,20,200,400);
		add(status);
		handcard.setBounds(0,420,600,150);
		add(handcard);
		chatroom.setBounds(600,420,200,150);
		add(chatroom);
		draw=new JButton("抽牌");
		draw.setBounds(530,330,70,70);
		draw.addActionListener(listener);
		map.add(draw);
		nets=new JButton("網");
		nets.setBounds(530,260,70,70);
		nets.addActionListener(listener);
		map.add(nets);
		nets.setVisible(false);
	}
	public void setGame(boolean if_dr){
		handcard.reset(if_dr);
		this.if_dr=if_dr;
		if(if_dr)nets.setText("解網");
		else{
			nets.setText("狂爆");
			nets.setVisible(true);
		}
	}
	public void do_it(int[] st,int step){
		if(st[1]==1||st[1]==2)game.ask_rival_defense(st[2],st[1]);//st[1]==血量
		else if(st[1]==3){
			if(judge.get_two_attack()==1){
				game.ask_rival_defense(st[2],1);
				if(map.canAttack()>1){
					game.set_round(1);
					game.addStack(0);
					game.addStack(3);
					game.addStack(0);
					game.addStack(0);
					game.finish();
				}
			}else game.ask_rival_defense(st[4],1);
		}
		else if(st[1]==4)game.ask_rival_defense(st[2],1);
		else if(st[1]==5||st[1]==6||st[1]==7)game.ask_rival_defense(st[2],st[1]-4);
		else if(st[1]==8||st[1]==9){
			boolean fi[]=map.get_fire(st[2],st[3]);
			int hurt=st[1]-7;
			for(int i=0;i<3;i++){
				if(fi[i])game.ask_rival_defense(i+1,hurt);
			}
		}
		else if(st[1]==10||st[1]==11)map.move(st[2],st[3],st[4]);
		else if(st[1]==13){
			map.move(st[2],st[3],st[4]);
			if(st[5]!=7122)map.move(st[5],st[6],st[7]);
		}
		else if(st[1]==14){
			judge.set_nets(2);
			nets.setVisible(true);
		}
		else if(st[1]==15){
			if(judge.set_nets(-1)==0)if(if_dr)nets.setVisible(false);
		}
		else if(st[1]==16)nets.setVisible(false);
		else if(st[1]==17)map.move(0,st[2],st[3]);
		else if(st[1]==18)game.ask_self_defense();
		else if(st[1]==21){
			map.move(1,st[2],st[3]);
			map.move(2,st[4],st[5]);
			map.move(3,st[6],st[7]);
			map.repaint();
			if(!if_dr){
				show.show_word("start");
				game.wait_for_show();
			}
		}
		else if(st[1]==22){
			handcard.draw(false);
			if(!handcard.draw(true)&&if_dr)draw.setText("跳過");
		}else if(st[1]==25){
			boolean if_win=judge.if_DR()^st[2]==1;
			show.end(if_win);
			game.wait_for_show();
			game.stopAll();
		}
		status.repaint();
	}
	public void _setEnabled(boolean x){
		if(!x){
			draw.setEnabled(x);
			nets.setEnabled(x);
		}else{
			draw.setEnabled(x);
			nets.setEnabled(x);
		}
	}
}