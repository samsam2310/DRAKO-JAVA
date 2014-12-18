package DRAKO.unit;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.image.PixelGrabber;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class ImgText extends ImgJPanel
{
	private static final long serialVersionUID=1L;
	JTextArea text;

	public ImgText(Image imgn){
		super(imgn);
		setLayout(null);
		text=new JTextArea();
		text.setOpaque(false);
		text.setBounds(60,30,400,140);
		text.setFont(new Font("Courier", Font.BOLD, 16));
		add(text);
	}
	public void setText(String x){
		text.setText(x);
	}
	public String getText(){
		return text.getText();
	}
}