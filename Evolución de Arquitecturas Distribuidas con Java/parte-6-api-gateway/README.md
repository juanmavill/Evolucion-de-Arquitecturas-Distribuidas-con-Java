# Parte VI - API Gateway

## Diagrama

```text
Cliente
   |
   v
WellnessGateway
   |---- AppointmentService  localhost:51051
   |---- MedicalService      localhost:51052
   |---- GymService          localhost:51053
   |---- RecreationService   localhost:51054
```

## WellnessGateway

Ubicacion: `wellness-gateway`.

El Gateway centraliza el acceso a los servicios internos del sistema de bienestar universitario.
El cliente usa una sola clase de entrada y no necesita conocer los puertos de cada microservicio.

Operaciones minimas implementadas:

```java
requestAppointment(studentId, serviceType)
getStudentWellnessSummary(studentId)
reserveGymSession(studentId, timeSlot)
reserveRecreationResource(studentId, resourceId)
```

## Ejecucion

Primero ejecute los servicios de la Parte V:

```bash
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.ms.AppointmentServer"
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.ms.MedicalServer"
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.ms.GymServer"
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.ms.RecreationServer"
```

Luego compile y ejecute el Gateway:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.gateway.WellnessGateway"
```

Evidencia esperada:

```text
requestAppointment: Cita solicitada
reserveGymSession: Sesion de gimnasio reservada
reserveRecreationResource: Recurso recreativo reservado
Resumen de bienestar para 20261001
Citas activas: 1
Reservas de gimnasio: 1
Reservas recreativas: 1
Especialidades disponibles: 3
```

## Preguntas de reflexion

**Que simplifica el Gateway para el cliente?**

El cliente deja de conocer cada puerto y cada servicio interno. Solo usa el Gateway para ejecutar casos
de uso de bienestar y recibir respuestas integradas.

**Que complejidad agrega al sistema?**

Agrega un componente que debe conocer los servicios internos, manejar fallos de comunicacion y
coordinar respuestas. Tambien requiere mantener los contratos que usa para conectarse a esos servicios.

**Que pasaria si el Gateway empieza a contener demasiada logica de negocio?**

Se convertiria en un punto central dificil de cambiar y probar. La logica principal debe permanecer en los
servicios dueños de los datos; el Gateway debe coordinar y simplificar el acceso.
