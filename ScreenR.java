import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class ScreenR extends JFrame implements ActionListener
{
Boolean done=false;
    String inFile="";
	int count;
	String padFile;
	String tfile="";
    Socket sk;
	String sig="";
    Random r=new Random();
    BufferedReader br;
    PrintStream pr;
    JTextField t1=new JTextField("");
    JButton browse=new JButton("Browse");
	 JButton recieve=new JButton("Recieve");
    JLabel lb1=new JLabel("Enter Block Size");
    JTextField tb1=new JTextField("");
    JButton genSig1=new JButton("Generate Signature");
    JButton verify=new JButton("Verify");
    JLabel lb2=new JLabel("Meta Data:"); 
    JLabel FS=new JLabel("FileSize:");
    JLabel fSize=new JLabel("0");
    JLabel type=new JLabel("Signature:");
    JLabel fType=new JLabel("--");
    JLabel sign=new JLabel("Signature Status:");
    JLabel gSig=new JLabel("--");
    JButton send=new JButton("Send");
    JButton clear=new JButton("Clear");
    JButton cancel=new JButton("Cancel");
	File file,f;
	//String genSig="";
	String recfile="",recsign="",rfile="",blocksize="",nblocks="",recfile1="",recsize="";
String[] temp1;
String selfi="";
    ScreenR(Socket sk)
      {
        super("User2");
        this.sk=sk;
        setLayout(null);
        setLayout(null);
        addc(t1,30,30,200,20);
        addc(browse,235,30,80,20);
		browse.addActionListener(this);
		addc(recieve,320,30,80,20);
		recieve.addActionListener(this);
        addc(lb1,30,55,100,20);
		addc(tb1,140,55,260,20);
		addc(genSig1,150,80,150,20);
		genSig1.addActionListener(this);
		addc(verify,310,80,90,20);
		verify.addActionListener(this);
		addc(lb2,30,110,100,20);
		addc(FS,30,130,100,20);
		addc(fSize,135,130,100,20);
		addc(type,30,155,100,20);
		addc(fType,135,155,100,20);
		addc(sign,30,180,100,20);
		addc(gSig,135,180,100,20);
		gSig.setForeground(Color.RED);
		addc(send,150,205,80,20);
		send.addActionListener(this);
		addc(clear,235,205,80,20);
		clear.addActionListener(this);
		addc(cancel,320,205,80,20);
		cancel.addActionListener(this);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(440,300);
		setResizable(false);
		fSize.setForeground(Color.RED);
		fType.setForeground(Color.RED);
		lb2.setForeground(Color.BLUE);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screensize.width / 2) - (getSize().width / 2),
                    (screensize.height / 2) - (getSize().height / 2));
		connect();
		getMsg();
	  }
 public void actionPerformed(ActionEvent ae)
     {
	 if(ae.getSource()==browse)
	     {
			JFileChooser chooser=new JFileChooser();
			int result=chooser.showOpenDialog(this);
			if(result==JFileChooser.CANCEL_OPTION)
				file=null;
			else
			{
			try{
			file=chooser.getSelectedFile();
			String f=file.toString();
			t1.setText(f);
		    BufferedReader br=new BufferedReader(new FileReader(file));
			String st="";
			while((st=br.readLine())!=null)
			{
			inFile=inFile+st+" ";
			}
			inFile = inFile.substring(0, inFile.length()-1);
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			//fType.setText(chooser.getTypeDescription(file));
                                                   fSize.setText(""+inFile.length()+ "Bytes");
		}
		
	}



if(ae.getSource()==genSig1)
{
if(!done)
{

selfi=t1.getText();
System.out.print(selfi);
    if(selfi.trim().isEmpty())
    {
    JOptionPane.showMessageDialog(this,"Select file to generate signature","Select File",JOptionPane.ERROR_MESSAGE);
    }
   else
   {
	try
	{
		if(sig=="")
		{
		int n=0;
		String bs="";
		int m=0;
		padFile=inFile;	
		m=inFile.length();
		bs=tb1.getText();
			
			if(bs.trim().isEmpty() || (Integer.parseInt(bs))>(padFile.length()))
			{
			JOptionPane.showMessageDialog(this,"Enter Proper Block Size","Block Size",JOptionPane.ERROR_MESSAGE);
			}	
			else
			 {
			        final long startTime = System.nanoTime();
			        n=Integer.parseInt(bs);	
			        int P=m%n;
			        int i;
			        int l=padFile.length();
			        count=l/n;
			        if(P!=0)
			         {
			        count++;
			          }
			        String strf[]=new String[count];
					for(int j=0;j<count;j++)
					{
						strf[j]="";
						int S=0;
						if(j==(count-1) && P!=0)
						{
						for(int A=1;A<=P;A++)
						{
						int B=padFile.charAt((n*j)+A-1);
						int temp=(A^B) | (A&B);
						S=S+temp;
						char ch=padFile.charAt((n*j)+A-1);
						strf[j]=strf[j]+ch;
						}
						}
						else
						{
						for(int A=1;A<=n;A++)
						{
						int B=padFile.charAt((n*j)+A-1);
						int temp=(A^B) | (A&B);
						S=S+temp;
						char ch=padFile.charAt((n*j)+A-1);
						strf[j]=strf[j]+ch;
						}
						}
						
					if(j!=0)
					{
					strf[j]=strf[j-1]+(char)(201)+strf[j];
					}
			        		int revS=reverse(S);
			       		String binS=Integer.toBinaryString(revS);
			       		if(j!=0)
			        		sig=sig+"&";
			        		sig=sig+binS;
					}
			final long duration = System.nanoTime() - startTime;
			JOptionPane.showMessageDialog(this,"Time taken to generate signature(in nano sec):"+(duration),"Processing 									Time!!",JOptionPane.INFORMATION_MESSAGE);



			//corrupting the file for testing purpose!!
			if (JOptionPane.showConfirmDialog(null, "Do you want to corrupt the file for testing?", "Testing purpose!!", 
    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
    				== JOptionPane.YES_OPTION)
			{
			strf[count-1]="?#123"+strf[count-1];
			JOptionPane.showMessageDialog(this," Block 1is 				corrupted!!","Corrupted!!",JOptionPane.INFORMATION_MESSAGE);
			}

			tfile=(Integer.toString(count))+(char)(200)+(Integer.toString(n))+(char)(200)+strf[count-1]+(char)(200)+sig								+(char)(200)+(Integer.toString(m));  
			JOptionPane.showMessageDialog(this,"Signature:\n"+(sig),"Signature 													Generated",JOptionPane.INFORMATION_MESSAGE);
			
			fType.setText(sig);
			gSig.setText("Signature Generated");
			}
	}//signature
	else
	{
	JOptionPane.showMessageDialog(this,"Signature:"+(sig),"Signature Generated",JOptionPane.INFORMATION_MESSAGE);
	}			
          }//try
          catch(Exception ex)
          {
          System.out.println(ex.getMessage());
          }
    }//if
}//done
else
{

	if(sig=="")
	{
	int j;
	int a=Integer.parseInt(nblocks);
	int b=Integer.parseInt(blocksize);
	int c=(Integer.parseInt(recsize))%b;
	final long startTime1 = System.nanoTime();
	for(j=0;j<a;j++)
	{
	int S=0;
	int d=temp1[j].length();
		for(int A=1;A<=d;A++)
		{
		int B=temp1[j].charAt(A-1);
		int temp=(A^B) | (A&B);
		S=S+temp;
		}
	int revS=reverse(S);
	String binS=Integer.toBinaryString(revS);
	if(j!=0)
	sig=sig+"&";
	sig=sig+binS;
	}
	final long duration1 = System.nanoTime() - startTime1;
			JOptionPane.showMessageDialog(this,"Time taken to generate signature(in nano sec):"+(duration1),"Processing 									Time!!",JOptionPane.INFORMATION_MESSAGE);
	JOptionPane.showMessageDialog(this,"Signature:"+(sig),"Signature Generated",JOptionPane.INFORMATION_MESSAGE);
	fType.setText(sig);
	gSig.setText("Signature Generated");
	}
else
	{
	JOptionPane.showMessageDialog(this,"Signature:"+(sig),"Signature Generated",JOptionPane.INFORMATION_MESSAGE);
	}

}			   
}
  if(ae.getSource()==verify)
		 {
if(rfile==""){JOptionPane.showMessageDialog(this,"No File to Verify","Alert",JOptionPane.ERROR_MESSAGE);}

else{
if(sig==""){JOptionPane.showMessageDialog(this,"Generate signature to verify","Alert",JOptionPane.ERROR_MESSAGE);}
else{
		 String temp1[]=recsign.split("&");
		 String temp2[]=sig.split("&");
		 int a=Integer.parseInt(nblocks);
		 int f=0;
		 for(int i=0;i<a;i++)
		 {
			if(!temp1[i].equals(temp2[i]))
			{
			f=1;
			JOptionPane.showMessageDialog(this,"Error at block "+(i+1),"Corrupt",JOptionPane.ERROR_MESSAGE);
			}
		 }
		 if(f==0)
		 JOptionPane.showMessageDialog(this,"Verified Successfully","Success",JOptionPane.INFORMATION_MESSAGE);}
			 }
		 }
	 if(ae.getSource()==send)
		 {
			pr.println(tfile);
			JOptionPane.showMessageDialog(this,"Sent Successfully","Success",JOptionPane.INFORMATION_MESSAGE);
			inFile="";
		 }
 if(ae.getSource()==recieve)
		{
			try
{
if(rfile==""){JOptionPane.showMessageDialog(
                  this,"No File Recieved","Alert",JOptionPane.ERROR_MESSAGE);}
else{
tb1.setText(blocksize);
tb1.setEnabled(false);
done=true;
FileInputStream fin= new  FileInputStream("rec.txt");
    byte[] by1 = new byte[(int)fin.available()];
    fin.read(by1);
    fin.close();
     inFile= new String(by1);
fSize.setText(""+inFile.length()+"Bytes");
//fType.setText("TEXT File");
JOptionPane.showMessageDialog(
                  this,"File Recieved","Alert",JOptionPane.INFORMATION_MESSAGE);}
		
}
catch(Exception ex)
{
JOptionPane.showMessageDialog(
                  this,ex.toString(),"Exception",JOptionPane.ERROR_MESSAGE);
}
				      
        }
 if(ae.getSource()==clear)
		{
		t1.setText("");
		tb1.setText("");
		fSize.setText("0");
		fType.setText("--");
		gSig.setText("--");
		sig="";
		inFile="";
		padFile="";blocksize="";
		tfile="";count=0;recfile="";recsign="";rfile="";recsize="";
		nblocks="";done=false;
try
{
if(f.delete()==true)
{
JOptionPane.showMessageDialog(this,"All Files and data Cleared!!","Clear",JOptionPane.INFORMATION_MESSAGE);
}
else
{
JOptionPane.showMessageDialog(this,"All data Cleared!!","Clear",JOptionPane.INFORMATION_MESSAGE);
}
}
catch(Exception ex)
{
JOptionPane.showMessageDialog(this,"No files to clear, but all other data Cleared!!","Clear",JOptionPane.INFORMATION_MESSAGE);
}
		}
		if(ae.getSource()==cancel)
		{
		this.dispose();
		System.exit(0);
		}
     }
public int reverse(int S)
	{
	    int rev=0;
	    while(S>0)
	       {
				int d=S%10;
				rev=rev*10+d;
				S=S/10;	   
		   }
		     return rev;
	}

 public void connect()
     {
        try
	      {
			br = new BufferedReader(new InputStreamReader(sk.getInputStream()));
			pr = new PrintStream(sk.getOutputStream());
          }
		catch(Exception e)
		  {
            System.out.println("Error at connect method"+e.getMessage());
		  }
     }
 public void getMsg()
    {
       try
	     {
            while(true)
             {
		rfile=br.readLine();
                                String temp[]=rfile.split(""+(char)(200));
		nblocks=temp[0];
		blocksize=temp[1];
		recfile1=temp[2];
		recsign=temp[3];
		recsize=temp[4];
		int a=Integer.parseInt(nblocks);
		temp1=new String[a];
		String temp2[]=recfile1.split(""+(char)(201));
		for(int i=0;i<a;i++)
		{
		temp1[i]="";
		temp1[i]=temp2[i];
		recfile=recfile+temp2[i];
		}
                            OutputStream outputStream = new FileOutputStream("rec.txt");
         Writer       writer       = new OutputStreamWriter(outputStream);

        writer.write(recfile);
		f=new File("rec.txt");
		t1.setText(f.getAbsolutePath());
        writer.close();
             }
         }
      catch(Exception e)
	     {
             System.out.println("Error at get msgR:"+e.getMessage());
         }
     }
 public void addc(JComponent c,int x,int y,int w,int h)
     {
        c.setBounds(x,y,w,h);
        add(c);
     }
}