package com.batchcreate.nft.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import javax.imageio.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ImageUtil {
    public static String SAVE_PATH = "D:\\NFT-FINISH\\version-1.0\\多数基础款-图\\pic\\";
    public static String META_DATA_PATH = "D:\\NFT-FINISH\\version-1.0\\多数基础款-图\\metadata\\";

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


    public static void resetSize() {
        File[] images = new File("D:\\NFT-3-7-定稿\\pic-1\\").listFiles();
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
     * 重置图片大小
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
     * 根据要删除得图片集 删除对应的metadata数据
     */
    public static void deleteMetadataByImage() {
        String deleteImageUrl = "D:\\NFT-FINISH\\version-1.0\\test\\delete\\";
        String metadataUrl = "D:\\NFT-FINISH\\version-1.0\\test\\metaData\\";
        File[] images = new File(deleteImageUrl).listFiles();
        Arrays.stream(Objects.requireNonNull(images)).forEach(file -> {
            String number = file.getName().substring(0, file.getName().lastIndexOf("."));
            File delFile = new File(metadataUrl + number + ".token.json");
            delFile.delete();
            log.info("删除文件：{}",delFile.getAbsolutePath());
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


    @SneakyThrows
    public static void main(String[] args) {
        // 生成图片
//        ImageUtil.batchDrawn();
        // 重置size
//        ImageUtil.resetSize();
        // 删除文件
        ImageUtil.deleteMetadataByImage();
    }

}
