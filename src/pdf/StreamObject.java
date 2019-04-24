/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdf;

/**
 * Abstract Representation of stream object
 *
 */
abstract class StreamObject extends PDFObject {

    public StreamObject() {
        super(null);
    }

    public abstract String buildStream();

    public void addSpecificAttributes() {
        addAttribute("Length", Integer.valueOf(100));
    }

    @Override
    public StringBuilder buildObject() {
        StringBuilder sb = super.buildObject();
        sb.append("\nstream").append(buildStream()).append("\nendstream");
        return sb;
    }

}
