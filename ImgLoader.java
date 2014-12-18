package DRAKO.img;

import DRAKO.Data.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class ImgLoader implements CardData
{
	Image[] dr=new Image[11];
	Image[] hu=new Image[16];
	Image[] th=new Image[22];
	Image[] act=new Image[22];
	Image[] button=new Image[6];
	Image[] background=new Image[10];
	Image[] sta=new Image[8];
	Image non;
	public ImgLoader(){
		for(int i=0;i<16;i++){
			//if(i<11)dr[i]=Toolkit.getDefaultToolkit().createImage("DRAKO/img/DR/"+(17+i)+".png");
			//hu[i]=Toolkit.getDefaultToolkit().createImage("DRAKO/img/HU/"+(i+1)+".png");
			if(i<11)dr[i]=Toolkit.getDefaultToolkit().createImage("DRAKO/img/DR/"+(17+i)+" 拷貝.png");
			hu[i]=Toolkit.getDefaultToolkit().createImage("DRAKO/img/HU/"+(i+1)+" 拷貝.png");
			if(i<11)dr[i]=dr[i].getScaledInstance(100,150,5);
			hu[i]=hu[i].getScaledInstance(100,150,5);
		}
		for(int i=0;i<22;i++){
			act[i]=Toolkit.getDefaultToolkit().createImage("DRAKO/img/ACT/"+(i+1)+".png");
			th[i]=Toolkit.getDefaultToolkit().createImage("DRAKO/img/TH/"+(i+1)+".png");
			act[i]=act[i].getScaledInstance(100,150,5);
			th[i]=th[i].getScaledInstance(100,150,5);
		}
		for(int i=0;i<5;i++){
			button[i]=Toolkit.getDefaultToolkit().createImage("DRAKO/img/BTN/"+i+".png");
			button[i]=button[i].getScaledInstance(101,86,5);
			//act[i]=act[i].getScaledInstance(100,150,5);
		}
		button[5]=Toolkit.getDefaultToolkit().createImage("DRAKO/img/BTN/"+5+".png");

		for(int i=0;i<10;i++){
			background[i]=Toolkit.getDefaultToolkit().createImage("DRAKO/img/BG/"+i+".png");
			background[i]=background[i].getScaledInstance(800,600,5);
			//act[i]=act[i].getScaledInstance(100,150,5);
		}
		for(int i=0;i<8;i++){
			sta[i]=Toolkit.getDefaultToolkit().createImage("DRAKO/img/STA/"+i+".png");
			sta[i]=sta[i].getScaledInstance(40,40,5);
			//act[i]=act[i].getScaledInstance(100,150,5);
		}

		non=Toolkit.getDefaultToolkit().createImage("DRAKO/img/cell.png");
	}
	public Image get(int id){
		if(id==-1)return non;
		if(id>=17){
			System.out.println(id+" HH");
			return dr[id-17];
		}else{ 
			return hu[id-1];
		}
	}
	public Image get_th(int id){
		return th[id-1];
	}
	public Image get_act(int id){
		return act[id-1];
	}
	public Image get_bg(int id){
		if(id==0)return background[id].getScaledInstance(600,400,5);
		return background[id];
	}
	public Image get_btn(int id){
		return button[id];
	}
	public Image get_btn(int x,int y){
		return button[5].getScaledInstance(x,y,1);
	}
	public Image get_sta(int id){
		return sta[id];
	}
}

/*
background{
	map=0
}
button{
	0=龍
	1=弓
	2=狂
	3=網
	4=cell
}
*/