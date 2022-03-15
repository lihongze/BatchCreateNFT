package com.batchcreate.nft.utils;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class PooledImageWriter {
    static List<ImageWriter> mPool = new ArrayList<ImageWriter>();

    public PooledImageWriter(String formatName, int size) {
        for (int i = 0; i < size; i++) {
            mPool.add((ImageWriter) ImageIO.getImageWritersByFormatName(formatName).next());
        }
    }

    synchronized ImageWriter get() {
        if (mPool.isEmpty()) {
            return null;
        }
        return mPool.remove(0);
    }

    synchronized void put(ImageWriter o) {
        mPool.add(o);
    }

    public void write(BufferedImage image, File file,String jsonPath,String json) throws Exception {
        ImageWriter writer = get();
        FileWriter writer1 = new FileWriter(new File(jsonPath));
        writer1.write(json);
        writer1.close();

        ImageOutputStream ios = ImageIO.createImageOutputStream(file);
        writer.reset();
        writer.setOutput(ios);
        writer.write(image);
        ios.close();

        put(writer);
    }
}