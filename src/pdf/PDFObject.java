package pdf;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract Representation of PDF objects. All objects in PDF must extend this.
 *
 */
abstract class PDFObject {

    private PDFObjectReference reference = new PDFObjectReference();

    private Map<String, Object> attributes = new HashMap<>();

    public PDFObject(String type) {
        super();
        this.attributes.put("Type", type);
    }

    public void addAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    public abstract void addSpecificAttributes();

    public void setObjectNumber(int objectNumber) {
        this.reference.setObjectNumber(objectNumber);
    }

    PDFObjectReference getReference() {
        return reference;
    }
    
    public String build() {

        addSpecificAttributes();

        StringBuilder pdfObject = new StringBuilder();
        pdfObject.append(reference.getObjectNumber()).append(" ").append(reference.getGeneration()).append(" obj\n  ")
                .append(buildObject()).append("\nendobj\n\n");

        return pdfObject.toString();
    }

    public StringBuilder buildObject() {
        StringBuilder pdfObject = new StringBuilder();
        pdfObject.append("<< \n");

        for (String key : attributes.keySet()) {

            Object value = attributes.get(key);
            if (value instanceof String) {
                pdfObject.append("\n     /").append(key).append(" ").append(((String) value).contains("[") ? "" : "/")
                        .append(value);
            } else if (value instanceof Integer) {
                pdfObject.append("\n     /").append(key).append(" ").append(value);
            } else if (value instanceof PDFObject) {
                pdfObject.append("\n     /").append(key).append(" \n").append(((PDFObject) value).buildObject());
            } else if (value instanceof PDFObjectReference[]) {

                pdfObject.append("\n     /").append(key).append(" [");
                for (PDFObjectReference ref : (PDFObjectReference[]) value) {
                    pdfObject.append(ref.getObjectNumber() + " " + ref.getGeneration() + " R ");
                }
                pdfObject.append("]");
            } else if (value instanceof PDFObjectReference) {
                pdfObject.append("\n     /").append(key).append(" ")
                        .append(((PDFObjectReference) value).getObjectNumber() + " "
                                + ((PDFObjectReference) value).getGeneration() + " R ");
            }
        }
        pdfObject.append("  >>");

        return pdfObject;
    }

}
