package DRAKO.table;

import DRAKO.unit.*;
import DRAKO.img.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class MainMenu extends ImgJPanel
{
	private static final long serialVersionUID=1L;
	public ImgJButton room;
	public ImgJButton link;
	public ImgJButton set;
	public ImgJButton exit;
	ImgLoader imgloader;
	public MainMenu(ImgLoader imgloader){
		super(imgloader.get_bg(3));
		this.imgloader=imgloader;
		setLayout(null);

		room=new ImgJButton("New room",imgloader.get_btn(200,40),32);
		link=new ImgJButton("New link",imgloader.get_btn(200,40),32);
		set=new ImgJButton("Set",imgloader.get_btn(200,40),32);
		exit=new ImgJButton("Exit",imgloader.get_btn(200,40),32);

		room.setBounds(300,250,200,40);
		room.setFont(new Font("SansSerif",Font.ITALIC,28));
		room.setForeground(Color.WHITE);
		add(room);
		link.setBounds(300,300,200,40);
		add(link);
		set.setBounds(300,350,200,40);
		add(set);
		exit.setBounds(300,400,200,40);
		add(exit);
	}
	public void addMainL(ActionListener mainl){
		room.addActionListener(mainl);
		link.addActionListener(mainl);
		set.addActionListener(mainl);
		exit.addActionListener(mainl);
	}
}