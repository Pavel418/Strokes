import java.awt.*;

/**
 * Abstract base class for all drawable shapes. Each shape has a start point and
 * an end point
 * that define its position and size on the canvas.
 */
abstract class Shape implements Cloneable {
    protected Point start, end;

    /**
     * Default constructor for shape. Initializes a shape object.
     */
    public Shape() {
    }

    /**
     * Sets the starting point of the shape.
     * 
     * @param start The starting point of the shape as a {@link Point}.
     */
    public void setPosition(Point start) {
        this.start = start;
    }

    /**
     * Abstract method to draw the shape on a given graphics context.
     * 
     * @param g The {@link Graphics} context on which the shape will be drawn.
     */
    public abstract void draw(Graphics g);

    /**
     * Sets the ending point of the shape, effectively resizing it based on the
     * start and end points.
     * 
     * @param end The ending point of the shape as a {@link Point}.
     */
    public void resize(Point end) {
        this.end = end;
    }

    /**
     * Clones the current shape.
     * 
     * @return A clone of the current shape.
     * @throws CloneNotSupportedException If the object's class does not support the
     *                                    {@link Cloneable} interface.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Helper method to calculate and return essential geometric values required for
     * mirroring or constructing complex shapes.
     * This method computes horizontal and vertical dimensions based on the start
     * and end points.
     * 
     * @return An array of four integers: [maximum X, width, maximum Y, height].
     */
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

        return new int[] { zeroX, x, zeroY, y };
    }
}

/**
 * Concrete implementation of {@link Shape} for drawing rectangles.
 */
class Rectangle extends Shape {
    public Rectangle() {
    }

    /**
     * Draws a rectangle using the start and end points to determine the top left
     * corner and dimensions.
     * 
     * @param g The graphics context to use for drawing.
     */
    @Override
    public void draw(Graphics g) {
        int startX = Math.min(start.x, end.x);
        int startY = Math.min(start.y, end.y);
        g.drawRect(startX, startY, Math.abs(end.x - start.x), Math.abs(end.y - start.y));
    }
}

/**
 * Concrete implementation of {@link Shape} for drawing ovals.
 */
class Oval extends Shape {
    public Oval() {
    }

    /**
     * Draws an oval within the bounding box defined by the start and end points.
     * 
     * @param g The graphics context to use for drawing.
     */
    @Override
    public void draw(Graphics g) {
        int startX = Math.min(start.x, end.x);
        int startY = Math.min(start.y, end.y);
        g.drawOval(startX, startY, Math.abs(end.x - start.x), Math.abs(end.y - start.y));
    }
}

/**
 * Concrete implementation of {@link Shape} for drawing right triangles.
 */
class RightTriangle extends Shape {
    public RightTriangle() {
    }

    /**
     * Draws a right triangle with a right angle at the end point.
     * 
     * @param g The graphics context to use for drawing.
     */
    @Override
    public void draw(Graphics g) {
        int[] xPoints = { start.x, start.x, end.x };
        int[] yPoints = { start.y, end.y, end.y };
        g.drawPolygon(xPoints, yPoints, 3);
    }
}

/**
 * Concrete implementation of {@link Shape} for drawing equilateral triangles.
 */
class Triangle extends Shape {
    public Triangle() {
    }

    /**
     * Draws an equilateral triangle based on the start and end points.
     * 
     * @param g The graphics context to use for drawing.
     */
    @Override
    public void draw(Graphics g) {
        int[] xPoints = { start.x, end.x, start.x + (end.x - start.x) / 2 };
        int[] yPoints = { start.y, end.y, start.y };
        g.drawPolygon(xPoints, yPoints, 3);
    }
}

/**
 * Concrete implementation of {@link Shape} for drawing straight lines.
 */
class Line extends Shape {
    public Line() {
    }

    /**
     * Draws a line from the start point to the end point.
     * 
     * @param g The graphics context to use for drawing.
     */
    @Override
    public void draw(Graphics g) {
        g.drawLine(start.x, start.y, end.x, end.y);
    }
}

/**
 * Concrete implementation of {@link Shape} for drawing diamonds.
 */
class Diamond extends Shape {
    public Diamond() {
    }

    /**
     * Draws a diamond shape centered at the midpoint between the start and end
     * points.
     * 
     * @param g The graphics context to use for drawing.
     */
    @Override
    public void draw(Graphics g) {
        int[] xPoints = { start.x, start.x + (end.x - start.x) / 2, end.x, start.x + (end.x - start.x) / 2 };
        int[] yPoints = { start.y + (end.y - start.y) / 2, end.y, start.y + (end.y - start.y) / 2, start.y };
        g.drawPolygon(xPoints, yPoints, 4);
    }
}

/**
 * Concrete implementation of {@link Shape} for drawing pentagons.
 */
class Pentagon extends Shape {
    public Pentagon() {
    }

    /**
     * Draws a pentagon using geometric calculations from {@link calculateValues()}
     * to determine the points.
     * 
     * @param g The graphics context to use for drawing.
     */
    @Override
    public void draw(Graphics g) {
        int zeroX, x, zeroY, y;
        int[] values = calculateValues();
        zeroX = values[0];
        x = values[1];
        zeroY = values[2];
        y = values[3];

        int[] xPoints = { zeroX + x / 6, zeroX + x * 5 / 6, zeroX + x, zeroX + x / 2, zeroX };
        int[] yPoints = { zeroY, zeroY, zeroY + y * 13 / 22, zeroY + y, zeroY + y * 13 / 22 };
        g.drawPolygon(xPoints, yPoints, 5);
    }
}

/**
 * Concrete implementation of {@link Shape} for drawing arrows.
 */
class Arrow extends Shape {
    public Arrow() {
    }

    /**
     * Draws an arrow pointing right, using geometric calculations to define the
     * shape based on the start and end points.
     * 
     * @param g The graphics context to use for drawing.
     */
    @Override
    public void draw(Graphics g) {
        int zeroX, x, zeroY, y;
        int[] values = calculateValues();
        zeroX = values[0];
        x = values[1];
        zeroY = values[2];
        y = values[3];

        int[] xPoints = { zeroX, zeroX, zeroX + x / 2, zeroX + x / 2, zeroX + x, zeroX + x / 2, zeroX + x / 2 };
        int[] yPoints = { zeroY + y / 4, zeroY + y * 3 / 4, zeroY + y * 3 / 4, zeroY + y, zeroY + y / 2, zeroY,
                zeroY + y / 4 };
        g.drawPolygon(xPoints, yPoints, 7);
    }
}