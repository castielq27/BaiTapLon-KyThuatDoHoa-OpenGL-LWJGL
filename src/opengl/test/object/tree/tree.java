/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.test.object.tree;

import Utils.Matrix4F;

/**
 *
 * @author castiel
 */
public class tree {
    private traicay f;
    private leaf l;
    private trunk t;
    private int vao;
    
    public tree(int vao){
        this.vao = vao;
        this.f = new traicay(vao);
        this.l = new leaf(vao);
        this.t = new trunk(vao);
    }
    
    public void deleteProgram(){
        f.deleteProgram();
        l.deleteProgram();
        t.deleteProgram();
    }

   
    
    public void setModelMatrix(Matrix4F m){
        f.setModelMatrix(m);
        l.setModelMatrix(m);
        t.setModelMatrix(m);
    }
    
    public void setViewMatrix(Matrix4F v){
        f.setViewMatrix(v);
        l.setViewMatrix(v);
        t.setViewMatrix(v);
    }

    public void setProjectionMatrix(Matrix4F p){
        f.setProjectionMatrix(p);
        l.setProjectionMatrix(p);
        t.setProjectionMatrix(p);
 }

    
    
    
    public void render(){
        f.render();
        l.render();
        t.render();
     }
}
