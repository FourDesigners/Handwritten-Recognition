/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Representation of entire PDF file.
 *
 */
public class PDF {

    private CatalogObject catalogObject;

    private int objectCount = 0;

    public PDF(CatalogObject catalogObject) {
        this.catalogObject = catalogObject;
    }

    public String build() {
        populateObjectNumbers();
        StringBuilder pdf = new StringBuilder();
        pdf.append("%PDF-1.1\n\n");

        pdf.append(catalogObject.build());
        pdf.append(catalogObject.getPages().build());

        for (PageObject page : catalogObject.getPages().getPages()) {
            pdf.append(page.build());
            if (page.getContent() != null) {
                pdf.append(page.getContent().build());
            }
        }

        pdf.append("trailer\n  << /Root " + catalogObject.getReference().getObjectNumber() + " "
                + catalogObject.getReference().getGeneration() + " R" + "\n   /Size " + (objectCount + 1) + "\n  >>\n"
                + "%%EOF");

        return pdf.toString();
    }

    /**
     * Populate object numbers to avoid manual numbering.
     */
    private void populateObjectNumbers() {
        catalogObject.setObjectNumber(++objectCount);
        catalogObject.getPages().setObjectNumber(++objectCount);

        for (PageObject page : catalogObject.getPages().getPages()) {
            page.setObjectNumber(++objectCount);

            if (page.getContent() != null) {
                page.getContent().setObjectNumber(++objectCount);

            }
        }
    }

}
