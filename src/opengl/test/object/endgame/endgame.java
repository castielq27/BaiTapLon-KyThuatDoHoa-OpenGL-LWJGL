/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.test.object.endgame;

import Utils.objLoad;
import Utils.Matrix4F;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import Utils.objLoad;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import opengl.test.Caro;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

/**
 *
 * @author castiel
 */
public class endgame {

    public static int win = 0;
    public static int lose = 1;
    public static int draw = 2;
    
    public int status = 0;
    

    private int programID;
    private int vertexID;
    private int fragmentID;
    
    private int vao;
    private int vbo;
    
    private FloatBuffer VertexColorTexCoordBuffer;
    private int size = 0;
    
    private int modelID;
    private int viewID;
    private int projectionID;
    
    private int textureIDWIN;
    private int textureIDLOSE;
    private int textureIDDRAW;

   
    /**
     * 
     * @param vao
     * @param XO 
     */
    public endgame(int vao ){
        Logger.getGlobal().entering("tugiac", "tugiac", new Object[]{vao} );

        if ( vao == 0 )
            throw new IllegalArgumentException("init : paramaters is null ");

        
        this.vao = vao;

        
        this.programID = GL20.glCreateProgram();
                          
        this.vertexShader("opengl/test/object/endgame/endgame.vs");
        this.fragmentShader("opengl/test/object/endgame/endgame.fs");
        this.link();
        this.initVertex();
        this.initUniformValues();
    }
    
    
    private void vertexShader(String file){
        this.vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(this.vertexID, endgame.sourceLoader(file));
        GL20.glCompileShader(this.vertexID);
        if ( GL20.glGetShaderi(this.vertexID, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE ){
            throw new RuntimeException("Khong the compile vertexShader");
        }
        GL20.glAttachShader(this.programID, this.vertexID);
    }

    private void fragmentShader(String file){
        this.fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(this.fragmentID, endgame.sourceLoader(file));
        GL20.glCompileShader(this.fragmentID);
        if ( GL20.glGetShaderi(this.fragmentID, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE ){
            throw new RuntimeException("Khong the compile fragmentShader");
        }
        GL20.glAttachShader(this.programID, this.fragmentID);        
        
    }
    private static String sourceLoader(String file){

        Scanner in = new Scanner( endgame.class.getClassLoader().getResourceAsStream(file) );
        /*Scanner in = null;
        try {
            in = new Scanner( new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(endgame.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        StringBuilder source = new StringBuilder("");
        while( in.hasNextLine() ){
            source.append(in.nextLine() + "\n");
        }
        return source.toString();
    }
    
    private void link(){
        GL20.glLinkProgram(programID);
        if ( GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) != GL11.GL_TRUE ){
            throw new RuntimeException("Khong the link program");
        }
    }
    
    public void bind(){
        GL20.glUseProgram(programID);
    }
    
    public void unbind(){
        GL20.glUseProgram(0);
    }
    
    public void deleteProgram(){
        // disable program
        this.unbind();
        
        // detach shader
        GL20.glDetachShader(this.programID, this.vertexID);
        GL20.glDetachShader(this.programID, this.fragmentID);
        // delete shader
        GL20.glDeleteShader(this.vertexID);
        GL20.glDeleteShader(this.fragmentID);
        GL20.glDeleteProgram(this.programID);
        
        
        // delete vao
        GL30.glBindVertexArray(0);
        // delete vbo
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(this.vbo);
       
        
    }
    public int getProgramID(){
        return this.programID;
    }
    
    private void initVertex(){
        GL30.glBindVertexArray(vao);//bind vao  
        
        Object[] dataInput = objLoad.Wavefront(endgame.class.getClassLoader().getResourceAsStream("opengl/test/object/endgame/endgame.obj"), 1.0f, 1.0f, 1.0f);
        this.VertexColorTexCoordBuffer = (FloatBuffer)dataInput[0];
        this.size = (int)dataInput[1];
        
        this.vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo); 
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.VertexColorTexCoordBuffer, GL15.GL_STATIC_DRAW);
        
        this.VertexAttribPointer();


        
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        STBImage.stbi_set_flip_vertically_on_load(1);
        ByteBuffer image = STBImage.stbi_load("resource/win.png", w, h, comp, 0);

        textureIDWIN = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIDWIN);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w.get(0), h.get(0), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
        
        image = STBImage.stbi_load("resource/lose.png", w, h, comp, 0);
        
        textureIDLOSE = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIDLOSE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w.get(0), h.get(0), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
        
        image = STBImage.stbi_load("resource/draw.png", w, h, comp, 0);
        
        textureIDDRAW = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIDDRAW);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w.get(0), h.get(0), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
        
        GL30.glBindVertexArray(0);//unbind vao
    }
  
    
    /**
     * Method nay duoc tach rieng ra tu initVertex 
     * Vi render nhieu doi tuong neu dung Multi vbo --> truoc khi draw phai bind lai vbo --> moi lan bind lai se phai set lai VertexAttribPointer
     */
    private void VertexAttribPointer(){
        if ( this.vbo == 0 )
            throw new RuntimeException("Chua khoi tao VBO");
        
        int positionID = GL20.glGetAttribLocation(programID, "position");
        GL20.glEnableVertexAttribArray(positionID);
        GL20.glVertexAttribPointer(positionID, 3, GL11.GL_FLOAT, false, 8*4, 0);// float size = 4 byte --> next is 3*4 byte
                                            // size = 3 --> position = x,y,z vec3

        int colorID = GL20.glGetAttribLocation(programID, "color");
        GL20.glEnableVertexAttribArray(colorID);
        GL20.glVertexAttribPointer(colorID, 3, GL11.GL_FLOAT, false, 8*4, 3*4);
                                             // size = 3 --> vec3

        int texcoordID = GL20.glGetAttribLocation(programID, "texcoord");
        GL20.glEnableVertexAttribArray(texcoordID);
        GL20.glVertexAttribPointer(texcoordID, 2, GL11.GL_FLOAT, false, 8*4, 6*4);
                                            // size = 2 --> vec2

    }
    
    private void initUniformValues(){
        this.bind();
      
        modelID = GL20.glGetUniformLocation(this.getProgramID(), "model");
        Matrix4F m = new Matrix4F();
        GL20.glUniformMatrix4fv(modelID, false, m.toFloatBuffer());            
    
        viewID = GL20.glGetUniformLocation(this.getProgramID(), "view");
        Matrix4F v = new Matrix4F();
        GL20.glUniformMatrix4fv(viewID, false, v.toFloatBuffer());
        
        projectionID = GL20.glGetUniformLocation(this.getProgramID(), "projection");
        Matrix4F p = new Matrix4F();
        
        GL20.glUniformMatrix4fv(projectionID, false, p.toFloatBuffer());      
        

        this.unbind();
    }
    
    public void setModelMatrix(Matrix4F m){
        this.bind();
        GL20.glUniformMatrix4fv(modelID, false, m.toFloatBuffer());
        this.unbind();
    }
    
    public void setViewMatrix(Matrix4F v){
        this.bind();
        GL20.glUniformMatrix4fv(viewID, false, v.toFloatBuffer());
        this.unbind();
    }
    public void setProjectionMatrix(Matrix4F v){
        this.bind();
        GL20.glUniformMatrix4fv(this.projectionID, false, v.toFloatBuffer());
        this.unbind();
    }
    public void render(int status){
        
        this.bind();// use porgrma --> ket thuc disable program
        
        GL30.glBindVertexArray(this.vao);// bind vao -- > unbind vao
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
        this.VertexAttribPointer();

        
        GL20.glEnableVertexAttribArray(0);// set index ve 0 
        if ( status == endgame.win )
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIDWIN);
        else if ( status == endgame.lose )
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIDLOSE);
        else 
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIDDRAW);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, this.size);
        //GL11.glDrawElements(GL11.GL_TRIANGLES, this.indices.capacity(), GL11.GL_UNSIGNED_INT, 0);// capacity --> luon luon chua max
        //GL11.
        GL20.glDisableVertexAttribArray(0);// disable 
        
        GL30.glBindVertexArray(0);// unbind vao
        
        this.unbind();// dsiable program
    }

    public int getModelID() {
        return modelID;
    }

    public int getViewID() {
        return viewID;
    }

    public int getProjectionID() {
        return projectionID;
    }
    
    
    
    

}
    

