package helpers;

import static helpers.Handler.*;
import static org.lwjgl.glfw.GLFW.*;

import java.nio.*;

import org.lwjgl.*;

public class Mouse {
    private static float x, y;
    private static DoubleBuffer xTemp, yTemp;
    private static boolean lastLeft = false, currentLeft = false;
    private static boolean lastRight = false, currentRight = false;
    private static boolean hand = false;

    public Mouse() {
        xTemp = BufferUtils.createDoubleBuffer(1);
        yTemp = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(window, xTemp, yTemp);
//        int offset = 0;
//        if(StateManager.game != null) {
//            offset = StateManager.game.player1.getX();
//        }
        x = (float) xTemp.get(0);// + offset);
        y = HEIGHT - (float) (yTemp.get(0)) - 1;
    }

    /**
     * Updates Coordinates and Button States
     */
    public static void update() {
        updateCoordinates();
        updateClicks();
    }

    /**
     * Update x and y coordinates for the mouse
     */
    private static void updateCoordinates() {
        glfwGetCursorPos(window, xTemp, yTemp);
        x = (int) Math.floor(xTemp.get(0));
        y = (int) Math.floor(yTemp.get(0));
    }

    /**
     * Updates button states to determine if a button is clicked
     */
    private static void updateClicks() {
        lastLeft = currentLeft;
        lastRight = currentRight;
        if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS) {
            currentLeft = true;
        } else {
            currentLeft = false;
        }

        if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_RIGHT) == GLFW_PRESS) {
            currentRight = true;
        } else {
            currentRight = false;
        }
    }

    /**
     * Gets mouse x coordinate
     *
     * @return Mouse x coordinate
     */
    public static float getX() {
        return x;
    }

    /**
     * Gets mouse y coordinate
     *
     * @return Mouse y coordinate
     */
    public static float getY() {
        return y;
    }

    /**
     * Returns true if LMB is clicked
     *
     * @return true if LMB is clicked
     */
    public static boolean leftClick() {
        return !lastLeft && currentLeft;
    }

    /**
     * Returns true if RMB is clicked
     *
     * @return true if RMB is clicked
     */
    public static boolean rightClick() {
        return !lastRight && currentRight;
    }

    /**
     * Returns true if LMB is pressed
     *
     * @return true if LMB is pressed
     */
    public static boolean leftDrag() {
        return currentLeft;
    }

    /**
     * Returns true if RMB is pressed
     *
     * @return true if RMB is pressed
     */
    public static boolean rightDrag() {
        return currentRight;
    }

    /**
     * Returns true if LMB is not pressed
     *
     * @return true if LMB is not pressed
     */
    public static boolean leftRelease() {
        return glfwGetMouseButton(window,
                GLFW_MOUSE_BUTTON_LEFT) == GLFW_RELEASE;
    }

    /**
     * Returns true if RMB is not pressed
     *
     * @return true if RMB is not pressed
     */
    public static boolean rightRelease() {
        return glfwGetMouseButton(window,
                GLFW_MOUSE_BUTTON_RIGHT) == GLFW_RELEASE;
    }

    public static void setHand(boolean hand) {
        if (hand && !Mouse.hand) {
            Mouse.hand = true;
            glfwSetCursor(window, handCursor);
        } else if (!hand && Mouse.hand) {
            Mouse.hand = false;
            if (StateManager.game != null) {
                glfwSetCursor(window, arrowCursor);
            } else {
                glfwSetCursor(window, menuCursor);
            }
        }
    }

    public static void hideCursor() {
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
    }

    public static void showCursor() {
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

}
