import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class Receiver
{
	Socket s;
	Receiver(String ip,int pno)
	{
		try
		{ 
			s = new Socket(ip,pno);
			System.out.println("Client connected");
			new ScreenR(s);
		}
		catch(Exception e)
		{
		System.out.println("Error"+e);
		}
	}
	public static void main(String args[])
	{
	String ip=args[0];
	int pno=Integer.parseInt(args[1]);
	new Receiver(ip,pno);
	}
}