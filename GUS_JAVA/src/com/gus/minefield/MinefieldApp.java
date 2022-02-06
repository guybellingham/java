package com.gus.minefield;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MinefieldApp {

    private static final Logger logger = Logger.getLogger("com.gus.minefield");
    /**
     * Run me!
     * @param args [0] the path/to/the/mines.txt  file
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String minesFilename= "mines.txt"; 
        if (args.length != 1 || args[0].isBlank()) {
            System.out.println(String.format("MinefieldApp got no args, looking for default mines file \"%s\" in current directory...",minesFilename));
        } else {
            minesFilename= args[0];
        }
        
        logger.log(Level.INFO, "MinefieldApp got file name="+minesFilename);
        MineFileReader reader = MineFileReader.getInstance();
        List<String> minesText = reader.readSmallAsciiTextFile(minesFilename);
        logger.log(Level.INFO, "MinefieldApp read "+minesText.size()+" lines from file...");
        MineField mineField = new MineField(minesText);
        logger.log(Level.INFO, "MinefieldApp created a "+mineField);
        
        Map<Integer, List<Mine>> reactions = mineField.getChainReactions();
        Optional<Integer> maxCount = reactions.keySet().stream().max(Comparator.comparingInt(Integer::intValue));
        Integer max = maxCount.get();
        logger.log(Level.INFO, "MinefieldApp Max number of Explosions = "+max);
        List<Mine> mines = reactions.get(max);
        logger.log(Level.INFO, "MinefieldApp Mine/s causing max explosions = "+mines);

    }

}
