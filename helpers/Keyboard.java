package helpers;

import static helpers.Handler.*;
import static org.lwjgl.glfw.GLFW.*;

import java.util.*;

public class Keyboard {

    public static enum Key {
        SPACE(GLFW_KEY_SPACE),
        APOSTROPHE(GLFW_KEY_APOSTROPHE), //'
        COMMA(GLFW_KEY_COMMA), //,
        MINUS(GLFW_KEY_MINUS), //-
        PERIOD(GLFW_KEY_PERIOD), //.
        SLASH(GLFW_KEY_SLASH), // /
        ZERO(GLFW_KEY_0),
        ONE(GLFW_KEY_1),
        TWO(GLFW_KEY_2),
        THREE(GLFW_KEY_3),
        FOUR(GLFW_KEY_4),
        FIVE(GLFW_KEY_5),
        SIX(GLFW_KEY_6),
        SEVEN(GLFW_KEY_7),
        EIGHT(GLFW_KEY_8),
        NINE(GLFW_KEY_9),
        SEMICOLON(GLFW_KEY_SEMICOLON),
        EQUAL(GLFW_KEY_EQUAL),
        A(GLFW_KEY_A),
        B(GLFW_KEY_B),
        C(GLFW_KEY_C),
        D(GLFW_KEY_D),
        E(GLFW_KEY_E),
        F(GLFW_KEY_F),
        G(GLFW_KEY_G),
        H(GLFW_KEY_H),
        I(GLFW_KEY_I),
        J(GLFW_KEY_J),
        K(GLFW_KEY_K),
        L(GLFW_KEY_L),
        M(GLFW_KEY_M),
        N(GLFW_KEY_N),
        O(GLFW_KEY_O),
        P(GLFW_KEY_P),
        Q(GLFW_KEY_Q),
        R(GLFW_KEY_R),
        S(GLFW_KEY_S),
        T(GLFW_KEY_T),
        U(GLFW_KEY_U),
        V(GLFW_KEY_V),
        W(GLFW_KEY_W),
        X(GLFW_KEY_X),
        Y(GLFW_KEY_Y),
        Z(GLFW_KEY_Z),
        LEFT_BRACKET(GLFW_KEY_LEFT_BRACKET), // [
        BACKSLASH(GLFW_KEY_BACKSLASH), // \
        RIGHT_BRACKET(GLFW_KEY_RIGHT_BRACKET), // ]
        GRAVE_ACCENT(GLFW_KEY_GRAVE_ACCENT), // `
        ESCAPE(GLFW_KEY_ESCAPE),
        ENTER(GLFW_KEY_ENTER),
        TAB(GLFW_KEY_TAB),
        BACKSPACE(GLFW_KEY_BACKSPACE),
        INSERT(GLFW_KEY_INSERT),
        DELETE(GLFW_KEY_DELETE),
        RIGHT(GLFW_KEY_RIGHT),
        LEFT(GLFW_KEY_LEFT),
        UP(GLFW_KEY_UP),
        DOWN(GLFW_KEY_DOWN),
        PAGE_UP(GLFW_KEY_PAGE_UP),
        PAGE_DOWN(GLFW_KEY_PAGE_DOWN),
        HOME(GLFW_KEY_HOME),
        END(GLFW_KEY_END),
        CAPS_LOCK(GLFW_KEY_CAPS_LOCK),
        SCROLL_LOCK(GLFW_KEY_SCROLL_LOCK),
        NUM_LOCK(GLFW_KEY_NUM_LOCK),
        PRINT_SCREEN(GLFW_KEY_PRINT_SCREEN),
        PAUSE(GLFW_KEY_PAUSE),
        F1(GLFW_KEY_F1),
        F2(GLFW_KEY_F2),
        F3(GLFW_KEY_F3),
        F4(GLFW_KEY_F4),
        F5(GLFW_KEY_F5),
        F6(GLFW_KEY_F6),
        F7(GLFW_KEY_F7),
        F8(GLFW_KEY_F8),
        F9(GLFW_KEY_F9),
        F10(GLFW_KEY_F10),
        F11(GLFW_KEY_F11),
        F12(GLFW_KEY_F12),
        NUM_0(GLFW_KEY_KP_0),
        NUM_1(GLFW_KEY_KP_1),
        NUM_2(GLFW_KEY_KP_2),
        NUM_3(GLFW_KEY_KP_3),
        NUM_4(GLFW_KEY_KP_4),
        NUM_5(GLFW_KEY_KP_5),
        NUM_6(GLFW_KEY_KP_6),
        NUM_7(GLFW_KEY_KP_7),
        NUM_8(GLFW_KEY_KP_8),
        NUM_9(GLFW_KEY_KP_9),
        DECIMAL(GLFW_KEY_KP_DECIMAL),
        DIVIDE(GLFW_KEY_KP_DIVIDE),
        MULIPLY(GLFW_KEY_KP_MULTIPLY),
        SUBTRACT(GLFW_KEY_KP_SUBTRACT),
        ADD(GLFW_KEY_KP_ADD),
        NUM_ENTER(GLFW_KEY_ENTER),
        NUM_EQUAL(GLFW_KEY_EQUAL),
        LEFT_SHIFT(GLFW_KEY_LEFT_SHIFT),
        LEFT_CONTROL(GLFW_KEY_LEFT_CONTROL),
        LEFT_ALT(GLFW_KEY_LEFT_ALT),
        RIGHT_SHIFT(GLFW_KEY_RIGHT_SHIFT),
        RIGHT_CONTROL(GLFW_KEY_RIGHT_CONTROL),
        RIGHT_ALT(GLFW_KEY_RIGHT_ALT),
        NULL(GLFW_KEY_UNKNOWN);

        int code;

        Key(int code) {
            this.code = code;
        }

        public boolean isPressed() {
            return this.code != GLFW_KEY_UNKNOWN
                    ? glfwGetKey(window, this.code) == GLFW_PRESS : false;
        }

        public boolean isReleased() {
            return this.code != GLFW_KEY_UNKNOWN
                    ? glfwGetKey(window, this.code) == GLFW_RELEASE : false;
        }
    }

    private static ArrayList<Key> lastKeys, currentKeys;

    public Keyboard() {
        Keyboard.lastKeys = new ArrayList<>();
        Keyboard.currentKeys = new ArrayList<>();
    }

    public static void update() {
        Keyboard.lastKeys = Keyboard.currentKeys;
        getCurrentKeys();
    }

    private static void getCurrentKeys() {
        ArrayList<Key> keyList = new ArrayList<>();
        if (Key.ZERO.isPressed()) {
            keyList.add(Key.ZERO);
        }
        if (Key.A.isPressed()) {
            keyList.add(Key.A);
        }
        if (Key.ADD.isPressed()) {
            keyList.add(Key.ADD);
        }
        if (Key.APOSTROPHE.isPressed()) {
            keyList.add(Key.APOSTROPHE);
        }
        if (Key.B.isPressed()) {
            keyList.add(Key.B);
        }
        if (Key.BACKSLASH.isPressed()) {
            keyList.add(Key.BACKSLASH);
        }
        if (Key.BACKSPACE.isPressed()) {
            keyList.add(Key.BACKSPACE);
        }
        if (Key.C.isPressed()) {
            keyList.add(Key.C);
        }
        if (Key.CAPS_LOCK.isPressed()) {
            keyList.add(Key.CAPS_LOCK);
        }
        if (Key.COMMA.isPressed()) {
            keyList.add(Key.COMMA);
        }
        if (Key.D.isPressed()) {
            keyList.add(Key.D);
        }
        if (Key.DECIMAL.isPressed()) {
            keyList.add(Key.DECIMAL);
        }
        if (Key.DELETE.isPressed()) {
            keyList.add(Key.DELETE);
        }
        if (Key.DIVIDE.isPressed()) {
            keyList.add(Key.DIVIDE);
        }
        if (Key.DOWN.isPressed()) {
            keyList.add(Key.DOWN);
        }
        if (Key.E.isPressed()) {
            keyList.add(Key.E);
        }
        if (Key.END.isPressed()) {
            keyList.add(Key.END);
        }
        if (Key.ENTER.isPressed()) {
            keyList.add(Key.ENTER);
        }
        if (Key.EQUAL.isPressed()) {
            keyList.add(Key.EQUAL);
        }
        if (Key.ESCAPE.isPressed()) {
            keyList.add(Key.ESCAPE);
        }
        if (Key.F.isPressed()) {
            keyList.add(Key.F);
        }
        if (Key.F1.isPressed()) {
            keyList.add(Key.F1);
        }
        if (Key.F10.isPressed()) {
            keyList.add(Key.F10);
        }
        if (Key.F11.isPressed()) {
            keyList.add(Key.F11);
        }
        if (Key.F12.isPressed()) {
            keyList.add(Key.F12);
        }
        if (Key.F2.isPressed()) {
            keyList.add(Key.F2);
        }
        if (Key.F3.isPressed()) {
            keyList.add(Key.F3);
        }
        if (Key.F4.isPressed()) {
            keyList.add(Key.F4);
        }
        if (Key.F5.isPressed()) {
            keyList.add(Key.F5);
        }
        if (Key.F6.isPressed()) {
            keyList.add(Key.F6);
        }
        if (Key.F7.isPressed()) {
            keyList.add(Key.F7);
        }
        if (Key.F8.isPressed()) {
            keyList.add(Key.F8);
        }
        if (Key.F9.isPressed()) {
            keyList.add(Key.F9);
        }
        if (Key.G.isPressed()) {
            keyList.add(Key.G);
        }
        if (Key.GRAVE_ACCENT.isPressed()) {
            keyList.add(Key.GRAVE_ACCENT);
        }
        if (Key.H.isPressed()) {
            keyList.add(Key.H);
        }
        if (Key.HOME.isPressed()) {
            keyList.add(Key.HOME);
        }
        if (Key.I.isPressed()) {
            keyList.add(Key.I);
        }
        if (Key.INSERT.isPressed()) {
            keyList.add(Key.INSERT);
        }
        if (Key.J.isPressed()) {
            keyList.add(Key.J);
        }
        if (Key.K.isPressed()) {
            keyList.add(Key.K);
        }
        if (Key.L.isPressed()) {
            keyList.add(Key.L);
        }
        if (Key.LEFT.isPressed()) {
            keyList.add(Key.LEFT);
        }
        if (Key.LEFT_ALT.isPressed()) {
            keyList.add(Key.LEFT_ALT);
        }
        if (Key.LEFT_BRACKET.isPressed()) {
            keyList.add(Key.LEFT_BRACKET);
        }
        if (Key.LEFT_CONTROL.isPressed()) {
            keyList.add(Key.LEFT_CONTROL);
        }
        if (Key.LEFT_SHIFT.isPressed()) {
            keyList.add(Key.LEFT_SHIFT);
        }
        if (Key.M.isPressed()) {
            keyList.add(Key.M);
        }
        if (Key.MINUS.isPressed()) {
            keyList.add(Key.MINUS);
        }
        if (Key.MULIPLY.isPressed()) {
            keyList.add(Key.MULIPLY);
        }
        if (Key.N.isPressed()) {
            keyList.add(Key.N);
        }
        if (Key.NUM_0.isPressed()) {
            keyList.add(Key.NUM_0);
        }
        if (Key.NUM_1.isPressed()) {
            keyList.add(Key.NUM_1);
        }
        if (Key.NUM_2.isPressed()) {
            keyList.add(Key.NUM_2);
        }
        if (Key.NUM_3.isPressed()) {
            keyList.add(Key.NUM_3);
        }
        if (Key.NUM_4.isPressed()) {
            keyList.add(Key.NUM_4);
        }
        if (Key.NUM_5.isPressed()) {
            keyList.add(Key.NUM_5);
        }
        if (Key.NUM_6.isPressed()) {
            keyList.add(Key.NUM_6);
        }
        if (Key.NUM_7.isPressed()) {
            keyList.add(Key.NUM_7);
        }
        if (Key.NUM_8.isPressed()) {
            keyList.add(Key.NUM_8);
        }
        if (Key.NUM_9.isPressed()) {
            keyList.add(Key.NUM_9);
        }
        if (Key.NUM_ENTER.isPressed()) {
            keyList.add(Key.NUM_ENTER);
        }
        if (Key.NUM_EQUAL.isPressed()) {
            keyList.add(Key.NUM_EQUAL);
        }
        if (Key.NUM_LOCK.isPressed()) {
            keyList.add(Key.NUM_LOCK);
        }
        if (Key.O.isPressed()) {
            keyList.add(Key.O);
        }
        if (Key.P.isPressed()) {
            keyList.add(Key.P);
        }
        if (Key.PAGE_DOWN.isPressed()) {
            keyList.add(Key.PAGE_DOWN);
        }
        if (Key.PAGE_UP.isPressed()) {
            keyList.add(Key.PAGE_UP);
        }
        if (Key.PAUSE.isPressed()) {
            keyList.add(Key.PAUSE);
        }
        if (Key.PERIOD.isPressed()) {
            keyList.add(Key.PERIOD);
        }
        if (Key.PRINT_SCREEN.isPressed()) {
            keyList.add(Key.PRINT_SCREEN);
        }
        if (Key.Q.isPressed()) {
            keyList.add(Key.Q);
        }
        if (Key.R.isPressed()) {
            keyList.add(Key.R);
        }
        if (Key.RIGHT.isPressed()) {
            keyList.add(Key.RIGHT);
        }
        if (Key.RIGHT_ALT.isPressed()) {
            keyList.add(Key.RIGHT_ALT);
        }
        if (Key.RIGHT_BRACKET.isPressed()) {
            keyList.add(Key.RIGHT_BRACKET);
        }
        if (Key.RIGHT_CONTROL.isPressed()) {
            keyList.add(Key.RIGHT_CONTROL);
        }
        if (Key.RIGHT_SHIFT.isPressed()) {
            keyList.add(Key.RIGHT_SHIFT);
        }
        if (Key.S.isPressed()) {
            keyList.add(Key.S);
        }
        if (Key.SCROLL_LOCK.isPressed()) {
            keyList.add(Key.SCROLL_LOCK);
        }
        if (Key.SEMICOLON.isPressed()) {
            keyList.add(Key.SEMICOLON);
        }
        if (Key.SLASH.isPressed()) {
            keyList.add(Key.SLASH);
        }
        if (Key.SPACE.isPressed()) {
            keyList.add(Key.SPACE);
        }
        if (Key.SUBTRACT.isPressed()) {
            keyList.add(Key.SUBTRACT);
        }
        if (Key.T.isPressed()) {
            keyList.add(Key.T);
        }
        if (Key.TAB.isPressed()) {
            keyList.add(Key.TAB);
        }
        if (Key.U.isPressed()) {
            keyList.add(Key.U);
        }
        if (Key.UP.isPressed()) {
            keyList.add(Key.UP);
        }
        if (Key.V.isPressed()) {
            keyList.add(Key.V);
        }
        if (Key.W.isPressed()) {
            keyList.add(Key.W);
        }
        if (Key.X.isPressed()) {
            keyList.add(Key.X);
        }
        if (Key.Y.isPressed()) {
            keyList.add(Key.Y);
        }
        if (Key.Z.isPressed()) {
            keyList.add(Key.Z);
        }
        if (Key.ONE.isPressed()) {
            keyList.add(Key.ONE);
        }
        if (Key.TWO.isPressed()) {
            keyList.add(Key.TWO);
        }
        if (Key.THREE.isPressed()) {
            keyList.add(Key.THREE);
        }
        if (Key.FOUR.isPressed()) {
            keyList.add(Key.FOUR);
        }
        if (Key.FIVE.isPressed()) {
            keyList.add(Key.FIVE);
        }
        if (Key.SIX.isPressed()) {
            keyList.add(Key.SIX);
        }
        if (Key.SEVEN.isPressed()) {
            keyList.add(Key.SEVEN);
        }
        if (Key.EIGHT.isPressed()) {
            keyList.add(Key.EIGHT);
        }
        if (Key.NINE.isPressed()) {
            keyList.add(Key.NINE);
        }

        currentKeys = keyList;
    }

    public static boolean isPressed(Key k) {
        return currentKeys.contains(k);
    }

    public static boolean isClicked(Key k) {
        return currentKeys.contains(k) && !lastKeys.contains(k);
    }

    public static boolean isReleased(Key k) {
        return !currentKeys.equals(k);
    }

}
