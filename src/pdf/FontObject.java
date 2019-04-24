/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdf;

/**
 * Representation of font object
 *
 */
public class FontObject extends PDFObject {

    public FontObject(final String fontAliasName, final String fontName) {
        super(null);

        final PDFObject fontDef = new PDFObject("Font") {
            @Override
            public void addSpecificAttributes() {
                addAttribute("Subtype", "Type1");
                addAttribute("BaseFont", fontName);
            }
        };
        fontDef.addSpecificAttributes();

        PDFObject fontAlias = new PDFObject(null) {
            @Override
            public void addSpecificAttributes() {
                addAttribute(fontAliasName, fontDef);
            }
        };
        fontAlias.addSpecificAttributes();

        addAttribute("Font", fontAlias);
    }

    @Override
    public void addSpecificAttributes() {

    }

}
