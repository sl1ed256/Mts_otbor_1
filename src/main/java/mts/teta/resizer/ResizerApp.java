package mts.teta.resizer;


import mts.teta.resizer.imageprocessor.ImageProcessor;
import picocli.CommandLine;


import java.io.File;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "convert", mixinStandardHelpOptions = false, version = "resizer 0.0.1", description = "Option settings:",
        header = "Version: crop-crop 1.0 https://github.com/sl1ed256/mts-otbor\n" + "Available formats: jpeg png", sortOptions = false)
class ConsoleAttributes {
    @CommandLine.Parameters(index = "0", description = "input-file")
    protected File inputFile = null;

    @CommandLine.Parameters(index = "1", description = "output-file")
    protected File outputFile = null;

    @CommandLine.Option(names = "--resize", paramLabel = "width height", description = "resize the image")
    protected int ResizeWidth = -1;
    protected int ResizeHeight = -1;

    @CommandLine.Option(names = "--quality", paramLabel = "value", arity = "1", description = "JPEG/PNG compression level")
    protected int requiredQuality = -1;

    @CommandLine.Option(names = "--crop", paramLabel = "width height x y", description = "cut out one rectangular area of the image")
    protected int targetCropWidth = -1;
    protected int targetCropHeight = -1;
    protected int X = -1;
    protected int Y = -1;

    @CommandLine.Option(names = "--blur", paramLabel = "radius", arity = "1", description = "reduce image noise detail levels")
    protected int blurRadius = -1;

    @CommandLine.Option(names = "--format", paramLabel = "'outputFormat'", arity = "1", description = "the image format type")
    protected String formatOfOutput = null;
}

public class ResizerApp extends ConsoleAttributes implements Callable<Integer> {
    public static void main(String... args) {
        int exitCode = runConsole(args);
        System.exit(exitCode);
    }

    protected static int runConsole(String[] args) {
        return new CommandLine(new ResizerApp()).execute(args);
    }

    public void setInputFile(File file) {
        this.inputFile = file; //сеттер входного файла
        return;
    }

    public File getInputFile() {
        return this.inputFile; //геттер входного файла
    }

    public void setOutputFile(File file) {
        this.outputFile = file; //сеттер входного файла
        return;
    }

    public File getOutputFile() {
        return this.outputFile; //геттер выходного файла
    }

    public void setResizeWidth(int width) {
        this.ResizeWidth = width; // cеттер ширины ресайза
        return;
    }

    public int getResizeWidth() {
        return this.ResizeWidth; //геттер ширины ресайза
    }

    public void setResizeHeight(int height) {
        this.ResizeHeight = height; // cеттер высоты ресайза
        return;
    }

    public int getResizeHeight() {
        return this.ResizeHeight; //геттер высоты ресайза
    }

    public void setQuality(int quality) {
        this.requiredQuality = quality; // сеттер качества
        return;
    }

    public int getQuality() {
        return this.requiredQuality; // геттер качества
    }

    public int getTargetCropWidth() { // геттер для ширины кропа
        return this.targetCropWidth;
    }

    public int getTargetCropHeight() { // геттер для высоты кропа
        return this.targetCropHeight;
    }

    public int getX() { // геттер  х кропа
        return this.X;
    }

    public int getY() { // геттер для  y кропа
        return this.Y;
    }

    public int getBlurRadius() { // геттер для  радиуса размытия
        return this.blurRadius;
    }

    public String getOutputFormat() { // геттер для выходного формата
        return this.formatOfOutput;
    }

    @Override
    public Integer call() throws Exception {
        ImageProcessor imageProcessor = new ImageProcessor();
        imageProcessor.processImage(this);
        return 0;
    }
}
