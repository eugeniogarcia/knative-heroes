# Creamos una clave privada
```
genrsa -out euge_old.key 2048
```
# Cambiamos el formato de la clave privada
```
pkcs8 -topk8 -nocrypt -in euge_old.key -out euge.key
```
# Creamos una solicitud de certificado
... para enviar a la CA. Usamos extensiones, las especificadas en v3_req del archivo .cnf  
```
req -new -key euge.key -out euge.pem -config conf.cnf -reqexts v3_req
```
# Firmamos con la CA la solicitud de certificado
... creando de esta manera el certificado. Añadimos en el certificado las extensiones indicadas en v3_ca  
```
x509 -req -days 3650 -in euge.pem -signkey euge.key -out euge.pem -extfile conf.cnf -extensions v3_ca
```
# Creamos el pfx para poder distribuir el certificado
Nos pedira que indiquemos una contraseña porque el certificado incluye la clave privada  

```
pkcs12 -export -in euge.pem -inkey euge.key -out euge.pfx
```
# Mostramos la clave publica
```
rsa -in euge.key -pubout
```
# Mostramos el contenido del certificado
```
keytool -printcert -file .\euge.pem
```
# UPDATE. How to create a .crt from a .pem
```
openssl x509 -outform der -in euge.pem -out euge.crt
```
