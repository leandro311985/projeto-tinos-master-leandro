package br.com.iresult.gmfmobile.print;

import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.printer.ZebraPrinter;

import java.util.Vector;

public class RW420PrintManager {

    ZebraConnection zebraConnection = new ZebraConnection();


    private static RW420PrintManager printManager = null;

    protected byte[] buffer = new byte[256]; // starts with 256 bytes since readSmallString uses it
    private static final double DOTS_DENSITY = 8;

    public static final String ARIAL_06 = "Ari06Bpt.cpf";

    public static final String ARIAL_09 = "Ari09bpt.cpf";

    public static final int FONT_SIZE_0 = 0;

    public static final int FONT_SIZE_1 = 1;

    public static final int FONT_SIZE_2 = 2;

    public static final int FONT_SIZE_3 = 3;

    public static final int FONT_SIZE_4 = 4;

    public static final int FONT_SIZE_5 = 5;

    public static final int FONT_SIZE_6 = 6;

    public static final int FONT_SIZE_7 = 7;

    public static final int FONT_TYPE_STANDARD = 0;

    public static final int FONT_TYPE_SIGNATURE = 1;

    public static final int FONT_TYPE_OCRA = 2;

    public static final int FONT_TYPE_HLV = 4;

    public static final int FONT_TYPE_TMS = 5;

    public static final int FONT_TYPE_MICR = 6;

    public static final int FONT_TYPE_MONACO = 7;

    public static final String ALIGNMENT_LEFT = "LEFT";

    public static final String ALIGNMENT_RIGHT = "RIGHT";

    public static final String ALIGNMENT_CENTER = "CENTER";

    private StringBuffer template = null;

    private StringBuffer data = null;

    private boolean isTemplateMode = false;



    public String getTemplate() {
        return template.toString();
    }
    /**
     * Método responsável por setar um template para a impressora.
     *
     * A string do template deverá ser passada no seguinte formato, por exemplo:
     *
     * StringBuffer template = new StringBuffer();
     *
     * template.append("! DF CONTA.FMT\n"); template.append("! 0 200 200 2232
     * 1\n"); template.append("LABEL\n"); template.append("CONTRAST 0\n");
     * template.append("TONE 0\n"); template.append("SPEED 5\n");
     * template.append("PAGE-WIDTH 800\n"); template.append("BAR-SENSE\n");
     * template.append(";// PAGE 0000000008002232\n"); template.append("T 0 2
     * 647 93 \\\\\n"); template.append("T 0 0 515 32 \\\\\n");
     * template.append("T 0 2 642 29 \\\\\n"); template.append("T 0 0 57 95
     * \\\\\n"); template.append(";//{{BEGIN-BT\n"); template.append("T 0 0 40
     * 153 \\\\\n"); template.append("T 0 0 40 173 \\\\\n");
     * template.append(";//{{END-BT\n"); template.append("T 0 0 719 304
     * \\\\\n"); template.append("T 0 0 294 288 \\\\\n"); template.append("T 0 0
     * 84 307 \\\\\n"); template.append("T 0 0 60 286 \\\\\n");
     * template.append("T 0 0 348 308 \\\\\n"); template.append("T 0 0 438 308
     * \\\\\n"); template.append("T 0 0 534 307 \\\\\n"); template.append("B
     * I2OF5 1 2 100 48 373 \\\\\n"); template.append("FORM\n");
     * template.append("PRINT");
     *
     *
     * @param template
     */
    public void setTemplate(StringBuffer template) {
        this.template = template;
    }


    public String getData() {
        return data.toString();
    }

    /**
     * Método responsável por setar os dados referentes a um template.
     *
     * A string de dados deverá ser passada no seguinte formato, por exemplo:
     *
     * StringBuffer data = new StringBuffer();
     *
     * data.append("! UF CONTA.FMT\n"); data.append("0517049-0\n");
     * data.append("01/2008\n"); data.append("10/10/2008\n");
     * data.append("CENTRO PESQ ENERGIA ELETRICA\n"); data.append("AV 1 CIDADE
     * UNIVERSITARIA, 0000 ENTRE 16/18 ILHAS\n"); data.append("ESQ RUA 1C/ RUA
     * 16\n"); data.append("0.2000\n"); data.append("21/11/2007\n");
     * data.append("747656\n"); data.append("21/11/2007\n");
     * data.append("100\n"); data.append("29\n"); data.append("5.8\n");
     * data.append("82660000001176000140033635301081940801020006");
     *
     * @param
     */
    public void setData(StringBuffer data) {
        this.data = data;
    }

    public void setPageConfig(int mmHeight, int qtyLabel) {
        data = new StringBuffer();
        data.append("! 0 200 200 " + (int) convertMmToDots(mmHeight) + " "
                + qtyLabel);
        data.append("\n");

    }


    public void printTextCenter(String font, int size, int x, int y, String data) {
        this.data.append("CENTER \n");
        this.data.append("T ");
        this.textParam(font, size, x, y, data);
        this.data.append("LEFT \n");
    }


    public void setBarSense(int sense0to255) {
        data.append("BAR-SENSE " + sense0to255 + "\n");
    }


    public void appendNewLine(String dataLine) {
        this.data.append(dataLine);
        this.data.append("\n");
    }

    public void append(String data) {
        this.data.append(data);
    }

    public double convertMmToDots(double mm) {
        double conversion = mm * DOTS_DENSITY;
        return conversion;
    }

    public void print() throws Exception {
        Vector  list = null;

        if (isTemplateMode == true) {

            list = stringToList(template.toString(), "\n");

            if (list != null && list.size() > 0) {

                for (int i = 0; i < list.size(); i++) {
                    writeBytes(((String) list.get(i) + "\r\n" ).getBytes());

                }

                 // Sleep 1000
            }
        }


        list = stringToList(data.toString(), "\n");

        for (int i = 0; i < list.size(); i++) {
            writeBytes(((String) list.get(i) + "\r\n" ).getBytes());
        }
    }

    public void printText(String font, int size, int x, int y, String data) {
        this.data.append("T ");
        this.textParam(font, size, x, y, data);
    }

    public void printText(int font, int size, int x, int y, String data) {
        this.data.append("T ");
        this.textParam(font, size, x, y, data);
    }

    public void printText(String text) {
        this.data.append(text);
        this.data.append("\n");

    }


    private void textParam(int font, int size, int x, int y, String data) {
        this.data.append(font + " " + size + " " + x + " " + y + " " + data);
        this.data.append("\n");
    }

    private void textParam(String font, int size, int x, int y, String data) {
        this.data.append(font + " " + size + " " + x + " " + y + " " + data);
        this.data.append("\n");
    }

    private void writeBytes(byte[] bytes) throws Exception {
        this.zebraConnection.getPrinterConnection().write(bytes);
    }
    public void printBarCode(String type, int width, int ratio, int height,
                             int x, int y, String data) {
        this.data.append("B " + type + " " + width + " " + ratio + " " + height
                + " " + x + " " + y + " " + data);
        this.data.append("\n");
    }

    public void printBarCodeV(String type, int width, int ratio, int height,
                              int x, int y, String data) {
        this.data.append("VB " + type + " " + width + " " + ratio + " "
                + height + " " + x + " " + y + " " + data);
        this.data.append("\n");
    }

    public static Vector stringToList(String sourceString, String separatorToken) {
        Vector destinationList = new Vector();

        if (sourceString != null) {
            int index = -1;

            while (true) {
                int oldIndex = index + 1;
                index = sourceString.indexOf(separatorToken, oldIndex);

                if (index != -1) {
                    destinationList.addElement(sourceString.substring(oldIndex,
                            index));
                } else {
                    destinationList.addElement(sourceString.substring(oldIndex,
                            sourceString.length()));
                    break;
                }
            }
        }

        return destinationList;
    }

    public void printLine(int xi, int yi, int xf, int yf, int width) {
        this.data.append("LINE " + xi + " " + yi + " " + xf + " " + yf + " "
                + width);
        this.data.append("\n");
    }

    public void disconnect(){
        this.zebraConnection.disconnect();
    }

    public boolean isPortOpen() {
         return zebraConnection.isPortOpen();
    }

    public void setJournal() {
        data.append("JOURNAL\n");
    }

    public void setLabel() {
        data.append("LABEL\n");
    }

    public void setContrast(int contrast) {
        String string = "CONTRAST " + contrast + "\n";
        data.append(string);
    }

    public void setTone(int tone) {
        String string = "TONE " + tone + "\n";
        data.append(string);
    }

    public void setSpeed(int speed) {
        String string = "SPEED " + speed + "\n";
        data.append(string);
    }

    public void setPageWidth(int mmWidth) {
        String string = "PAGE-WIDTH " + (int) convertMmToDots(mmWidth) + "\n";
        data.append(string);
    }

    public void setBarSense() {
        data.append("BAR-SENSE\n");
    }

    public void setPageFinalize() {
        this.data.append("FORM");
        this.data.append("\n");
        this.data.append("PRINT");
    }


    public void printMultiLineText(int height, String font, int size, int x, int y, String data){
        this.data.append("ML ");
        this.data.append(height + "\n");
        this.data.append("TEXT ");
        this.data.append(font + " " + size + " " + x + " " + y + "\n");
        this.data.append(data + "\n");
        this.data.append("ENDML\n");
    }

    public void printMultiLineText(int height, int font, int size, int x, int y, String data){
        this.printMultiLineText(height, String.valueOf(font), size, x, y, data);
    }

    public void printMultiLineText270(int height, String font, int size, int x, int y, String data){
        this.data.append("ML ");
        this.data.append(height + "\n");
        this.data.append("T270 ");
        this.data.append(font + " " + size + " " + x + " " + y + "\n");
        this.data.append(data + "\n");
        this.data.append("ENDML\n");
    }
}
