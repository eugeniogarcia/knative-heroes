# Registry Local básico
```
docker run -d -p 5000:5000 --restart=always --name registry registry:2

docker run -d -p 5000:5000 --restart=always --name registry registry:latest
```

Arranca un registry local. Podemos acceder a el en `http://localhost:5000/v2/_catalog`

# Configurar Minikube para usar el Local Registry
Necesitamos crear una entrada en el /etc/hosts - DNS - del nodo donde estamos ejecutando Minikube. Primero averiguamos la IP del Local registry, o lo que es lo mismo, la IP del PC.  

Averiguamos la IP que tiene asignada el nodo de Minikube. `minikube ip` devuelve `192.168.0.130`. Si hacemos `minikube ssh` y a continuacion un `ìfconfig`, veremos que `eth0` es efectivamente `192.168.0.130`.  

Si en el pc miramos las distintas conexiones, veremos que la conexion que usa el hyperv para Minikube es `192.168.0.127`. Esta ip y la del node estan en la misma subred y se ven. Si hacemos 

```
minikube ssh

sudo -s

echo "192.168.0.127 www.gz.com" >> /etc/hosts

```
# Configuracion avanzada del Local REgistry
![]()