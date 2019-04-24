/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representation of text stream object
 *
 */
public class GraphicStreamObject extends StreamObject {

    private static final String MOVE_POINTER = "m";
    private static final String LINE = "l";
    private static final String LINE_WIDTH = "w";
    private static final String RECTANGLE = "re";
    private static final String FILL = "f";
    private static final String BEZIER_CURVE = "c";
    private static final String BORDER_COLOR = "rg";
    private static final String FILL_COLOR = "RG";
    private static final String STROKE = "S";
    private static final String CLOSE_FILL_STROKE = "b";

    private List<String> graphics = new ArrayList<>();

    public void addLine(int xFrom, int yFrom, int xTo, int yTo) {
        this.graphics.add(
                "\n " + xFrom + " " + yFrom + " " + MOVE_POINTER + " " + xTo + " " + yTo + " " + LINE + " " + STROKE);
    }

    public void addRectangle(int a, int b, int c, int d) {
        this.graphics.add("\n " + a + " " + b + " " + c + " " + d + " " + RECTANGLE + " " + STROKE);
    }

    public void addFilledRectangle(int a, int b, int c, int d, String color) {
        this.graphics.add("\n" + color);
        this.graphics.add("\n " + a + " " + b + " " + c + " " + d + " " + RECTANGLE + " " + FILL + " " + STROKE);
    }

    public void addBezierCurve(int movex, int movey, int a, int b, int c, int d, int e, int f, String borderColor,
            int borderWidth, String fillColor) {
        this.graphics.add("\n" + borderWidth + " " + LINE_WIDTH);
        this.graphics.add("\n" + fillColor + " " + FILL_COLOR);
        this.graphics.add("\n" + borderColor + " " + BORDER_COLOR);
        this.graphics.add("\n" + movex + " " + movey + " " + MOVE_POINTER);
        this.graphics.add("\n " + a + " " + b + " " + c + " " + d + " " + e + " " + f + " " + BEZIER_CURVE + " \n "
                + CLOSE_FILL_STROKE);
    }

    @Override
    public String buildStream() {
        return graphics.stream().collect(Collectors.joining());
    }

}
