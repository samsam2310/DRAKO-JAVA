package DRAKO.table;

import DRAKO.unit.*;
import DRAKO.Data.*;
import DRAKO.Listener.*;
import DRAKO.img.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

 public class SetupMenu extends ImgJPanel implements CardData,MenuListener,ExplanationListener
{
	private static final long serialVersionUID=1L;
	ASelect head;
	ImgLoader imgloader;
	Text text;
	JPanel table;
	ImgText explanation;
	GameListener game;
	Judge judge;
	CardTable handcard;
	int[] st;
	int step;
	boolean is_your_turn;

	ActionListener listener=new ActionListener(){
	    @Override
	    public void actionPerformed(ActionEvent e) {
			game.set_hand_id(((ASelect)e.getSource()).get_hand_id());
			tell_Game(((ASelect)e.getSource()).get_id());
		}
	};
	public SetupMenu(ImgLoader imgloader,GameListener game,Text text,Judge judge,CardTable handcard){
		super(imgloader.get_bg(1));
		this.imgloader=imgloader;
		this.game=game;
		this.text=text;
		this.judge=judge;
		this.handcard=handcard;
		setAl(.0f);
		setLayout(null);
		//setPreferredSize(new Dimension(800,600));
		setBounds(0,0,800,600);
		head=new ASelect(1,1,listener,imgloader,this,false,0);
		
		table=new JPanel();
		table.setLayout(null);
		table.setBounds(100,100,600,150);
		add(table);
		explanation=new ImgText(imgloader.get_bg(9).getScaledInstance(500,200,5));
		explanation.setBounds(200,300,500,200);
		add(explanation);
	}
	public void ask_menu(int[] st,int step){
		removeButton();
		this.st=st;
		this.step=step;
		if(step==1)Ask_Card(st[0]);
		else if(step>0&&st[1]==18)Ask_handcard(0);
		else if(step>0&&st[1]==23)Ask_handcard(1);
		else if(step>1)Ask_Act(st[1]);
	}
	public void Ask_Card(int id){
		for(int i=1;i<=CARD[id-1][0];i++){
			int tag=judge.act_id(CARD[id-1][i]);
			if(!is_your_turn&&tag!=0&&CARD[id-1][i]!=20)tag=1;
			if(tag!=0)table.add(head.add(CARD[id-1][i],-100,listener,imgloader,tag==2,0,0));
		}
		game.showMenu(true);
	}
	public void Ask_Act(int id){
		for(int i=1;i<=STEP_1[id-1][0];i++){
			int tag=judge.step_1(id,STEP_1[id-1][i]);
			if(tag!=0)table.add(head.add(STEP_1[id-1][i],-100,listener,imgloader,tag==2,1,0));
		}
		game.showMenu(true);
	}
	public void Ask_handcard(int tag){
		int[][] tmp=handcard.get_handcard(tag);
		for(int i=0;i<handcard.lenght();i++){
			if(tag==0&&tmp[i][1]!=0)table.add(head.add(tmp[i][0],-100,listener,imgloader,true,2,tmp[i][1]));
			if(tag==1)table.add(head.add(tmp[i][0],-100,listener,imgloader,true,2,tmp[i][1]));
		}
		if(tag==0)table.add(head.add(20,-100,listener,imgloader,true,0,0));
		game.showMenu(true);
	}
	void tell_Game(int id2){
		removeButton();
		game.addStack(id2);
		game.showMenu(false);
		game.finish();
	}
	public void setText(int id){
		if(step==1){
			explanation.setText(text.get_text("ACT_"+id));
		}
		if(step>1){
			explanation.setText(text.get_text("TH_"+id));
		}
	}
	public void set_is_your_turn(boolean x){
		this.is_your_turn=x;
	}
	void removeButton(){
		table.removeAll();
		//table.repaint();
		head.remove();
	}
}