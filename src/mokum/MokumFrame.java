package mokum;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicArrowButton;

@SuppressWarnings("serial")
public class MokumFrame extends JFrame {
	private Button redrawButton;			// Roept de redraw methode aan
	private Scrollbar schaal;				// Laat de gebruiker de schaal tijdens de run
	private BasicArrowButton left, right;	// veranderen
	private GlobalTraffic traffic;
	private MokumFrame frame;
	private DienstregelingCanvas dienst; 
	private static int mouseX, mouseY;
	private JLabel mouseposition;

	public MokumFrame() {
		setTitle("Heuristieken 2014 - Mokum Airlines! - Group 4 \"De Vliegende Hollanders\"");
		setLayout(new BorderLayout());
		setSize(1024, 768);

		frame = this;
		
		Dienstregeling dienstregeling = maakDienstregeling();

		schaal = new Scrollbar();
		schaal.setMaximum(100);
		schaal.setMinimum(1);
		schaal.setOrientation(Scrollbar.HORIZONTAL);
		schaal.setValue(45);
		schaal.setPreferredSize(new Dimension(250, 20));
		redrawButton = new Button("Redraw!");
		left = new BasicArrowButton(BasicArrowButton.WEST);
		right = new BasicArrowButton(BasicArrowButton.EAST);
		
		
		traffic = new GlobalTraffic(dienstregeling);
		dienst = new DienstregelingCanvas(dienstregeling);

		schaal.addAdjustmentListener(new AdjustmentListener(){
			//Houdt bij of er aan de scrollbar gezet is of niet
			public void adjustmentValueChanged(AdjustmentEvent e) {
				double scaleFactor = (double) schaal.getValue() / 100;
				dienst.setScaleFactor(scaleFactor);
				frame.redraw(200);
		   }
		});
		redrawButton.addActionListener(new ActionListener(){
			//Houdt bij of er op de knop gedrukt is
			public void actionPerformed(ActionEvent evt) {
				if(evt.getSource() == redrawButton){
					Dienstregeling dienstregeling = maakDienstregeling();
					traffic.setDienstregeling(dienstregeling);
					dienst.setDienstregeling(dienstregeling);
					frame.redraw(200);
				}
			}
		});
		
		left.addActionListener(new ActionListener(){
			//Houdt bij of er op de knop gedrukt is
			public void actionPerformed(ActionEvent evt) {
				if(evt.getSource() == left){
					int val = schaal.getValue();
					schaal.setValue(val-1);
					double scaleFactor = (double) schaal.getValue() / 100;
					dienst.setScaleFactor(scaleFactor);
					frame.redraw(200);
				}
			}
		});
		
		right.addActionListener(new ActionListener(){
			//Houdt bij of er op de knop gedrukt is
			public void actionPerformed(ActionEvent evt) {
				if(evt.getSource() == right){
					int val = schaal.getValue();
					schaal.setValue(val+1);
					double scaleFactor = (double) schaal.getValue() / 100;
					dienst.setScaleFactor(scaleFactor);
					frame.redraw(200);
				}
			}
		});

		Panel buttonPane = new Panel();
		buttonPane.setLayout(new FlowLayout());
		buttonPane.add(left);
		buttonPane.add(schaal);
		buttonPane.add(right);
		buttonPane.add(redrawButton);
		
		buttonPane.setPreferredSize(new Dimension(1024,40));
		
		this.add(buttonPane, BorderLayout.SOUTH);
		
		JPanel topPanel = new JPanel();
		topPanel.add(traffic,BorderLayout.CENTER);
		traffic.setLocation(frame.getWidth() / 2, 0);
		
		this.add(topPanel,BorderLayout.NORTH);
		this.add(dienst,BorderLayout.CENTER);
		
		// Exit the program when the window is closed
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Close the window when escape is pressed
		this.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
					System.exit(0);
				}
			}
		});
		
		mouseX = 0;
		mouseY = 0;
		
		this.addMouseMotionListener(new MouseMotionAdapter(){

			@Override
			public void mouseMoved(MouseEvent e) {
				super.mouseMoved(e);
				mouseX = (e.getX());
				mouseY = (e.getY()) - 25; // offset the 25px top bar
				
				mouseposition.setText(String.format("Mouse: %d, %d",mouseX,mouseY));
			}
			
		});
		
		mouseposition = new JLabel("Mouse:");
		buttonPane.add(mouseposition);
		
		// Resize the window
		pack();
		
		setVisible(true);
		frame.redraw(200);
	}

	// Maakt een random dienstregeling aan
	// || In deze methode zou je je algoritme kunnen plaatsen in plaats van de
	// 'domme'
	// random methode die nu gebruikt wordt
	public Dienstregeling maakDienstregeling() {
		//Dienstregeling d = new Dienstregeling(true);
		//Dienstregeling d = Optimizer.Optimize(10000,1000,2);
		Dienstregeling d = new Dienstregeling();
		int resultaatArray[] = new int[1000];
		int min = 9999999;
		int max = 0;
		double avg = 0;
		for(int i=0;i<1000;i++){
			d = Optimizer.Optimize2(100,100,0.1,0.01);
			int temp = d.telPassagiersKilometers();
			avg += temp/1000;
			if(temp>max){
				max = temp;
			}
			if(temp<min){
				min = temp;
			}
			resultaatArray[i] = temp;			
		}
		System.out.println("min: "+min+" max: "+max+" avg: "+avg);
		return d;
	}

	public static void main(String[] args) {
		City.initialiseCities();
		Dienstregeling.maakPKmatrix();
		
		new MokumFrame(); //Hier start de magie. 
		//Even een voorbeeldje (voorbeeld is met een "D" ) :P
	}
	
	public void redraw(int delay){
		repaint();
		invalidate();
		validate();
		
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
