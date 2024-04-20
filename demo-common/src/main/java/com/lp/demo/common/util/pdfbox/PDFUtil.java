package com.lp.demo.common.util.pdfbox;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lp
 * @date 2024/4/11 15:47
 * @desc
 **/
@Slf4j
public class PDFUtil {

    /**
     * ------------------------------pdf写文本----------------------------------
     */

    /**
     * 写文本到pdf
     *
     * @param path        pdf存储路径
     * @param pages       内容页列表，外层list表示几页，内层list表示每页的内容
     * @param fontFile    字体文件
     * @param fontSize    字体大小
     * @param leading     行间距
     * @param tx          起始坐标，x轴距左边栏宽度
     * @param ty          起始坐标，y轴距顶部栏高度
     * @param pdRectangle 纸张大小
     */
    private static void writeTxt2PDF(String path, List<List<String>> pages, File fontFile, float fontSize, float leading, float tx, float ty, PDRectangle pdRectangle) {
        // 创建PDF文档对象
        try (PDDocument doc = new PDDocument()) {

            // 页面高度
            float pageHeight = pdRectangle.getHeight();
            for (List<String> lines : pages) {
                // 创建一页，并指定大小
                PDPage page = new PDPage(pdRectangle);
                // 获取一页的内容流对象
                try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                    content.beginText();
                    // 设置字体和字号
                    if (fontFile == null) {
                        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), fontSize);
                    } else {
                        // 加载本地字体ttf
                        PDType0Font pdType0Font = PDType0Font.load(doc, fontFile);
                        content.setFont(pdType0Font, fontSize);
                    }

                    //行间距
                    content.setLeading(leading);

                    // 设置字符间距
//                    content.setCharacterSpacing(2);

                    // 设置文本开头（x,y）原点是从左下角开始的
                    content.newLineAtOffset(tx, pageHeight - ty);

                    // 按行写入
                    for (String line : lines) {
                        content.showText(line);
                        // 换行
                        content.newLine();
//                        content.newLineAtOffset(0, -leading);
                    }

                    content.endText();
                    doc.addPage(page);
                } catch (IOException e) {
                    log.error("e: ", e);
                }
            }
            // 保存PDF文档
            doc.save(path);
        } catch (IOException e) {
            log.error("e: ", e);
        }
    }

    private static void writeTxt2PDF(String path, List<List<String>> pages, InputStream fontFileStream, float fontSize, float leading, float tx, float ty, PDRectangle pdRectangle) {
        // 创建PDF文档对象
        try (PDDocument doc = new PDDocument()) {

            // 页面高度
            float pageHeight = pdRectangle.getHeight();
            for (List<String> lines : pages) {
                // 创建一页，并指定大小
                PDPage page = new PDPage(pdRectangle);
                // 获取一页的内容流对象
                try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                    content.beginText();
                    // 设置字体和字号
                    if (fontFileStream == null) {
                        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), fontSize);
                    } else {
                        // 加载本地字体ttf
                        PDType0Font pdType0Font = PDType0Font.load(doc, fontFileStream);
                        content.setFont(pdType0Font, fontSize);
                    }

                    //行间距
                    content.setLeading(leading);
                    // 设置文本开头（x,y）原点是从左下角开始的
                    content.newLineAtOffset(tx, pageHeight - ty);

                    // 按行写入
                    for (String line : lines) {
                        content.showText(line);
                        // 换行
                        content.newLine();
                    }

                    content.endText();
                    doc.addPage(page);
                } catch (IOException e) {
                    log.error("e: ", e);
                }
            }
            // 保存PDF文档
            doc.save(path);
        } catch (IOException e) {
            log.error("e: ", e);
        }
    }


    public static void create(OutputStream outputStream, long count, float tx, float ty) {
        // 创建PDF文档对象
        try (PDDocument doc = new PDDocument()) {

            // 创建第一页
            PDPage page = new PDPage();
            doc.addPage(page);
            // 获取第一页的内容流对象
            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                // 加载本地字体ttf
                ClassPathResource classPathResource = new ClassPathResource("static/fonts/Deng.ttf");
                PDType0Font pdType0Font = PDType0Font.load(doc, classPathResource.getInputStream());
                // 设置字体和字号
                content.setFont(pdType0Font, 12);
//            content.setFont(new PDType1Font(Standard14Fonts.FontName.SYMBOL), 12);
                // 写入文本内容
                content.beginText();
                //行间距
                content.setLeading(20);
                // 设置文本开头
                content.newLineAtOffset(tx, ty);
                for (int i = 0; i < count; i++) {
                    String line = "第" + i + "行内容";
                    content.showText(line);
                    content.newLine();
                }
                String line1 = "a这里是第1行内容...~!@#$%^&*()_+{}|:<>?\"";
                content.showText(line1);

                content.newLine();
                String line2 = "b这里是第2行内容...`1234567890-=[]\\;',./";
                content.showText(line2);

                content.newLine();
                String line3 = "c这里是第3行内容...·1234567890-=【】、；‘，。、";
                content.showText(line3);

                content.newLine();
                String line4 = "d这里是第4行内容...~！@#￥%……&*（）——+{}|：”《》？";
                content.showText(line4);

                content.endText();

                // 关闭内容流对象
//                content.close();
            } catch (IOException e) {
                log.error("e: ", e);
            }
            // 保存PDF文档
            doc.save(outputStream);
            // 关闭PDF文档对象
//            doc.close();
        } catch (IOException e) {
            log.error("e: ", e);
        }
    }


    /**
     * ------------------------------pdf转图片----------------------------------
     */


    /**
     * 使用pdfbox将整个pdf转换成图片
     * 多页pdf会生成多页的图片，后缀会生成图片的位置序号
     *
     * @param fileAddress 文件地址 如:D:/mnt/pdffile
     * @param filename    PDF文件名不带后缀名 testFile
     * @param imgType     图片类型 PNG 和 JPG
     * @param dpi         dpi越大转换后越清晰，相对转换速度越慢
     */
    public static void pdfToPngOne(String fileAddress, String filename, String imgType, Integer dpi) {
        long startTime = System.currentTimeMillis();
        // 将文件地址和文件名拼接成路径
        File file = new File(fileAddress + File.separator + filename + ".pdf");
        String imgFileName = "";
        String imgFilePath = "";

        PDDocument pdDocument = null;
        try {
            // 写入文件
            pdDocument = Loader.loadPDF(file);

            PDFRenderer renderer = new PDFRenderer(pdDocument);
            int pageCount = pdDocument.getNumberOfPages();
            // 遍历把每页pdf转换为图片
            for (int pageNum = 0; pageNum < pageCount; pageNum++) {
                imgFileName = filename + "_" + (pageNum + 1) + "." + imgType;
                imgFilePath = fileAddress + File.separator + imgFileName;
                // BufferedImage image = renderer.renderImage(i, 2.5f);
                // dpi为130，越高越清晰，转换越慢
                BufferedImage imageB = renderer.renderImageWithDPI(pageNum, dpi); // Windows native DPI
                // 将图片写出到该路径下
                ImageIO.write(imageB, imgType, new File(imgFilePath));
            }
            long endTime = System.currentTimeMillis();
            System.out.println("共耗时：" + ((endTime - startTime) / 1000.0) + "秒");  //转化用时
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 需要关闭PDDocument，不关闭会出现想要删除pdf文件时会提示文件正在使用，无法删除的情况
                pdDocument.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自定义转换pdf起始页和终止页
     * 多页pdf会生成多页的图片，后缀会生成图片的位置序号
     *
     * @param fileAddress  文件地址 如:D:/mnt/pdffile
     * @param filename     PDF文件名不带后缀名 testFile
     * @param indexOfStart 开始页  开始转换的页码，从0开始
     * @param indexOfEnd   结束页  停止转换的页码，-1为全部
     * @param imgType      图片类型 PNG 和 JPG
     * @param dpi          dpi越大转换后越清晰，相对转换速度越慢
     */
    public static void pdfTopngTwo(String fileAddress, String filename, int indexOfStart, int indexOfEnd, String imgType, Integer dpi) {
        long startTime = System.currentTimeMillis();
        // 将文件地址和文件名拼接成路径
        File file = new File(fileAddress + File.separator + filename + ".pdf");
        String imgFileName = "";
        String imgFilePath = "";

        PDDocument pdDocument = null;
        try {
            // 3.x加载pdf
            pdDocument = Loader.loadPDF(file);
            // PDDocument.load(file); 2.x加载pdf

            PDFRenderer renderer = new PDFRenderer(pdDocument);
            int pageCount = pdDocument.getNumberOfPages();
            for (int pageNum = indexOfStart; pageNum < indexOfEnd; pageNum++) {
                if (indexOfStart < pageCount) {
                    imgFileName = filename + "_" + (pageNum + 1) + "." + imgType;
                    imgFilePath = fileAddress + File.separator + imgFileName;
                    // BufferedImage image = renderer.renderImage(i, 2.5f);
                    // dpi为130，越高越清晰，转换越慢
                    BufferedImage imageB = renderer.renderImageWithDPI(pageNum, dpi); // Windows native DPI
                    // 将图片写出到该路径下
                    ImageIO.write(imageB, imgType, new File(imgFilePath));
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println("共耗时：" + ((endTime - startTime) / 1000.0) + "秒"); // 转换用时
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用pdfbox将整个pdf转换成图片，生成一张长图
     *
     * @param fileAddress 文件地址 如:D:/mnt/pdffile
     * @param filename    PDF文件名不带后缀名 testFile
     * @param imgType     图片类型 PNG 和 JPG
     * @param dpi         dpi越大转换后越清晰，相对转换速度越慢
     */
    public static void pdfToPngThree(String fileAddress, String filename, String imgType, Integer dpi) {
        long startTime = System.currentTimeMillis();
        // 将文件地址和文件名拼接成路径
        File file = new File(fileAddress + File.separator + filename + ".pdf");
        PDDocument pdDocument = null;
        try {
            // 写入文件
            pdDocument = Loader.loadPDF(file);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            int pageCount = pdDocument.getNumberOfPages();

            BufferedImage combined = null;
            // 遍历把pdf转换为图片
            for (int pageNum = 0; pageNum < pageCount; pageNum++) {
                // BufferedImage image = renderer.renderImage(i, 2.5f);
                // dpi为130，越高越清晰，转换越慢
                BufferedImage imageB = renderer.renderImageWithDPI(pageNum, dpi); // Windows native DPI
                if (pageNum == 0) {
                    combined = imageB;
                } else {
                    combined = mergeImage(combined, imageB);
                }
            }
            // 输出生成图片
            ImageIO.write(combined, imgType, new File(fileAddress + File.separator + filename + "." + imgType));
            long endTime = System.currentTimeMillis();
            System.out.println("共耗时：" + ((endTime - startTime) / 1000.0) + "秒");  //转化用时
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 需要关闭PDDocument，不关闭会出现想要删除pdf文件时会提示文件正在使用，无法删除的情况
                pdDocument.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 合并两张图片流
     *
     * @param imageOne 图片1
     * @param imageTwo 图片2
     * @return
     */
    private static BufferedImage mergeImage(BufferedImage imageOne, BufferedImage imageTwo) {
        BufferedImage combined = new BufferedImage(
                imageOne.getWidth(),
                imageOne.getHeight() + imageTwo.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics g = combined.getGraphics();
        g.drawImage(imageOne, 0, 0, null);
        g.drawImage(imageTwo, 0, imageOne.getHeight(), null);
        g.dispose();
        return combined;
    }

    public static void main(String[] args) throws IOException {
        /**
         * 已验证OK
         */
        // 多页pdf生成多页的图片，多页pdf会生成多页的图片，后缀会生成图片的位置序号
        pdfToPngOne("D:\\Doc", "response7", "PNG", 130);
        // 多页pdf会生成多页的图片，自定义转换pdf起始页和终止页
        pdfTopngTwo("D:\\Doc", "response7", 2, 5, "PNG", 130);
        // 使用pdfbox将整个pdf转换成图片，生成一张长图
        pdfToPngThree("D:\\Doc", "response7", "PNG", 130);

        /**
         * 占一个位置：1大写字母；1中文；1中文符号；2小写字母；2数字；2英文符号
         */
        float fontSize = 12;
        float leading = 20;
        float tx = 25;
        float ty = 35;
        PDRectangle pdRectangle = PDRectangle.A4;
        float pageWidth = pdRectangle.getWidth();
        float pageHeight = pdRectangle.getHeight();
        List<List<String>> pages = new ArrayList<>();
        int i = 1;
        List<String> lines = new ArrayList<>();
        String endTxt = "";
        for (int j = 1; j < 50; j++) {
            if (j % 2 == 0) {
                endTxt = "无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无无";
            }
            if (j % 2 == 1) {
                endTxt = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
            }
            if (j % 3 == 0) {
                endTxt = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ";
            }
            if (j % 7 == 3) {
                endTxt = "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";
            }
            String line = "第" + i + "页，第" + j + "行内容" + endTxt;
            // 判断行文本中是否包含换行键；对行文本按长度分割
            List<String> unitLines = new ArrayList<>();
            if (line.contains("\n")) {
                String[] splitLines = line.split("\n");
                for (String splitLine : splitLines) {
                    unitLines.addAll(splitByLength(splitLine, (int) (pageWidth - tx)));
//                    unitLines.addAll(splitByLength(splitLine, (int) (pageWidth - tx), fontSize));
                }
            } else {
                unitLines = splitByLength(line, (int) (pageWidth - tx));
//                unitLines = splitByLength(line, (int) (pageWidth - tx), fontSize);
            }
            if (unitLines.size() > 0) {
                lines.addAll(unitLines);
            }

            if (lines.size() / 40 >= i) {
                i++;
            }

        }
        // 按40行每页分割页数
        pages.addAll(Lists.partition(lines, 40));
        writeTxt2PDF("D:\\Doc\\writeTxt2PDF.pdf", pages, new File("C:\\Windows\\Fonts\\Deng.ttf"), fontSize, leading, tx, ty, PDRectangle.A4);
        // 按上边配置，建议一页40行内容， TODO 每行文本长度限制 自动换行


        /**
         * simple
         */
        create(null, 50, 25, 250);
    }

    private static final float POINTS_PER_INCH = 72.0F;
    private static final float POINTS_PER_MM = 2.8346457F;

    /**
     * 递归分割按长度字符串
     *
     * @param str
     * @param length
     * @return
     */
    public static List<String> splitByLength(String str, int length) {
        List<String> result = new ArrayList<>();
        if (str.length() <= length) {
            result.add(str);
        } else {
            result.add(str.substring(0, length));
            result.addAll(splitByLength(str.substring(length), length));
        }
        return result;
    }
//    public static List<String> splitByLength(String str, int length, float fontSize) {
//        // 字符串长度
//        int countChinese = countChinese(str);
//        int countUpperCase = countUpperCase(str);
//        int countLowerCase = countLowerCase(str);
//        int countDigits = countDigits(str);
//        int countChinesePunctuations = countChinesePunctuations(str);
//        int countEnglishPunctuations = countEnglishPunctuations(str);
//
//
//        double strLength = ((countChinese) * 2 + countUpperCase * 1.7 + (str.length() - countChinese - countUpperCase)) * fontSize;
//
//        List<String> result = new ArrayList<>();
//        if (strLength <= length) {
//            result.add(str);
//        } else {
//            result.add(str.substring(0, (int) (length / fontSize)));
//            result.addAll(splitByLength(str.substring((int) (length / fontSize)), length, fontSize));
//        }
//        return result;
//    }

//    /**
//     * 指定页插入一段文字
//     * @param inputFilePath
//     * @param outputFilePath
//     * @param pageNum
//     * @param message
//     * @throws Exception
//     */
//    public static void InsertPageContent (String inputFilePath, String outputFilePath, Integer pageNum, String message) throws Exception {
//        File inputPDFFile = new File(inputFilePath);
//        File outputPDFFile = new File(outputFilePath);
//        // the document
//        PDDocument doc = null;
//        try{
//            doc = PDDocument.load(inputPDFFile);
//            PDPageTree allPages = doc.getDocumentCatalog().getPages();
//            PDFont font = PDType1Font.HELVETICA_BOLD;
//            //字体大小
//            float fontSize = 36.0f;
//            PDPage page = (PDPage)allPages.get(pageNum - 1);
//            PDRectangle pageSize = page.getMediaBox();
//            float stringWidth = font.getStringWidth(message)*fontSize/1000f;
//            // calculate to center of the page
//            int rotation = page.getRotation();
//            boolean rotate = rotation == 90 || rotation == 270;
//            float pageWidth = rotate ? pageSize.getHeight() : pageSize.getWidth();
//            float pageHeight = rotate ? pageSize.getWidth() : pageSize.getHeight();
//            double centeredXPosition = rotate ? pageHeight/2f : (pageWidth - stringWidth)/2f;
//            double centeredYPosition = rotate ? (pageWidth - stringWidth)/2f : pageHeight/2f;
//            // append the content to the existing stream
//            PDPageContentStream contentStream = new PDPageContentStream(doc, page, true, true,true);
//            contentStream.beginText();
//            // set font and font size
//            contentStream.setFont( font, fontSize );
//            // set text color to red
//            contentStream.setNonStrokingColor(255, 0, 0);
//            if (rotate) {
//                // rotate the text according to the page rotation
//                contentStream.setTextRotation(Math.PI/2, centeredXPosition, centeredYPosition);
//            } else {
//                contentStream.setTextTranslation(centeredXPosition, centeredYPosition);
//            }
//            contentStream.drawString(message);
//            contentStream.endText();
//            contentStream.close();
//            doc.save(outputPDFFile);
//            System.out.println("成功向pdf插入文字");
//        } finally {
//            if( doc != null ) {
//                doc.close();
//            }
//        }
//    }

    public static int countChinese(String input) {
        return input.replaceAll("[^\\u4E00-\\u9FA5]", "").length();
    }

    public static int countEnglish(String input) {
        return input.replaceAll("[^A-Za-z]", "").length();
    }

    public static int countDigits(String input) {
        return input.replaceAll("[^0-9]", "").length();
    }

    public static int countUpperCase(String input) {
        return input.replaceAll("[^A-Z]", "").length();
    }

    public static int countLowerCase(String input) {
        return input.replaceAll("[^a-z]", "").length();
    }

    private static int countChinesePunctuations(String text) {
        String regex = "[\u3002\uFF1F\uFF01\uFF0C\u3001\uFF0E\uFFE5\uFF08\uFF09\uFF0D\u300A\u300B]";
        return text.replaceAll("[^" + regex + "]", "").length();
    }

    private static int countEnglishPunctuations(String text) {
        String regex = "[\\.,;!?]";
        return text.replaceAll("[^" + regex + "]", "").length();
    }

}
