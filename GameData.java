package DRAKO.talk;

import java.io.*;

public class GameData implements Serializable
{
	private static final long serialVersionUID=1L;
	String[] _data;
	String chat;
	int step;
	int[] st;
	public GameData(String in){
		_data=in.split(",");
	}
	public GameData(String in,String nu){
		chat=in;
	}
	public GameData(int[] st,int step){
		this.st=st.clone();
		this.step=step;
	}
	public String get(int id){
		return _data[id];
	}
	public int get_step(){
		return step;
	}
	public int[] get_st(){
		return st;
	}
	public String get_chat(){
		return chat;
	}
}