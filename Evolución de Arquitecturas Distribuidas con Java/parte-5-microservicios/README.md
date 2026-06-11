# Parte V - Arquitectura de Microservicios

## Diagrama de microservicios

```text
Cliente directo
   |---- localhost:51051 -> AppointmentService
   |---- localhost:51052 -> MedicalService
   |---- localhost:51053 -> GymService
   |---- localhost:51054 -> RecreationService
```

## Descomposicion de Bienestar Universitario

Ubicacion: `wellness-microservices`.

| Servicio | Puerto | Responsabilidad |
| --- | --- | --- |
| AppointmentService | 51051 | Gestionar citas y turnos de atencion. |
| MedicalService | 51052 | Gestionar informacion basica de especialidades medicas disponibles. |
| GymService | 51053 | Gestionar reservas simples de sesiones de gimnasio. |
| RecreationService | 51054 | Gestionar reserva de recursos recreativos. |

Contratos:

```text
src/main/proto/common.proto
src/main/proto/appointment.proto
src/main/proto/medical.proto
src/main/proto/gym.proto
src/main/proto/recreation.proto
```

## Ejecucion

Generar clases y compilar:

```bash
mvn clean compile
```

Ejecutar servicios en terminales distintas:

```bash
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.ms.AppointmentServer"
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.ms.MedicalServer"
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.ms.GymServer"
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.ms.RecreationServer"
```

Ejecutar cliente directo:

```bash
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.ms.WellnessDirectClient"
```

Evidencia esperada:

```text
AppointmentService: Cita solicitada
MedicalService especialidades: 3
GymService: Sesion de gimnasio reservada
RecreationService: Recurso recreativo reservado
```

## Preguntas de reflexion

**Por que decidio separar esos servicios y no otros?**

Se separaron porque representan capacidades distintas de bienestar: citas, informacion medica,
gimnasio y recreacion. Cada servicio puede cambiar sus reglas internas sin obligar a modificar todo el
sistema.

**Que datos pertenecen a cada servicio?**

`AppointmentService` conserva citas por estudiante. `MedicalService` conserva especialidades y lugares
de atencion. `GymService` conserva reservas de sesiones de gimnasio. `RecreationService` conserva
recursos recreativos y sus reservas.

**Que riesgo aparece cuando el cliente conoce todos los servicios?**

El cliente queda acoplado a todos los puertos, contratos y direcciones. Si cambia un servicio, el cliente
puede necesitar cambios aunque su caso de uso sea simple.
