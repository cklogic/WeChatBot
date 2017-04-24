package com.cklogic.tool;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ShowImg {
	public static Frame showImg(String url){
		Frame frame=new Frame();  
		frame.setTitle("«Î…®√Ë"); 
		Panel panel = new Panel();
		JLabel helloLabel = new JLabel();  
		helloLabel.setIcon(new ImageIcon(url));  
		helloLabel.setBackground(Color.BLACK);  
		helloLabel.setBounds(0, 0, 105, 50);  
		panel.add(helloLabel); 
		frame.add(panel);
		/*frame.addWindowListener(new WindowAdapter() {  
            public void windowClosing(WindowEvent e) {  
                System.exit(0);  
            }  
        });  */
		frame.pack();  
		frame.setVisible(true);
		return frame;
	}
}
