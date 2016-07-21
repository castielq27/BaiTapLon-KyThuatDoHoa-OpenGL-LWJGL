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
import opengl.test.object.object;
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
public class endgame extends object {

    public static int win = 0;
    public static int lose = 1;
    public static int draw = 2;
    
    public int status = 0;
    
    private int size = 0;
    
    private int textureIDWIN;
    private int textureIDLOSE;
    private int textureIDDRAW;

   
    /**
     * 
     * @param vao
     * @param XO 
     */
    public endgame(int vao ){
        Logger.getGlobal().entering("endgame", "endgame", new Object[]{vao} );

        if ( vao == 0 )
            throw new IllegalArgumentException("init : paramaters is null ");

        this.vao = vao;
        this.programID = GL20.glCreateProgram();
                          
        this.vertexShader("opengl/test/object/object.vs");
        this.fragmentShader("opengl/test/object/object.fs");
        this.link();
        this.initVertex();
        this.initUniformValues();
    }
    
    @Override
    protected void initVertex(){
        GL30.glBindVertexArray(vao);//bind vao  
        
        Object[] dataInput = objLoad.Wavefront(endgame.class.getClassLoader().getResourceAsStream("opengl/test/object/endgame/endgame.obj"), 1.0f, 1.0f, 1.0f);
        super.dataBuffer = (FloatBuffer)dataInput[0];
        this.size = (int)dataInput[1];
        
        this.vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo); 
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, super.dataBuffer, GL15.GL_STATIC_DRAW);
        
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
    @Override
    protected void VertexAttribPointer(){
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
    
    @Override
    protected void initUniformValues(){
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

    @Override
    protected void render() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    

}
    

