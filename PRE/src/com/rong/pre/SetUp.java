package com.rong.pre;




import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class SetUp {
	 CurveParams curveParams = new CurveParams().load("curve.properties");
	 Pairing pairing = PairingFactory.getPairing(curveParams);
     Field G=pairing.getG1();
     Field GT=pairing.getGT();
     Field Zq=pairing.getZr();
//    public static void main(String[] args) {
		Element g=G.newRandomElement();
		Element g1=G.newRandomElement();
		Element u=G.newRandomElement();
		Element v=G.newRandomElement();
		Element w=G.newRandomElement();
//		Element Z=GT.newZeroElement();
		Element Z=pairing.pairing(g, g);
		
//	}
    
}
