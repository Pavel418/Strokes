import java.awt.*;

abstract class Shape implements Cloneable {
    protected Point start, end;

    public Shape() {}

    public void setPosition(Point start) {
        this.start = start;
    }

    public abstract void draw(Graphics g);

    public void resize(Point end) {
        this.end = end;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    protected int[] calculateValues() {
        int zeroX, x, zeroY, y;

        if (start.x < end.x) {
            zeroX = end.x;
            x = start.x - end.x;
        } else {
            zeroX = start.x;
            x = end.x - start.x;
        }

        if (start.y < end.y) {
            zeroY = end.y;
            y = start.y - end.y;
        } else {
            zeroY = start.y;
            y = end.y - start.y;
        }

        return new int[] {zeroX, x, zeroY, y};
    }
}

class Rectangle extends Shape {
    public Rectangle() {

    }

    @Override
    public void draw(Graphics g) {
        g.fillRect(start.x, start.y, end.x - start.x, end.y - start.y);
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
}

class RightTriangle extends Shape {
    public RightTriangle() {}

    @Override
    public void draw(Graphics g) {
        int[] xPoints = {start.x, start.x, end.x};
        int[] yPoints = {start.y, end.y, end.y};
        g.drawPolygon(xPoints, yPoints, 3);
    }
}

class Triangle extends Shape {
    public Triangle() {}

    @Override
    public void draw(Graphics g) {
        int[] xPoints = {start.x, end.x, start.x + (end.x - start.x) / 2};
        int[] yPoints = {start.y, end.y, start.y};
        g.drawPolygon(xPoints, yPoints, 3);
    }
}

class Line extends Shape {
    public Line() {}

    @Override
    public void draw(Graphics g) {
        g.drawLine(start.x, start.y, end.x, end.y);
    }
}

class Diamond extends Shape {
    public Diamond() {}

    @Override
    public void draw(Graphics g) {
        int[] xPoints = {start.x, start.x + (end.x - start.x) / 2, end.x, start.x + (end.x - start.x) / 2};
        int[] yPoints = {start.y + (end.y - start.y) / 2, end.y, start.y + (end.y - start.y) / 2, start.y};
        g.drawPolygon(xPoints, yPoints, 4);
    }
}

class Pentagon extends Shape {
    public Pentagon() {}

    @Override
    public void draw(Graphics g) {
        int zeroX, x, zeroY, y;
        int[] values = calculateValues();
        zeroX = values[0];
        x = values[1];
        zeroY = values[2];
        y = values[3];

        int[] xPoints = {zeroX + x/6, zeroX + x*5/6, zeroX + x, zeroX + x/2, zeroX};
        int[] yPoints = {zeroY, zeroY, zeroY + y*13/22, zeroY + y, zeroY + y*13/22};
        g.drawPolygon(xPoints, yPoints, 5);
    }
}

class Arrow extends Shape {
    public Arrow() {}

    @Override
    public void draw(Graphics g) {
        int zeroX, x, zeroY, y;
        int[] values = calculateValues();
        zeroX = values[0];
        x = values[1];
        zeroY = values[2];
        y = values[3];

        int[] xPoints = {zeroX, zeroX, zeroX + x/2, zeroX + x/2, zeroX + x, zeroX + x/2, zeroX + x/2};
        int[] yPoints = {zeroY + y/4, zeroY + y*3/4, zeroY + y*3/4, zeroY + y, zeroY + y/2, zeroY, zeroY + y/4};
        g.drawPolygon(xPoints, yPoints, 7);
    }
}