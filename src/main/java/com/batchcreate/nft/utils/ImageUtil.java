package com.batchcreate.nft.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.imageio.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ImageUtil {
    public static String SAVE_PATH = "D:\\NFT\\";

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
     * @param path
     * @return LinkedHashMap<String,String>
     */
    public static LinkedHashMap<String,String> getFiles(String path) {
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
        File[] backgrounds = new File("D:\\project\\nft\\src\\main\\resources\\background").listFiles();
        File[] glasses = new File("D:\\project\\nft\\src\\main\\resources\\glass").listFiles();
        File[] peoples = new File("D:\\project\\nft\\src\\main\\resources\\people").listFiles();
        File[] maozi = new File("D:\\project\\nft\\src\\main\\resources\\maozi").listFiles();
        File[] pet = new File("D:\\project\\nft\\src\\main\\resources\\pet").listFiles();
        File[] shous = new File("D:\\project\\nft\\src\\main\\resources\\shou").listFiles();
        AsyncTaskExecutor executor = getAsyncExecutor();


        AtomicInteger integer = new AtomicInteger(0);
        for (File lian : maozi) {
                for (File glass : glasses) {
                    for (File dao : pet) {
                        for (File shou : shous) {
                            for (File people : peoples) {
                            for (File back : backgrounds) {
                                PooledImageWriter imageWriter = new PooledImageWriter("png", 10);
                                executor.execute(() -> {
                                            try {
                                                BufferedImage buffImage = ImageAddWord(800, 800);
                                                Graphics2D g = buffImage.createGraphics();
                                                g.drawImage(ImageIO.read(back).getScaledInstance(800, 800, Image.SCALE_SMOOTH), 0, 0, null);
                                                g.drawImage(ImageIO.read(people).getScaledInstance(800, 800, Image.SCALE_SMOOTH), 0, 0, null);
                                                g.drawImage(ImageIO.read(dao).getScaledInstance(800, 800, Image.SCALE_SMOOTH), 0, 0, null);
                                                g.drawImage(ImageIO.read(lian).getScaledInstance(800, 800, Image.SCALE_SMOOTH), 0, 0, null);
                                                g.drawImage(ImageIO.read(glass).getScaledInstance(800, 800, Image.SCALE_SMOOTH), 0, 0, null);
                                                g.drawImage(ImageIO.read(shou).getScaledInstance(800, 800, Image.SCALE_SMOOTH), 0, 0, null);
                                                String filepath = SAVE_PATH + integer.get() + ".png";

                                                imageWriter.write(buffImage, new File(filepath));
//                                                                Thread.sleep(1000);
                                                log.info("生成图片{}张", integer.get());
                                                integer.addAndGet(1);

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                );
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 设置源图片为背景透明，并设置透明度
     * @param srcImage 源图片
     * @param alpha 透明度
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
        for(int i = 0; i < imgWidth; ++i)//把原图片的内容复制到新的图片，同时把背景设为透明
        {
            for(int j = 0; j < imgHeight; ++j)
            {
                //把背景设为透明
                if(srcImage.getRGB(i, j) == c){
                    bi.setRGB(i, j, c & 0x00ffffff);
                }
//                //设置透明度
                else{
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
    public static void main(String [] args) {
//        File background = new File("K:\\nft\\src\\main\\resources\\background\\maomao.png");
//        String filepath = SAVE_PATH + 1 + ".png";
//        ImageIO.write(ImageIO.read(background),0), "png",new File(filepath));
        ImageUtil.batchDrawn();
    }


}
