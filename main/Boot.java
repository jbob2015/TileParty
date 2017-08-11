package main;

import static helpers.Handler.*;
import static org.lwjgl.glfw.GLFW.*;

import helpers.*;

public class Boot {

    public static float t = 0f;
    public static final long dt = 16;

    public static long currentTime = System.currentTimeMillis();
    public static float accumulator = 0;
    public static float alpha;

    public Boot() {
        try {

            // Setup OpenGL, OpenAL, GLFW
            //beginSession();

            debugSession();

            // Start GameStateManager
            StateManager manager = new StateManager();

            while (!glfwWindowShouldClose(window)) {
                // Process All Pending Events
                glfwPollEvents();

                long frameTime = (System.currentTimeMillis()
                        - Boot.currentTime);
                if (frameTime > 16) {
                    frameTime = 16;
                } // note: max frame time to avoid spiral of death
                Boot.currentTime = System.currentTimeMillis();

                Boot.accumulator += frameTime;

                while (Boot.accumulator >= Boot.dt) {
                    // Update GameStateManager (Essentially Everything)
                    manager.update();
                    Boot.t += Boot.dt;
                    Boot.accumulator -= Boot.dt;
                }

                alpha = Boot.accumulator / Boot.dt;

                manager.draw();

                // Swap Buffers, Draw to Screen (Where All Drawing Updates Happen)
                glfwSwapBuffers(window);
            }

            // Close OpenGL, OpenAL, GLFW
            endSession();
        } catch (Exception e) {
            System.err.println("Oops, our bad");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Boot();
    }

}
