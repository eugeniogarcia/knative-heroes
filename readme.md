# Usando JIB

Actualizar las propiedades del plugin jib con los datos del repositorio, el usuario y la contraseña:

```yml
<plugin>
<groupId>com.google.cloud.tools</groupId>
<artifactId>jib-maven-plugin</artifactId>
<version>1.7.0</version>
<configuration>
  <from>
	<image>openjdk:8-jre-alpine</image>
	<auth>
		<username>egsmartin</username>
		<password>Vera1511</password>
	</auth>
  </from>

  <to>
	<image>pruebacontenedor.azurecr.io/${project.artifactId}:${project.version}</image>  
	<auth>
		<username>pruebacontenedor</username>
		<password>Pr0bDGtfdIKbWj+pGbGEsFpc8D/3enAH</password>
	</auth>
  </to>
```

Podemos personalizar como la imagen va a ser creada. Por ejemplo, podriamos especificar el puerto que la imagen va a exponer - en este caso el `8082`:

```yml
	  <container>
		<ports>
			<port>8082</port>
		</ports>
		<!-- 
		<jvmFlags>
			<jvmFlag>-Xms512m</jvmFlag>
			<jvmFlag>-Xmx512m</jvmFlag>
		</jvmFlags>
		 -->
	  </container>
	  <allowInsecureRegistries>true</allowInsecureRegistries>
 </configuration>
```
			
Lanzamos el proceso de creacion de la imagen como sigue:

```sh
mvn compile jib:build
```

# Con Dockerfile

Tenemos que arrancar el demonio de docker. Una vez arrancado podemos proceder a construir la imagen con nuestro `dockerfile`.

```sh
docker build -t knative-heroes .
```

Podemos ejecutar la imagen:

```sh
docker run  -d -p8084:8082 knative-heroes
```

Para comprobar que efectivamente esta corriendo:

```sh
curl http://localhost:8084/heroes

[{"id":1,"name":"Pupa"},{"id":2,"name":"Nani"},{"id":3,"name":"Mausi"},{"id":4,"name":"Nico"},{"id":5,"name":"Verita"},{"id":6,"name":"Eugenio"}]
```

Listamos las imagenes:

```sh
docker images

knative-heroes									latest              6beba03c0b73        9 minutes ago       103MB
```

Vemos que figura la imagen que acabamos de crear, `knative-heroes `. Vamos a añadirle el tag de nuestro repositorio en Azure:

```sh
docker tag knative-heroes:latest pruebacontenedor.azurecr.io/knative-heroes:latest
```

Ahora volvemos a mirar las imagenes:

```sh
docker images

knative-heroes									latest              6beba03c0b73        9 minutes ago       103MB
pruebacontenedor.azurecr.io/knative-heroes      latest              6beba03c0b73        9 minutes ago       103MB
```

Notese que aparece nuestra imagen con el tag, sigue figurando la imagen sin el tag, pero ambas tienen el __mismo hash__.

Publicamos nuestro repositorio en el repositorio de Azure

```sh
docker push pruebacontenedor.azurecr.io/knative-heroes

The push refers to repository [pruebacontenedor.azurecr.io/knative-heroes]
0bb60cb7757e: Pushing [==>                                                ]  786.9kB/18.24MB
edd61588d126: Pushing [=>                                                 ]  2.148MB/79.39MB
9b9b7f3d56a0: Pushed                                                                                                                   
f1b5933fe4b5: Pushing [========>                                          ]  994.8kB/5.533MB
```