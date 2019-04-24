// Software: Dysgraphia Diagnosis in Java


package handwriting.recognition;

import LibOCR.Normalization;
import Metrics.CalculateMetrics;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Index extends JFrame {
    
    public static final int WORDSTHRESHOLD = 39;
    public static final double RATETHRESHOLD = 1.2;
    public static final int AVGTHRESHOLD = 25;
    public static final int MAXTHRESHOLD = 39;
 
    
    private final String titleFrame = "Riepilogo metriche";
    private final int widthFrame = 1024;
    private final int heightFrame = 600;
    private final int marginFrame = 20;
    private final int widthLabel = 150;
    private final int widthText = 200/*widthFrame / 2 - widthLabel - 2 * marginFrame*/;
    private final int heightComponent = 30;
    private final int sizeButton = 80;
    private final int sizeImage = widthFrame / 2 - 2 * marginFrame;
    
    private final String imageFolder = "my-images/";
    private final int nImages = 5;
    private final BufferedImage buttonImage[] = new BufferedImage [nImages + 1];
    private int currentImage = 1;
    
    private final String info[] = {
        "1° metrica:", "Altezza delle lettere",
        "2° metrica:", "Margine",
        "3° metrica:", "Scrittura altalenante",
        "4° metrica:", "Spazio insufficiente",
        "5° metrica:", "Angoli acuti",
        "6° metrica:", "Collegamenti interrotti tra le lettere",
        "7° metrica:", "Collisioni tra le lettere",
        "8° metrica:", "Altezza caratteri incostante",
        "9° metrica:", "Altezza riga incoerente",
        "10° metrica:", "Lettere atipiche",
        "11° metrica:", "Lettere ambigue",
        "12° metrica:", "Lettere ricalcate",
        "13° metrica:", "Traccia instabile"
    };
    
    private JButton pictureButton, resetDistanceButton, applyDistanceButton, resetRateButton, applyRateButton;   
    private JLabel distanceLabel, rateLabel, avgLabel, maxminLabel, startendLabel;
    private JTextField avgText, maxminText, startendText;
    private JComboBox rateBox;
    private final String options[] = {
        "10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%"
    };
    
    public Index() {
        setTitle(titleFrame);
        setSize(widthFrame, heightFrame);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        int lastY = addInformationText();
            
        //readImages();
        //addImages(lastY);
        
        pictureButton = new JButton();
        add(pictureButton);
        
        int x = widthFrame/2 + 50;
        int y = marginFrame;
        
        distanceLabel = new JLabel("Distanze");
        distanceLabel.setBounds(x, y, widthLabel, heightComponent);
        distanceLabel.setForeground(Color.blue);
        add(distanceLabel);
        distanceLabel.setVisible(false);
        
        rateLabel = new JLabel("Percentuale");
        rateLabel.setBounds(x, y, widthLabel, heightComponent);
        rateLabel.setForeground(Color.blue);
        add(rateLabel);
        rateLabel.setVisible(false);
        
        y += heightComponent;
        
        avgLabel = new JLabel("Media parole");
        avgLabel.setBounds(x, y, widthLabel, heightComponent);
        avgLabel.setForeground(Color.blue);
        add(avgLabel);
        avgLabel.setVisible(false);
        
        rateBox = new JComboBox(options);
        rateBox.setBounds(x, y, 50, heightComponent);
        rateBox.setSelectedIndex(1);
        add(rateBox);
        rateBox.setVisible(false);
        
        avgText = new JTextField();
        avgText.setBounds(x + widthLabel, y, 50, heightComponent-10);
        avgText.setText(String.valueOf(Math.round(WORDSTHRESHOLD/7.8*10)/10.0));
        add(avgText);
        avgText.setVisible(false);
        
        applyDistanceButton = new JButton("Applica");
        applyDistanceButton.setBounds(x + 2 * widthLabel, y, widthLabel-50, heightComponent);
        add(applyDistanceButton);
        applyDistanceButton.addActionListener(applyDistance);
        applyDistanceButton.setVisible(false);
        
        applyRateButton = new JButton("Applica");
        applyRateButton.setBounds(x + 2 * widthLabel, y, widthLabel-50, heightComponent);
        add(applyRateButton);
        applyRateButton.addActionListener(applyRate);
        applyRateButton.setVisible(false);

        y += heightComponent;
        
        maxminLabel = new JLabel("Baseline parole");
        maxminLabel.setBounds(x, y, widthLabel, heightComponent);
        maxminLabel.setForeground(Color.blue);
        add(maxminLabel);
        maxminLabel.setVisible(false);
        
        maxminText = new JTextField();
        maxminText.setBounds(x + widthLabel, y, 50, heightComponent-10);
        maxminText.setText(String.valueOf(Math.round(MAXTHRESHOLD/7.8*10)/10.0));
        add(maxminText);
        maxminText.setVisible(false);
        y += heightComponent;
        
        startendLabel = new JLabel("Baseline parola");
        startendLabel.setBounds(x, y, widthLabel, heightComponent);
        startendLabel.setForeground(Color.blue);
        add(startendLabel);
        startendLabel.setVisible(false);
        
        startendText = new JTextField();
        startendText.setBounds(x + widthLabel, y, 50, heightComponent-10);
        startendText.setText(String.valueOf(Math.round(AVGTHRESHOLD/7.8*10)/10.0));
        add(startendText);
        startendText.setVisible(false);
        
        
        resetDistanceButton = new JButton("Default");
        resetDistanceButton.setBounds(x + 2 * widthLabel, y, widthLabel-50, heightComponent);
        add(resetDistanceButton);
        resetDistanceButton.addActionListener(resetDistance);
        resetDistanceButton.setVisible(false);
        
        resetRateButton = new JButton("Default");
        resetRateButton.setBounds(x + 2 * widthLabel, y, widthLabel-50, heightComponent);
        add(resetRateButton);
        resetRateButton.addActionListener(resetRate);
        resetRateButton.setVisible(false);
        
        y += heightComponent;
        
        drawPicture();
        setVisible(true);
    }
    
    private ActionListener resetDistance = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            avgText.setText(String.valueOf(Math.round(WORDSTHRESHOLD/7.8*10)/10.0));
            maxminText.setText(String.valueOf(Math.round(MAXTHRESHOLD/7.8*10)/10.0));
            startendText.setText(String.valueOf(Math.round(AVGTHRESHOLD/7.8*10)/10.0));
        }
    };
    
    private ActionListener resetRate = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            rateBox.setSelectedIndex(1);
        }
    };
    
    private ActionListener applyDistance = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean flag = true;
            int val1 = 0, val2 = 0, val3 = 0;
            try {
                val1 = (int) Math.round(Double.valueOf(startendText.getText())*7.8);
                val2 = (int) Math.round(Double.valueOf(avgText.getText())*7.8);
                val3 = (int) Math.round(Double.valueOf(maxminText.getText())*7.8);
                if(val1<0 || val2<0 || val3<0) {
                    flag = false;
                    JOptionPane.showMessageDialog(null, "Errore! Campo negativo");
                }
            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Errore! Campo non numerico");
                flag = false;
            }
            if(flag) {
                CalculateMetrics.avgthreshold = val1;
                CalculateMetrics.wordsthreshold = val2;
                CalculateMetrics.maxthreshold = val3;
                JOptionPane.showMessageDialog(null, "Modifiche salvate!");
            }
        }
    };
    
    private ActionListener applyRate = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            CalculateMetrics.ratethreshold = Integer.valueOf(String.valueOf(rateBox.getSelectedItem()).split("%")[0])/100.0+1;
            JOptionPane.showMessageDialog(null, "Modifiche salvate!");
        }
    };
    
    private void drawPicture() {
        String imageName = imageFolder + Integer.toString(currentImage) + "_image.jpg";
        BufferedImage image = null;
      
        try {
            image = ImageIO.read(new File(imageName));
        } catch (IOException exc) {
            System.err.println(exc.toString());
            JOptionPane.showMessageDialog(null, "Cannot find the image " + imageName);
        }
        
        int widthImage = image.getWidth(null);
        int heightImage = image.getHeight(null);
            
        int x = widthFrame / 2 + (widthFrame / 2 - widthImage) / 2;
        int y = (heightFrame - heightImage - marginFrame) / 2 + 50;
        
        pictureButton.setBounds(x, y, widthImage, heightImage);
        pictureButton.setIcon(new ImageIcon(image.getScaledInstance(widthImage, heightImage, Image.SCALE_DEFAULT)));
    }
    
    private int addInformationText() {
        int x = marginFrame;
        int y = marginFrame;
        int i = 0;
        
        while (i < info.length) {
            JLabel label = new JLabel(info[i]);
            label.setBounds(x, y, widthLabel, heightComponent);
            label.setForeground(Color.blue);
            add(label);
            
            ++i;
            
            JTextField text = new JTextField(info[i]);
            text.setBounds(x + widthLabel, y, widthText, heightComponent);
            text.setEditable(false);
            add(text);
            
            ++i;
            
            if(i==6 || i==18) {
                JButton button = new JButton("Modifica");
                button.setLocation(x + 2 * widthLabel + 50, y);
                button.setSize(widthText / 2, heightComponent);
                button.putClientProperty("ID", i);
                button.addActionListener(ModifyButtonAction);
                add(button);
            }
            y += heightComponent;
        }
        
        
        return y + heightComponent;
    }
    
    private void addImages(int y) {
        int x0 = marginFrame;
        for (int i = 1; i <= nImages; ++i) {           
            JButton button = new JButton();
            
            int heightImage = buttonImage[i].getHeight(null);
            int widthImage = buttonImage[i].getWidth(null);
            button.setIcon(new ImageIcon(buttonImage[i]));
            
            // String buttonName = imageFolder + Integer.toString(i) + "_button.jpg";
            // button.setIcon(new ImageIcon(buttonName));
            
            button.setLocation(x0, y + (heightFrame - y - heightImage) / 2);
            button.setSize(widthImage, heightImage);
            button.putClientProperty("ID", i);
            button.addActionListener(ImageButtonAction);
            add(button);
            // buttonPanel.add(button);
            
            x0 += widthImage + marginFrame;
        }        
    }
    
    private final ActionListener ImageButtonAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton)(e.getSource());
            int ID = (int)(button.getClientProperty("ID"));
            currentImage = ID;
            drawPicture();
        }
    };
    
    private final ActionListener ModifyButtonAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton)(e.getSource());
            int ID = (int)(button.getClientProperty("ID"));
            distanceLabel.setVisible(ID == 6);
            avgLabel.setVisible(ID == 6);
            maxminLabel.setVisible(ID == 6);
            startendLabel.setVisible(ID == 6);
            avgText.setVisible(ID == 6);
            maxminText.setVisible(ID == 6);
            startendText.setVisible(ID == 6);
            resetDistanceButton.setVisible(ID == 6);
            applyDistanceButton.setVisible(ID == 6);
            rateLabel.setVisible(ID != 6);
            rateBox.setVisible(ID != 6);
            resetRateButton.setVisible(ID != 6);
            applyRateButton.setVisible(ID != 6);
        }
    };
    
    private void readImages() {
        for (int i = 1; i <= nImages; ++i) {
            String buttonName = imageFolder + Integer.toString(i) + "_button.jpg";
            
            boolean hasButton = true;
            buttonImage[i] = null;
            try {
                buttonImage[i] = ImageIO.read(new File(buttonName));
            } catch (IOException exc) {
                System.err.println(exc.toString());
                hasButton = false;
            }
            
            if (!hasButton) {
                String originName = imageFolder + Integer.toString(i) + ".jpg";
                BufferedImage originImage;
                try {
                    originImage = ImageIO.read(new File(originName));
                } catch (IOException exc) {
                    System.err.println(exc.toString());
                    JOptionPane.showMessageDialog(null, "Cannot find the image " + originName);
                    continue;
                }

                resizeImage(originImage, buttonName, sizeButton);
                
                try {
                    buttonImage[i] = ImageIO.read(new File(buttonName));
                } catch (IOException exc) {
                    System.err.println(exc.toString());
                }
            }
        }
    }
    
    private int RGB(int red, int green, int blue){
        return (0xff000000) | (red << 16) | (green << 8) | blue;
    }
    
    private void resizeImage(BufferedImage inputImage, String imageName, int size) {
        int widthImage = inputImage.getWidth(null);
        int heightImage = inputImage.getHeight(null);
        
        int r1[][] = new int [widthImage][heightImage];
        int g1[][] = new int [widthImage][heightImage];
        int b1[][] = new int [widthImage][heightImage];
        
        double ratio;
        if (widthImage > heightImage) {
            ratio = (double)(size) / widthImage;
        } else {
            ratio = (double)(size) / heightImage;
        }
        
        int w = (int)(widthImage * ratio);
        int h = (int)(heightImage * ratio);
        
        int r2[][] = new int [w][h];
        int g2[][] = new int [w][h];
        int b2[][] = new int [w][h];
                
        for (int i = 0; i < widthImage; ++i) {
            for (int j = 0; j < heightImage; ++j) {
                int RGB = inputImage.getRGB(i, j);
                r1[i][j] = (RGB & 0x00ff0000) >> 16;
		g1[i][j] = (RGB & 0x0000ff00) >> 8;
		b1[i][j] = RGB & 0x000000ff;
            }
        }
        
        Normalization.Resize(r1, r2, widthImage, heightImage, w, h);
        Normalization.Resize(g1, g2, widthImage, heightImage, w, h);
        Normalization.Resize(b1, b2, widthImage, heightImage, w, h);
        
	BufferedImage outputImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	for (int i = 0; i < w; ++i)
            for (int j = 0; j < h; ++j) {
                outputImage.setRGB(i, j, RGB(r2[i][j], g2[i][j], b2[i][j]));
            }
        
        try {
            ImageIO.write(outputImage, "jpg", new File(imageName));
        } catch (IOException exc) {
            System.err.println(exc.toString());
        }
    }
    
}
