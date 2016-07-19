/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

/**
 *
 * @author castiel
 */
public class Matrix4F {

   
    private float[][] matrix = new float[4][4];
    
    public Matrix4F(){
        for ( int i = 0; i<4; i++ )
            for ( int j = 0; j<4; j++ )
                if ( i == j )
                    matrix[i][j] = 1;
                else
                    matrix[i][j] = 0;
    }
    
    public static Matrix4F rotateOX(float d){
        Matrix4F m = new Matrix4F();
        double rad = Math.toRadians(d);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        m.matrix[1][1] = cos;
        m.matrix[1][2] = sin;
        m.matrix[2][1] = -sin;
        m.matrix[2][2] = cos;
        return m;
    }

    
    public static Matrix4F rotateOY(float d){
        Matrix4F m = new Matrix4F();
        double rad = Math.toRadians(d);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        m.matrix[0][0] = cos;
        m.matrix[0][2] = -sin;
        m.matrix[2][0] = sin;
        m.matrix[2][2] = cos;
        return m;
    }
    
    
    public static Matrix4F rotateOZ(float d){
        Matrix4F m = new Matrix4F();
        double rad = Math.toRadians(d);
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);
        m.matrix[0][0] = cos;
        m.matrix[0][1] = sin;
        m.matrix[1][0] = -sin;
        m.matrix[1][1] = cos;
        return m;
    }
    
    public static Matrix4F move(float x, float y, float z) {
        Matrix4F m = new Matrix4F();
        m.matrix[3][0] = x;
        m.matrix[3][1] = y;
        m.matrix[3][2] = z;
        return m;
    }
    
    public static Matrix4F ratio(float x, float y, float z){
        Matrix4F m = new Matrix4F();
        m.matrix[0][0] = x;
        m.matrix[1][1] = y;
        m.matrix[2][2] = z;
        return m;
    }
    
    public FloatBuffer toFloatBuffer(){
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        for ( int i = 0; i<4; i++ )
            for ( int j = 0; j<4; j++ )
                fb.put(matrix[i][j]);
        fb.flip();
        return fb;
    }
    
    public Matrix4F nhanMaTran(Matrix4F o){
        if ( o == null )
            throw new RuntimeException("Matrix4F.nhanMaTran parameter == null");
        Matrix4F result = new Matrix4F();

        for ( int i = 0; i<4; i++ ){
            for ( int j = 0; j<4; j++ ){
                result.matrix[i][j] = 0;
                for ( int k = 0; k<4; k++ )
                result.matrix[i][j] += this.matrix[i][k]*o.matrix[k][j];
            }            
        }
        return result;
    }
    
    public static Matrix4F PROJECTION(float left, float right, float bottom, float top, float zNear, float zFar){
        Matrix4F result = new Matrix4F();
        result.matrix[0][0] = 2/(right-left);
        result.matrix[1][1] = 2/(top-bottom);
        result.matrix[2][2] = -2/(zFar-zNear);
        result.matrix[0][3] = -(right+left)/(right-left);
        result.matrix[1][3] = -(top+bottom)/(top-bottom);
        result.matrix[2][3] = -(zFar+zNear)/(zFar-zNear);
        return result;
    }
    
    public String toString(){
        return matrix[0][0] + " " + matrix[0][1] + " " + matrix[0][2] + " " + matrix[0][3] + "\n" +
                    matrix[1][0] + " " + matrix[1][1] + " " + matrix[1][2] + " " + matrix[1][3] + "\n" +
                            matrix[2][0] + " " + matrix[2][1] + " " + matrix[2][2] + " " + matrix[2][3] + "\n" +
                                    matrix[3][0] + " " + matrix[3][1] + " " + matrix[3][2] + " " + matrix[3][3];
    }
    
    public Matrix4F clone(){
        Matrix4F r = new Matrix4F();
        for ( int i = 0; i<4; i++ )
            for ( int j = 0; j<4; j++ )
                r.matrix[i][j] = this.matrix[i][j];
        return r;
    }
}
