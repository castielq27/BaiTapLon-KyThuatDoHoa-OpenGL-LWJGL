/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.test.object;

import Utils.Matrix4F;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Scanner;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

/**
 *
 * @author castiel
 */
public class lineCube extends object {

    private int x;
    private int y;
    
    private    float x_calc=0;
    private    float y_calc=0;
    private Matrix4F Model;
    
    
    public lineCube(int vao,int x, int y){
        if ( vao == 0 )
            throw new IllegalArgumentException("vao == 0 ");
        super.vao = vao;
        super.programID = GL20.glCreateProgram();
        
        this.x = x;
        this.y = y;
        
        this.Model = new Matrix4F();
        
        this.vertexShader("opengl/test/object/lineCube.vs");
        this.fragmentShader("opengl/test/object/lineCube.fs");
        this.link();
        this.initVertex();
        this.initUniformValues();
    }
    
    @Override
    protected void initVertex(){
        GL30.glBindVertexArray(vao);//bind
        
        float[] data = new float[]{  -0.5f, -0.5f,    0.25f, 0f, 1f, 0f,
                                      0.5f, -0.5f,    0.25f, 0f, 1f, 0f,
                                      0.5f,  0.5f,    0.25f, 0f, 1f, 0f,  
                                     -0.5f,  0.5f,    0.25f, 0f, 1f, 0f, 
                                     -0.5f, -0.5f,    -0.25f, 0f, 1f, 0f, 
                                      0.5f, -0.5f,    -0.25f, 0f, 1f, 0f,
                                      0.5f,  0.5f,    -0.25f, 0f, 1f, 0f, 
                                     -0.5f,  0.5f,    -0.25f, 0f, 1f, 0f};//, 1f };
        
        FloatBuffer dataBuffer = BufferUtils.createFloatBuffer(data.length);
        dataBuffer.put(data);
        dataBuffer.flip();
 
        this.vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, dataBuffer, GL15.GL_DYNAMIC_DRAW);

        this.VertexAttribPointer();
        
    
        int[] indices = {   0, 1, 2,
                            3, 0, 4,
                            5, 6, 7,
                            4, 5, 1,
                            2, 6, 7,
                            3  };
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();
        this.ebo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.ebo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_DYNAMIC_DRAW);
        
    
        GL30.glBindVertexArray(0);//unbind
    }
    
    @Override
    protected void VertexAttribPointer(){
        if ( this.vbo == 0 )
            throw new RuntimeException("Chua khoi tao VBO");
        
        int positionID = GL20.glGetAttribLocation(programID, "position");
        GL20.glEnableVertexAttribArray(positionID);
        GL20.glVertexAttribPointer(positionID, 3, GL11.GL_FLOAT, false, 6*4, 0);// float size = 4 byte --> next is 3*4 byte
        
        int colorID = GL20.glGetAttribLocation(programID, "color");
        GL20.glEnableVertexAttribArray(colorID);
        GL20.glVertexAttribPointer(colorID, 3, GL11.GL_FLOAT, false, 6*4, 3*4);
    }
    
    public void update(int x, int y){
        this.bind();

        x_calc=0;
        y_calc=0;
        if ( x == 0 )
            y_calc = 0.3f;
        if ( x == 1 )
            y_calc = 0;
        if ( x == 2 )
            y_calc = -0.3f;
        if ( y == 0 )
            x_calc = -0.3f;
        if ( y == 1 )
            x_calc = 0f;
        if ( y == 2 )
            x_calc = 0.3f;
        
        Matrix4F m = Matrix4F.ratio(0.3f, 0.3f, 0.3f).nhanMaTran(Matrix4F.move(x_calc, y_calc, 0f)).nhanMaTran(Model);
        GL20.glUniformMatrix4fv(modelID, false, m.toFloatBuffer());
        
          

        
        this.unbind();
    }
    
    @Override
    protected void initUniformValues(){
        this.bind();

        modelID = GL20.glGetUniformLocation(this.getProgramID(), "model");
        x_calc=0;
        y_calc=0;
        if ( x == 0 )
            y_calc = 0.3f;
        if ( x == 1 )
            y_calc = 0;
        if ( x == 2 )
            y_calc = -0.3f;
        if ( y == 0 )
            x_calc = -0.3f;
        if ( y == 1 )
            x_calc = 0f;
        if ( y == 2 )
            x_calc = 0.3f;
                    
        Matrix4F m = Matrix4F.ratio(0.3f, 0.3f, 0.3f).nhanMaTran(Matrix4F.move(x_calc, y_calc, 0f)).nhanMaTran(Model);
        
        GL20.glUniformMatrix4fv(modelID, false, m.toFloatBuffer());
        
        viewID = GL20.glGetUniformLocation(this.getProgramID(), "view");
        Matrix4F v = new Matrix4F();
        GL20.glUniformMatrix4fv(viewID, false, v.toFloatBuffer());
        
        projectionID = GL20.glGetUniformLocation(this.getProgramID(), "projection");
        Matrix4F p = new Matrix4F();
        GL20.glUniformMatrix4fv(projectionID, false, p.toFloatBuffer());      
        
          
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
        
       
        GL11.glDrawElements(GL11.GL_LINE_STRIP, 16, GL11.GL_UNSIGNED_INT, 0);

        GL20.glDisableVertexAttribArray(0);// disable 
        
        GL30.glBindVertexArray(0);// unbind vao
        
        this.unbind();// dsiable program
    }
    
    public void setModelMatrix(Matrix4F m){
        this.Model = m.clone();
    }
}
