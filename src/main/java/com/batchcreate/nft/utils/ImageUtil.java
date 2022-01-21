package com.batchcreate.nft.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedHashMap;

@Slf4j
public class ImageUtil {
    public static String SAVE_PATH = "D:\\NFT\\";
    public static Integer NFT_WIDTH = 350;
    public static Integer NFT_HIGH = 350;

    public static BufferedImage ImageAddWord(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * 批量生成NFT
     */
    public static void batchDrawn() {
        File[] backgrounds = new File("D:\\project\\nft\\src\\main\\resources\\background").listFiles();
        File[] glasses = new File("D:\\project\\nft\\src\\main\\resources\\glass").listFiles();
        File[] peoples = new File("D:\\project\\nft\\src\\main\\resources\\people").listFiles();
        File[] pets = new File("D:\\project\\nft\\src\\main\\resources\\pet").listFiles();

        BufferedImage buffImage = ImageAddWord(NFT_WIDTH,NFT_HIGH);

        Graphics2D g = buffImage.createGraphics();
        int i = 0;
        for (File back:backgrounds) {
            for(File people:peoples) {
                for(File glass:glasses) {
                    for(File pet : pets) {
                        try {
                            g.drawImage(ImageUtil.transparentImage(ImageIO.read(back),0).getScaledInstance(NFT_WIDTH,NFT_HIGH, Image.SCALE_SMOOTH), 0, 0, null);
                            g.drawImage(ImageUtil.transparentImage(ImageIO.read(people),0).getScaledInstance(NFT_WIDTH,NFT_HIGH, Image.SCALE_SMOOTH), 0, 0, null);
                            g.drawImage(ImageUtil.transparentImage(ImageIO.read(glass),0).getScaledInstance(NFT_WIDTH,NFT_HIGH, Image.SCALE_SMOOTH), 0, 0, null);
                            g.drawImage(ImageUtil.transparentImage(ImageIO.read(pet),0).getScaledInstance(NFT_WIDTH,NFT_HIGH, Image.SCALE_SMOOTH), 0, 0, null);
                            String filepath = SAVE_PATH + i + ".png";
                            ImageIO.write(buffImage, "png",new File(filepath));
                            i++;
                        } catch (IOException e) {
                            e.printStackTrace();
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
            for (int j = 0; j < imgHeight; ++j) {
                //设置透明度
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
        return bi;
    }

    @SneakyThrows
    public static void main(String [] args) {
//        File background = new File("D:\\project\\nft\\src\\main\\resources\\background\\xiongmao.png");
//        String filepath = SAVE_PATH + 1 + ".png";
//        ImageIO.write(ImageUtil.transparentImage(ImageIO.read(background),1), "png",new File(filepath));
        ImageUtil.batchDrawn();
    }
}
