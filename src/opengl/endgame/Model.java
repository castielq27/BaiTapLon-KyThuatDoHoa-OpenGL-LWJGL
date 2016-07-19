/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.endgame;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL15;
import static org.lwjgl.opengl.GL15.*;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author l
 */
public class Model {

    private int draw_count;
    private int v_id;

    private int t_id;
    
    private int i_id;
    
    private int vao;

    public Model(float[] vertices, float[] tex_coords, int[] indices, int vao) {
        draw_count = indices.length;
        this.vao = vao;
        
        GL30.glBindVertexArray(this.vao);//bind vao  
        
        
        v_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

        t_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, t_id);
        glBufferData(GL_ARRAY_BUFFER, createBuffer(tex_coords), GL_STATIC_DRAW);

        i_id = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
        
        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices);
        buffer.flip();
        
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        GL30.glBindVertexArray(0);//unbind vao
    }

    public void render() {
        
        GL30.glBindVertexArray(this.vao);// bind vao -- > unbind vao
        
        GL20.glEnableVertexAttribArray(0);// set index ve 0 

        glBindBuffer(GL_ARRAY_BUFFER, v_id);
//       glVertexPointer(3, GL_FLOAT, 0, 0); // vẽ điểm 3: số tọa độ xác định 1 đỉnh, Float: kiểu dữ liệu, hàm định vị điểm [chỉ ra địa điểm và cách tổ chức của mảng điểm]
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, t_id);
//        glTexCoordPointer(2, GL_FLOAT, 0, 0); // mỗi đỉnh là 2 phần tử thuộc mảng, [ hàm định vị hệ tọa độ dệt ảnh ]
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
        
        glDrawElements(GL_TRIANGLES, draw_count, GL_UNSIGNED_INT, 0); // thực hiện chuỗi hành động vẽ [draw_count: số điểm muốn vẽ]
        
        GL20.glDisableVertexAttribArray(0);// disable
        
        GL30.glBindVertexArray(0);// unbind vao
    }

    public void deleteModel(){
        
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(this.v_id);
        GL15.glDeleteBuffers(this.t_id);
        
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(this.i_id);

    }
    
    
    private FloatBuffer createBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
