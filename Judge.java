package DRAKO.Data;

import DRAKO.unit.*;
import DRAKO.table.*;
import DRAKO.Listener.*;

public class Judge implements CardData
{
	Status status;
	Map map;
	CardTable handcard;
	GameListener game;
	int two_walk=0,two_attack=0,nets=0,mad=1;
	boolean if_dr;
	public Judge(Status status,Map map,CardTable handcard,GameListener game){
		this.status=status;
		this.map=map;
		this.handcard=handcard;
		this.game=game;
	}
	public void set(boolean if_dr){
		this.if_dr=if_dr;
	}
	public int act_id(int id){
		if(id==1||id==2||id==3||id==5||id==6||id==7){
			if(map.canAttack()>0)return 2;
			return 1;
		}
		if(id==4)return status.if_live(1)? (map.line(0,1)? 2:1):0;
		if(id==8||id==9){
			if(!status.if_live(6))return 0;
			if(map.line(0,1)||map.line(0,2)||map.line(0,3))return 2;
			return 1;
		}
		if(id==10||id==11){
			if(if_dr&&status.if_live(5)&&map.canwalk(0)&&nets==0)return 2;
			if(if_dr&&(nets>0||!map.canwalk(0)))return 1;
			if(!if_dr)return 2;
			return 0;
		}
		if(id==14)return status.if_live(3)? (nets>0? 1:2):0;
		if(id==15)return 2;
		if(id==16)return 2;
		if(id==17){
			if(status.if_live(4))return nets==0? 2:1;
			return 0;
		}
		if(id==18)return 0;
		if(id==22)return handcard.draw(true)? 2:1;
		if(id==24)return (if_dr&&!handcard.draw(true))? 2:0;
		return 2;
	}
	public int step_1(int id,int id2){//id動作 di2物品編號
		if(id==1||id==2||id==3||id==4){
			if(id2==7){
				if(!status.if_dr_dying())return 2;
				else return 0;
			}
			if(status.if_live(id2)&&status.if_dr_dying())return 2;
			else return 0;
		}
		if(id==5||id==6||id==7){
			if(!status.if_live(id2))return 0;
			if(map.side(0,id2))return 2;
			return 1;
		}
		if(id==12||id==19)return 2;
		if(id==10||id==11){
			if(status.if_live(id2)&&map.canwalk(id2))return 2;
			if(!status.if_live(id2))return 0;
			return 1;
		}
		if(id==13){
			if(id2==two_walk)return 0;
			if(status.if_live(id2))return 2;
			return 0;
		}
		return 0;
	}
	public int next_step(int[] st,int step){
		if(step==0)return 0;
		if(step==1)return 2;
		if(st[1]!=18&&st[step-1]==20)return 20;//取消時
		if(step==2){
			if(st[1]==1||st[1]==2||st[1]==3||st[1]==4)return 2;
			if(st[1]==5||st[1]==6||st[1]==7)return 2;
			if(st[1]==8||st[1]==9)return 1;
			if(!if_dr&&(st[1]==10||st[1]==11))return 2;//矮人走
			if(st[1]==10||st[1]==11||st[1]==17)return 1;//龍走
			if(st[1]==12||st[1]==19)return 2;
			if(st[1]==13){
				two_walk=0;
				return 2;
			}
			if(st[1]==14)return 0;
			if(st[1]==15)return 0;
			if(st[1]==16)return 6;
			if(st[1]==21)return 1;
			if(st[1]==22)return 6;
			if(st[1]==23)return 2;
		}
		if(step==3){
			if(st[1]==1||st[1]==2||st[1]==4)return 6;
			if(st[1]==3){
				two_attack=1;
				return 6;
			}
			if(st[1]==5||st[1]==6||st[1]==7)return 6;
			if(st[1]==10||st[1]==11)return 1;
			if(st[1]==12)return 4;
			if(st[1]==13){//雙走
				two_walk=st[2];
				return 1;
			}
			if(st[1]==19)return 3;
			if(st[1]==23)return 6;
			if(st[1]==25)return 6;
		}
		if(step==4){
			if(st[1]==3){
				return 2;
			}
			if(st[1]==8||st[1]==9)return 6;
			if(st[1]==17)return 0;
			if(st[1]==21){
				map.move(1,st[2],st[3]);
				return 1;
			}
		}
		if(step==5){
			if(st[1]==3){
				two_attack=0;
				return 6;
			}
			if(st[1]==10||st[1]==11)return 0;
			if(st[1]==13){
				map.move(st[2],st[3],st[4]);
				if(status.if_live(1)&&two_walk!=1||status.if_live(2)&&two_walk!=2||status.if_live(3)&&two_walk!=3)return 2;
				//if only one hunter
				game.addStack(7122);
				return 0;
			}
			if(st[1]==18)return 5;
		}
		if(step==6){
			if(st[1]==13)return 1;
			if(st[1]==21){
				map.move(2,st[4],st[5]);
				return 1;
			}
		}
		if(step==8){
			if(st[1]==13)return 0;
			if(st[1]==21){
				map.move(3,st[6],st[7]);
				return 0;
			}
		}
		return 0;
		/*
		0=do it
		1=MAP
		2=Menu
		*/
	}
	public boolean if_DR(){
		return if_dr;
	}
	public int get_round(int id){//每個動作的COST
		return round[id-1];
	}
	public int set_nets(int x){
		return nets+=x;
	}
	public int get_two_attack(){
		return two_attack;
	}
	public int if_end(){
		/*1=HU W 2=DR W*/
		if(!status.if_live(4)&&!status.if_live(5)&&!status.if_live(6))return 1;
		if(!status.if_live(1)&&!status.if_live(2)&&!status.if_live(3))return 2;
		if(!if_dr&&!have_th_to_do())return 2;
		return 0;
	}
	public boolean have_th_to_do(){
		if(if_dr&&nets>0)return true;
		if(handcard.draw(true))return true;
		int[][] tmp=handcard.get_handcard(1);
		for(int i=0;i<tmp.length;i++){
			for(int j=1;j<=CARD[tmp[i][0]-1][0];j++){
				if(CARD[tmp[i][0]-1][i]!=20&&act_id(CARD[tmp[i][0]-1][i])==2)return true;
			}
		}
		return false;
	}
}