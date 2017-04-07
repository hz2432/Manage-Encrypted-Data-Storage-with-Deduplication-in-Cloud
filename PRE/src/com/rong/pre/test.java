package com.rong.pre;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class test {
int[] i={1,1};
public test(){
	
}
int[] get(){
	return this.i;
}
public static void main(String[] args) {
	 CurveParams curveParams = new CurveParams().load("E:/javaworkspace/test/src/com/rong/pre/e_181_1024.properties");
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
		
//		
		System.out.println("sk1");
		long start = System.nanoTime();
		KeyGen test_keygen=new KeyGen(Zq,G,g);
		byte[] test_keygensk_byte=test_keygen.GetSk().toBytes();
		long end =System.nanoTime();
		System.out.println("--------------------------");
		System.out.println((end - start) / 1000);
		Element sk_i=test_keygen.GetSk();
		for(int i=0;i<test_keygensk_byte.length;i++){
			System.out.print(test_keygensk_byte[i]+" ");}
		System.out.println(" ");
		System.out.println("pk1");
		long start1 = System.nanoTime();
		byte[] test_keygenpk_byte=test_keygen.GetPk().toBytes();
		long end1 =System.nanoTime();
		System.out.println("--------------------------");
		System.out.println((end1 - start1) / 1000);
		Element pk_i=test_keygen.GetPk();
		for(int i=0;i<test_keygenpk_byte.length;i++){
			System.out.print(test_keygenpk_byte[i]+" ");}
		System.out.println(" ");
//		打印pki1
		
		
		System.out.println("sk2");
		KeyGen test_keygen2=new KeyGen(Zq,G, g);
		Element sk_j=test_keygen2.GetSk();
		byte[] test_keygen2sk_byte=test_keygen2.GetSk().toBytes();
		for(int i=0;i<test_keygen2sk_byte.length;i++){
			System.out.print(test_keygen2sk_byte[i]+" ");}
		System.out.println(" ");
		System.out.println("pk2");
		Element pk_j=test_keygen2.GetPk();
		byte[] test_keygen2pk_byte=test_keygen2.GetPk().toBytes();
		for(int i=0;i<test_keygen2pk_byte.length;i++){
			System.out.print(test_keygen2pk_byte[i]+" ");
			}
		System.out.println(" ");
		//测试私钥公钥产生类
		
		//产生第二组公私钥，并和第一组比较
		System.out.println("rk_1-2");
		long start2 = System.nanoTime();
		ReKeyGen rekeygen=new ReKeyGen(Zq,sk_i, pk_j);
		byte[] rekey_byte=rekeygen.GetRk_i_j().toBytes();
		long end2 =System.nanoTime();
		System.out.println("--------------------------");
		System.out.println((end2 - start2) / 1000);
		for(int i=0;i<rekey_byte.length;i++){
			System.out.print(rekey_byte[i]+" ");}
		System.out.println(" ");
		
		//加密2层
		String m="110626014111117";
		System.out.println("加密2层");
		EnC2 test_enc2=new EnC2(pairing,pk_i, m, Zq, g, g1, Z, GT, u, v, w);
		System.out.println("加密2层完成");
		System.out.println("解密2层");	
		Dec2 test_dec2=new Dec2(GT,G,pairing,sk_i,g, m.length(), Zq, test_enc2.GetC1(), test_enc2.GetC2(), test_enc2.GetC3_byte(), test_enc2.GetC4());
        test_dec2.Print_M();
		System.out.println("解密2层完成");		
//        
		System.out.println("加密1层");
		EnC1 test_enc1=new EnC1(pairing,pk_j, m, Zq, g, g1, Z, GT, u, v, w);
		System.out.println("加密1层完成");
		System.out.println("解密1层");
		Dec1 test_dec1=new Dec1(pairing, pk_j, sk_j, g, m.length(), Zq, test_enc1.GetC1(), test_enc1.GetC2e(), test_enc1.GetC3_byte());
		test_dec1.Print_M();
		System.out.println("解密1层完成");	
//		//重加密密钥
		Element rk=rekeygen.GetRk_i_j();
////		
////		//密钥转换
		System.out.println("重加密");
		int x=10;
		while(x>0){
		ReEnc test_re=new ReEnc(Zq,rk, test_enc2.GetC1(), test_enc2.GetC2(), test_enc2.GetC3(), test_enc2.GetC4(), test_enc2.GetT());
		x--;
		}
//		System.out.println("重加密完成");
////		//密钥转换后解密
//		System.out.println("重解密");
//		Dec1 test_dec11=new Dec1(pairing, pk_j, sk_j, g, m.length(), Zq, test_enc2.GetC1(), test_re.GetC2e(), test_enc2.GetC3_byte());
//		test_dec11.Print_M();
//		System.out.println("重解密完成");
}
}
