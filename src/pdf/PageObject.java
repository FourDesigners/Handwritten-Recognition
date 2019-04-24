/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdf;

/**
 * Representation of page object.
 *
 */
public class PageObject extends PDFObject {

    private StreamObject content;

    public PageObject() {
        super("Page");
    }

    public void addContent(StreamObject streamObject) {
        content = streamObject;
    }

    @Override
    public void addSpecificAttributes() {
        addAttribute("Contents", content.getReference());
    }

    StreamObject getContent() {
        return content;
    }

}
