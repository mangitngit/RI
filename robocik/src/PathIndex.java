/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Radosław
 */
public class PathIndex {
    int acutalIdx;
    int cameFieldIdx;

    
int getActualIdx(){ return acutalIdx; }

void setActualIdx(int fieldIdx){this.acutalIdx = fieldIdx;}
    
int getFieldIdx(){ return cameFieldIdx; }

void setFieldIdx(int fieldIdx){this.cameFieldIdx = fieldIdx;}

}
