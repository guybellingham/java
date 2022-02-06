package com.gus.minefield;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MineFieldTest {
    List<Mine> mines = new ArrayList<>();
    
    @Before
    public void setUp() throws Exception {
        Mine mine1 = new Mine(1, 1, 5);         //causes mine2 to explode
        Mine mine2 = new Mine(4, 4, 4);         //causes 0 other explosions
        Mine mine3 = new Mine(-3, -4, 15);      //causes both other mines to explode
        mines.add(mine1);
        mines.add(mine2);
        mines.add(mine3);
    }

    @Test
    public void testGetExplosionCountMine1() {
        System.out.println("testGetExplosionCountMine1 mines="+mines);
        Mine mine1 = mines.get(0);
        int result = MineFieldWalker.getExplosionCount(mine1, mines);
        System.out.println(mine1+" causes "+result+" explosions");
        assertTrue(result == 2);
    }
    @Test
    public void testGetExplosionCountMine2() {
        System.out.println("testGetExplosionCountMine2 mines="+mines);
        Mine mine2 = mines.get(1);
        int result = MineFieldWalker.getExplosionCount(mine2, mines);
        System.out.println(mine2+" causes "+result+" explosions");
        assertTrue(result == 1);
    }
    @Test
    public void testGetExplosionCountMine3() {
        System.out.println("testGetExplosionCountMine3 mines="+mines);
        Mine mine3 = mines.get(2);
        int result = MineFieldWalker.getExplosionCount(mine3, mines);
        System.out.println(mine3+" causes "+result+" explosions");
        assertTrue(result == 3);
    }

    @Test
    public void testWillExplode() {
        Mine mine1 = new Mine("1 1 5");   //are 4.24 ft away
        Mine mine2 = new Mine("4 4 4");
        System.out.println(mine1+" and "+mine2+" are "+MineFieldWalker.getDistanceBetween(mine1, mine2)+" ft away");
        // If mine1 explodes it also causes mine2 to explode
        boolean result = MineFieldWalker.willExplode(mine1, mine2);
        assertTrue(result);
        // However if mine2 explodes it will not set off mine1 
        result = MineFieldWalker.willExplode(mine2, mine1);
        assertFalse(result);
        
    }
    @Test
    public void testGetDistanceBetween() {
        Mine mine1 = new Mine("5 0 6");  
        Mine mine2 = new Mine("3 4 5");
        System.out.println(mine1+" and "+mine2+" are "+MineFieldWalker.getDistanceBetween(mine1, mine2)+" ft away");
    }
    @Test
    public void testGetChainReactions() {
        List<String> minesText = new ArrayList<>();
        minesText.add("0 0 5");
        minesText.add("3 4 5");
        minesText.add("3 -4 5");
        minesText.add("5 0 6");
        minesText.add("11 0 5");
        MineField mineField = new MineField(minesText);
        System.out.println("mineField="+mineField);
        Map<Integer, List<Mine>> reactions = mineField.getChainReactions();
        reactions.entrySet()
            .stream()        
            .forEachOrdered(entry -> { System.out.println("Map.Entry. key:"+entry.getKey()+",value:"+entry.getValue()); });
    }
}
