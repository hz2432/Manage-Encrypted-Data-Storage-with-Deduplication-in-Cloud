package com.rong.pre;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class test2 {
public static void main(String[] args) {
//	String a="1234";
//	String b="2222";
//	byte[] t=a.getBytes();
//	byte[] t2=b.getBytes();
//	byte[] c="0000".getBytes();
//	byte[] d="0000".getBytes();
//	for(int i=0;i<t.length;i++){
//		c[i]=(byte) (t[i]^t2[i]);
//	}
//	for(int i=0;i<t.length;i++){
//		d[i]=(byte) (c[i]^t2[i]);
//	}
//	String d2=new String(d);
//	System.out.println(d2);
//	System.out.println(new String(c));
	
	CurveParams curveParams = new CurveParams().load("C:/Documents and Settings/Administrator/Workspaces/MyEclipse 8.5/PRE/src/com/Setup/e_181_1024.properties");
	 Pairing pairing = PairingFactory.getPairing(curveParams);
	 Field G1=pairing.getG1();
	 Field GT=pairing.getGT();
	 Field Zq=pairing.getZr();
	 Element g=G1.newRandomElement();
	 
	 Element e=GT.newZeroElement();
	 
	Element w=g; 
	Element r = Zq.newRandomElement();
//	Element q=w.powZn(r);
	
	System.out.println("变化前g");
	 byte[] g1_byte=w.toBytes();
	 for(int i=0;i<g1_byte.length;i++){
		 System.out.print(g1_byte[i]+" ");
	 }
	 System.out.println();
	 
	Element C2e=g.powZn(r);
	 e=pairing.pairing(g,C2e);
	 
	 //test g
	 System.out.println("变化后");
	 byte[] g2_byte=w.toBytes();
	 for(int i=0;i<g2_byte.length;i++){
		 System.out.print(g2_byte[i]+" ");
	 }
	 System.out.println();
	
//	 e.powZn(r);
//	 Element t=e.powZn(r);
	 Element sk_j = Zq.newRandomElement();
	 Element s2=w.powZn(sk_j);
	 Element p2=C2e.powZn(sk_j.invert());
	 Element s3=pairing.pairing(s2,p2);
//	 Element s3_2=s3_1.powZn(r);
//	 Element s3=s3_2.powZn(r2.invert());
	 
	 byte[] e_byte=e.toBytes();
	 byte[] s3_byte=s3.toBytes();
	 for(int i=0;i<e_byte.length;i++){
		 System.out.print(e_byte[i]+" ");
	 }
	 System.out.println();
//	 byte[] t_byte=t.toBytes();
//	 for(int i=0;i<t_byte.length;i++){
//		 System.out.print(t_byte[i]+" ");
//	 }
//	 System.out.println();
	 for(int i=0;i<s3_byte.length;i++){
		 System.out.print(s3_byte[i]+" ");
	 }
	 System.out.println();
	 int a=1;
	 int b=0;
	 b=a;
	 int c=b;
	 a=2;
	 System.out.println(a);
	 System.out.println(b);
	 System.out.println(c);
}
}
