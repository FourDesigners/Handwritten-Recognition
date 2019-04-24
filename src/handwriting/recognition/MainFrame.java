// Software: Dysgraphia Diagnosis in Java

package handwriting.recognition;

import Continuous_Handwriting.ContinuousHandwriting;
import Metrics.ImagePanel;
import Metrics.LineForHeight;
import Metrics.WrittenFileException;
import Separated_Handwriting.SeparatedHandwriting;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainFrame extends JFrame {
    
    private final String frameTitle = "Dysgraphia Diagnosis";
    private final int frameWidth = 500;
    private final int frameHeight = 550;
    private final int marginFrame = 20;
    private final int heightComponent = 30;
    private final int labelWidth = 120;
    private final int buttonWidth = 100;
    
    private JLabel optionLabel;
    private JComboBox optionBox;
    private JComboBox optionClasBox;
    private JComboBox optionSexBox;
    private JLabel nameLabel;
    private JTextField nameText;
    private JLabel surnameLabel;
    private JTextField surnameText;
    private JLabel classLabel;
    private JLabel sexLabel;
    private JLabel textLabel;
    private JTextField hiddenLayerText;
    private JLabel outputLayerLabel;
    private JTextField outputLayerText;
    private JLabel dictionaryFileLabel;
    private JTextField dictionaryFileText;
    private JButton dictionaryFileButton;
    private JLabel modelFileLabel;
    private JTextField modelFileText;
    private JButton modelFileButton;
    private JLabel importedImageLabel;
    private JTextField importedImageText;
    private JButton importedImageButton;
    private JLabel exportedImageLabel;
    private JTextField exportedImageText;
    private JButton exportedImageButton;
    private JLabel exportedOutputLabel;
    private JTextField exportedOutputText;
    private JButton exportedOutputButton;
    private JButton aboutButton;
    private JButton startButton;
    private JFileChooser fileChooser = new JFileChooser();
    
    private final String infoFileName = "info-parameters.dat";
    
    private final String options[] = {
        "Free-style (curve and continuous) handwriting recognition"
    };
    
    private final String optionsClas[] = {
        "2", "3", "4", "5"
    };
    
    private final String optionsSex[] = {
        "Maschio", "Femmina"
    };
    
    private int sampleWidth = 32;
    private int sampleHeight = 32;
    private int inputLayer = sampleWidth * sampleHeight;
    private int hiddenLayer = 100;
    private int outputLayer = 46;
    
    private String name;
    private String surname;
    private int clas, sex;
    
    private String dictionaryFile = "Models/Dictionary";
    private String modelFile = "Models/MLP-1024-100-46";
    private String importedImageList[] = {
        "Import/Continuous.jpg",
        "Import/Separated.jpg"
    };
    private String importedImage;
    private String exportedImage = "Export/demo-output.png";
    private String exportedOutput = "Export/demo-output.txt";
    private LineForHeight[] lines;
    
    public MainFrame() {
        this.lines = lines;
        setTitle(frameTitle);
        setSize(frameWidth, frameHeight);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        initGUI();
      
        setVisible(true);
    }
    
    private ActionListener startButtonAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            try {
                name = nameText.getText();
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(null, "Nome errato!");
                return;
            }
            
            try {
                surname = surnameText.getText();
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(null, "Cognome errato!");
                return;
            }
            /*
            if ((sampleWidth < 10) || (sampleHeight < 10) || (sampleWidth > 100) || (sampleHeight > 100)) {
                JOptionPane.showMessageDialog(null, "The size of sample is not applicable!");
                return;
            }
            
            try {
                hiddenLayer = Integer.parseInt(hiddenLayerText.getText());
            } catch (NumberFormatException exc) {
                JOptionPane.showMessageDialog(null, "Number format of hidden layer is not correct!");
                return;
            }
            
            if (hiddenLayer <= 0) {
                JOptionPane.showMessageDialog(null, "Hidden layer must be greater than 0!");
                return;
            }
            
            try {
                outputLayer = Integer.parseInt(outputLayerText.getText());
            } catch (NumberFormatException exc) {
                JOptionPane.showMessageDialog(null, "Number format of output layer is not correct!");
                return;
            }
            
            if (outputLayer <= 0) {
                JOptionPane.showMessageDialog(null, "Output layer must be greater than 0!");
                return;
            }
            
            dictionaryFile = dictionaryFileText.getText();
            if ((dictionaryFile == null) || (dictionaryFile.length() == 0)) {
                JOptionPane.showMessageDialog(null, "You have to choose a dictionary file!");
                return;
            }
            
            modelFile = modelFileText.getText();
            if ((modelFile == null) || (modelFile.length() == 0)) {
                JOptionPane.showMessageDialog(null, "You have to choose a model file for the Neural Network!");
                return;
            }
            */
            importedImage = importedImageText.getText();
            if ((importedImage == null) || (importedImage.length() == 0)) {
                JOptionPane.showMessageDialog(null, "Scegliere immagine di input!");
                return;
            }
            
            exportedImage = exportedImageText.getText();
            if ((exportedImage == null) || (exportedImage.length() == 0)) {
                JOptionPane.showMessageDialog(null, "Scegliere nome per immagine di output!");
                return;
            }
            
            /*
            exportedOutput = exportedOutputText.getText();
            if ((exportedOutput == null) || (exportedOutput.length() == 0)) {
                JOptionPane.showMessageDialog(null, "You have to choose an text file name as the output!");
                return;
            }
            */
            try {
                saveParametersToFile(infoFileName);
            } catch (IOException exc) {
                JOptionPane.showMessageDialog(null, exc.toString());
                return;
            }
                
            int index = optionBox.getSelectedIndex();
            int clas = optionClasBox.getSelectedIndex()+2;
            int sex = optionSexBox.getSelectedIndex();
            
            
            // Image Viewer
            //
            
            Desktop dt_input = Desktop.getDesktop();
            /*
            try {
                dt_input.open(new File(importedImage));
            } catch (IOException exc) {
                JOptionPane.showMessageDialog(null, exc.toString());
                return;
            }
            */
            //
            // Continuous Handwriting
            // 
            
            if (index == 0) {
                try {
                    String fileName = "Export\\" + importedImage.substring(importedImage.lastIndexOf("\\")+1).split("\\.")[0].concat(".pdf");
                    new ImagePanel(infoFileName, name, surname, clas, sex, fileName, importedImage, exportedImage);
                } catch (IOException exc) {
                    JOptionPane.showMessageDialog(null, exc.toString());
                    return;
                }
            }
            
            
            //
            // Separated Handwriting
            //
            
            if (index == 1) {
                try {
                    SeparatedHandwriting.Recognition(infoFileName);
                } catch (IOException exc) {
                    JOptionPane.showMessageDialog(null, exc.toString());
                    return;
                }
            }
            
            //
            // Text File Viewer
            //
            
            //new TextFileViewer(exportedOutput);
            
            //
            // Image Viewer
            //
            
            /*
            Desktop dt_output = Desktop.getDesktop();
            try {
                dt_output.open(new File(exportedImage));
            } catch (IOException exc) {
                JOptionPane.showMessageDialog(null, exc.toString());
            }
            */
        }

    };
    
    private void saveParametersToFile(String fileName) throws IOException {
        PrintWriter file = new PrintWriter(new FileWriter(fileName));
        file.println("Sample width:");
        file.println(sampleWidth);
        file.println("Sample height:");
        file.println(sampleHeight);
        file.println("Hidden layer:");
        file.println(hiddenLayer);
        file.println("Output layer:");
        file.println(outputLayer);
        file.println("Dictionary file:");
        file.println(dictionaryFile);
        file.println("Model file:");
        file.println(modelFile);
        file.println("Imported image:");
        file.println(importedImage);
        file.println("Exported image:");
        file.println(exportedImage);
        file.println("Exported text:");
        file.println(exportedOutput);
        file.close();
    }
    
    private ActionListener optionBoxAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = optionBox.getSelectedIndex();
            //importedImageText.setText(importedImageList[index]);
        }
    };
    
    private ActionListener dictionaryFileAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            File workingDirectory = new File(System.getProperty("user.dir"));
            fileChooser.setCurrentDirectory(workingDirectory);
            fileChooser.showSaveDialog(null);
            File file = null;
            try {
                file = fileChooser.getSelectedFile();
            } catch (Exception exc) {
                return;
            }
            if (file != null) {
                dictionaryFileText.setText(file.getPath());
            }
        }
    };
    
    private ActionListener modelFileAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            File workingDirectory = new File(System.getProperty("user.dir"));
            fileChooser.setCurrentDirectory(workingDirectory);
            fileChooser.showSaveDialog(null);
            File file = null;
            try {
                file = fileChooser.getSelectedFile();
            } catch (Exception exc) {
                return;
            }
            if (file != null) {
                modelFileText.setText(file.getPath());
            }
        }
    };
    
    private ActionListener importedImageAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            File workingDirectory = new File(System.getProperty("user.dir"));
            fileChooser.setCurrentDirectory(workingDirectory);
            fileChooser.showSaveDialog(null);
            File file = null;
            try {
                file = fileChooser.getSelectedFile();
            } catch (Exception exc) {
                return;
            }
            if (file != null) {
                importedImageText.setText(file.getPath());
            }
        }
    };
    
    private ActionListener exportedImageAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            File workingDirectory = new File(System.getProperty("user.dir"));
            fileChooser.setCurrentDirectory(workingDirectory);
            fileChooser.showSaveDialog(null);
            File file = null;
            try {
                file = fileChooser.getSelectedFile();
            } catch (Exception exc) {
                return;
            }
            if (file != null) {
                exportedImageText.setText(file.getPath());
            }
        }
    };
    
    private ActionListener exportedOutputAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            File workingDirectory = new File(System.getProperty("user.dir"));
            fileChooser.setCurrentDirectory(workingDirectory);
            fileChooser.showSaveDialog(null);
            File file = null;
            try {
                file = fileChooser.getSelectedFile();
            } catch (Exception exc) {
                return;
            }
            if (file != null) {
                exportedOutputText.setText(file.getPath());
            }
        }
    };
    
    private ActionListener aboutButtonAL = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Index();
        }
    };
    
    private void initGUI() {
        int x = marginFrame;
        int y = marginFrame;
        
        // 
        // Option Box
        //
        
        optionLabel = new JLabel();
        optionLabel.setText("Opzioni:");
        optionLabel.setBounds(x, y, frameWidth - 2 * marginFrame, heightComponent);
        optionLabel.setForeground(Color.blue);
        add(optionLabel);
        
        y += heightComponent;
        
        optionBox = new JComboBox(options);
        optionBox.setBounds(x, y, frameWidth - 2 * marginFrame, heightComponent);
        add(optionBox);
        optionBox.addActionListener(optionBoxAL);
        
        //
        // Sample width
        //
        
        y += heightComponent + marginFrame;
        
        nameLabel = new JLabel();
        nameLabel.setText("Nome:");
        nameLabel.setBounds(x, y, labelWidth, heightComponent);
        nameLabel.setForeground(Color.blue);
        add(nameLabel);
        
        nameText = new JTextField();
        nameText.setBounds(x + labelWidth, y, 
                frameWidth - 2 * marginFrame - labelWidth, heightComponent);
        add(nameText);
        
        //
        // Sample height
        //
        
        y += heightComponent + marginFrame;
        
        surnameLabel = new JLabel();
        surnameLabel.setText("Cognome:");
        surnameLabel.setBounds(x, y, labelWidth, heightComponent);
        surnameLabel.setForeground(Color.blue);
        add(surnameLabel);
        
        surnameText = new JTextField();
        surnameText.setBounds(x + labelWidth, y, 
                frameWidth - 2 * marginFrame - labelWidth, heightComponent);
        add(surnameText);
        
        //
        // Hidden layer
        //
        
        y += heightComponent + marginFrame;
        
        classLabel = new JLabel();
        classLabel.setText("Classe:");
        classLabel.setBounds(x, y, labelWidth, heightComponent);
        classLabel.setForeground(Color.blue);
        add(classLabel);
        
        
        optionClasBox = new JComboBox(optionsClas);
        optionClasBox.setBounds(x + labelWidth, y, frameWidth - 8 * marginFrame, heightComponent);
        add(optionClasBox);
        optionClasBox.addActionListener(optionBoxAL);
        
        y += heightComponent + marginFrame;
        
        sexLabel = new JLabel();
        sexLabel.setText("Sesso:");
        sexLabel.setBounds(x, y, labelWidth, heightComponent);
        sexLabel.setForeground(Color.blue);
        add(sexLabel);
        
        
        optionSexBox = new JComboBox(optionsSex);
        optionSexBox.setBounds(x + labelWidth, y, frameWidth - 8 * marginFrame, heightComponent);
        add(optionSexBox);
        optionSexBox.addActionListener(optionBoxAL);
        
        /*
        hiddenLayerText = new JTextField();
        hiddenLayerText.setText(Integer.toString(hiddenLayer));
        hiddenLayerText.setBounds(x + labelWidth, y, 
                frameWidth - 2 * marginFrame - labelWidth, heightComponent);
        add(hiddenLayerText);
        */
        
        //
        // Output layer
        //
        
        /*
        y += heightComponent + marginFrame;
        
        outputLayerLabel = new JLabel();
        outputLayerLabel.setText("Output layer:");
        outputLayerLabel.setBounds(x, y, labelWidth, heightComponent);
        outputLayerLabel.setForeground(Color.blue);
        add(outputLayerLabel);
        
        outputLayerText = new JTextField();
        outputLayerText.setText(Integer.toString(outputLayer));
        outputLayerText.setBounds(x + labelWidth, y, 
                frameWidth - 2 * marginFrame - labelWidth, heightComponent);
        add(outputLayerText);
        outputLayerText.setEditable(false);
        
        //
        // Dictionary File
        //
        
        y += heightComponent + marginFrame;
        
        dictionaryFileLabel = new JLabel();
        dictionaryFileLabel.setText("Dictionary file:");
        dictionaryFileLabel.setBounds(x, y, frameWidth - 2 * marginFrame, heightComponent);
        dictionaryFileLabel.setForeground(Color.blue);
        add(dictionaryFileLabel);
        
        y += heightComponent;
        
        dictionaryFileText = new JTextField();
        dictionaryFileText.setText(dictionaryFile);
        dictionaryFileText.setBounds(x, y, frameWidth - 3 * marginFrame - buttonWidth, heightComponent);
        dictionaryFileText.setEditable(false);
        add(dictionaryFileText);
        
        x = frameWidth - marginFrame - buttonWidth;
        
        dictionaryFileButton = new JButton("Browse");
        dictionaryFileButton.setBounds(x, y, buttonWidth, heightComponent);
        add(dictionaryFileButton);
        dictionaryFileButton.addActionListener(dictionaryFileAL);
        
        //
        // Model File
        //
        
        x = marginFrame;
        y += heightComponent;
        
        modelFileLabel = new JLabel();
        modelFileLabel.setText("Model file:");
        modelFileLabel.setBounds(x, y, frameWidth - 2 * marginFrame, heightComponent);
        modelFileLabel.setForeground(Color.blue);
        add(modelFileLabel);
        
        y += heightComponent;
        
        modelFileText = new JTextField();
        modelFileText.setText(modelFile);
        modelFileText.setBounds(x, y, frameWidth - 3 * marginFrame - buttonWidth, heightComponent);
        modelFileText.setEditable(false);
        add(modelFileText);
        
        x = frameWidth - marginFrame - buttonWidth;
        
        modelFileButton = new JButton("Browse");
        modelFileButton.setBounds(x, y, buttonWidth, heightComponent);
        add(modelFileButton);
        modelFileButton.addActionListener(modelFileAL);
        
        //
        // Input image
        //
        */
        x = marginFrame;
        y += heightComponent;
        
        importedImageLabel = new JLabel();
        importedImageLabel.setText("Immagine input:");
        importedImageLabel.setBounds(x, y, frameWidth - 2 * marginFrame, heightComponent);
        importedImageLabel.setForeground(Color.blue);
        add(importedImageLabel);
        
        y += heightComponent;
        
        importedImageText = new JTextField();
        importedImageText.setBounds(x, y, frameWidth - 3 * marginFrame - buttonWidth, heightComponent);
        importedImageText.setEditable(false);
        add(importedImageText);
        
        x = frameWidth - marginFrame - buttonWidth;
        
        importedImageButton = new JButton("Cerca");
        importedImageButton.setBounds(x, y, buttonWidth, heightComponent);
        add(importedImageButton);
        importedImageButton.addActionListener(importedImageAL);
        
        //
        // Output image
        //
        
        x = marginFrame;
        y += heightComponent;
        
        exportedImageLabel = new JLabel();
        exportedImageLabel.setText("Immagine output:");
        exportedImageLabel.setBounds(x, y, frameWidth - 2 * marginFrame, heightComponent);
        exportedImageLabel.setForeground(Color.blue);
        add(exportedImageLabel);
        
        y += heightComponent;
        
        exportedImageText = new JTextField();
        exportedImageText.setText(exportedImage);
        exportedImageText.setBounds(x, y, frameWidth - 3 * marginFrame - buttonWidth, heightComponent);
        add(exportedImageText);
        
        x = frameWidth - marginFrame - buttonWidth;
        
        exportedImageButton = new JButton("Cerca");
        exportedImageButton.setBounds(x, y, buttonWidth, heightComponent);
        add(exportedImageButton);
        exportedImageButton.addActionListener(exportedImageAL);
        
        //
        // Output text
        //
        
        x = marginFrame;
        
        /*
        y += heightComponent;
        
        exportedOutputLabel = new JLabel();
        exportedOutputLabel.setText("Output text:");
        exportedOutputLabel.setBounds(x, y, frameWidth - 2 * marginFrame, heightComponent);
        exportedOutputLabel.setForeground(Color.blue);
        add(exportedOutputLabel);
        
        
        y += heightComponent;
        
        exportedOutputText = new JTextField();
        exportedOutputText.setText(exportedOutput);
        exportedOutputText.setBounds(x, y, frameWidth - 3 * marginFrame - buttonWidth, heightComponent);
        add(exportedOutputText);
        
        x = frameWidth - marginFrame - buttonWidth;
        
        exportedOutputButton = new JButton("Cerca");
        exportedOutputButton.setBounds(x, y, buttonWidth, heightComponent);
        add(exportedOutputButton);
        exportedOutputButton.addActionListener(exportedOutputAL);
        
        */
        //
        // Start Button
        //
        
        x = frameWidth - marginFrame - buttonWidth;
        y += 2 * heightComponent + marginFrame;
        
        startButton = new JButton("Avvio");
        startButton.setBounds(x, y, buttonWidth, heightComponent);
        startButton.addActionListener(startButtonAL);
        add(startButton);
        
        //
        // About Button
        //
        
        x -= 3 * (buttonWidth + marginFrame);
        
        aboutButton = new JButton("Indice");
        aboutButton.setBounds(x, y, buttonWidth, heightComponent);
        aboutButton.addActionListener(aboutButtonAL);
        add(aboutButton);

    }
}
