package com.rong.pre;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

import java.security.MessageDigest;

public class EnC1 {
    byte[] C1_byte;
    byte[] C2_byte;
    byte[] C3_byte;
    byte[] C4_byte;
    Element C1;
    Element C2e;
    Element C3;
    Element C4;
    Element t;
    Element k;
	public EnC1() {
		// TODO Auto-generated constructor stub
	}
	public EnC1(Pairing pairing,Element pk_j,String m,Field Zq,Element gg,Element gg1,Element Z,Field G,Element u,Element v,Element w){
		Element r = Zq.newRandomElement();
		Element w1=gg1.duplicate();
		C1=w1.powZn(r);
		
		C3=G.newZeroElement();
		 
		Element g_temp=G.newZeroElement();
		g_temp=gg.duplicate();
		 Element C2ee=pairing.pairing(pk_j, gg);
		 C2e=(C2ee.powZn(r)).duplicate();
		 Element z_temp=Z.duplicate();
		 Element K=z_temp.powZn(r);
		 
		 System.out.println("变化后gg");
		 byte[] g2_byte=gg.toBytes();
		 for(int i=0;i<g2_byte.length;i++){
			 System.out.print(g2_byte[i]+" ");
		 }
		 System.out.println();
		 System.out.println("变化后c2e");
		 byte[] g3_byte=C2e.toBytes();
		 for(int i=0;i<g3_byte.length;i++){
			 System.out.print(g3_byte[i]+" ");
		 }
		 System.out.println();
		k=K;
		//test K
		System.out.println("test K");
		byte[] K_byte=K.toBytes();
		for(int i=0;i<K_byte.length;i++){
			System.out.print(K_byte[i]+" ");
		}
		System.out.println();
		
		System.out.println("C1"+C1.getLengthInBytes()+"K"+K.getLengthInBytes());
		byte[] m_byte=m.getBytes();
		C1_byte=C1.toBytes();
		
		
		byte[] K_C1_before_hash=new byte[K_byte.length+C1_byte.length];
		System.arraycopy(K_byte, 0, K_C1_before_hash, 0, K_byte.length);
		System.arraycopy(C1_byte, 0, K_C1_before_hash, K_byte.length, C1_byte.length);
		Element K_C1_after_hash=Zq.newZeroElement();
//		K_C1_after_hash.setFromHash(MD5_128.getMD5_128(K_C1_before_hash).getBytes(), 0, MD5_128.getMD5_128(K_C1_before_hash).getBytes().length);
		try{
			MessageDigest digest = MessageDigest.getInstance("MD5");
		
	      digest.update(K_C1_before_hash);
	      byte[] dig=digest.digest();
	      K_C1_after_hash.setFromHash(dig, 0, dig.length);
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		byte[] K_C1_after_hash_byte=K_C1_after_hash.toBytes();
		System.out.println(K_C1_after_hash_byte.length);
		System.out.println(K_C1_before_hash.length);
		//test k_c1_after_hash
		System.out.println("K_C1_after_hash_byte");
		for(int i=0;i<K_C1_after_hash_byte.length;i++){
			System.out.print(K_C1_after_hash_byte[i]+" ");
		}
		System.out.println();
		int m_length=m.length();
		byte[] K_C1_m_length=new byte[m_length];
		System.arraycopy(K_C1_after_hash_byte, 0, K_C1_m_length, 0, m_length);
		byte[] K_C1_XOR_m=new byte[m_length];
		for(int i=0;i<m_length;i++){
			K_C1_XOR_m[i]=(byte) (K_C1_m_length[i]^m_byte[i]);
		}
		
		C3_byte=new byte[K_C1_after_hash_byte.length];
		System.arraycopy(K_C1_XOR_m, 0, C3_byte, 0, m_length);
		System.arraycopy(K_C1_after_hash_byte, m_length, C3_byte, m_length, (K_C1_after_hash_byte.length-m_length));
		C3.setFromBytes(C3_byte);
		//test c3_byte
		System.out.println("test c3_byte!");
		byte[] test_C3=C3.toBytes();
		for(int i=0;i<test_C3.length;i++)
		{System.out.print(test_C3[i]+" ");}
		System.out.println();
		byte[] test_C3_byte=new byte[m_length];
		System.arraycopy(C3_byte, 0, test_C3_byte, 0, m_length);
		for(int i=0;i<m_length;i++)
		{System.out.print(test_C3_byte[i]+" ");}
		System.out.println();
		//step2
		
		t = Zq.newRandomElement();
		C2_byte=C2e.toBytes();
		byte[] C1_C3_before_hash=new byte[C1_byte.length+C3_byte.length];
		System.arraycopy(C1_byte, 0, C1_C3_before_hash, 0, C1_byte.length);
		System.arraycopy(C3_byte, 0, C1_C3_before_hash, C1_byte.length, C3_byte.length);
		Element h=Zq.newZeroElement();
		try{
			MessageDigest digest = MessageDigest.getInstance("MD5");
		
	      digest.update(C1_C3_before_hash);
	      byte[] dig=digest.digest();
	      h.setFromHash(dig, 0, dig.length);
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		C4=u;
		C4.powZn(h);
		C4.mul(v.powZn(t));
		C4.mul(w);
		C4.powZn(r);
		C4_byte=C4.toBytes();
		
		
	}
	
	byte[] GetC1_byte(){
		return this.C1_byte;
	}
	byte[] GetC2_byte(){
		return this.C2_byte;
	}
	byte[] GetC3_byte(){
		return this.C3_byte;
	}
	byte[] GetC4_byte(){
		return this.C4_byte;
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
	Element GetK(){
		return k;
	}
}
