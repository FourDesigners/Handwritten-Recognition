/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Metrics;

import Continuous_Handwriting.ContinuousHandwriting;
import Metrics.LineForHeight;
import handwriting.recognition.MainFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ImagePanel extends JPanel implements MouseListener{
    
    private final static int NPOINTS = 20;
    private final static int NROWS = 5;
    
    private JPanel panel;
    private JLabel label, labelCounter;
    private static BufferedImage image;
    static int[] vectorY = new int[NPOINTS];
    private List<Point> points = new ArrayList<>();
    private static int n = 0;
    private static LineForHeight[] lines = new LineForHeight[NROWS];
    private static int counter = 20;
    
    private final int heightComponent = 5;
    private final int labelWidth = 5;
    
    private String infoFileName, name, surname, fileName, export;
    private int clas, sex;
    
    public ImagePanel(String infoFileName, String name, String surname, int clas, int sex, String fileName, String img, String export) throws IOException {
        this.infoFileName = infoFileName;
        this.name = name;
        this.surname = surname;
        this.clas = clas;
        this.fileName = fileName;
        this.export = export;
        this.sex = sex;
        
        
        panel = new JPanel();
       
        image = ImageIO.read(new File(img));
        //JLabel label = new JLabel(new ImageIcon(image));
        
        label = new JLabel(new ImageIcon(image)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                labelCounter.setText("Punti da selezionare: " + counter);
                g.setColor(Color.red);
                for(Point p : points) {
                    g.fillOval(p.x, p.y, 7, 7);
                }
            }
        };
        
        panel.add(label);
        
        labelCounter = new JLabel();
        labelCounter.setText("Punti da selezionare: " + counter);
        labelCounter.setBounds(image.getWidth(), 0, labelWidth, heightComponent);
        labelCounter.setForeground(Color.blue);
        panel.add(labelCounter);
        
        // main window
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("JPanel Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add the Jpanel to the main window
        frame.add(panel);

        frame.pack();
        frame.setVisible(true);
        //JOptionPane.showMessageDialog(null, "Pressed");
       
        //setBackground(Color.WHITE);
        panel.addMouseListener(this);
    }
    
    
    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = new Point(e.getX()-8, e.getY()-7);
        //JOptionPane.showMessageDialog(null, e.getX() + " " + e.getY());
        if (!points.isEmpty()) {
            Point last = points.get(points.size() - 1);
            if (Math.abs(p.x-last.x)<4 && Math.abs(p.y-last.y)<4) {
                points.remove(last);
                counter++;
                n--;
            } else {
                vectorY[n++] = e.getY();
                points.add(p);
                counter--;
            }
        }else {
            vectorY[n++] = e.getY();
            points.add(p);
            counter--;
        }
        
        label.repaint();
        //JOptionPane.showMessageDialog(null, e.getY());
        if (n == NPOINTS) {
            JComponent comp = (JComponent) e.getSource();
            Window win = SwingUtilities.getWindowAncestor(comp);
            win.dispose();
            calculateHeightScore();
            try {
                ContinuousHandwriting.Recognition(infoFileName, name, surname, clas, sex, fileName, export, lines);
            } catch (IOException ex) {
                Logger.getLogger(ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WrittenFileException ex) {
                Logger.getLogger(ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    private void calculateHeightScore() {
        for(int i=0; i<NROWS; i++) {
            lines[i] = new LineForHeight();
            calculateLineHeightScore(lines[i], vectorY[i*4], vectorY[i*4+1], vectorY[i*4+2], vectorY[i*4+3]);
        }
    }

    private void calculateLineHeightScore(LineForHeight line, int y1, int y2, int y3, int y4) {
        //line = new LineForHeight();
        int h1 = Math.abs(y1-y2);
        int h2 = Math.abs(y3-y4);
        if(h1 > h2) {
            line.maxHeight = h1;
            line.minHeight = h2;
        }else {
            line.maxHeight = h2;
            line.minHeight = h1;
        }
        line.maxHeight = Math.round(DotsToMm(line.maxHeight)*100)/100.0;
        line.minHeight = Math.round(DotsToMm(line.minHeight)*100)/100.0;
        line.score = 0;
        if(line.maxHeight<=3 && line.maxHeight-line.minHeight>1) {
            line.score = 1;
        }
        if(line.maxHeight<=5 && line.maxHeight-line.minHeight>1.5) {
            line.score = 1;
        }
        if(line.maxHeight<=8.5 && line.maxHeight-line.minHeight>2) {
            line.score = 1;
        }
        if(line.maxHeight<=10 && line.maxHeight-line.minHeight>2.5) {
            line.score = 1;
        }
        if(line.maxHeight-line.minHeight > 3) {
            line.score = 1;
        }
        //System.out.println("Altezza minima : " + line.minHeight + " Altezza massima : " + line.maxHeight);
    }

    public static double DotsToMm(double height) {
        return height / 5.3;
    }
}

