package DRAKO.table;

import DRAKO.unit.*;
import DRAKO.Listener.*;
import DRAKO.img.*;
import DRAKO.Data.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class Map extends ImgJPanel implements ActionListener
{
	private static final long serialVersionUID=1L;
	int step;
	int[] st;
	Cell[][] mp;
	int[][] chess;
	Judge judge;
	GameListener game;

	Map(ImgLoader imgloader,GameListener game){
		super(imgloader.get_bg(0));
		//super("DRAKO/img/map.png");
		this.game=game;
		setLayout(null);
		mp=new Cell[][]{
			{new Cell(148,25,0,0,false,imgloader,this,this),new Cell(250,25,0,1,false,imgloader,this,this),new Cell(352,25,0,2,false,imgloader,this,this),new Cell(0,0,0,3,true,imgloader,this,this),new Cell(0,0,0,4,true,imgloader,this,this),},
			{new Cell(97,92,1,0,false,imgloader,this,this),new Cell(199,92,1,1,false,imgloader,this,this),new Cell(301,92,1,2,false,imgloader,this,this),new Cell(403,92,1,3,false,imgloader,this,this),new Cell(0,0,1,4,true,imgloader,this,this),},
			{new Cell(46,158,2,0,false,imgloader,this,this),new Cell(148,158,2,1,false,imgloader,this,this),new Cell(250,158,2,2,false,imgloader,this,this),new Cell(352,158,2,3,false,imgloader,this,this),new Cell(454,158,2,4,false,imgloader,this,this),},
			{new Cell(0,0,3,0,true,imgloader,this,this),new Cell(97,225,3,1,false,imgloader,this,this),new Cell(199,225,3,2,false,imgloader,this,this),new Cell(301,225,3,3,false,imgloader,this,this),new Cell(403,225,3,4,false,imgloader,this,this),},
			{new Cell(0,0,4,0,true,imgloader,this,this),new Cell(0,0,4,1,true,imgloader,this,this),new Cell(148,292,4,2,false,imgloader,this,this),new Cell(250,292,4,3,false,imgloader,this,this),new Cell(352,292,4,4,false,imgloader,this,this),},
		};
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
				add(mp[i][j]);
		chess=new int[][]{{2,2},{0,4},{0,4},{0,4}};
		mp[2][2].setSB(0);
		//mp[0][1].setSB(1);
		//mp[4][2].setSB(2);
		//mp[2][4].setSB(3);
	}
	@Override public void actionPerformed(ActionEvent e){
		off();
		game.addStack(((Cell)e.getSource()).get_x());
		game.addStack(((Cell)e.getSource()).get_y());
		game.finish();
		return;
	}
	public void ask_where(int[] st,int step){
		this.st=st;
		this.step=step;
		if(step==2){
			if(st[1]==8||st[1]==9)Spitfire();
			if(st[1]==10||st[1]==11){
				boolean tag=judge.if_DR();
				if(tag){
					game.addStack(0);
					game.finish();
				}
			}
			if(st[1]==17){
				System.out.println("TT");
				fly();
			}
			if(st[1]==21){
				set_HU(1);
			}
		}
		if(step==3){
			if(st[1]==10||st[1]==11){
				walk(st[2],st[1]-9);
			}
			if(st[1]==13){
				walk(st[2],1);
			}
		}
		if(step==4){
			if(st[1]==21){
				set_HU(2);
			}
		}
		if(step==6){
			if(st[1]==13){//雙走
				walk(st[5],1);
			}
			if(st[1]==21){
				set_HU(3);
			}
		}
	}
	public void addJudge(Judge judge){
		this.judge=judge;
	}
	public boolean line(int a,int b){//判斷在同一條線上
		//if 一個座標一樣||座標差一樣
		return chess[a][0]==chess[b][0]||chess[a][1]==chess[b][1]||chess[a][0]-chess[a][1]==chess[b][0]-chess[b][1];
	}
	public void move(int id,int x,int y){//移動
		mp[chess[id][0]][chess[id][1]].setSB(4);
		chess[id]=new int[]{x,y};
		mp[chess[id][0]][chess[id][1]].setSB(id);
	}
	void Spitfire(){
		int l=0,r=4;
		int[][] go={{-1,-1},{-1,0},{0,1},{1,1},{1,0},{0,-1}};
		int x=chess[0][0],y=chess[0][1];
		for(int i=1;i<5;i++)
			for(int j=0;j<6;j++){
				int nx=x+go[j][0]*i,ny=y+go[j][1]*i;
				if(nx>=0&&nx<5&&ny>=0&&ny<5)mp[nx][ny].th_on(j);
			}
	}
	public boolean[] get_fire(int x,int y){
		int j=mp[x][y].get_firej();
		boolean[] fi=new boolean[3];
		for(int i=1;i<4;i++){
			if(chess[i][0]!=-1&&mp[chess[i][0]][chess[i][1]].get_firej()==j)fi[i-1]=true;
		}
		return fi;
	}
	public int canAttack(){
		int tmp=0;
		if(side(0,1))tmp+=1;
		if(side(0,2))tmp+=1;
		if(side(0,3))tmp+=1;
		return tmp;
	}
	public boolean canwalk(int id){
		boolean tt=false;
		int x=chess[id][0],y=chess[id][1];
		if(x+1<5)tt|=mp[x+1][y].can();
		if(x-1>=0)tt|=mp[x-1][y].can();
		if(y+1<5)tt|=mp[x][y+1].can();
		if(y-1>=0)tt|=mp[x][y-1].can();
		if(x+1<5&&y+1<5)tt|=mp[x+1][y+1].can();
		if(x-1>=0&&y-1>=0)tt|=mp[x-1][y-1].can();
		return tt;
	}
	void walk(int id,int sp){//可以走的格子亮起來
		//where l1 can walk to
		System.out.println("walk" +sp);
		Queue<E> qq=new LinkedList<E>();
		qq.offer(new E(chess[id][0],chess[id][1],0));
		while(qq.peek()!=null){
			int x=qq.peek().x,y=qq.peek().y,dr=qq.peek().step;
			qq.poll();
			if(x+1<5&&dr<sp&&mp[x+1][y].on())qq.offer(new E(x+1,y,dr+1));
			if(x-1>=0&&dr<sp&&mp[x-1][y].on())qq.offer(new E(x-1,y,dr+1));
			if(y+1<5&&dr<sp&&mp[x][y+1].on())qq.offer(new E(x,y+1,dr+1));
			if(y-1>=0&&dr<sp&&mp[x][y-1].on())qq.offer(new E(x,y-1,dr+1));
			if(x+1<5&&y+1<5&&dr<sp&&mp[x+1][y+1].on())qq.offer(new E(x+1,y+1,dr+1));
			if(x-1>=0&&y-1>=0&&dr<sp&&mp[x-1][y-1].on())qq.offer(new E(x-1,y-1,dr+1));
		}
	}
	public boolean side(int a,int b){//判斷在旁邊?
		//if 兩個座標一個一樣一個差一 或同加減一
		int x=chess[a][0]-chess[b][0],y=chess[a][1]-chess[b][1];
		if(x==0&&(y==1||y==-1))return true;
		if(y==0&&(x==1||x==-1))return true;
		if(x==-1&&y==-1)return true;
		if(x==1&&y==1)return true;
		return false;
	}
	void fly(){ //可以飛的格子
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
				mp[i][j].on();
	}
	/*void hunter(){
		for(int i=1;i<=3;i++)
			mp[chess[i][0]][chess[i][1]].th_on();
	}*/
	public void kill(int id){
		mp[chess[id][0]][chess[id][1]].setSB(4);
		chess[id]=new int[]{-1,9};
	}
	void off(){
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
				mp[i][j].off();
	}
	void set_HU(int sb){
		System.out.println("set HU!!");
		for(int i=0;i<5;i++){
			for(int j=0;j<5;j++){
				mp[i][j].on_HU(sb);
			}
		}
	}
}
class E{
	public int x,y,step;
	E(int x,int y,int step){
		this.x=x;
		this.y=y;
		this.step=step;
	}
}

/*
    1 2 3 4 5
   1 2 3 4 5
  1 2 3 4 5
 1 2 3 4 5
1 2 3 4 5

0=龍
1=弓
2=狂
3=網
*/
	