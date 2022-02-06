package com.gus.minefield;

import java.util.List;
import java.util.ListIterator;
/**
 * Stateless (static) helper class that 'knows' how to 'walk' a {@link #MineField}.
 * @author guybellingham
 *
 */
public class MineFieldWalker {

    /**
     * Compare the given mine to all the unexploded mines to 
     * see if it causes any chain reaction. If other mines will explode it 
     * recursively gets the explosion counts for those mines too!
     * <li>Note: If 2 exploding mines can cause the same unexploded mine to explode we have to 
     * be careful not to count that mine twice!  Removing exploded mines from the List 
     * with the ListIterator causes ConcurrentModificationException ... 
     * which is why each Mine now has an <code>exploded</code> property.
     * @param mine - starting mine
     * @param mines - all mines
     * @return the number of mines that will explode (including the starting mine)
     */
    static int getExplosionCount(Mine mine, List<Mine> mines) {
        int explosions = 0;
        // get all mines (including myself) and see if they will explode 
        ListIterator<Mine> itor = mines.listIterator();
        while (itor.hasNext()) {
            Mine nextMine = itor.next();
            if (nextMine.isUnexploded() && willExplode(mine, nextMine)) {
                // explode this mine and count the mine
                nextMine.setExploded(true);
                //System.out.println("mine "+mine+" --> "+nextMine);
                explosions++;
                explosions += getExplosionCount(nextMine, mines);  //recurse
            }
            
        }
        return explosions;
    }

    /**
     * IF the blast radius of mine is >= the distance between the 
     * two mines then the next mine will also explode - and we return true;
     * @param mine - the mine exploding
     * @param next - a nearby unexploded mine
     * @return true if the next mine will also explode
     */
    static boolean willExplode(Mine mine, Mine next) {        
        return mine.getRadius() >= getDistanceBetween(mine, next);
    }

    static double getDistanceBetween(Mine mine, Mine next) {
        // For an equilateral triangle
        // Pythagoras says the square on the hypotenuse = the sum of the squares of the other 2 sides
        return Math.sqrt(
                Math.pow((next.getX()-mine.getX()),2) + 
                Math.pow((next.getY()-mine.getY()),2)
                );
    }
}
