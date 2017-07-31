package ui;

import java.util.*;

public class UI {
    public ArrayList<Button> buttonList;
    public ArrayList<Panel> panelList;
    public ArrayList<CheckBox> boxList;

    public UI() {
        this.buttonList = new ArrayList<>();
        this.panelList = new ArrayList<>();
        this.boxList = new ArrayList<>();
    }

    public void draw() {
        for (Button b : this.buttonList) {
            b.draw();
        }
        for (CheckBox c : this.boxList) {
            if (c.isEditable) {
                c.update();
            }
            c.draw();
        }
        for (Panel p : this.panelList) {
            p.draw();
        }
    }

    // BUTTONS

    public void addButton(String name, int x, int y) {
        this.buttonList.add(new Button(name, x, y));
    }

    public boolean isClicked(String name) {
        if (this.getButton(name) != null && this.getButton(name).isClicked()) {
            return true;
        }
        return false;
    }

    public boolean mouseOver() {
        boolean ret = false;
        for (Button b : this.buttonList) {
            if (b.mouseOver()) {
                ret = true;
                break;
            }
        }
        if (ret != true) {
            for (Panel p : this.panelList) {
                for (Button b : p.getButtonList()) {
                    if (b.mouseOver()) {
                        ret = true;
                        break;
                    }
                }
            }
        }
        return ret;
    }

    public Button getButton(String name) {
        for (Button b : this.buttonList) {
            if (b.name.equals(name)) {
                return b;
            }
        }
        for (Panel p : this.panelList) {
            if (p.getButton(name) != null) {
                return p.getButton(name);
            }
        }
        return null;
    }

    // PANELS

    public void addPanel(String name, int x, int y, int width, int height) {
        this.panelList.add(new Panel(name, x, y, width, height));
    }

    public void addPanel(String name, int x, int y, int width, int height,
            int bw, int bh) {
        this.panelList.add(new Panel(name, x, y, width, height, bw, bh));
    }

    public void addPanel(Panel panel) {
        this.panelList.add(panel);
    }

    public Panel getPanel(String name) {
        for (Panel p : this.panelList) {
            if (p.name.equals(name)) {
                return p;
            }
        }
        return null;
    }

    public void addButton(String name, int x, int y, double scale) {
        this.buttonList.add(new Button(name, x, y, scale));
    }

    public void addButton(Button button) {
        this.buttonList.add(button);
    }

    public void addCheckBox(String name, int x, int y) {
        this.boxList.add(new CheckBox(name, x, y));
    }

    public void addCheckBox(String name, int x, int y, boolean canEdit) {
        this.boxList.add(new CheckBox(name, x, y, canEdit));
    }

    public void addCheckBox(CheckBox box) {
        this.boxList.add(box);
    }

    public CheckBox getCheckBox(String name) {
        for (CheckBox c : this.boxList) {
            if (c.name.equals(name)) {
                return c;
            }
        }
        return null;
    }
}
