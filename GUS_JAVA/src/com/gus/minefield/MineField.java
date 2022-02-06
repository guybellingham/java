package com.gus.minefield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MineField {
    
    private List<Mine> mines;
    /**
     * Constructs a new MineField using the list of "x y r" values read 
     * from a mines.txt file.
     * @param minesText
     */
    public MineField(List<String> minesText) {
        this.mines = minesText.stream()
            .map(txt -> {
                return new Mine(txt);
            })
            .collect(Collectors.toList());
        //.toArray(new Mine[minesText.size()])
    }
   
    @Override
    public String toString() {
        return "MineField [mines=" + mines + "]";
    }
    
    public List<Mine> cloneMines() {
        List<Mine> cloneList = new ArrayList<>();
        Iterator<Mine> itor = mines.iterator();
        while (itor.hasNext()) {
            cloneList.add((Mine) itor.next().clone());
        }
        return cloneList;
    }
    
    public Map<Integer, List<Mine>> getChainReactions() {
        Map<Integer, List<Mine>> chainReactions = new TreeMap<>();
        //1. For each Mine we need to 'walk' the chain of explosions 
        // to find the one/s with the most in a chain reaction
       ListIterator<Mine> itor = mines.listIterator();
       while (itor.hasNext()) {
           Mine mine = itor.next();
           int explosionCount = MineFieldWalker.getExplosionCount(mine, cloneMines());
           System.out.println("Mine "+mine+" has explosion count="+explosionCount);
           List<Mine> mineList = chainReactions.get(explosionCount);
           if (mineList == null) {
               mineList = new ArrayList<Mine>();
               mineList.add(mine);
               chainReactions.put(explosionCount, mineList);
           } else {
               mineList.add(mine);
           }
       }
       return chainReactions;      
    }
}
