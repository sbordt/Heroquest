package de.d2dev.heroquest.engine.rendering.quads;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

public class RunJmeCanvasInSwing implements Runnable {
	
	private JFrame frame = null;
	private JPanel panel = null;
	private SimpleApplication jmeApp = null;

	public RunJmeCanvasInSwing (JFrame frame, SimpleApplication jmeApp){
		this.frame = frame;
		this.jmeApp = jmeApp;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		AppSettings settings = new AppSettings(true);
		settings.setWidth(640);
		settings.setHeight(480);
		jmeApp.setSettings(settings);
		jmeApp.createCanvas();
		JmeCanvasContext canvasContext = (JmeCanvasContext) jmeApp.getContext();
		Dimension dim = new Dimension(640, 480);
		canvasContext.getCanvas().setPreferredSize(dim);
		
		if ((panel == null) && (frame != null)){
			JPanel panel = new JPanel();
			panel.add(canvasContext.getCanvas());
			frame.add(panel);
			frame.pack();
			frame.setVisible(true);
		}
		else if (panel != null){
			panel.add(canvasContext.getCanvas());
		}
		
		jmeApp.startCanvas();
	}

}
