package com.RedundancyCodes.pw.edu.pl.services.impl.fileServices;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class PNGWriter {

    private int width;
    private int height;

    public PNGWriter(int width, int height) {
        this.height = height;
        this.width = width;
    }

    public boolean convertToFile(int[] rgbs, String fname) {

        DataBuffer rgbData = new DataBufferInt(rgbs, rgbs.length);

        WritableRaster raster = Raster.createPackedRaster(rgbData, width, height, width,
                new int[]{0xff0000, 0xff00, 0xff, 0xff000000},
                null);

        ColorModel colorModel = new DirectColorModel(32, 0xff0000, 0xff00, 0xff, 0xff000000);

        BufferedImage img = new BufferedImage(colorModel, raster, false, null);

        try {
            ImageIO.write(img, "png", new File(fname));
        } catch (IOException e) {
            System.out.println("Issue occurred while writing " + fname);
            return false;
        }
        return true;
    }

}
