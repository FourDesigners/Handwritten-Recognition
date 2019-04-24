// Software: Dysgraphia Diagnosis in Java

package Metrics;

import LibOCR.aRect;
import handwriting.recognition.Index;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author utente1
 */
public class CalculateMetrics {
    private static final int NTABLEROWS = 4;
    private static final int NTABLECOLUMNS = 7;
    private static final int[][] heightScores = new int[NTABLEROWS][NTABLECOLUMNS];
    private static final int NCHARACTERS = 6;
    private static final int NFIRSTLINE = 4;
    private static final int NSECONDLINE = 3;
    private static final int NTHIRDLINE = 3;
    private static final int NFORTHLINE = 4;
    private static final int NFIFTHLINE = 3;
    private static final int NWORDS = 17;
    private static final int NROWS = 5;
    private static final int MAXNRECTS = 2000;
    private static final int MAXNWORDS = 100;
    //private static final String exportedOutput = "Export/demo-output.txt";
    private static final int ABOVETHRESHOLD = 50;
    private static final int BELOWTHRESHOLD = 10;
    private static final int POINTSTHRESHOLD = 65;
    
    public static int avgthreshold, maxthreshold, wordsthreshold;
    public static double ratethreshold;
    
    private static final aRect FirstColumn[] = new aRect[NROWS];
    private static final aRect FirstLine[] = new aRect[NFIRSTLINE];
    private static final aRect SecondLine[] = new aRect[NSECONDLINE];
    private static final aRect ThirdLine[] = new aRect[NTHIRDLINE];
    private static final aRect ForthLine[] = new aRect[NFORTHLINE];
    private static final aRect FifthLine[] = new aRect[NFIFTHLINE];
    private static final aRect Header[] = new aRect[NCHARACTERS];
    private static final aRect Footer[] = new aRect[NCHARACTERS];
    private static int offset = 0;
    
    private static HashSet<Integer> setFirst = new HashSet<>();
    private static HashSet<Integer> setSecond = new HashSet<>();
    private static HashSet<Integer> setThird = new HashSet<>();
    private static HashSet<Integer> setForth = new HashSet<>();
    private static HashSet<Integer> setFifth = new HashSet<>();
    private static final aRect Words[] = new aRect[MAXNRECTS];
    
    private static int nWords = 0;
    
    private static int avgHeightScore;
    private static int oWidthScore;
    private static int marginScore;
    private static double oWidth;
    private static int skewScore;
    private static int stretchScore;
    private static String[] logs = new String[5];
    private static LineForHeight[] lines;
    
    public static void start(aRect w[], int n, String name, String surname, int clas, int sex, int InfoGray[][], String fileName, String export, LineForHeight[] lines) throws IOException, WrittenFileException {
        CalculateMetrics.lines = lines;
        initializeMatrix();
        clearPoints(w, n);
        if(nWords < NCHARACTERS + NWORDS) {
            separateWords(InfoGray);
        }
        orderWordsVertically(Words, NWORDS+NCHARACTERS);
        separateLines(Words);
        
        avgHeightScore = calculateHeightCharacters(clas);
        
        marginScore = calculateMargin(FirstColumn);
       
        skewScore = calculateSkew(InfoGray);
        
        oWidth = (Header[2].x2-Header[2].x1+Header[3].x2-Header[3].x1)/2;
        oWidthScore = calculateDistanceWords();
        
        stretchScore = calculateStretch(FirstLine, SecondLine, ThirdLine, ForthLine, FifthLine);
        
        new ParametersFrame(name, surname, clas, sex, avgHeightScore, marginScore, skewScore, oWidthScore, stretchScore, logs, fileName, export, lines);
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
        int intAvg = (int) Math.round(avg);
        if(intAvg > 9) {
            column = 6;
        }else if(intAvg < 3) {
            column = 0;
        }else {
            column = intAvg - 3;
        }
        logs[0] = "Altezza caratteri media : " + intAvg + " mm";
        return heightScores[clas-2][column];
    }
    
    public static double PixelToMm(double d) {
        int dpi = 30; 
        return d/7.8;
    }
    
    private static int calculateMargin(aRect[] FirstColumn) {
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
        int margin = (int) Math.round(PixelToMm(max-min));
        logs[1] = ("Margine : " + margin + " mm");
        if(margin < 15) {
            return 0;
        }
        if(margin < 27) {
            return 1;
        }
        if(margin < 38) {
            return 2;
        }
        if(margin < 50) {
            return 3;
        }
        if(margin < 62) {
            return 4;
        }
        return 5;
    }
    
    private static void separateLines(aRect Words[]) {
        //boolean[] inserted = new boolean[NWORDS];
        int start;
        for(int i=0; i<NCHARACTERS; i++) {
            Header[i] = Words[i];
        }
        orderWordsHorizontally(Header, NCHARACTERS);
        
        start = NCHARACTERS;
        for(int i=start; i<start+NFIRSTLINE; i++) {
            FirstLine[i-start] = Words[i];
        }
        orderWordsHorizontally(FirstLine, NFIRSTLINE);
        FirstColumn[0] = FirstLine[0];
        
        start += NFIRSTLINE;
        for(int i=start; i<start+NSECONDLINE; i++) {
            SecondLine[i-start] = Words[i];
        }
        orderWordsHorizontally(SecondLine, NSECONDLINE);
        FirstColumn[1] = SecondLine[0];
        
        start += NSECONDLINE;
        for(int i=start; i<start+NTHIRDLINE; i++) {
            ThirdLine[i-start] = Words[i];
        }
        orderWordsHorizontally(ThirdLine, NTHIRDLINE);
        FirstColumn[2] = ThirdLine[0];
        
        start += NTHIRDLINE;
        for(int i=start; i<start+NFORTHLINE; i++) {
            ForthLine[i-start] = Words[i];
        }
        orderWordsHorizontally(ForthLine, NFORTHLINE);
        FirstColumn[3] = ForthLine[0];
        
        start += NFORTHLINE;
        for(int i=start; i<start+NFIFTHLINE; i++) {
            FifthLine[i-start] = Words[i];
        }
        orderWordsHorizontally(FifthLine, NFIFTHLINE);
        FirstColumn[4] = FifthLine[0];
    }
    
    private static void clearPoints(aRect w[], int n) throws FileNotFoundException, IOException {
        //try (BufferedReader in = new BufferedReader(new FileReader(exportedOutput))) {
            int minimumPixel = 8;
            for(int i=0; i<n; i++) {
                //String s = in.readLine();
                if(w[i].x2-w[i].x1>minimumPixel && w[i].y2-w[i].y1>minimumPixel) {
                    Words[nWords] = w[i];
                    //textWords[nWords] = s;
                    nWords++;
                }
            }
        //}
    }
    
    private static void separateWords(int[][] InfoGray) {
        int tall;
        for(int k=nWords; k<NCHARACTERS + NWORDS; k++) {
            tall = 0;
            for(int j=0; j<nWords; j++) {
                if(Words[tall].y2-Words[tall].y1 < Words[j].y2-Words[j].y1) {
                    tall = j;
                }
            }
            int[] count = new int[Words[tall].y2 - Words[tall].y1 + 1];
            for(int i=Words[tall].y1; i<=Words[tall].y2; i++) {
                count[i-Words[tall].y1] = 0;
                for(int j=Words[tall].x1; j<=Words[tall].x2; j++) {
                    if(InfoGray[j][i] != 255) {
                        //System.out.format("%3d ", InfoGray[j][i]);
                        count[i-Words[tall].y1]++;
                    }else {
                        //System.out.format("    ");
                    }
                }
                //System.out.println();
            }
            
            int cut = 10;
            for (int j = 10; j < count.length-10; j++) {
                //System.out.println(count[j]);
                if (count[cut] > count[j]) {
                    cut = j;
                }
            }
            //System.out.println("Cut : " + cut + " Min : " + count[cut]);
            aRect newWord = new aRect();
            newWord.x1 = Words[tall].x1;
            newWord.x2 = Words[tall].x2;
            newWord.y2 = Words[tall].y2;
            newWord.y1 = Words[tall].y1 + cut;
            Words[tall].y2 = Words[tall].y1 + cut;
            adjust(Words[tall], InfoGray);
            adjust(newWord, InfoGray);
            Words[nWords++] = newWord;
        }
    }
    
    private static void adjust(aRect word, int[][] InfoGray) {
        int[] minAdjust = new int[word.x2-word.x1+1];
        int count = 0;
        
        for (int i = word.x1; i <= word.x2; i++) {
            boolean found = false;
            for (int j=word.y2; j>=word.y1 &&!found; j--) {
                if (InfoGray[i][j] < 100 && isFree(i, j)) {
                    minAdjust[count++] = j;
                    found = true;
                }
            }
            if(!found) {
                minAdjust[count++] = word.y2;
            }
        }
        
        int first = 0;
        while(minAdjust[first]==word.y2 && first<count) {
            first++;
        }
        first += word.x1;
        
        int last = count-1;
        while(minAdjust[last]==word.y2 && last>=0) {
            last--;
        }
        last += word.x1;
        word.x1 = first;
        word.x2 = last;
        //System.out.println("Y2 : " + word.y2);
        for(int i=0; i<count; i++) {
            //System.out.print(minAdjust[i] + " ");
        }
        //System.out.println();
    }
    
    private static boolean isFree(int x, int y) {
        int countW = 0;
        for(int i=0; i<nWords; i++) {
            if(x>=Words[i].x1 && x<=Words[i].x2 && y>=Words[i].y1 && y<=Words[i].y2) {
                countW++;
            }
        }
        return countW<2;
    }
    
    private static void orderWordsVertically(aRect[] Words, int lenght) {
        aRect temp;
        String textTemp;
        for(int i=0; i<lenght-1; i++) {
            for(int j=i+1; j<lenght; j++) {
                if(Words[i].y1>Words[j].y1) {
                    temp = Words[i];
                    Words[i] = Words[j];
                    Words[j] = temp;
                    /*
                    textTemp = words[i];
                    words[i] = words[j];
                    words[j] = textTemp;*/
                }
            }
        }
    }
    
    private static void orderWordsHorizontally(aRect[] Words, int lenght) {
        aRect temp;
        String textTemp;
        for(int i=0; i<lenght-1; i++) {
            for(int j=i+1; j<lenght; j++) {
                if(Words[i].x1>Words[j].x1) {
                    temp = Words[i];
                    Words[i] = Words[j];
                    Words[j] = temp;
                    /*
                    textTemp = words[i];
                    words[i] = words[j];
                    words[j] = textTemp;*/
                }
            }
        }
    }
    
    private static int calculateDistanceWords() {
        boolean found = false;
        int count = 0;
        logs[3] = "";
        for(int i=1; i<NFIRSTLINE && !found; i++) {
            if(FirstLine[i].x1-FirstLine[i-1].x2 < oWidth) {
                logs[3] += ("Linea 1, poco spazio tra la parola " + i + " e la parola " + (i+1)+"\n");
                found = true;
                count++;
            }
        }
        found = false;
        for(int i=1; i<NSECONDLINE && !found; i++) {
            if(SecondLine[i].x1-SecondLine[i-1].x2 < oWidth) {
                logs[3] += ("Linea 2, poco spazio tra la parola " + i + " e la parola " + (i+1)+"\n");
                found = true;
                count++;
            }
        }
        found = false;
        for(int i=1; i<NTHIRDLINE && !found; i++) {
            if(ThirdLine[i].x1-ThirdLine[i-1].x2 < oWidth) {
                logs[3] += ("Linea 3, poco spazio tra la parola " + i + " e la parola " + (i+1)+"\n");
                found = true;
                count++;
            }
        }
        
        found = false;
        for(int i=1; i<NFORTHLINE && !found; i++) {
            if(ForthLine[i].x1-ForthLine[i-1].x2 < oWidth) {
                logs[3] += ("Linea 4, poco spazio tra la parola " + i + " e la parola " + (i+1)+"\n");
                found = true;
                count++;
            }
        }
        
        found = false;
        for(int i=1; i<NFIFTHLINE && !found; i++) {
            if(FifthLine[i].x1-FifthLine[i-1].x2 < oWidth) {
                logs[3] += ("Linea 5, poco spazio tra la parola " + i + " e la parola " + (i+1)+"\n");
                found = true;
                count++;
            }
        }
        return count;
    }

    private static int calculateSkew(int[][] InfoGray) {
        logs[2] = "";
        if(avgthreshold == 0) avgthreshold = Index.AVGTHRESHOLD;
        if(wordsthreshold == 0) wordsthreshold = Index.WORDSTHRESHOLD;
        if(maxthreshold == 0) maxthreshold = Index.MAXTHRESHOLD;
        
        logs[2] += "Baseline parola : " + Math.round(PixelToMm(avgthreshold)*10)/10.0 + " mm, Media parole : " + Math.round(PixelToMm(wordsthreshold)*10)/10.0 + " mm, Baseline parole : " + Math.round(PixelToMm(maxthreshold)*10)/10.0 + " mm\n";
        return calculateLineSkew(FirstLine, NFIRSTLINE, InfoGray, 1) + calculateLineSkew(SecondLine, NSECONDLINE, InfoGray, 2) + calculateLineSkew(ThirdLine, NTHIRDLINE, InfoGray, 3) +
               calculateLineSkew(ForthLine, NFORTHLINE, InfoGray, 4) + calculateLineSkew(FifthLine, NFIFTHLINE, InfoGray, 5);
    }
    
    private static int calculateLineSkew(aRect[] Line, int length, int[][] InfoGray, int line) {
        final int maxWidth = 5000;
        int min[] = new int[maxWidth];
        int minWords[] = new int[NWORDS];
        int avg[] = new int[length];
        int start[] = new int[length];
        offset = 0;
        
        boolean skew = false;
        for(int i=0; i<length && !skew; i++) {
            skew = calculateWordSkew(Line[i], i, min, minWords, avg, InfoGray, line);
        }
        
        start[0] = 0;
        for(int i=1; i<length; i++) {
            start[i] = start[i-1] + Line[i-1].x2 - Line[i-1].x1; 
        }
        //System.out.println();
        if(skew) {
            return 1;
        }
 
        int first = min[0];
        boolean change = false;
        for(int i=1; i<=15 && !change; i++) {
            if(min[i]!=min[i-1]) {
                first = min[i];
                change = true;
            }
        }
        int last = min[offset-1];
        for(int i=offset-2; i>=offset-15; i--) {
            if(min[i]>last) {
                last = min[i];
            }
        }
        for(int i=0; i<offset; i++) {
            //System.out.print(min[i] + " ");
        }
        //System.out.println("\nFirst : " + first + " Last : " + last + " First - last : " + (Math.abs(first-last)));
        if(maxthreshold == 0) maxthreshold = Index.MAXTHRESHOLD;
        //System.out.println(Math.abs(first-last));
        if(Math.abs(first-last) > maxthreshold) {
            logs[2] += ("Linea " + line + ", baseline inclinata\n");
            return 1;
        }
        //int belowCounter = 0;
        boolean bug = false;
        int minRelative = Math.max(first, last);
        
        
        for(int i=0; i<offset && !bug; i++) {
            if(min[i]-minRelative > BELOWTHRESHOLD) {
                //belowCounter++;
            }
        }
        for(int i=1; i<length-1; i++) {
            if(minRelative-minWords[i] > ABOVETHRESHOLD) {
                //System.out.println("Differenza min : " + (minRelative-minWords[i]));
                bug = true;
            }
        }
        //System.out.println("Below counter : " + belowCounter);
        //if(bug || belowCounter>POINTSTHRESHOLD) return 1;
        //System.out.println("Medie : ");
        for(int i=0; i<length-1 && !bug; i++) {
            for(int j=i+1; j<length && !bug; j++) {
                //System.out.print(Math.abs(avg[i] - avg[j]) + " ");
                if(wordsthreshold == 0) wordsthreshold = Index.WORDSTHRESHOLD;
                if(Math.abs(avg[i] - avg[j]) > wordsthreshold) {
                    logs[2] += ("Linea " + line + ", media delle parole incostante\n");
                    bug = true;
                }
            }
        }
        if (bug) return 1;
        return 0;
    }
    
    private static boolean calculateWordSkew(aRect word, int index, int[] min, int[] minWords, int[] avg, int[][] InfoGray, int line) {
        avg[index] = 0;
        minWords[index] = 0;
        int length = word.x2 - word.x1 + 1;
        for(int i=word.x1; i<=word.x2; i++) {
            for(int j=word.y2; j>=word.y1; j--) {
                //System.out.format("%3d ", InfoGray[i][j]);
            }
            
            ///System.out.println();
            
        }
        for (int i = word.x1; i <= word.x2; i++) {
            boolean found = false;
            for (int j=word.y2; j>=word.y1 &&!found; j--) {
                if (InfoGray[i][j] < 100) {
                    min[offset++] = j;
                    if (minWords[index] < j) {
                        minWords[index] = j;
                    }
                    avg[index] += j;
                    found = true;
                }
            }
            if(!found) {
                min[offset++] = word.y2;
                avg[index] += word.y2;
            }
        }
        avg[index] /= length;
        
        //System.out.println(vMin + " " + vMax + " " + avg[index]);
      
        for(int i=0; i<length; i++) {
            //System.out.format("%3d ", min[i]);
        }
        /*
        System.out.println("\nLunghezza : " + length);
        System.out.println("\nInizio : " + min[offset-length]);
        System.out.println("\nMedia : " + avg[index]);
        System.out.println(" Offset media : " + Math.abs(min[offset-length] - avg[index]));
        */
        if(avgthreshold == 0) avgthreshold = Index.AVGTHRESHOLD;
        if((Math.abs(min[offset-length] - avg[index])) > avgthreshold) {
            logs[2] += ("Linea " + line + ", parola inclinata\n");
            return true;
        }
        return false;
    }
    
    private static int calculateStretch(aRect[] FirstLine, aRect[] SecondLine, aRect[] ThirdLine, aRect[] ForthLine, aRect[] FifthLine) {
        logs[4] = "";
        if(ratethreshold == 0) ratethreshold = Index.RATETHRESHOLD;
        logs[4] += "Percentuale : " + Math.round((ratethreshold-1)*100) + " %\n";
        return detectStretch(FirstLine, setFirst, 1) + detectStretch(SecondLine, setSecond, 2) + detectStretch(ThirdLine, setThird, 3) +
               detectStretch(ForthLine, setForth, 4) + detectStretch(FifthLine, setFifth, 5);

    }

    private static int detectStretch(aRect[] Line, HashSet<Integer> set, int line) {
        List<Integer> heightNormalCharacters = new LinkedList<>();
        List<Integer> heightLongCharacters = new LinkedList<>();
      
        for(int i=0; i<Line.length; i++) {
            if(set.contains(i)) {
                heightLongCharacters.add(Line[i].y2 - Line[i].y1);
            }else {
                heightNormalCharacters.add(Line[i].y2 - Line[i].y1); 
            }
        }
        
        for(int heightNormal : heightNormalCharacters) {
            for(int heightLong : heightLongCharacters) {
                double rate = (double) heightLong/ (double) heightNormal;
                if(ratethreshold == 0) {
                    ratethreshold = Index.RATETHRESHOLD;
                }
                if(rate < ratethreshold) {
                    logs[4] += "Linea " + line + ", altezza parole incoerente\n";
                    return 1;
                }
            }
        }
        return 0;
    }
}





