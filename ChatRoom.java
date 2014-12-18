package DRAKO.unit;

import DRAKO.talk.*;
import DRAKO.img.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class ChatRoom extends ImgJPanel implements ActionListener
{
	private static final long serialVersionUID=1L;
	JTextArea sh=new JTextArea();
	JTextField en=new JTextField();
	JScrollPane sc= new JScrollPane(sh);
	JButton ben;
	StreamData chat;
	boolean if_dr,tag;
	ImgLoader imgloader;
	public ChatRoom(ImgLoader imgloader){
		super(imgloader.get_btn(200,150));
		this.imgloader=imgloader;

		setLayout(null);
		
		sh.setLineWrap(true);
		sh.setWrapStyleWord(true);
		sh.setOpaque(false);
		sh.setEditable(false);
		sc.setBounds(0,0,200,130);
		sc.setOpaque(false);
		sc.getViewport().setOpaque(false);
		sc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		sc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		sc.setVisible(true); 
		add(sc);
		en.setBounds(0,130,170,20);
		en.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	if(en.getText().length()>0){
					sh.append("\n"+(if_dr?"DR ":"HU ")+en.getText());
					sh.setCaretPosition(sh.getText().length());
					chat.chat(en.getText());
				}
				en.setText("");
            }});
		add(en);
		ben=new ImgJButton(imgloader.get_btn(30,20));
		ben.setBounds(170,130,30,20);
		ben.addActionListener(this);
		add(ben);
	}
	public void actionPerformed(ActionEvent e){
		if(en.getText().length()>0){
			sh.append("\n"+(if_dr?"DR ":"HU ")+en.getText());
			sh.setCaretPosition(sh.getText().length());
			chat.chat(en.getText());
		}
		en.setText("");
	}
	Thread chver= new Thread(new Runnable(){
        public void run(){
        	while(tag){
        		String word=chat.read().get_chat();
        		sh.append("\n"+(if_dr?"HU ":"DR ")+word);
        		sh.setCaretPosition(sh.getText().length());
        	}
        }
    });
    public void addStream(StreamData chat){
    	this.chat=chat;
    }
    public void set(boolean if_dr){
    	this.if_dr=if_dr;
    	if(if_dr){
    		set_img(imgloader.get_bg(6).getScaledInstance(200,200,5));
    	}else{
    		set_img(imgloader.get_bg(7).getScaledInstance(200,200,5));
    	}
    	tag=true;
    	chver.start();
    }
    public void close(){
    	chat.close();
    }
}
