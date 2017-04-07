#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <time.h>
#include "openssl/evp.h"
#include "openssl/x509.h"
#include "openssl/rsa.h"
#include "openssl/md5.h"
#include "openssl/objects.h"
#include <mysql/mysql.h>

//Des加密函数
int Des_Encrypt_File(char *filename,char *username)
{
  struct timeval start, end;
  unsigned char key[EVP_MAX_KEY_LENGTH];          //保存密钥的数组
  unsigned char iv[EVP_MAX_KEY_LENGTH];    //保存初始化向量的数组
  EVP_CIPHER_CTX ctx;                        //EVP加密上下文环境
  unsigned char out[102400];                  //保存密文的缓冲区
  int outl;
  unsigned char in[1024];                   //保存原文的缓冲区
  char keyname[100],cname[100];
  int inl;
  int rv;
  int i;
  FILE *fpIn;
  FILE *fpOut;
  FILE *fpkey;
  //打开待加密文件
  fpIn= fopen(filename,"rb");
  if(fpIn==NULL)
  {
  		printf("not find file1");
            return-1;
  }
  //设置key和iv
  sprintf(keyname,"key_%s_%s",username,filename);
  fpkey= fopen(keyname,"wb+");
  for(i=0;i<16;i++)
  {
            key[i]=rand()%15;
            fprintf(fpkey,"%d\n",key[i]);
  }
  fclose(fpkey);

  sprintf(cname,"C_%s",filename);
  fpOut= fopen(cname,"wb");
  if(fpOut==NULL)
  {
  		printf("not find file2");
            fclose(fpIn);
            return-1;
  }
  for(i=0;i<8;i++)
  {
            iv[i]=i;
  }
  //初始化ctx
  EVP_CIPHER_CTX_init(&ctx);
  //设置密码算法、key和iv
  rv= EVP_EncryptInit_ex(&ctx,EVP_aes_128_cbc(),NULL,key,iv);
  if(rv!= 1)
  {
            printf("Err\n");
            return-1;
  }
  //循环读取原文，加密后后保存到密文文件。
  long int Enc_time;
  for(;;)
    {
              inl= fread(in,1,1024,fpIn);
              if(inl<= 0)//读取原文结束
                        break;
              gettimeofday(&start, NULL);
              rv= EVP_EncryptUpdate(&ctx,out,&outl,in,inl);//加密
              gettimeofday(&end, NULL);
              Enc_time=Enc_time+(end.tv_sec*1000000+end.tv_usec-start.tv_sec*1000000-start.tv_usec);
              if(rv!= 1)
              {
                        fclose(fpIn);
                        fclose(fpOut);
                        EVP_CIPHER_CTX_cleanup(&ctx);
                        return-1;
              }
              fwrite(out,1,outl,fpOut);//保存密文到文件
    }
    //加密结束
    gettimeofday( &start, NULL );
    rv= EVP_EncryptFinal_ex(&ctx,out,&outl);
    gettimeofday( &end, NULL );
    Enc_time=Enc_time+(end.tv_sec*1000000+end.tv_usec-start.tv_sec*1000000-start.tv_usec);
    if(rv!= 1)
    {
              fclose(fpIn);
              fclose(fpOut);
              EVP_CIPHER_CTX_cleanup(&ctx);
              return-1;
    }
    fwrite(out,1,outl,fpOut);          //保密密文到文件
    fclose(fpIn);
    fclose(fpOut);
//    printf("time : %d\n", Enc_time);
    printf("Encrypted!\n");
    return 1;
}

//Des解密函数
int Des_Decrypt_File(char *filename,char *username)
{
  struct timeval start, end;
  unsigned char key[EVP_MAX_KEY_LENGTH];                  //保存密钥的数组
  unsigned char iv[EVP_MAX_KEY_LENGTH];            //保存初始化向量的数组
  EVP_CIPHER_CTX ctx;                                //EVP加密上下文环境
  unsigned char out[1024+EVP_MAX_KEY_LENGTH];      //保存解密后明文的缓冲区数组
  int outl;
  unsigned char in[1024];                              //保存密文数据的数组
  char cfilename[50],mfilename[50];
  int inl;
  int rv;
  int i;
//  int key_temp[20];
  FILE*fpIn;
//  FILE*fpkey;
  FILE*fpOut;

  //打开待解密的密文文件
  sprintf(cfilename,"C_%s",filename);
  fpIn= fopen(cfilename,"rb");
  if(fpIn==NULL)
  {
	  	  	printf("Download Failed! File doesn't exit!\n");
            return-1;
  }
  //打开保存明文的文件
  sprintf(mfilename,"M_%s",filename);
  fpOut= fopen(mfilename,"wb");
  if(fpOut==NULL)
  {
            fclose(fpIn);
            return-1;
  }

  for(i=0;i<16;i++)
  {
      key[i]=rand()%15;
  }
  for(i=0;i<8;i++)
  {
            iv[i]=i;
  }

  //初始化ctx
  EVP_CIPHER_CTX_init(&ctx);
  //设置解密的算法、key和iv
  rv= EVP_DecryptInit_ex(&ctx,EVP_aes_128_cbc(),NULL,key,iv);
  if(rv!= 1)
  {
            EVP_CIPHER_CTX_cleanup(&ctx);
            return-1;
  }
  //循环读取原文，解密后后保存到明文文件。
  long int Dec_time=0;
  for(;;)
  {
            inl= fread(in,1,1024,fpIn);
            if(inl<= 0)
                      break;
            gettimeofday( &start, NULL );
            rv= EVP_DecryptUpdate(&ctx,out,&outl,in,inl);//解密
            gettimeofday( &end, NULL );
            Dec_time=Dec_time+(end.tv_sec*1000000+end.tv_usec-start.tv_sec*1000000-start.tv_usec);
            if(rv!= 1)
            {
                      fclose(fpIn);
                      fclose(fpOut);
                      EVP_CIPHER_CTX_cleanup(&ctx);
                      return -1;
            }
            fwrite(out,1,outl,fpOut);//保存明文到文件
  }
  //解密结束
  gettimeofday( &start, NULL );
  rv=EVP_DecryptFinal_ex(&ctx,out,&outl);
  gettimeofday( &end, NULL );
  Dec_time=Dec_time+(end.tv_sec*1000000+end.tv_usec-start.tv_sec*1000000-start.tv_usec);
  if(rv!= 1)
  {
            fclose(fpIn);
            fclose(fpOut);
            EVP_CIPHER_CTX_cleanup(&ctx);
            return-1;
  }
  fwrite(out,1,outl,fpOut);//保存明文到文件
  fclose(fpIn);
  fclose(fpOut);
  EVP_CIPHER_CTX_cleanup(&ctx);//清除EVP加密上下文环境
//  printf("time : %d\n", Dec_time);
  printf("Decryption! Download Success!\n");
  return 1;
}

//RSA密钥生成
char *RSA_Sign_Gen(char *filename,char *username)
{
  struct timeval start, end;
  int  ret;
  int i,n;
  long int hash_time=0,sign_time=0;
  FILE *fp;
  RSA *r;
  int bits=1024,signlen,datalen,nid;
  unsigned long e=RSA_3;
  BIGNUM  *bne;
  unsigned char data[10240],signret[200],out[15],out2[15];
  fp=fopen(filename,"r");
  i=0;
  while(!feof(fp))
  {
    data[i]=fgetc(fp);
    i++;
  }
  data[i-1]='\0';
  n=strlen(data);
  bne=BN_new();
  ret=BN_set_word(bne,e);
  r=RSA_new();
  ret=RSA_generate_key_ex(r,bits,bne,NULL);
  if(ret!=1)
  {
    printf("RSA_generate_key_ex err!\n");
    return -1;
  }
  gettimeofday(&start, NULL);
  MD5(data,n,out);
  gettimeofday(&end, NULL);
  hash_time=end.tv_sec*1000000+end.tv_usec-start.tv_sec*1000000-start.tv_usec;
  char check[20];
  for(i=0;i<15;i++)
  {
	  sprintf(check+i,"%x",out[i]);
  }
  check[i-1]='\0';
  datalen=15;
  nid=NID_md5;
  gettimeofday(&start, NULL);
  ret=RSA_sign(nid,out,datalen,signret,&signlen,r);
  gettimeofday(&end, NULL);
  sign_time=sign_time+end.tv_sec*1000000+end.tv_usec-start.tv_sec*1000000-start.tv_usec;
  if(ret!=1)
  {
    printf("RSA_sign err!\n");
    RSA_free(r);
    return -1;
  }
  printf("Sign success！!\n");
  gettimeofday(&start, NULL);
  ret=RSA_verify(nid,out,datalen,signret,signlen,r);
  gettimeofday(&end, NULL);
  sign_time=sign_time+end.tv_sec*1000000+end.tv_usec-start.tv_sec*1000000-start.tv_usec;
  if(ret!=1)
  {
    printf("RSA_verify err!\n");
    RSA_free(r);
    return -1;
  }
  printf("Verify success！!\n");
//  printf("hash_time:%d\n",hash_time);
//  printf("sign_time:%d\n",sign_time);
  return check;
}

int Check_Duplicate(char *filename,char *hashcode,char *username)
{
	 MYSQL *pConn;
	 MYSQL_RES *result;
	 MYSQL_ROW row;
	 char *name,*uname;
	 int rc;
	 int x;
	 char statement[128];
	 pConn = mysql_init(NULL);
	 if(!mysql_real_connect(pConn,"192.168.0.106","root","zhq823","csp",0,NULL,0))
	 {
	  printf("无法连接数据库:%s",mysql_error(pConn));
	  return -1;
	 }
	 if(filename=="0")sprintf(statement,"select user_name from save where file_hash='%s'",hashcode);
	 if(hashcode=="0")sprintf(statement,"select user_name from save where file_name='%s'",filename);
	 rc = mysql_query(pConn,statement);
	 if(rc!= 0)
	 {
	      printf("Error: %s\n", mysql_error(pConn));
	      return -1;
	 }
	   result = mysql_store_result(pConn);
	   if(result == NULL)
	   {
	      printf("Error: %s\n", mysql_error(pConn));
	      return -1;
	   }
	   row = mysql_fetch_row(result);
	   if(row == NULL)
	   {
	      x=0;
	      mysql_free_result(result);
	      mysql_close(pConn);
	   }
	   else
	   {
		  name=username;
		  uname = row[0];
	      if(strcmp(name,uname)==0)x=1;
		  if(strcmp(name,uname)!=0)x=2;
		  mysql_free_result(result);
		  mysql_close(pConn);
	   }
	   return x;
}

int Upload_File_To_CSP(char *p1,int p2,char *p3,char *p4,char *p5,char *p6,int p7,int d)
{
	 MYSQL *pConn;
	 char sql_insert[1024];
	 char sql_delete[1024];
	 sprintf(sql_insert, "INSERT INTO save(user_name,authority,file_name,file_hash,file_C,C_key,share) values('%s','%d','%s','%s','%s','%s','%d')", p1,p2,p3,p4,p5,p6,p7);
	 pConn = mysql_init(NULL);
	 if(!mysql_real_connect(pConn,"192.168.0.106","root","zhq823","csp",0,NULL,0))
	 {
	  printf("无法连接数据库:%s",mysql_error(pConn));
	  return -1;
	 }
	 mysql_query(pConn,"set names gbk");
	 if(d==1)
	 {
		 sprintf(sql_delete,"delete from save where file_hash = '%s'",p4);
		 if(mysql_query(pConn,sql_delete))
		 {
			  printf("执行delete失败%s",mysql_error(pConn));
			  return -1;
		 }
	 }
	 if(mysql_query(pConn,sql_insert))
	 {
	  printf("执行insert失败%s",mysql_error(pConn));
	  return -1;
	 }
	 printf("Upload Success！\n");
	 mysql_close(pConn);
	 return 0;
}

int Check_Share(char *p1)
{
	 MYSQL *pConn;
	 MYSQL_RES *result;
	 MYSQL_ROW row;
	 int rc;
	 int x;
	 char *t;
	 char sql_select[128];
	 pConn = mysql_init(NULL);
	 if(!mysql_real_connect(pConn,"192.168.0.106","root","zhq823","csp",0,NULL,0))
	 {
	  printf("无法连接数据库:%s",mysql_error(pConn));
	  return -1;
	 }
	 sprintf(sql_select,"select share from save where file_name='%s'",p1);
	 rc = mysql_query(pConn,sql_select);
	 if(rc!= 0)
	 {
	      printf("Error: %s\n", mysql_error(pConn));
	      return -1;
	 }
	   result = mysql_store_result(pConn);
	   if(result == NULL)
	   {
	      printf("Error: %s\n", mysql_error(pConn));
	      return -1;
	   }
	   row = mysql_fetch_row(result);
	   if(row == NULL)
	   {
		   x = 2;
	   }
	   else
	   {
		   t=row[0];
		   if(strcmp(t,"0")==0)x=0;
		   if(strcmp(t,"1")==0)x=1;
	   }
	   mysql_free_result(result);
	   mysql_close(pConn);
	   return x;
}

int Share(char *p1,int p2)
{
	 MYSQL *pConn;
	 char sql_update[1024];
	 sprintf(sql_update, "update save set share = '%d' where file_name = '%s'", p2,p1);
	 pConn = mysql_init(NULL);
	 if(!mysql_real_connect(pConn,"192.168.0.106","root","zhq823","csp",0,NULL,0))
	 {
	  printf("无法连接数据库:%s",mysql_error(pConn));
	  return -1;
	 }
	 mysql_query(pConn,"set names gbk");
	 if(mysql_query(pConn,sql_update))
	 {
	  printf("执行insert失败%s",mysql_error(pConn));
	  return -1;
	 }
	 printf("set share success！\n");
	 mysql_close(pConn);
	 return 0;
}

int Delete_File(char *p1)
{
	 MYSQL *pConn;
	 char sql_delete[1024];
	 pConn = mysql_init(NULL);
	 if(!mysql_real_connect(pConn,"192.168.0.106","root","zhq823","csp",0,NULL,0))
	 {
	  printf("无法连接数据库:%s",mysql_error(pConn));
	  return -1;
	 }
	 mysql_query(pConn,"set names gbk");
	 sprintf(sql_delete,"delete from save where file_hash = '%s'",p1);
	 if(mysql_query(pConn,sql_delete))
	 {
		printf("执行delete失败%s",mysql_error(pConn));
		return -1;
	 }
	 return 0;
}


int main()
{
  int i;
  char f[50],name[50];
  char *filehash,*Cfile,*Ckey;
  int auth;
  int check_copy;
  OpenSSL_add_all_algorithms();
  printf("|-------Welcome to Cloud! Please choose service!--------|\n");
  printf("|                                |\n");
  printf("|  1:Upload  2:Download  3:Delete  4:Quit  |\n");
  printf("|                                |\n");
  printf("|---------------------------------|\n");
  printf("Your choice:");
  scanf("%d",&i);
  printf("Please input your Account :\n");
  scanf("%s",name);
  switch(i)
  {
    case 1:
    	printf("Please input the name of file which you want to save in the Cloud:\n");
    	scanf("%s",f);
    	printf("If you are owner or holder?owner input 1,holder input 0 :\n");
    	scanf("%d",&auth);
    	printf("Please Wait.........\n");
    	Des_Encrypt_File(f,name);
    	filehash=RSA_Sign_Gen(f,name);
    	check_copy=Check_Duplicate("0",filehash,name);
    	sprintf(Ckey,"key_%s_%s",name,f);
    	sprintf(Cfile,"C_%s",f);
    	if(check_copy==0)Upload_File_To_CSP(name,auth,f,filehash,Cfile,Ckey,0,0);
    	if(check_copy==1)printf("You have saved the file before!!\n");
    	if(check_copy==2)
    	{
    		if(auth==1)Upload_File_To_CSP(name,auth,f,filehash,Cfile,Ckey,1,1);
    		if(auth==0)
    		{
    			Share(f,1);
    			printf("File has been saved by other users!\n");
    		}
    	}
    	break;
    case 2:
    	printf("Please input the name of file which you want download from Cloud:\n");
    	scanf("%s",f);
//    	check_copy=Check_Duplicate(f,"0",name);
//    	if(Check_Share(f) == 1 || (Check_Share(f) == 0 && check_copy == 1))Des_Decrypt_File(f,name);
//    	if(Check_Share(f) == 0 && check_copy == 2)printf("You don't have right to download this file!");
//    	if(Check_Share(f) == 2)printf("File doesn't exist!\n");
    	Des_Decrypt_File(f,name);
    	break;
    case 3:
    	printf("Please input the name of file you want to delete:\n");
    	scanf("%s",f);
    	if(Check_Share(f) == 1)Share(f,0);
    	if(Check_Share(f) == 0)Delete_File(f);
    	if(Check_Share(f) == 2)printf("File doesn't exist!\n");
    	printf("Delete Success!");
    	break;
    case 4:
    	exit(0);
    	break;
    default:
    	printf("Erorr! Choose again!\n");
  }
  return 0;
}
