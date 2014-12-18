package DRAKO.unit;

import DRAKO.table.*;
import DRAKO.Listener.*;
import DRAKO.img.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Status extends ImgJPanel
{
	private static final long serialVersionUID=1L;
	Map map;
	GameListener game;
	ImgLoader imgloader;
	CardTable handcard;
	int[][] hu=new int[][]{{5,5},{6,6},{4,4}};
	int[][] dr=new int[][]{{2,2},{3,3},{2,2},{4,4}};

	public Status(Map map,GameListener game,ImgLoader imgloader,CardTable handcard){
		super(imgloader.get_bg(2).getScaledInstance(200,400,5));
		setLayout(null);
		this.map=map;
		this.game=game;
		this.imgloader=imgloader;
		this.handcard=handcard;

		repaint();
	}
	@Override public void paintComponent(Graphics g){
		super.paintComponent(g);
        Graphics g2=g;
        int x=50,y=20;
        for(int i=0;i<3;i++){
			g2.drawImage(imgloader.get_sta(i+1),y,x,null);
			int t=hu[i][0]-hu[i][1],h=0;
			for(int j=1;j<=hu[i][0];j++){
				if(y+j*30-h>150){
					h+=150;
					x+=30;
				}
				g2.drawImage(imgloader.get_sta(j<=t?7:0),y+j*30-h,x,null);
			}
			x+=30;
		}
		x+=10;
		for(int i=3;i>=0;i--){
			g2.drawImage(imgloader.get_sta(i+4),y,x,null);
			int t=dr[i][0]-dr[i][1],h=0;
			for(int j=1;j<=dr[i][0];j++){
				if(y+j*30-h>150){
					h+=150;
					x+=30;
				}
				g2.drawImage(imgloader.get_sta(j<=t?7:0),y+j*30-h,x,null);
			}
			x+=30;
		}
		g2.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		g2.setColor(new Color(333333));
		int s=game.set_round(0);
		String st=s>0? ""+s:"wait";
		g2.drawString("剩下的行動數:"+st,25,33);
		g2.setFont(new Font("微軟正黑體", Font.BOLD, 12));
		g2.drawString("手牌數:"+5+" 敵方手牌數:"+5,10,382);
    }
	public void attack(int id,int hurt){
		if(id>=4){
			dr[id-4][1]-=hurt;
			if(dr[id-4][1]<=0)
				dr[id-4][1]=0;
		}else{
			hu[id-1][1]-=hurt;
			if(hu[id-1][1]<=0){
				hu[id-1][1]=0;
				map.kill(id);//map中id 是一樣的
			}
		}
		repaint();
	}
	public boolean if_dr_dying(){
		return (dr[0][1]+dr[1][1]+dr[2][1]>0)&&dr[3][1]==0;
	}
	public boolean if_live(int id){
		if(id<=3)return hu[id-1][1]>0;
		return dr[id-4][1]>0;
	}
}

/*
0=弓
1=狂
2=網
0=翅
1=腳
2=嘴
3=身體
*/
