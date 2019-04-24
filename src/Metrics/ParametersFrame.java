/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Metrics;

import pdf.TextStreamObject;
import pdf.CatalogObject;
import pdf.PageObject;
import pdf.PDF;
import pdf.PageCollectionObject;
import pdf.FontObject;
import pdf.GraphicStreamObject;
import Continuous_Handwriting.ContinuousHandwriting;
import Separated_Handwriting.SeparatedHandwriting;
import handwriting.recognition.Index;
import handwriting.recognition.MainFrame;
import handwriting.recognition.TextFileViewer;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 *
 * @author utente1
 */
public class ParametersFrame extends JFrame{
    private final String frameTitle = "Dysgraphia Diagnosis";
    private final int frameWidth = 800;
    private final int frameHeight = 550;
    
    private final int marginFrame = 20;
    private final int heightComponent = 30;
    private final int labelWidth = 210;
    private final int buttonWidth = 100;
    
    private final String fileName;
    
    private JLabel nameLabel, surnameLabel, clasLabel, nameLabelText, surnameLabelText, clasLabelText;
    private JComboBox optionFirst, optionSecond, optionThird, optionForth, optionFifth, optionSixth, optionSeventh, optionEighth,
                      optionNinth, optionTenth, optionEleventh, optionTwelveth, optionThirteenth;
    private JLabel firstLabel, secondLabel, thirdLabel, forthLabel, fifthLabel, sixthLabel, seventhLabel, eighthLabel, ninthLabel,
                   tenthLabel, eleventhLabel, twelvethLabel, thirteenthLabel;;
    private JButton startButton;
  
    
    private final String options[] = {
        "0", "1", "2", "3", "4", "5"
    };
    
    private final String sexString[] = {"M", "F"};
    
    private static String name, surname, export;
    private static int clas, sex;
    private static String[] logs;
    private static LineForHeight[] lines;
    
  
    
    public ParametersFrame(String name, String surname, int clas, int sex, int avgHeightScore, int marginScore, int skewScore, int oWidthScore, int stretchScore, String[] logs, String fileName, String export, LineForHeight[] lines) {
        this.name = name;
        this.surname = surname;
        this.export = export;
        this.clas = clas;
        this.sex = sex;
        this.logs = logs;
        this.fileName = fileName;
        this.lines = lines;
        
        setTitle(frameTitle);
        setSize(frameWidth, frameHeight);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Desktop dt_output = Desktop.getDesktop();
        try {
            dt_output.open(new File(export));
        } catch (IOException exc) {
            JOptionPane.showMessageDialog(null, exc.toString());
        }

        initGUI(avgHeightScore, marginScore, skewScore, oWidthScore, stretchScore);
      
        setVisible(true);
    }

    private void initGUI(int avgHeightScore, int marginScore, int skewScore, int oWidthScore, int stretchScore) {
        int x = marginFrame;
        int y = marginFrame;
        
        nameLabel = new JLabel();
        nameLabel.setText("Nome:");
        nameLabel.setBounds(x, y, labelWidth, heightComponent);
        nameLabel.setForeground(Color.blue);
        add(nameLabel);
        
        nameLabelText = new JLabel();
        nameLabelText.setText(name);
        nameLabelText.setBounds(x + labelWidth, y, labelWidth, heightComponent);
        nameLabelText.setForeground(Color.black);
        add(nameLabelText);
        
        y += heightComponent + marginFrame;
        
        surnameLabel = new JLabel();
        surnameLabel.setText("Cognome:");
        surnameLabel.setBounds(x, y, labelWidth, heightComponent);
        surnameLabel.setForeground(Color.blue);
        add(surnameLabel);
        
        surnameLabelText = new JLabel();
        surnameLabelText.setText(surname);
        surnameLabelText.setBounds(x + labelWidth, y, labelWidth, heightComponent);
        surnameLabelText.setForeground(Color.black);
        add(surnameLabelText);
        
        y += heightComponent + marginFrame;
        
        clasLabel = new JLabel();
        clasLabel.setText("Classe:");
        clasLabel.setBounds(x, y, labelWidth, heightComponent);
        clasLabel.setForeground(Color.blue);
        add(clasLabel);
        
        clasLabelText = new JLabel();
        clasLabelText.setText(String.valueOf(clas));
        clasLabelText.setBounds(x + labelWidth, y, labelWidth, heightComponent);
        clasLabelText.setForeground(Color.black);
        add(clasLabelText);
        
        y += heightComponent + marginFrame;
        
      
        // First metric
        firstLabel = new JLabel();
        firstLabel.setText("Altezza delle lettere:");
        firstLabel.setBounds(x, y, labelWidth, heightComponent);
        firstLabel.setForeground(Color.red);
        add(firstLabel);
        
        optionFirst = new JComboBox(options);
        optionFirst.setBounds(x + labelWidth, y, 100, heightComponent);
        optionFirst.setSelectedIndex(avgHeightScore);
        add(optionFirst);
        //y += heightComponent + marginFrame;
        
        x = frameWidth - 2 * labelWidth;
        
        // Second metric
        secondLabel = new JLabel();
        secondLabel.setText("Margine:");
        secondLabel.setBounds(x, y, labelWidth, heightComponent);
        secondLabel.setForeground(Color.red);
        add(secondLabel);
        
        optionSecond = new JComboBox(options);
        optionSecond.setBounds(x + labelWidth, y, 100, heightComponent);
        optionSecond.setSelectedIndex(marginScore);
        add(optionSecond);
        y += heightComponent + marginFrame;
        x = marginFrame;
        
        // Third metric
        thirdLabel = new JLabel();
        thirdLabel.setText("Scrittura altalenante:");
        thirdLabel.setBounds(x, y, labelWidth, heightComponent);
        thirdLabel.setForeground(Color.red);
        add(thirdLabel);
        
        optionThird = new JComboBox(options);
        optionThird.setBounds(x + labelWidth, y, 100, heightComponent);
        optionThird.setSelectedIndex(skewScore);
        add(optionThird);
        //y += heightComponent + marginFrame;
        x = frameWidth - 2 * labelWidth;
        
        // Forth metric
        forthLabel = new JLabel();
        forthLabel.setText("Spazio insufficiente:");
        forthLabel.setBounds(x, y, labelWidth, heightComponent);
        forthLabel.setForeground(Color.red);
        add(forthLabel);
        
        optionForth = new JComboBox(options);
        optionForth.setBounds(x + labelWidth, y, 100, heightComponent);
        optionForth.setSelectedIndex(oWidthScore);
        add(optionForth);
        y += heightComponent + marginFrame;
        x = marginFrame;
        
        // Fifth metric
        fifthLabel = new JLabel();
        fifthLabel.setText("Angoli acuti:");
        fifthLabel.setBounds(x, y, labelWidth, heightComponent);
        fifthLabel.setForeground(Color.blue);
        add(fifthLabel);
        
        optionFifth = new JComboBox(options);
        optionFifth.setBounds(x + labelWidth, y, 100, heightComponent);
        add(optionFifth);
        //y += heightComponent + marginFrame;
        x = frameWidth - 2 * labelWidth;
        
        // Sixth metric
        sixthLabel = new JLabel();
        sixthLabel.setText("Collegamenti interrotti tra le lettere:");
        sixthLabel.setBounds(x, y, labelWidth, heightComponent);
        sixthLabel.setForeground(Color.blue);
        add(sixthLabel);
        
        optionSixth = new JComboBox(options);
        optionSixth.setBounds(x + labelWidth, y, 100, heightComponent);
        add(optionSixth);
        y += heightComponent + marginFrame;
        x = marginFrame;
        
        // Seventh metric
        seventhLabel = new JLabel();
        seventhLabel.setText("Collisioni tra le lettere:");
        seventhLabel.setBounds(x, y, labelWidth, heightComponent);
        seventhLabel.setForeground(Color.blue);
        add(seventhLabel);
        
        optionSeventh = new JComboBox(options);
        optionSeventh.setBounds(x + labelWidth, y, 100, heightComponent);
        add(optionSeventh);
        //y += heightComponent + marginFrame;
        x = frameWidth - 2 * labelWidth;
        
        // Eighth metric
        eighthLabel = new JLabel();
        eighthLabel.setText("Altezza caratteri incostante:");
        eighthLabel.setBounds(x, y, labelWidth, heightComponent);
        eighthLabel.setForeground(Color.red);
        add(eighthLabel);
        
        optionEighth = new JComboBox(options);
        optionEighth.setBounds(x + labelWidth, y, 100, heightComponent);
        optionEighth.setSelectedIndex(lines[0].score + lines[1].score + lines[2].score + lines[3].score + lines[4].score);
        add(optionEighth);
        y += heightComponent + marginFrame;
        x = marginFrame;
        
        // Ninth metric
        ninthLabel = new JLabel();
        ninthLabel.setText("Altezza riga incoerente:");
        ninthLabel.setBounds(x, y, labelWidth, heightComponent);
        ninthLabel.setForeground(Color.red);
        add(ninthLabel);
        
        String[] ninthOptions = new String[options.length-1];
        for(int i=0; i<ninthOptions.length; i++) {
            ninthOptions[i] = options[i];
        }
        optionNinth = new JComboBox(ninthOptions);
        optionNinth.setBounds(x + labelWidth, y, 100, heightComponent);
        optionNinth.setSelectedIndex(stretchScore);
        add(optionNinth);
        //y += heightComponent + marginFrame;
        x = frameWidth - 2 * labelWidth;
        
        // Tenth metric
        tenthLabel = new JLabel();
        tenthLabel.setText("Lettere atipiche:");
        tenthLabel.setBounds(x, y, labelWidth, heightComponent);
        tenthLabel.setForeground(Color.blue);
        add(tenthLabel);
        
        optionTenth = new JComboBox(options);
        optionTenth.setBounds(x + labelWidth, y, 100, heightComponent);
        add(optionTenth);
        y += heightComponent + marginFrame;
        x = marginFrame;
        
        // Eleventh metric
        eleventhLabel = new JLabel();
        eleventhLabel.setText("Lettere ambigue:");
        eleventhLabel.setBounds(x, y, labelWidth, heightComponent);
        eleventhLabel.setForeground(Color.blue);
        add(eleventhLabel);
        
        optionEleventh = new JComboBox(options);
        optionEleventh.setBounds(x + labelWidth, y, 100, heightComponent);
        add(optionEleventh);
        //y += heightComponent + marginFrame;
        x = frameWidth - 2 * labelWidth;
        
        // Twelveth metric
        twelvethLabel = new JLabel();
        twelvethLabel.setText("Lettere ricalcate:");
        twelvethLabel.setBounds(x, y, labelWidth, heightComponent);
        twelvethLabel.setForeground(Color.blue);
        add(twelvethLabel);
        
        optionTwelveth = new JComboBox(options);
        optionTwelveth.setBounds(x + labelWidth, y, 100, heightComponent);
        add(optionTwelveth);
        y += heightComponent + marginFrame;
        x = marginFrame;
        
        // Thirteenth metric
        thirteenthLabel = new JLabel();
        thirteenthLabel.setText("Traccia instabile:");
        thirteenthLabel.setBounds(x, y, labelWidth, heightComponent);
        thirteenthLabel.setForeground(Color.blue);
        add(thirteenthLabel);
        
        optionThirteenth = new JComboBox(options);
        optionThirteenth.setBounds(x + labelWidth, y, 100, heightComponent);
        add(optionThirteenth);
        
        
        x = frameWidth - 2 * labelWidth;
        
        startButton = new JButton("Fine");
        startButton.setBounds(x, y, buttonWidth, heightComponent);
        startButton.addActionListener(startButtonAL);
        add(startButton);
    }
    
    private ActionListener startButtonAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            double media, ds;
               
            int first = optionFirst.getSelectedIndex();
            int second = optionSecond.getSelectedIndex();
            int third = optionThird.getSelectedIndex();
            int forth = optionForth.getSelectedIndex();
            int fifth = optionFifth.getSelectedIndex();
            int sixth = optionSixth.getSelectedIndex();
            int seventh = optionSeventh.getSelectedIndex();
            int eighth = optionEighth.getSelectedIndex();
            int ninth = optionNinth.getSelectedIndex();
            int tenth = optionTenth.getSelectedIndex();
            int eleventh = optionEleventh.getSelectedIndex();
            int twelveth = optionTwelveth.getSelectedIndex();
            int thirteenth = optionThirteenth.getSelectedIndex();
            int sum = first + second + third + forth + fifth + sixth + seventh +
                      eighth + ninth + tenth + eleventh + twelveth + thirteenth;
            if(sex == 0) {
                media = 20.3;
                ds = 6.1;
            }else {
                media = 18.1;
                ds = 6.7;
            }
            double finalScore = (media-sum)/ds;
            writePdf(first, second, third, forth, fifth, sixth, seventh,
                     eighth, ninth, tenth, eleventh, twelveth, thirteenth, finalScore);
        }

        private void writePdf(int first, int second, int third, int forth, int fifth, int sixth, int seventh,
                     int eighth, int ninth, int tenth, int eleventh, int twelveth, int thirteenth, double finalScore) {
            /*
            * Create text stream with few lines
            */
            int h = 330;
            TextStreamObject textStreamObject = new TextStreamObject("F1", 18, 30, 480, "Diagnosi disgrafia");
            textStreamObject.add("F1", 8, 30, 460, "Nome : " + name);
            textStreamObject.add("F1", 8, 30, 450, "Cognome : " + surname);
            textStreamObject.add("F1", 8, 30, 440, "Classe : " + String.valueOf(clas));
            textStreamObject.add("F1", 8, 30, 430, "Sesso : " + sexString[sex]);
            textStreamObject.add("F1", 8, 0, 420, "___________________________________________________________________________________________________________________________");
            textStreamObject.add("F1", 8, 30, 400, "1' metrica : Altezza delle lettere");
            textStreamObject.add("F1", 8, 30, 390, logs[0]);
            textStreamObject.add("F1", 8, 30, 380, "...");
            textStreamObject.add("F1", 8, 30, 370, "2' metrica : Margine");
            textStreamObject.add("F1", 8, 30, 360, logs[1]);
            textStreamObject.add("F1", 8, 30, 350, "...");
            textStreamObject.add("F1", 8, 30, 340, "3' metrica : Scrittura altalenante");
            for(String s : logs[2].split("\n")) {
                textStreamObject.add("F1", 8, 30, h, s);
                h -= 10;
            }
            textStreamObject.add("F1", 8, 30, h, "...");
            h -= 10;
            textStreamObject.add("F1", 8, 30, h, "4' metrica : Spazio insufficiente");
            h -= 10;
            for(String s : logs[3].split("\n")) {
                textStreamObject.add("F1", 8, 30, h, s);
                h -= 10;
            }
            textStreamObject.add("F1", 8, 30, h, "...");
            h -= 10;
            textStreamObject.add("F1", 8, 30, h, "8' metrica : Altezza caratteri incostante");
            h -= 10;
            for(int i=0; i<lines.length; i++) {
                if(lines[i].score == 1) {
                    String s = "Linea " + (i+1) + ", " + "Altezza minima : " + lines[i].minHeight + " mm Altezza massima : " + lines[i].maxHeight + " mm";
                    textStreamObject.add("F1", 8, 30, h, s);
                    h -= 10;
                }
            }
            textStreamObject.add("F1", 8, 30, h, "...");
            h -= 10;
            textStreamObject.add("F1", 8, 30, h, "9' metrica : Altezza riga incoerente");
            h -= 10;
            for(String s : logs[4].split("\n")) {
                textStreamObject.add("F1", 8, 30, h, s);
                h -= 10;
            }
            /*
		 * First page with above text stream
             */
            PageObject page1 = new PageObject();
            page1.addAttribute("Resources", new FontObject("F1", "Times-Roman"));
            page1.addContent(textStreamObject);
            page1.addAttribute("MediaBox", "[0 0 400 500]");
            
               
            textStreamObject = new TextStreamObject("F1", 18, 30, 480, "Diagnosi disgrafia");
            textStreamObject.add("F1", 8, 30, 460, "Nome : " + name);
            textStreamObject.add("F1", 8, 30, 450, "Cognome : " + surname);
            textStreamObject.add("F1", 8, 30, 440, "Classe : " + String.valueOf(clas));
            textStreamObject.add("F1", 8, 30, 430, "Sesso : " + sexString[sex]);
            textStreamObject.add("F1", 8, 0, 420, "___________________________________________________________________________________________________________________________");

            textStreamObject.add("F1", 8, 30, 400, "1' metrica : Altezza delle lettere");
            textStreamObject.add("F1", 8, 30, 390, "Punteggio : " + first);
            textStreamObject.add("F1", 8, 30, 380, "...");
            textStreamObject.add("F1", 8, 240, 400, "2' metrica : Margine");
            textStreamObject.add("F1", 8, 240, 390, "Punteggio : " + second);
            textStreamObject.add("F1", 8, 240, 380, "...");
            
            textStreamObject.add("F1", 8, 30, 370, "3' metrica : Scrittura altalenante");
            textStreamObject.add("F1", 8, 30, 360, "Punteggio : " + third);
            textStreamObject.add("F1", 8, 30, 350, "...");
            textStreamObject.add("F1", 8, 240, 370, "4' metrica : Spazio insufficiente");
            textStreamObject.add("F1", 8, 240, 360, "Punteggio : " + forth);
            textStreamObject.add("F1", 8, 240, 350, "...");
            
            textStreamObject.add("F1", 8, 30, 340, "5' metrica : Angoli acuti");
            textStreamObject.add("F1", 8, 30, 330, "Punteggio : " + fifth);
            textStreamObject.add("F1", 8, 30, 320, "...");
            textStreamObject.add("F1", 8, 240, 340, "6' metrica : Collegamenti interrotti tra le lettere");
            textStreamObject.add("F1", 8, 240, 330, "Punteggio : " + sixth);
            textStreamObject.add("F1", 8, 240, 320, "...");
            
            textStreamObject.add("F1", 8, 30, 310, "7' metrica : Collisioni tra le lettere");
            textStreamObject.add("F1", 8, 30, 300, "Punteggio : " + seventh);
            textStreamObject.add("F1", 8, 30, 290, "...");
            textStreamObject.add("F1", 8, 240, 310, "8' metrica : Altezza caratteri incostante");
            textStreamObject.add("F1", 8, 240, 300, "Punteggio : " + eighth);
            textStreamObject.add("F1", 8, 240, 290, "...");
            
            textStreamObject.add("F1", 8, 30, 280, "9' metrica : Altezza riga incoerente");
            textStreamObject.add("F1", 8, 30, 270, "Punteggio : " + ninth);
            textStreamObject.add("F1", 8, 30, 260, "...");
            textStreamObject.add("F1", 8, 240, 280, "10' metrica : Lettere atipiche");
            textStreamObject.add("F1", 8, 240, 270, "Punteggio : " + tenth);
            textStreamObject.add("F1", 8, 240, 260, "...");
            
            textStreamObject.add("F1", 8, 30, 250, "11' metrica : Lettere ambigue");
            textStreamObject.add("F1", 8, 30, 240, "Punteggio : " + eleventh);
            textStreamObject.add("F1", 8, 30, 230, "...");
            textStreamObject.add("F1", 8, 240, 250, "12' metrica : Lettere ricalcate");
            textStreamObject.add("F1", 8, 240, 240, "Punteggio : " + twelveth);
            textStreamObject.add("F1", 8, 240, 230, "...");
            
            textStreamObject.add("F1", 8, 30, 220, "13' metrica : Traccia instabile");
            textStreamObject.add("F1", 8, 30, 210, "Punteggio : " + thirteenth);
            textStreamObject.add("F1", 8, 240, 220, "Punteggio finale:");
            String s = String.valueOf(Math.round(finalScore*10)/10.0);
            if(finalScore < -1.5) {
                s += " < ";
            }else if(finalScore > -1.5) {
                s += " > ";
            }else {
                s += " = ";
            }
            s += "-1.5";
            textStreamObject.add("F1", 8, 240, 210, String.valueOf(s));
            
            textStreamObject.add("F1", 8, 30, 190, "Annotazioni:");
            textStreamObject.add("F1", 8, 30, 175, "__________________________________________________________________________");
            textStreamObject.add("F1", 8, 30, 160, "__________________________________________________________________________");
            textStreamObject.add("F1", 8, 30, 145, "__________________________________________________________________________");
            textStreamObject.add("F1", 8, 30, 130, "__________________________________________________________________________");
            
            textStreamObject.add("F1", 8, 30, 30, "Firma dello specialista");
            textStreamObject.add("F1", 8, 30, 5, "_______________________");
            

            PageObject page2 = new PageObject();
            page2.addAttribute("Resources", new FontObject("F1", "Times-Roman"));
            page2.addContent(textStreamObject);
            page2.addAttribute("MediaBox", "[0 0 400 500]");
            
            /*
		 * Prepare pages & catalog objects.
             */
            PageCollectionObject pageCollectionObject = new PageCollectionObject();
            pageCollectionObject.addPages(page1, page2);
            CatalogObject catalogObject = new CatalogObject(pageCollectionObject);

            /*
		 * Build final PDF.
             */
            PDF pdf = new PDF(catalogObject);

            /*
		 * Write PDF to a file.
             */
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter(fileName);
                fileWriter.write(pdf.build());
                fileWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(ParametersFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
    
    
}