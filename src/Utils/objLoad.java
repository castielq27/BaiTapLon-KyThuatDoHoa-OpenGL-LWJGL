package Utils;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author castiel
 */
public class objLoad {
    public static FloatBuffer vertexLoad(InputStream file ) {

            ArrayList<Float> lst = new ArrayList<>();
            Scanner in = new Scanner( file);
            while( in.hasNextLine()){
                String aline = in.nextLine();
                Scanner line = new Scanner( aline );
                if ( aline.indexOf("v") != -1 ){
                    line.next();
                    while(line.hasNextFloat()){
                        lst.add(line.nextFloat());
                    }
                } 
            }
            in.close();
            FloatBuffer fb = BufferUtils.createFloatBuffer(lst.size());
            for ( int i = 0; i<lst.size(); i++ ){
                fb.put(lst.get(i).floatValue());
            }
            
            fb.flip();
            return fb;
    }
    public static FloatBuffer VertexColorLoad(InputStream file ) {
            ArrayList<Float> lst = new ArrayList<>();
            Scanner in = new Scanner( file);
            while( in.hasNextLine()){
                String aline = in.nextLine();
                Logger.getGlobal().log(Level.INFO, aline);
                Scanner line = new Scanner( aline );
                if ( aline.indexOf("vc") != -1 ){
                    line.next();
                    while(line.hasNextFloat()){
                        lst.add(line.nextFloat());
                    }
                } 
            }
            in.close();
            FloatBuffer fb = BufferUtils.createFloatBuffer(lst.size());
            for ( int i = 0; i<lst.size(); i++ ){
                fb.put(lst.get(i).floatValue());
            }
            
            fb.flip();
            return fb;
    }
    public static IntBuffer indicesLoad(InputStream file ) {
            ArrayList<Integer> lst = new ArrayList<>();
            Scanner in = new Scanner( file);
            while( in.hasNextLine()){
                String aline = in.nextLine();
                Scanner line = new Scanner( aline );
                if ( aline.indexOf("f") != -1 ){
                    line.next();
                    while(line.hasNextFloat()){
                        lst.add(line.nextInt()-1);// -1 
                    }
                } 
            }
            in.close();
            IntBuffer ib = BufferUtils.createIntBuffer(lst.size());
            for ( Integer e: lst ){
                ib.put(e);
            }
            ib.flip();
            return ib;
    }
    
    public static FloatBuffer colorLoad(InputStream file ) {
            ArrayList<Float> lst = new ArrayList<>();
            Scanner in = new Scanner( file);
            while( in.hasNextLine()){
                String aline = in.nextLine();
                Scanner line = new Scanner( aline );
                if ( aline.indexOf("v") != -1 ){
                    line.next();
                    while(line.hasNextFloat()){
                        lst.add(line.nextFloat());
                    }
                } 
            }
            in.close();
            FloatBuffer fb = BufferUtils.createFloatBuffer(lst.size());
            for ( int i = 0; i<lst.size(); i++ ){
                fb.put(lst.get(i).floatValue());
            }
            
            fb.flip();
            return fb;
    }
}
