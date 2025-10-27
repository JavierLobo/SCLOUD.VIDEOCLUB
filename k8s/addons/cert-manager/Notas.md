# 🧩 Cert-Manager Add-on (v1.14.1)

Este directorio contiene los manifiestos necesarios para instalar **cert-manager** en un clúster Kubernetes.

## 📦 Archivos incluidos

| Archivo | Descripción |
|----------|--------------|
| `cert-manager.yaml` | Manifiesto oficial descargado desde el repositorio de Jetstack. |
| `kustomization.yaml` | Permite aplicar la instalación con `kubectl apply -k`. |

---
## Crear un nuevo contexto
### 🧱 1️⃣ Ver tus contextos actuales
   ```bash
   kubectl config get-contexts
   ```

### 🧩 2️⃣ Crear un contexto nuevo para Videoclub

Supongamos que tu clúster se llama videoclub-cluster y el usuario admin-videoclub.
Puedes crear el contexto así:

   ```bash
   kubectl config set-context videoclub \
   --cluster=videoclub-cluster \
   --user=admin-videoclub \
   --namespace=videoclub
   ```
Esto crea un nuevo contexto llamado videoclub con:
   * el clúster videoclub-cluster,
   * el usuario admin-videoclub,
   * el namespace por defecto videoclub.

### 🔄 3️⃣ Cambiar de contexto

   ```bash
   kubectl config use-context videoclub
   ```
Y verificar:
   ```bash
   kubectl config current-context
   ```
Debe mostrar:

   ```bash
   videoclub
   ```


### Descarga los manifests exactos que instalaste
   ```bash
   curl -L -o cert-manager.yaml https://github.com/cert-manager/cert-manager/releases/download/v1.14.1/cert-manager.yaml
   ```

### ✅ 4️⃣ Verificación final

Asegúrate de que el namespace y los pods son los correctos:
   ```bash
   kubectl get ns
   kubectl get pods -n videoclub
   ```
Y que los recursos que desplegaste (como el Ingress, auth-service, etc.) aparecen solo en ese namespace.

### 🧠 Recomendación extra

Si quieres evitar errores al aplicar manifests, puedes definir este contexto por defecto para el namespace “videoclub”:
   ```bash
   kubectl config set-context --current --namespace=videoclub
   ```

Así no tendrás que escribir -n videoclub en cada comando.


## ⚙️ Instalación
### Opcion A
Kubernetes descargará e instalará todos los recursos en el namespace cert-manager automáticamente

   ```bash
   kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.14.1/cert-manager.yaml
   ```
   Ejemplo de salida esperada:

    $ kubectl get pods -n cert-manager
   ```bash
   NAME                                       READY   STATUS    RESTARTS   AGE
   cert-manager-7c9b77d94b-mttfx             1/1     Running   0          2m
   cert-manager-cainjector-5f5db97dbd-9nbx9  1/1     Running   0          2m
   cert-manager-webhook-76b58cb7c7-5vjxw     1/1     Running   0          2m
   ```

### Opcion B
1. Verifica que tu `kubectl` está apuntando al clúster correcto:
   ```bash
   kubectl config current-context
   ```

2. Instala **cert-manager** o para reinstalarlo en otro clúster (crear namespace, CRDs y componentes):
   ```bash
   kubectl apply -k k8s/addons/cert-manager
   ```

3. Espera a que los pods estén listos:
   ```bash
   kubectl get pods -n cert-manager
   ```

   Ejemplo de salida esperada:
   ```
   NAME                                       READY   STATUS    RESTARTS   AGE
   cert-manager-7f7c9b5cc6-hxwmk             1/1     Running   0          2m
   cert-manager-cainjector-654ff7bd89-2mnl4  1/1     Running   0          2m
   cert-manager-webhook-7b7cdd8756-n8czd     1/1     Running   0          2m
   ```

4. Aplica el **ClusterIssuer** de tu proyecto Videoclub:
   ```bash
   kubectl apply -f k8s/base/clusterissuer.yaml
   ```

5. Verifica que el issuer está activo:
   ```bash
   kubectl get clusterissuers
   kubectl describe clusterissuer letsencrypt-prod
   ```

---

## 🔐 Notas de seguridad

- **No guardes los certificados TLS reales** en el repositorio (`videoclub-tls-cert`, `letsencrypt-prod-private-key`, etc.).
- Estos certificados se almacenan como `Secrets` dentro del clúster y son regenerados automáticamente por cert-manager.

Para verlos:
```bash
kubectl get secrets -n videoclub | grep tls
```

---

## 🧪 Prueba de funcionamiento

Después de desplegar tu Ingress con TLS:

```bash
kubectl get certificates -A
```

Deberías ver un certificado emitido:

```
NAMESPACE   NAME                  READY   SECRET                 AGE
videoclub   videoclub-tls-cert    True    videoclub-tls-cert     1m
```

Y el sitio debería ser accesible vía HTTPS ✅

---

## 🧱 Fuente oficial

https://github.com/cert-manager/cert-manager/releases/tag/v1.14.1
