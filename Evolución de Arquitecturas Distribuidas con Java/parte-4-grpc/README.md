# Parte IV - Comunicacion moderna con gRPC

## Diagrama

```text
appointment.proto
      |
      | genera clases Java
      v
Servidor gRPC <---- canal gRPC ----> Cliente gRPC
```

## Ejemplo guiado: MovieService con gRPC

Ubicacion: `movie-grpc`.

Generar clases y compilar:

```bash
mvn clean compile
```

Ejecutar servidor:

```bash
mvn exec:java -Dexec.mainClass="edu.eci.arsw.movie.MovieGrpcServer"
```

Ejecutar cliente:

```bash
mvn exec:java -Dexec.mainClass="edu.eci.arsw.movie.MovieGrpcClient"
```

Evidencia esperada:

```text
Pelicula: Interstellar - Christopher Nolan - 2014
```

## Ejercicio aplicado 4: Sistema de Bienestar Universitario con gRPC

Ubicacion: `wellness-grpc`.

Contrato implementado:

```proto
service AppointmentService {
  rpc RequestAppointment (AppointmentRequest) returns (AppointmentResponse);
  rpc CancelAppointment (CancelRequest) returns (CancelResponse);
  rpc GetAppointments (StudentRequest) returns (AppointmentList);
}
```

Entidades implementadas:

```text
Student: id, name, institutionalEmail
Appointment: id, studentId, serviceType, date, status
ServiceType: MEDICINE, PSYCHOLOGY, DENTISTRY
Status: REQUESTED, CANCELLED, ATTENDED
```

Reglas implementadas:

```text
Una cita solicitada queda en estado REQUESTED.
Una cita cancelada no aparece como cita activa.
Las citas se consultan por estudiante.
La informacion se mantiene en memoria.
```

Generar clases y compilar:

```bash
mvn clean compile
```

Ejecutar servidor:

```bash
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.AppointmentGrpcServer"
```

Ejecutar cliente:

```bash
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.AppointmentGrpcClient"
```

Evidencia esperada:

```text
Solicitud: Cita solicitada APT-1 REQUESTED
Citas activas: 1
Cancelacion: Cita cancelada
Citas activas despues de cancelar: 0
```

## Preguntas de reflexion

**Por que el archivo .proto se considera un contrato?**

Porque define formalmente servicios, operaciones, parametros, tipos de datos y respuestas. A partir de
ese archivo se genera codigo para cliente y servidor, asi que ambos lados dependen del mismo acuerdo.

**Que tan facil seria crear un cliente en otro lenguaje?**

Es mucho mas facil que con RMI. Mientras el otro lenguaje tenga soporte de gRPC y Protocol Buffers,
puede generar sus propios stubs desde el mismo `.proto`.

**Que diferencias encuentra entre RMI y gRPC?**

RMI usa interfaces remotas Java y queda ligado a ese ecosistema. gRPC usa un contrato `.proto`,
mensajes tipados y puede interoperar con varios lenguajes, lo que lo hace mas adecuado para servicios
modernos distribuidos.
