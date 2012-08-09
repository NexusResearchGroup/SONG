

import java.awt.*;




  class MenuFrame extends Frame {
  Demo demo;
	
	public MenuFrame(String title,  Demo demo) {
	  super(title);
	  this.demo = demo;
		
	  setLayout( new BorderLayout() );
		
/*	  MenuBar mbar = new MenuBar();
	  setMenuBar(mbar);
		
	  Menu file = new Menu("File");
		
	  Menu openFile = new Menu("New...");
		
	  MenuItem  circular, grid1, grid2, grid3, grid4, grid5;
	  //openFile.add(circular = new MenuItem("Circular Network"));
	  openFile.add(grid1 = new MenuItem("Twin Cities"));
	  openFile.add(grid2 = new MenuItem("10X10 nodes Grid Network"));
	  openFile.add(grid3 = new MenuItem("15X15 nodes Grid Network")); 
	  openFile.add(grid4 = new MenuItem("20X20 nodes Grid Network")); 
	  openFile.add(grid5 = new MenuItem("100X100 nodes Grid Network")); 
				
	  MenuItem item1 = new MenuItem("-");
	  MenuItem quit = new MenuItem("Quit");
		
	  file.add( openFile);
	  file.add(item1);
	  file.add(quit);
	  mbar.add(file);
		
	  Menu view = new Menu("View");
		
	  MenuItem graph = new MenuItem("Graph");
	  MenuItem speedDynamix = new MenuItem("Speed Dynamics");
		
	  view.add(graph);
	  view.add(speedDynamix);
	  mbar.add(view);
		
	  //circular.addActionListener(this.demo);
	  grid1.addActionListener(this.demo);
	  grid2.addActionListener(this.demo);
	  grid3.addActionListener(this.demo);
	  grid4.addActionListener(this.demo);
		
	  openFile.addActionListener(this.demo);
	  quit.addActionListener(this.demo);
		
	  graph.addActionListener(this.demo);
	  speedDynamix.addActionListener(this.demo);
*/		

	  }
	
		
	
}
///// End of MenyFrame class