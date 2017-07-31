package helpers;

public class Timer {

    private long lastTime, currentTime;
    private double fps;

    public Timer(double fps) {
        this.lastTime = System.currentTimeMillis();
        this.currentTime = System.currentTimeMillis();
        this.fps = fps;
    }

    /**
     * Gets Delta time (ms) since last update
     *
     * @return currentTime - lastTime
     */
    public long deltaMillis() {
        this.currentTime = System.currentTimeMillis();
        return this.currentTime - this.lastTime;
    }

    /**
     * Returns true if a value could be updated according to this.fps
     *
     * @return true if a value could be updated according to this.fps
     */
    public boolean canUpdate() {
        boolean ret = this.deltaMillis() >= (1000 / this.fps);
        if (ret) {
            this.lastTime = this.currentTime;
        }
        return ret;
    }
}