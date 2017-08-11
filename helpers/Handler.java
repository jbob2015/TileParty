package helpers;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.nio.*;
import java.util.*;

import javax.imageio.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.openal.*;
import org.lwjgl.opengl.*;

import main.*;

public class Handler {

    private static ArrayList<Integer> sounds = new ArrayList<Integer>();

    public static int WIDTH, HEIGHT, T_WIDTH = 64, T_HEIGHT = 64;
    public static int PIXELS = 64;

    public static long window, device, arrowCursor, menuCursor, handCursor;

    /**
     * Opens the window and initially configures OpenGL and OpenAL
     */
    public static void beginSession() {
        beginGLFW();
        beginOpenGL();
        beginOpenAL();
    }

    /**
     * Opens the window and initially configures OpenGL and OpenAL
     */
    public static void debugSession() {
        debugGLFW();
        beginOpenGL();
        beginOpenAL();
    }

    /**
     * Creates Context, Sets WIDTH, HEIGHT, T_WIDTH, and T_HEIGHT
     */
    public static void beginGLFW() {
        if (!glfwInit()) {
            System.err.println("GLFW Failed to initialize!");
            System.exit(1);
        }

        /**
         * Create FullScreen Window and Set Tile Width/Height Based on Size
         */
        GLFWVidMode monitor = glfwGetVideoMode(glfwGetPrimaryMonitor());
        WIDTH = monitor.width();
        HEIGHT = monitor.height();

        if (WIDTH / HEIGHT != 16 / 9) {
            System.out.println("Yep");
            int width = 0;
            int height = 0;
            for (; height <= HEIGHT - 9
                    && width <= WIDTH; height += 9, width += 16) {
            }

            WIDTH = width;
            HEIGHT = height;
        }

        /**
         * Create and Show Window
         */
        window = glfwCreateWindow(WIDTH, HEIGHT, "Tile Party",
                glfwGetPrimaryMonitor(), 0);

        glfwShowWindow(window);
        glfwMakeContextCurrent(window);

        /**
         * Create and set up Cursors for Buttons Functionality
         */
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        menuCursor = createCursor();
        handCursor = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        glfwSetCursor(window, menuCursor);
    }

    /**
     * Creates Context, Sets WIDTH, HEIGHT, T_WIDTH, and T_HEIGHT
     */
    public static void debugGLFW() {
        if (!glfwInit()) {
            System.err.println("GLFW Failed to initialize!");
            System.exit(1);
        }

        /**
         * Create FullScreen Window and Set Tile Width/Height Based on Size
         */
        WIDTH = 1024;
        HEIGHT = 576;

        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);

        /**
         * Create and Show Window
         */
        window = glfwCreateWindow(WIDTH, HEIGHT, "Tile Party", 0, 0);
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);

        /**
         * Create and set up Cursors for Buttons Functionality
         */
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        arrowCursor = createCursor();
        handCursor = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        glfwSetCursor(window, arrowCursor);
    }

    /**
     * Sets up OpenGL before we load or draw anything
     */
    public static void beginOpenGL() {
        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);
        glMatrixMode(GL_PROJECTION);
        glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glLoadIdentity();
    }

    /**
     * Sets up OpenAL before we load or play anything
     */
    public static void beginOpenAL() {
        device = alcOpenDevice((ByteBuffer) null);
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);

        long context = alcCreateContext(device, (IntBuffer) null);
        alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);

        AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
        AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
    }

    /**
     *
     * Draws a Quad based on parameters
     *
     * @param x
     *            left position of quad
     * @param y
     *            top position of quad
     * @param width
     *            width of quad
     * @param height
     *            height of quad
     */
    public static void drawQuad(Color color, int x, int y, int width,
            int height) {

        glClearColor(color.getRed(), color.getGreen(), color.getBlue(),
                color.getAlpha());
        glClear(GL_COLOR_BUFFER_BIT);

        glBegin(GL_QUADS);
        glVertex2f(x, y);
        glVertex2f(x + width, y);
        glVertex2f(x + width, y + height);
        glVertex2f(x, y + height);
        glEnd();
    }

    /**
     *
     * @param tex
     *            texture to be drawn on a quad
     * @param x
     *            left position of quad
     * @param y
     *            top position of quad
     * @param width
     *            width of quad
     * @param height
     *            height of quad
     */
    public static void drawQuadTex(Texture tex, int x, int y, int width,
            int height) {
        tex.bind();
        glTranslatef(x, y, 0);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(0, 0);
        glTexCoord2f(1, 0);
        glVertex2f(width, 0);
        glTexCoord2f(1, 1);
        glVertex2f(width, height);
        glTexCoord2f(0, 1);
        glVertex2f(0, height);
        glEnd();
        glLoadIdentity();
    }

    //TODO Fix Sprite Sheet Drawing
    public static void drawSheetTex(Texture tex, int x, int y, int xPos,
            int yPos) {
        tex.bind();
        glTranslatef(xPos, yPos, 0);
        glBegin(GL_QUADS);
        glTexCoord2f(x / tex.getWidth(), y / tex.getHeight());
        glVertex2f(0, 0);
        glTexCoord2f(x / tex.getWidth(), y * 128 / tex.getHeight());
        glVertex2f(0, 128);
        glTexCoord2f(x * 128 / tex.getWidth(), y * 128 / tex.getHeight());
        glVertex2f(128, 128);
        glTexCoord2f(x * 128 / tex.getWidth(), y / tex.getHeight());
        glVertex2f(128, 0);
        glEnd();
        glLoadIdentity();

    }

    /**
     * Loads a texture and returns it
     *
     * @param path
     *            location of .png to be loaded
     * @return Texture object
     */
    public static Texture loadTex(String path) {
        return new Texture(path);
    }

    /**
     * Loads a sound and returns its id
     *
     * @param path
     *            location of .wav to be loaded
     * @return Texture object
     */
    public static int loadSound(String path) {
        int sound = AL10.alGenBuffers();
        sounds.add(sound);
        WaveData waveFile = WaveData.create("audio/" + path + ".wav");
        AL10.alBufferData(sound, waveFile.format, waveFile.data,
                waveFile.samplerate);
        waveFile.dispose();
        return sound;
    }

    /**
     * Deals with all loose ends and ends the program
     */
    public static void endSession() {
        for (int s : sounds) {
            AL10.alDeleteBuffers(s);
        }
        alcCloseDevice(device);
        StateManager.deleteSource();
        GLFW.glfwSetWindowShouldClose(window, true);
        System.exit(0);
    }

    public static void changeCursor(int id) {
        if (id < 7 && id > 0) {
            arrowCursor = createCursor(id);
            glfwSetCursor(window, arrowCursor);
        } else {
            glfwSetCursor(window, menuCursor);
        }
    }

    private static long createCursor() {
        InputStream stream;
        BufferedImage image = null;
        try {
            stream = new FileInputStream("./res/cursor.png");
            image = ImageIO.read(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = image.getWidth();
        int height = image.getHeight();

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        // convert image to RGBA format
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y * width + x];

                buffer.put((byte) ((pixel >> 16) & 0xFF)); // red
                buffer.put((byte) ((pixel >> 8) & 0xFF)); // green
                buffer.put((byte) (pixel & 0xFF)); // blue
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // alpha
            }
        }
        buffer.flip(); // this will flip the cursor image vertically

        // create a GLFWImage
        GLFWImage cursorImg = GLFWImage.create();
        cursorImg.width(width); // setup the images' width
        cursorImg.height(height); // setup the images' height
        cursorImg.pixels(buffer); // pass image data

        // create custom cursor and store its ID
        int hotspotX = 0;
        int hotspotY = 0;
        long cursorID = GLFW.glfwCreateCursor(cursorImg, hotspotX, hotspotY);
        return cursorID;
    }

    private static long createCursor(int id) {
        InputStream stream;
        BufferedImage image = null;
        try {
            stream = new FileInputStream("./res/cursor" + id + ".png");
            image = ImageIO.read(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = image.getWidth();
        int height = image.getHeight();

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        // convert image to RGBA format
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y * width + x];

                buffer.put((byte) ((pixel >> 16) & 0xFF)); // red
                buffer.put((byte) ((pixel >> 8) & 0xFF)); // green
                buffer.put((byte) (pixel & 0xFF)); // blue
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // alpha
            }
        }
        buffer.flip(); // this will flip the cursor image vertically

        // create a GLFWImage
        GLFWImage cursorImg = GLFWImage.create();
        cursorImg.width(width); // setup the images' width
        cursorImg.height(height); // setup the images' height
        cursorImg.pixels(buffer); // pass image data

        // create custom cursor and store its ID
        int hotspotX = 0;
        int hotspotY = 0;
        long cursorID = GLFW.glfwCreateCursor(cursorImg, hotspotX, hotspotY);
        return cursorID;
    }

}
