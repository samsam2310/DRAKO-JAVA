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

public class Link extends ImgJPanel implements ActionListener
{
	private static final long serialVersionUID=1L;
	static String DEFAULT_IP="230.0.0.1";
	static int DEFAULT_PORT=6000;
	static int PORT_1=6001,PORT_2=6002;
	String roomname,player1,player2,name,rname,rip;

	JLabel la;
	JLabel lb;
	JLabel lc;
	JTextField ta;
	ImgJButton submit,rescan,connection;
	public ImgJButton cancel;
	public ImgJButton start;
	JScrollPane sp;
	DefaultListModel<String> list;
	JList<String> roomlist;
	ActionListener mainl;
	DocumentListener textl;
	ListSelectionListener text2;
	StartGameListener startgame;
	Thread scanr=null,wait_for_start=null;;
	Boolean tag=false;
	MulticastSocket socket=null;
	IpList head;
	ServerSocket waitl;
	Socket rival_1=null,rival_2=null;
	StreamData _talk,_chat;
	ImgLoader imgloader;

	public Link(StartGameListener startgame,ImgLoader imgloader){
		super(imgloader.get_bg(5));
		this.startgame=startgame;
		this.imgloader=imgloader;
		reset();
	}
	public void reset(){
		removeAll();
		repaint();

		la=new JLabel("房間名稱：");
		lb=new JLabel("暱稱：");
		ta=new JTextField("PlayerL");
		submit=new ImgJButton("submit",imgloader.get_btn(100,30),24);
		start=new ImgJButton("start",imgloader.get_btn(100,30),24);
		connection=new ImgJButton("連線",imgloader.get_btn(100,30),24);
		rescan=new ImgJButton("刷新",imgloader.get_btn(100,30),24);
		cancel=new ImgJButton("cancel",imgloader.get_btn(100,30),24);
		setLayout(null);
		removeAll();
		textl=new DocumentListener(){
			public void changedUpdate(DocumentEvent e){}
			public void insertUpdate(DocumentEvent e){
				submit.setEnabled(ta.getText().length()!=0);
			}
	        public void removeUpdate(DocumentEvent e){
	        	submit.setEnabled(ta.getText().length()!=0);
	        }
		};
		
		la.setText("暱稱：");
		la.setBounds(230,200,100,30);
		la.setForeground(Color.YELLOW);
		la.setFont(new Font("helvetica",Font.BOLD,16));
		add(la);
		ta.setText("PlayerL");
		ta.setBounds(300,200,200,30);
		ta.getDocument().addDocumentListener(textl);
		add(ta);
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
	     		name=ta.getText();
	     		setList();
	    	}
	    	if(e.getSource()==rescan){
	     		runscan();
	    	}
	    	if(e.getSource()==connection){
				try{
					rname=roomlist.getSelectedValue().toString();
					int id=roomlist.getAnchorSelectionIndex();
					rip=head.getIp(id+1);
					setLink(id+1);
				}catch(NullPointerException f){
					System.out.println("請選取房間!!");
				}
			}
	    }
	void setList(){
		removeAll();
		repaint();
		text2=new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				connection.setEnabled(true);
			}
		};
		
		sp=new JScrollPane();
		
		
		list=new DefaultListModel<String>();
		roomlist=new JList<String>(list);
		roomlist.setForeground(Color.YELLOW);
		roomlist.setFont(new Font("helvetica",Font.BOLD,16));
		roomlist.setOpaque(false);
		sp.setBounds(100,80,400,300);
		sp.setOpaque(false);
		sp.setViewportView(roomlist);
		add(sp);
		roomlist.addListSelectionListener(text2);

		connection.setBounds(550,250,100,30);
		connection.addActionListener(this);
		add(connection);
		rescan.setBounds(550,290,100,30);
		rescan.addActionListener(this);
		add(rescan);
		cancel.setBounds(550,330,100,30);
		add(cancel);
		
		runscan();
    }
	void runscan(){
		scanr=new Thread(new Runnable(){
	        public void run(){
	        	try{
	        		scanRoom();
	        	}catch(IOException e){}
	        }
		});
		tag=true;
		scanr.start();
		try{
        	java.lang.Thread.sleep(1000);
        }catch(InterruptedException e){}
        tag=false;
        socket.close();
	} 
	void scanRoom() throws IOException{
		head=new IpList("head","","",0);
		list.clear();
		connection.setEnabled(false);

		socket=new MulticastSocket(DEFAULT_PORT);
		InetAddress address=InetAddress.getByName(DEFAULT_IP);
		socket.joinGroup(address);
		DatagramPacket get,go;
		byte[] buf=new byte[1024];
		String te=new String("$f$,");
		buf=te.getBytes();
		go=new DatagramPacket(buf,buf.length,address,6000);
		socket.send(go);
		while(tag){
			byte[] bug=new byte[1024];
			get=new DatagramPacket(bug,bug.length);
			socket.receive(get);
			InetAddress sendIP = get.getAddress();
			System.out.println("IP : "+sendIP.getHostAddress());
			String rnm = new String(bug,0,get.getLength());
			String[] getString=rnm.split(",");
			if(!getString[0].equals(new String("$f$"))&&head.add(sendIP.getHostAddress(),getString[0],getString[1])){
				String blank="                                          ";
 				getString[0]=(getString[0]+blank+blank+blank).substring(0,50-getString[0].getBytes().length);
				list.addElement(getString[0]+"| "+getString[1]);
			}
		}
	}
	public void setLink(int id){
		roomname=head.getRoom(id);
		player1=head.getName(id);
		player2=name;

		removeAll();
		repaint();

		la.setText("房間名稱：  "+roomname);
		la.setBounds(200,200,300,30);
		add(la);
		lb.setText("玩家1：  "+player1);
		lb.setBounds(220,250,300,30);
		lb.setForeground(Color.YELLOW);
		lb.setFont(new Font("helvetica",Font.BOLD,16));
		add(lb);
		lc=new JLabel("玩家2：  "+player2);
		lc.setBounds(220,300,200,30);
		lc.setForeground(Color.YELLOW);
		lc.setFont(new Font("helvetica",Font.BOLD,16));
		add(lc);
		start.setText("waiting");
		start.setBounds(280,340,100,30);
		start.setEnabled(false);
		add(start);
		cancel.setBounds(400,340,100,30);
		add(cancel);
		tag=true;

		wait_for_start=new Thread(new Runnable(){
	        public void run(){
		        try{
		        	waitl=new ServerSocket(PORT_2);
		        	rival_1=new Socket(rip,PORT_1);
		        	rival_2=waitl.accept();
		        	waitl.close();
					_talk=new StreamData(rival_1,true);
					_chat=new StreamData(rival_2,true);
					System.out.println("talk to room!");
					_talk.write(name+",");
					while(!_talk.read().get(0).equals(new String("start"))){}
					startgame.BuildGame(_talk,_chat);
				}catch(IOException f){System.out.println("fuck");}
	        }
		});
		wait_for_start.start();
	}
	public void addMainL(ActionListener mainl){
		this.mainl=mainl;
	}
	public void stopAll(){
		tag=false;
		if(socket!=null)socket.close();
		socket=null;
	}
}

class IpList
{
	String _ip,_name,_room;
	int _id;
	IpList _next=null;
	IpList(String ip,String room,String name,int id){
		_ip=ip;
		_name=name;
		_room=room;
		_id=id;
	}
	public boolean add(String ip,String room,String name){
		if(_ip.equals(ip))return false;
		if(_next!=null)return _next.add(ip,room,name);
		_next=new IpList(ip,room,name,_id+1);
		return true;
	}
	public String getIp(int id){
		if(_id==id)return _ip;
		return _next.getIp(id);
	}
	public String getRoom(int id){
		if(_id==id)return _room;
		return _next.getRoom(id);
	}
	public String getName(int id){
		if(_id==id)return _name;
		return _next.getName(id);
	}
}