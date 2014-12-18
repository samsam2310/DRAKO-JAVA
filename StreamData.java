package DRAKO.talk;

import java.net.*;
import java.io.*;
import java.util.*;

public class StreamData
{
	ObjectInputStream in;
	ObjectOutputStream out;
	String rname,name,rip;
	int w,r;
	public StreamData(Socket rival,boolean tag){
		w=r=0;
		try{
			if(tag){
				out=new ObjectOutputStream(rival.getOutputStream());
				in=new ObjectInputStream(rival.getInputStream());
			}else{
				in=new ObjectInputStream(rival.getInputStream());
				out=new ObjectOutputStream(rival.getOutputStream());
			}
		}catch(IOException e){System.out.println("link false!!!");}
		System.out.println("link successful!!");
	}
	public void write(GameData _out){
		System.out.println("W id "+w);
		w+=1;
		try{
			out.writeObject(_out);
			out.flush();
		}catch(IOException e){}
	}
	public void write(String _out){
		System.out.println("W id "+w);
		w+=1;
		try{
			out.writeObject(new GameData(_out));
			out.flush();
		}catch(IOException e){}
	}
	public void chat(String _out){
		try{
			out.writeObject(new GameData(_out,""));
			out.flush();
		}catch(IOException e){}
	}
	public GameData read(){
		System.out.println("R id "+r);
		r+=1;
		try{
			return (GameData)in.readObject();
		}catch(ClassNotFoundException e){}
		catch(IOException e){}
		return null;
	}
	public void close(){
		try{
			in.close();
			out.close();
		}catch(IOException e){}
	}
}