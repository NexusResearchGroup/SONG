
import java.io.*;
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;



public class Demo extends Applet  implements ActionListener,ItemListener,WindowListener
{
///layout of the simulator interface is:
///menuframe: the whole window;
///variablesPanel(left);
///DrawArea(right-up);
///DrawPanel(right-down),including legend, status bar and buttons.

	URL url=null;
	URL helpurl=null;
	public String currentInputFile;
	Frame menuframe;
	VariablesPanel vp;
	public DrawArea da;
	public DrawPanel dp;
	public NetworkDynamics nd;


	boolean  graphRead = false;
	boolean  evolved = false;
	boolean  drawSpeeds = true;//true when speed is drawn; false when volume is drawn
	public String getnetwork;
	public Frame f;
	public TextArea stat;
	MenuBar mbar,fmbar;


	public void init() {

		url=getCodeBase();
		vp = new VariablesPanel();
		dp = new DrawPanel(this);
		da = new DrawArea( dp );
		getnetwork="10X10 Grid Network";

		WindowDestroyer windowKiller=new WindowDestroyer();


	//Define the main window
		menuframe = new MenuFrame("SONG: Simulator of Network Growth 1.0",  this )  ;
		//define the size of menuframe according to the screen size
		Dimension screensize = getToolkit().getScreenSize();

		menuframe.setLayout(new BorderLayout());
		menuframe.add("West", vp);
		menuframe.add("Center", da);

		menuframe.addWindowListener(this);
		menuframe.setSize ((int)(1.0*screensize.width),
					  (int)(0.99*screensize.height));

		menuframe.setVisible(true);
		//define the menu
		mbar = new MenuBar();
		menuframe.setMenuBar(mbar);
		Menu song = new Menu("SONG1.0");
		Menu help=new Menu("Help");

		MenuItem  evolve1,quit,about,instruction;
		song.add(evolve1 = new MenuItem("Evolve "));
		song.add(quit = new MenuItem("Quit"));
		help.add(instruction = new MenuItem("Instructions"));
		//help.add(about=new MenuItem("About Song1.0"));

		mbar.add(song);
		mbar.add (help);

		evolve1.addActionListener(this);
		quit.addActionListener(this);
		//about.addActionListener(this);
		instruction.addActionListener(this);

	//Define the result window
		f=new Frame("Statistics");
		stat=new TextArea("");


///load the 10*10 network when the window is opened
		vp.network.select("10X10 Grid Network" );
		dp.showStatus.setText("10X10 Network Loaded...");

		dp.evolve.setEnabled(true) ;
		dp.statistics .setEnabled(false);
		currentInputFile = "Grid10.txt";

		try {
			nd = new NetworkDynamics( vp.variables,url, currentInputFile);
		} catch (IOException e) {
		}
		
		dp.first.setEnabled(false);
		dp.previous .setEnabled(false);
		dp.next .setEnabled(false) ;
		dp.last.setEnabled(false) ;
		dp.statistics .setEnabled( false);
		dp.whichAttribute.setEnabled(false) ;
		dp.scale .setEnabled( false);

		da.setMapVariables();
		graphRead = true;
		evolved = false;
		da.currentYear = 0;
		da.repaint();
///////////

	}




	public void paint( Graphics g ) {
		//da.paint( g);
	}


///define the events related to window
	public void windowClosing(WindowEvent e){
		Object obj = e.getSource();
		if(obj.equals( menuframe))menuframe.dispose() ;
		else if (obj.equals( f))f.dispose() ;
	}

	public void windowOpened(WindowEvent e){
		da.setVisible(true) ;

	}

	public void windowActivated(WindowEvent e){

		da.repaint() ;
	}

	public void windowDeactivated(WindowEvent e){

		da.repaint() ;
	}

	public void windowIconified(WindowEvent e){

		da.repaint() ;
	}

	public void windowDeiconified(WindowEvent e){

		da.repaint() ;
	}

	public void windowClosed(WindowEvent e){


	}
//

	public void actionPerformed( ActionEvent ae) {
		String arg = (String) ae.getActionCommand();
		Object obj = ae.getSource();

//
		  if(arg=="Evolve "){
			da.dp.evolve .setEnabled( false);
			vp.setEnabled( false);
			evolved = false;


	///initializing...			
			try {
				nd = new NetworkDynamics( vp.variables, url,currentInputFile,this);
			}
			catch(IOException ie) {
			}
	//////////////////
			da.currentYear = 0;
			da.dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );

	///running...	
			nd.NetworkDynamix(url, vp.variables);
			da.repaint();
			da.dp.setVisible(true);
	/////////////			
	
			da.dp.evolve .setEnabled( false);
			vp.setEnabled( true);
			evolved = true;	
	
	
			da.dp.first.setEnabled(true);
			da.dp.previous .setEnabled(true);
			da.dp.next .setEnabled(true) ;
			da.dp.last.setEnabled(true) ;
			da.dp.statistics .setEnabled( true);
			da.dp.whichAttribute.setEnabled(true) ;
			da.dp.scale .setEnabled( true);
		  }


		if(arg=="Quit"){
			menuframe.dispose() ;
		}



		if(arg=="Instructions"){

			try {
			helpurl=new URL(url,"HelpFileSONG1.0.htm"); }

			catch (MalformedURLException e) {
			System.out.println("Bad URL:" + helpurl);
			}

			getAppletContext().showDocument(helpurl,"_blank");
		}

		if(arg=="About Song1.0"){

			try {
			helpurl=new URL(url,"HelpFileSONG1.0.htm"); }

			catch (MalformedURLException e) {
			System.out.println("Bad URL:" + helpurl);
			}

			getAppletContext().showDocument(helpurl,"_blank");
		}

		if(arg=="Save"){
			FileDialog savefile=new FileDialog(f,"Save Statistics...",FileDialog.SAVE);

			savefile.show() ;

			FileOutputStream out= null;
			System.out.print(savefile.getFilenameFilter()+"\n");
			System.out.print(savefile.getFile()+"\n");
			File s= new File(savefile.getDirectory(),savefile.getFile()  );

			try{
						out= new FileOutputStream(s);
					}catch(Exception e)
					{
						System.out.println("Unable to open file");
						return;
					}
			PrintStream psOut=new PrintStream(out);
			psOut.print(stat.getText());///
			try{
			out.close();
			}catch(IOException e){
			System.out.println("e");
			}
		}

		if(arg=="Close"){
			f.dispose()  ;
		}

// Command Evolve
		if(obj.equals(dp.evolve )){
			
			da.dp.evolve .setEnabled( false);
			vp.setEnabled( false);
			evolved = false;


	///initializing...			
			try {
				nd = new NetworkDynamics( vp.variables, url,currentInputFile,this);
			}
			catch(IOException ie) {
			}
	//////////////////
			da.currentYear = 0;
			da.dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );

	///running...	
			nd.NetworkDynamix(url, vp.variables);
			da.repaint();
			da.dp.setVisible(true);
	/////////////			
	
			da.dp.evolve .setEnabled( false);
			vp.setEnabled( true);
			evolved = true;	
	
	
			da.dp.first.setEnabled(true);
			da.dp.previous .setEnabled(true);
			da.dp.next .setEnabled(true) ;
			da.dp.last.setEnabled(true) ;
			da.dp.statistics .setEnabled( true);
			da.dp.whichAttribute.setEnabled(true) ;
			da.dp.scale .setEnabled( true);
		}

//Command Statistics
		if(obj.equals(dp.statistics)){
			//JOptionPane.showMessageDialog(menuframe,"The Moe's Results: avgSpeed="+nd.avgSpeed+"; avgVolume="+nd.avgVolume+"; vkt="+nd.vkt+"; vht="+nd.vht);
			f.dispose();
			f=new Frame("Statistics");
			f.addWindowListener(this);

			stat=new TextArea("");
			Dimension screensize = getToolkit().getScreenSize();
			//define the size of menuframe according to the screen size
			f.setSize ((int)(0.35*screensize.width),
						  (int)(0.80*screensize.height));
			//define the menu
			fmbar = new MenuBar();
			f.setMenuBar(fmbar);
			Menu file = new Menu("File");

			MenuItem  save,close;
			file.add(save = new MenuItem("Save"));
			file.add(close = new MenuItem("Close"));

			fmbar.add(file);

			save.addActionListener(this);
			close.addActionListener(this);
			stat.setText( "");
			f.setVisible( false);
			stat.setVisible(false);

			String temp="";

			stat.append(new String("\n\n---Network Summary---\n\n"));

			stat.append(new String("       Item                    Description/Value\n\n"));

			stat.append(new String("0.  Network Type       \t"+vp.network .getSelectedItem() +"\n"));
			stat.append(new String("1.  Speed Distribution        \t"+vp.speed.getSelectedItem() +"\n"));
			stat.append(new String("	Speed Multipler           \t"+vp.v99.value() +"\n"));
			stat.append(new String("2.  Land use Distribution     \t"+vp.landuse.getSelectedItem() +"\n"));
			stat.append(new String("	Land use Multipler        \t"+vp.v100.value() +"\n"));			
			stat.append(new String("3.  Travel Demand Model\t"+"\n"));
			stat.append(new String("3.1 Value of Time             \t"+vp.v6.value() +"\n"));
			stat.append(new String("3.2 Friction Factor           \t"+vp.v10.value()+"\n"));
			stat.append(new String("4.  Revenue Model\t"+"\n"));
			stat.append(new String("4.1 Toll rate                 \t"+vp.v13.value() +"\n"));
			stat.append(new String("4.2 Coeff. of length          \t"+vp.v14.value() +"\n"));
			stat.append(new String("4.3 Coeff. of speed           \t"+vp.v15.value() +"\n"));
			stat.append(new String("5.  Cost Model\t" +"\n"));
			stat.append(new String("5.1 Coeff. of length          \t"+vp.v17.value() +"\n"));
			stat.append(new String("5.2 Coeff. of flow            \t"+vp.v18.value() +"\n"));
			stat.append(new String("5.3 Coeff. of speed           \t"+vp.v19.value()+"\n"));
			stat.append(new String("6.  Investment Model\t"+"\n"));
			stat.append(new String("6.1 Speed improvement coeff.  \t"+vp.v20.value() +"\n"));
			stat.append(new String("\nActual Number of Iterations   \t"+(nd.endyear +1) +"\n"));

			stat.append(new String("\n---MOEs Results---\n\n"));

			stat.append(new String("  MOE       Value\n\n"));

			stat.setFont(new Font("Times New Roman",Font.PLAIN|Font.ROMAN_BASELINE |Font.BOLD ,12));
			stat.append(new String("AvgSpeed\t"+nd.avgSpeed +"\n"));
			stat.append(new String("AvgFlow\t"+nd.avgVolume  +"\n"));
			stat.append(new String("vkt\t"+nd.vkt +"\n"));
			stat.append(new String("vht\t"+nd.vht +"\n"));
			stat.append(new String("Total Cost\t"+nd.totalCost +"\n"));
			stat.append(new String("Total Revenue\t"+nd.totalRevenue  +"\n"));
			stat.append(new String("Cumulative Cost\t"+nd.cumulativeCost  +"\n"));
			stat.append(new String("Cumulative Revenue\t"+nd.cumulativeRevenue  +"\n"));
			stat.append(new String("Improvement Term\t")+nd.ImproveTerm +"\n");


			stat.setVisible( true);
			f.add(stat,"Center");
			f.setVisible( true);

		}

//Command << < > >>
		  if(arg.equals("<<")) {
			  da.currentYear = 0;
			  da.repaint();
		  } else if( arg.equals("<") ) {
			  if( da.currentYear > 0 ) {
				  da.currentYear--;
				  da.repaint();
			  }  else {
				  da.currentYear = nd.endyear ;
				  da.repaint();
			  }
		  } else if(arg.equals(">") ) {
			  if(da.currentYear < nd.endyear ) {
				  da.currentYear ++;
				  da.repaint();
			  } else if(da.currentYear == nd.endyear){
				  da.currentYear = 0;
				  da.repaint();
			  }
		  } else if(arg.equals(">>") ) {
			  da.currentYear = nd.endyear;
			  da.repaint();
		  }

		dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );

	}

	public void itemStateChanged( ItemEvent ie) {
		String arg = (String) ie.getItem();
		Object obj = ie.getSource();
		if (obj.equals(dp.whichAttribute)){
				if(dp.scale .getSelectedItem() =="Absolute"){
					if(arg.equals( "Speed")){
						drawSpeeds = true;
						dp.unit.setText("");
						dp.bluefor.setText("0~" + Integer.toString(5));
						dp.greenfor.setText(Integer.toString(5) +"~" + Integer.toString(10));
						dp.yellowfor.setText(Integer.toString(10)+"~" + Integer.toString(15));
						dp.orangefor.setText(Integer.toString(15) +"~"+ Integer.toString(20));
						dp.redfor.setText(Integer.toString(20) +"~"+ "  ");
						da.repaint();
					}
					else{
						drawSpeeds = false;
						dp.unit.setText("");
						dp.bluefor.setText("0~" + Integer.toString(1000));
						dp.greenfor.setText(Integer.toString(1000)+"~" + Integer.toString(2000));
						dp.yellowfor.setText(Integer.toString(2000)+"~" + Integer.toString(3000));
						dp.orangefor.setText(Integer.toString(3000)+"~"+Integer.toString(4000));
						dp.redfor.setText(Integer.toString(4000)+"~");
						da.repaint();
					}

				}

				else{
					if(arg.equals( "Speed")){
						drawSpeeds = true;
						dp.unit.setText("");
						da.repaint();
					}
					else{
						drawSpeeds = false;
						dp.unit.setText("");
						da.repaint();
					}
				}
		}

		else if (obj.equals(dp.scale)){
			if(arg.equals( "Relative")){
				dp.unit.setText("");
				dp.bluefor.setText("Lowest");
				dp.greenfor.setText("Lower");
				dp.yellowfor.setText("Middle");
				dp.orangefor.setText("Higher");
				dp.redfor.setText("Highest");
				da.repaint() ;
			}
			else{
				if(dp.whichAttribute.getSelectedItem() .equals( "Speed")){
					drawSpeeds = true;
					dp.unit.setText("");
					dp.bluefor.setText("0~" + Integer.toString(5));
					dp.greenfor.setText(Integer.toString(5) +"~" + Integer.toString(10));
					dp.yellowfor.setText(Integer.toString(10)+"~" + Integer.toString(15));
					dp.orangefor.setText(Integer.toString(15) +"~"+ Integer.toString(20));
					dp.redfor.setText(Integer.toString(20) +"~"+ "  ");
					da.repaint();
				}
				else{
					drawSpeeds = false;
					dp.unit.setText("");
					dp.bluefor.setText("0~" + Integer.toString(1000));
					dp.greenfor.setText(Integer.toString(1000)+"~" + Integer.toString(2000));
					dp.yellowfor.setText(Integer.toString(2000)+"~" + Integer.toString(3000));
					dp.orangefor.setText(Integer.toString(3000)+"~"+Integer.toString(4000));
					dp.redfor.setText(Integer.toString(4000)+"~");
					da.repaint();
				}
			}



		}
	}

	public void writeStat(URL url){

		PrintWriter out=null;

		try
			{
				out=new PrintWriter(new FileOutputStream("Stat.htm") );
			}
		catch(IOException e)
			{

				System.out.print("Error opening the files!");
				System.exit(0);
			}
		dp.showStatus.setText("right!");
									dp.repaint() ;
		out.print("<html>"+
//		"<head>"+
//		"<meta http-equiv=\"Content-Language\" content=\"en-us\">"+
//		"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">"+
//		"<meta name=\"GENERATOR\" content=\"Microsoft FrontPage 4.0\">"+
//		"<meta name=\"ProgId\" content=\"FrontPage.Editor.Document\">"+
		"<title>Statistics</title>"+
//		"</head>"+
		"<body>"+
		"<p align=\"left\"><font face=\"Times New Roman\" size=\"4\"><b>Statistics</b></font></p>"+
		"<p align=\"left\"><font face=\"Times New Roman\" size=\"3\"><b><u>MOE's Results</u></b></font></p>"+
		"<table border=\"1\">"+
		" <tr>"+
			"<td align=\"center\"><font face=\"Times New Roman\" size=\"3\">MOE</font></td>"+
			"<td align=\"center\"><font face=\"Times New Roman\" size=\"3\">Value</font></td>"+
		  "</tr>"+
		"<tr><td align=\"center\"><font face=\"Times New Roman\" size=\"3\">Average Speed</font></td><td><p align=\"center\">��<font face=\"Times New Roman\" size=\"3\">"+nd.avgSpeed+"</font></p></td></tr>"+
		"<tr><td align=\"center\"><font face=\"Times New Roman\" size=\"3\">Average Flow</font></td><td><p align=\"center\">��<font face=\"Times New Roman\" size=\"3\">"+nd.avgVolume+"</font></p></td></tr>"+
		"<tr><td align=\"center\"><font face=\"Times New Roman\" size=\"3\">vkt</font></td><td><p align=\"center\">��<font face=\"Times New Roman\" size=\"3\">"+nd.vkt+"</font></p></td></tr>"+
		"<tr><td align=\"center\"><font face=\"Times New Roman\" size=\"3\">vht</font></td><td><p align=\"center\">��<font face=\"Times New Roman\" size=\"3\">"+nd.vht+"</font></p></td></tr>"+
		"</table>"+
		"<p><font face=\"Times New Roman\" size=\"3\"><b><u>Network Summary</u></b></font></p>"+
		"<table border=\"1\">"+
		  "<tr><td></td><td><font face=\"Times New Roman\" size=\"3\">Description or Value</font></td></tr>"+
		"<tr><td><font face=\"Times New Roman\" size=\"3\">0. Network Type</font></td><td><font face=\"Times New Roman\" size=\"3\">"+vp.network .getSelectedItem() +"</font></td></tr>"+
		"<tr><td><font face=\"Times New Roman\" size=\"3\">1. Speed Distribution</font></td><td><font face=\"Times New Roman\" size=\"3\">"+vp.speed .getSelectedItem() +"</font></td></tr>"+
		"<tr><td><font face=\"Times New Roman\" size=\"3\">2. Land use Distribution</font></td><td><font face=\"Times New Roman\" size=\"3\">"+vp.landuse.getSelectedItem() +"</font></td></tr>"+
		"<tr><td><font face=\"Times New Roman\" size=\"3\">5. Travel Demand Model</font></td><td><font face=\"Times New Roman\" size=\"3\">"+"</font></td></tr>"+
		"<tr><td><font face=\"Times New Roman\" size=\"3\">5.1 Value of Time</font></td><td><font face=\"Times New Roman\" size=\"3\">"+vp.v6.value() +"</font></td></tr>"+
		"<tr><td><font face=\"Times New Roman\" size=\"3\">5.2 Friction Factor</font></td><td><font face=\"Times New Roman\" size=\"3\">"+vp.v10.value() +"</font></td></tr>"+
		"<tr><td><font face=\"Times New Roman\" size=\"3\">6. Revenue Model</font></td><td><font face=\"Times New Roman\" size=\"3\">"+"</font></td></tr>"+
		"<tr><td><font face=\"Times New Roman\" size=\"3\">6.1 Toll rate</font></td><td><font face=\"Times New Roman\" size=\"3\">"+vp.v13.value() +"</font></td></tr>"+
		"<tr><td><font face=\"Times New Roman\" size=\"3\">6.2 Coeff. of length</font></td><td><font face=\"Times New Roman\" size=\"3\">"+vp.v14.value() +"</font></td></tr>"+
		"<tr><td><font face=\"Times New Roman\" size=\"3\">6.3 Coeff. of speed</font></td><td><font face=\"Times New Roman\" size=\"3\">"+vp.v15.value() +"</font></td></tr>"+
		"<tr><td><font face=\"Times New Roman\" size=\"3\">7. Cost Model</font></td><td><font face=\"Times New Roman\" size=\"3\">"+"</font></td></tr>"+
		"<tr><td><font face=\"Times New Roman\" size=\"3\">7.1 Coeff. of length</font></td><td><font face=\"Times New Roman\" size=\"3\">"+vp.v17.value() +"</font></td></tr>"+
		"<tr><td><font face=\"Times New Roman\" size=\"3\">7.2 Coeff. of flow</font></td><td><font face=\"Times New Roman\" size=\"3\">"+vp.v18.value() +"</font></td></tr>"+
		"<tr><td><font face=\"Times New Roman\" size=\"3\">7.3 Coeff. of speed</font></td><td><font face=\"Times New Roman\" size=\"3\">"+vp.v19.value() +"</font></td></tr>"+
		"<tr><td><font face=\"Times New Roman\" size=\"3\">8 Investment Model</font></td><td><font face=\"Times New Roman\" size=\"3\">"+"</font></td></tr>"+
		"<tr><td><font face=\"Times New Roman\" size=\"3\">8.1 Speed improvement coeff.</font></td><td><font face=\"Times New Roman\" size=\"3\">"+vp.v20.value() +"</font></td></tr>"+
		"</table></body></html>");
		out.close();

	}

///	total 23 variable are allocated to get the parameters of models
/// some of them are 'visible' in the interface
/// the others are 'invisible' and are fixed by default
/// this method is used to give the values of some 'invisible' variables
	public void writeVariables(){
	//total 23 variables in vp.variables[]
	//0,1,3,4,5,11 can be obtained from pull-down boxes
	//6-10,13-15,17-20 can be obtained from scrollbars,where 7=13,8=14,9=15
	//12,16,21,22 are fixed
	//2 never used

//from pull-down boxes
	//0,1
	if(vp.speed .getSelectedItem() .equals(vp.speed)){

		if(vp.speed .getSelectedItem() .equals("Uniform")){
			vp.variables[0]=5*(float)vp.v99.value();;
			vp.variables[1]=5*(float)vp.v99.value();;
		}
		else if(vp.speed .getSelectedItem() .equals("Random")){
			vp.variables[0]=1*(float)vp.v99.value();;
			vp.variables[1]=10*(float)vp.v99.value();;
		}
		else if(vp.speed .getSelectedItem() .equals("Prespecified Random")){
			if(getnetwork.equals("5X5 Grid Network")){vp.variables[0]=vp.variables[1]=-5;}
			else if(getnetwork.equals("10X10 Grid Network")){vp.variables[0]=vp.variables[1]=-10;}
			else if(getnetwork.equals("15X15 Grid Network")){vp.variables[0]=vp.variables[1]=-15;}
			else if(getnetwork.equals("20X20 Grid Network")){vp.variables[0]=vp.variables[1]=-20;}
			else if(getnetwork.equals("A  Network  with  River")){vp.variables[0]=vp.variables[1]=-99;}
		}
	}

	//3,4,5
	if(vp.landuse.getSelectedItem() .equals(vp.landuse)){


		if(vp.landuse.getSelectedItem() .equals("Uniform")){
			vp.variables[3]=10*(float)vp.v100.value();
			vp.variables[4]=10*(float)vp.v100.value();
			vp.variables[5]=(float)0.0;
			}
		else if(vp.landuse.getSelectedItem() .equals("Random")){
			vp.variables[3]=5*(float)vp.v100.value();
			vp.variables[4]=15*(float)vp.v100.value();
			vp.variables[5]=(float)0.0;
			}
		else if(vp.landuse.getSelectedItem() .equals("Downtown")){
			vp.variables[3]=5*(float)vp.v100.value();
			vp.variables[4]=15*(float)vp.v100.value();
			vp.variables[5]=(float)1.0;
			}
		else if(vp.landuse.getSelectedItem() .equals("Prespecified Random")){
			if(getnetwork.equals("5X5 Grid Network")){vp.variables[3]=vp.variables[4]=-5;}
			else if(getnetwork.equals("10X10 Grid Network")){vp.variables[3]=vp.variables[4]=-10;}
			else if(getnetwork.equals("15X15 Grid Network")){vp.variables[3]=vp.variables[4]=-15;}
			else if(getnetwork.equals("20X20 Grid Network")){vp.variables[3]=vp.variables[4]=-20;}
			else if(getnetwork.equals("A  Network  with  River")){vp.variables[3]=vp.variables[4]=-99;}
			vp.variables[5]=(float)0.0;
		}

	}
	//11
		if(vp.speed.getSelectedItem()=="Uniform" && vp.landuse.getSelectedItem()=="Uniform")
			vp.variables[11]=1;
		else if(vp.speed.getSelectedItem()=="Uniform" && vp.landuse.getSelectedItem()=="Downtown")
			vp.variables[11]=1;
		else
			vp.variables[11]=0;

		if(vp.network.getSelectedItem()=="A  Network  with  River")
			vp.variables[11]=0;


///from scroll bars
	vp.variables[6]=(float)vp.v6.value();
	vp.variables[13]=(float)vp.v13.value();
	vp.variables[10]=(float)vp.v10.value();
	vp.variables[14]=(float)vp.v14.value();
	vp.variables[15]=(float)vp.v15.value();
	vp.variables[17]=(float)vp.v17.value();
	vp.variables[18]=(float)vp.v18.value();
	vp.variables[19]=(float)vp.v19.value();
	vp.variables[20]=(float)vp.v20.value();

	vp.variables[7]=vp.variables[13];
	vp.variables[8]=vp.variables[14];
	vp.variables[9]=vp.variables[15];

///fixed
	vp.variables [12]=1;
	vp.variables [16]=365;
	vp.variables [21]=0;
	vp.variables [22]=20;
	}


	class DrawPanel extends Panel {

		Demo sd;

		Panel legend=new Panel();
		Panel button=new Panel();;
		Panel status=new Panel();;
///////////////////////////////////////////////
		Choice whichAttribute = new Choice ();
		Choice scale = new Choice ();
		//Button help=new Button("Help");
		Label blank=new Label("    ");
		Button evolve = new Button("Evolve");

		Button statistics=new Button("Statistics");

		Button first = new Button("<<");
		Button previous = new Button("<");
		Label year = new Label(  "   Year 0    " , Label.CENTER );
		Button next = new Button(">");
		Button last = new Button(">>");

////////////////////////////////////////////////

		Label unit=new Label("");

		Label blue=new Label("    ");
		Label green=new Label("    ");
		Label yellow=new Label("    ");
		Label orange=new Label("    ");
		Label red=new Label("    ");

		Label bluefor=new Label("            ");
		Label greenfor=new Label("            ");
		Label yellowfor=new Label("            ");
		Label orangefor=new Label("            ");
		Label redfor=new Label("            ");

////////////////////////////////////////////////
		Label showStatus=new Label("");


		public DrawPanel( Demo sd) {
			showStatus.setFont(new Font("",Font.BOLD,12));
			this.sd = sd;
			setLayout(new BorderLayout());

//			button panel

			whichAttribute.addItem("Speed");
			whichAttribute.addItem("Volume");
			whichAttribute.select("Speed");
			drawSpeeds=true;
			whichAttribute.addItemListener(this.sd);

			scale.addItem("Absolute");
			scale.addItem("Relative");
			scale.select("Absolute");
			scale.addItemListener(this.sd);



			evolve.addActionListener(this.sd);
			first.addActionListener(this.sd);
			previous.addActionListener(this.sd);
			next.addActionListener(this.sd);
			last.addActionListener(this.sd);
			statistics.addActionListener( this.sd);

			evolve.setEnabled(false);
			statistics.setEnabled(false);
			first.setEnabled(false);
			previous .setEnabled(false);
			next .setEnabled(false) ;
			last.setEnabled(false) ;
			scale.setEnabled( false);
			whichAttribute.setEnabled(false) ;



			button.add(evolve);
			button.add(blank);
			button.add(scale);
			button.add(whichAttribute);
			button.add( first);
			button.add( previous );
			button.add(year);
			button.add( next );
			button.add( last);
			button.add(new Label("   "));
			button.add(statistics);

			add(button,"South");

//			legend panel
			legend.setLayout( new GridLayout(1,11));


			blue.setBackground(new Color(60, 100, 250));
			legend.add(blue);
			legend.add(bluefor);

			green.setBackground(new Color(8, 140, 14));
			legend.add(green);
			legend.add(greenfor);

			yellow.setBackground(Color.YELLOW );
			legend.add(yellow);
			legend.add(yellowfor);

			orange.setBackground(new Color(250, 125, 0));
			legend.add(orange);
			legend.add(orangefor);


			red.setBackground(new Color(200, 20, 20));
			legend.add(red);
			legend.add(redfor);
			if (scale.getSelectedItem() =="Absolute"){
				if (whichAttribute.getSelectedIndex() ==0)
				{
					unit.setText("");
					bluefor.setText("0~" + Integer.toString(5));
					greenfor.setText(Integer.toString(5) +"~" + Integer.toString(10));
					yellowfor.setText(Integer.toString(10)+"~" + Integer.toString(15));
					orangefor.setText(Integer.toString(15) +"~"+ Integer.toString(20));
					redfor.setText(Integer.toString(20) +"~"+ "  ");
					repaint();
				}
				else
				{
					unit.setText("");
					drawSpeeds = false;
					bluefor.setText("0~" + Integer.toString(1000));
					greenfor.setText(Integer.toString(1000)+"~" + Integer.toString(2000));
					yellowfor.setText(Integer.toString(2000)+"~" + Integer.toString(3000));
					orangefor.setText(Integer.toString(3000)+"~"+Integer.toString(4000));
					redfor.setText(Integer.toString(4000)+"~");
					repaint();
				}

			}
			else{

				unit.setText("");
				bluefor.setText("Lowest");
				greenfor.setText("Lower");
				yellowfor.setText("Middle");
				orangefor.setText("Higher");
				redfor.setText("Highest");
				repaint() ;

			}



			add(legend,"North");


//			status	panel
			status.setLayout( new GridLayout(1,1));
			status.add(showStatus);
			add(status,"Center");


		}
	}

	class DrawArea extends Panel {

		DrawPanel dp;

		int Scale;	// Scale of magnification or diminision; scale=dim/Max
		int Trans;	// translation
		int dim;      // size of the DrawArea,, which is equal to the number of pixes of the draw area
		int radius;   //Radius of circle that represents a node
		Dimension d;  //Current Dimension of the DrawArea (dynamic variable)
		Dimension sd;
		int Max;   // Maximum number of cells

		int n;
		int currentYear = 0;

		float c1,c2,c3,c4; //used to decide which color to use

		public DrawArea(DrawPanel dp) {
			this.dp = dp;

			setLayout(new BorderLayout() );
			add("South", dp );

		}


		void setMapVariables() {

			n = (int)vp.variables[22] +1;
			Max = nd.Max;
			sd = getToolkit().getScreenSize();
			//System.out.print(sd.width +"\t"+sd.height);
			d=getSize() ;

			//System.out.println(" Dimension of the DrawArea: width =  "+d.width + "  height = " + d.height );

			dim = (int)    (      (d.width<d.height) ? (0.90*d.width) : (0.90*d.height)        );

			//System.out.println("dim = "+ dim);

			if(Max != 0){
				Scale = (int)(dim/Max);
			} else {
				System.out.println("From DrawArea class Max variable is 0. Erorr!!!!!");
				Scale = 2;
			}
			if(Scale == 0)
				Scale = 1;

			Trans = (int) (0.05*dim);

			radius = (int) (Scale);

			if(radius == 0)
				radius = 1;
			//System.out.println("Trans = "+Trans+";  radius = "+ radius);
			//System.out.println("End of setScale()!!!!!");

		}


//// network will be drawn for the current year
		private void drawLinks_Speed(Graphics g) {

			float min, max;


//read speed/volume data into the matrix f
			FloatStack  f[] = null;
			if(evolved) {
				if(  drawSpeeds)
					f = nd.Speed[currentYear];
				else {
					//if( currentYear == n-1 )
					//	f = nd.Volume[n-2];
					//else
						f = nd.Volume[currentYear];
				}
			} else

				f = nd.dg.Speed;


			float temp = 0;
			min = 10;
			max =  1 ;
			for(int i=0; i<nd.dg.Vertices(); i++) {
				for(int j=0; j<nd.dg.NoofLinks(i+1); j++) {
					temp = f[i].access(j);

					if( max < temp)
						max = temp;
					if( min > temp)
						min = temp;
				}
			}
			//System.out.println ("max="+max+"; min="+min+"\n");


			int xcoord[] = new int[5];
			int ycoord[] = new int[5];
			float factor;

			for(int i =0; i<nd.dg.Vertices(); i++) {
				for(int j=0; j<nd.dg.NoofLinks(i+1); j++) {
					factor  = (float)(0.5*f[i].access(j) );
					int startx, starty, endx, endy;
					startx = Trans+(int)(Scale/2) + (int)(nd.dg.XCoordinate(i+1)*Scale);
					starty =   Trans- (int)(Scale/2)+ (int)(Scale*Max) - (int)(nd.dg.YCoordinate(i+1)*Scale);
					int k = nd.dg.EndNodeNumbers(i+1, j+1);
					endx = Trans+(int)(Scale/2) + (int)(nd.dg.XCoordinate(k)*Scale);
					endy =  Trans - (int)(Scale/2)+(int) (Scale* Max) - (int)(nd.dg.YCoordinate(k)*Scale);


				if (dp.scale .getSelectedItem() =="Absolute"){
					///absolute scale
					if(drawSpeeds)
						{c1=5;c2=10;c3=15;c4=20;}
					else
						{c1=1000;c2=2000;c3=3000;c4=4000;}
							 if( f[i].access( j ) <=  c1  ) {
								g.setColor(new Color(60, 100, 250) );  /////Blue
								//g.setColor(new Color(150, 150, 150) );
								factor = (float) (0.5*Scale);
								//count1++;
							}
							else if ( f[i].access( j ) <=  c2  ) {
								g.setColor(new Color(8, 140, 14) );   ////Green
								//g.setColor(new Color( 115, 115, 115) );
								factor = (float) (0.75*Scale);
								//count2++;
							}
							else if ( f[i].access( j ) <=  c3  ) {
								g.setColor(Color.yellow);    ////// Yellow
								//g.setColor(new Color(70, 70, 70) );
								factor = (float) (Scale);
								//count3++;
							}
							else if ( f[i].access( j ) <=  c4  ) {
								g.setColor(new Color(250, 125, 0));    ////// Oringe
								//g.setColor(new Color(70, 70, 70) );
								factor = (float) (Scale);
								//count3++;
							}
							else {
								g.setColor(new Color (200, 20, 20) );   //// Red
								//g.setColor(new Color(25, 25, 25) );
								factor = (float) (1.25*Scale);
								//count4++;
							}

				}
				else{
					////relative scale
							float step = (max-min)/5;

							if( f[i].access( j ) <=  min+step  ) {
							   g.setColor(new Color(60, 100, 250) );  /////Blue
							   //g.setColor(new Color(150, 150, 150) );sc
							   factor = (float) (0.5*Scale);
							   //count1++;
						   }
						   else if ( f[i].access( j ) <=  min+2*step  ) {
							   g.setColor(new Color(8, 140, 14) );   ////Green
							   //g.setColor(new Color( 115, 115, 115) );
							   factor = (float) (0.75*Scale);
							   //count2++;
						   }
						   else if ( f[i].access( j ) <=  min+3*step  ) {
							   g.setColor(Color.yellow);    ////// Yellow
							   //g.setColor(new Color(70, 70, 70) );
							   factor = (float) (Scale);
							   //count3++;
						   }
						   else if ( f[i].access( j ) <=  min+4*step  ) {
							   g.setColor(new Color(250, 125, 0));    ////// Oringe
							   //g.setColor(new Color(70, 70, 70) );
							   factor = (float) (Scale);
							   //count3++;
						   }
						   else {
							   g.setColor(new Color (200, 20, 20) );   //// Red
							   //g.setColor(new Color(25, 25, 25) );
							   factor = (float) (1.25*Scale);
							   //count4++;
						   }

				}


					int xerror, yerror;
					int x = endx - startx;
					int y = endy - starty;


					xerror = (int) (factor*y/Math.sqrt(x*x+y*y));
					yerror = (int)(-factor*x/Math.sqrt(x*x+y*y));

					int endxadd = endx+xerror, startxadd = startx+xerror;
					int endyadd = endy+yerror, startyadd = starty+yerror;

					xcoord[0] = startx-1;
					xcoord[1] = endx-1;
					xcoord[2] = endxadd;
					xcoord[3] = startxadd;
					xcoord[4] = startx-1;

					ycoord[0] = starty-1;
					ycoord[1] = endy-1;
					ycoord[2] = endyadd;
					ycoord[3] = startyadd;
					ycoord[4] = starty-1;


					g.fillPolygon(xcoord, ycoord, 5);
					g.setColor(Color.white);
					g.drawLine(startx, starty, endx, endy);

				}
			}

			//System.out.println("Current Year = "+currentYear +"*****Count = "+count1 + "  " + count2+ "  " + count3+ "  " + count4);

		}


		private void paintCells(Graphics g) {
			//int noOfLines;
			float sizeofcell;
			int sizeOfGrid;

			g.setColor(new Color(220, 220, 220) );

			sizeofcell = (Scale);
			sizeOfGrid = (int) (Scale * (Max));
			for(int i=1; i<=Max+1; i++) {
				g.drawLine(Trans, sizeOfGrid+(int)(Trans-(i-1)*sizeofcell), Trans+sizeOfGrid, sizeOfGrid+(int) (Trans-(i-1)*sizeofcell) );   /// draw lines parallel to x-axis
				g.drawLine((int)(Trans+(i-1)*sizeofcell),  Trans,  (int)(Trans+(i-1)*sizeofcell),  sizeOfGrid+Trans);
			}

		}

		private void paintDG(Graphics g) {
			////  Draw Speed boxes
			g.setColor(Color.black);
			drawLinks_Speed(g);

			///// Draw Nodes
			for(int i = 0; i< nd.dg.Vertices(); i++) {
				g.setColor(Color.black);
				int newx, newy;
				newx = (int)(Scale/2)+Trans + (int)(nd.dg.XCoordinate(i+1)*Scale);
				newy  = (int)(Scale*Max)-(int)(Scale/2) - (int)(nd.dg.YCoordinate(i+1)*Scale) + Trans;
				g.fillOval(newx-(int)(radius/2) , newy-(int)(radius/2), radius, radius);
			}

		}



		public void paint(Graphics g) {

			if  (graphRead) {
				paintCells(g);
				paintDG(g);
			}

		}



	}


///scrollPanel is used to define the scroll bars embeded in the variablePanel
	class ScrollPanel extends Panel implements AdjustmentListener{

		public double value;
		double maxvalue;
		double minvalue;
		int x;
		int y;
		int index;
		Label lvalue=new Label("");
		JScrollBar sb=new JScrollBar(JScrollBar.HORIZONTAL,0,1,0,101);

		public ScrollPanel(double minvalue, double maxvalue,double defaultvalue,int index){

		setLayout(new GridBagLayout());
		value =defaultvalue;
		this.maxvalue =maxvalue;
		this.minvalue=minvalue;
		this.index=index;
		
		if(index==22)lvalue=new Label(Integer.toString((int)defaultvalue));	
		else if(index==99||index==100)lvalue.setText(Integer.toString((int) (100*defaultvalue))+"%");
		else lvalue=new Label(Double.toString(defaultvalue));
		
		sb.setValue ((int)Math.round(100*(defaultvalue-minvalue)/(maxvalue-minvalue)));

		sb.addAdjustmentListener( this);
		}


		public void adjustmentValueChanged(AdjustmentEvent ame){
		Object obj=ame.getSource() ;
		int arg=ame.getAdjustmentType() ;

			if(obj.equals(this.sb)){
				if(arg==AdjustmentEvent.TRACK){
					value=minvalue+(maxvalue-minvalue)*(double)sb.getValue()/100.0;
				}
				else if(arg==AdjustmentEvent.UNIT_INCREMENT){
					value+=(double)(maxvalue-minvalue) /100.0;
					}
				else if(arg==AdjustmentEvent.UNIT_DECREMENT){
					value-=(double)(maxvalue-minvalue) /100.0;
					}
				else if(arg==AdjustmentEvent.BLOCK_INCREMENT){
					value+=(double)(maxvalue-minvalue) /10.0;
					}
				else if(arg==AdjustmentEvent.BLOCK_DECREMENT){
					value-=(double)(maxvalue-minvalue) /10.0;
					}
			}

			////some limitations by the model
			//the toll rate can't be zero, or the revenue will be zero
			if(index==13)
				{
					if (value==0.0)value+=(double)(maxvalue-minvalue) /100.0;
				}

	/////update corresponding vp.variables[]...			
			value=Math.round(value*100.0)/100.0;
			if(index<90){
				vp.variables[index]=(float)value;
				lvalue.setText(Double.toString( value));
			}
			else if(index==99){
				if(vp.speed .getSelectedItem() .equals("Uniform")){
					vp.variables[0]=5*(float)value;
					vp.variables[1]=5*(float)value;
				}
				else if(vp.speed .getSelectedItem() .equals("Random")){
					vp.variables[0]=1*(float)value;
					vp.variables[1]=10*(float)value;
				}
				else if(vp.speed .getSelectedItem() .equals("Prespecified Random")){
					if(vp.network.getSelectedItem() .equals("5X5 Grid Network")){vp.variables[0]=vp.variables[1]=-5;}
					else if(vp.network.getSelectedItem() .equals("10X10 Grid Network")){vp.variables[0]=vp.variables[1]=-10;}
					else if(vp.network.getSelectedItem() .equals("15X15 Grid Network")){vp.variables[0]=vp.variables[1]=-15;}
					else if(vp.network.getSelectedItem() .equals("20X20 Grid Network")){vp.variables[0]=vp.variables[1]=-20;}
					else if(vp.network.getSelectedItem() .equals("A  Network  with  River")){vp.variables[0]=vp.variables[1]=-99;}
				}
				lvalue.setText(Integer.toString((int) (100*value))+"%");
			}
			else if(index==100){
				if(vp.landuse.getSelectedItem() .equals("Uniform")){
					vp.variables[3]=5*(float)value;
					vp.variables[4]=5*(float)value;
				}
				else if(vp.landuse .getSelectedItem() .equals("Random")||vp.landuse .getSelectedItem() .equals("Downtown")){
					vp.variables[3]=1*(float)value;
					vp.variables[4]=10*(float)value;
				}
				else if(vp.landuse .getSelectedItem() .equals("Prespecified Random")){
					if(vp.network.getSelectedItem() .equals("5X5 Grid Network")){vp.variables[3]=vp.variables[4]=-5;}
					else if(vp.network.getSelectedItem() .equals("10X10 Grid Network")){vp.variables[3]=vp.variables[4]=-10;}
					else if(vp.network.getSelectedItem() .equals("15X15 Grid Network")){vp.variables[3]=vp.variables[4]=-15;}
					else if(vp.network.getSelectedItem() .equals("20X20 Grid Network")){vp.variables[3]=vp.variables[4]=-20;}
					else if(vp.network.getSelectedItem() .equals("A  Network  with  River")){vp.variables[3]=vp.variables[4]=-99;}
				}	
				lvalue.setText(Integer.toString((int) (100*value))+"%");
			}
	///////
			/*if(index==22)
				{
					if(value-(int)value==0.0)lvalue.setText(Integer.toString( (int)value));
					else {int tempvalue=(int)value+1;lvalue.setText(Integer.toString((int)value));} 
				}
			*/
			this.repaint() ;


/////any changes in scroll bars will repaint the network

			//reset right-hand panel	
			dp.scale.select( "Absolute");		
			dp.whichAttribute.select ("Speed");
			drawSpeeds=true;
				
			dp.bluefor.setText("0~" + Integer.toString(5));
			dp.greenfor.setText(Integer.toString(5) +"~" + Integer.toString(10));
			dp.yellowfor.setText(Integer.toString(10)+"~" + Integer.toString(15));
			dp.orangefor.setText(Integer.toString(15) +"~"+ Integer.toString(20));
			dp.redfor.setText(Integer.toString(20) +"~"+ "  ");
			
			//all variables in the right-hand panel, except "evolve" are set disabled			
			dp.first.setEnabled(false);
			dp.previous .setEnabled(false);
			dp.next .setEnabled(false) ;
			dp.last.setEnabled(false) ;
			dp.statistics .setEnabled( false);
			dp.whichAttribute.setEnabled(false) ;
			dp.scale .setEnabled( false);
			da.dp.evolve .setEnabled( true);
			
			writeVariables();
			//System.out.print("ScrollBars Changed,writeVariables:\n");
			//for(int i=0;i<23;i++){
			//	System.out.print(i+"\t"+vp.variables [i]+"\n");
			//}



			if(vp.network.getSelectedItem() .equals(" "))
				{dp.evolve.setEnabled(false);}
			else if(vp.network.getSelectedItem() .equals("10X10 Grid Network")){
				dp.showStatus.setText("10X10 Network Loaded...");
				dp.evolve.setEnabled(true) ;
				dp.statistics .setEnabled(false);
				currentInputFile = "Grid10.txt";
				getnetwork="10X10 Grid Network" ;
				try {
					nd = new NetworkDynamics( vp.variables,url, currentInputFile);
				} catch (IOException e) {
				}

				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			}
			else if(vp.network.getSelectedItem() .equals("15X15 Grid Network")){
				dp.showStatus.setText("15X15 Network Loaded...");
				dp.evolve.setEnabled(true) ;
				dp.statistics .setEnabled(false);
				currentInputFile = "Grid15.txt";
				getnetwork="15X15 Grid Network" ;
				try {
					nd = new NetworkDynamics( vp.variables, url, currentInputFile);
				} catch (IOException e) {
				}

				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			}
			else if(vp.network.getSelectedItem().equals("20X20 Grid Network")){
				dp.showStatus.setText("20X20 Network Loaded...");
				dp.evolve.setEnabled(true) ;
				dp.statistics .setEnabled(false);
				currentInputFile = "Grid20.txt";
				getnetwork="20X20 Grid Network" ;
			try {
					nd = new NetworkDynamics( vp.variables, url, currentInputFile);
				} catch (IOException e) {
				}
				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			}

			else if(vp.network.getSelectedItem() .equals("5X5 Grid Network")){
				dp.showStatus.setText("5X5 Network Loaded...");
				dp.evolve.setEnabled(true) ;
				dp.statistics .setEnabled(false);
				currentInputFile = "Grid5.txt";
				getnetwork="5X5 Grid Network" ;
			try {
					nd = new NetworkDynamics( vp.variables, url, currentInputFile);
				} catch (IOException e) {
				}
				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				da.repaint();
			}

			else if(vp.network.getSelectedItem().equals("A  Network  with  River")){
				dp.showStatus.setText("A  Network  with  River Loaded...");
				dp.evolve.setEnabled(true) ;
				dp.statistics .setEnabled(false);
				currentInputFile = "River.txt";
				getnetwork="A  Network  with  River" ;
				try {
					nd = new NetworkDynamics( vp.variables,url, currentInputFile);
				} catch (IOException e) {
				}

				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			}
		//////////////////////



		}

		public float value(){
			return (float)value;
		}

	}


	class VariablesPanel extends Panel implements ActionListener, ItemListener  {

		float variables[] = new float[23];
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints constraints=new GridBagConstraints();
		Label temp,temp2;

		Panel help=new Panel();
		Button forhelp=new Button();
		Button restore=new Button();
		Choice network=new Choice();
		Choice speed=new Choice();

		Choice landuse=new Choice();

		Choice investment=new Choice();

		Choice pricing=new Choice();

		ScrollPanel v6,v13,v10,v14,v15,v17,v18,v19,v20,v99,v100;
		//// Constructor
		public VariablesPanel() {

			defaultVars();

			//setSize(250,1000);
			setLayout(gbl);

			help.setLayout(new FlowLayout() );
			forhelp=new Button("?");
			forhelp.addActionListener( this);
			forhelp.setFont( new Font("",Font.BOLD ,11));

			help.add(temp=new Label("--Please click"));
			temp.setAlignment( Label.RIGHT );
			temp.setFont(new Font("",Font.PLAIN|Font.ITALIC|Font.BOLD ,11));
			temp.setForeground( Color.black );
			help.add(forhelp);
			help.add(temp2=new Label("for HELP!--"));
			temp2.setFont(new Font("",Font.PLAIN|Font.ITALIC|Font.BOLD ,11));
			temp2.setForeground( Color.black  );

			restore=new Button("Restore");
			restore.addActionListener( this);

			constraints.weightx =1.0;
			constraints.weighty=1.0;

			constraints.anchor=GridBagConstraints.WEST;
			constraints.fill=GridBagConstraints.HORIZONTAL ;
			//addComponent(0,0,4,1,help);

			addComponent(0,1,2,1,temp=new Label("0. Network Type"));
			temp.setFont(new Font("",Font.BOLD,13));

			//network.addItem(" ");
			network.addItem("5X5 Grid Network");
			network.addItem("10X10 Grid Network");
			network.addItem("15X15 Grid Network");
			network.addItem("20X20 Grid Network");
			network.addItem("A  Network  with  River");

			addComponent(2,1,2,1,network);
			network.addItemListener( this);


			addComponent(0,2,2,1,temp=new Label("1. Speed Distribution"));
			temp.setFont(new Font("",Font.BOLD,13));



			speed.addItem("Uniform");
			speed.addItem("Random");
			speed.addItem("Prespecified Random");
			addComponent(2,2,2,1,speed);
			speed.select( "Uniform");
			speed.addItemListener(this);

			v99=new ScrollPanel(0.2,2.2,1,99);
			addComponent(0,3,1,1,temp=new Label("     Speed Multiplier:"));
			addComponent(2,3,2,1,v99.sb);
			addComponent(1,3,1,1,v99.lvalue );
			v99.lvalue .setAlignment( Label.RIGHT );


			addComponent(0,4,2,1,temp=new Label("2. Land use Distribution"));
			temp.setFont(new Font("",Font.BOLD,13));

			landuse.addItem("Uniform");
			landuse.addItem("Random");
			landuse.addItem("Downtown");
			landuse.addItem("Prespecified Random");

			addComponent(2,4,2,1,landuse);
			landuse.select("Uniform");
			landuse.addItemListener(this);

			v100=new ScrollPanel(0.2,2.2,1,100);
			addComponent(0,5,1,1,temp=new Label("     Land Use Multiplier:"));
			addComponent(2,5,2,1,v100.sb);
			addComponent(1,5,1,1,v100.lvalue );
			v100.lvalue .setAlignment( Label.RIGHT );

			//addComponent(0,4,2,1,temp=new Label("3. Investment Rules"));
			//temp.setFont(new Font("",Font.BOLD,13));

			investment.addItem("Self Consumption");
			investment.addItem("Revenue         Sharing");
			//addComponent(2,4,2,1,investment);
			investment.select("Self Consumption");
			investment.addItemListener(this);
			investment.setEnabled(false) ;

			//addComponent(0,5,2,1,temp=new Label("4. Pricing Policies"));
			//temp.setFont(new Font("",Font.BOLD,13));

			pricing.addItem("Decreasing Toll");
			pricing.addItem("Decreasing    Parking");
			//addComponent(2,5,2,1,pricing);
			pricing.select("Decreasing Toll");
			pricing.addItemListener(this);
			pricing.setEnabled(false);


			
			constraints.fill=GridBagConstraints.HORIZONTAL ;
			constraints.anchor=GridBagConstraints.WEST;
			addComponent(0,7,2,1,temp=new Label("3. Travel Demand Model"));
			temp.setFont(new Font("",Font.BOLD,13));
			addComponent(2,7,2,1,temp=new Label(""));

			v6=new ScrollPanel(0,5,1,6);
			addComponent(0,8,1,1,new Label("     3.1 Value of time"));
			addComponent(2,8,2,1,v6.sb);
			addComponent(1,8,1,1,v6.lvalue);
			v6.lvalue .setAlignment( Label.RIGHT );

			v10=new ScrollPanel(0,1,0.01,10);
			addComponent(0,9,1,1,new Label("     3.2 Friction factor"));
			addComponent(2,9,2,1,v10.sb);
			addComponent(1,9,1,1,v10.lvalue);
			v10.lvalue .setAlignment( Label.RIGHT );

			addComponent(0,11,4,1,temp=new Label("4. Revenue Model"));
			temp.setFont(new Font("",Font.BOLD,13));

			v13=new ScrollPanel(0.5,1.5,1,13);
			addComponent(0,12,1,1,new Label("     4.1 Toll rate"));
			addComponent(2,12,2,1,v13.sb);
			addComponent(1,12,1,1,v13.lvalue);
			v13.lvalue .setAlignment( Label.RIGHT );
			
			v14=new ScrollPanel(0,1.5,1,14);
			addComponent(0,13,1,1,new Label("     4.2 Coeff. of length"));
			addComponent(2,13,2,1,v14.sb);
			addComponent(1,13,1,1,v14.lvalue);
			v14.lvalue .setAlignment( Label.RIGHT );
			

			v15=new ScrollPanel(0,1,0,15);
			addComponent(0,14,1,1,new Label("     4.3 Coeff. of speed"));
			addComponent(2,14,2,1,v15.sb);
			addComponent(1,14,1,1,v15.lvalue);
			v15.lvalue .setAlignment( Label.RIGHT );
			
			addComponent(0,17,4,1,temp=new Label("5. Cost Model"));
			temp.setFont(new Font("",Font.BOLD,13));

			v17=new ScrollPanel(0,1.2,1,17);
			addComponent(0,18,1,1,new Label("     5.1 Coeff. of length"));
			addComponent(2,18,2,1,v17.sb);
			addComponent(1,18,1,1,v17.lvalue);
			v17.lvalue .setAlignment( Label.RIGHT );
			

			v18=new ScrollPanel(0,1.2,0.75,18);
			addComponent(0,19,1,1,new Label("     5.2 Coeff. of flow"));
			addComponent(2,19,2,1,v18.sb);
			addComponent(1,19,1,1,v18.lvalue);
			v18.lvalue .setAlignment( Label.RIGHT );
			

			v19=new ScrollPanel(0,1.2,0.75,19);
			addComponent(0,20,1,1,new Label("     5.3 Coeff. of speed"));
			addComponent(2,20,2,1,v19.sb);
			addComponent(1,20,1,1,v19.lvalue);
			v19.lvalue .setAlignment( Label.RIGHT );
			

			addComponent(0,22,4,1,temp=new Label("6. Investment Model"));
			temp.setFont(new Font("",Font.BOLD,13));

			v20=new ScrollPanel(0,1,1,20);
			addComponent(0,23,1,1,new Label("     6.1 Speed impr coeff."));
			addComponent(2,23,2,1,v20.sb);
			addComponent(1,23,1,1,v20.lvalue);
			v20.lvalue .setAlignment( Label.RIGHT );
			
			constraints.fill=GridBagConstraints.NONE;
			constraints.anchor=GridBagConstraints.EAST;		
			addComponent(0,24,1,1,restore);
			addComponent(3,24,1,1,temp=new Label(""));
			//addComponent(0,23,2,1,new Label(""));
			//addComponent(2,23,2,1,new Label(""));

			//addComponent(0,23,2,1,new Label(""));
			//addComponent(2,23,2,1,restore);

			//addComponent(0,25,2,1,new Label(""));
			//addComponent(2,25,2,1,new Label(""));



		}

		public void addComponent(int x, int y, int w, int h, Component c)
		{
		constraints.gridx=x;
			constraints.gridy=y;
			constraints.gridwidth=w;
			constraints.gridheight=h;

			gbl.setConstraints( c,constraints);

			add(c);
		}



		void defaultVars() {

			variables[0] = (float) 5;	//speedmin
			variables[1] = (float) 5;	//speedmax
			variables[2] = (float) 0;  ///
			variables[3] = (float) 10;	//landmin
			variables[4] = (float) 10;	//landmax
			variables[5] = (float) 0.0;   //// downtown?
			variables[6] = (float) 1.0;		//volue of time
			variables[7] = (float) 1.0;		//tax rate
			variables[8] = (float) 1.0;		//length rate
			variables[9] = (float) 0.0;		//speed rate
			variables[10] = (float) 0.01;	//friction factor
			variables[11] = (float) 1;	//symmetry?
			variables[12] = (float) 1;	//avg speed?
			variables[13] = (float) 1.0; //tax rate(toll rate)
			variables[14] = (float) 1.0; //length
			variables[15] = (float) 0;	//speed
			variables[16] = (float) 365;	//cost rate
			variables[17] = (float) 1.0;	//length coefficient
			variables[18] = (float) 0.75;	//flow coefficient
			variables[19] = (float) 0.75;	// speed coefficient
			variables[20] = (float) 1.0;	//speed reduction factor
			variables[21] = (float) 0;	//X
			variables[22] = (float) 20;	//time period

		}




		public void actionPerformed( ActionEvent ae) {
			String arg=(String) ae.getActionCommand();
			Object obj = ae.getSource();

			if(obj==forhelp){
				try { helpurl=new URL(url,"HelpFileSONG1.0.htm");

				 }

				catch (MalformedURLException e) {

				  System.out.println("Bad URL:" + helpurl);

				 }
				getAppletContext().showDocument(helpurl,"_blank");

			}

			else if(obj==restore){
				v99.value =(float)1;
				v99.lvalue.setText ("100%");
				v99.sb.setValue ((int)Math.round(100*(1.0-v99.minvalue)/(v99.maxvalue-vp.v99.minvalue)));

				v100.value =(float)1;
				v100.lvalue.setText ("100%");
				v100.sb.setValue ((int)Math.round(100*(1.0-v100.minvalue)/(v100.maxvalue-vp.v100.minvalue)));
				
				v6.value =vp.variables [6]=1;
				v6.lvalue.setText (Double.toString(1.0));
				v6.sb.setValue ((int)Math.round(100*(1.0-v6.minvalue)/(v6.maxvalue-vp.v6.minvalue)));

				v10.value =vp.variables [10]=(float)0.01;
				v10.lvalue.setText (Double.toString(0.01));
				v10.sb.setValue ((int)Math.round(100*(0.01-v10.minvalue)/(v10.maxvalue-vp.v10.minvalue)));

				v13.value =vp.variables [13]=1;
				vp.v13.lvalue.setText(Double.toString(1.0));
				vp.v13.sb.setValue ((int)Math.round(100*(1.0-v13.minvalue)/(v13.maxvalue-vp.v13.minvalue)));
				vp.v13.repaint() ;

				v14.value =vp.variables [14]=1;
				v14.lvalue.setText(Double.toString(1.0));
				v14.sb.setValue ((int)Math.round(100*(1.0-v14.minvalue)/(v14.maxvalue-v14.minvalue)));

				v15.value =vp.variables [15]=0;
				v15.lvalue.setText(Double.toString(0.0));
				v15.sb.setValue ((int)Math.round(100*(0.0-v15.minvalue)/(v15.maxvalue-v15.minvalue)));

				v17.value =vp.variables [17]=1;
				v17.lvalue.setText(Double.toString(1.0));
				v17.sb.setValue ((int)Math.round(100*(1.0-v17.minvalue)/(v17.maxvalue-v17.minvalue)));

				v18.value =vp.variables [18]=(float)0.75;
				v18.lvalue.setText(Double.toString(0.75));
				v18.sb.setValue ((int)Math.round(100*(0.75-v18.minvalue)/(v18.maxvalue-v18.minvalue)));

				v19.value =vp.variables [19]=(float)0.75;
				v19.lvalue.setText(Double.toString(0.75));
				v19.sb.setValue ((int)Math.round(100*(0.75-v19.minvalue)/(v19.maxvalue-v19.minvalue)));

				v20.value =vp.variables [20]=1;
				v20.lvalue.setText(Double.toString(1.0));
				v20.sb.setValue ((int)Math.round(100*(1.0-v20.minvalue)/(v20.maxvalue-v20.minvalue)));

	//reset right-hand panel	
				da.dp.scale.select( "Absolute");		
				da.dp.whichAttribute.select ("Speed");
				drawSpeeds=true;
				
				da.dp.bluefor.setText("0~" + Integer.toString(5));
				da.dp.greenfor.setText(Integer.toString(5) +"~" + Integer.toString(10));
				da.dp.yellowfor.setText(Integer.toString(10)+"~" + Integer.toString(15));
				da.dp.orangefor.setText(Integer.toString(15) +"~"+ Integer.toString(20));
				da.dp.redfor.setText(Integer.toString(20) +"~"+ "  ");
			
				//all variables in the right-hand panel, except "evolve" are set disabled			
				da.dp.first.setEnabled(false);
				da.dp.previous .setEnabled(false);
				da.dp.next .setEnabled(false) ;
				da.dp.last.setEnabled(false) ;
				da.dp.statistics .setEnabled( false);
				da.dp.whichAttribute.setEnabled(false) ;
				da.dp.scale .setEnabled( false);
				da.dp.evolve .setEnabled( true);

	///reload the network
	////
				if(vp.network.getSelectedItem() .equals(" "))
					{dp.evolve.setEnabled(false);}
				else if(vp.network.getSelectedItem() .equals("10X10 Grid Network")){
					dp.showStatus.setText("10X10 Network Loaded...");
					dp.evolve.setEnabled(true) ;
					dp.statistics .setEnabled(false);
					currentInputFile = "Grid10.txt";
					getnetwork="10X10 Grid Network" ;
					try {
						nd = new NetworkDynamics( variables,url, currentInputFile);
					} catch (IOException e) {
					}

				
					da.setMapVariables();
					graphRead = true;
					evolved = false;
					da.currentYear = 0;
					da.dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
					da.repaint();
				}
				else if(vp.network.getSelectedItem() .equals("15X15 Grid Network")){
					da.dp.showStatus.setText("15X15 Network Loaded...");
					da.dp.evolve.setEnabled(true) ;
					da.dp.statistics .setEnabled(false);
					currentInputFile = "Grid15.txt";
					getnetwork="15X15 Grid Network" ;
					try {
						nd = new NetworkDynamics( variables, url, currentInputFile);
					} catch (IOException e) {
					}

					da.setMapVariables();
					graphRead = true;
					evolved = false;
					da.currentYear = 0;
					da.dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
					da.repaint();
				}
				else if(vp.network.getSelectedItem().equals("20X20 Grid Network")){
					da.dp.showStatus.setText("20X20 Network Loaded...");
					da.dp.evolve.setEnabled(true) ;
					da.dp.statistics .setEnabled(false);
					currentInputFile = "Grid20.txt";
					getnetwork="20X20 Grid Network" ;
				try {
						nd = new NetworkDynamics( variables, url, currentInputFile);
					} catch (IOException e) {
					}
					da.setMapVariables();
					graphRead = true;
					evolved = false;
					da.currentYear = 0;
					dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
					da.repaint();
				}

				else if(vp.network.getSelectedItem() .equals("5X5 Grid Network")){
					dp.showStatus.setText("5X5 Network Loaded...");
					dp.evolve.setEnabled(true) ;
					dp.statistics .setEnabled(false);
					currentInputFile = "Grid5.txt";
					getnetwork="5X5 Grid Network" ;
				try {
						nd = new NetworkDynamics( variables, url, currentInputFile);
					} catch (IOException e) {
					}
					da.setMapVariables();
					graphRead = true;
					evolved = false;
					da.currentYear = 0;
					dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
					da.repaint();
				}

				else if(vp.network.getSelectedItem().equals("A  Network  with  River")){
					dp.showStatus.setText("A  Network  with  River Loaded...");
					dp.evolve.setEnabled(true) ;
					dp.statistics .setEnabled(false);
					currentInputFile = "River.txt";
					getnetwork="A  Network  with  River" ;
					try {
						nd = new NetworkDynamics( variables,url, currentInputFile);
					} catch (IOException e) {
					}

					da.setMapVariables();
					graphRead = true;
					evolved = false;
					da.currentYear = 0;
					dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
					da.repaint();
				}

				writeVariables();
				//System.out.print("command restore,writeVariables:\n");
				//for(int i=0;i<23;i++){
				//	System.out.print(i+"\t"+vp.variables [i]+"\n");
				//}

			}
		}

		public void itemStateChanged( ItemEvent ie) {
			String arg=(String) ie.getItem();
			Object obj=ie.getSource();

			//reset right-hand panel	
			da.dp.scale.select( "Absolute");		
			da.dp.whichAttribute.select ("Speed");
			drawSpeeds=true;
				
			da.dp.bluefor.setText("0~" + Integer.toString(5));
			da.dp.greenfor.setText(Integer.toString(5) +"~" + Integer.toString(10));
			da.dp.yellowfor.setText(Integer.toString(10)+"~" + Integer.toString(15));
			da.dp.orangefor.setText(Integer.toString(15) +"~"+ Integer.toString(20));
			da.dp.redfor.setText(Integer.toString(20) +"~"+ "  ");
			
			//all variables in the right-hand panel, except "evolve" are set disabled			
			da.dp.first.setEnabled(false);
			da.dp.previous .setEnabled(false);
			da.dp.next .setEnabled(false) ;
			da.dp.last.setEnabled(false) ;
			da.dp.statistics .setEnabled( false);
			da.dp.whichAttribute.setEnabled(false) ;
			da.dp.scale .setEnabled( false);
			da.dp.evolve .setEnabled( true);


		/////Network
			if(obj.equals(vp.network)){
				if(arg.equals(" "))
					{dp.evolve.setEnabled(false);}
				else if(arg.equals("10X10 Grid Network")){
					dp.showStatus.setText("10X10 Network Loaded...");
					dp.evolve.setEnabled(true) ;
					dp.statistics .setEnabled(false);
					currentInputFile = "Grid10.txt";
					getnetwork="10X10 Grid Network" ;
					if(vp.speed .getSelectedItem() =="Prespecified Random"){variables[0]=variables[1]=-10;}
					if(vp.landuse .getSelectedItem() =="Prespecified Random"){variables[3]=variables[4]=-10;}

					try {
						nd = new NetworkDynamics( variables,url, currentInputFile);
					} catch (IOException e) {
					}

					da.setMapVariables();
					graphRead = true;
					evolved = false;
					da.currentYear = 0;
					dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
					da.repaint();
				}
				else if(arg.equals("15X15 Grid Network")){
					dp.showStatus.setText("15X15 Network Loaded...");
					dp.evolve.setEnabled(true) ;
					dp.statistics .setEnabled(false);
					currentInputFile = "Grid15.txt";
					getnetwork="15X15 Grid Network" ;
					if(vp.speed .getSelectedItem() =="Prespecified Random"){variables[0]=variables[1]=-15;}
					if(vp.landuse .getSelectedItem() =="Prespecified Random"){variables[3]=variables[4]=-15;}

					try {
						nd = new NetworkDynamics( variables, url, currentInputFile);
					} catch (IOException e) {
					}

					da.setMapVariables();
					graphRead = true;
					evolved = false;
					da.currentYear = 0;
					dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
					da.repaint();
				}
				else if(arg.equals("20X20 Grid Network")){
					dp.showStatus.setText("20X20 Network Loaded...");
					dp.evolve.setEnabled(true) ;
					dp.statistics .setEnabled(false);
					currentInputFile = "Grid20.txt";
					getnetwork="20X20 Grid Network" ;
					if(vp.speed .getSelectedItem() =="Prespecified Random"){variables[0]=variables[1]=-20;}
					if(vp.landuse .getSelectedItem() =="Prespecified Random"){variables[3]=variables[4]=-20;}

				try {
						nd = new NetworkDynamics( variables, url, currentInputFile);
					} catch (IOException e) {
					}
					da.setMapVariables();
					graphRead = true;
					evolved = false;
					da.currentYear = 0;
					dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
					da.repaint();
				}

				else if(arg.equals("5X5 Grid Network")){
					dp.showStatus.setText("5X5 Network Loaded...");
					dp.evolve.setEnabled(true) ;
					dp.statistics .setEnabled(false);
					currentInputFile = "Grid5.txt";
					getnetwork="5X5 Grid Network" ;
					if(vp.speed .getSelectedItem() =="Prespecified Random"){variables[0]=variables[1]=-5;}
					if(vp.landuse .getSelectedItem() =="Prespecified Random"){variables[3]=variables[4]=-5;}

				try {
						nd = new NetworkDynamics( variables, url, currentInputFile);
					} catch (IOException e) {
					}
					da.setMapVariables();
					graphRead = true;
					evolved = false;
					da.currentYear = 0;
					dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
					da.repaint();
				}

				else if(arg.equals("A  Network  with  River")){
					dp.showStatus.setText("A  Network  with  River Loaded...");
					dp.evolve.setEnabled(true) ;
					dp.statistics .setEnabled(false);
					currentInputFile = "River.txt";
					getnetwork="A  Network  with  River" ;
					if(vp.speed .getSelectedItem() =="Prespecified Random"){variables[0]=variables[1]=-99;}
					if(vp.landuse .getSelectedItem() =="Prespecified Random"){variables[3]=variables[4]=-99;}

					try {
						nd = new NetworkDynamics( variables,url, currentInputFile);
					} catch (IOException e) {
					}

					da.setMapVariables();
					graphRead = true;
					evolved = false;
					da.currentYear = 0;
					dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
					da.repaint();
				}
			}

			else{

				/// pull-down box Speed
				if(obj.equals(vp.speed)){

					if(arg.equals("Uniform")){
						vp.v99.sb.setEnabled( true);
						vp.variables[0]=5;
						vp.variables[1]=5;
					}
					else if(arg.equals("Random")){
						vp.v99.sb.setEnabled( true);
						vp.variables[0]=0;
						vp.variables[1]=10;
					}
					else if(arg.equals("Prespecified Random")){						
						vp.v99.value =(float)1;
						vp.v99.lvalue.setText ("100%");
						vp.v99.sb.setValue ((int)Math.round(100*(1.0-vp.v99.minvalue )/(vp.v99.maxvalue -vp.v99.minvalue )));
						vp.v99.sb.setEnabled( false);
						
						if(getnetwork.equals("5X5 Grid Network")){variables[0]=variables[1]=-5;}
						else if(getnetwork.equals("10X10 Grid Network")){variables[0]=variables[1]=-10;}
						else if(getnetwork.equals("15X15 Grid Network")){variables[0]=variables[1]=-15;}
						else if(getnetwork.equals("20X20 Grid Network")){variables[0]=variables[1]=-20;}
						else if(getnetwork.equals("A  Network  with  River")){variables[0]=variables[1]=-99;}
					}
				}

				///pull-down
				if(obj.equals(vp.landuse)){


					if(arg.equals("Uniform")){
						vp.v100.sb.setEnabled( true);
						variables[3]=10;
						variables[4]=10;
						variables[5]=(float)0.0;
						}
					else if(arg.equals("Random")){
						vp.v100.sb.setEnabled( true);
						variables[3]=5;
						variables[4]=15;
						variables[5]=(float)0.0;
						}
					else if(arg.equals("Downtown")){
						vp.v100.sb.setEnabled( true);
						variables[3]=5;
						variables[4]=15;
						variables[5]=(float)1.0;
						}
					else if(arg.equals("Prespecified Random")){						
						vp.v100.value =(float)1;
						vp.v100.lvalue.setText ("100%");
						vp.v100.sb.setValue ((int)Math.round(100*(1.0-vp.v100.minvalue )/(vp.v100.maxvalue -vp.v100.minvalue)));
						vp.v100.sb.setEnabled(false);

						if(getnetwork.equals("5X5 Grid Network")){variables[3]=variables[4]=-5;}
						else if(getnetwork.equals("10X10 Grid Network")){variables[3]=variables[4]=-10;}
						else if(getnetwork.equals("15X15 Grid Network")){variables[3]=variables[4]=-15;}
						else if(getnetwork.equals("20X20 Grid Network")){variables[3]=variables[4]=-20;}
						else if(getnetwork.equals("A  Network  with  River")){variables[3]=variables[4]=-99;}
						variables[5]=(float)0.0;
					}

				}

			///others
				if(obj.equals(vp.investment)){}
				if(obj.equals(vp.pricing)){}


	////any changes in any pull-down boxes other than the network pull-down will also rapaint the network
			
			writeVariables();
			//System.out.print("Choices ItemChanged,writeVariables:\n");
			//for(int i=0;i<23;i++){
			//	System.out.print(i+"\t"+vp.variables [i]+"\n");
			//}

			////
			if(vp.network.getSelectedItem() .equals(" "))
				{dp.evolve.setEnabled(false);}
			else if(vp.network.getSelectedItem() .equals("10X10 Grid Network")){
				dp.showStatus.setText("10X10 Network Loaded...");
				dp.evolve.setEnabled(true) ;
				dp.statistics .setEnabled(false);
				currentInputFile = "Grid10.txt";
				getnetwork="10X10 Grid Network" ;
							
				try {
					nd = new NetworkDynamics( variables,url, currentInputFile,dp.sd );
				} catch (IOException e) {
				}

				
				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			
			}
			else if(vp.network.getSelectedItem() .equals("15X15 Grid Network")){
				dp.showStatus.setText("15X15 Network Loaded...");
				dp.evolve.setEnabled(true) ;
				dp.statistics .setEnabled(false);
				currentInputFile = "Grid15.txt";
				getnetwork="15X15 Grid Network" ;
				try {
					nd = new NetworkDynamics( variables, url, currentInputFile);
				} catch (IOException e) {
				}

				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			}
			else if(vp.network.getSelectedItem().equals("20X20 Grid Network")){
				dp.showStatus.setText("20X20 Network Loaded...");
				dp.evolve.setEnabled(true) ;
				dp.statistics .setEnabled(false);
				currentInputFile = "Grid20.txt";
				getnetwork="20X20 Grid Network" ;
			try {
					nd = new NetworkDynamics( variables, url, currentInputFile);
				} catch (IOException e) {
				}
				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			}

			else if(vp.network.getSelectedItem() .equals("5X5 Grid Network")){
				dp.showStatus.setText("5X5 Network Loaded...");
				dp.evolve.setEnabled(true) ;
				dp.statistics .setEnabled(false);
				currentInputFile = "Grid5.txt";
				getnetwork="5X5 Grid Network" ;
			try {
					nd = new NetworkDynamics( variables, url, currentInputFile);
				} catch (IOException e) {
				}
				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			}

			else if(vp.network.getSelectedItem().equals("A  Network  with  River")){
				dp.showStatus.setText("A  Network  with  River Loaded...");
				dp.evolve.setEnabled(true) ;
				dp.statistics .setEnabled(false);
				currentInputFile = "River.txt";
				getnetwork="A  Network  with  River" ;
				try {
					nd = new NetworkDynamics( variables,url, currentInputFile);
				} catch (IOException e) {
				}

				da.setMapVariables();
				graphRead = true;
				evolved = false;
				da.currentYear = 0;
				dp.year.setText( "   Year "+ Integer.toString( da.currentYear ) + "   " );
				da.repaint();
			}
		 }//end of else


		}///End of public void ()

	}
	////// End of class VariablesPanel



}

///////  End of Demo Class


