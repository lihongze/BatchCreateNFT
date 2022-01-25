package com.batchcreate.nft.utils;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PooledImageWriter {
    static List<ImageWriter> mPool = new ArrayList<ImageWriter>();

    public PooledImageWriter(String formatName, int size) {
        for (int i = 0; i < size; i++) {
            mPool.add((ImageWriter) ImageIO.getImageWritersByFormatName(formatName).next());
        }
    }

    static ImageWriter get() {
        if (mPool.isEmpty()) {
            return null;
        }
        return mPool.remove(0);
    }

    static void put(ImageWriter o) {
        mPool.add(o);
    }

    public static synchronized void write(BufferedImage image, File file) throws Exception {
        ImageWriter writer = get();

        ImageOutputStream ios = ImageIO.createImageOutputStream(file);
        writer.reset();
        writer.setOutput(ios);
        writer.write(image);
        ios.close();

        put(writer);
    }
}