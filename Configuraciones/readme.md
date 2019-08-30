# Utilidades
## Conectarse a un contenedor 
Connecting to the heroes container:  

```
kubectl exec -it heroes-69464bcd77-8bmwg --container heroes -- /bin/bash
```

Connecting to the feheroes container:  

```
kubectl exec -it feheroes-844494cc46-j9m28 --container feheroes -- /bin/bash
```

Once we are in the container with bash, we can see the environment variables that are available:  

```
env
```
We can do curl:  
```
curl http://localhost:8082/heroes

curl http://heroes-svc/heroes

```

## Deployment status
```
kubectl get deployments
```

Actualizamos la imagen de uno de los contenedores del deployment:  
```
$ kubectl --record deployment.apps/feheroes set image deployment.v1.apps/feheroes feheroes=www.gz.com:5000/fehello-jib:0.0.1-SNAPSHOT
deployment.apps/feheroes image updated

$kubectl rollout status deployment.v1.apps/feheroes
deployment "feheroes" successfully rolled out
```