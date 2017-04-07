package com.rong.pre;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class ReEnc {
	Element C1;
    Element C2;
    Element C2e;
    Element C3;
    Element C4;
    Element t;
public ReEnc(){}
public ReEnc(Field Zq,Element rk_i_jj,Element C11,Element C22,Element C33,Element C44,Element tt){
	CurveParams curveParams = new CurveParams().load("E:/javaworkspace/test/src/com/rong/pre/e_181_1024.properties");
	Pairing pairing = PairingFactory.getPairing(curveParams);
	long start3 = System.nanoTime();
	C2e=pairing.pairing(C22,rk_i_jj);
	long end3 =System.nanoTime();
	System.out.println("--------------------------");
	System.out.println((end3 - start3) / 1000);
	Element C=Zq.newZeroElement();
//	C2e=C22.powZn(rk_i_jj);
	C1=C11;
	C2=C22;
	C3=C33;
	C4=C44;
	t=tt;
}
Element GetT(){
	return t;
}
Element GetC1(){
	return C1;
}
Element GetC2e(){
	return C2e;
}
Element GetC3(){
	return C3;
}
Element GetC4(){
	return C4;
}

}
