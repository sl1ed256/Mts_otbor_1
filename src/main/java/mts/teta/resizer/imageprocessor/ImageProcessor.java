package mts.teta.resizer.imageprocessor;


import mts.teta.resizer.ResizerApp;

import net.coobird.thumbnailator.Thumbnails;

import marvin.image.*;
import marvin.io.*;

import static marvinplugins.MarvinPluginCollection.*;

import java.io.File;
import javax.imageio.IIOException;


public class ImageProcessor {

    public void processImage(ResizerApp app) throws Exception {
        boolean[] valid = {true, true, true, true, true}; //для запуска функций по одной (если передан один параметр) или всех (при всех параметрах)

        File inputFile = app.getInputFile(); //получаем входной файл
        File outputFile = app.getOutputFile(); //получаем выходной файл

        int ResizeWidth = app.getResizeWidth();
        if (ResizeWidth <= 0) { //проверка параметра для resize
            valid[0] = false;
        }
        int ResizeHeight = app.getResizeHeight();
        if (ResizeHeight <= 0) { //проверка параметра для resize
            valid[0] = false;
        }

        int quality = app.getQuality();
        if (quality < 1 || quality > 100) { //проверка параметра для изменения качества
            valid[1] = false;
        }

        int targetCropWidth = app.getTargetCropWidth();
        if (targetCropWidth < 1) { //проверка параметра для crop
            valid[2] = false;
        }
        int targetCropHeight = app.getTargetCropHeight();
        if (targetCropHeight < 1) { //проверка параметра для crop
            valid[2] = false;
        }
        int x = app.getX();
        if (x < 1) { //проверка параметра для crop
            valid[2] = false;
        }
        int y = app.getY();
        if (y < 1) { //проверка параметра для crop
            valid[2] = false;
        }

        int blurRadius = app.getBlurRadius();
        if (blurRadius < 1) { //проверка параметра для размытия
            valid[3] = false;
        }

        String format = app.getOutputFormat();
        if (format == null) { //проверка параметра для формата
            valid[4] = false;
        }


        if (!inputFile.exists()) { //файл не существует
            throw new IIOException("Can't read input file!");
        }

        try {
            Thumbnails.of(inputFile).scale(1.).toFile(outputFile);
            if (valid[0]) { //если валидные параметры ресайза
                Thumbnails.of(outputFile).forceSize(ResizeWidth, ResizeHeight).toFile(outputFile);
            }
            if (valid[1]) { //если валидный параметр качества

                Thumbnails.of(outputFile).scale(1.).outputQuality(quality / 100.).toFile(outputFile);
            }
            if (valid[2]) { //если валидные параметры для кропа
                MarvinImage outImage = MarvinImageIO.loadImage(outputFile.getAbsolutePath()); //получаем результат из предыдущих функций и работаем с ней
                crop(outImage.clone(), outImage, x, y, targetCropWidth, targetCropHeight);
                MarvinImageIO.saveImage(outImage, outputFile.getAbsolutePath());
            }
            if (valid[3]) { //если валидный параметры для размытия
                MarvinImage outImage = MarvinImageIO.loadImage(outputFile.getAbsolutePath()); //получаем копию из предыдущих функций и работаем с ней
                gaussianBlur(outImage.clone(), outImage, blurRadius);
                MarvinImageIO.saveImage(outImage, outputFile.getAbsolutePath());
            }
            if (valid[4]) { //если валидный параметр для выходного формата
                Thumbnails.of(outputFile).scale(1.).outputFormat(format).toFile(outputFile);
            }

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            throw new BadAttributesException("Please check params!");
        } catch (IllegalArgumentException e) { //если функции вернули ошибку
            System.out.println(e.getMessage());
            throw new BadAttributesException("Please check params!");
        } catch (IIOException e) { //проблемы с файлом
            System.out.println(e.getMessage());
            throw new IIOException("Please check files params!");
        }
        return;
    }
}
