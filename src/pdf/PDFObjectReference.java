/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdf;

/**
 * Representation of reference to any PDF object.
 *
 */
class PDFObjectReference {
	private int objectNumber;
 
	private int generation = 0; // Hardcode as it remains same always
 
	int getObjectNumber() {
		return objectNumber;
	}
 
	int getGeneration() {
		return generation;
	}
 
	void setObjectNumber(int objectNumber) {
		this.objectNumber = objectNumber;
	}
 
}
