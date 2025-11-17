# En este archivo hay que añadir todas las notas para el uso del proyecto. Instrucciones para compilar y ejecutar el proyecto en java, generacion de imagenes y utilizacion de kubernetes como levantar el entorno o visualizar los logs. Todas esas instrucciones van copiadas aqui junto a una pequeña descripcion.

## Arrancando el entorno k8s

    $ minikube profile list
    $ minikube stop && minikube start --cpus=3 --memory=6144


## Buscando informacion 

    $ kubectl logs deployment/rental-service-dev -n videoclub-dev --tail=50

    $ kubectl describe pod <pod-del-servicio> -n videoclub-dev

# Eliminar los pods 

    $ kubectl get all -n videoclub-dev | grep inventory
    
    $ kubectl delete deployment inventory-service-dev -n videoclub-dev 2>/dev/null
    $ kubectl delete deployment inventory-service -n videoclub-dev 2>/dev/null
    $ kubectl delete service inventory-service-dev -n videoclub-dev 2>/dev/null
    $ kubectl delete service inventory-service -n videoclub-dev 2>/dev/null
    $ kubectl delete serviceaccount inventory-service-sa-dev -n videoclub-dev 2>/dev/null
    $ kubectl delete serviceaccount inventory-service-sa -n videoclub-dev 2>/dev/null
    $ kubectl delete hpa inventory-hpa-dev -n videoclub-dev 2>/dev/null

# Verificando el correcto funcionamiento de los pods

  $ kubectl port-forward svc/catalog-service-dev 8080:80 -n videoclub-dev
  $ kubectl port-forward svc/rental-service-dev 8084:8084 -n videoclub-dev

    $ kubectl run curl-tmp -n videoclub-dev --rm -it --restart=Never   --image=curlimages/curl --   curl -sf http://catalog-service-dev:8083/actuator/health

    $ kubectl exec deploy/rental-service-dev -n videoclub-dev --   curl -s http://discovery-service-dev:8761/eureka/apps/rental-service