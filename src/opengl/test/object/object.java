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
import java.util.logging.Level;
import java.util.logging.Logger;
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
public abstract class object {
    
    protected int programID;
    protected int vertexID;
    protected int fragmentID;
    
    protected int vao;
    protected int vbo;
    protected int ebo;
    
    protected int textureID;
    protected FloatBuffer dataBuffer;
    
    protected int modelID;
    protected int viewID;
    protected int projectionID;
    
    
    public object(){
        
    }
    
    public object(int vao){
        if ( vao == 0 )
            throw new RuntimeException("init : paramaters is null ");
        this.vao = vao;
        
        this.programID = GL20.glCreateProgram();
        this.vertexShader("opengl/test/object/object.vs");
        this.fragmentShader("opengl/test/object/object.fs");
        this.link();
        this.initVertex();
        this.initUniformValues();
    }
    
    protected final void vertexShader(String file){
        this.vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(this.vertexID, object.sourceLoader(file));
        GL20.glCompileShader(this.vertexID);
        if ( GL20.glGetShaderi(this.vertexID, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE ){
            throw new RuntimeException("Khong the compile vertexShader");
        }
        GL20.glAttachShader(this.programID, this.vertexID);
    }

    protected final void fragmentShader(String file){
        this.fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(this.fragmentID, object.sourceLoader(file));
        GL20.glCompileShader(this.fragmentID);
        if ( GL20.glGetShaderi(this.fragmentID, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE ){
            throw new RuntimeException("Khong the compile fragmentShader");
        }
        GL20.glAttachShader(this.programID, this.fragmentID);        
        
    }
    private final static String sourceLoader(String file){
        Scanner in = new Scanner( object.class.getClassLoader().getResourceAsStream(file));
        StringBuilder source = new StringBuilder("");
        while( in.hasNextLine() ){
            source.append(in.nextLine() + "\n");
        }
        return source.toString();
    }
    
    protected final void link(){
        GL20.glLinkProgram(programID);
        if ( GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) != GL11.GL_TRUE ){
            throw new RuntimeException("Khong the link program");
        }
    }
    
    public final void bind(){
        GL20.glUseProgram(programID);
    }
    
    public final void unbind(){
        GL20.glUseProgram(0);
    }
    
    public final void deleteProgram(){
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
        //GL30.glDeleteVertexArrays(this.vao);
        // delete vbo
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(this.vbo);
       
        //delete vbo
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(this.ebo);
        
    }
    public final int getProgramID(){
        return this.programID;
    }
    
    public void setModelMatrix(Matrix4F m){
        this.bind();
        GL20.glUniformMatrix4fv(modelID, false, m.toFloatBuffer());
        this.unbind();
    }
    public final void setViewMatrix(Matrix4F v){
        this.bind();
        GL20.glUniformMatrix4fv(viewID, false, v.toFloatBuffer());
        this.unbind();
    }
    
    public final void setProjectionMatrix(Matrix4F v){
        this.bind();
        GL20.glUniformMatrix4fv(this.projectionID, false, v.toFloatBuffer());
        this.unbind();
    }
    
    public final int getModelID() {
        return modelID;
    }

    public final int getViewID() {
        return viewID;
    }

    public final int getProjectionID() {
        return projectionID;
    }

    
    protected abstract void initVertex();
    
    /**
     * Method nay duoc tach rieng ra tu initVertex 
     * Vi render nhieu doi tuong neu dung Multi vbo --> truoc khi draw phai bind lai vbo --> moi lan bind lai se phai set lai VertexAttribPointer
     */
    protected abstract void VertexAttribPointer();
    // float size = 4 byte --> next is 3*4 byte
    // size = 3 --> position = x,y,z vec3
    // size = 3 --> vec3
    // size = 2 --> vec2

    protected abstract void initUniformValues();
    
    protected abstract void render();

    
}
