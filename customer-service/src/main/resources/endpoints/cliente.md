
## CREAR UN CLIENTE
curl -X POST "http://localhost:8082/api/v1/cliente" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "name": "Javier",
    "lastName": "Lobo",
    "dni": "12345678A",
    "email": "javier.lobo@example.com",
    "phone": "600321321",
    "registrationDate": "2025-11-19",
    "active": true
  }'

## ACTUALIZAR UN CLIENTE
Para tu endpoint actual (GET /api/v1/cliente/{clientId}) puedes probar así:

curl -X GET "http://localhost:8082/api/v1/cliente/1" \
  -H "Accept: application/json"
  
Si quieres parametrizar el id:

CLIENT_ID=1
curl -X GET "http://localhost:8082/api/v1/cliente/$CLIENT_ID" \
  -H "Accept: application/json"

## ELIMINAR UN CLIENTE
curl -X DELETE http://localhost:8080/api/v1/cliente/123
