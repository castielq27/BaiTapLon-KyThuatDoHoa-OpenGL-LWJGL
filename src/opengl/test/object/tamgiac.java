/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.test.object;

import Utils.Matrix4F;
import java.nio.FloatBuffer;
import java.util.Scanner;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author castiel
 */
public class tamgiac {
    private int programID;
    private int vertexID;
    private int fragmentID;
    
    private int vao;
    private int vboPosition;
    private int vboColor;
    
    private int modelID;
    private int viewID;
    private int projectionID;
    
    public tamgiac(){
        this.programID = GL20.glCreateProgram();
        
        this.vertexShader("opengl/test/tamgiac/tamgiac.vs");
        this.fragmentShader("opengl/test/tamgiac/tamgiac.fs");
        this.link();
        this.initVertex();
        this.initUniformValues();
    }
    
    private void vertexShader(String file){
        this.vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(this.vertexID, tamgiac.sourceLoader(file));
        GL20.glCompileShader(this.vertexID);
        if ( GL20.glGetShaderi(this.vertexID, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE ){
            throw new RuntimeException("Khong the compile vertexShader");
        }
        GL20.glAttachShader(this.programID, this.vertexID);
    }

    private void fragmentShader(String file){
        this.fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(this.fragmentID, tamgiac.sourceLoader(file));
        GL20.glCompileShader(this.fragmentID);
        if ( GL20.glGetShaderi(this.fragmentID, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE ){
            throw new RuntimeException("Khong the compile fragmentShader");
        }
        GL20.glAttachShader(this.programID, this.fragmentID);        
        
    }
    private static String sourceLoader(String file){
        Scanner in = new Scanner( tamgiac.class.getClassLoader().getResourceAsStream(file));
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
        GL30.glDeleteVertexArrays(this.vao);
        // delete vbo
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(this.vboPosition);
        GL15.glDeleteBuffers(this.vboColor);
        
    }
    public int getProgramID(){
        return this.programID;
    }
    
    private void initVertex(){
        this.vao = GL30.glGenVertexArrays();// Vertex Array Object link du lieu giua VBO va data
        GL30.glBindVertexArray(vao);//bind
        
        float[] position = { -0.6f, -0.4f, 0f,
                              0.6f, -0.4f, 0f,
                                0f,  0.6f, 0f  };
        float[] color = {   1f, 0f, 0f,
                            0f, 1f, 0f,
                            0f, 0f, 1f  };
        FloatBuffer positionBuffer = BufferUtils.createFloatBuffer(position.length);
        positionBuffer.put(position);
        positionBuffer.flip();
        
        FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(color.length);
        colorBuffer.put(color);
        colorBuffer.flip();
        
        this.vboPosition = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboPosition);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positionBuffer, GL15.GL_DYNAMIC_DRAW);

        int positionID = GL20.glGetAttribLocation(programID, "position");
        GL20.glEnableVertexAttribArray(positionID);
        GL20.glVertexAttribPointer(positionID, 3, GL11.GL_FLOAT, false, 3*4, 0);// float size = 4 byte --> next is 3*4 byte
        
        
        this.vboColor = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboColor);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_DYNAMIC_DRAW);

        int colorID = GL20.glGetAttribLocation(programID, "color");
        GL20.glEnableVertexAttribArray(colorID);
        GL20.glVertexAttribPointer(colorID, 3, GL11.GL_FLOAT, false, 3*4, 0);

        GL30.glBindVertexArray(0);//unbind
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
    
    public void render(){
        this.bind();// use porgrma --> ket thuc disable program
        
        GL30.glBindVertexArray(this.vao);// bind vao -- > unbind vao
        
        GL20.glEnableVertexAttribArray(0);// set index ve 0 
        
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
        
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
