package com.rong.pre;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import com.rong.pre.SetUp;

public class KeyGen {
	Element sk;
	Element pk;
	public KeyGen(){}
	public KeyGen(Field Zq,Field G,Element g) {
		
		this.sk = Zq.newRandomElement();
		pk = G.newRandomElement();
		Element g1_temp=G.newZeroElement();
		g1_temp.set(g);
        this.pk=(g1_temp.powZn(sk)).duplicate();
        
	}
	Element GetSk(){
		return this.sk;
	}
	Element GetPk(){
		return this.pk;
	}
}
