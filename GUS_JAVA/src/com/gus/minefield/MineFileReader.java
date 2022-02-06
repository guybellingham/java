package com.gus.minefield;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * A File Reader Service that is thread safe ... and could therefore be a singleton bean. 
 * @author guybellingham
 *
 */
public class MineFileReader {

    private static final Logger logger = Logger.getLogger("com.gus.minefield");
    
    private static final MineFileReader instance = new MineFileReader();
    /**
     * Only I can instantiate yself.
     */
    private MineFileReader() {
        
    }
    /**
     * Get the instance of the MineFileReader.
     * @return the singleton
     */
    public static MineFileReader getInstance() {
        return instance;
    }
    
    /**
     * Reads the contents of the given filename using US ASCII character encoding. 
     * @param filename
     * @return a List containing the lines read from the text file.
     * @throws IOException
     */
    public List<String>  readSmallAsciiTextFile(String filename) throws IOException {
        Charset charset = Charset.forName("US-ASCII");
        return readTextFile(filename, charset);
    }
    
    public List<String>  readTextFile(String filename, Charset charset) throws IOException {
        Path path = Paths.get(filename);
        return readTextFile(path, charset);
    }
    
    public List<String>  readTextFile(Path path, Charset charset) throws IOException {
        List<String> output = new ArrayList<String>(); 
        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "FAILED  to read Mines TextFile "+path.toAbsolutePath(),e);
            throw e;
        } 
        return output;
    }
}
