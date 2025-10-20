#!/bin/bash
# ============================================
# 📦 Script: build_and_push.sh
# 🚀 Descripción: Construye y sube imágenes Docker a Docker Hub
# ============================================

# --- Configuración ---
DOCKER_USER="javixulobo"     # 👈 Cambia por tu usuario DockerHub
TAG="v0.0.0"
DOCKERFILE="docker/dockerfile.empty"

# Lista de servicios a construir
SERVICIOS=(
  "config-service"
  "auth-service"
  "catalog-service"
  "customer-service"
  "discovery-service"
  "gateway-service"
  "inventory-service"
  "notification-service"
  "payment-service"
  "rental-service"
)

# --- Login a Docker Hub ---
echo "🔐 Iniciando sesión en Docker Hub..."
docker login -u "$DOCKER_USER"
if [ $? -ne 0 ]; then
  echo "❌ Error: No se pudo iniciar sesión en Docker Hub."
  exit 1
fi

# --- Construcción y subida ---
for SERVICE in "${SERVICIOS[@]}"; do
  IMAGE_NAME="${DOCKER_USER}/videoclub-${SERVICE}-dev:${TAG}" # ej: docker push javixulobo/videoclub-config-service-dev:v0.0.0
 
  echo "============================================"
  echo "🛠️  Construyendo imagen: $IMAGE_NAME"
  echo "============================================"

  docker build -t "$IMAGE_NAME" -f "$DOCKERFILE" .
  if [ $? -ne 0 ]; then
    echo "❌ Error al construir $SERVICE"
    continue
  fi

  echo "✅ Imagen construida correctamente: $IMAGE_NAME"

  echo "⬆️  Subiendo imagen a Docker Hub..."
  docker push "$IMAGE_NAME"
  if [ $? -ne 0 ]; then
    echo "❌ Error al subir $SERVICE"
    continue
  fi

  echo "🚀 Imagen subida correctamente: $IMAGE_NAME"
done

echo "============================================"
echo "🎉 Todas las imágenes procesadas."
echo "============================================"
