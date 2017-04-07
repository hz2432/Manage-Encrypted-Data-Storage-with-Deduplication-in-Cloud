package com.rong.pre;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class test3 {
	public static void main(String[] args) {
		 CurveParams curveParams = new CurveParams().load("C:/Documents and Settings/Administrator/Workspaces/MyEclipse 8.5/PRE/src/com/Setup/e_181_1024.properties");
		 Pairing pairing = PairingFactory.getPairing(curveParams);
		 Field G=pairing.getG1();
	     Field GT=pairing.getGT();
	     Field Zq=pairing.getZr();
//	    public static void main(String[] args) {
			Element g=G.newRandomElement();
			Element g1=G.newRandomElement();
			Element u=G.newRandomElement();
			Element v=G.newRandomElement();
			Element w=G.newRandomElement();
//			Element Z=GT.newZeroElement();
			Element Z=pairing.pairing(g, g);
			
//			byte[] test_g=g.toBytes();
//			for(int i=0;i<test_g.length;i++){
//			System.out.print(test_g[i]+" ");}
//			System.out.println(" ");
//			//测试初始化参数
//			//打印g1
//			System.out.println("用前打印g1");
//			byte[] test_g1=g1.toBytes();
//			for(int i=0;i<test_g1.length;i++){
//			System.out.print(test_g1[i]+" ");}
//			System.out.println(" ");
////			
//			System.out.println("私钥1");
//			KeyGen test_keygen=new KeyGen(Zq,G, g1);
//			System.out.println("用后打印g1");
//			byte[] test_g11=g1.toBytes();
//			for(int i=0;i<test_g11.length;i++){
//			System.out.print(test_g11[i]+" ");}
//			System.out.println(" ");
//			byte[] test_keygensk_byte=test_keygen.GetSk().toBytes();
//			Element sk_i=test_keygen.GetSk();
//			for(int i=0;i<test_keygensk_byte.length;i++){
//				System.out.print(test_keygensk_byte[i]+" ");}
//			System.out.println(" ");
//			System.out.println("公钥1");
//			byte[] test_keygenpk_byte=test_keygen.GetPk().toBytes();
//			Element pk_i=G.newZeroElement();
//			pk_i.set(test_keygen.GetPk());
//			for(int i=0;i<test_keygenpk_byte.length;i++){
//				System.out.print(test_keygenpk_byte[i]+" ");}
//			System.out.println(" ");
//			//用后打印g1
//			System.out.println("用后打印g1");
//			byte[] test_g1122=g1.toBytes();
//			for(int i=0;i<test_g1122.length;i++){
//			System.out.print(test_g1122[i]+" ");}
//			System.out.println(" ");
////			打印pki1
//			System.out.println("打印pki1");
//			byte[] test_g1112=pk_i.toBytes();
//			for(int i=0;i<test_g1112.length;i++){
//			System.out.print(test_g1112[i]+" ");}
//			System.out.println(" ");
//			
//			g1.powZn(sk_i.invert());
////			私钥2用前打印g1
//			System.out.println("私钥2用前打印g1");
//			byte[] test_g111=g1.toBytes();
//			for(int i=0;i<test_g111.length;i++){
//			System.out.print(test_g111[i]+" ");}
//			System.out.println(" ");
////			打印pki2
//			System.out.println("打印pki2");
//			byte[] test_g11122=pk_i.toBytes();
//			for(int i=0;i<test_g11122.length;i++){
//			System.out.print(test_g11122[i]+" ");}
//			System.out.println(" ");
//			
//			
//			
//			System.out.println("私钥2");
//			KeyGen test_keygen2=new KeyGen(Zq, G,g1);
//			Element sk_j=test_keygen2.GetSk();
//			byte[] test_keygen2sk_byte=test_keygen2.GetSk().toBytes();
//			for(int i=0;i<test_keygen2sk_byte.length;i++){
//				System.out.print(test_keygen2sk_byte[i]+" ");}
//			System.out.println(" ");
//			System.out.println("公钥2");
//			Element pk_j=test_keygen2.GetPk();
//			byte[] test_keygen2pk_byte=test_keygen2.GetPk().toBytes();
//			for(int i=0;i<test_keygen2pk_byte.length;i++){
//				System.out.print(test_keygen2pk_byte[i]+" ");
//				}
//			System.out.println(" ");
			Element gg1=g.duplicate();
			Element r=Zq.newRandomElement();
			Element gg2=(gg1.powZn(r)).duplicate();
			Element t=pairing.pairing(gg2, g);
			
			Element t2=pairing.pairing(g, g);
			t2.powZn(r);
			byte[] test_g11122=t.toBytes();
			for(int i=0;i<test_g11122.length;i++){
			System.out.print(test_g11122[i]+" ");}
			System.out.println(" ");
			byte[] test_g111222=t2.toBytes();
			for(int i=0;i<test_g111222.length;i++){
			System.out.print(test_g111222[i]+" ");}
			System.out.println(" ");
			
}}
