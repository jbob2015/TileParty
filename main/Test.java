package main;

import static helpers.Handler.*;
import static org.lwjgl.glfw.GLFW.*;

import helpers.*;
import ui.*;

public class Test {

    public Test() {
        debugSession();
        new Keyboard();
        new Mouse();

        CheckBox test = new CheckBox("Test", WIDTH / 2 - 16, HEIGHT / 2 - 16);

        while (!glfwWindowShouldClose(window)
                && !Keyboard.isClicked(Keyboard.Key.ESCAPE)) {
            glfwPollEvents();
            Keyboard.update();
            Mouse.update();

            test.update();
            test.draw();
            if (test.isChecked) {
                System.out.println("Checked");
            } else {
                System.out.println("UnChecked");
            }

            glfwSwapBuffers(window);
        }

        endSession();
        long bombStartTime = System.currentTimeMillis();
        while (bombStartTime > System.currentTimeMillis() - 5000) {
        }
        //Blow up Bomb
        System.out.println("BOOOOM!!!");

    }

    public static void main(String[] args) {
        new Test();
    }

}
