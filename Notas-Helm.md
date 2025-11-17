# Notas sobre la integración con Helm

## Objetivo
Centralizar la configuración necesaria para empaquetar y desplegar los microservicios mediante Helm, reutilizando los artefactos que ya genera Maven (Docker images versionadas).

## Estructura de carpetas
- `k8s/charts/<servicio>/Chart.yaml`: metadatos del chart (nombre, versión del chart, `appVersion` opcional).
- `k8s/charts/<servicio>/values.yaml`: valores por defecto (repositorio y tag de la imagen, replica count, env vars, etc.).
- `k8s/charts/<servicio>/templates/*.yaml`: manifiestos Helm (Deployment, Service, ConfigMap, etc.).

Se recomienda crear un chart por servicio (`discovery-service` primero) y replicar el patrón para los demás.

## Definición del chart base (ejemplo `discovery-service`)

`k8s/charts/discovery-service/Chart.yaml`
```yaml
apiVersion: v2
name: discovery-service
description: Helm chart para el servicio de descubrimiento
type: application
version: 0.1.0       # versión del chart
appVersion: "v0.0.1" # puede sincronizarse con Maven o dejarse como referencia
```

`k8s/charts/discovery-service/values.yaml`
```yaml
image:
  repository: javixulobo/videoclub-discovery-service-dev
  tag: v0.0.1            # se sobreescribe al desplegar
  pullPolicy: IfNotPresent

replicaCount: 2

env:
  - name: EUREKA_INSTANCE_HOSTNAME
    value: discovery-service-dev
```

`k8s/charts/discovery-service/templates/deployment.yaml`
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ include "discovery-service.fullname" . }}
spec:
  replicas: {{ .Values.replicaCount }}
  selector:
    matchLabels:
      app.kubernetes.io/name: {{ include "discovery-service.name" . }}
  template:
    metadata:
      labels:
        app.kubernetes.io/name: {{ include "discovery-service.name" . }}
    spec:
      containers:
        - name: discovery-service
          image: "{{ .Values.image.repository }}:{{ .Values.image.tag }}"
          imagePullPolicy: {{ .Values.image.pullPolicy }}
          env:
            {{- range .Values.env }}
            - name: {{ .name }}
              value: {{ .value | quote }}
            {{- end }}
```

## Flujo recomendado (build + deploy)
1. **Build Maven (ya existente):**
   ```bash
   mvn -pl discovery-service clean package -Pdev -DskipTests
   ```
   El plugin `spring-boot-maven-plugin` genera la imagen `javixulobo/videoclub-discovery-service-dev:${project.version}`.

2. **Carga automática de la imagen en Minikube (si no usas Docker Hub):**
   - El `pom` del `discovery-service` ejecuta `minikube image load javixulobo/videoclub-discovery-service-dev:${project.version}` durante la fase `deploy` cuando el perfil `dev` está activo, por lo que no necesitas preparar el daemon manualmente.
   - Para entornos distintos (o servicios adicionales) replica el mismo patrón cambiando el repositorio/tag.

3. **Despliegue con Helm:**
   ```bash
   helm upgrade --install discovery-service \
     k8s/charts/discovery-service \
     --namespace videoclub-dev \
     --create-namespace \
     --set image.tag=${project.version}
   ```
   - `--set image.tag=${project.version}` asegura que el chart use la misma versión que Maven acaba de empaquetar.
   - Para varios servicios, replicar la llamada por cada chart (o usar un script que itere).

3. **Automatización opcional en Maven:**
   - Añadir un `exec-maven-plugin` en el perfil `dev` que ejecute el comando Helm tras el empaquetado.
   - Ejemplo (fraccionado):
     ```xml
     <plugin>
       <groupId>org.codehaus.mojo</groupId>
       <artifactId>exec-maven-plugin</artifactId>
       <version>3.4.0</version>
       <executions>
         <execution>
           <id>helm-deploy</id>
           <phase>deploy</phase>
           <goals><goal>exec</goal></goals>
           <configuration>
             <executable>helm</executable>
             <arguments>
               <argument>upgrade</argument>
               <argument>--install</argument>
               <argument>discovery-service</argument>
               <argument>k8s/charts/discovery-service</argument>
               <argument>--namespace</argument>
               <argument>videoclub-dev</argument>
               <argument>--set</argument>
               <argument>image.tag=${project.version}</argument>
             </arguments>
           </configuration>
         </execution>
       </executions>
     </plugin>
     ```
   - Ajustar `namespace`, `profile` y/o parámetros según el entorno (`dev`, `int`, `pro`).

## Buenas prácticas
- Mantener `values.yaml` con valores por defecto estables y sobreescribirlos vía `--values` o `--set` para cada entorno.
- Versionar el chart (`Chart.yaml:version`) y el aplicativo (`appVersion`) para seguir cambios y rollbacks.
- Usar un repositorio de charts si se requiere distribución (por ahora basta con mantenerlos en `k8s/charts/`).
- Documentar en este archivo cualquier variable necesaria por servicio (puertos, recursos mínimos, secretos, etc.) conforme se Helm-icen los demás microservicios.

## Próximos pasos sugeridos
1. Crear la carpeta `k8s/charts/discovery-service` con los archivos anteriores.
2. Probar `helm template` localmente para validar que la salida coincide con lo que se despliega hoy vía Kustomize.
3. Integrar los comandos Helm en el pipeline o en el perfil Maven correspondiente.
4. Garantizar que cada servicio cargue su imagen en Minikube (o en el registro objetivo) como parte del `mvn deploy`.
5. Replicar el chart para los otros microservicios cuando el flujo esté validado.


kubectl delete deployment rental-service-dev -n videoclub-dev 2>/dev/null
kubectl delete deployment rental-service -n videoclub-dev 2>/dev/null
kubectl delete service rental-service-dev -n videoclub-dev 2>/dev/null
kubectl delete service rental-service -n videoclub-dev 2>/dev/null
kubectl delete serviceaccount rental-service-sa-dev -n videoclub-dev 2>/dev/null
kubectl delete serviceaccount rental-service-sa -n videoclub-dev 2>/dev/null
kubectl delete hpa rental-hpa-dev -n videoclub-dev 2>/dev/null  # si existía un HPA antiguo