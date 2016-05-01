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
public class cube {
    private int programID;
    private int vertexID;
    private int fragmentID;
    
    private int vao;
    private int vbo;
    private int ebo;
    
    private int textureID;
    
    private int modelID;
    private int viewID;
    private int projectionID;
    
    public cube(){
        this.programID = GL20.glCreateProgram();
        
        this.vertexShader("opengl/test/object/cube.vs");
        this.fragmentShader("opengl/test/object/cube.fs");
        this.link();
        this.initVertex();
        this.initUniformValues();
    }
    
    private void vertexShader(String file){
        this.vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(this.vertexID, cube.sourceLoader(file));
        GL20.glCompileShader(this.vertexID);
        if ( GL20.glGetShaderi(this.vertexID, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE ){
            throw new RuntimeException("Khong the compile vertexShader");
        }
        GL20.glAttachShader(this.programID, this.vertexID);
    }

    private void fragmentShader(String file){
        this.fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(this.fragmentID, cube.sourceLoader(file));
        GL20.glCompileShader(this.fragmentID);
        if ( GL20.glGetShaderi(this.fragmentID, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE ){
            throw new RuntimeException("Khong the compile fragmentShader");
        }
        GL20.glAttachShader(this.programID, this.fragmentID);        
        
    }
    private static String sourceLoader(String file){
        Scanner in = new Scanner( cube.class.getClassLoader().getResourceAsStream(file));
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
        GL15.glDeleteBuffers(this.vbo);
        GL15.glDeleteBuffers(this.ebo);
        
    }
    public int getProgramID(){
        return this.programID;
    }
    
    private void initVertex(){
        this.vao = GL30.glGenVertexArrays();// Vertex Array Object link du lieu giua VBO va data
        GL30.glBindVertexArray(vao);//bind
        
        float[] data = new float[]{  -0.5f, -0.5f,   0f, 1f, 1f, 1f, 0f, 0f,// 0f,
                                      0.5f, -0.5f,   0f, 1f, 1f, 1f, 1f, 0f,// 0f,
                                     -0.5f,  0.5f,   0f, 1f, 1f, 1f, 0f, 1f,// 0f,
                                      0.5f,  0.5f,   0f, 1f, 1f, 1f, 1f, 1f,// 0f,  
                                     -0.5f, -0.5f, 1.0f, 1f, 0f, 1f, 0f, 0f,// 1f,
                                      0.5f, -0.5f, 1.0f, 1f, 0f, 1f, 1f, 0f,// 1f,
                                     -0.5f,  0.5f, 1.0f, 1f, 0f, 1f, 0f, 1f,// 1f,
                                      0.5f,  0.5f, 1.0f, 1f, 0f, 1f, 1f, 1f };//, 1f };
        
        FloatBuffer dataBuffer = BufferUtils.createFloatBuffer(data.length);
        dataBuffer.put(data);
        dataBuffer.flip();
 
        this.vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, dataBuffer, GL15.GL_DYNAMIC_DRAW);

        int positionID = GL20.glGetAttribLocation(programID, "position");
        GL20.glEnableVertexAttribArray(positionID);
        GL20.glVertexAttribPointer(positionID, 3, GL11.GL_FLOAT, false, 8*4, 0);// float size = 4 byte --> next is 3*4 byte
        
        int colorID = GL20.glGetAttribLocation(programID, "color");
        GL20.glEnableVertexAttribArray(colorID);
        GL20.glVertexAttribPointer(colorID, 3, GL11.GL_FLOAT, false, 8*4, 3*4);
        
        /*int texcoordID = GL20.glGetAttribLocation(programID, "texcoord");
        GL20.glEnableVertexAttribArray(texcoordID);
        GL20.glVertexAttribPointer(texcoordID, 2, GL11.GL_FLOAT, false, 8*4, 6*4);*/

        int[] indices = {   0, 1, 2,
                            2, 1, 3,
                            0, 1, 4,
                            4, 1, 5,
                            1, 5, 3,
                            3, 5, 7,
                            3, 7, 2,
                            2, 7, 6,
                            2, 6, 0,
                            0, 6, 4,
                            4, 5, 6,
                            6, 5, 7  };
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();
        this.ebo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.ebo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_DYNAMIC_DRAW);
        

       /* IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);
        STBImage.stbi_set_flip_vertically_on_load(1);
        ByteBuffer image = STBImage.stbi_load("resource/ptit.png", w, h, comp, 0);
        int weight = w.get(0);
        int height = h.get(0);
        int compe = comp.get(0);
        System.out.println(weight+"::"+height+"::"+compe+":::"+(image==null));

        textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
      

        GL11.glTexImage2D(GL12.GL_TEXTURE_3D, 0, GL11.GL_RGBA, weight, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);*/
         
    

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
        
        //---------------------------
        int uniTex = GL20.glGetUniformLocation(this.getProgramID(), "texImage");
        GL20.glUniform1i(uniTex, 0);
        
        this.unbind();
    }
    
    public void render(){
        this.bind();// use porgrma --> ket thuc disable program
        
        GL30.glBindVertexArray(this.vao);// bind vao -- > unbind vao
        
        GL20.glEnableVertexAttribArray(0);// set index ve 0 
        
        //GL11.glBindTexture(GL12.GL_TEXTURE_3D, textureID);
        
        GL11.glDrawElements(GL11.GL_TRIANGLE_STRIP, 6, GL11.GL_UNSIGNED_INT, 0);
        GL11.glDrawElements(GL11.GL_TRIANGLE_STRIP, 6, GL11.GL_UNSIGNED_INT, 6*5*4);


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
