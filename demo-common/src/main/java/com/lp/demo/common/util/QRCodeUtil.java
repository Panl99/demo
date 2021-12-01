package com.lp.demo.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;

/**
 * @author lp
 * @date 2021/11/25 14:48
 **/
public class QRCodeUtil {
    // 二维码存储路径
    private static final String CURRENT_PATH = System.getProperty("user.dir"); //new File("").getAbsolutePath()
    private static final String QR_CODE_PATH = CURRENT_PATH;
    // logo图片路径
    private static final String LOGO_PATH = "D:\\Others\\tx.jpg";
    // 编码格式
    private static final String CHARSET = "UTF-8";
    // 生成二维码图片格式
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QR_CODE_SIZE = 300;
    // LOGO宽度
    private static final int LOGO_WIDTH = 60;
    // LOGO高度
    private static final int LOGO_HEIGHT = 60;
    // 二维码颜色
    private static final int ON_COLOR = 0xFF000000;
    private static final int OFF_COLOR = 0xFFFFFFFF;
/**
    中文名	英文名	ARGB	示例
    黑色	black	0xFF000000	▇▇▇
    深灰	darkGray	0xFF555555	▇▇▇
    浅灰	lightGray	0xFFAAAAAA	▇▇▇
    白色	white	0xFFFFFFFF	▇▇▇
    灰色	gray	0xFF7F7F7F	▇▇▇
    红色	red	0xFFFF0000	▇▇▇
    绿色	green	0xFF00FF00	▇▇▇
    蓝色	blue	0xFF0000FF	▇▇▇
    青色	cyan	0xFF00FFFF	▇▇▇
    黄色	yellow	0xFFFFFF00	▇▇▇
    洋红	magenta	0xFFFF00FF	▇▇▇
    橙色	orange	0xFFFF7F00	▇▇▇
    紫色	purple	0xFF7F007F	▇▇▇
    棕色	brown	0xFF996633	▇▇▇
    透明	clear	0x00000000
*/

    // ------------ type 2 ---------------

    /**
     * 创建二维码
     *
     * @param content 二维码内容
     * @param imgPath LOGO路径
     * @param needCompress 是否压缩LOGO
     * @return
     * @throws Exception
     */
    private static BufferedImage createImage(String content,
                                             String imgPath,
                                             boolean needCompress) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? ON_COLOR : OFF_COLOR);
            }
        }
        if (imgPath == null || "".equals(imgPath)) {
            return image;
        }
        // 插入图片
        insertImage(image, imgPath, needCompress);
        return image;
    }

    /**
     * 插入LOGO
     *
     * @param source       二维码图片
     * @param imgPath      LOGO图片地址
     * @param needCompress 是否压缩
     * @throws Exception
     */
    private static void insertImage(BufferedImage source,
                                    String imgPath,
                                    boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println(imgPath + "   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > LOGO_WIDTH) {
                width = LOGO_WIDTH;
            }
            if (height > LOGO_HEIGHT) {
                height = LOGO_HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QR_CODE_SIZE - width) / 2;
        int y = (QR_CODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 生成二维码
     *
     * @param content      内容
     * @param imgPath      LOGO地址
     * @param destPath     存放目录
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static String encode(String content,
                                String imgPath,
                                String destPath,
                                boolean needCompress) throws Exception {
        BufferedImage image = createImage(content, imgPath, needCompress);
        mkdirs(destPath);
        String file = new Random().nextInt(99999999) + "." + FORMAT_NAME.toLowerCase(Locale.ROOT);
        ImageIO.write(image, FORMAT_NAME, new File(destPath + "/" + file));
        return file;
    }

    /**
     * 生成二维码
     *
     * @param content      内容
     * @param imgPath      LOGO地址
     * @param output       输出流
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content,
                              String imgPath,
                              OutputStream output,
                              boolean needCompress) throws Exception {
        BufferedImage image = createImage(content, imgPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * 生成二维码(内嵌LOGO)
     * 压缩logo：logo过大会覆盖掉二维码
     *
     * @param content  内容
     * @param imgPath  LOGO地址
     * @param destPath 存储地址
     * @throws Exception
     */
    public static String encode(String content,
                              String imgPath,
                              String destPath) throws Exception {
        return encode(content, imgPath, destPath, true);
    }

    /**
     * 生成二维码(不带LOGO)
     *
     * @param content      内容
     * @param destPath     存储地址
     * @throws Exception
     */
    public static String encode(String content,
                                String destPath) throws Exception {
        return encode(content, null, destPath, false);
    }

    /**
     * 生成二维码(不带LOGO)
     *
     * @param content 内容
     * @param output  输出流
     * @throws Exception
     */
    public static void encode(String content,
                              OutputStream output) throws Exception {
        encode(content, null, output, false);
    }

    /**
     * 创建目录
     * 当文件夹不存在时，mkdirs会自动创建多层目录
     * 区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
     *
     * @param destPath 存放目录
     */
    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        //当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    // ---------------------------------解析二维码--------------------------------------------

    /**
     * 解析二维码
     *
     * @param path 二维码图片地址
     * @return
     * @throws Exception
     */
    public static String decode(String path) throws Exception {
        return decode(new File(path));
    }

    /**
     * 解析二维码
     *
     * @param file 二维码图片
     * @return
     * @throws Exception
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    // ---------------------------------解析二维码 END--------------------------------------------

    // ------------ type 1 :simple ---------------
    private static void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        filePath +=  "\\" + System.currentTimeMillis() + "-MyQRCode.png";
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

    }

    public static void main(String[] args) throws Exception {
        // type 1
//        try {
//            generateQRCodeImage("This is my first QR Code", QRCODE_SIZE, QRCODE_SIZE, QR_CODE_PATH);
//        } catch (WriterException e) {
//            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
//        } catch (IOException e) {
//            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
//        }

        // type 2
        String text = "http://www.baidu.com";  //这里设置自定义网站url
        String ironMan = "I am iron man";

        String qrCodeLogo = encode(ironMan, LOGO_PATH, QR_CODE_PATH);
        String qrCodeNoLogo = encode(ironMan, QR_CODE_PATH);
        System.out.println("有LOGO：" + QR_CODE_PATH + File.separator + qrCodeLogo
                + "\n无LOGO：" + QR_CODE_PATH + File.separator + qrCodeNoLogo);

        String decodeQRCode = decode(QR_CODE_PATH + File.separator + qrCodeLogo);
        System.out.println("decodeQRCode = " + decodeQRCode);
    }
}
