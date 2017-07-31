package main;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.*;

import org.lwjgl.*;

public class Texture {
    private int id;
    private int width;
    private int height;
    public String name;

    public Texture(String filename) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

        ByteBuffer data = stbi_load("./res/" + filename + ".png", width, height,
                comp, 4);

        this.id = glGenTextures();
        this.width = width.get();
        this.height = height.get();
        this.name = filename;

        this.bind();

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
                GL_NEAREST_MIPMAP_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, data);

        glGenerateMipmap(GL_TEXTURE_2D);

        //System.err.println(stbi_failure_reason());
        stbi_image_free(data);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.id);
    }

    public int getImageWidth() {
        return this.getWidth();
    }

    public int getImageHeight() {
        return this.getHeight();
    }

}
