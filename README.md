## Generate RSA Key Pair using OpenSSL
Generate a KeyPair : This line generates an RSA private key with a length of 2048 bits using OpenSSL (openssl genrsa). It then specifies the output file (-out keypair.pem) where the generated private key will be saved. The significance lies in creating a private key that can be used for encryption, decryption, and digital signatures in asymmetric cryptography.

```openssl genrsa -out keypair.pem 2048 ```  
Generate a Public Key from the Private Key: This command extracts the public key from the previously generated private key (openssl rsa). It reads the private key from the file specified by -in keypair.pem and outputs the corresponding public key (-pubout) to a file named publicKey.pem. The significance is in obtaining the public key from the private key, which can be shared openly for encryption and verification purposes while keeping the private key secure.

```openssl rsa -in keypair.pem -pubout -out publicKey.pem```

Format the Private Key (keypair.pem) in Supported Format (PKCS8 format): This line converts the private key generated in the first step (keypair.pem) into PKCS#8 format, a widely used standard for private key encoding (openssl pkcs8). It specifies that the input key format is PEM (-inform PEM), the output key format is also PEM (-outform PEM), and there is no encryption applied (-nocrypt). The resulting private key is saved in a file named private.pem. The significance is in converting the private key into a standard format that is interoperable across different cryptographic systems and applications.

```openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out privateKey```