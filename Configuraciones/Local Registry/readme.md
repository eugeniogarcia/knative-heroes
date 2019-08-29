# Registry Local básico
El registry esta implementado con un [contenedor](https://hub.docker.com/_/registry). Podemos lanzar el registry con una configuracion por defecto:  
```
docker run -d -p 5000:5000 --restart=always --name registry registry:2

docker run -d -p 5000:5000 --restart=always --name registry registry:latest
```

Arranca un registry local. Podemos acceder a el en `http://localhost:5000/v2/_catalog`. Este registry esta expuesto en el puerto 5000, no tiene autentificacion, ni esta protegido con TLS.  

# Configuracion avanzada del Local Registry
Podemos ver la informacion detallada [aqui](https://docs.docker.com/registry/deploying/). Vamos a habilitar basic authentication, y TLS.  

## Basic Authentication
El usuario y password de cada una de las credenciales se guardara en un archivo. Utilizaremos htpasswd para generar el archivo. Hay una copia de htpasswd en la propia imagen del registry, asi que podemos generar el archivo de credenciales como sigue:

```
docker run --rm --entrypoint htpasswd registry:2 -Bbn egsmartin "Vera1511" > htpasswd

```
Esto crea el archivo `htpasswd` con las credenciales, incluyendo la contraseña encriptada.    

__NOTA__  
En el procedimiento anterior, en windows, hay que quitar los retornos de carro. 

### Arrancar el registry con basic authentication
Para arrancar el registry con basic authentication:  
```
docker run -it -p 5000:5000 --restart=always --name registry -v C:/Users/Eugenio/Documents/imagenes/auth:/auth -e "REGISTRY_AUTH=htpasswd" -e "REGISTRY_AUTH_HTPASSWD_REALM=Registry Realm" -e REGISTRY_AUTH_HTPASSWD_PATH=/auth/htpasswd registry:2
```

Aqui hemos especificado con la variable de entorno `REGISTRY_AUTH` que queremos usar htpasswd para guardar las credenciales. Con `-v C:/Users/Eugenio/Documents/imagenes/auth:/auth` estamos montando un volumen de nuestro pc, donde el contenedor encontrara el archivo con las credenciales de basic authentication. Con la variable de entorno `REGISTRY_AUTH_HTPASSWD_PATH` indicamos la ruta del archivo de credenciales.  

## Habilitar TLS
Vamos mapear un volumen al contenedor `-v C:/Users/Eugenio/Documents/certs:/certs`. En este volumen depositaremos la clave privada y la clave publica del certficado a utilizar. En mi caso `euge.key` y `euge.pem`.  

Las variables de entorno `REGISTRY_HTTP_TLS_CERTIFICATE` y `REGISTRY_HTTP_TLS_KEY` indican cual es la clave publica y privada que se tienen que utilizar.  

```
docker run -d -p 5000:5000 --restart=always --name registry -v C:/Users/Eugenio/Documents/imagenes/auth:/auth -e "REGISTRY_AUTH=htpasswd" -e "REGISTRY_AUTH_HTPASSWD_REALM=Registry Realm" -e REGISTRY_AUTH_HTPASSWD_PATH=/auth/htpasswd -v C:/Users/Eugenio/Documents/certs:/certs -e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/euge.pem -e REGISTRY_HTTP_TLS_KEY=/certs/euge.key registry:2
```

Con esta configuracion en `https://www.gz.com:5000/v2/_catalog` se mostrara el registry.  

### Generar los certificados
Los certificados los podemos generar usando openssl tal y como se indica [aqui](../openssl/README.md).  

En la documentacion se hace referencia a un archivo `.crt`. Este archivo guarda la clave publica. En mi caso no funcionaba, asi que he utilizado el `.pem` - que tambien tiene la clave publica, pero en otro formato.

# Configuracion avanzada con docker-compose
Con docker-compose podemos lograr la misma configuracion:  
```
registry:
  restart: always
  image: registry:2
  hostname: www.gz.com
  ports:
    - 5000:5000
  environment:
    REGISTRY_HTTP_TLS_CERTIFICATE: /certs/euge.pem
    REGISTRY_HTTP_TLS_KEY: /certs/euge.key
    REGISTRY_AUTH: htpasswd
    REGISTRY_AUTH_HTPASSWD_PATH: /auth/htpasswd
    REGISTRY_AUTH_HTPASSWD_REALM: Registry Realm
  volumes:
    - C:/Users/Eugenio/Documents/imagenes:/var/lib/registry
    - C:/Users/Eugenio/Documents/certs:/certs
    - C:/Users/Eugenio/Documents/imagenes/auth:/auth
```

Ahora podemos hacer:  
```
docker-compose up -d
```
En el siguiente archivo de configuracion vamos a configurar no el puerto 5000, pero el 443. Notese que con `REGISTRY_HTTP_ADDR: 0.0.0.0:443` le estamos diciendo al registry que queremos que se exponga en el puerto 443, no en el 5000. Con `443:443` estamos exponiendo el puerto 443 con el 443.  

```
registry:
  restart: always
  image: registry:2
  hostname: www.gz.com
  ports:
    - 443:443
  environment:
    REGISTRY_HTTP_ADDR: 0.0.0.0:443
    REGISTRY_HTTP_TLS_CERTIFICATE: /certs/euge.pem
    REGISTRY_HTTP_TLS_KEY: /certs/euge.key
    REGISTRY_AUTH: htpasswd
    REGISTRY_AUTH_HTPASSWD_PATH: /auth/htpasswd
    REGISTRY_AUTH_HTPASSWD_REALM: Registry Realm
  volumes:
    - C:/Users/Eugenio/Documents/imagenes:/var/lib/registry
    - C:/Users/Eugenio/Documents/certs:/certs
    - C:/Users/Eugenio/Documents/imagenes/auth:/auth
```
# Configurar Minikube para usar el Local Registry
## Actualizar el DNS
Necesitamos crear una entrada en el /etc/hosts - DNS - del nodo donde estamos ejecutando Minikube. Primero averiguamos la IP del Local registry, o lo que es lo mismo, la IP del PC.  

Averiguamos la IP que tiene asignada el nodo de Minikube. `minikube ip` devuelve `192.168.0.130`. Si hacemos `minikube ssh` y a continuacion un `ìfconfig`, veremos que `eth0` es efectivamente `192.168.0.130`.  

Si en el pc miramos las distintas conexiones, veremos que la conexion que usa el hyperv para Minikube es `192.168.0.127`. Esta ip y la del node estan en la misma subred y se ven. Si hacemos 

```
minikube ssh

sudo -s

echo "192.168.0.127 www.gz.com" >> /etc/hosts

```
## Configurar el TLS

## Probar