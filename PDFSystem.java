package com.tipsycoder.core;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by TipsyCoder on 3/22/15.
 */
public class PDFSystem {
    PDFont fontPlain = PDType1Font.HELVETICA;
    PDFont fontBold = PDType1Font.HELVETICA_BOLD;
    PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
    PDFont fontMono = PDType1Font.COURIER;
    float line = 0;

    private PDPageContentStream getPDStream(PDDocument doc, PDPage page) {
        try {
            return new PDPageContentStream(doc, page);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PDPage getA4NewPage() {
        return new PDPage(PDPage.PAGE_SIZE_A4);
    }

    private void putLetterHead(PDPageContentStream cos, PDDocument doc, PDRectangle rect) {
        int padding = 20;
        // add an image
        try {
            BufferedImage awtImage = ImageIO.read(new File("res/letter_head.png"));
            PDXObjectImage ximage = new PDPixelMap(doc, awtImage);
            float scale = 0.5f; // alter this value to set the image size
            cos.drawXObject(ximage, rect.getWidth()/2 - (ximage.getWidth()*scale/2), rect.getHeight() - ximage.getHeight()*scale - padding, ximage.getWidth()*scale, ximage.getHeight()*scale);
        } catch (FileNotFoundException fnfex) {
            System.out.println("No image for you");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printOfficerRec(Officer info) throws Exception {
        PDDocument document = new PDDocument();
        PDPage page = getA4NewPage();
        PDRectangle rect = page.getMediaBox();
        document.addPage(page);

        PDPageContentStream cos = getPDStream(document, page);

        putLetterHead(cos, document, rect);

        line = rect.getHeight() - 200;
        int bodyLB = 20, bodyIndentR = 250, bodyIndentL = 50, bodyFS = 12, bodyS = 20;

        writeText("Badge Number: " + info.getBadgeNum(), cos, fontPlain, bodyFS, bodyIndentL, getNewLine(bodyLB));

        writeText("Name: " + info.getFirstName() + getSpaces(2) + info.getLastName(), cos, fontPlain, bodyFS, bodyIndentL, getNewLine(bodyLB));
        writeText("Martial Status: " + info.getmStatus(), cos, fontPlain, bodyFS, bodyIndentL, getNewLine(bodyLB));
        writeText("Phone Number: " + info.getpNumber(), cos, fontPlain, bodyFS, bodyIndentR, getOldLine());
        writeText("Gender: Male", cos, fontPlain, bodyFS, bodyIndentL, getNewLine(bodyLB));
        writeText("DOB: " + info.getDob().toString(), cos, fontPlain, bodyFS, bodyIndentR, getOldLine());
        writeText("Street Address: " + info.getAddress(), cos, fontPlain, bodyFS, bodyIndentL, getNewLine(bodyLB));
        writeText("Parish: " + info.getParish(), cos, fontPlain, bodyFS, bodyIndentL, getNewLine(bodyLB));
        writeText("Nationality: " + info.getNationality(), cos, fontPlain, bodyFS, bodyIndentR, getOldLine());

        writeText("Rank: " + info.getRank(), cos, fontPlain, bodyFS, bodyIndentL, getNewLine(bodyLB));
        writeText("Area: " + info.getArea(), cos, fontPlain, bodyFS, bodyIndentR, getOldLine());
        writeText("Division: " + info.getDivision(), cos, fontPlain, bodyFS, bodyIndentL, getNewLine(bodyLB));
        writeText("Station: " + info.getStation(), cos, fontPlain, bodyFS, bodyIndentR, getOldLine());

        /*writeText("", cos, fontPlain, bodyFS, bodyIndentL, getNewLine(bodyLB));
        writeText("", cos, fontPlain, bodyFS, bodyIndent, getLine(bodyLB));
        writeText("", cos, fontPlain, bodyFS, bodyIndent, getLine(bodyLB));
        writeText("", cos, fontPlain, bodyFS, bodyIndentR, getOldLine());*/

        cos.close();

        document.save("lolo.pdf");
        document.close();

    }

    private void printVehicle(Vehicle info) throws Exception {
        PDDocument document = new PDDocument();
        PDPage page = getA4NewPage();
        PDRectangle rect = page.getMediaBox();
        document.addPage(page);

        PDPageContentStream cos = getPDStream(document, page);

        putLetterHead(cos, document, rect);

        line = rect.getHeight() - 200;
        int bodyLB = 20, bodyIndentR = 250, bodyIndentL = 50, bodyFS = 12, bodyS = 20;

        cos.close();

        document.save("lolv.pdf");
        document.close();
    }

    private String getSpaces(int amt) {
        String space = "";

        for (int i = 0; i < amt; i++) {
            space += " ";
        }

        return space;
    }

    private float getNewLine(int adjust) {
        line -= adjust;
        return line;
    }

    private float getOldLine() {
        return line;
    }

    private void writeText(String txt, PDPageContentStream cos, PDFont font, int fontSize, float x, float y) throws IOException {
        cos.beginText();
        cos.setFont(font, fontSize);
        cos.moveTextPositionByAmount(x, y);
        cos.drawString(txt);
        cos.endText();
    }
}
