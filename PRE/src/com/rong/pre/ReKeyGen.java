package com.rong.pre;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

public class ReKeyGen {
Element rk_i_j,sk_i,pk_j;
public ReKeyGen(){}
public ReKeyGen(Field Zq,Element sk_i,Element pk_j){
	rk_i_j= Zq.newZeroElement();
	Element sk_i_temp = sk_i.duplicate();
	Element pk_j_temp=pk_j.duplicate();
	rk_i_j=pk_j_temp.powZn(sk_i_temp.invert());
	
	
}
Element GetRk_i_j(){
	return this.rk_i_j;
}
}

