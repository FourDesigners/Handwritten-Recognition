// Software: Dysgraphia Diagnosis in Java

// C:\Users\\utente1\Desktop\3-6.jpg
package handwriting.recognition;

import Metrics.ImagePanel;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainProgram {
    
    public static void main(String[] args) throws IOException, InterruptedException, InvocationTargetException {  
        new MainFrame();
    }
}
