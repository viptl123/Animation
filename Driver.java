import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.lang.*;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import javax.swing.Timer;
import java.util.TimerTask;
import java.io.File;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;




public class Driver implements ActionListener {
	
	// Declare class data
		ArrayList<TripPoint> trip;
		ArrayList<TripPoint> movingTrip;
		Panel panel;
		JButton play;
		JCheckBox stops;
		JMapViewer map;
		JComboBox animationTime;
		JFrame frame;
		
		
	
	public Driver() throws FileNotFoundException, IOException {
		TripPoint.readFile("triplog.csv");
    	TripPoint.h2StopDetection();
    	trip = TripPoint.getTrip();
    	movingTrip = TripPoint.getMovingTrip();
    	
    	
    	// Set up frame, include your name in the title
    	frame = new JFrame();
        frame.setTitle("Project 5 - Vishnu Patel");
        frame.setSize(1200, 800);
        
        // Set up Panel for input selections
        panel = new Panel();
    	
        // Play Button
        play = new JButton("Play");
        
    	
        // CheckBox to enable/disable stops
        stops = new JCheckBox("Include Stops");
    	stops.setBounds(100, 100, 50 ,50);
    	
        // ComboBox to pick animation time
        Integer[] times = {15, 30, 60, 90};
        animationTime = new JComboBox(times);
    	
        // Add all to top panel
       
        panel.add(animationTime);
        panel.add(stops);
        panel.add(play);
        frame.add(panel);
       
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set up mapViewer
        map = new JMapViewer();
        map.setTileSource(new OsmTileSource.TransportMap());
        frame.add(map);
        frame.setVisible(true);
        panel.setSize(1200,30);
        map.setDisplayPosition(new Coordinate(36, -100), 5);
     
        play.addActionListener(this);
        frame.setVisible(true);
	}
	

    public static void main(String[] args) throws FileNotFoundException, IOException  {

    	Driver driver = new Driver();
    }
   
    @Override
    public void actionPerformed(ActionEvent event) {
    	
    	

    	
    	  // Add listeners for GUI components
        
        boolean includeStops = false;
        if (stops.isSelected()) {
     	   includeStops = true;
        }
        Integer distanceTime = (Integer) animationTime.getSelectedItem();
   
        int time = distanceTime *1000;
        // divide by either trip size or moving trip size
    	ArrayList<TripPoint> use;
    	if (includeStops) {
    		use = trip;
    	}
    	else {
    		use = movingTrip;
    	}
    	time = time / use.size();
    	
        
    	Image image = null;
    	try {
    	image = ImageIO.read(new File("raccoon.png"));
    	}
    	catch (Exception e) {}
    	
    	
    	
    	this.drawings(time, use, image);
    	
  
    		
    	
    	
    }
   
   
    

    
    
    
  
      public void drawings(int delay, ArrayList<TripPoint> trip, Image image) {
    	 
    	  Timer timer = new Timer(delay, null);
    			  
    	  timer.addActionListener(new ActionListener() {
    		  
    		  int i =0;
    	  
      		public void actionPerformed(ActionEvent e) {
      			TripPoint one = trip.get(i);
      	      	TripPoint two = trip.get(i+1);
      	      	
      	      	Coordinate cordOne = new Coordinate(one.getLat(), one.getLon());
      	      	Coordinate cordTwo = new Coordinate(two.getLat(), two.getLon());
      	      	
      	    	 MapPolygonImpl mapline = new MapPolygonImpl(cordOne, cordTwo, cordTwo);
      	    	 mapline.getStyle().setColor(Color.RED);
      	    	 mapline.setBackColor(new Color(0,0,0,0));
      	    	 map.addMapPolygon(mapline);
      	    	 
      	    	 
      	    	 Point point = map.getMapPosition(cordTwo);
      	    	 
      	    	
      	    	
      	    	
      	    
      	    	 
      	    	 
      	    	 
      	    	 
      	    	 i++;
      		if (i == trip.size()) {
      			timer.stop();
      		}
      			
 
      		}
      		
      	});
    	  timer.start();
    	  
    
    
    // Animate the trip based on selections from the GUI components
    
    	  
      }
}