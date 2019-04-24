// Software: Dysgraphia Diagnosis in Java

package Metrics;

/**
 *
 * @author utente1
 */
public class WrittenFileException extends Exception{
    public WrittenFileException() {
        super("Written file exception");
    }
    
    public WrittenFileException(String s) {
        super(s);
    }
}
