package com.batchcreate.nft.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.imageio.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ImageUtil {
    public static String SAVE_PATH = "D:\\NFT-FINISH\\version-1.0\\多数基础款-图\\pic\\";
    public static String META_DATA_PATH = "D:\\NFT-FINISH\\version-1.0\\多数基础款-图\\metadata\\";

    public static final String IMAGE_SUFFIX = ".png";
    public static final String METADATA_SUFFIX = ".token.json";

    public static AsyncTaskExecutor executor = getAsyncExecutor();

    public static AsyncTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();
        // 设置核心线程数
        executor.setCorePoolSize(10);
        // 设置最大线程数
        executor.setMaxPoolSize(10);
        // 设置队列容量
        executor.setQueueCapacity(100);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(300);
        // 设置默认线程名称
        executor.setThreadNamePrefix("drawn-thread-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    public static BufferedImage ImageAddWord(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * 读取某个目录下所有文件、文件夹
     *
     * @param path
     * @return LinkedHashMap<String, String>
     */
    public static LinkedHashMap<String, String> getFiles(String path) {
        LinkedHashMap<String, String> files = new LinkedHashMap<String, String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isDirectory()) {
                files.put(tempList[i].getName(), tempList[i].getPath());
            }
        }
        return files;
    }


    public static void batchDrawn() {
        // load different layer materials
        File[] backgrounds = new File("D:\\project\\nft\\src\\main\\resources\\background").listFiles();
        File[] glasses = new File("D:\\project\\nft\\src\\main\\resources\\eye").listFiles();
        File[] peoples = new File("D:\\project\\nft\\src\\main\\resources\\body").listFiles();
        File[] heads = new File("D:\\project\\nft\\src\\main\\resources\\head").listFiles();
        File[] cloths = new File("D:\\project\\nft\\src\\main\\resources\\cloth").listFiles();
        File[] hands = new File("D:\\project\\nft\\src\\main\\resources\\hand").listFiles();
        File[] mouths = new File("D:\\project\\nft\\src\\main\\resources\\mouth").listFiles();
        File[] tips = new File("D:\\project\\nft\\src\\main\\resources\\body-tag").listFiles();

        // init random,executor,anti-duplication list
        AsyncTaskExecutor executor = getAsyncExecutor();
        Random random = new Random();
        List<String> list = Collections.synchronizedList(new ArrayList<>());
        PooledImageWriter imageWriter = new PooledImageWriter("png", 50);
        // init nft token number
        AtomicInteger integer = new AtomicInteger(0);
        // i means nft quantity
        for (int i = 0; i < 150000; i++) {
            log.info("循环第{}次", i);
            // concurrency create ,you can specify the layer order according to your needs
            executor.execute(() -> {
                        try {
                            BufferedImage buffImage = ImageAddWord(1000, 1000);
                            List<Map<String, String>> metaData = new ArrayList<>();

                            Graphics2D g = buffImage.createGraphics();
                            int a = random.nextInt(backgrounds.length);
                            int b = random.nextInt(peoples.length);
                            int c = random.nextInt(cloths.length);
                            int d = random.nextInt(mouths.length);
                            int e = random.nextInt(tips.length);
                            int f = random.nextInt(glasses.length);
                            int gg = random.nextInt(heads.length);
                            int ff = random.nextInt(hands.length);

                            String s = a + b + c + d  + f + gg + String.valueOf(ff);// + e
                            // anti-duplication
                            if (list.contains(s)) {
                                return;
                            }
                            list.add(s);

                            File back = backgrounds[a];
                            g.drawImage(ImageIO.read(back).getScaledInstance(1000, 1000, Image.SCALE_SMOOTH), 0, 0, null);
                            Map<String, String> metaMap = new HashMap<>(2);
                            metaMap.put("trait_type", "background");
                            metaMap.put("value", back.getName().substring(0, back.getName().lastIndexOf(".")));
                            metaData.add(metaMap);

                            File people = peoples[b];
                            g.drawImage(ImageIO.read(people).getScaledInstance(1000, 1000, Image.SCALE_SMOOTH), 0, 0, null);
                            Map<String, String> metaMap1 = new HashMap<>(2);
                            metaMap1.put("trait_type", "body");
                            metaMap1.put("value", people.getName().substring(0, people.getName().lastIndexOf(".")));
                            metaData.add(metaMap1);

                            File mouth = mouths[d];
                            g.drawImage(ImageIO.read(mouth).getScaledInstance(1000, 1000, Image.SCALE_SMOOTH), 0, 0, null);
                            Map<String, String> metaMap3 = new HashMap<>(2);
                            metaMap3.put("trait_type", "mouth");
                            metaMap3.put("value", mouth.getName().substring(0, mouth.getName().lastIndexOf(".")));
                            metaData.add(metaMap3);

                            //                            specify the probability of occurrence of a layer
                            if (random.nextInt(100) >= 10) {
                                File bodyTip2 = tips[e];
                                g.drawImage(ImageIO.read(bodyTip2).getScaledInstance(1000, 1000, Image.SCALE_SMOOTH), 0, 0, null);
                                Map<String, String> metaMap4 = new HashMap<>(2);
                                metaMap4.put("trait_type", "body-tag");
                                metaMap4.put("value", bodyTip2.getName().substring(0, bodyTip2.getName().lastIndexOf(".")));
                                metaData.add(metaMap4);
                            }

                            File glass = glasses[f];
                            g.drawImage(ImageIO.read(glass).getScaledInstance(1000, 1000, Image.SCALE_SMOOTH), 0, 0, null);
                            Map<String, String> metaMap5 = new HashMap<>(2);
                            metaMap5.put("trait_type", "eye");
                            metaMap5.put("value", glass.getName().substring(0, glass.getName().lastIndexOf(".")));
                            metaData.add(metaMap5);

                            File cloth = cloths[c];
                            g.drawImage(ImageIO.read(cloth).getScaledInstance(1000, 1000, Image.SCALE_SMOOTH), 0, 0, null);
                            Map<String, String> metaMap2 = new HashMap<>(2);
                            metaMap2.put("trait_type", "cloth");
                            metaMap2.put("value", cloth.getName().substring(0, cloth.getName().lastIndexOf(".")));
                            metaData.add(metaMap2);

                            File head = heads[gg];
                            g.drawImage(ImageIO.read(head).getScaledInstance(1000, 1000, Image.SCALE_SMOOTH), 0, 0, null);
                            Map<String, String> metaMap6 = new HashMap<>(2);
                            metaMap6.put("trait_type", "head");
                            metaMap6.put("value", head.getName().substring(0, head.getName().lastIndexOf(".")));
                            metaData.add(metaMap6);

                            File hand = hands[ff];
                            g.drawImage(ImageIO.read(hand).getScaledInstance(1000, 1000, Image.SCALE_SMOOTH), 0, 0, null);
                            Map<String, String> metaMap7 = new HashMap<>(2);
                            metaMap7.put("trait_type", "hand");
                            metaMap7.put("value", hand.getName().substring(0, hand.getName().lastIndexOf(".")));
                            metaData.add(metaMap7);


                            int i1 = integer.addAndGet(1);
                            String filepath = SAVE_PATH + i1 + ".png";
                            String metaPath = META_DATA_PATH + i1 + ".token.json";
                            String metaJson = String.format("{\"name\":\"Token %s\",\"description\":\"An art token.\",\"image\":\"/%s.png\",\"attributes\":%s}",
                                    i1, i1, JsonUtils.object2Json(metaData));
                            // generate images and metadata
                            imageWriter.write(buffImage, new File(filepath), metaPath, metaJson);
                            buffImage.flush();
                            log.info("生成图片{}张", i1);
                            if(i1 >= 8500) {
                                System.exit(0);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
    }



    public static void resetSize(String absolutePath) {
        File[] images = new File(absolutePath).listFiles();
        Arrays.stream(Objects.requireNonNull(images)).forEach(image -> {
            try {
                BufferedImage buffImage = ImageIO.read(image);
                ImageIO.write(resizeImageWithHint(buffImage),"png",new File(image.getAbsolutePath()));
                log.info("resetSize path :{}",image.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * 重置图片大小为1000x1000,大小可修改
     */
    private static BufferedImage resizeImageWithHint(BufferedImage originalImage){
        BufferedImage resizedImage = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, 1000, 1000, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        return resizedImage;
    }

    /**
     * 根据指定metadata删除对应图片和metadata文件（当有部分艺术家不想要的属性时，可根据此方法批量删除）
     * @param metadata 要删除的属性名list
     * @param metadataPath 对应 metadata path
     * @param imagePath 图片 path
     */
    private static void deleteByMetadata(List<String> metadata,String metadataPath,String imagePath) {
        File[] metas = new File(metadataPath).listFiles();
        assert metas != null;
        Arrays.stream(metas).forEach(file -> {
            boolean delete = false;
            try (FileReader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader)) {
                StringBuffer buffer = new StringBuffer();
                String tempStr;
                while ((tempStr = bufferedReader.readLine()) != null) {
                    buffer.append(tempStr);
                }
                if (metadata.stream().anyMatch(data -> buffer.toString().contains("\"".concat(data).concat("\"")))) {
                    delete = true;
                    String number = file.getName().replaceAll(METADATA_SUFFIX,"");
                    File delFile = new File(imagePath + number + IMAGE_SUFFIX);
                    delFile.delete();
                    log.info("delete image :{}",delFile.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(delete) {
                    file.delete();
                    log.info("delete metadata:{}",file.getAbsolutePath());
                }
            }
        });
    }

    /**
     * 根据要删除得图片集 删除对应的metadata数据
     */
    public static void deleteMetadataByImage(String deleteImageUrl,String metadataUrl) {
        File[] images = new File(deleteImageUrl).listFiles();
        Arrays.stream(Objects.requireNonNull(images)).forEach(file -> {
            String number = file.getName().substring(0, file.getName().lastIndexOf("."));
            File delFile = new File(metadataUrl + number + METADATA_SUFFIX);
            if ( delFile.delete() ) {
                log.info("del image success：{}",delFile.getAbsolutePath());
            }
        });
    }

    /**
     * 增序重命名图片&metadata
     * @param imagePath 需重命名的图片文件夹路径
     * @param metadataPath 重命名的metadata文件夹路径
     * @param startNum 起始数
     */
    public static void renameByStartNumber(String imagePath,String metadataPath,Integer startNum) {
        File[] images = new File(imagePath).listFiles();
        File[] metas = new File(metadataPath).listFiles();
        if (isEmpty(images)) {
            log.error("image path is empty");
        }
        if (isEmpty(metas)) {
            log.error("metadataPath is empty");
        }
        if (images.length != metas.length) {
            log.error("image amount not equal to metaData");
            return;
        }

        List<File> files = sortImageFile(images);
        for (File file : files) {
            if(file.renameTo(new File(imagePath + startNum + IMAGE_SUFFIX)) ) {
                log.info("image rename success：{}", startNum);
            }
            String number = file.getName().substring(0, file.getName().lastIndexOf("."));
            File metaData = new File(metadataPath + number + METADATA_SUFFIX);
            if (metaData.renameTo(new File(metadataPath + startNum + METADATA_SUFFIX)) ) {
                log.info("metadata rename success：{}",startNum);
            }
            startNum += 1;
        }
    }

    /**
     * 根据图片序号进行排序（默认排序是乱的）
     * @param images 图片集
     */
    private static List<File> sortImageFile(File[] images) {
        List<File> files = new ArrayList<File>(Arrays.asList(images));
        files.sort(new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return Integer.compare(getImageNumber(o2), getImageNumber(o1)) * -1;
            }
        });
        return files;
    }

    /**
     * 随机重排图片和元数据（图片数需等于元数据数量）
     * @param imagePath 图片路径
     * @param metadataPath 元数据路径
     */
    public static void randomSort(String imagePath,String metadataPath) {
        File[] images = new File(imagePath).listFiles();
        File[] metas = new File(metadataPath).listFiles();
        assert images != null;
        assert metas != null;
        if(images.length != metas.length) {
            log.error("Error: image number not equal to metas number");
            return;
        }
        Random rnd = new Random();
        for (int i = images.length; i > 1; i--) {
            int j = rnd.nextInt(i);
            j = j > 0 ? j : 1;
            int random = (new Random().nextInt(100000) * 10000);
            swapImage(images,imagePath,i - 1, j, random);
            swapMetadata(metas,metadataPath,i - 1, j, random);
        }
    }

    @SneakyThrows
    private static void swapImage(File[] arr,String imagePath, int i, int j,int random) {
        String copyPath = imagePath + random + ".png";
        String iPath = arr[i].getAbsolutePath();
        String jPath = arr[j].getAbsolutePath();
        if (new File(iPath).renameTo(new File(copyPath)) && new File(jPath).renameTo(new File(iPath)) &&
            new File(copyPath).renameTo(new File(jPath))) {
            log.info("image replace success:{},{}",i,j);
        }
    }

    @SneakyThrows
    private static void swapMetadata(File[] arr,String metadataPath, int i, int j, int random) {
        String copyPath = metadataPath + random + ".token.json";
        String iPath = arr[i].getAbsolutePath();
        String jPath = arr[j].getAbsolutePath();
        if (new File(iPath).renameTo(new File(copyPath)) && new File(jPath).renameTo(new File(iPath)) &&
                new File(copyPath).renameTo(new File(jPath))) {
            log.info("metadata replace success:{},{}",i,j);
        }
    }

    private static void copyFile(String fromPath,String toPath) {
        // 封装数据源
        try (FileInputStream fis = new FileInputStream(fromPath);
             // 打开输出流
             FileOutputStream fos = new FileOutputStream(toPath)) {

            // 读取和写入信息
            int len = 0;
            while ((len = fis.read()) != -1) {
                fos.write(len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getImageNumber(File file) {
        String number = file.getName().substring(0, file.getName().lastIndexOf("."));
        return Integer.parseInt(number);
    }

    private static int getMetadataNumber(File file) {
        String number = file.getName().replaceAll(METADATA_SUFFIX,"");
        return Integer.parseInt(number);
    }

    /**
     * 批量重写元数据（用于生成盲盒metadata）
     * 若nft想采用盲盒玩法，需要先复制对应数量的metadata文件，使用此方法批量替换内容，主要是image链接替换为盲盒图片链接
     * @param metadataPath 元数据地址
     * @param imagePath 盲盒图片地址
     */
    public static void batchChangeMetadata(String metadataPath,String imagePath) {
        File[] metas = new File(metadataPath).listFiles();
        assert metas != null;
        Arrays.stream(metas).forEach(file -> {
            int number = getMetadataNumber(file);
            String metaJson = String.format("{\"name\":\"Unrevealed %s\",\"image\":\"%s\",\"description\":\"\",\"attributes\":[]}", number,imagePath);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(metaJson);
                log.info("change metadata:number {}",number);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 设置源图片为背景透明，并设置透明度
     *
     * @param srcImage 源图片
     * @param alpha    透明度
     * @throws IOException
     */
    public static BufferedImage transparentImage(BufferedImage srcImage, int alpha) throws IOException {
        int imgHeight = srcImage.getHeight();//取得图片的长和宽
        int imgWidth = srcImage.getWidth();
        int c = srcImage.getRGB(3, 3);
        //防止越位
        if (alpha < 0) {
            alpha = 0;
        } else if (alpha > 10) {
            alpha = 10;
        }
        BufferedImage bi = new BufferedImage(imgWidth, imgHeight,
                BufferedImage.TYPE_4BYTE_ABGR);//新建一个类型支持透明的BufferedImage
        for (int i = 0; i < imgWidth; ++i)//把原图片的内容复制到新的图片，同时把背景设为透明
        {
            for (int j = 0; j < imgHeight; ++j) {
                //把背景设为透明
                if (srcImage.getRGB(i, j) == c) {
                    bi.setRGB(i, j, c & 0x00ffffff);
                }
//                //设置透明度
                else {
                    int rgb = srcImage.getRGB(i, j);

                    int R = (rgb & 0xff0000) >> 16;
                    int G = (rgb & 0xff00) >> 8;
                    int B = (rgb & 0xff);
                    if (((255 - R) < 30) && ((255 - G) < 30) && ((255 - B) < 30)) {
                        rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    }

                    bi.setRGB(i, j, rgb);
                }
            }
        }
        return bi;
    }


    public static boolean isEmpty(File[] files) {
        return files == null || files.length == 0;
    }

    @SneakyThrows
    public static void main(String[] args) {
        // step 1 生成图片
//        ImageUtil.batchDrawn();
        // 批量重置size（若有需要）
//        ImageUtil.resetSize("D:\\NFT-3-7-定稿\\pic-1\\");
        // step 2 挑选后根据删除的图片，删除对应metadata
//        ImageUtil.deleteMetadataByImage("D:\\NFT-FINISH\\version-1.0\\run\\奶白-图\\删除-2\\",
//                "D:\\NFT-FINISH\\version-1.0\\run\\奶白-图\\metadata\\");
        // step 3 不同系列根据不同起始id，增序重命名
//        ImageUtil.renameByStartNumber("D:\\NFT-FINISH\\version-1.0\\run\\final-v1\\pic\\",
//                "D:\\NFT-FINISH\\version-1.0\\run\\final-v1\\metadata\\",1);
        // 根据指定metadata名称删除对应图片和metadata （若有需要）
//        ImageUtil.deleteByMetadata(Arrays.asList("Green Sport Jacket","Orange Sport Jacket","Purple Sport Jacket","Yellow Sport Jacket","Sun Hat"),"D:\\NFT-FINISH\\version-1.0\\run\\final-after-delete\\metadata\\",
//                "D:\\NFT-FINISH\\version-1.0\\run\\final-after-delete\\pic\\");
        // step 4 随机重排图片和metadata
//        ImageUtil.randomSort("D:\\NFT-FINISH\\version-1.0\\run\\final-sort\\metadata\\",
//                "D:\\NFT-FINISH\\version-1.0\\run\\final-sort\\pic\\");
        // 盲盒玩法 生成盲盒metadata
        ImageUtil.batchChangeMetadata("D:\\NFT-FINISH\\version-1.0\\run\\FINALLY\\unknow-metadata\\",
                "https://xxxx/xxxx.gif");

    }

}
