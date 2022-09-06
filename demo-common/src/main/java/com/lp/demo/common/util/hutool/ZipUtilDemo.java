package com.lp.demo.common.util.hutool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import com.lp.demo.common.exception.DisplayableException;
import com.lp.demo.common.service.ThreadLocalService;
import com.lp.demo.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author lp
 * @date 2022/9/3 16:31
 * @desc 解压、读取压缩包下内容
 **/
public class ZipUtilDemo {

    /**
     * 背景：
     * 上传智能手表表盘，手动一个一个上传太慢，按压缩包格式批量解析上传
     * 表盘信息：
     *      文件1：xxx.bin表盘文件
     *      文件2：.png/.jpg等格式的1张表盘图片
     *      文件3：xxx.txt表盘信息描述文件，内容包含：
     *          名称：xxx
     *          风格：xxx
     *          标签：aa;bb;cc
     *          作者：xxx
     *          描述：xxx
     *          价格：xxx
     * <p>
     * 压缩包格式：xxx.zip
     * 压缩文件编码：utf-8
     */
    public static void main(String[] args) {
        // 校验压缩包、压缩包下文件格式

        // 校验压缩包下文件内容格式

        // 解压文件

        // 读取文件内容 并处理

        // 删除解压文件

        String path = "D:\\Code\\jingxun\\test333.zip";
        String pathCn = "D:\\Life\\work\\jingxun\\智能手表\\表盘文件-test.zip";
        File file = new File(path);
//        unzip(file); Charset.defaultCharset();
        unzip(pathCn);
    }

    public static void preCheck(File file) {
        File[] files = file.listFiles();
        if (files == null) {
            System.out.println("files is null");
            throw new DisplayableException(2001, "files is null!");
        }
        System.out.println(">>>>>> " + file.getName());

        if (files.length < 1) {
            throw new DisplayableException(2001, "files length < 1!");
        }
    }

    private static final String[] imgFileType = {"png", "jpg", "jpeg", "bmp"};
    public static void read(File file, List<Map<String, String>> contentList) {
        File[] files = file.listFiles();
        if (files == null) {
            System.out.println("files is null");
            return;
        }

        System.out.println(">>>>>> " + file.getName());

        Map<String, String> item = new HashMap<>();

        Iterator<File> iterator = Arrays.asList(files).iterator();
        while (iterator.hasNext()) {
            File fileN = iterator.next();
            if (fileN.isFile()) {
                if (FileUtil.isEmpty(fileN)) {
                    continue;
                }

                String fileName = fileN.getName();
//                String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
                // 读取文件
                if (fileName.endsWith(".bin")) {
                    byte[] binBytes = IoUtil.readBytes(FileUtil.getInputStream(fileN));
                    item.put("binBytes", String.valueOf(binBytes.length));
                }

//                if (!ArrayUtils.contains(imgFileType, fileType.toLowerCase())) {
//                    System.out.println("Image format error! only supports [png,jpg,jpeg,bmp] format image, image = "+ fileName);
//                    return;
//                }
                if (fileName.endsWith(".png")) {
                    byte[] imgBytes = IoUtil.readBytes(FileUtil.getInputStream(fileN));
                    item.put("imgBytes", String.valueOf(imgBytes.length));
                }
                if (fileName.endsWith(".txt")) {
                    if (FileUtil.isEmpty(fileN)) {
                        System.err.println("txt is empty!");
                        return;
                    }

                    // 读取文件信息
                    List<String> lines = FileUtil.readLines(fileN, CharsetUtil.defaultCharset());
                    for (String line : lines) {
                        String[] content = line.trim().split("：");
                        switch (content[0]) {
                            case "名称":
                                item.put("name", content[1]);
                                break;
                            case "风格":
                                item.put("style", content[1]);
                                break;
                            case "标签":
                                item.put("tag", content[1]);
                                break;
                            case "作者":
                                item.put("author", content[1]);
                                break;
                            case "描述":
                                item.put("desc", content[1]);
                                break;
                            case "价格":
                                item.put("price", content[1]);
                                break;
                            default:
                                break;
                        }

                    }
                }

            } else if (fileN.isDirectory()) {
                if (FileUtil.isDirEmpty(fileN)) {
                    continue;
                }
                read(fileN, contentList);
            }
        }

        if (!item.isEmpty()) {
            contentList.add(item);
        }
    }

    public static List read(File file) {
        List<Map<String, String>> content = new ArrayList<>();
        if (file.isDirectory()) {
            read(file, content);
        } else {
            System.out.println("unzip file is not dir!");
        }

        System.out.println("contentList = " + content);
        return content;
    }

    public static List unzip(String path) {
        File unzipFile = ZipUtil.unzip(path, CharsetUtil.systemCharset());
        String absolutePath = unzipFile.getAbsolutePath();
        System.out.println("absolutePath = " + absolutePath);

        return read(unzipFile);
    }

    public static List unzip(InputStream in) throws IOException {
        String fileName = (String) ThreadLocalService.getInstance().getAndRemove();
        if (StringUtil.isBlank(fileName)) {
            fileName = "dialUploadTemp";
        }
        File destDir = FileUtil.mkdir(System.getProperty("java.io.tmpdir") + File.separator + fileName);
        File file = ZipUtil.unzip(in, destDir, CharsetUtil.systemCharset());
        List content = read(file);
        FileUtil.del(destDir);
        return content;
    }

    public static void unzip(byte[] bytes) throws IOException {
        unzip(new ByteArrayInputStream(bytes));
    }

    public static void unzip(MultipartFile file) throws IOException {
        unzip(file.getInputStream());
    }

/*



    @PostMapping(value = "/dial/upload")
    public JsonResult<List<String>> upload(MultipartFile file) {
        log.info("Start batch upload dial!");
        if (file == null || file.isEmpty()) {
            log.error("Upload dial zip file is empty!");
            throw new DisplayableException("PARAMS_ERROR");
        }
        String fileName = file.getOriginalFilename();
        if (StringUtil.isEmpty(fileName) ) {
            log.error("File name is empty!");
            throw new DisplayableException("PARAMS_ERROR");
        }
        if (!fileName.endsWith(".zip")) {
            log.error("Unsupported file type! file = {}", fileName);
            throw new DisplayableException("PARAMS_ERROR");
        }
        List<String> uploadFailedDials;
        try {
            uploadFailedDials = ZipUtilDemo.upload(file.getBytes(), fileName);
        } catch (IOException e) {
            log.error("Dial file get bytes error! e : ", e);
            throw new DisplayableException("SERVER_ERROR");
        }
        log.info("End batch upload dial!");
        return new JsonResult<>(uploadFailedDials);
    }


    @Transactional(rollbackFor = Exception.class)
    public List<String> upload(InputStream is, String fileName) {
        if (StringUtil.isEmpty(fileName)) {
            String now = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            fileName = "DialUploadTempDir_" + now;
        }
        File destDir = FileUtil.mkdir(System.getProperty("java.io.tmpdir") + File.separator + fileName);
        File file = ZipUtil.unzip(is, destDir, CharsetUtil.systemCharset());
        List<String> uploadFailedDials = save(file);
        FileUtil.del(destDir);
        return uploadFailedDials;
    }

    @Override
    public List<String> upload(byte[] bytes, String fileName) {
        return upload(new ByteArrayInputStream(bytes), fileName);
    }

    private List<String> save(File file) {
        if (!file.isDirectory()) {
            log.error("Unzip file is not dir!");
            throw new DisplayableException(ErrCode.PARAMS_ERROR);
        }
        List<Dial> dials = new ArrayList<>();
        read(file, dials);

        List<String> uploadFailedDials = new ArrayList<>();
        Date date = new Date();
        String now = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
        for (Dial dial : dials) {
            // 上传图片
            String oss = "https://ossscn.jingxun.xyz/";
            if (dial.getImgBytes() != null) {
                String imageBasePath = "watch_band/dial/image/";
                String imageType = "png";
                String ossImageName = now + "_" + dial.getName() + "." + imageType;
                dial.setImage(oss + imageBasePath + ossImageName);
                try {
                    UploadUtil.upload2OSS(dial.getImgBytes(), imageType, imageBasePath, ossImageName);
                } catch (DisplayableException e) {
                    uploadFailedDials.add(dial.getStyle()+"--"+dial.getName() + ", reason: " +e.getMessage());
                    continue;
                }
            }

            // 上传文件
            if (dial.getBinBytes() != null) {
                String fileBasePath = "watch_band/dial/file/";
                String ossFileName = now + "_" + dial.getName() + ".bin";
                UploadUtil.upload2OSS(dial.getBinBytes(), "bin", fileBasePath, ossFileName);
                dial.setFile(oss + fileBasePath + ossFileName);
            } else {
                dial.setSize(0);
            }

            if (dial.getPrice() == null) {
                dial.setPrice(BigDecimal.ZERO);
            }
            if (!StringUtils.isEmpty(dial.getRegion())) {
                dial.setRegion(dial.getRegion());
            } else {
                dial.setRegion(Locale.SIMPLIFIED_CHINESE.getLanguage() + "_" + Locale.SIMPLIFIED_CHINESE.getCountry());
            }
            dial.setUploaderId(1L);
            dial.setUploader("ABCD");
            dial.setDownloads(0);
            dial.setGmtCreate(date);
            dial.setGmtModified(date);

            if (dialMapper.insert(dial) < 1) {
                log.error("add dial error! dial : {}", dial);
                throw new DisplayableException(ErrCode.SERVER_ERROR);
            }

            // TODO get pid list
            // 添加表盘-产品关系
            ProductDialRelation productDialRelation = new ProductDialRelation();
            productDialRelation.setDialId(dial.getId());
            productDialRelation.setPid(391975958L);
            productDialRelation.setGmtCreate(date);
            int count = dialProductRelationMapper.insert(productDialRelation);
            if (count < 1) {
                log.error("add dial product relation error! productDialRelation = {}", productDialRelation);
                throw new DisplayableException(ErrCode.SERVER_ERROR);
            }

            productDialRelation.setPid(3385460019L);
            if (dialProductRelationMapper.insert(productDialRelation) < 1) {
                log.error("add2 dial product relation error! productDialRelation = {}", productDialRelation);
                throw new DisplayableException(ErrCode.SERVER_ERROR);
            }
        }
        log.warn("Upload failed dials : {}", uploadFailedDials);
        return uploadFailedDials;
    }

    private List<Dial> read(File file, List<Dial> dials) {
        try {
            File[] files = file.listFiles();
            if (files == null) {
                log.error("Files is null");
                throw new DisplayableException(ErrCode.PARAMS_ERROR);
            }
//            log.info(">>>>>> " + file.getName());

            Dial dial = new Dial();
            Iterator<File> iterator = Arrays.asList(files).iterator();
            while (iterator.hasNext()) {
                File subFile = iterator.next();
                if (subFile.isFile()) {
                    if (FileUtil.isEmpty(subFile)) {
                        continue;
                    }

                    String fileName = subFile.getName();
//                String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
                    // 读取文件
                    if (fileName.endsWith(".bin")) {
                        byte[] binBytes = IoUtil.readBytes(FileUtil.getInputStream(subFile));
                        dial.setBinBytes(binBytes);
                        dial.setSize((int) Math.ceil(subFile.length() / 1024.00));
                    }

//                if (!ArrayUtils.contains(imgFileType, fileType.toLowerCase())) {
//                    System.out.println("Image format error! only supports [png,jpg,jpeg,bmp] format image, image = "+ fileName);
//                    return;
//                }
                    if (fileName.endsWith(".png")) {
                        byte[] imgBytes = IoUtil.readBytes(FileUtil.getInputStream(subFile));
                        dial.setImgBytes(imgBytes);

                        // 获取图片宽高
                        BufferedImage bufferedImage = ImageIO.read(subFile);
                        if (bufferedImage == null) {
                            log.error("image format error! img = {}", subFile.getName());
                            throw new DisplayableException(ErrCode.PARAMS_ERROR);
                        }
                        dial.setImageHeight(bufferedImage.getHeight());
                        dial.setImageWidth(bufferedImage.getWidth());
                    }

                    if (fileName.endsWith(".txt")) {
                        if (FileUtil.isEmpty(subFile)) {
                            log.error("txt is empty!");
                            throw new DisplayableException(ErrCode.PARAMS_ERROR);
                        }

                        // 读取文件信息
                        List<String> lines = FileUtil.readLines(subFile, CharsetUtil.defaultCharset());
                        for (String line : lines) {
                            String[] content = line.trim().split("：");
                            switch (content[0]) {
                                case "名称":
                                    dial.setName(content[1].trim());
                                    break;
                                case "风格":
                                    dial.setStyle(content[1].trim());
                                    break;
                                case "标签":
                                    dial.setTag(content[1].replace("；", ";").trim());
                                    break;
                                case "作者":
                                    dial.setAuthor(content[1].trim());
                                    break;
                                case "描述":
                                    dial.setDescribe(content[1].trim());
                                    break;
                                case "价格":
                                    dial.setPrice(BigDecimal.valueOf(Double.parseDouble(content[1].trim())));
                                    break;
                                default:
                                    break;
                            }

                        }
                    }

                } else if (subFile.isDirectory()) {
                    if (FileUtil.isDirEmpty(subFile)) {
                        continue;
                    }
                    read(subFile, dials);
                }
            }

            if (StringUtil.isNotEmpty(dial.getName())) {
                dials.add(dial);
            }
        } catch (IOException e) {
            log.error("Read file error! e: ", e);
        }
        return dials;
    }

*/

}
