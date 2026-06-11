# Taller Integrador ARSW 2026-I

Evolucion de Arquitecturas Distribuidas con Java.

Este repositorio contiene las partes solicitadas en el taller:

| Parte | Carpeta | Producto |
| --- | --- | --- |
| I | `parte-1-sockets` | Sockets TCP con MovieServer y gestion de salones. |
| II | `parte-2-http` | HTTP basico con MovieHttpServer y salones via HTTP. |
| III | `parte-3-rmi` | RMI con MovieService e inventario de laboratorios. |
| IV | `parte-4-grpc` | gRPC con MovieService y citas de bienestar. |
| V | `parte-5-microservicios` | Descomposicion inicial de bienestar universitario. |
| VI | `parte-6-api-gateway` | WellnessGateway para centralizar acceso a servicios. |
| Final | `ejercicio-final-eciciencia` | Diseno arquitectonico de la plataforma ECICIENCIA. |

Cada parte tiene su propio `README.md` con:

```text
diagrama
comandos de ejecucion
evidencia esperada
preguntas de reflexion respondidas
```

## Herramientas usadas

```text
Java 21
Maven 3.9.12
```

## Verificacion general

Para las partes Java simples, compile dentro de cada carpeta:

```bash
javac *.java
```

Para los proyectos Maven gRPC, compile dentro del proyecto correspondiente:

```bash
mvn clean compile
```

Nota en Windows: si `protoc` falla mostrando la ruta como `Evoluci?n`, copie el proyecto Maven a una
ruta sin acentos y ejecute alli `mvn clean compile`. El codigo fue verificado de esa forma porque la ruta
local del taller contiene `Evolucion` con tilde.
