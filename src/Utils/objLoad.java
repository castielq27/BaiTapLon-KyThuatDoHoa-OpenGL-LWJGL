package Utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;


/*
 * Utility doc object ve tren blender --> Export ra dinh dang wavefront .obj
 * Yeu cau tringle hoa object truoc khi xuat!
 */

/**
 *
 * @author castiel
 */
public class objLoad {
    /**
     * 
     * @param file
     * @return vertext == { v1, v2, v3 
     *                      .. .. ..   } // vec3
     */
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
    /**
     * vc
     * @param file
     * @return vertxt and color { v1, v2, v3, r, g, b
     *                            v4, v5, v6, r, g, b }
     */
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
    /**
     * 
     * @param file
     * @param r
     * @param g
     * @param b
     * @return vertxt and color { v1, v2, v3, r, g, b
     *                            v4, v5, v6, r, g, b }
     */
    public static FloatBuffer VertexColorLoad(InputStream file, float r, float g, float b ) {
            ArrayList<Float> lst = new ArrayList<>();
            Scanner in = new Scanner( file);
            while( in.hasNextLine()){
                String aline = in.nextLine();
                Logger.getGlobal().log(Level.INFO, aline);
                Scanner line = new Scanner( aline );
                if ( aline.indexOf("v") != -1 ){
                    line.next();
                    while(line.hasNextFloat()){
                        lst.add(line.nextFloat());
                    }
                    lst.add(r);
                    lst.add(g);
                    lst.add(b);
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
    /**
     * 
     * @param file
     * @param r
     * @param g
     * @param b
     * @return  { v1, v2, v3, r, g, b, textcoord1, textcoord2 
     *                      ......                              }
     */
    public static FloatBuffer VertexColorTextCoordLoad(InputStream file, float r, float g, float b ) {
            ArrayList<Float> lst = new ArrayList<>();
            Scanner in = new Scanner( file);
            String vertext = "";
            String textCoord = "";
            while( in.hasNextLine() ){
                String aline = in.nextLine();
                if ( aline.indexOf("v") != -1 ){
                    vertext = vertext + "\n" + aline;
                } else if ( aline.indexOf("vt") != -1 ){
                    textCoord = textCoord + "\n" + aline;
                }
            }
            Scanner v = new Scanner( vertext );
            Scanner t = new Scanner( textCoord );
            while( v.hasNextLine() && t.hasNextLine() ){
                String aline = in.nextLine();
                Logger.getGlobal().log(Level.INFO, aline);
                Scanner line = new Scanner( aline );
                if ( aline.indexOf("v") != -1 ){
                    line.next();
                    while(line.hasNextFloat()){
                        lst.add(line.nextFloat());
                    }
                    lst.add(r);
                    lst.add(g);
                    lst.add(b);
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
    /**
     * 
     * @param file
     * @return  textCoord { x, y ,
     *                      x, y ,
     * }
     */
    public static IntBuffer textCoordLoad(InputStream file ) {
            ArrayList<Integer> lst = new ArrayList<>();
            Scanner in = new Scanner( file);
            while( in.hasNextLine()){
                String aline = in.nextLine();
                Scanner line = new Scanner( aline );
                if ( aline.indexOf("vt") != -1 ){
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
    /**
     * 
     * @param file
     * @return  indices f -->
     */
    public static IntBuffer indicesLoad(InputStream file ) {
            ArrayList<Integer> lst = new ArrayList<>();
            Scanner in = new Scanner( file);
            while( in.hasNextLine()){
                String aline = in.nextLine();
                
                if ( aline.indexOf("f") != -1 ){
                    if ( aline.indexOf("/") != -1 ){
                        StringBuilder tmp = new StringBuilder(aline);
                        for ( int i = 0; i<tmp.length(); i++ ){
                            if ( tmp.charAt(i) == '/' ){
                                while(i<tmp.length() && tmp.charAt(i)!=' '){
                                    tmp.deleteCharAt(i);
                                }
                            }
                        }
                        aline = tmp.toString();
                    }
                    
                    Scanner line = new Scanner( aline );
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
    /**
     * 
     * @param file
     * @param r
     * @param g
     * @param b
     * @return  Object[] size = 2 object[0] = FloatBuffer
     *                            object[1] =  size;
     */
    public static Object[] Wavefront(InputStream file , float r, float g, float b) {
            
            ArrayList<Float> vertext = new ArrayList<>();
            ArrayList<Float> textCoord = new ArrayList<>();
            
            ArrayList<Float> dataBuild = new ArrayList<>();
            int count = 0;
            //ArrayList<Integer> indicesBuild = new ArrayList<>();
            
            Scanner in = new Scanner( file);

            String indices = "";
            while( in.hasNextLine() ){
                String aline = in.nextLine();
                if ( aline.length() > 2 ){
                    if ( aline.charAt(0) == 'v' && aline.charAt(1) == ' ' ){
                        Scanner v = new Scanner( aline );
                        v.next();// loai bo string
                        while( v.hasNext()){
                            vertext.add(v.nextFloat());
                        }
                    } else if ( aline.charAt(0) == 'v' && aline.charAt(1) == 't' ){
                        Scanner v = new Scanner( aline );
                        v.next();// loai bo string
                        while( v.hasNext()){
                            textCoord.add(v.nextFloat());
                        }
                    } else if ( aline.charAt(0) == 'f' && aline.charAt(1) == ' '){
                        indices = indices + "\n" + aline;
                    }                    
                }
            }
            in.close();
            Scanner i = new Scanner(indices);
            while(i.hasNext()){
                String aline = i.nextLine();
                //System.out.print(aline+"       ");
                if ( aline.length()>2 && aline.charAt(0) == 'f' && aline.charAt(1) == ' '){
                    String value[] = aline.split(" ");
                    for ( int j = 1; j<4; j++ ){
                        String value1[] = value[j].split("/");
                        
                        count++;
                        
                        int temp1 = Integer.parseInt(value1[0]);
                        int temp2 = Integer.parseInt(value1[1]);
                        //indicesBuild.add(temp1-1);
                        
                        
                        dataBuild.add( vertext.get((temp1-1)*3+0));
                        dataBuild.add( vertext.get((temp1-1)*3+1));
                        dataBuild.add( vertext.get((temp1-1)*3+2));
                        dataBuild.add(r);
                        dataBuild.add(g);
                        dataBuild.add(b);
                        dataBuild.add( textCoord.get((temp2-1)*2+0) );
                        dataBuild.add( textCoord.get((temp2-1)*2+1) );
                    }
                }
                //System.out.print("\n");
            }
            /*for ( int p = 0; p<dataBuild.size(); p=p+8){
                System.out.println( dataBuild.get(p)+"\t"+dataBuild.get(p+1)+"\t"+
                                    dataBuild.get(p+2)+"\t"+dataBuild.get(p+3)+"\t"+
                                    dataBuild.get(p+4)+"\t"+dataBuild.get(p+5)+"\t"+
                                    dataBuild.get(p+6)+"\t"+dataBuild.get(p+7)
                                                    );
            }*/
            FloatBuffer fb = BufferUtils.createFloatBuffer(dataBuild.size());
            for ( float e : dataBuild ){
                fb.put(e);
            }

            fb.flip();
            Object[] array = { fb, count };
            
            return array;
            
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        Object[] out = objLoad.Wavefront(objLoad.class.getClassLoader().getResourceAsStream("opengl/test/object/table/table.obj"),1f,1f,1f);
            
    }
}
