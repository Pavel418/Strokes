import java.awt.*;

abstract class Shape implements Cloneable {
    protected Point start, end;

    public Shape() {}

    public void setPosition(Point start) {
        this.start = start;
    }

    public abstract void draw(Graphics g);

    public abstract void resize(Point end);

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

class Rectangle extends Shape {
    private int width, height;

    public Rectangle() {

    }

    @Override
    public void draw(Graphics g) {
        g.fillRect(start.x, start.y, width, height);
    }

    @Override
    public void resize(Point end) {
        int width = end.x - start.x;
        int height = end.y - start.y;

        this.width = width;
        this.height = height;
    }
}

class Oval extends Shape {
    public Oval() {}

    @Override
    public void draw(Graphics g) {
        int startX = Math.min(start.x, end.x);
        int startY = Math.min(start.y, end.y);
        g.drawOval(startX, startY, Math.abs(end.x - start.x), Math.abs(end.y - start.y));
    }

    @Override
    public void resize(Point end) {
        this.end = end;
    }
}