/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Metrics;

import LibOCR.aRect;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author utente1
 */
public class CalculateMetrics {
    private static final int NTABLEROWS = 4;
    private static final int NTABLECOLUMNS = 7;
    private static final int[][] heightScores = new int[NTABLEROWS][NTABLECOLUMNS];
    private static final int NCHARACTERS = 5;
    private static final int NFIRSTLINE = 4;
    private static final int NSECONDLINE = 3;
    private static final int NTHIRDLINE = 3;
    private static final int NFORTHLINE = 4;
    private static final int NFIFTHLINE = 3;
    private static final int NWORDS = 17;
    private static final int NROWS = 5;
    private static final int MAXNRECTS = 2000;
    private static final int MAXNWORDS = 100;
    private static final String exportedOutput = "Export/demo-output.txt";
    
    private static final aRect FirstColumn[] = new aRect[NROWS];
    private static final aRect FirstLine[] = new aRect[NFIRSTLINE];
    private static final aRect SecondLine[] = new aRect[NSECONDLINE];
    private static final aRect ThirdLine[] = new aRect[NTHIRDLINE];
    private static final aRect ForthLine[] = new aRect[NFORTHLINE];
    private static final aRect FifthLine[] = new aRect[NFIFTHLINE];
    private static final String TextFirstLine[] = new String[NFIRSTLINE];
    private static final String TextSecondLine[] = new String[NSECONDLINE];
    private static final String TextThirdLine[] = new String[NTHIRDLINE];
    private static final String TextForthLine[] = new String[NFORTHLINE];
    private static final String TextFifthLine[] = new String[NFIFTHLINE];
    
    private static final aRect Words[] = new aRect[MAXNRECTS];
    private static final String[] textWords = new String[MAXNWORDS];
    
    private static int nWords = 0;
    private static int avgHeightScore;
    private static int oWidthScore;
    private static double margin;
    private static double oWidth;
    
    public static void start(aRect w[], int n, int clas, int InfoGray[][]) throws IOException {
        initializeMatrix();
        clearPoints(w, n);
        orderWordsVertically(Words, textWords, NWORDS+NCHARACTERS);
        avgHeightScore = calculateHeightCharacters(clas);
        separateLines(Words);
        margin = calculateMargin(FirstColumn);
        oWidth = Words[2].x2-Words[2].x1;
        oWidthScore = calculateWidthWords();
        /*
        for(int i=Words[5].x1; i<Words[5].x2; i++) {
            for(int j=Words[5].y1; j<Words[5].y2; j++) {
                if(InfoGray[i][j] != 255)
                    System.out.print(String.format("%3d ", InfoGray[i][j]));
                else System.out.print("    ");
            }
            System.out.println();
        }
        */
        printResults();
        
    }
    
    private static void initializeMatrix() {
        heightScores[0][0] = 0;
        for(int j=1; j<NTABLECOLUMNS-1; j++) {
            heightScores[0][j] = j-1;
        }
        for(int i=1; i<NTABLEROWS; i++) {
            for(int j=0; j<NTABLECOLUMNS-1; j++){
                heightScores[i][j] = j;
            }
        }
        for(int i=0; i<NTABLEROWS; i++) {
            heightScores[i][NTABLECOLUMNS-1] = 5;
        }
    }
    
    private static int calculateHeightCharacters(int clas) {
        double avg = 0;
        int column;
        for(int i=0; i<NCHARACTERS; i++) {
            avg += Words[i].y2-Words[i].y1;
        }
        avg /= NCHARACTERS;
        avg = PixelToMm(avg);
        int intAvg = (int) avg;
        if(intAvg > 9) {
            column = 6;
        }else if(intAvg < 3) {
            column = 0;
        }else {
            column = intAvg - 3;
        }
        return heightScores[clas-2][column];
    }
    
    private static int PixelToMm(double d) {
        int dpi = 30; 
        return (int) Math.round(d/7.8);
    }
    
    private static double calculateMargin(aRect[] FirstColumn) {
        int min, max;
        max = 0;
        
        for(int i=0; i<NROWS; i++) {
            if(FirstColumn[i].x1 > max) {
                max = FirstColumn[i].x1;
            }
        }
        min = max;
        for(int i=0; i<NROWS; i++) {
            if(FirstColumn[i].x1 < min) {
                min = FirstColumn[i].x1;
            }
        }
        return PixelToMm(max-min);
    }
    
    private static void separateLines(aRect Words[]) {
        //boolean[] inserted = new boolean[NWORDS];
        int start;
        for(int i=0; i<NWORDS; i++) {
            //inserted[i] = false;
        }
        start = NCHARACTERS;
        for(int i=start; i<start+NFIRSTLINE; i++) {
            FirstLine[i-start] = Words[i];
            TextFirstLine[i-start] = textWords[i];
        }
        orderWordsHorizontally(FirstLine, TextFirstLine, NFIRSTLINE);
        FirstColumn[0] = FirstLine[0];
        
        start += NFIRSTLINE;
        for(int i=start; i<start+NSECONDLINE; i++) {
            SecondLine[i-start] = Words[i];
            TextSecondLine[i-start] = textWords[i];
        }
        orderWordsHorizontally(SecondLine, TextSecondLine, NSECONDLINE);
        FirstColumn[1] = SecondLine[0];
        
        start += NSECONDLINE;
        for(int i=start; i<start+NTHIRDLINE; i++) {
            ThirdLine[i-start] = Words[i];
            TextThirdLine[i-start] = textWords[i];
        }
        orderWordsHorizontally(ThirdLine, TextThirdLine, NTHIRDLINE);
        FirstColumn[2] = ThirdLine[0];
        
        start += NTHIRDLINE;
        for(int i=start; i<start+NFORTHLINE; i++) {
            ForthLine[i-start] = Words[i];
            TextForthLine[i-start] = textWords[i];
        }
        orderWordsHorizontally(ForthLine, TextForthLine, NFORTHLINE);
        FirstColumn[3] = ForthLine[0];
        
        start += NFORTHLINE;
        for(int i=start; i<start+NFIFTHLINE; i++) {
            FifthLine[i-start] = Words[i];
            TextFifthLine[i-start] = textWords[i];
        }
        orderWordsHorizontally(FifthLine, TextFifthLine, NFIFTHLINE);
        FirstColumn[4] = FifthLine[0];
    }
    
    private static void clearPoints(aRect w[], int n) throws FileNotFoundException, IOException {
        BufferedReader in = new BufferedReader(new FileReader(exportedOutput));
        int minimumPixel = 20;
        for(int i=0; i<n; i++) {
            String s = in.readLine();
            if(w[i].x2-w[i].x1>minimumPixel && w[i].y2-w[i].y1>minimumPixel) {
                Words[nWords] = w[i];
                textWords[nWords] = s;
                nWords++;
            }
        }
        in.close();
    }
    
    private static void orderWordsVertically(aRect[] Words, String[] words, int lenght) {
        aRect temp;
        String textTemp;
        for(int i=0; i<lenght-1; i++) {
            for(int j=i+1; j<lenght; j++) {
                if(Words[i].y1>Words[j].y1) {
                    temp = Words[i];
                    Words[i] = Words[j];
                    Words[j] = temp;
                    textTemp = words[i];
                    words[i] = words[j];
                    words[j] = textTemp;
                }
            }
        }
    }
    
    private static void orderWordsHorizontally(aRect[] Words, String[] words, int lenght) {
        aRect temp;
        String textTemp;
        for(int i=0; i<lenght-1; i++) {
            for(int j=i+1; j<lenght; j++) {
                if(Words[i].x1>Words[j].x1) {
                    temp = Words[i];
                    Words[i] = Words[j];
                    Words[j] = temp;
                    textTemp = words[i];
                    words[i] = words[j];
                    words[j] = textTemp;
                }
            }
        }
    }
    
    private static int calculateWidthWords() {
        boolean found = false;
        int count = 0;
        for(int i=1; i<NFIRSTLINE && !found; i++) {
            if(FirstLine[i].x1-FirstLine[i-1].x2 < oWidth) {
                found = true;
                count++;
            }
        }
        found = false;
        for(int i=1; i<NSECONDLINE && !found; i++) {
            if(SecondLine[i].x1-SecondLine[i-1].x2 < oWidth) {
                found = true;
                count++;
            }
        }
        found = false;
        for(int i=1; i<NTHIRDLINE && !found; i++) {
            if(ThirdLine[i].x1-ThirdLine[i-1].x2 < oWidth) {
                found = true;
                count++;
            }
        }
        found = false;
        for(int i=1; i<NFORTHLINE && !found; i++) {
            if(ForthLine[i].x1-ForthLine[i-1].x2 < oWidth) {
                found = true;
                count++;
            }
        }
        found = false;
        for(int i=1; i<NFIFTHLINE && !found; i++) {
            if(FifthLine[i].x1-FifthLine[i-1].x2 < oWidth) {
                found = true;
                count++;
            }
        }
        return count;
    }
    
    private static void printResults() {
        System.out.println("Punteggio altezza caratteri : " + avgHeightScore);
        System.out.println("Margine : " + margin);
        System.out.println("Numero righe con spazio insufficiente : " + oWidthScore);
    }
}
