package DRAKO.table;

import DRAKO.unit.*;
import DRAKO.Listener.*;
import DRAKO.talk.*;
import DRAKO.img.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class Room extends ImgJPanel implements ActionListener
{
	private static final long serialVersionUID=1L;
	static String DEFAULT_IP="230.0.0.1";
	static int DEFAULT_PORT=6000;
	int PORT_1=6001,PORT_2=6002;
	String roomname,player1,player2;

	JLabel la;
	JLabel lb;
	JLabel lc;
	JTextField ta,tb;
	JButton submit;
	public JButton cancel;
	public JButton start;
	ActionListener mainl;
	DocumentListener textl;
	StartGameListener startgame;
	Thread sever=null,sever2=null;
	Boolean tag=false;
	MulticastSocket socket=null;
	Socket rival_1=null,rival_2=null;
	ServerSocket waitl;
	StreamData _talk,_chat;
	ImgLoader imgloader;

	public Room(StartGameListener startgame,ImgLoader imgloader){
		super(imgloader.get_bg(4));
		this.startgame=startgame;
		this.imgloader=imgloader;
		reset();
	}
	public void reset(){
		la=new JLabel("房間名稱：");
		lb=new JLabel("暱稱：");
		ta=new JTextField("newGame");
		tb=new JTextField("PlayerR");
		submit=new ImgJButton("submit",imgloader.get_btn(100,30),24);
		start=new ImgJButton("start",imgloader.get_btn(100,30),24);
		cancel=new ImgJButton("cancel",imgloader.get_btn(100,30),24);
		setLayout(null);
		removeAll();
		textl=new DocumentListener(){
			public void changedUpdate(DocumentEvent e){}
			public void insertUpdate(DocumentEvent e){
				submit.setEnabled(ta.getText().length()!=0&&tb.getText().length()!=0);
			}
	        public void removeUpdate(DocumentEvent e){
	        	submit.setEnabled(ta.getText().length()!=0&&tb.getText().length()!=0);
	        }
		};

		la.setText("房間名稱：");
		la.setBounds(200,200,100,30);
		la.setForeground(Color.YELLOW);
		la.setFont(new Font("helvetica",Font.BOLD,16));
		add(la);
		lb.setText("暱稱：");
		lb.setBounds(230,250,70,30);
		lb.setForeground(Color.YELLOW);
		lb.setFont(new Font("helvetica",Font.BOLD,16));
		add(lb);
		ta.setText("newGame");
		ta.setBounds(300,200,200,30);
		ta.getDocument().addDocumentListener(textl);
		add(ta);
		tb.setText("PlayerR");
		tb.setBounds(300,250,200,30);
		tb.getDocument().addDocumentListener(textl);
		add(tb);
		submit.setBounds(270,320,100,30);
		submit.addActionListener(this);
		submit.setEnabled(true);
		add(submit);
		cancel.setBounds(400,320,100,30);
		cancel.addActionListener(mainl);
		add(cancel);
	}
	@Override
	    public void actionPerformed(ActionEvent e) {
	        if(e.getSource()==submit){
	        	roomname=ta.getText();
	        	player1=tb.getText();
        		setRoom();
	    	}
	    	if(e.getSource()==start){
	    		_talk.write("start,");
	    		startgame.BuildGame(_talk,_chat);
	    	}
	    }
	void setRoom(){
		sever=new Thread(new Runnable(){
	        public void run(){
	        	try{
	        		receiveRoom();
	        	}catch(IOException e){}
	        }
		});
		sever2=new Thread(new Runnable(){
	        public void run(){
	        	try{
	        		waitForLink();
	        	}catch(IOException e){}
	        }
		});
		removeAll();
		repaint();

		la.setText("房間名稱：  "+roomname);
		la.setBounds(200,200,300,30);
		add(la);
		lb.setText("玩家1：  "+player1);
		lb.setBounds(200,250,300,30);
		add(lb);
		lc=new JLabel("玩家2：  連線中...");
		lc.setBounds(220,300,300,30);
		lc.setForeground(Color.YELLOW);
		lc.setFont(new Font("helvetica",Font.BOLD,16));
		add(lc);
		start.setBounds(280,340,100,30);
		start.addActionListener(this);
		start.setEnabled(false);
		add(start);
		cancel.setBounds(400,340,100,30);
		add(cancel);
		tag=true;

		sever.start();
		sever2.start();
	}
	public void addMainL(ActionListener mainl){
		this.mainl=mainl;
	}
	void receiveRoom() throws IOException{
		socket=new MulticastSocket(DEFAULT_PORT);
		InetAddress address=InetAddress.getByName(DEFAULT_IP);
		socket.joinGroup(address);
		String data=roomname+","+player1+",";
		byte[] bug=data.getBytes();
		DatagramPacket get,go;
		while(tag){
			byte[] buf=new byte[1024];
			get=new DatagramPacket(buf,buf.length);
			socket.receive(get);
			String tmp=new String(buf,0,get.getLength());
			String[] getString=tmp.split(",");
			if(getString[0].equals(new String("$f$"))){
				InetAddress sendIP = get.getAddress();
				System.out.println("IP : "+sendIP.getHostAddress());
				go=new DatagramPacket(bug,bug.length,address,DEFAULT_PORT);
				socket.send(go);
			}
		}
	}
	void waitForLink() throws IOException{
		waitl=new ServerSocket(PORT_1);
		rival_1=waitl.accept();
		waitl.close();
		rival_2=new Socket(rival_1.getInetAddress(),PORT_2);
		System.out.println("get RIVAL!");
		tag=false;
		if(socket!=null)socket.close();
		socket=null;
		_talk=new StreamData(rival_1,false);
		_chat=new StreamData(rival_2,false);
		GameData tmp=_talk.read();
		lc.setText("玩家2：    "+tmp.get(0));
		start.setEnabled(true);
	}
	public void stopAll() throws IOException{
		tag=false;
		if(socket!=null)socket.close();
		if(rival_1!=null)rival_1.close();
		if(rival_2!=null)rival_2.close();
		if(_talk!=null)_talk.close();
		socket=null;
		rival_1=rival_2=null;
		_talk=null;
	}
}