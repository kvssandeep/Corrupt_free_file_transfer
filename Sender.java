import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class Sender
{
ServerSocket ss;
Socket s;
Sender(int pno)
{
try{
ss=new ServerSocket(pno);
System.out.println("Server Connected\nWaiting for Client");
while(true)
{
s=ss.accept();
System.out.println("Client Connected");
new ScreenS(s);
}
}catch(Exception e)
{
System.out.println("Error"+e);
}
}
public static void main(String args[]) throws IOException
{
int pno=Integer.parseInt(args[0]);
new Sender(pno);
}
}