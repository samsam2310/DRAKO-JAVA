package DRAKO;

import DRAKO.table.*;
import DRAKO.unit.*;
import DRAKO.Listener.*;
import DRAKO.talk.*;
import DRAKO.img.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class MainFrame extends JFrame implements StartGameListener
{
	private static final long serialVersionUID=1L;
	FlowLayout f=new FlowLayout(0,0,0);
	public CardLayout g=new CardLayout(0,0);
	JPanel table=new JPanel(g);
	MainMenu _mmu;
	Room _rm;
	Link _lk;
	Game _gm;
	ImgLoader imgloader;

	ActionListener mainl=new ActionListener(){
	    @Override
	    public void actionPerformed(ActionEvent e) {
			if(e.getSource()==_mmu.room){
				_rm.reset();
				g.show(table,"IR");
			}
			if(e.getSource()==_mmu.room){
				_rm.reset();
				g.show(table,"IR");
			}
			if(e.getSource()==_mmu.link){
				_lk.reset();
				g.show(table,"IL");
			}
			if(e.getSource()==_mmu.set){
				;
			}
			if(e.getSource()==_mmu.exit){
				System.exit(3);
			}
			if(e.getSource()==_rm.cancel){
				try{
					_rm.stopAll();
				}catch(IOException f){}
				g.show(table,"MM");
			}
			if(e.getSource()==_lk.cancel){
				_lk.stopAll();
				g.show(table,"MM");
			}
		}
	};
	public void BuildGame(StreamData _talk,StreamData _chat){
		_gm.addStream(_talk,_chat);
		g.show(table,"G");
	    _gm.start();
	}
	public void end_game(){
		g.show(table,"MM");
	}
	public MainFrame(){
		setBounds(100,100,810,620);
		setVisible(true);
		imgloader=new ImgLoader();
		_mmu=new MainMenu(imgloader);
		_rm=new Room(this,imgloader);
		_lk=new Link(this,imgloader);
		_gm=new Game(imgloader,this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(f);

		table.setPreferredSize(new Dimension(800,600));
		table.setLayout(g);
		
		add(table);
		_mmu.addMainL(mainl);
		_rm.addMainL(mainl);
		_lk.addMainL(mainl);

		table.add(_mmu,"MM");
		table.add(_rm,"IR");
		table.add(_lk,"IL");
		table.add(_gm,"G");
	}
}
