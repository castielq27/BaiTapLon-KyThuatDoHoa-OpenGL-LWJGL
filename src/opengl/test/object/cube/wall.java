/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.test.object.cube;

import Utils.Matrix4F;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import opengl.test.object.object;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

/**
 *
 * @author castiel
 */

public class wall extends object {
    
    private float x;
    private float y;
    
    private String path;
    
    private float repeatCount = 0;
    
    public wall(int vao, float x, float y,String path, float reapetNumber){
        this.programID = GL20.glCreateProgram();
        this.path = path;
        this.vao =vao;
        this.x = x;
        this.y = y;
        this.repeatCount = reapetNumber;
        this.vertexShader("opengl/test/object/object.vs");
        this.fragmentShader("opengl/test/object/object.fs");
        this.link();
        this.initVertex();
        this.initUniformValues();
    }
    
    /**
     * Must be override
     */
    @Override
    protected void initVertex(){

        GL30.glBindVertexArray(vao);//bind

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        STBImage.stbi_set_flip_vertically_on_load(1);
        ByteBuffer image = STBImage.stbi_load(this.path, w, h, comp, 0);
       
        int weight = w.get(0);
        int height = h.get(0);
        int compe = comp.get(0);
        //System.out.println(weight+":"+height+":"+this.path);
        float[] data = new float[]{ 
                                     -x, -y,   0f, 1f, 1f, 1f, 0f, 0f,
                                      x, -y,   0f, 1f, 1f, 1f, (2*x)*(this.repeatCount), 0f,
                                     -x,  y,   0f, 1f, 1f, 1f, 0f, (2*y)*(this.repeatCount),
                                      x,  y,   0f, 1f, 1f, 1f, (2*x)*(this.repeatCount), (2*y)*(this.repeatCount),
                                   };
        dataBuffer = BufferUtils.createFloatBuffer(data.length);
        dataBuffer.put(data);
        dataBuffer.flip();
        Logger.getGlobal().log(Level.SEVERE, "FloatBuffer capacity  : " + dataBuffer.capacity() );

        this.vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo); 
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, dataBuffer, GL15.GL_STATIC_DRAW);
        
        this.VertexAttribPointer();

        int[] indices = {  
                                0,1,2,	
				2,1,3,	
                        };
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();
        this.ebo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.ebo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_DYNAMIC_DRAW);
        

        textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        
        GL11.glTexParameteri(textureID, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(textureID, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

         
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, weight, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
         
    

        GL30.glBindVertexArray(0);//unbind
    }
    
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
        
        //---------------------------
        int uniTex = GL20.glGetUniformLocation(this.getProgramID(), "texImage");
        GL20.glUniform1i(uniTex, 0);
        
        this.unbind();
    }
    
    @Override
    public void render(){
        this.bind();// use porgrma --> ket thuc disable program
        
        GL30.glBindVertexArray(this.vao);// bind vao -- > unbind vao
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
        this.VertexAttribPointer();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.ebo);
        
        GL20.glEnableVertexAttribArray(0);// set index ve 0 
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        
        GL11.glDrawElements(GL11.GL_TRIANGLE_STRIP, 6, GL11.GL_UNSIGNED_INT, 0);

        GL20.glDisableVertexAttribArray(0);// disable 
        
        GL30.glBindVertexArray(0);// unbind vao
        
        this.unbind();// dsiable program
    }
    
}
