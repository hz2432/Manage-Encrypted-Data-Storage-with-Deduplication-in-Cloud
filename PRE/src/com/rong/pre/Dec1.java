package com.rong.pre;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

import java.security.MessageDigest;

public class Dec1 {
	Element K;
	byte[] m_byte;
	byte[] C3_byte;
	
public Dec1() {
	// TODO Auto-generated constructor stub
}
public Dec1(Pairing pairing,Element pk_j,Element sk_j,Element gg,int m_length,Field Zq,Element C11,Element C22e,byte[] C33){
//	 CurveParams curveParams = new CurveParams().load("C:/Documents and Settings/Administrator/Workspaces/MyEclipse 8.5/PRE/src/com/Setup/e_181_1024.properties");
//	 Pairing pairing = PairingFactory.getPairing(curveParams);
//	K=GTT.newZeroElement();
//	K=pairing.pairing(C22, g);
//	K.powZn(sk_i.invert());
//	K=pairing.pairing(gg.powZn(sk_j), C22e.powZn(sk_j.invert()));
	 Element skj_temp=sk_j.duplicate();
	 Element c2e_temp=C22e.duplicate();
	 
	 K=c2e_temp.powZn(skj_temp.invert());
	byte[] C1_byte=C11.toBytes();
	byte[] K_byte=K.toBytes();
	
	//test K
	System.out.println("test K");
	for(int i=0;i<K_byte.length;i++){
		System.out.print(K_byte[i]+" ");
	}
	System.out.println();
	byte[] K_C1_before_hash=new byte[K_byte.length+C1_byte.length];
	System.arraycopy(K_byte, 0, K_C1_before_hash, 0, K_byte.length);
	System.arraycopy(C1_byte, 0, K_C1_before_hash, K_byte.length, C1_byte.length);
	Element K_C1_after_hash=Zq.newZeroElement();
//	K_C1_after_hash.setFromHash(MD5_128.getMD5_128(K_C1_before_hash).getBytes(), 0, MD5_128.getMD5_128(K_C1_before_hash).getBytes().length);
	try{
		MessageDigest digest = MessageDigest.getInstance("MD5");
	
      digest.update(K_C1_before_hash);
      byte[] dig=digest.digest();
      K_C1_after_hash.setFromHash(dig, 0, dig.length);
	}catch (Exception e) {
		// TODO: handle exception
	}
	
	byte[] K_C1_after_hash_byte=K_C1_after_hash.toBytes();
	
	byte[] K_C1_after_hash_byte_m_length=new byte[m_length];
	System.arraycopy(K_C1_after_hash_byte, 0, K_C1_after_hash_byte_m_length, 0, m_length);
	//test K_C1_after_hash_byte_m_length
	System.out.println("K_C1_after_hash_byte_m_length");
	for(int i=0;i<m_length;i++){
		System.out.print(K_C1_after_hash_byte_m_length[i]+" ");
	}
	System.out.println();
	C3_byte=C33;
//	//test C3
//	for(int i=0;i<C3_byte.length;i++)
//	{System.out.print(C3_byte[i]+" ");}
//	System.out.println();
//	
	byte[] C3_byte_m_length=new byte[m_length];
	System.arraycopy(C3_byte, 0, C3_byte_m_length, 0, m_length);
	//TEST c3_byte
	System.out.println("test c3_byte");
	
	for(int i=0;i<m_length;i++)
	{System.out.print(C3_byte_m_length[i]+" ");}
	System.out.println();
	
	m_byte=new byte[m_length];
	for(int i=0;i<m_length;i++){
		m_byte[i]=(byte)(K_C1_after_hash_byte_m_length[i]^C3_byte_m_length[i]);
	}
	
}
void Print_M(){
	
	try{
		String t=new String(m_byte);
		System.out.println(t);
	}catch (Exception e) {
		e.printStackTrace();// TODO: handle exception
	}
}
}
