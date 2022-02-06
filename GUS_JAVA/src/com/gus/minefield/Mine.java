package com.gus.minefield;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;
/**
 * A JavaBean representing a single Mine located at coordinates {@link #x}, {@link #y} 
 * and with a blast radius of {@link #r}. 
 * @author guybellingham
 *
 */
public class Mine implements Comparable<Mine>, Cloneable {
    /**
     * A Mine has an X (horizontal) coordinate within the minefield.
     */
    private int x;
    /**
     * A Mine has an Y (vertical) coordinate within the minefield.
     */
    private int y;
    /**
     * A Mine has a blast radius within the minefield.
     */
    private int radius;
    /**
     * <code>true</code> if this Mine is already exploded on the MineField.
     */
    private boolean exploded = false;
    /**
     * Constructs an empty Mine (no args for JavaBean compliance).
     */
    public Mine() {
        
    }
    /**
     * For unit testing this is handy.
     * @param x
     * @param y
     * @param r
     */
    public Mine(int x, int y, int r) {
        setX(x);
        setY(y);
        setRadius(r);
    }
    /**
     * Constructs a Mine from the given text string, which should contain 
     * 3 integers for the x, y, and r properties in that order.
     * @param text
     * @throws RuntimeException if the integers are missing or non numeric.
     */
    public Mine(String text) {
        super();
        //Tokenizer is more efficient than using string.split(regex)
        //Default tokenizer uses white space characters as the delimiter
        StringTokenizer tokenizer = new StringTokenizer(text);
        try {
            this.x = Integer.parseInt( tokenizer.nextToken() );
        } catch (NoSuchElementException | NumberFormatException e) {
            throw new RuntimeException(String.format("Missing or invalid x coordinate for mine in text line \"%s\"", text));
        }
        try {
            this.y = Integer.parseInt( tokenizer.nextToken() );
        } catch (NoSuchElementException | NumberFormatException e) {
            throw new RuntimeException(String.format("Missing or invalid y coordinate for mine in text line \"%s\"", text));
        }
        try {
            this.radius = Integer.parseInt( tokenizer.nextToken() );
        } catch (NoSuchElementException | NumberFormatException e) {
            throw new RuntimeException(String.format("Missing or invalid radius for mine in text line \"%s\"", text));
        }
    }

    protected int getX() {
        return x;
    }

    protected void setX(int x) {
        this.x = x;
    }

    protected int getY() {
        return y;
    }

    protected void setY(int y) {
        this.y = y;
    }

    protected int getRadius() {
        return radius;
    }

    protected void setRadius(int r) {
        this.radius = r;
    }

    @Override
    public String toString() {
        return "Mine[x=" + x + ", y=" + y + ", r=" + radius + (isExploded() ? " boom!" : "") + "]";
    }
    /**
     * Mines are naturally ordered by blast radius.
     */
    @Override
    public int compareTo(Mine other) {
        return getRadius() - other.getRadius();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + radius;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }
    /**
     * 2 Mines are equal if they exist at the same coordinates and have the same blast radius.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Mine other = (Mine) obj;
        if (radius != other.radius)
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }
    protected boolean isExploded() {
        return exploded;
    }
    protected boolean isUnexploded() {
        return ! isExploded();
    }
    protected void setExploded(boolean exploded) {
        this.exploded = exploded;
    }
    
    @Override
    protected Object clone() {
        Mine clone = null;
        try  {
            clone = (Mine) super.clone();
            clone.setX(getX());
            clone.setY(getY());
            clone.setRadius(getRadius());
            clone.setExploded(isExploded());
        } 
        catch (CloneNotSupportedException e) 
        {
            throw new RuntimeException(e);
        }
        return clone;
    }
}
